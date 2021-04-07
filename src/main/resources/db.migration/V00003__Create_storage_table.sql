CREATE TABLE storage (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         store_id INT,
                         product VARCHAR(255),
                         count INT UNSIGNED,
                         FOREIGN KEY (store_id) REFERENCES store (id),
                         UNIQUE (store_id,product)
);