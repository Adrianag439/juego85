-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: juego_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `jugador`
--

DROP TABLE IF EXISTS `jugador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jugador` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apodo` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sesion_activa` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_registro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_jugador_apodo` (`apodo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jugador`
--

LOCK TABLES `jugador` WRITE;
/*!40000 ALTER TABLE `jugador` DISABLE KEYS */;
INSERT INTO `jugador` VALUES ('044b9140-4270-4bbe-8f9e-6c19e71ee548','mdmdm',1,'2026-04-26 20:38:16'),('1029469a-04e2-4e59-b5b0-515110ef4422','kilo',1,'2026-04-26 20:30:15'),('1053ab8c-c86c-41a8-a1f3-0b8b4053d0f5','y',1,'2026-04-29 11:33:03'),('10da2307-db55-4923-9110-e7e973bc83d5','derlys',1,'2026-04-29 10:30:36'),('1248ca1b-dbac-461c-bf15-702ffd62e6a5','ala',1,'2026-04-26 20:39:57'),('169e2bdf-cbfd-4cb1-9b21-9dd7aa7e4c29','doris',1,'2026-04-28 18:37:25'),('1a69c041-d0de-4f5f-a3ea-f71224a9ac54','na',1,'2026-04-29 16:38:30'),('1cbbf11e-874b-4826-9ee1-6411278ed119','b',1,'2026-04-30 08:24:23'),('23ef5609-e594-42c6-aef7-a90a9af5487d','k',1,'2026-04-28 18:38:30'),('25983e27-8217-474d-9fe1-857fab2222c8','r',1,'2026-04-30 08:28:26'),('29b59a1c-3785-40b1-8fd3-281107654af9','A',1,'2026-04-28 20:50:55'),('2c721ce3-3294-44f5-9a02-df12fffdc435','nala',1,'2026-04-24 23:05:19'),('300cf57a-62f1-408d-8b94-ab51f7ea9ada','ox',1,'2026-04-29 11:47:21'),('36c0565a-bc94-4a94-8b53-3bab56c037a7','jugador1',1,'2026-04-24 21:49:00'),('38065cde-f738-4540-8871-bbecf0df40e4','qa',1,'2026-04-29 11:30:05'),('38e0c1a8-e8d6-4415-b95c-2c58182baf8c','angela',1,'2026-04-28 18:31:35'),('3d3da489-2e99-432e-b676-5b25f417ec99','aol',1,'2026-04-29 16:55:40'),('420d8cf5-168c-4b47-94bd-7efa78b36793','hi',1,'2026-04-29 11:28:08'),('44eaf568-1fc8-40f3-892b-fc2b38d4795c','1',1,'2026-04-29 14:34:26'),('510963da-b3a5-4f0c-9f9e-a1aae3cbeec5','er',1,'2026-04-29 18:25:06'),('588cefc9-2d69-4f05-99f0-b7ecc8246fed','jokolmk',1,'2026-04-26 20:04:10'),('5969a0bf-69ef-4913-9832-d58485ae1632','s',1,'2026-04-29 17:21:38'),('5b12afac-49bc-45a3-b5df-6d9c221b9136','AQE',1,'2026-04-29 18:08:53'),('5c95a015-a9a6-4f70-988f-1af6623503ad','pr',1,'2026-04-29 11:43:21'),('5ca3ad6e-9f80-4652-9753-ab5bca610ec7','an',1,'2026-04-29 14:42:31'),('5cf471fa-3f7c-4149-9aff-4f038df8b9f5','w',1,'2026-04-29 10:51:08'),('61372507-4dd1-47d8-bcd0-5fec19603a10','osu',1,'2026-04-29 11:49:41'),('64b9d4cb-f387-461a-8e96-e1458c5172a6','O',1,'2026-04-28 20:53:47'),('6b46eb56-97e1-40f1-89c4-e1438cab425f','angel',1,'2026-04-28 19:29:31'),('6de54955-7a23-40a8-8944-d974304ddbfd','l',1,'2026-04-29 16:47:12'),('7133ccf3-a224-434a-900d-b977b1d485c1','t',1,'2026-04-29 16:50:41'),('7a9bf44f-0835-4001-83a3-ba2e887f4194','sa',1,'2026-04-29 17:14:16'),('7c654e53-30ef-4cf7-aa27-8547fac7735d','ji',1,'2026-05-07 07:17:16'),('8778694c-d544-47d6-994e-3079782f2469','hola',1,'2026-04-24 22:54:04'),('8e7c6899-c170-44eb-8c3f-ab23ff0491ed','as',1,'2026-04-29 11:25:23'),('94d818f1-afca-4a55-8367-9667cfea86ee','4',1,'2026-04-29 12:09:01'),('99308a1e-dde5-4501-910b-c341e2e7ca74','I',1,'2026-04-28 21:09:30'),('a0883451-d7a9-41a8-b583-66cd863beba9','u',1,'2026-04-28 21:14:26'),('a4d32523-f2fc-42ac-bce7-ec4dffc18636','TestUser',0,'2026-03-25 19:56:33'),('aa6f9a0e-5df9-4fab-abcf-f3491635a5f5','po',1,'2026-04-28 21:17:03'),('aae7b0c2-21b7-40af-a909-9cbabef553d7','ho',1,'2026-04-28 21:04:53'),('b4afd59a-98f4-45e6-8b8e-02f2a514a159','odio',1,'2026-04-28 20:55:01'),('b9791768-cbe5-4764-83b7-32dd17934174','LO',1,'2026-04-29 11:31:22'),('badfe1dc-99d9-430d-9500-faf291572f31','ujuj',1,'2026-04-27 07:39:39'),('bb685ee3-0bda-4a60-84bf-fe67315d53ea','q',1,'2026-04-29 11:24:17'),('bd35a85b-9b1f-43a9-ba7a-a900a6d069f3','H',1,'2026-04-28 18:42:26'),('bd8b8e06-3d55-4c1b-87a0-d5592cac916e','yi',1,'2026-04-29 17:57:52'),('bfb4a4e5-8fbc-4bf7-af07-1c374814d3b4','Ñ',1,'2026-04-28 19:46:27'),('c78b17c0-e284-4420-91fa-de0089b6a2bc','j',1,'2026-04-29 17:52:44'),('ca4912aa-8720-4e47-b8d1-41219f0e036f','samu',1,'2026-04-24 23:01:23'),('db683312-913c-4558-b226-82c0a4f071ec','we',1,'2026-04-29 18:00:22'),('e5c8e20d-f2e1-4dd0-89c4-f5e085096123','clase',1,'2026-04-28 18:25:03'),('ecc643dc-e175-41c2-808d-c858b8a6a99f','hol',1,'2026-04-28 18:23:05'),('edcf3a94-ddeb-4ad3-b015-41149827462a','no',1,'2026-04-29 18:18:22'),('ef81917d-4218-44a3-a460-8b5954e7ad47','DO',1,'2026-04-28 19:32:41'),('efcb53e0-d001-4106-baac-aa3c809cf604','adriana',1,'2026-04-28 17:46:45'),('f097e49e-90ac-4756-8294-d5e8e683cd9f','P',1,'2026-04-28 19:50:12'),('f560a641-4894-4d30-ab52-8226031b75b9','que',1,'2026-05-09 19:10:22'),('fd112c6d-5ee3-41bc-88f0-85447ed7ab34','sao',1,'2026-04-29 17:31:03'),('fd552fa4-550c-4532-ab59-264cb0afe69f','plo',1,'2026-04-29 17:39:14');
/*!40000 ALTER TABLE `jugador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nivel`
--

DROP TABLE IF EXISTS `nivel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nivel` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `vidas` int NOT NULL,
  `tiene_temporizador` tinyint(1) NOT NULL DEFAULT '0',
  `puntuacion_base` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_nivel_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nivel`
--

LOCK TABLES `nivel` WRITE;
/*!40000 ALTER TABLE `nivel` DISABLE KEYS */;
INSERT INTO `nivel` VALUES ('nivel-dificil-001','DIFICIL',3,1,200),('nivel-facil-001','FACIL',5,0,100);
/*!40000 ALTER TABLE `nivel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partida`
--

DROP TABLE IF EXISTS `partida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partida` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apodo_jugador` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_nivel` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `vidas_restantes` int NOT NULL,
  `puntuacion` int NOT NULL DEFAULT '0',
  `intentos_fallidos` int NOT NULL DEFAULT '0',
  `activa` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_inicio` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_partida_jugador` (`apodo_jugador`),
  KEY `idx_partida_nivel` (`id_nivel`),
  KEY `idx_partida_activa` (`activa`),
  CONSTRAINT `fk_partida_jugador` FOREIGN KEY (`apodo_jugador`) REFERENCES `jugador` (`apodo`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_partida_nivel` FOREIGN KEY (`id_nivel`) REFERENCES `nivel` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partida`
--

LOCK TABLES `partida` WRITE;
/*!40000 ALTER TABLE `partida` DISABLE KEYS */;
INSERT INTO `partida` VALUES ('124098ff-5de8-4601-b264-5afbecd587c4','TestUser','nivel-facil-001',4,10,1,0,'2026-03-25 20:10:10'),('cc7f6b05-6e94-4933-914e-f676d0a9dd77','TestUser','nivel-facil-001',4,10,1,0,'2026-03-25 19:56:33');
/*!40000 ALTER TABLE `partida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pista`
--

DROP TABLE IF EXISTS `pista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pista` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_partida` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numero` int NOT NULL,
  `texto` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `usada` tinyint(1) NOT NULL DEFAULT '0',
  `fecha_uso` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_pista_partida` (`id_partida`),
  CONSTRAINT `fk_pista_partida` FOREIGN KEY (`id_partida`) REFERENCES `partida` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pista`
--

LOCK TABLES `pista` WRITE;
/*!40000 ALTER TABLE `pista` DISABLE KEYS */;
/*!40000 ALTER TABLE `pista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesion`
--

DROP TABLE IF EXISTS `sesion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesion` (
  `apodo` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`token`),
  KEY `fk_sesion_jugador` (`apodo`),
  CONSTRAINT `fk_sesion_jugador` FOREIGN KEY (`apodo`) REFERENCES `jugador` (`apodo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesion`
--

LOCK TABLES `sesion` WRITE;
/*!40000 ALTER TABLE `sesion` DISABLE KEYS */;
/*!40000 ALTER TABLE `sesion` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-10 17:55:31
