# ToDoApp

## Зависимости:

1. Java 21
2. Gradle 8.5 (или wrapper)
3. Docker
4. PostgreSQL

## Порядок запуска:

1. Сначала запускается docker-compose файл из папки docker, для поднятия Kafka
2. Затем запускаются сервисы ToDoAppMainApplication и ToDoAppEmulatorApplication (в любом порядке)