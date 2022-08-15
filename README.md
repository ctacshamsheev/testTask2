# testTask2

Тестовое задание на соискателя Java Junior Developer aikamsoft.com

Необходимо разработать приложение, предоставляющее сервис работы с данными в БД. Данный сервис, на основании входных параметров(аргументы командной строки), типа операции и входного файла – извлекает необходимые данные из БД и формирует результат обработки в выходной файл. 
Все возможные ошибки должны быть обработаны и зафиксированы в выходном файле

## Инструкция по сборке (os linux, mac)

-1) склонировать проект 

`https://github.com/ctacshamsheev/testTask2.git`

0) заполнить таблицу базы данных 

`sudo -u postgres psql`

`\i select.sql`

(в Main в константы вынесены основные параметры подключения
- DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/postgres";
- DB_USER = "postgres";
- DB_PASSWORD = "postgres";
  при необходимости, их можно поменять, на нужные
)

1) Необходимо войти в корень директории testTask2/

`cd testTask2/`

2) Сборка проекта 

`mvn package`

3) Запуск 

перейти в директорию target/

`cd target/`

выполнить запуск 

 `java -jar testTask2-1.0-SNAPSHOT.jar search in1.json out1.json`
 
 `java -jar testTask2-1.0-SNAPSHOT.jar search in2.json out2.json`
 
