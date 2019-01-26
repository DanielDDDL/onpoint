CREATE TABLE IF NOT EXISTS mark_type (
   id INT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR (255) NOT NULL
);

CREATE TABLE IF NOT EXISTS mark (
   id INT AUTO_INCREMENT PRIMARY KEY,
   when_happened TIMESTAMP NOT NULL,
   marked_date TIMESTAMP NOT NULL,
   marked_type_id INT,
   FOREIGN KEY (marked_type_id) REFERENCES mark_type(id)
);