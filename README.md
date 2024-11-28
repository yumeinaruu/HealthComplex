## 1. Клонировать репозиторий

Непосредственно belrw-library:
```dtd
git clone 
```

Создать в корневой папке файл **.env** на основе **.env-example** и заполнить 
нужные переменные окружения.

## 2. Сборка прокта

Перейти в корневую папку проекта и выполнить следующую команду для запуска базы данных и приложения:
```shell
docker compose up -d --build health-complex-db
docker compose up -d --build health-complex
```
