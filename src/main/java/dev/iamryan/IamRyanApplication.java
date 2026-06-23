package dev.iamryan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableRetry
@EnableScheduling
@EnableFeignClients
@EnableTransactionManagement
public class IamRyanApplication {

    public static void main(String[] args) {
        SpringApplication.run(IamRyanApplication.class, args);
    }

}
 