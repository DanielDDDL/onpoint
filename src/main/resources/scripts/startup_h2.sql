-- TODO create the database and place here the script to start up the tables for the H2 database

DROP TABLE IF EXISTS books;

CREATE TABLE IF NOT EXISTS books (
   id INT AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(50) NOT NULL,
   author VARCHAR(20) NOT NULL
);

