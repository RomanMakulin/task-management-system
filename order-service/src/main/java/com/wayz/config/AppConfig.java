package com.wayz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Общая конфигурация сервиса
 */
@Configuration
public class AppConfig {

    /**
     * Бин restTemplate для его создания в контексте
     *
     * @return новый RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Определяем objectMapper для корректной сериализации объектов
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Регистрация модуля для обработки Java 8 DateTime API
        mapper.registerModule(new JavaTimeModule());
        // Отключение сериализации дат как таймстампов
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }
}
