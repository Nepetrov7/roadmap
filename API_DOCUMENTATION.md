# FMS API Документация

## Описание

API для управления профилем пользователя и аутентификации мигрантов в ФМС.

## Базовый URL

```
http://localhost:8080
```

## Аутентификация

Все запросы (кроме `/api/auth/**`) требуют JWT токен в заголовке:

```
Authorization: Bearer <token>
```

## Эндпоинты

### 1. Регистрация

**POST** `/api/auth/register`

**Тело запроса:**

```json
{
    "firstName": "Иван",
    "lastName": "Иванов",
    "middleName": "Иванович",
    "username": "ivanov",
    "password": "password123"
}
```

**Ответ:**

```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "ivanov",
    "userId": 1
}
```

### 2. Авторизация

**POST** `/api/auth/login`

**Тело запроса:**

```json
{
    "username": "ivanov",
    "password": "password123"
}
```

**Ответ:**

```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "ivanov",
    "userId": 1
}
```

### 3. Получить профиль пользователя

**GET** `/api/user/profile`

**Заголовки:**

```
Authorization: Bearer <token>
```

**Ответ:**

```json
{
    "id": 1,
    "firstName": "Иван",
    "lastName": "Иванов",
    "middleName": "Иванович",
    "username": "ivanov",
    "countryOfArrival": "Узбекистан",
    "arrivalDate": "2024-01-15",
    "isProfileComplete": true
}
```

### 4. Обновить профиль пользователя

**PUT** `/api/user/profile`

**Заголовки:**

```
Authorization: Bearer <token>
```

**Тело запроса:**

```json
{
    "countryOfArrival": "Узбекистан",
    "arrivalDate": "2024-01-15"
}
```

**Ответ:**

```json
{
    "id": 1,
    "firstName": "Иван",
    "lastName": "Иванов",
    "middleName": "Иванович",
    "username": "ivanov",
    "countryOfArrival": "Узбекистан",
    "arrivalDate": "2024-01-15",
    "isProfileComplete": true
}
```

## H2 Console

Для доступа к консоли H2 базы данных:

```
http://localhost:8080/h2-console
```

**Настройки подключения:**

-   JDBC URL: `jdbc:h2:mem:fmsdb`
-   Username: `sa`
-   Password: (пусто)
