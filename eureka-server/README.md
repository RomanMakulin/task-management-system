# Eureka Service

Eureka Service служит центром обнаружения сервисов для микросервисной архитектуры проекта. Этот сервис позволяет автоматически обнаруживать микросервисы в сети, облегчая их взаимодействие и балансировку нагрузки.

## Основные Возможности
- Регистрация микросервисов
- Обнаружение микросервисов
- Поддержка взаимодействия между микросервисами

## Технологический Стек
- **Spring Boot**
- **Spring Cloud Netflix Eureka**
- **Java**

## Установка и Запуск

### Требования
- Java 17
- Maven 3.8+

### Шаги Установки
1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/yourusername/task-management-system.git
    cd task-management-system/eureka-server
    ```

2. Установите зависимости и соберите проект:
    ```bash
    mvn clean install
    ```

3. Запустите Eureka Server:
    ```bash
    mvn spring-boot:run
    ```

## Конфигурация

### application.yml
```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  server:
    enableSelfPreservation: false
    evictionIntervalTimerInMs: 4000

logging:
  level:
    com.netflix.eureka: OFF
    com.netflix.discovery: OFF
