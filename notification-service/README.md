# Notification Service

Notification Service обрабатывает уведомления о заказах и отправляет их пользователям.

## Основные Возможности
- Прослушивание сообщений из Kafka
- Отправка уведомлений пользователям

## Технологический Стек
- **Spring Boot**
- **Spring Kafka**
- **Jakarta Mail**
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
    cd task-management-system/notification-service
    ```

2. Установите зависимости и соберите проект:
    ```bash
    mvn clean install
    ```

3. Запустите приложение:
    ```bash
    mvn spring-boot:run
    ```
