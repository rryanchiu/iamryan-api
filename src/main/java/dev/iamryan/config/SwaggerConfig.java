package me.rryan.tinyurl.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info().title("TinyUrl API").description("").version("1.0.0"));
//                .contact(new Contact("Ryan", "http://rryan.me", "xiaoliang.q@outlook.com"))
    }
}
