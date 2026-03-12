package dev.iamryan.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.iamryan.api.RadioBrowserInfoAPI;
import dev.iamryan.service.RadioInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RadioInfoFetchTask {

    private final RadioInfoService radioInfoService;
    private final RadioBrowserInfoAPI radioBrowserInfoAPI;

    private static final int PAGE_SIZE = 500;
    private static final int MAX_FETCH_LIMIT = 500_000;

    // 创建固定线程池（建议线程数为CPU核心数 * 2）
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Scheduled(initialDelay = 0, fixedRate = 6 * 60 * 60 * 1000)
    public void fetchRadioInfo() {
        int offset = 0;
        int totalFetched = 0;

        log.info("开始同步电台信息");

        List<Future<?>> futureList = new ArrayList<>();

        while (true) {
            try {
                String response = radioBrowserInfoAPI.getStations(offset, PAGE_SIZE, true, true, "clickcount", true);
                if (StringUtils.isBlank(response)) {
                    log.warn("获取电台信息失败，返回为空。offset={}", offset);
                    break;
                }

                JSONArray stationsArray = JSON.parseArray(response);
                int fetchedThisRound = stationsArray.size();

                if (fetchedThisRound == 0) {
                    log.info("已无更多电台信息可抓取，结束任务。");
                    break;
                }

                totalFetched += fetchedThisRound;
                offset += fetchedThisRound;

                // 将每条 station 提交线程池异步处理
                for (int i = 0; i < fetchedThisRound; i++) {
                    JSONObject station = stationsArray.getJSONObject(i);

                    Future<?> future = executor.submit(() -> {
                        try {
                            radioInfoService.saveOrUpdateStation(station);
                        } catch (Exception e) {
                            log.error("处理单个电台数据出错: {}", station.getString("name"), e);
                        }
                    });

                    futureList.add(future);
                }

                if (totalFetched >= MAX_FETCH_LIMIT) {
                    log.warn("已达到抓取上限 {} 条，停止继续抓取。", MAX_FETCH_LIMIT);
                    break;
                }

                if (fetchedThisRound < PAGE_SIZE) {
                    break;
                }

            } catch (Exception e) {
                log.error("抓取或处理电台数据时出错，offset={}，异常信息：", offset, e);
                break;
            }
        }

        // 等待所有任务完成
        for (Future<?> future : futureList) {
            try {
                future.get(); // 阻塞直到执行完成
            } catch (Exception e) {
                log.warn("等待任务执行完成时出错", e);
            }
        }

        log.info("同步电台信息任务完成，总共处理了 {} 条数据", totalFetched);
    }
}
