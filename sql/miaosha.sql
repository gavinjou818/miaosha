-- MySQL dump 10.13  Distrib 5.7.31, for Linux (x86_64)
--
-- Host: localhost    Database: miaosha
-- ------------------------------------------------------
-- Server version	5.7.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `miaosha`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `miaosha` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `miaosha`;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL DEFAULT '',
  `price` double(10,0) NOT NULL DEFAULT '0',
  `description` varchar(500) NOT NULL DEFAULT '',
  `sales` int(11) NOT NULL DEFAULT '0',
  `img_url` varchar(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'iphone99',800,'zuihaoyong',1,'https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg'),(2,'iphone99',800,'zuihaoyong',2,'https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg'),(3,'iphone99',800,'zuihaoyong',3,'https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg'),(4,'iphone99',800,'zuihaoyong',4,'https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg'),(5,'iphone99',800,'zuihaoyong',5,'https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg'),(6,'iphone99',800,'zuihaoyong',44,'https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg'),(7,'iphone99',800,'zuihaoyong',0,'https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_stock`
--

DROP TABLE IF EXISTS `item_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stock` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_id_index` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_stock`
--

LOCK TABLES `item_stock` WRITE;
/*!40000 ALTER TABLE `item_stock` DISABLE KEYS */;
INSERT INTO `item_stock` VALUES (1,100,1),(2,100,2),(3,100,3),(4,100,4),(5,100,5),(6,64,6),(7,100,7);
/*!40000 ALTER TABLE `item_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_info` (
  `id` varchar(32) COLLATE utf8_bin NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) NOT NULL DEFAULT '0',
  `item_price` double(10,2) NOT NULL DEFAULT '0.00',
  `amount` int(11) NOT NULL DEFAULT '0',
  `order_price` double(40,2) NOT NULL DEFAULT '0.00',
  `promo_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info`
--

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
INSERT INTO `order_info` VALUES ('2020072500000000',2,6,800.00,1,800.00,0),('2020072500000100',2,6,800.00,1,800.00,0),('2020072500000200',2,6,800.00,1,800.00,0),('2020072500000300',2,6,100.00,1,100.00,1),('2020073100000400',2,6,100.00,1,100.00,1),('2020080300000500',2,6,800.00,1,800.00,0),('2020080300000600',2,6,800.00,1,800.00,0),('2020080300000700',2,6,800.00,1,800.00,0),('2020080300000800',2,6,800.00,1,800.00,0),('2020080300000900',2,6,800.00,1,800.00,0),('2020080300001000',2,6,800.00,1,800.00,0),('2020080300001100',2,6,800.00,1,800.00,0),('2020080300001200',2,6,800.00,1,800.00,0),('2020080300001300',2,6,800.00,1,800.00,0),('2020080300001400',2,6,800.00,1,800.00,0),('2020080300001500',2,6,800.00,1,800.00,0),('2020080300001600',2,6,800.00,1,800.00,0),('2020080300001700',2,6,800.00,1,800.00,0),('2020080300001800',2,6,800.00,1,800.00,0),('2020080400001900',2,6,800.00,1,800.00,0),('2020080400002000',2,6,800.00,1,800.00,0),('2020080400002100',2,6,800.00,1,800.00,0),('2020080400002200',2,6,800.00,1,800.00,0),('2020080400002300',2,6,800.00,1,800.00,0),('2020080400002400',2,6,800.00,1,800.00,0),('2020081100002500',2,6,800.00,1,800.00,0),('2020081100002600',2,6,800.00,1,800.00,0),('2020081100002700',2,6,800.00,1,800.00,0),('2020081100002800',2,6,800.00,1,800.00,0),('2020081100002900',2,6,800.00,1,800.00,0),('2020081100003000',2,6,800.00,1,800.00,0),('2020081100003100',2,6,800.00,1,800.00,0),('2020081100003200',2,6,800.00,1,800.00,0),('2020081200003300',2,6,800.00,1,800.00,0),('2020081200003400',2,6,800.00,1,800.00,0),('2020081200003500',2,6,800.00,1,800.00,0),('2020081200003600',2,6,100.00,1,100.00,1),('2020081200003700',2,6,100.00,1,100.00,1),('2020081200003800',2,6,100.00,1,100.00,1),('2020081200003900',2,6,100.00,1,100.00,1);
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promo`
--

DROP TABLE IF EXISTS `promo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(255) NOT NULL DEFAULT '',
  `start_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `item_id` int(11) NOT NULL DEFAULT '0',
  `promo_item_price` double NOT NULL DEFAULT '0',
  `end_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promo`
--

LOCK TABLES `promo` WRITE;
/*!40000 ALTER TABLE `promo` DISABLE KEYS */;
INSERT INTO `promo` VALUES (1,'iphone4抢购活动','2020-07-25 22:03:36',6,100,'2020-08-13 22:16:14');
/*!40000 ALTER TABLE `promo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequence_info`
--

DROP TABLE IF EXISTS `sequence_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_info` (
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `current_value` int(11) NOT NULL DEFAULT '0',
  `step` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequence_info`
--

LOCK TABLES `sequence_info` WRITE;
/*!40000 ALTER TABLE `sequence_info` DISABLE KEYS */;
INSERT INTO `sequence_info` VALUES ('order_info',40,1);
/*!40000 ALTER TABLE `sequence_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_log`
--

DROP TABLE IF EXISTS `stock_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_log` (
  `stock_log_id` varchar(64) NOT NULL,
  `item_id` int(11) NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '//1表示初始状态，2表示下单扣减库存成功，3表示下单回滚',
  PRIMARY KEY (`stock_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_log`
--

LOCK TABLES `stock_log` WRITE;
/*!40000 ALTER TABLE `stock_log` DISABLE KEYS */;
INSERT INTO `stock_log` VALUES ('006bc393c83a43eabb8d440fc0203fb8',6,1,3),('09c0df6371cb47058993b1e707e13554',6,1,2),('2bd8adc3ff7b4fa28cc4305f54590bdf',6,1,2),('2cdc99b4322a4943871bdbd0c611b8cf',6,1,2),('32a5c14d9c004f309f70dfe9dc321bdb',6,1,2),('591cd7eb928b415fa202a3326435475f',6,1,1),('650bd7f41cd443e0aaab03177a05238b',6,1,2),('6ae83ac2198546dc8bb3374950560538',6,1,3),('7680abe640d5458ea025732f8236c41c',6,1,2),('9bb7e84e3b0a4e9aab520b8c9f5b1d80',6,1,2),('a5678a16efc94d7cbd7f180019640f5c',6,1,2);
/*!40000 ALTER TABLE `stock_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '',
  `gender` tinyint(4) NOT NULL DEFAULT '0',
  `age` int(11) NOT NULL DEFAULT '0',
  `telphone` varchar(255) NOT NULL DEFAULT '',
  `register_mode` varchar(255) NOT NULL DEFAULT '' COMMENT 'byphone,bywechat,byalipay',
  `third_party_id` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `telphone_unique_index` (`telphone`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'zhouzhifeng',1,20,'1312345678','byphone',''),(2,'zhouzhifeng',1,20,'131111','byphone',''),(3,'zzz',1,20,'131','byphone',''),(4,'111',11,22,'1311','byphone',''),(6,'zzzz',1,20,'111','byphone','');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_password`
--

DROP TABLE IF EXISTS `user_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_password` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `encrpt_password` varchar(128) NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_password`
--

LOCK TABLES `user_password` WRITE;
/*!40000 ALTER TABLE `user_password` DISABLE KEYS */;
INSERT INTO `user_password` VALUES (1,'JdVa0oOqQAr0ZMdtcTwHrQ==',1),(2,'ytlQXrsDhB8Qgui56v6M+w==',2),(3,'aY1RoZ2KEhzlgUmde3AWaA==',3),(4,'ZRK9Q9nKpuAsmQsKgmUtyg==',4),(5,'ZRK9Q9nKpuAsmQsKgmUtyg==',6);
/*!40000 ALTER TABLE `user_password` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-15  4:01:12
