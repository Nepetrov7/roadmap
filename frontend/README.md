# FMS Frontend

Фронтенд приложение для системы управления дорожной картой ФМС.

## Технологии

- React 18
- TypeScript
- Vite
- React Router
- Axios
- SCSS Modules

## Установка

```bash
npm install
```

## Запуск в режиме разработки

```bash
npm run dev
```

Приложение будет доступно на `http://localhost:3000`

## Сборка для продакшена

```bash
npm run build
```

Собранные файлы будут в `./src/main/resources/static` 

## Структура проекта

```
src/
├── components/      # React компоненты
│   ├── Profile/    # Компонент профиля
│   ├── Roadmap/    # Компонент дорожной карты
├── services/       # API сервисы
├── types/          # TypeScript типы
├── App.tsx         # Главный компонент
└── main.tsx        # Точка входа
```

## API

Фронтенд использует прокси для API запросов. Все запросы к `/api/*` проксируются на `http://localhost:8080` (настроено в `vite.config.ts`).

