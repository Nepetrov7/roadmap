# FMS - Система управления дорожной картой для мигрантов

## Описание

Приложение для ФМС, которое помогает мигрантам получить персональную дорожную карту действий на основе их данных.

## Технологии

### Backend

-   Spring Boot 4.0
-   Spring Security с JWT
-   Spring Data JPA
-   H2 Database (in-memory)
-   Lombok
-   Java 21

## Запуск приложения

### Требования

**Backend:**

-   Java 21 или выше
-   Maven 3.6+

### Команды для запуска

**Backend:**

```bash
# Сборка проекта
mvn clean install

# Запуск приложения
mvn spring-boot:run
```

Backend будет доступен по адресу: `http://localhost:8080`

## API Документация

Подробная документация API находится в файле [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## База данных

Используется H2 in-memory база данных. Для доступа к консоли:

-   URL: `http://localhost:8080/h2-console`
-   JDBC URL: `jdbc:h2:mem:fmsdb`
-   Username: `sa`
-   Password: (пусто)
