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
    `./gradlew :bootJar`
    - создание my-simple-blog-0.0.1-SNAPSHOT.jar в каталоге build/libs.
2. Запуск тестов:
   `./gradlew :test`


## Развертывание:

1. `java -jar build/libs/my-simple-blog-0.0.1-SNAPSHOT.jar`
2. Доступ к приложению (для локальной сборки):
    - http://localhost:8080/my-simple-blog/
