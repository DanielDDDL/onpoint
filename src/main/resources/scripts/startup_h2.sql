DROP TABLE IF EXISTS mark_type;
CREATE TABLE mark_type (
   id INT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (255) NOT NULL
);

DROP TABLE IF EXISTS mark;
CREATE TABLE mark (
   id INT AUTO_INCREMENT PRIMARY KEY,
   when_happened TIMESTAMP NOT NULL,
   marked_date TIMESTAMP NOT NULL,
   marked_type_id INT NOT NULL,
   FOREIGN KEY (marked_type_id) REFERENCES mark_type(id)
);