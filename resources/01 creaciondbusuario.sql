
DELIMITER ;
CREATE DATABASE Restaurante;

USE Restaurante;

CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';

GRANT ALL PRIVILEGES ON Restaurante.* TO 'admin'@'%';

# DROP USER admin;
# DROP DATABASE Restaurante;