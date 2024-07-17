# Order Service

Order Service управляет информацией о заказах и предоставляет REST API для создания, обновления и получения информации о заказах.

## Основные Возможности
- Создание заказа
- Обновление информации о заказе
- Получение информации о заказе

## Технологический Стек
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Kafka**
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
    cd task-management-system/order-service
    ```

2. Установите зависимости и соберите проект:
    ```bash
    mvn clean install
    ```

3. Запустите приложение:
    ```bash
    mvn spring-boot:run
    ```

