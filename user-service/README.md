# User Service

User Service управляет информацией о пользователях и предоставляет REST API для создания, обновления и получения информации о пользователях.

## Основные Возможности
- Создание пользователя
- Обновление информации о пользователе
- Получение информации о пользователе

## Технологический Стек
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
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
    cd task-management-system/user-service
    ```

2. Установите зависимости и соберите проект:
    ```bash
    mvn clean install
    ```

3. Запустите приложение:
    ```bash
    mvn spring-boot:run
    ```