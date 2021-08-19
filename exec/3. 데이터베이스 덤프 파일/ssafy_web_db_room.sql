-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ssafy_web_db
-- ------------------------------------------------------
-- Server version	5.7.33-log

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
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `observers` int(11) DEFAULT NULL,
  `participants` int(11) DEFAULT NULL,
  `playing` bit(1) DEFAULT NULL,
  `private_room` bit(1) DEFAULT NULL,
  `room_password` varchar(255) DEFAULT NULL,
  `start` bit(1) DEFAULT NULL,
  `times` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `topic_agree` varchar(255) DEFAULT NULL,
  `topic_opposite` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbx9snvq7gghcs7i2hjasjln6f` (`owner_id`),
  CONSTRAINT `FKbx9snvq7gghcs7i2hjasjln6f` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'2021-08-19 15:08:53.786000',NULL,4,NULL,_binary '\0',NULL,_binary '',40,'민트초코는?','옳다','아니다',1),
        (2,'2021-08-19 15:27:43.360000',NULL,6,NULL,_binary '\0',NULL,_binary '\0',30,'복숭아는?','물복','딱복',1),
        (3,'2021-08-19 15:29:43.360000',NULL,6,NULL,_binary '\0',NULL,_binary '\0',30,'취업은?','워라밸','연봉',1),
        (4,'2021-08-19 15:29:44.360000',NULL,6,NULL,_binary '\0',NULL,_binary '\0',40,'동물은?','고양이','강아지',1),
        (5,'2021-08-19 15:29:45.360000',NULL,6,NULL,_binary '\0',NULL,_binary '',30,'냉면은?','비냉','물냉',1),
        (6,'2021-08-19 15:29:46.360000',NULL,6,NULL,_binary '\0',NULL,_binary '',30,'동물은?','고양이','강아지',1),
        (7,'2021-08-19 15:29:47.360000',NULL,6,NULL,_binary '\0',NULL,_binary '',40,'복숭아는?','물복','딱복',1),
        (8,'2021-08-19 15:29:48.360000',NULL,6,NULL,_binary '\0',NULL,_binary '\0',30,'취업은?','워라밸','연봉',1),
        (9,'2021-08-19 15:29:49.360000',NULL,6,NULL,_binary '\0',NULL,_binary '\0',40,'민트초코는?','옳다','아니다',1),
        (10,'2021-08-19 15:29:50.360000',NULL,6,NULL,_binary '\0',NULL,_binary '\0',30,'냉면은?','비냉','물냉',1)
        ;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-20  4:18:44
