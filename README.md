
This is an example of real time chat created with Play Framework, AngularJS and websockets for
the [blog post](http://groz.github.io/scala/practical/chat/).

Setup:
* Install mysql
* Create the necessary databases and tables
```
mysql> CREATE DATABASE chatdb;
mysql> USE chatdb;
mysql> CREATE TABLE messages(id int NOT NULL AUTO_INCREMENT, text varchar(255), PRIMARY KEY (id));
```
* Set the environment variables $DB_USER and $DB_PASSWORD to your mysql login information

Usage:
* Run "sbt run" from the root of the repo
* Navigate to localhost:9000 in your browser
