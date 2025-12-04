# FMS API Документация

## Описание

API для системы управления дорожной картой действий для мигрантов в ФМС.

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

### 1. Авторизация

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

### 2. Получить профиль пользователя

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
    "hasRussianLanguageCertificate": false,
    "hasWorkPatent": false,
    "hasPaidStateDuty": false,
    "isProfileComplete": true
}
```

### 3. Обновить профиль пользователя

**PUT** `/api/user/profile`

**Заголовки:**

```
Authorization: Bearer <token>
```

**Тело запроса:**

```json
{
    "countryOfArrival": "Узбекистан",
    "arrivalDate": "2024-01-15",
    "hasRussianLanguageCertificate": false,
    "hasWorkPatent": false,
    "hasPaidStateDuty": false
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
    "hasRussianLanguageCertificate": false,
    "hasWorkPatent": false,
    "hasPaidStateDuty": false,
    "isProfileComplete": true
}
```

### 4. Получить дорожную карту

**GET** `/api/roadmap`

**Заголовки:**

```
Authorization: Bearer <token>
```

**Ответ (если профиль не заполнен):**

```json
{
    "roadmapByChapter": {},
    "isProfileComplete": false,
    "message": "Для получения дорожной карты необходимо заполнить профиль"
}
```

**Ответ (если профиль заполнен):**

```json
{
    "roadmapByChapter": {
        "Сертификат владения русским языком": [
            {
                "id": 1,
                "name": "Подать заявку в отделение ФМС",
                "chapter": "Сертификат владения русским языком",
                "orderInChapter": 1
            },
            {
                "id": 2,
                "name": "Прийти на экзамен в назначенную дату",
                "chapter": "Сертификат владения русским языком",
                "orderInChapter": 2
            },
            {
                "id": 3,
                "name": "Получить сертификат после успешной сдачи экзамена",
                "chapter": "Сертификат владения русским языком",
                "orderInChapter": 3
            }
        ],
        "Патент на работу": [
            {
                "id": 4,
                "name": "Собрать необходимые документы",
                "chapter": "Патент на работу",
                "orderInChapter": 1
            },
            {
                "id": 5,
                "name": "Подать заявление на получение патента",
                "chapter": "Патент на работу",
                "orderInChapter": 2
            }
        ]
    },
    "isProfileComplete": true,
    "message": "Ваша дорожная карта действий"
}
```

## Логика формирования дорожной карты

Дорожная карта формируется автоматически на основе данных профиля пользователя:

1. **Приоритет 1 (самый важный)**: Сертификат владения русским языком

    - Добавляется, если `hasRussianLanguageCertificate = false`

2. **Приоритет 2**: Патент на работу

    - Добавляется, если `hasWorkPatent = false`

3. **Приоритет 3**: Оплата госпошлины за патент
    - Добавляется, если `hasWorkPatent = true` и `hasPaidStateDuty = false`

## H2 Console

Для доступа к консоли H2 базы данных:

```
http://localhost:8080/h2-console
```

**Настройки подключения:**

-   JDBC URL: `jdbc:h2:mem:fmsdb`
-   Username: `sa`
-   Password: (пусто)
