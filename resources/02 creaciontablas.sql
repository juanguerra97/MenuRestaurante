DELIMITER ;

USE Restaurante;

CREATE TABLE Menu(
	nombre VARCHAR(64) PRIMARY KEY,
    disponible BOOLEAN DEFAULT TRUE
);

CREATE TABLE Alimento(
	nombre VARCHAR(64) PRIMARY KEY,
    disponible BOOLEAN DEFAULT TRUE,
	nombreMenu VARCHAR(64) NOT NULL,
    CONSTRAINT FK_menu_de_alimiento FOREIGN KEY(nombreMenu) 
		REFERENCES Menu(nombre) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Precio(
	id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombreAlimento VARCHAR(64) NOT NULL,
    nombreMedida VARCHAR(64) NOT NULL,
    precio DOUBLE,
    CONSTRAINT FK_alimento_de_precio FOREIGN KEY(nombreAlimento)
		REFERENCES Alimento(nombre) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Pedido(
	numero BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(48),
    estado VARCHAR(48),
    horaPedido DATETIME,
    horaEntrega DATETIME DEFAULT NULL,
    total DOUBLE DEFAULT 0,
    numeroMesa INT UNSIGNED,
    direccion VARCHAR(64)
);

CREATE TABLE AlimentoPedido(
	numeroPedido BIGINT UNSIGNED NOT NULL,
    id_precio BIGINT UNSIGNED NOT NULL,
    cantidad INT UNSIGNED DEFAULT 0,
    total DOUBLE DEFAULT 0,
    CONSTRAINT PK_AlimentoPedido PRIMARY KEY(numeroPedido,id_precio),
    CONSTRAINT FK_pedido_alimento FOREIGN KEY(numeroPedido) 
		REFERENCES Pedido(numero) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_precio_alimentopedido FOREIGN KEY(id_precio)
		REFERENCES Precio(id) ON DELETE RESTRICT ON UPDATE CASCADE
);
 