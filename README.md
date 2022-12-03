## Начало работы

1. Клонировать проект - https://github.com/Vurhis1/Diplom.git

### Предусловие

IDEA Docker Docker-compose: container mysql-1

### Зупуск проекта

1. Открыть проект в IDEA
2. Открыть программу DOCKER
3. Запуск docker-compose.yml - команда в терминале: docker-compose up
4. Запуск artifacts/aqa-shop.jar - команда в терминале: java -jar artifacts/aqa-shop.jar
5. Открыть тестовый класс "Test" - команда: Ctrl+Shift+N -> src/test/java/ru/netology/test/Test.java
6. Запуск тестов - команда в терминале: ./gradlew clean test
7. Просмотр отчетов Allure по итогам тестирования - команда в терминале: ./gradlew allureServe
