package com.wayz.service.integration.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

/**
 * Абстрактнгый метод интеграции с другими сервисами
 * Использует дженерики и параметризованные типы данных для точного определения типов
 *
 * @param <T> дженерики
 */
public abstract class AbstractIntegrationService<T> {

    protected final RestTemplate restTemplate;

    protected AbstractIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Отправка запроса (интеграция с другим сервисом) и получение ответа (дженерик)
     * Поддерживает как GET, так и POST запросы
     *
     * @param url          сфорированный адрес до конкретного эндпоинта
     * @param token        токен авторизации
     * @param responseType тип ответа (класс) использующий типобезопасность
     * @param httpMethod   тип запроса
     * @param requestBody  null при GET запросе и object при POST
     * @return ответ запроса через доп проверку
     */
    protected T sendRequest(String url, String token, ParameterizedTypeReference<T> responseType, HttpMethod httpMethod, Object requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, token);
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, requestEntity, responseType);
            return handleResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during request to " + url, e);
        }
    }

    /**
     * Доп. обработчик на success
     *
     * @param response ответ запроса
     * @return объект ответа
     */
    private T handleResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get a successful response. Status code: " + response.getStatusCode());
        }
    }

    /**
     * Билдер url до конечного эндпоинта
     *
     * @param baseUrl   базовый url сервиса
     * @param endpoint  конкретный адрес до эендпоинта
     * @param parameter параметр эндпоинта
     * @return готовый url
     */
    protected String constructUrl(String baseUrl, String endpoint, String parameter) {
        if (parameter == null) {
            return String.format("%s/%s", baseUrl, endpoint);
        }
        return String.format("%s/%s/%s", baseUrl, endpoint, parameter);
    }

}
