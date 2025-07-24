# My Simple Blog - простое веб-приложение-блог на Spring Framework 6

Предоставляет простой базовый функционал блога с просмотром списка постов, их созданием, редактированием, добавлением комментариев и лайков.

## Технологии:

- Spring Framework 6 (**без Spring Boot**)
- Java 21
- Maven
- in memory H2
- JUnit 5
- Spring TestContext Framework
- MapStruct

## Для запуска необходимо:

- Java 21
- Maven 
- Сервлет-контейнер (Tomcat или Jetty)

## Сборка и тестирование:

1.  **Компиляция и упаковка:**
    `mvn clean package`
    - создание my-simple-blog.war в каталоге target.
2. Запуск тестов:
   `mvn clean test`


## Развертывание:

1. Развертывание в сервлет-контейнере:
   - Переместить my-simple-blog.war из каталога target в каталог webapps контейнера сервлетов.
   - Запустить контейнер сервлетов (Tomcat или Jetty).
2. Доступ к приложению (для локальной сборки):
   - http://localhost:8080/my-simple-blog/
