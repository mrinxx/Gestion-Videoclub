-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.5.8-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para gestion_videoclub
CREATE DATABASE IF NOT EXISTS `gestion_videoclub` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `gestion_videoclub`;

-- Volcando estructura para tabla gestion_videoclub.alquileres
CREATE TABLE IF NOT EXISTS `alquileres` (
  `numero_alquiler` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(50) NOT NULL,
  `pelicula` varchar(11) NOT NULL,
  `fecha_alquiler` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`numero_alquiler`) USING BTREE,
  KEY `alquileres_pelicula_fk` (`pelicula`),
  KEY `alquileres_usuario_fk` (`usuario`),
  CONSTRAINT `alquileres_pelicula_fk` FOREIGN KEY (`pelicula`) REFERENCES `peliculas` (`id`) ON DELETE CASCADE,
  CONSTRAINT `alquileres_usuario_fk` FOREIGN KEY (`usuario`) REFERENCES `usuarios` (`nombreusuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla gestion_videoclub.alquileres: ~0 rows (aproximadamente)
DELETE FROM `alquileres`;
/*!40000 ALTER TABLE `alquileres` DISABLE KEYS */;
INSERT INTO `alquileres` (`numero_alquiler`, `usuario`, `pelicula`, `fecha_alquiler`) VALUES
	(21, 'manuelortega', '25687-P', '2021-02-20');
/*!40000 ALTER TABLE `alquileres` ENABLE KEYS */;

-- Volcando estructura para tabla gestion_videoclub.peliculas
CREATE TABLE IF NOT EXISTS `peliculas` (
  `id` varchar(11) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `genero` varchar(50) NOT NULL,
  `actor_ppal` varchar(100) NOT NULL,
  `copias_disponibles` int(11) NOT NULL DEFAULT 0,
  `estreno` varchar(6) NOT NULL,
  `precio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Índice 2` (`titulo`),
  CONSTRAINT `CK_peliculas_premium` CHECK (`estreno` in ('true','false'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla gestion_videoclub.peliculas: ~5 rows (aproximadamente)
DELETE FROM `peliculas`;
/*!40000 ALTER TABLE `peliculas` DISABLE KEYS */;
INSERT INTO `peliculas` (`id`, `titulo`, `genero`, `actor_ppal`, `copias_disponibles`, `estreno`, `precio`) VALUES
	('00000-L', 'Coco', 'animacion', 'Anthony Gonzalez', 5, 'true', 2),
	('1234-R', 'Star Wars: El retorno del Jedi', 'ciencia ficcion', 'Carrie Fisher', 4, 'false', 1),
	('23458-H', 'The Social Network', 'drama', 'Jesse Eisenberg', 6, 'false', 1),
	('25687-P', 'Origen', 'accion', 'Leonardo Di Caprio', 9, 'false', 1),
	('45215-M', 'Dando la nota', 'comedia', 'Jack Black', 1, 'false', 1),
	('645786-B', 'Cisne Negro', 'drama', 'Natalie Portman', 1, 'true', 2),
	('753865', 'El diario de Noa', 'romance', 'Ryan Gosling', 15, 'true', 2),
	('85468-N', 'Skyfall', 'accion', 'Daniel Craig', 0, 'true', 2);
/*!40000 ALTER TABLE `peliculas` ENABLE KEYS */;

-- Volcando estructura para tabla gestion_videoclub.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `nombreusuario` varchar(50) NOT NULL,
  `clave` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `saldo` double(22,2) DEFAULT 0.00,
  `premium` varchar(6) DEFAULT '0',
  PRIMARY KEY (`nombreusuario`),
  CONSTRAINT `CK_usuarios_premium` CHECK (`premium` in ('true','false'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla gestion_videoclub.usuarios: ~2 rows (aproximadamente)
DELETE FROM `usuarios`;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`nombreusuario`, `clave`, `nombre`, `apellidos`, `email`, `saldo`, `premium`) VALUES
	('epimentel', 'eduedu', 'Eduardo', 'Pimentel', 'epimentel@test.com', 2.30, 'false'),
	('manuelortega', 'manuelmanuel', 'Manuel ', 'Ortega', 'manuel@test.com', 1.10, 'true'),
	('sandrarom_', 'sandrasandra', 'Sandra', 'Romero', 'sandra@test.com', 0.00, 'true');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
