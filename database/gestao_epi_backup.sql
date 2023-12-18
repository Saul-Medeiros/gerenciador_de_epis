-- MariaDB dump 10.19  Distrib 10.4.28-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: gestao_epi
-- ------------------------------------------------------
-- Server version	10.4.28-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `gestao_epi`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `gestao_epi` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */;

USE `gestao_epi`;

--
-- Table structure for table `alocacao`
--

DROP TABLE IF EXISTS `alocacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alocacao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_entrega` date NOT NULL,
  `data_devolucao` date DEFAULT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_funcionario` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_alocacao_usuario1_idx` (`id_usuario`),
  KEY `fk_alocacao_Funcionario1_idx` (`id_funcionario`),
  CONSTRAINT `fk_alocacao_Funcionario1` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_alocacao_usuario1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alocacao`
--

LOCK TABLES `alocacao` WRITE;
/*!40000 ALTER TABLE `alocacao` DISABLE KEYS */;
INSERT INTO `alocacao` VALUES (20,'2023-11-15',NULL,1,6);
/*!40000 ALTER TABLE `alocacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alocacao_epi`
--

DROP TABLE IF EXISTS `alocacao_epi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alocacao_epi` (
  `id_alocacao` int(11) NOT NULL,
  `id_epi` int(11) NOT NULL,
  PRIMARY KEY (`id_alocacao`,`id_epi`),
  KEY `fk_alocacao_has_epi_epi1_idx` (`id_epi`),
  KEY `fk_alocacao_has_epi_alocacao1_idx` (`id_alocacao`),
  CONSTRAINT `fk_alocacao_has_epi_alocacao1` FOREIGN KEY (`id_alocacao`) REFERENCES `alocacao` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_alocacao_has_epi_epi1` FOREIGN KEY (`id_epi`) REFERENCES `epi` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alocacao_epi`
--

LOCK TABLES `alocacao_epi` WRITE;
/*!40000 ALTER TABLE `alocacao_epi` DISABLE KEYS */;
INSERT INTO `alocacao_epi` VALUES (20,2),(20,24);
/*!40000 ALTER TABLE `alocacao_epi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cargo`
--

DROP TABLE IF EXISTS `cargo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cargo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(64) NOT NULL,
  `funcionarios_vinculados` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargo`
--

LOCK TABLES `cargo` WRITE;
/*!40000 ALTER TABLE `cargo` DISABLE KEYS */;
INSERT INTO `cargo` VALUES (1,'Auxiliar de Rampa',0),(2,'Auxiliar de Triagem',0),(3,'Coordenador de Rampa',0),(4,'Líder de Rampa',0),(5,'Líder de Limpeza',0),(6,'Auxiliar de Limpeza',0),(7,'Auxiliar Administrativo',0),(8,'Mecanico de Equipamentos',0),(9,'Operador de Equipamentos',0),(10,'Borracheiro',0),(11,'Coordenador de Trafego',0),(12,'Assistente Administrativo',0),(13,'Profiler',0);
/*!40000 ALTER TABLE `cargo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cargo_epi`
--

DROP TABLE IF EXISTS `cargo_epi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cargo_epi` (
  `id_cargo` int(11) NOT NULL,
  `id_epi` int(11) NOT NULL,
  PRIMARY KEY (`id_cargo`,`id_epi`),
  KEY `fk_cargo_has_epi_epi1_idx` (`id_epi`),
  KEY `fk_cargo_has_epi_cargo1_idx` (`id_cargo`),
  CONSTRAINT `fk_cargo_has_epi_cargo1` FOREIGN KEY (`id_cargo`) REFERENCES `cargo` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_cargo_has_epi_epi1` FOREIGN KEY (`id_epi`) REFERENCES `epi` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargo_epi`
--

LOCK TABLES `cargo_epi` WRITE;
/*!40000 ALTER TABLE `cargo_epi` DISABLE KEYS */;
INSERT INTO `cargo_epi` VALUES (12,2),(12,24);
/*!40000 ALTER TABLE `cargo_epi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `epi`
--

DROP TABLE IF EXISTS `epi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `epi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `descricao` varchar(45) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `epi`
--

LOCK TABLES `epi` WRITE;
/*!40000 ALTER TABLE `epi` DISABLE KEYS */;
INSERT INTO `epi` VALUES (1,'Avental de PVC/QTU',NULL,'1'),(2,'Botas Masculinas',NULL,'1'),(9,'Botas de PVC',NULL,'1'),(10,'Calçado Feminino Usafe',NULL,'1'),(11,'Calçado Feminino Marluvas',NULL,'1'),(12,'Calçado Feminino Calbras',NULL,'1'),(13,'Calçado Feminino Fujiwara',NULL,'1'),(14,'Capa de Chuva',NULL,'1'),(15,'Luva de Malha Amarela',NULL,'1'),(16,'Luva de Malha Azul',NULL,'1'),(17,'Luva de Malha Triagem',NULL,'1'),(18,'Luva Nitrílica',NULL,'1'),(19,'Luva Tátil',NULL,'1'),(20,'Teste',NULL,'0'),(21,'Óculos de Proteção',NULL,'1'),(22,'Protetor Tipo Plug','Protetor para Ouvido (Auricular)','1'),(23,'Protetor Tipo Concha','Protetor para Ouvido (Auricular)','1'),(24,'Colete Preto',NULL,'1');
/*!40000 ALTER TABLE `epi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `funcionario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `setor` varchar(45) NOT NULL,
  `id_cargo` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_funcionario_cargo1_idx` (`id_cargo`),
  CONSTRAINT `fk_funcionario_cargo1` FOREIGN KEY (`id_cargo`) REFERENCES `cargo` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionario`
--

LOCK TABLES `funcionario` WRITE;
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` VALUES (6,'Gabriel Peres','Bagagens',12,1);
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(64) NOT NULL,
  `link` varchar(128) NOT NULL,
  `icone` varchar(64) NOT NULL,
  `exibir` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'Home','home.jsp','bi bi-house',1,1),(2,'Alocações','gerenciarAlocacao?acao=listar','bi bi-archive',1,1),(3,'Cargos','gerenciarCargo?acao=listar','bi bi-person-badge',1,1),(4,'EPIs','gerenciarEpi?acao=listar','bi bi-gear',1,1),(5,'Funcionários','gerenciarFuncionario?acao=listar','bi bi-person-rolodex',1,1),(6,'Menus','gerenciarMenu?acao=listar','bi bi-list-check',1,1),(7,'Perfis','gerenciarPerfil?acao=listar','bi bi-person-video2',1,1),(8,'Usuários do Sistema','gerenciarUsuario?acao=listar','bi bi-person',1,1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_perfil`
--

DROP TABLE IF EXISTS `menu_perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_perfil` (
  `id_menu` int(11) NOT NULL,
  `id_perfil` int(11) NOT NULL,
  PRIMARY KEY (`id_menu`,`id_perfil`),
  KEY `fk_menu_has_perfil_perfil1_idx` (`id_perfil`),
  KEY `fk_menu_has_perfil_menu1_idx` (`id_menu`),
  CONSTRAINT `fk_menu_has_perfil_menu1` FOREIGN KEY (`id_menu`) REFERENCES `menu` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_menu_has_perfil_perfil1` FOREIGN KEY (`id_perfil`) REFERENCES `perfil` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_perfil`
--

LOCK TABLES `menu_perfil` WRITE;
/*!40000 ALTER TABLE `menu_perfil` DISABLE KEYS */;
INSERT INTO `menu_perfil` VALUES (1,1),(1,3),(2,1),(2,3),(3,1),(4,1),(4,3),(5,1),(5,3),(6,1),(7,1),(8,1);
/*!40000 ALTER TABLE `menu_perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil`
--

DROP TABLE IF EXISTS `perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfil` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(64) NOT NULL,
  `data_cadastro` date NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES (1,'Administrador','2023-11-03',1),(3,'Técnico de Segurança','2023-11-15',1);
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(128) NOT NULL,
  `login` varchar(24) NOT NULL,
  `senha` varchar(254) NOT NULL,
  `status` int(11) NOT NULL,
  `id_perfil` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `senha_UNIQUE` (`senha`),
  KEY `fk_usuario_perfil_idx` (`id_perfil`),
  CONSTRAINT `fk_usuario_perfil` FOREIGN KEY (`id_perfil`) REFERENCES `perfil` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'admin','admin','admin',1,1),(3,'Flávia','flavia01','senha123',1,3);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-15 16:42:19
