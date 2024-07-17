# Gateway Service

Gateway Service служит шлюзом для маршрутизации запросов к соответствующим микросервисам.

## Основные Возможности
- Маршрутизация запросов к микросервисам
- Аутентификация и авторизация с использованием JWT

## Технологический Стек
- **Spring Boot**
- **Spring Cloud Gateway**
- **Eureka**
- **Lombok**

## Установка и Запуск

### Требования
- Java 17
- Maven 3.8+

### Шаги Установки
1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/yourusername/task-management-system.git
    cd task-management-system/gateway-service
    ```

2. Установите зависимости и соберите проект:
    ```bash
    mvn clean install
    ```

3. Запустите приложение:
    ```bash
    mvn spring-boot:run
    ```