package com.wayz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonMapperConfig {
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
