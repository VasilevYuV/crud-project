# CRUD Application

Универсальное JavaFX приложение для управления сущностями с функционалом CRUD (Create, Read, Update, Delete).

## 📋 Функциональные возможности

- ✅ Создание сущностей
- ✅ Просмотр списка сущностей
- ✅ Редактирование сущностей
- ✅ Удаление сущностей с подтверждением
- ✅ Валидация данных
- ✅ Поиск по имени
- ✅ Пагинация
- ✅ Стильный современный интерфейс

---

## 🛠 Технологии

- **Java 25** - основной язык
- **JavaFX 25** - графический интерфейс
- **PostgreSQL** - база данных
- **JDBC** - доступ к БД
- **Maven** - сборка проекта
- **JUnit 5** - тестирование
- **dotenv-java** - управление конфигурацией .env

---

## 📁 Структура проекта
```
crud_project/
├── src/main/java/com/vasilevyuv/crud_project/
│   ├── controller/
│   ├── dao/
│   ├── model/
│   ├── service/
│   ├── config/
│   ├── util/
│   └── MainApp.java
├── src/main/resources/com/vasilevyuv/crud_project/
│   ├── scenes/
│   ├── styles/
│   └── static/
├── src/test/
├── target/
├── pom.xml
├── .env
└── README.md
```

---

## 🚀 Запуск приложения

### Предварительные требования

1. **Java 25+**
2. **PostgreSQL 14+**
3. **Maven 3.9+**
```
### Шаг 1: Настройка базы данных
1. Создайте базу данных в PostgreSQL:
   CREATE DATABASE crud_app;

2. Настройте подключение в файле .env:
   DB_URL=jdbc:postgresql://localhost:5432/crud_app
   DB_USERNAME=postgres
   DB_PASSWORD=ваш_пароль
   DB_POOL_SIZE=10

### Шаг 2: Сборка проекта
mvn clean package

### Шаг 3: Запуск приложения

**Вариант 1: Через Maven**
mvn javafx:run

**Вариант 2: Из JAR файла**
java -jar target/crud_project-1.0-SNAPSHOT.jar
```

---

## 🧪 Тестирование

**Запуск тестов:**
mvn test

**Сборка с тестами:**
mvn clean test package

---

## 📦 Сборка исполняемого JAR
```
mvn clean compile assembly:single
```
Собранный JAR файл будет находиться в target/crud_project-1.0-SNAPSHOT.jar

---

## 🔧 Конфигурация

**Файл .env**
```
Основные настройки приложения:

DB_URL=jdbc:postgresql://localhost:5432/crud_app
DB_USERNAME=postgres
DB_PASSWORD=ваш_пароль
DB_POOL_SIZE=10
```
---

## 📝 Структура сущности

Каждая сущность содержит:
- Уникальный идентификатор (UUID)
- Название (3-50 символов)
- Описание (до 255 символов, опционально)
- Дата создания
- Дата последнего обновления

---

## Отладка

**Логирование**
Логи приложения выводятся в консоль. Для включения детального логирования добавьте:

java -Djavafx.debug=true -jar target/crud_project-1.0-SNAPSHOT.jar

**Запуск без сборки**
mvn compile javafx:run

---

## 📄 Лицензия

MIT License. Полный текст лицензии доступен в файле 
<a href="https://github.com/VasilevYuV/crud-project/blob/develop/LICENSE.md">LICENSE</a>.