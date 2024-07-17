package com.wayz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация приложения
 */
@Configuration
public class AppConfig {

    /**
     * Бин создания RestTemplate
     *
     * @return new restTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
