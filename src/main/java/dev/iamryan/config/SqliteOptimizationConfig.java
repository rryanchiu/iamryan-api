package dev.iamryan.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqliteOptimizationConfig {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void optimize() {
        try {
            jdbcTemplate.execute("PRAGMA journal_mode=WAL");
            jdbcTemplate.execute("PRAGMA synchronous=NORMAL");
            jdbcTemplate.execute("PRAGMA busy_timeout=5000");
            jdbcTemplate.execute("""
                    create index if not exists idx_radio_info_stations_list
                    on radio_info(click_count desc)
                    where geo_lat is not null and geo_long is not null
                    """);
            jdbcTemplate.execute("""
                    create index if not exists idx_radio_info_station_uuid_lookup
                    on radio_info(station_uuid)
                    """);
            jdbcTemplate.execute("""
                    create index if not exists idx_domain_like_domain_lookup
                    on domain_like(domain)
                    """);
            jdbcTemplate.execute("PRAGMA optimize");
        } catch (Exception e) {
            log.warn("SQLite optimization skipped", e);
        }
    }
}
