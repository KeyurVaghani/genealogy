-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: finalgenealogy
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `attributes`
--

DROP TABLE IF EXISTS `attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attributes` (
  `personId` int NOT NULL,
  `attributeKey` varchar(100) NOT NULL,
  `attributeValue` varchar(100) NOT NULL,
  KEY `personId` (`personId`),
  CONSTRAINT `attributes_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `personidentity` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attributes`
--

LOCK TABLES `attributes` WRITE;
/*!40000 ALTER TABLE `attributes` DISABLE KEYS */;
INSERT INTO `attributes` VALUES (433,'gender','MALE'),(433,'dateOfBirth','2000-01-01'),(433,'locationOfBirth','halifax'),(433,'job','worker'),(439,'gender','FEMALE'),(439,'locationOfBirth','halifax'),(439,'dateOfBirth','1998-09-11');
/*!40000 ALTER TABLE `attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biologicalparentingrelation`
--

DROP TABLE IF EXISTS `biologicalparentingrelation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `biologicalparentingrelation` (
  `childId` int NOT NULL,
  `ancestorId` int NOT NULL,
  `generation` int NOT NULL,
  KEY `childId` (`childId`),
  CONSTRAINT `biologicalparentingrelation_ibfk_1` FOREIGN KEY (`childId`) REFERENCES `personidentity` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biologicalparentingrelation`
--

LOCK TABLES `biologicalparentingrelation` WRITE;
/*!40000 ALTER TABLE `biologicalparentingrelation` DISABLE KEYS */;
INSERT INTO `biologicalparentingrelation` VALUES (433,436,1),(434,436,1),(435,436,1),(436,437,1),(433,437,2),(434,437,2),(435,437,2),(441,439,1),(442,440,1),(439,438,1),(441,438,2),(440,438,1),(442,438,2),(438,437,1),(439,437,2),(441,437,3),(440,437,2),(442,437,3),(438,443,1),(439,443,2),(441,443,3),(440,443,2),(442,443,3),(444,443,1),(445,444,1),(445,443,2),(434,433,1),(434,436,2),(434,437,3);
/*!40000 ALTER TABLE `biologicalparentingrelation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biologicalpartneringrelation`
--

DROP TABLE IF EXISTS `biologicalpartneringrelation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `biologicalpartneringrelation` (
  `partner1Id` int NOT NULL,
  `partner2Id` int NOT NULL,
  `relation` enum('PARTNER','DISSOLUTE') NOT NULL,
  KEY `partner1Id` (`partner1Id`),
  CONSTRAINT `biologicalpartneringrelation_ibfk_1` FOREIGN KEY (`partner1Id`) REFERENCES `personidentity` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biologicalpartneringrelation`
--

LOCK TABLES `biologicalpartneringrelation` WRITE;
/*!40000 ALTER TABLE `biologicalpartneringrelation` DISABLE KEYS */;
INSERT INTO `biologicalpartneringrelation` VALUES (435,436,'DISSOLUTE'),(435,436,'DISSOLUTE'),(435,436,'DISSOLUTE'),(435,436,'PARTNER'),(437,443,'PARTNER');
/*!40000 ALTER TABLE `biologicalpartneringrelation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fileidentifier`
--

DROP TABLE IF EXISTS `fileidentifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fileidentifier` (
  `fileId` int NOT NULL AUTO_INCREMENT,
  `fileName` varchar(250) NOT NULL,
  PRIMARY KEY (`fileId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fileidentifier`
--

LOCK TABLES `fileidentifier` WRITE;
/*!40000 ALTER TABLE `fileidentifier` DISABLE KEYS */;
INSERT INTO `fileidentifier` VALUES (1,'this is file location 1'),(2,'this is file location 2'),(3,'this is file a location 2'),(4,'this is file a location 2'),(5,'this is file a location 2'),(6,'this is file a location 2'),(7,'is file a location 2'),(8,'txt file'),(9,'this is for location 2'),(10,'this is 3rd file');
/*!40000 ALTER TABLE `fileidentifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `individuals`
--

DROP TABLE IF EXISTS `individuals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `individuals` (
  `fileId` int NOT NULL,
  `personId` int NOT NULL,
  KEY `fileId` (`fileId`),
  KEY `personId` (`personId`),
  CONSTRAINT `individuals_ibfk_1` FOREIGN KEY (`fileId`) REFERENCES `fileidentifier` (`fileId`),
  CONSTRAINT `individuals_ibfk_2` FOREIGN KEY (`personId`) REFERENCES `personidentity` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `individuals`
--

LOCK TABLES `individuals` WRITE;
/*!40000 ALTER TABLE `individuals` DISABLE KEYS */;
INSERT INTO `individuals` VALUES (5,433),(4,438),(2,440),(1,445),(3,437);
/*!40000 ALTER TABLE `individuals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mediaattributes`
--

DROP TABLE IF EXISTS `mediaattributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mediaattributes` (
  `fileId` int NOT NULL,
  `mediaAttributeKey` varchar(100) NOT NULL,
  `mediaAttributeValue` varchar(100) NOT NULL,
  KEY `fileId` (`fileId`),
  CONSTRAINT `mediaattributes_ibfk_1` FOREIGN KEY (`fileId`) REFERENCES `fileidentifier` (`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mediaattributes`
--

LOCK TABLES `mediaattributes` WRITE;
/*!40000 ALTER TABLE `mediaattributes` DISABLE KEYS */;
INSERT INTO `mediaattributes` VALUES (8,'date','2005-01-01'),(8,'country','Canada'),(8,'province','Montreal'),(8,'location','Toronto');
/*!40000 ALTER TABLE `mediaattributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notes` (
  `personId` int NOT NULL,
  `note` varchar(250) NOT NULL,
  KEY `personId` (`personId`),
  CONSTRAINT `notes_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `personidentity` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
INSERT INTO `notes` VALUES (433,'note will be here');
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personidentity`
--

DROP TABLE IF EXISTS `personidentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personidentity` (
  `personId` int NOT NULL AUTO_INCREMENT,
  `personName` varchar(100) NOT NULL,
  PRIMARY KEY (`personId`)
) ENGINE=InnoDB AUTO_INCREMENT=521 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personidentity`
--

LOCK TABLES `personidentity` WRITE;
/*!40000 ALTER TABLE `personidentity` DISABLE KEYS */;
INSERT INTO `personidentity` VALUES (433,'A'),(434,'B'),(435,'C'),(436,'D'),(437,'J'),(438,'I'),(439,'G'),(440,'H'),(441,'E'),(442,'F'),(443,'M'),(444,'L'),(445,'K'),(446,'testPerson1'),(447,'testPerson1'),(448,'testPerson1'),(449,'A'),(450,'B'),(451,'C'),(452,'D'),(453,'J'),(454,'I'),(455,'G'),(456,'H'),(457,'E'),(458,'F'),(459,'M'),(460,'L'),(461,'K'),(462,'testPerson1'),(463,'testPerson1'),(464,'testPerson1'),(465,'A'),(466,'B'),(467,'C'),(468,'D'),(469,'J'),(470,'I'),(471,'G'),(472,'H'),(473,'E'),(474,'F'),(475,'M'),(476,'L'),(477,'K'),(478,'testPerson1'),(479,'A'),(480,'B'),(481,'C'),(482,'D'),(483,'J'),(484,'I'),(485,'G'),(486,'H'),(487,'E'),(488,'F'),(489,'M'),(490,'L'),(491,'K'),(492,'testPerson1'),(493,'testPerson1'),(494,'A'),(495,'B'),(496,'C'),(497,'D'),(498,'J'),(499,'I'),(500,'G'),(501,'H'),(502,'E'),(503,'F'),(504,'M'),(505,'L'),(506,'K'),(507,'testPerson1'),(508,'A'),(509,'B'),(510,'C'),(511,'D'),(512,'J'),(513,'I'),(514,'G'),(515,'H'),(516,'E'),(517,'F'),(518,'M'),(519,'L'),(520,'K');
/*!40000 ALTER TABLE `personidentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sourcereference`
--

DROP TABLE IF EXISTS `sourcereference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sourcereference` (
  `personId` int NOT NULL,
  `sourceRef` varchar(250) NOT NULL,
  KEY `personId` (`personId`),
  CONSTRAINT `sourcereference_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `personidentity` (`personId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sourcereference`
--

LOCK TABLES `sourcereference` WRITE;
/*!40000 ALTER TABLE `sourcereference` DISABLE KEYS */;
INSERT INTO `sourcereference` VALUES (433,'this is a word');
/*!40000 ALTER TABLE `sourcereference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `fileId` int NOT NULL,
  `tag` varchar(50) NOT NULL,
  KEY `fileId` (`fileId`),
  CONSTRAINT `tags_ibfk_1` FOREIGN KEY (`fileId`) REFERENCES `fileidentifier` (`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (1,'keyur'),(8,'keyur');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-14 17:30:30
