-- MySQL dump 10.13  Distrib 8.3.0, for macos14.2 (arm64)
--
-- Host: 127.0.0.1    Database: jxc
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `s_admin`
--

DROP TABLE IF EXISTS `s_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态',
  `last_login_date` datetime(6) DEFAULT NULL COMMENT '最后登录时间',
  `merchant_id` int NOT NULL,
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `role_id` int DEFAULT NULL,
  `system_default` bit(1) DEFAULT NULL COMMENT '是否系统默认',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK23bugxg955svtuy2j9d8wpqwu` (`mobile`,`merchant_id`) USING BTREE,
  UNIQUE KEY `UK_hby25rpnay6vu2dgjpdxkn1lx` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_admin`
--

LOCK TABLES `s_admin` WRITE;
/*!40000 ALTER TABLE `s_admin` DISABLE KEYS */;
INSERT INTO `s_admin` VALUES (1,_binary '',NULL,1,'13944878765','李泽龙','$2a$10$ujHczbGBDvUrXjGDST4HQ.MrykZ7j1Da4aeTw.CygesOk13xx1u16',1,_binary '','10011'),(2,_binary '',NULL,4,'12345678909','王五','$2a$10$.BYY1gh6yU1ShLwr3s89Muw298rtBpQblagCn/NDmd5Hk3wJuZypK',5,_binary '','10021');
/*!40000 ALTER TABLE `s_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_args_setting`
--

DROP TABLE IF EXISTS `s_args_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_args_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cost_method` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '成本核算方法',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjn3xkat0ctm3qqtxkufpo9u5b` (`merchant_id`,`organization_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_args_setting`
--

LOCK TABLES `s_args_setting` WRITE;
/*!40000 ALTER TABLE `s_args_setting` DISABLE KEYS */;
INSERT INTO `s_args_setting` VALUES (1,'平',1,4),(2,'先',1,3),(3,'平',1,2),(4,'平',1,1),(5,'平',4,5),(6,'平',4,6),(7,'平',4,7),(8,'平',4,8);
/*!40000 ALTER TABLE `s_args_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_checkout`
--

DROP TABLE IF EXISTS `s_checkout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_checkout` (
  `id` int NOT NULL AUTO_INCREMENT,
  `check_date` date DEFAULT NULL,
  `check_month` int DEFAULT NULL,
  `check_year` int DEFAULT NULL,
  `merchant_id` int NOT NULL,
  `organization_id` int NOT NULL,
  `status` int DEFAULT NULL,
  `check_id` bigint DEFAULT NULL COMMENT '结账人',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UKkhxj6kpav426h8kl1y7ydih5a` (`check_year`,`check_month`,`organization_id`,`merchant_id`) USING BTREE,
  UNIQUE KEY `UKarvoax0i74a2lftwyb51gln9i` (`check_date`,`organization_id`,`merchant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_checkout`
--

LOCK TABLES `s_checkout` WRITE;
/*!40000 ALTER TABLE `s_checkout` DISABLE KEYS */;
INSERT INTO `s_checkout` VALUES (21,'2024-02-09',NULL,NULL,1,4,NULL,1,'2024-05-23 16:59:04.532597');
/*!40000 ALTER TABLE `s_checkout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_code_seed`
--

DROP TABLE IF EXISTS `s_code_seed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_code_seed` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` int NOT NULL,
  `merchant_id` int DEFAULT NULL,
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UKon77o69u9r44ubgcgyi35ldtf` (`merchant_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_code_seed`
--

LOCK TABLES `s_code_seed` WRITE;
/*!40000 ALTER TABLE `s_code_seed` DISABLE KEYS */;
INSERT INTO `s_code_seed` VALUES (1,2,0,'merchant','2024-05-24 09:30:21.009000'),(2,4,1,'1001','2024-05-06 12:03:12.346000'),(4,22,1,'productsCode','2024-05-22 14:24:33.556000'),(5,7,1,'CustomCode1','2024-05-18 08:20:06.837000'),(7,13,1,'CGRKD:20240508','2024-05-08 11:47:58.230000'),(9,3,1,'CGCKD:20240509','2024-05-09 11:37:04.439000'),(10,3,1,'CGRKD:20240509','2024-05-09 15:41:50.832000'),(11,1,1,'CGTHD:20240509','2024-05-09 15:38:30.875000'),(12,2,1,'CGCKD:20240510','2024-05-10 10:14:49.206000'),(13,2,1,'XSTHD:20240510','2024-05-10 10:43:50.546000'),(14,1,1,'CGRKD:20240510','2024-05-10 10:12:31.410000'),(15,3,1,'QTRKD:20240510','2024-05-10 15:51:26.303000'),(16,1,1,'QTCKD:20240510','2024-05-10 16:09:39.117000'),(17,2,1,'CKDBD:20240511','2024-05-11 10:04:33.405000'),(20,1,1,'PD:20240511','2024-05-11 16:19:19.860000'),(21,1,1,'PD:20240513','2024-05-13 09:41:27.271000'),(22,3,1,'QTRKD:20240513','2024-05-13 11:15:52.452000'),(23,3,1,'QTCKD:20240513','2024-05-13 11:56:42.653000'),(24,5,1,'XSCKD:20240514','2024-05-14 15:12:26.724000'),(25,2,1,'CGRKD:20240514','2024-05-14 11:11:15.724000'),(26,3,1,'XSTHD:20240514','2024-05-14 11:51:24.804000'),(27,1,1,'CGTHD:20240514','2024-05-14 12:04:42.644000'),(28,4,1,'PD:20240514','2024-05-14 14:41:04.755000'),(29,4,1,'QTCKD:20240514','2024-05-14 14:34:40.819000'),(30,7,1,'QTRKD:20240514','2024-05-14 14:41:14.066000'),(31,3,1,'CKDBD:20240514','2024-05-14 15:13:03.288000'),(32,9,1,'CGRKD:20240515','2024-05-15 16:31:49.908000'),(33,23,1,'XSCKD:20240515','2024-05-15 16:32:17.849000'),(34,1,1,'CGRKD:20240516','2024-05-16 10:49:19.992000'),(35,4,1,'XSCKD:20240516','2024-05-16 14:37:11.784000'),(36,10,1,'CGRKD:20240517','2024-05-17 16:19:34.899000'),(37,3,1,'XSCKD:20240518','2024-05-18 16:17:54.670000'),(38,4,1,'CKDBD:20240521','2024-05-21 11:11:08.152000'),(39,1,1,'CGRKD:20240521','2024-05-21 14:37:06.282000'),(42,11,1,'CBTZB:20240522','2024-05-22 15:21:18.816000'),(43,1,1,'CGRKD:20240522','2024-05-22 14:35:20.763000'),(46,5,4,'1002','2024-05-24 10:15:49.940000');
/*!40000 ALTER TABLE `s_code_seed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_custom_category_price`
--

DROP TABLE IF EXISTS `s_custom_category_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_custom_category_price` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customers_category_id` int DEFAULT NULL COMMENT '客户分类id',
  `merchant_id` int NOT NULL,
  `price` double DEFAULT NULL COMMENT '基础单位价格',
  `products_id` int DEFAULT NULL COMMENT '商品id',
  `unit_id` int DEFAULT NULL COMMENT '基础单位',
  `organization_id` int NOT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_products_customersCategory` (`merchant_id`,`products_id`,`customers_category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_custom_category_price`
--

LOCK TABLES `s_custom_category_price` WRITE;
/*!40000 ALTER TABLE `s_custom_category_price` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_custom_category_price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_customers`
--

DROP TABLE IF EXISTS `s_customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `customers_category_id` int DEFAULT NULL,
  `linkman` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `merchant_id` int NOT NULL COMMENT '商户ID',
  `organization_id` int NOT NULL COMMENT '组织ID',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `customers_level_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_customers_code` (`merchant_id`,`organization_id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_customers`
--

LOCK TABLES `s_customers` WRITE;
/*!40000 ALTER TABLE `s_customers` DISABLE KEYS */;
INSERT INTO `s_customers` VALUES (1,'222',NULL,NULL,NULL,0,0,NULL,NULL,0),(2,'测试','10010001',1,'联系人',1,4,'19882932924','remarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremarkremark',1),(3,'客户二','10010002',1,NULL,1,4,NULL,NULL,2),(4,'客户一','10010003',4,NULL,1,3,NULL,NULL,4),(5,'客户二','10010004',5,NULL,1,3,NULL,NULL,5),(6,'客户三','10010005',5,NULL,1,3,NULL,NULL,4),(7,'客户四','10010006',4,NULL,1,3,NULL,NULL,4),(8,'1','10010007',6,NULL,1,1,NULL,NULL,6);
/*!40000 ALTER TABLE `s_customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_customers_category`
--

DROP TABLE IF EXISTS `s_customers_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_customers_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `merchant_id` int NOT NULL COMMENT '商户ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int NOT NULL COMMENT '组织ID',
  `pid` int DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_customers_category_name` (`merchant_id`,`organization_id`,`name`),
  UNIQUE KEY `uc_customers_category_code` (`merchant_id`,`organization_id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_customers_category`
--

LOCK TABLES `s_customers_category` WRITE;
/*!40000 ALTER TABLE `s_customers_category` DISABLE KEYS */;
INSERT INTO `s_customers_category` VALUES (1,NULL,1,'客户分类一',4,NULL),(4,NULL,1,'客户分类一',3,NULL),(5,NULL,1,'客户分类二',3,NULL),(6,NULL,1,'1',1,NULL);
/*!40000 ALTER TABLE `s_customers_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_customers_level`
--

DROP TABLE IF EXISTS `s_customers_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_customers_level` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int NOT NULL COMMENT '商户ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int NOT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_customers_level_name` (`merchant_id`,`organization_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_customers_level`
--

LOCK TABLES `s_customers_level` WRITE;
/*!40000 ALTER TABLE `s_customers_level` DISABLE KEYS */;
INSERT INTO `s_customers_level` VALUES (6,1,'1',1),(4,1,'等级一',3),(5,1,'等级二',3),(1,1,'A等级',4),(2,1,'B等级',4);
/*!40000 ALTER TABLE `s_customers_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_customers_level_price`
--

DROP TABLE IF EXISTS `s_customers_level_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_customers_level_price` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customers_level_id` int DEFAULT NULL COMMENT '客户分类id',
  `merchant_id` int NOT NULL COMMENT '商户ID',
  `organization_id` int NOT NULL COMMENT '组织ID',
  `price` double DEFAULT NULL COMMENT '基础单位价格',
  `products_id` int DEFAULT NULL COMMENT '商品id',
  `unit_id` int DEFAULT NULL COMMENT '基础单位',
  `unit_price` json DEFAULT NULL COMMENT '客户单位辅助价格',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_products_customersCategory` (`merchant_id`,`products_id`,`customers_level_id`) USING BTREE,
  UNIQUE KEY `uc_customers_level_price` (`merchant_id`,`organization_id`,`products_id`,`customers_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_customers_level_price`
--

LOCK TABLES `s_customers_level_price` WRITE;
/*!40000 ALTER TABLE `s_customers_level_price` DISABLE KEYS */;
INSERT INTO `s_customers_level_price` VALUES (17,1,1,4,1,9,1,'[{\"num\": 1.0, \"price\": 3.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 5.0, \"unitId\": 3, \"unitName\": \"吨\", \"isDefault\": false}]'),(18,2,1,4,2,9,1,'[{\"num\": 1.0, \"price\": 4.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 6.0, \"unitId\": 3, \"unitName\": \"吨\", \"isDefault\": false}]'),(19,1,1,4,2,8,1,NULL),(20,2,1,4,3,8,1,NULL),(21,1,1,4,9,7,1,'[{\"num\": 1.0, \"price\": 12.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(22,2,1,4,8,7,1,'[{\"num\": 1.0, \"price\": 10.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(23,1,1,4,4,6,1,'[{\"num\": 1.0, \"price\": 8.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(24,2,1,4,5,6,1,'[{\"num\": 1.0, \"price\": 9.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(29,1,1,4,1,10,1,'[{\"num\": 1.0, \"price\": 8, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(30,2,1,4,2,10,1,'[{\"num\": 1.0, \"price\": 89, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(33,1,1,4,1,11,1,'[{\"num\": 1.0, \"price\": 3, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 5, \"unitId\": 3, \"unitName\": \"吨\", \"isDefault\": false}]'),(34,2,1,4,2,11,1,'[{\"num\": 1.0, \"price\": 4, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 6, \"unitId\": 3, \"unitName\": \"吨\", \"isDefault\": false}]'),(35,1,1,4,1,12,1,'[{\"num\": 1.0, \"price\": 6, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(36,2,1,4,2,12,1,'[{\"num\": 1.0, \"price\": 8, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]'),(37,4,1,3,5,13,9,'[{\"num\": 1.0, \"price\": 2, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]'),(38,5,1,3,8,13,9,'[{\"num\": 1.0, \"price\": 3, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]'),(39,4,1,3,2,14,9,'[{\"num\": 1.0, \"price\": 5, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 67, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(40,5,1,3,3,14,9,'[{\"num\": 1.0, \"price\": 6, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 78, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(41,4,1,3,2,15,9,'[{\"num\": 1.0, \"price\": 89, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(42,5,1,3,3,15,9,'[{\"num\": 1.0, \"price\": 99, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(43,4,1,3,3,16,12,'[{\"num\": 1.0, \"price\": 68, \"unitId\": 13, \"unitName\": \"件\", \"isDefault\": false}]'),(44,5,1,3,5,16,12,'[{\"num\": 1.0, \"price\": 76, \"unitId\": 13, \"unitName\": \"件\", \"isDefault\": false}]'),(45,4,1,3,56,17,13,'[{\"num\": 1.0, \"price\": 98, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(46,5,1,3,78,17,13,'[{\"num\": 1.0, \"price\": 123, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(47,4,1,3,10,18,9,'[{\"num\": 1.0, \"price\": 18, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]'),(48,5,1,3,12,18,9,'[{\"num\": 1.0, \"price\": 22, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]'),(49,4,1,3,30,19,9,'[{\"num\": 1.0, \"price\": 50, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 1200, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(50,5,1,3,40,19,9,'[{\"num\": 1.0, \"price\": 70, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1.0, \"price\": 1500, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]'),(51,6,1,1,2,20,15,NULL),(52,6,1,1,0,21,15,NULL),(53,6,1,1,3,22,15,NULL),(54,4,1,3,5,23,9,'[{\"num\": 1.0, \"price\": 8, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]'),(55,5,1,3,6,23,9,'[{\"num\": 1.0, \"price\": 9, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]');
/*!40000 ALTER TABLE `s_customers_level_price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_menu`
--

DROP TABLE IF EXISTS `s_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件',
  `enabled` bit(1) DEFAULT NULL COMMENT '是否启用',
  `icon_cls` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `menu_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `menu_module` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'MERCHANT',
  `menu_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int DEFAULT NULL COMMENT '父id',
  `pos` int DEFAULT NULL COMMENT '位置',
  `require_auth` bit(1) DEFAULT NULL COMMENT '是否要求权限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_menu`
--

LOCK TABLES `s_menu` WRITE;
/*!40000 ALTER TABLE `s_menu` DISABLE KEYS */;
INSERT INTO `s_menu` VALUES (1,'setting',_binary '','h-icon-setting','MERCHANT','MERCHANT','MENU','设置',NULL,10,_binary ''),(2,'basic',_binary '','h-icon-star','MERCHANT','MERCHANT','MENU','资料',NULL,0,_binary ''),(3,'SALE',_binary '','h-icon-complete','MERCHANT','MERCHANT','MENU','销售',NULL,1,_binary ''),(4,'CG',_binary '','h-icon-task','MERCHANT','MERCHANT','MENU','采购',NULL,2,_binary ''),(5,'SET',_binary '',NULL,'MERCHANT','MERCHANT','MENU','系统设置',1,0,_binary ''),(6,'MerchantInfo',_binary '',NULL,'MERCHANT','MERCHANT','MENU','企业信息',5,1,_binary ''),(7,'RoleList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','角色管理',5,3,_binary ''),(8,'AdminList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','用户管理',5,4,_binary ''),(22,'KC',_binary '','h-icon-search','MERCHANT','MERCHANT','MENU','库存',NULL,3,_binary ''),(23,'CheckoutList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','结账/反结账',5,5,_binary ''),(24,NULL,_binary '',NULL,'MERCHANT','MERCHANT','MENU','商品管理',2,0,_binary ''),(25,'ProductsCategoryList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','商品分类',24,0,_binary ''),(26,'ProductsList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','商品档案',24,1,_binary ''),(27,NULL,_binary '',NULL,'MERCHANT','MERCHANT','MENU','基础资料',2,1,_binary ''),(28,'CustomersList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','客户档案',27,0,_binary ''),(29,'VendorsList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','供货商档案',27,2,_binary ''),(30,'WarehousesList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','仓库档案',27,3,_binary ''),(31,'UnitsList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','计量单位',24,3,_binary ''),(32,NULL,_binary '',NULL,'MERCHANT','MERCHANT','MENU','采购管理',4,0,_binary ''),(33,'PurchaseOrderList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','采购入库单',32,0,_binary ''),(34,'PurchaseReturnList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','采购退货单',32,1,_binary ''),(35,NULL,_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售管理',3,0,_binary ''),(36,'SalesOrderList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售出库单',35,0,_binary ''),(37,'SalesReturnList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售退货单',35,1,_binary ''),(38,NULL,_binary '',NULL,'MERCHANT','MERCHANT','MENU','库存管理',22,0,_binary ''),(39,'StockInventoryList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','盘点单',38,0,_binary ''),(41,'StockTransferList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','调拨单',38,2,_binary ''),(42,NULL,_binary '',NULL,'MERCHANT','MERCHANT','MENU','库存报表',22,1,_binary ''),(43,'Stock',_binary '',NULL,'MERCHANT','MERCHANT','MENU','商品库存查询',42,0,_binary ''),(44,'StockDetail',_binary '',NULL,'MERCHANT','MERCHANT','MENU','进销存明细表',42,1,_binary ''),(45,'StockSummary',_binary '',NULL,'MERCHANT','MERCHANT','MENU','进销存汇总表',42,3,_binary ''),(46,'OrganizationList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','组织机构',5,2,_binary ''),(47,'StockOutboundList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','其他出库单',38,3,_binary ''),(48,'StockInboundList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','其他入库单',38,5,_binary ''),(49,'StockCostAdjustmentView',_binary '',NULL,'MERCHANT','MERCHANT','MENU','成本调整单',38,6,_binary ''),(51,NULL,_binary '',NULL,'MERCHANT','MERCHANT','MENU','辅助资料',2,4,_binary ''),(52,'CustomersLevelList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','客户等级',51,1,_binary ''),(54,'CustomersCategoryList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','客户分类',51,0,_binary ''),(55,'VendorsCategoryList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','货商分类',51,2,_binary ''),(56,'SalesReport',_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售报表',3,1,_binary ''),(57,'SalesDetail',_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售明细表',56,0,_binary ''),(58,'SalesSummary',_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售汇总表',56,1,_binary ''),(59,'PurchaseReport',_binary '',NULL,'MERCHANT','MERCHANT','MENU','采购报表',4,1,_binary ''),(60,'PurchaseDetail',_binary '',NULL,'MERCHANT','MERCHANT','MENU','采购明细表',59,0,_binary ''),(61,'PurchaseSummary',_binary '',NULL,'MERCHANT','MERCHANT','MENU','采购汇总表',59,1,_binary ''),(62,'SalesProfitList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售利润表',56,2,_binary ''),(63,'SalesRankingsList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','销售排行表',56,3,_binary ''),(64,'StockCostAdjustmentList',_binary '',NULL,'MERCHANT','MERCHANT','MENU','成本调整单列表',42,4,_binary ''),(65,'ArgsSetting',_binary '',NULL,'MERCHANT','MERCHANT','MENU','系统参数',5,0,_binary '');
/*!40000 ALTER TABLE `s_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_menu_role`
--

DROP TABLE IF EXISTS `s_menu_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_menu_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `menu_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_menu_role`
--

LOCK TABLES `s_menu_role` WRITE;
/*!40000 ALTER TABLE `s_menu_role` DISABLE KEYS */;
INSERT INTO `s_menu_role` VALUES (1,1,2),(2,2,2),(3,3,2),(4,4,2),(5,5,2),(6,6,2),(7,7,2),(8,8,2),(9,22,2),(10,23,2),(11,24,2),(12,25,2),(13,26,2),(14,27,2),(15,28,2),(16,29,2),(17,30,2),(18,31,2),(19,32,2),(20,33,2),(21,34,2),(22,35,2),(23,36,2),(24,37,2),(25,38,2),(26,39,2),(27,41,2),(28,42,2),(29,43,2),(30,44,2),(31,45,2);
/*!40000 ALTER TABLE `s_menu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_merchant`
--

DROP TABLE IF EXISTS `s_merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_merchant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地址',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户编码',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `enabled` bit(1) DEFAULT NULL COMMENT '是否启用',
  `linkman` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '电话号码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户名称',
  `service_end_date` date DEFAULT NULL COMMENT '服务结束时间',
  `service_start_date` date DEFAULT NULL COMMENT '服务开始时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_msw6o9t7566sbb0ltmow8lo0j` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_merchant`
--

LOCK TABLES `s_merchant` WRITE;
/*!40000 ALTER TABLE `s_merchant` DISABLE KEYS */;
INSERT INTO `s_merchant` VALUES (1,'杭州','1001','2024-04-23 13:02:12.735894',NULL,_binary '','李泽龙','13944878765','纷析云',NULL,NULL),(4,NULL,'1002','2024-05-24 09:30:21.004475',NULL,_binary '','王五','12345678909','1002','2024-12-01','2024-05-01');
/*!40000 ALTER TABLE `s_merchant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_merchant_menu`
--

DROP TABLE IF EXISTS `s_merchant_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_merchant_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `menu_id` int NOT NULL,
  `merchant_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=606 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_merchant_menu`
--

LOCK TABLES `s_merchant_menu` WRITE;
/*!40000 ALTER TABLE `s_merchant_menu` DISABLE KEYS */;
INSERT INTO `s_merchant_menu` VALUES (508,1,1),(509,2,1),(510,3,1),(511,4,1),(512,5,1),(513,6,1),(514,7,1),(515,8,1),(516,22,1),(517,23,1),(518,24,1),(519,25,1),(520,26,1),(521,27,1),(522,28,1),(523,29,1),(524,30,1),(525,31,1),(526,32,1),(527,33,1),(528,34,1),(529,35,1),(530,36,1),(531,37,1),(532,38,1),(533,39,1),(534,41,1),(535,42,1),(536,43,1),(537,44,1),(538,45,1),(539,46,1),(540,47,1),(541,48,1),(542,49,1),(543,51,1),(544,52,1),(545,54,1),(546,55,1),(547,56,1),(548,57,1),(549,58,1),(550,59,1),(551,60,1),(552,61,1),(553,62,1),(554,63,1),(555,64,1),(556,65,1),(557,1,4),(558,2,4),(559,3,4),(560,4,4),(561,5,4),(562,6,4),(563,7,4),(564,8,4),(565,22,4),(566,23,4),(567,24,4),(568,25,4),(569,26,4),(570,27,4),(571,28,4),(572,29,4),(573,30,4),(574,31,4),(575,32,4),(576,33,4),(577,34,4),(578,35,4),(579,36,4),(580,37,4),(581,38,4),(582,39,4),(583,41,4),(584,42,4),(585,43,4),(586,44,4),(587,45,4),(588,46,4),(589,47,4),(590,48,4),(591,49,4),(592,51,4),(593,52,4),(594,54,4),(595,55,4),(596,56,4),(597,57,4),(598,58,4),(599,59,4),(600,60,4),(601,61,4),(602,62,4),(603,63,4),(604,64,4),(605,65,4);
/*!40000 ALTER TABLE `s_merchant_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_merchant_user`
--

DROP TABLE IF EXISTS `s_merchant_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_merchant_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) DEFAULT NULL COMMENT '是否启用',
  `last_login_date` datetime(6) DEFAULT NULL COMMENT '最后登录时间',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `system_default` bit(1) DEFAULT NULL COMMENT '是否系统默认',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_o7sn18pnb5rxs77lrf47krklc` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_merchant_user`
--

LOCK TABLES `s_merchant_user` WRITE;
/*!40000 ALTER TABLE `s_merchant_user` DISABLE KEYS */;
INSERT INTO `s_merchant_user` VALUES (1,_binary '','2024-04-23 14:45:13.736669','李泽龙','$2a$10$Ove9QzdJWqMWRU7Wv3l2C.qy6HqMud1ryeNUvTgDuyBx.PINjcQbK',_binary '','13944878765');
/*!40000 ALTER TABLE `s_merchant_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_order`
--

DROP TABLE IF EXISTS `s_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_date` date DEFAULT NULL COMMENT '单据日期',
  `business_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务类型',
  `check_id` bigint DEFAULT NULL COMMENT '审核人',
  `check_out_time` datetime(6) DEFAULT NULL COMMENT '审核时间',
  `code` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `customers_id` bigint DEFAULT NULL COMMENT '客户ID',
  `discount_amount` decimal(38,2) DEFAULT NULL COMMENT '优惠金额',
  `discounted_amount` decimal(38,2) DEFAULT NULL COMMENT '折后金额',
  `in_warehouse_id` bigint DEFAULT NULL COMMENT '调拨入库仓库ID',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `order_status` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '已保存' COMMENT '订单状态',
  `order_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单类型',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `out_warehouse_id` bigint DEFAULT NULL COMMENT '调拨出库仓库ID',
  `remark` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `total_amount` decimal(38,2) DEFAULT NULL COMMENT '订单原价总额',
  `user_id` bigint DEFAULT NULL COMMENT '创建人',
  `vendors_id` bigint DEFAULT NULL COMMENT '供货商ID',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `cost` decimal(38,2) DEFAULT NULL COMMENT '销售成本',
  `unit_quantity` decimal(38,2) DEFAULT NULL COMMENT '单位总数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK13jpp4fomvkkl8be08sghwx5n` (`code`,`merchant_id`),
  UNIQUE KEY `UK_m1q30v0p45cp1t8lj4egljjuo` (`code`),
  UNIQUE KEY `UK81wc8gageuw4na67o3nxbsqtw` (`code`,`merchant_id`,`organization_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_order`
--

LOCK TABLES `s_order` WRITE;
/*!40000 ALTER TABLE `s_order` DISABLE KEYS */;
INSERT INTO `s_order` VALUES (1,'2024-05-14',NULL,NULL,NULL,'XSCKD100120240514004','2024-05-14 15:04:44.819308',2,NULL,1692.00,NULL,1,'已审核','销售出库单',4,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(2,'2024-05-14',NULL,NULL,NULL,'XSCKD100120240514005','2024-05-14 15:12:26.710656',3,NULL,732.00,NULL,1,'已审核','销售出库单',4,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(3,'2024-05-14',NULL,NULL,NULL,'CKDBD100120240514003','2024-05-14 15:13:03.278415',NULL,NULL,0.00,1,1,'已审核','调拨单',4,2,NULL,NULL,1,NULL,NULL,NULL,NULL),(4,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515001','2024-05-15 14:56:33.666062',NULL,NULL,6115.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(5,'2024-05-15',NULL,1,'2024-05-15 14:57:49.003867','XSCKD100120240515001','2024-05-15 14:57:43.224100',4,NULL,112.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(6,'2024-05-15',NULL,1,'2024-05-15 15:05:48.962972','XSCKD100120240515002','2024-05-15 15:02:18.269770',4,NULL,168.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(7,'2024-05-15',NULL,1,'2024-05-15 15:23:36.587392','XSCKD100120240515003','2024-05-15 15:23:30.606503',4,NULL,3136.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(8,'2024-05-15',NULL,1,'2024-05-15 15:24:14.909953','XSCKD100120240515004','2024-05-15 15:24:10.814096',4,NULL,1861.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(9,'2024-05-15',NULL,1,'2024-05-15 15:28:02.928865','XSCKD100120240515005','2024-05-15 15:27:58.004082',4,NULL,1904.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(10,'2024-05-15',NULL,1,'2024-05-15 15:29:02.176748','XSCKD100120240515006','2024-05-15 15:28:57.781248',4,NULL,2520.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(11,'2024-05-15',NULL,1,'2024-05-15 15:30:27.557473','XSCKD100120240515007','2024-05-15 15:29:45.235984',4,NULL,1288.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(12,'2024-05-15',NULL,1,'2024-05-15 15:31:31.279962','XSCKD100120240515008','2024-05-15 15:30:40.787573',4,NULL,1904.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(13,'2024-05-15',NULL,1,'2024-05-15 15:32:51.823747','XSCKD100120240515009','2024-05-15 15:32:00.412317',4,NULL,4368.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(14,'2024-05-15',NULL,1,'2024-05-15 15:34:19.583344','XSCKD100120240515010','2024-05-15 15:33:02.823826',4,NULL,225.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(15,'2024-05-15',NULL,1,'2024-05-15 15:36:59.514537','XSCKD100120240515011','2024-05-15 15:34:42.835501',4,NULL,2520.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(16,'2024-05-15',NULL,1,'2024-05-15 15:42:12.453467','XSCKD100120240515012','2024-05-15 15:37:09.587631',4,NULL,1288.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(17,'2024-05-15',NULL,1,'2024-05-15 15:43:34.529425','XSCKD100120240515013','2024-05-15 15:42:49.420712',4,NULL,1680.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(18,'2024-05-15',NULL,1,'2024-05-15 15:50:22.637160','XSCKD100120240515014','2024-05-15 15:50:19.014335',5,NULL,4368.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,93.52,NULL),(19,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515002','2024-05-15 15:55:49.201125',NULL,NULL,40020.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(20,'2024-05-15',NULL,1,'2024-05-15 15:57:52.087881','XSCKD100120240515015','2024-05-15 15:57:38.500637',4,NULL,46008.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,25605.00,NULL),(21,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515003','2024-05-15 15:59:53.387538',NULL,NULL,432.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(22,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515004','2024-05-15 16:00:20.330988',NULL,NULL,670.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(23,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515005','2024-05-15 16:00:31.199266',NULL,NULL,12.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(24,'2024-05-15',NULL,1,'2024-05-15 16:01:09.417760','XSCKD100120240515016','2024-05-15 16:01:05.423980',4,NULL,9000.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,3376.00,NULL),(25,'2024-05-15',NULL,1,'2024-05-15 16:04:42.577082','XSCKD100120240515017','2024-05-15 16:04:38.574018',4,NULL,2.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,8.00,NULL),(26,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515006','2024-05-15 16:10:31.527849',NULL,NULL,525.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,4,NULL,NULL,NULL),(27,'2024-05-15',NULL,1,'2024-05-15 16:10:57.410150','XSCKD100120240515018','2024-05-15 16:10:53.146311',4,NULL,3500.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,300.00,NULL),(28,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515007','2024-05-15 16:14:24.758809',NULL,NULL,13.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(29,'2024-05-15',NULL,1,'2024-05-15 16:16:46.629267','XSCKD100120240515019','2024-05-15 16:14:41.015468',5,NULL,25.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,30.00,NULL),(30,'2024-05-15',NULL,1,'2024-05-15 16:18:34.482492','XSCKD100120240515020','2024-05-15 16:18:10.697729',4,NULL,3900.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,380.00,NULL),(31,'2024-05-15',NULL,1,'2024-05-15 16:22:57.538170','XSCKD100120240515021','2024-05-15 16:18:20.948619',4,NULL,18.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,16.00,NULL),(32,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515008','2024-05-15 16:25:18.149341',NULL,NULL,30.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(33,'2024-05-15',NULL,1,'2024-05-15 16:31:09.187783','XSCKD100120240515022','2024-05-15 16:28:54.483931',5,NULL,75.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,12.00,NULL),(34,'2024-05-15',NULL,NULL,NULL,'CGRKD100120240515009','2024-05-15 16:31:49.864031',NULL,NULL,51.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(35,'2024-05-15',NULL,1,'2024-05-15 16:32:40.321275','XSCKD100120240515023','2024-05-15 16:32:17.844494',6,NULL,18.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,42.00,NULL),(36,'2024-05-16',NULL,NULL,NULL,'CGRKD100120240516001','2024-05-16 10:49:19.995752',NULL,NULL,12250.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(37,'2024-05-16',NULL,1,'2024-05-16 11:51:26.836218','XSCKD100120240516001','2024-05-16 11:51:22.964448',4,NULL,1500.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,1162.00,NULL),(38,'2024-05-16',NULL,1,'2024-05-16 11:56:05.390590','XSCKD100120240516002','2024-05-16 11:56:01.016852',6,NULL,12000.00,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,9572.00,NULL),(39,'2024-05-16',NULL,NULL,NULL,'XSCKD100120240516003','2024-05-16 14:35:51.197995',4,NULL,24240.00,NULL,1,'已保存','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,NULL,2425.00),(40,'2024-05-16',NULL,1,'2024-05-17 16:22:37.349306','XSCKD100120240516004','2024-05-16 14:37:11.775794',4,52.18,1424.82,NULL,1,'已审核','销售出库单',3,NULL,NULL,NULL,1,NULL,NULL,185.60,964.00),(41,'2024-05-08',NULL,NULL,NULL,'CGRKD100120240517001','2024-05-17 11:29:00.142198',NULL,NULL,12000.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(45,'2024-02-10',NULL,NULL,NULL,'CGRKD100120240517005','2024-05-17 15:34:33.377188',NULL,NULL,12000.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(46,'2024-02-03',NULL,NULL,NULL,'CGRKD100120240517006','2024-05-17 15:34:51.281748',NULL,NULL,0.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(47,'2024-02-10',NULL,NULL,NULL,'CGRKD100120240517007','2024-05-17 15:41:33.267372',NULL,NULL,12000.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(48,'2024-05-17',NULL,NULL,NULL,'CGRKD100120240517008','2024-05-17 16:13:07.684680',NULL,NULL,5472.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(49,'2024-05-17',NULL,NULL,NULL,'CGRKD100120240517009','2024-05-17 16:14:37.564255',NULL,NULL,3648.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(50,'2024-05-17',NULL,NULL,NULL,'CGRKD100120240517010','2024-05-17 16:19:34.861311',NULL,NULL,20.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(51,'2024-05-18',NULL,1,'2024-05-18 08:21:17.899333','XSCKD100120240518001','2024-05-18 08:21:13.146494',8,0.00,2.00,NULL,1,'已审核','销售出库单',1,NULL,NULL,NULL,1,NULL,NULL,0.00,1.00),(52,'2024-05-18',NULL,NULL,NULL,'XSCKD100120240518002','2024-05-18 16:17:28.024649',3,0.00,13.00,NULL,1,'已保存','销售出库单',4,NULL,NULL,NULL,1,NULL,NULL,NULL,2.00),(53,'2024-05-18',NULL,NULL,NULL,'XSCKD100120240518003','2024-05-18 16:17:54.665305',2,0.00,13.00,NULL,1,'已保存','销售出库单',4,NULL,NULL,NULL,1,NULL,NULL,NULL,2.00),(54,'2024-05-21',NULL,1,'2024-05-21 10:51:24.153657','CKDBD100120240521001','2024-05-21 10:42:05.692219',NULL,NULL,0.00,8,1,'已审核','调拨单',1,9,NULL,NULL,1,NULL,NULL,0.00,NULL),(55,'2024-05-21',NULL,1,'2024-05-21 11:13:02.314930','CKDBD100120240521002','2024-05-21 10:55:46.999787',NULL,NULL,0.00,8,1,'已审核','调拨单',1,9,NULL,NULL,1,NULL,NULL,0.00,NULL),(56,'2024-05-21',NULL,1,'2024-05-21 11:10:03.898562','CKDBD100120240521003','2024-05-21 11:10:00.385052',NULL,NULL,0.00,8,1,'已审核','调拨单',1,9,NULL,NULL,1,NULL,NULL,0.00,NULL),(57,'2024-05-21',NULL,1,'2024-05-21 11:11:11.980006','CKDBD100120240521004','2024-05-21 11:11:08.148326',NULL,NULL,0.00,8,1,'已审核','调拨单',1,9,NULL,NULL,1,NULL,NULL,0.00,NULL),(58,'2024-05-21',NULL,NULL,NULL,'CGRKD100120240521001','2024-05-21 14:37:06.285153',NULL,NULL,0.00,NULL,1,'已保存','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL),(59,'2024-05-22',NULL,NULL,NULL,'CGRKD100120240522001','2024-05-22 14:35:20.770209',NULL,NULL,61050.00,NULL,1,'已审核','采购入库单',3,NULL,NULL,NULL,1,3,NULL,NULL,NULL);
/*!40000 ALTER TABLE `s_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_order_detail`
--

DROP TABLE IF EXISTS `s_order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `batch_date` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '批次号',
  `bill_date` date DEFAULT NULL COMMENT '单据日期',
  `discount` double NOT NULL DEFAULT '100' COMMENT '折扣率',
  `discount_amount` decimal(38,2) DEFAULT NULL COMMENT '优惠金额',
  `discounted_amount` decimal(38,2) DEFAULT NULL COMMENT '折后金额',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `num` double DEFAULT NULL COMMENT '基本数量比例',
  `order_id` bigint DEFAULT NULL COMMENT '订单主表ID',
  `order_price` decimal(38,2) DEFAULT NULL COMMENT '单价',
  `order_quantity` decimal(38,2) DEFAULT NULL COMMENT '下单数量',
  `order_unit_id` bigint DEFAULT NULL COMMENT '采购单位ID',
  `order_unit_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '采购单位名称',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `products_id` bigint DEFAULT NULL COMMENT '产品ID',
  `remark` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `stock_type` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '仓库类型',
  `sys_quantity` decimal(38,2) DEFAULT NULL COMMENT '系统数量,盘点使用',
  `unit_id` bigint DEFAULT NULL COMMENT '基础单位ID',
  `unit_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '基础单位名称',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `cost` decimal(38,2) DEFAULT NULL COMMENT '成本',
  `out_quantity` decimal(38,2) DEFAULT NULL COMMENT '出库数量',
  `unit_cost` decimal(38,2) DEFAULT NULL COMMENT '单位成本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_order_detail`
--

LOCK TABLES `s_order_detail` WRITE;
/*!40000 ALTER TABLE `s_order_detail` DISABLE KEYS */;
INSERT INTO `s_order_detail` VALUES (1,NULL,NULL,0,0.00,136.00,1,1,1,4.00,34.00,1,'斤',4,6,'','减',34.00,1,'斤',2,NULL,NULL,NULL),(2,NULL,NULL,0,0.00,360.00,1,2,1,8.00,45.00,2,'千克',4,6,'','减',90.00,1,'斤',2,NULL,NULL,NULL),(3,NULL,NULL,0,0.00,672.00,1,2,1,12.00,56.00,2,'千克',4,7,'','减',112.00,1,'斤',2,NULL,NULL,NULL),(4,NULL,NULL,0,0.00,134.00,1,1,1,2.00,67.00,1,'斤',4,8,'','减',67.00,1,'斤',2,NULL,NULL,NULL),(5,NULL,NULL,0,0.00,390.00,1,1000,1,5.00,78.00,3,'吨',4,9,'','减',78000.00,1,'斤',2,NULL,NULL,NULL),(6,NULL,NULL,0,0.00,170.00,1,1,2,5.00,34.00,1,'斤',4,6,'','减',34.00,1,'斤',1,NULL,NULL,NULL),(7,NULL,NULL,0,0.00,360.00,1,1,2,8.00,45.00,1,'斤',4,7,'','减',45.00,1,'斤',2,NULL,NULL,NULL),(8,NULL,NULL,0,0.00,134.00,1,1,2,2.00,67.00,1,'斤',4,10,'','减',67.00,1,'斤',2,NULL,NULL,NULL),(9,NULL,NULL,0,0.00,68.00,1,1,2,2.00,34.00,1,'斤',4,11,'','减',34.00,1,'斤',1,NULL,NULL,NULL),(10,NULL,NULL,0,0.00,0.00,1,1,3,0.00,3435.00,1,'斤',4,6,'','平',3435.00,1,'斤',1,NULL,NULL,NULL),(11,NULL,NULL,0,0.00,0.00,1,500,3,0.00,456.00,3,'吨',4,11,'','平',228000.00,1,'斤',1,NULL,NULL,NULL),(12,NULL,NULL,0,0.00,0.00,1,2,3,0.00,456.00,2,'千克',4,10,'','平',912.00,1,'斤',1,NULL,NULL,NULL),(13,NULL,NULL,0,0.00,115.00,1,3,4,5.00,23.00,11,'吨',3,17,'','加',69.00,13,'件',5,NULL,NULL,NULL),(14,NULL,NULL,0,0.00,6000.00,1,1,4,6.00,1000.00,12,'个',3,16,'','加',1000.00,12,'个',7,NULL,NULL,NULL),(15,NULL,NULL,0,0.00,112.00,1,1,5,56.00,2.00,13,'件',3,17,'','减',2.00,13,'件',7,NULL,NULL,NULL),(16,NULL,NULL,0,0.00,168.00,1,1,6,56.00,3.00,13,'件',3,17,'','减',3.00,13,'件',5,NULL,NULL,NULL),(17,NULL,NULL,0,0.00,3136.00,1,1,7,56.00,56.00,13,'件',3,17,'','减',56.00,13,'件',5,NULL,NULL,NULL),(18,NULL,NULL,0,0.00,1792.00,1,1,8,56.00,32.00,13,'件',3,17,'','减',32.00,13,'件',5,NULL,NULL,NULL),(19,NULL,NULL,0,0.00,69.00,1,1,8,3.00,23.00,12,'个',3,16,'','减',23.00,12,'个',7,NULL,NULL,NULL),(20,NULL,NULL,0,0.00,1904.00,1,1,9,56.00,34.00,13,'件',3,17,'','减',34.00,13,'件',5,NULL,NULL,NULL),(21,NULL,NULL,0,0.00,2520.00,1,1,10,56.00,45.00,13,'件',3,17,'','减',45.00,13,'件',5,NULL,NULL,NULL),(22,NULL,NULL,0,0.00,1288.00,1,1,11,56.00,23.00,13,'件',3,17,'','减',23.00,13,'件',5,NULL,NULL,NULL),(23,NULL,NULL,0,0.00,1904.00,1,1,12,56.00,34.00,13,'件',3,17,'','减',34.00,13,'件',5,NULL,NULL,NULL),(24,NULL,NULL,0,0.00,4368.00,1,1,13,56.00,78.00,13,'件',3,17,'','减',78.00,13,'件',5,NULL,NULL,NULL),(25,NULL,NULL,0,0.00,225.00,1,1,14,5.00,45.00,9,'斤',3,13,'','减',45.00,9,'斤',5,NULL,NULL,NULL),(26,NULL,NULL,0,0.00,2520.00,1,1,15,56.00,45.00,13,'件',3,17,'','减',45.00,13,'件',5,NULL,NULL,NULL),(27,NULL,NULL,0,0.00,1288.00,1,NULL,16,56.00,23.00,13,'件',3,17,NULL,'减',23.00,13,'件',5,NULL,NULL,NULL),(28,NULL,NULL,0,0.00,1680.00,1,1,17,56.00,30.00,13,'件',3,17,'','减',30.00,13,'件',5,NULL,NULL,NULL),(29,NULL,NULL,0,0.00,4368.00,1,1,18,78.00,56.00,13,'件',3,17,'','减',56.00,13,'件',5,NULL,NULL,NULL),(30,NULL,NULL,0,0.00,2070.00,1,1,19,6.00,345.00,12,'个',3,16,'','加',345.00,12,'个',7,NULL,NULL,NULL),(31,NULL,NULL,0,0.00,30408.00,1,1,19,56.00,543.00,13,'件',3,17,'','加',543.00,13,'件',7,NULL,NULL,NULL),(32,NULL,NULL,0,0.00,2280.00,1,1,19,5.00,456.00,9,'斤',3,15,'','加',456.00,9,'斤',7,NULL,NULL,NULL),(33,NULL,NULL,0,0.00,2070.00,1,1,19,6.00,345.00,9,'斤',3,14,'','加',345.00,9,'斤',7,NULL,NULL,NULL),(34,NULL,NULL,0,0.00,3192.00,1,1,19,7.00,456.00,9,'斤',3,13,'','加',456.00,9,'斤',7,NULL,NULL,NULL),(35,NULL,NULL,0,0.00,5.00,1,NULL,20,5.00,1.00,9,'斤',3,13,NULL,'减',1.00,9,'斤',7,NULL,NULL,NULL),(36,NULL,NULL,0,0.00,3.00,1,NULL,20,3.00,1.00,12,'个',3,16,NULL,'减',1.00,12,'个',7,NULL,NULL,NULL),(37,NULL,NULL,0,0.00,46000.00,1,NULL,20,46.00,1000.00,13,'件',3,17,NULL,'减',1000.00,13,'件',7,NULL,NULL,NULL),(38,NULL,NULL,0,0.00,432.00,1,1,21,8.00,54.00,9,'斤',3,15,'','加',54.00,9,'斤',7,NULL,NULL,NULL),(39,NULL,NULL,0,0.00,670.00,1,1,22,10.00,67.00,9,'斤',3,15,'','加',67.00,9,'斤',7,NULL,NULL,NULL),(40,NULL,NULL,0,0.00,12.00,1,1,23,12.00,1.00,9,'斤',3,15,'','加',1.00,9,'斤',7,NULL,NULL,NULL),(41,NULL,NULL,0,0.00,9000.00,1,1,24,9.00,1000.00,9,'斤',3,15,'','减',1000.00,9,'斤',7,NULL,NULL,NULL),(42,NULL,NULL,0,0.00,2.00,1,1,25,2.00,1.00,9,'斤',3,15,'','减',1.00,9,'斤',7,NULL,NULL,NULL),(43,NULL,NULL,0,0.00,25.00,1,1,26,5.00,5.00,12,'个',3,16,'','加',5.00,12,'个',7,NULL,NULL,NULL),(44,NULL,NULL,0,0.00,100.00,1,1,26,10.00,10.00,12,'个',3,16,'','加',10.00,12,'个',7,NULL,NULL,NULL),(45,NULL,NULL,0,0.00,400.00,1,1,26,20.00,20.00,12,'个',3,16,'','加',20.00,12,'个',7,NULL,NULL,NULL),(46,NULL,NULL,0,0.00,3500.00,1,1,27,70.00,50.00,12,'个',3,16,'','减',50.00,12,'个',7,NULL,NULL,NULL),(47,NULL,NULL,0,0.00,1.00,1,1,28,1.00,1.00,12,'个',3,16,'','加',1.00,12,'个',7,NULL,NULL,NULL),(48,NULL,NULL,0,0.00,4.00,1,1,28,4.00,1.00,12,'个',3,16,'','加',1.00,12,'个',7,NULL,NULL,NULL),(49,NULL,NULL,0,0.00,8.00,1,1,28,8.00,1.00,12,'个',3,16,'','加',1.00,12,'个',7,NULL,NULL,NULL),(50,NULL,NULL,0,0.00,25.00,1,1,29,5.00,5.00,12,'个',3,16,'','减',5.00,12,'个',7,NULL,NULL,NULL),(51,NULL,NULL,0,0.00,3900.00,1,1,30,3.00,1300.00,12,'个',3,16,'','减',1300.00,12,'个',7,NULL,NULL,NULL),(52,NULL,NULL,0,0.00,18.00,1,1,31,3.00,6.00,12,'个',3,16,'','减',6.00,12,'个',7,NULL,NULL,NULL),(53,NULL,NULL,0,0.00,3.00,1,1,32,1.00,3.00,12,'个',3,16,'','加',3.00,12,'个',7,NULL,NULL,NULL),(54,NULL,NULL,0,0.00,6.00,1,1,32,2.00,3.00,12,'个',3,16,'','加',3.00,12,'个',7,NULL,NULL,NULL),(55,NULL,NULL,0,0.00,9.00,1,1,32,3.00,3.00,12,'个',3,16,'','加',3.00,12,'个',7,NULL,NULL,NULL),(56,NULL,NULL,0,0.00,12.00,1,1,32,4.00,3.00,12,'个',3,16,'','加',3.00,12,'个',7,NULL,NULL,NULL),(57,NULL,NULL,0,0.00,75.00,1,1,33,5.00,15.00,12,'个',3,16,'','减',15.00,12,'个',7,NULL,NULL,NULL),(58,NULL,NULL,0,0.00,30.00,1,1,34,5.00,6.00,12,'个',3,16,'','加',6.00,12,'个',7,NULL,NULL,NULL),(59,NULL,NULL,0,0.00,6.00,1,1,34,6.00,1.00,12,'个',3,16,'','加',1.00,12,'个',7,NULL,NULL,NULL),(60,NULL,NULL,0,0.00,7.00,1,1,34,7.00,1.00,12,'个',3,16,'','加',1.00,12,'个',7,NULL,NULL,NULL),(61,NULL,NULL,0,0.00,8.00,1,1,34,8.00,1.00,12,'个',3,16,'','加',1.00,12,'个',7,NULL,NULL,NULL),(62,NULL,NULL,0,0.00,18.00,1,1,35,3.00,6.00,12,'个',3,16,'','减',6.00,12,'个',7,NULL,NULL,NULL),(63,NULL,NULL,0,0.00,200.00,1,1,36,20.00,10.00,9,'斤',3,19,'','加',10.00,9,'斤',7,NULL,NULL,NULL),(64,NULL,NULL,0,0.00,50.00,1,2,36,50.00,1.00,10,'千克',3,19,'','加',2.00,9,'斤',7,NULL,NULL,NULL),(65,NULL,NULL,0,0.00,12000.00,1,500,36,12000.00,1.00,11,'吨',3,19,'','加',500.00,9,'斤',7,NULL,NULL,NULL),(66,NULL,NULL,0,0.00,1500.00,1,1,37,30.00,50.00,9,'斤',3,19,'','减',50.00,9,'斤',7,NULL,NULL,NULL),(67,NULL,NULL,0,0.00,12000.00,1,1,38,30.00,400.00,9,'斤',3,19,'','减',400.00,9,'斤',7,NULL,NULL,NULL),(68,NULL,NULL,0,0.00,690.00,1,2,39,2.00,345.00,10,'千克',3,13,'','减',690.00,9,'斤',5,NULL,NULL,NULL),(69,NULL,NULL,0,0.00,23460.00,1,5,39,68.00,345.00,13,'件',3,16,'','减',1725.00,12,'个',7,NULL,NULL,NULL),(70,NULL,NULL,0,0.00,90.00,1,2,39,18.00,5.00,10,'千克',3,18,'','减',10.00,9,'斤',7,NULL,NULL,NULL),(71,NULL,NULL,34,4.08,7.92,1,NULL,40,3.00,4.00,12,'个',3,16,NULL,'减',4.00,12,'个',7,NULL,NULL,NULL),(72,NULL,NULL,45,3.60,4.40,1,NULL,40,2.00,4.00,9,'斤',3,15,NULL,'减',4.00,9,'斤',7,NULL,NULL,NULL),(73,NULL,NULL,50,44.50,44.50,1,NULL,40,89.00,1.00,11,'吨',3,15,NULL,'减',500.00,9,'斤',7,NULL,NULL,NULL),(74,NULL,NULL,0,0.00,1368.00,1,1,40,3.00,456.00,12,'个',3,16,'','减',456.00,12,'个',7,NULL,NULL,NULL),(75,NULL,NULL,0,0.00,12000.00,1,1,41,12000.00,1.00,9,'斤',3,19,'','加',1.00,9,'斤',7,NULL,NULL,NULL),(81,NULL,NULL,0,0.00,12000.00,1,1,45,12000.00,1.00,9,'斤',3,19,'','加',1.00,9,'斤',7,NULL,NULL,NULL),(82,NULL,NULL,0,0.00,0.00,1,1,46,0.00,1.00,9,'斤',3,18,'','加',1.00,9,'斤',7,NULL,NULL,NULL),(83,NULL,NULL,0,0.00,12000.00,1,1,47,12000.00,1.00,9,'斤',3,19,'','加',1.00,9,'斤',7,NULL,NULL,NULL),(84,NULL,NULL,0,0.00,5472.00,1,1,48,12.00,456.00,9,'斤',3,15,'','加',456.00,9,'斤',7,NULL,NULL,NULL),(85,NULL,NULL,0,0.00,3648.00,1,1,49,8.00,456.00,12,'个',3,16,'','加',456.00,12,'个',7,NULL,NULL,NULL),(86,NULL,NULL,0,0.00,8.00,1,1,50,8.00,1.00,12,'个',3,16,'','加',1.00,12,'个',7,NULL,NULL,NULL),(87,NULL,NULL,0,0.00,12.00,1,1,50,12.00,1.00,9,'斤',3,15,'','加',1.00,9,'斤',7,NULL,NULL,NULL),(88,NULL,NULL,0,0.00,2.00,1,1,51,2.00,1.00,15,'斤',1,20,'','减',1.00,15,'斤',8,NULL,NULL,NULL),(89,NULL,NULL,0,0.00,5.00,1,1,52,5.00,1.00,1,'斤',4,6,'','减',1.00,1,'斤',2,NULL,NULL,NULL),(90,NULL,NULL,0,0.00,8.00,1,1,52,8.00,1.00,1,'斤',4,7,'','减',1.00,1,'斤',2,NULL,NULL,NULL),(91,NULL,NULL,0,0.00,4.00,1,1,53,4.00,1.00,1,'斤',4,6,'','减',1.00,1,'斤',2,NULL,NULL,NULL),(92,NULL,NULL,0,0.00,9.00,1,1,53,9.00,1.00,1,'斤',4,7,'','减',1.00,1,'斤',2,NULL,NULL,NULL),(93,NULL,NULL,0,0.00,0.00,1,1,54,0.00,45.00,15,'斤',1,20,'','平',45.00,15,'斤',8,NULL,NULL,NULL),(94,NULL,NULL,0,0.00,0.00,1,1,55,0.00,100.00,15,'斤',1,20,'','平',100.00,15,'斤',8,NULL,NULL,NULL),(95,NULL,NULL,0,0.00,0.00,1,1,56,0.00,4.00,15,'斤',1,21,'','平',4.00,15,'斤',8,NULL,NULL,NULL),(96,NULL,NULL,0,0.00,0.00,1,1,57,0.00,45.00,15,'斤',1,22,'','平',45.00,15,'斤',8,NULL,NULL,NULL),(97,NULL,NULL,0,0.00,0.00,1,1,58,0.00,1.00,9,'斤',3,18,'','加',1.00,9,'斤',7,NULL,NULL,NULL),(98,NULL,NULL,0,0.00,50.00,1,1,59,5.00,10.00,9,'斤',3,23,'','加',10.00,9,'斤',7,NULL,NULL,NULL),(99,NULL,NULL,0,0.00,500.00,1,1,59,10.00,50.00,9,'斤',3,23,'','加',50.00,9,'斤',7,NULL,NULL,NULL),(100,NULL,NULL,0,0.00,500.00,1,1,59,5.00,100.00,9,'斤',3,23,'','加',100.00,9,'斤',7,NULL,NULL,NULL),(101,NULL,NULL,0,0.00,5000.00,1,2,59,10.00,500.00,10,'千克',3,23,'','加',1000.00,9,'斤',7,NULL,NULL,NULL),(102,NULL,NULL,0,0.00,15000.00,1,2,59,15.00,1000.00,10,'千克',3,23,'','加',2000.00,9,'斤',7,NULL,NULL,NULL),(103,NULL,NULL,0,0.00,40000.00,1,2,59,20.00,2000.00,10,'千克',3,23,'','加',4000.00,9,'斤',7,NULL,NULL,NULL);
/*!40000 ALTER TABLE `s_order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_organization`
--

DROP TABLE IF EXISTS `s_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_organization` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `legal_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `linkman` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `merchant_id` int NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `org_type` smallint DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `registration_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `current` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否是当前组织',
  `checkout_date` date DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UKlmlkqjoip19qwyfp4mm1wjy9i` (`code`,`merchant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_organization`
--

LOCK TABLES `s_organization` WRITE;
/*!40000 ALTER TABLE `s_organization` DISABLE KEYS */;
INSERT INTO `s_organization` VALUES (1,'12121212','S10012','2024-04-25 11:29:14.338668','88388996@qq.com',_binary '','123','132',1,'组织三',0,'13944878765','12312312312','1212','2024-05-01',_binary '\0','2024-02-01'),(2,'12','10013','2024-04-25 12:18:02.278601','12',_binary '','12312','1212',1,'组织二',0,'13456784500','1231223','12','2024-03-01',_binary '\0','2024-02-01'),(3,'浙江温州','123','2024-05-06 10:20:33.203298',NULL,_binary '',NULL,'联系人',1,'组织一',NULL,'12321321313',NULL,NULL,'2024-01-01',_binary '\0','2024-01-01'),(4,'浙江温州','S10014','2024-05-06 12:03:12.339494',NULL,_binary '',NULL,'联系人',1,'组织四',NULL,'12321312345',NULL,NULL,'2024-05-01',_binary '','2024-02-09');
/*!40000 ALTER TABLE `s_organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_products`
--

DROP TABLE IF EXISTS `s_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `merchant_id` int NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int NOT NULL,
  `pinyin` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `unit_id` int NOT NULL,
  `multi_unit` json DEFAULT NULL COMMENT '辅助单位',
  `enable_multi_unit` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否启动辅助单位',
  `img_path` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_products_code` (`merchant_id`,`organization_id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_products`
--

LOCK TABLES `s_products` WRITE;
/*!40000 ALTER TABLE `s_products` DISABLE KEYS */;
INSERT INTO `s_products` VALUES (6,31,'01020005',_binary '',1,'苹果',4,'pg,pingguo',NULL,'斤',1,'[{\"num\": 2.0, \"price\": 0.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]',_binary '',NULL,10,'2024-05-07 10:19:09.720000'),(7,32,'01010006',_binary '',1,'火龙果',4,'hlg,huolongguo',NULL,'个 ',1,'[{\"num\": 2.0, \"price\": 0.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-07 10:19:48.749000'),(8,31,'01020007',_binary '',1,'李子',4,'lz,lizi',NULL,'斤',1,'[]',_binary '\0',NULL,0,'2024-05-07 10:20:06.904000'),(9,33,'02010008',_binary '',1,'空心菜',4,'kxc,kongxincai',NULL,'把',1,'[{\"num\": 2.0, \"price\": 0.0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 1000.0, \"price\": 0.0, \"unitId\": 3, \"unitName\": \"吨\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-07 10:20:44.794000'),(10,32,'01010009',_binary '',1,'甘蔗',4,'gz,ganzhe',NULL,'根',1,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-09 11:36:47.224000'),(11,33,'02010010',_binary '',1,'油麦菜',4,'ymc,youmaicai',NULL,'斤',1,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 500.0, \"price\": 0, \"unitId\": 3, \"unitName\": \"吨\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-13 14:50:59.998000'),(12,31,'01010011',_binary '',1,'雪梨',4,'xl,xueli',NULL,'件',1,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 2, \"unitName\": \"千克\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-15 10:55:44.742000'),(13,36,'010012',_binary '',1,'苹果',3,'pg,pingguo',NULL,'个',9,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-15 14:52:36.278000'),(14,43,'02010013',_binary '',1,'白菜',3,'bc,baicai',NULL,'斤',9,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 500.0, \"price\": 0, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-15 14:53:26.756000'),(15,42,'01020014',_binary '',1,'橘子',3,'jz,juzi',NULL,'斤',9,'[{\"num\": 500.0, \"price\": 0, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-15 14:54:34.604000'),(16,41,'01010015',_binary '',1,'凤梨',3,'fl,fengli',NULL,'个',12,'[{\"num\": 5.0, \"price\": 0, \"unitId\": 13, \"unitName\": \"件\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-15 14:55:08.967000'),(17,42,'01020016',_binary '',1,'黄瓜',3,'hg,huanggua',NULL,'件',13,'[{\"num\": 3.0, \"price\": 0, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-15 14:56:00.919000'),(18,39,'03010017',_binary '',1,'猪肉',3,'zr,zhurou',NULL,'斤',9,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-16 10:47:15.677000'),(19,40,'03020018',_binary '',1,'巴西牛肉',3,'bxnr,baxiniurou',NULL,'斤',9,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}, {\"num\": 500.0, \"price\": 0, \"unitId\": 11, \"unitName\": \"吨\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-16 10:48:04.505000'),(20,45,'10019',_binary '',1,'测试',1,'cs,ceshi',NULL,'斤',15,'[]',_binary '\0',NULL,0,'2024-05-18 08:20:41.678000'),(21,45,'10020',_binary '',1,'黄瓜',1,'hg,huanggua',NULL,'斤',15,'[]',_binary '\0',NULL,0,'2024-05-21 11:09:46.447000'),(22,45,'10021',_binary '',1,'火龙果',1,'hlg,huolongguo',NULL,'个',15,'[]',_binary '\0',NULL,0,'2024-05-21 11:10:55.692000'),(23,40,'03020022',_binary '',1,'美国黄牛肉',3,'mghnr,meiguohuangniurou',NULL,'斤',9,'[{\"num\": 2.0, \"price\": 0, \"unitId\": 10, \"unitName\": \"千克\", \"isDefault\": false}]',_binary '',NULL,0,'2024-05-22 14:24:33.543000');
/*!40000 ALTER TABLE `s_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_products_category`
--

DROP TABLE IF EXISTS `s_products_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_products_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `merchant_id` int DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int DEFAULT NULL,
  `pid` int DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL COMMENT '查询路径',
  `img_path` varchar(255) DEFAULT NULL COMMENT '分类图片',
  `leaf` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否叶子节点',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_products_category_code` (`merchant_id`,`organization_id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_products_category`
--

LOCK TABLES `s_products_category` WRITE;
/*!40000 ALTER TABLE `s_products_category` DISABLE KEYS */;
INSERT INTO `s_products_category` VALUES (1,'001',1,'分类1',NULL,NULL,1,NULL,NULL,_binary ''),(2,'0011',1,'分类11',NULL,NULL,2,NULL,NULL,_binary ''),(27,'02',1,'蔬菜类',4,NULL,1,'27/',NULL,_binary '\0'),(30,'01',1,'水果',4,NULL,1,'30/',NULL,_binary '\0'),(31,'0101',1,'亚热带水果',4,30,1,'30/31/',NULL,_binary ''),(32,'0102',1,'热带水果',4,30,1,'30/32/',NULL,_binary ''),(33,'0201',1,'叶子菜',4,27,1,'27/33/',NULL,_binary ''),(34,'0202',1,'根茎类',4,27,1,'27/34/',NULL,_binary ''),(35,'03',1,'肉类',4,NULL,1,'35/',NULL,_binary ''),(36,'01',1,'水果',3,NULL,1,'36/',NULL,_binary '\0'),(37,'02',1,'蔬菜',3,NULL,1,'37/',NULL,_binary '\0'),(38,' 03',1,'肉类',3,NULL,1,'38/',NULL,_binary '\0'),(39,'0301',1,'猪肉',3,38,1,'38/39/',NULL,_binary ''),(40,'0302',1,'牛肉',3,38,1,'38/40/',NULL,_binary ''),(41,'0101',1,'热带水果',3,36,1,'36/41/',NULL,_binary ''),(42,'0102',1,'亚热带水果',3,36,1,'36/42/',NULL,_binary ''),(43,'0201',1,'叶子菜',3,37,1,'37/43/',NULL,_binary ''),(44,'0202',1,'根茎类',3,37,1,'37/44/',NULL,_binary ''),(45,'1',1,'1',1,NULL,1,'45/',NULL,_binary '');
/*!40000 ALTER TABLE `s_products_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_purchase_price`
--

DROP TABLE IF EXISTS `s_purchase_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_purchase_price` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_id` bigint NOT NULL,
  `price` decimal(38,2) DEFAULT NULL COMMENT '单位价格',
  `products_id` bigint DEFAULT NULL,
  `unit_id` bigint DEFAULT NULL COMMENT '单位',
  `unit_price` json DEFAULT NULL COMMENT '单位辅助价格',
  `vendors_id` bigint DEFAULT NULL,
  `organization_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_vendorsId_productsId` (`vendors_id`,`products_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_purchase_price`
--

LOCK TABLES `s_purchase_price` WRITE;
/*!40000 ALTER TABLE `s_purchase_price` DISABLE KEYS */;
INSERT INTO `s_purchase_price` VALUES (1,1,5.00,6,1,NULL,1,4),(2,1,0.00,6,1,NULL,2,4),(3,1,6.00,8,1,NULL,1,4),(4,1,8.00,9,1,NULL,1,4),(5,1,7.00,10,1,NULL,1,4),(6,1,8.00,7,1,NULL,1,4),(7,1,0.00,10,1,NULL,2,4);
/*!40000 ALTER TABLE `s_purchase_price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_purchase_price_records`
--

DROP TABLE IF EXISTS `s_purchase_price_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_purchase_price_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_id` bigint NOT NULL,
  `organization_id` bigint NOT NULL,
  `price` decimal(38,2) DEFAULT NULL COMMENT '单位价格',
  `products_id` bigint DEFAULT NULL,
  `unit_id` bigint DEFAULT NULL COMMENT '单位',
  `unit_price` json DEFAULT NULL COMMENT '单位辅助价格',
  `vendors_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_vendorsId_productsId` (`vendors_id`,`products_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_purchase_price_records`
--

LOCK TABLES `s_purchase_price_records` WRITE;
/*!40000 ALTER TABLE `s_purchase_price_records` DISABLE KEYS */;
INSERT INTO `s_purchase_price_records` VALUES (1,1,4,2.00,6,1,NULL,1),(2,1,4,3.00,11,1,NULL,1),(3,1,4,5.00,10,1,NULL,1),(4,1,4,6.00,8,1,NULL,1),(5,1,4,34.00,6,1,NULL,2),(6,1,4,77.00,11,1,NULL,2),(7,1,3,56.00,17,13,NULL,3),(8,1,3,8.00,16,12,NULL,3),(9,1,3,12.00,15,9,NULL,3),(10,1,3,6.00,14,9,NULL,3),(11,1,3,7.00,13,9,NULL,3),(12,1,3,20.00,16,12,NULL,4),(13,1,3,12000.00,19,9,NULL,3),(14,1,3,0.00,19,9,NULL,4),(15,1,3,0.00,18,9,NULL,4),(16,1,3,0.00,18,9,NULL,3),(17,1,3,20.00,23,9,NULL,3);
/*!40000 ALTER TABLE `s_purchase_price_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_role`
--

DROP TABLE IF EXISTS `s_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `system_default` bit(1) DEFAULT NULL COMMENT '是否系统默认',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK7y7m6gk2u5eabvlmvpbqdhkj9` (`name`,`merchant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_role`
--

LOCK TABLES `s_role` WRITE;
/*!40000 ALTER TABLE `s_role` DISABLE KEYS */;
INSERT INTO `s_role` VALUES (1,1,'商户管理员',_binary ''),(2,1,'管理员',_binary '\0'),(5,4,'商户管理员',_binary '');
/*!40000 ALTER TABLE `s_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_stock`
--

DROP TABLE IF EXISTS `s_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_stock` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `products_id` bigint DEFAULT NULL COMMENT '产品ID',
  `total_amount` decimal(38,2) DEFAULT NULL COMMENT '小计金额',
  `total_quantity` decimal(38,2) DEFAULT NULL COMMENT '数量',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `weighted_average_cost` decimal(38,2) DEFAULT NULL COMMENT '平均成本',
  `in_unit_cost` decimal(38,2) DEFAULT NULL COMMENT '最近入库成本',
  `weighted_cost` decimal(38,2) DEFAULT NULL COMMENT '总成本',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_warehouseId_productsId` (`merchant_id`,`organization_id`,`warehouse_id`,`products_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_stock`
--

LOCK TABLES `s_stock` WRITE;
/*!40000 ALTER TABLE `s_stock` DISABLE KEYS */;
INSERT INTO `s_stock` VALUES (1,1,4,6,NULL,-3514.00,2,NULL,NULL,NULL),(2,1,4,7,NULL,-101.00,2,NULL,NULL,NULL),(3,1,4,8,NULL,-67.00,2,NULL,NULL,NULL),(4,1,4,9,NULL,-78.00,2,NULL,NULL,NULL),(5,1,4,10,NULL,-523.00,2,NULL,NULL,NULL),(6,1,4,11,NULL,227966.00,1,NULL,NULL,NULL),(7,1,4,6,NULL,3401.00,1,NULL,NULL,NULL),(8,1,4,11,NULL,-456.00,2,NULL,NULL,NULL),(9,1,4,10,NULL,912.00,1,NULL,NULL,NULL),(10,1,3,16,NULL,-5.00,7,0.02,8.00,0.00),(11,1,3,17,NULL,-76.00,5,NULL,1.67,NULL),(12,1,3,17,NULL,-459.00,7,NULL,56.00,NULL),(16,1,3,13,NULL,-45.00,5,NULL,0.00,NULL),(19,1,3,15,NULL,-470.00,7,0.35,12.00,0.00),(20,1,3,14,NULL,345.00,7,NULL,6.00,NULL),(21,1,3,13,NULL,455.00,7,NULL,7.00,NULL),(22,1,3,19,NULL,65.00,7,550.31,12000.00,35770.00),(23,1,3,18,NULL,1.00,7,0.00,0.00,0.00),(24,1,1,20,NULL,143.00,8,0.00,0.00,0.00),(30,1,1,20,NULL,-190.00,9,0.00,0.00,0.00),(31,1,1,21,NULL,-8.00,9,0.00,0.00,0.00),(32,1,1,21,0.00,4.00,8,0.00,0.00,0.00),(33,1,1,22,NULL,-90.00,9,0.00,0.00,0.00),(34,1,1,22,0.00,45.00,8,0.00,0.00,0.00),(35,1,3,23,NULL,7160.00,7,14.18,10.00,101550.00);
/*!40000 ALTER TABLE `s_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_stock_cost_adjustment`
--

DROP TABLE IF EXISTS `s_stock_cost_adjustment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_stock_cost_adjustment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_date` date DEFAULT NULL COMMENT '单据日期',
  `code` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '调整单号',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `products_id` bigint NOT NULL COMMENT '产品ID',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `adjustment_amount` decimal(38,2) DEFAULT NULL COMMENT '调整金额',
  `amount` decimal(38,2) DEFAULT NULL COMMENT '调整总金额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l40ubyfbpp2o2bdesvg7vhrtp` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_stock_cost_adjustment`
--

LOCK TABLES `s_stock_cost_adjustment` WRITE;
/*!40000 ALTER TABLE `s_stock_cost_adjustment` DISABLE KEYS */;
INSERT INTO `s_stock_cost_adjustment` VALUES (3,'2024-05-22','CBTZB100120240522001','2024-05-22 11:37:21.494493',1,1,3,19,7,NULL,NULL),(5,'2024-05-22','CBTZB100120240522002','2024-05-22 11:54:46.673081',1,1,3,19,7,NULL,NULL),(6,'2024-05-22','CBTZB100120240522003','2024-05-22 11:55:02.024602',1,1,3,19,7,NULL,NULL),(7,'2024-05-22','CBTZB100120240522004','2024-05-22 14:14:39.761048',1,1,3,19,7,NULL,NULL),(8,'2024-05-22','CBTZB100120240522005','2024-05-22 14:15:20.669943',1,1,3,19,7,NULL,NULL),(9,'2024-05-22','CBTZB100120240522006','2024-05-22 14:17:50.993436',1,1,3,19,7,NULL,NULL),(10,'2024-05-22','CBTZB100120240522007','2024-05-22 14:18:06.943044',1,1,3,19,7,NULL,NULL),(11,'2024-05-22','CBTZB100120240522008','2024-05-22 14:19:22.077382',1,1,3,19,7,NULL,NULL),(12,'2024-05-22','CBTZB100120240522009','2024-05-22 14:19:58.783624',1,1,3,19,7,NULL,NULL),(13,'2024-05-22','CBTZB100120240522010','2024-05-22 14:36:53.222912',1,1,3,23,7,NULL,NULL),(14,'2024-05-22','CBTZB100120240522011','2024-05-22 15:21:18.781766',1,1,3,23,7,NULL,50000.00);
/*!40000 ALTER TABLE `s_stock_cost_adjustment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_stock_cost_adjustment_detail`
--

DROP TABLE IF EXISTS `s_stock_cost_adjustment_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_stock_cost_adjustment_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `adjustment_id` bigint DEFAULT NULL COMMENT '调整单Id',
  `after_cost` decimal(38,2) DEFAULT NULL COMMENT '调整前成本金额',
  `after_unit_cost` decimal(38,2) DEFAULT NULL COMMENT '调整前单位成本',
  `available_quantity` decimal(38,2) DEFAULT NULL COMMENT '可出库数量',
  `cost` decimal(38,2) DEFAULT NULL COMMENT '调整后成本金额',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `stock_item_id` bigint DEFAULT NULL COMMENT '仓库详情Id',
  `unit_cost` decimal(38,2) DEFAULT NULL COMMENT '调整后单位成本',
  `before_cost` decimal(38,2) DEFAULT NULL COMMENT '调整前成本金额',
  `before_unit_cost` decimal(38,2) DEFAULT NULL COMMENT '调整前单位成本',
  `adjustment_amount` decimal(38,2) DEFAULT NULL COMMENT '调整金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_stock_cost_adjustment_detail`
--

LOCK TABLES `s_stock_cost_adjustment_detail` WRITE;
/*!40000 ALTER TABLE `s_stock_cost_adjustment_detail` DISABLE KEYS */;
INSERT INTO `s_stock_cost_adjustment_detail` VALUES (1,3,NULL,23.00,1.00,NULL,1,3,74,NULL,NULL,12000.00,NULL),(2,5,NULL,12000.00,1.00,NULL,1,3,74,NULL,NULL,23.00,NULL),(3,6,NULL,23.00,1.00,NULL,1,3,74,NULL,NULL,12000.00,NULL),(4,7,NULL,20.00,1.00,NULL,1,3,74,NULL,NULL,23.00,NULL),(5,8,NULL,15.00,1.00,NULL,1,3,74,NULL,NULL,20.00,NULL),(6,9,NULL,10.00,1.00,NULL,1,3,74,NULL,NULL,15.00,NULL),(7,10,NULL,9.00,1.00,NULL,1,3,74,NULL,NULL,10.00,NULL),(8,11,NULL,8.00,1.00,NULL,1,3,74,NULL,NULL,9.00,NULL),(9,12,NULL,7.00,1.00,NULL,1,3,74,NULL,NULL,8.00,NULL),(10,12,NULL,1200.00,1.00,NULL,1,3,73,NULL,NULL,12000.00,NULL),(11,12,NULL,13000.00,1.00,NULL,1,3,71,NULL,NULL,12000.00,NULL),(12,13,NULL,5.00,4000.00,NULL,1,3,97,NULL,NULL,10.00,NULL),(13,13,NULL,10.00,2000.00,NULL,1,3,96,NULL,NULL,7.50,NULL),(14,13,NULL,10.00,1000.00,NULL,1,3,95,NULL,NULL,5.00,NULL),(15,13,NULL,10.00,100.00,NULL,1,3,94,NULL,NULL,5.00,NULL),(16,14,NULL,20.00,4000.00,NULL,1,3,97,NULL,NULL,5.00,60000.00),(17,14,NULL,5.00,2000.00,NULL,1,3,96,NULL,NULL,10.00,-10000.00);
/*!40000 ALTER TABLE `s_stock_cost_adjustment_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_stock_inventory`
--

DROP TABLE IF EXISTS `s_stock_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_stock_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_date` date DEFAULT NULL COMMENT '盘点日期',
  `code` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `in_order_id` bigint DEFAULT NULL COMMENT '盘盈订单ID',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `out_order_id` bigint DEFAULT NULL COMMENT '盘亏订单ID',
  `remark` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `stock_date` date DEFAULT NULL COMMENT '库存日期',
  `user_id` bigint DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_43l1ig77tq8um6t19po7vwveu` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_stock_inventory`
--

LOCK TABLES `s_stock_inventory` WRITE;
/*!40000 ALTER TABLE `s_stock_inventory` DISABLE KEYS */;
INSERT INTO `s_stock_inventory` VALUES (3,NULL,'PD100120240511001','2024-05-11 16:19:19.865643',NULL,1,4,NULL,NULL,'2024-05-11',1),(4,NULL,'PD100120240513001','2024-05-13 09:41:27.279912',37,1,4,40,NULL,'2024-05-13',1),(5,NULL,'PD100120240514001','2024-05-14 14:14:48.332172',16,1,4,17,NULL,'2024-05-14',1),(6,NULL,'PD100120240514002','2024-05-14 14:36:36.956155',18,1,4,NULL,NULL,'2024-05-14',1),(7,NULL,'PD100120240514003','2024-05-14 14:39:04.599244',19,1,4,NULL,NULL,'2024-05-14',1),(8,NULL,'PD100120240514004','2024-05-14 14:41:04.751396',20,1,4,NULL,NULL,'2024-05-14',1);
/*!40000 ALTER TABLE `s_stock_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_stock_inventory_item`
--

DROP TABLE IF EXISTS `s_stock_inventory_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_stock_inventory_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `inventory_quantity` decimal(38,2) DEFAULT NULL COMMENT '盘点数量',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `products_id` bigint DEFAULT NULL COMMENT '产品ID',
  `remark` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `stock_count_id` bigint DEFAULT NULL COMMENT '盘点单ID',
  `sys_quantity` decimal(38,2) DEFAULT NULL COMMENT '系统数量',
  `unit_id` bigint DEFAULT NULL COMMENT '基础单位ID',
  `unit_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '基础单位名称',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `stock_inventory_id` bigint DEFAULT NULL COMMENT '盘点单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_stock_inventory_item`
--

LOCK TABLES `s_stock_inventory_item` WRITE;
/*!40000 ALTER TABLE `s_stock_inventory_item` DISABLE KEYS */;
INSERT INTO `s_stock_inventory_item` VALUES (15,4456.00,1,4,9,NULL,NULL,-1100.00,1,'斤',2,3),(16,456.00,1,4,9,NULL,NULL,-1100.00,1,'斤',2,4),(17,30.00,1,4,7,NULL,NULL,40.00,1,'斤',1,4),(18,90.00,1,4,8,NULL,NULL,0.00,1,'斤',1,4),(19,1200.00,1,4,9,NULL,NULL,1146.00,1,'斤',1,4),(20,900.00,1,4,7,NULL,NULL,800.00,1,'斤',2,5),(21,4000.00,1,4,11,NULL,NULL,5000.00,1,'斤',1,5),(22,688.00,1,4,7,NULL,NULL,956.00,1,'斤',2,6),(23,8777.00,1,4,11,NULL,NULL,4900.00,1,'斤',1,6),(24,348000.00,1,4,11,NULL,NULL,347995.00,1,'斤',2,7),(25,7400.00,1,4,8,NULL,NULL,7464.00,1,'斤',2,7),(26,-3900.00,1,4,10,NULL,NULL,-3925.00,1,'斤',2,8);
/*!40000 ALTER TABLE `s_stock_inventory_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_stock_item`
--

DROP TABLE IF EXISTS `s_stock_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_stock_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `after_quantity` decimal(38,2) DEFAULT NULL COMMENT '变动后数量',
  `available_quantity` decimal(38,2) DEFAULT NULL COMMENT '可出库数量',
  `batch_date` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '批次号',
  `before_quantity` decimal(38,2) DEFAULT NULL COMMENT '变动前数量',
  `bill_date` date DEFAULT NULL COMMENT '单据日期',
  `code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '单号',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `inventory_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '库存业务类型',
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `order_id` bigint DEFAULT NULL COMMENT '订单Id',
  `organization_id` bigint NOT NULL COMMENT '组织ID',
  `products_id` bigint DEFAULT NULL COMMENT '产品ID',
  `quantity` decimal(38,2) DEFAULT NULL COMMENT '数量',
  `stock_type` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '仓库类型',
  `total_amount` decimal(38,2) DEFAULT NULL COMMENT '小计金额',
  `unit_cost` decimal(38,2) DEFAULT NULL COMMENT '单位成本',
  `warehouse_id` bigint NOT NULL COMMENT '仓库ID',
  `cost` decimal(38,2) DEFAULT NULL COMMENT '成本金额',
  `order_detail_id` bigint DEFAULT NULL COMMENT '订单Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_stock_item`
--

LOCK TABLES `s_stock_item` WRITE;
/*!40000 ALTER TABLE `s_stock_item` DISABLE KEYS */;
INSERT INTO `s_stock_item` VALUES (1,NULL,34.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:04:49.842070',NULL,1,1,4,6,34.00,'减',136.00,NULL,2,NULL,NULL),(2,NULL,90.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:04:49.844617',NULL,1,1,4,6,45.00,'减',360.00,NULL,2,NULL,NULL),(3,NULL,112.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:04:49.845655',NULL,1,1,4,7,56.00,'减',672.00,NULL,2,NULL,NULL),(4,NULL,67.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:04:49.846636',NULL,1,1,4,8,67.00,'减',134.00,NULL,2,NULL,NULL),(5,NULL,78000.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:04:49.847529',NULL,1,1,4,9,78.00,'减',390.00,NULL,2,NULL,NULL),(6,NULL,34.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:12:31.810624',NULL,1,2,4,6,34.00,'减',170.00,NULL,1,NULL,NULL),(7,NULL,45.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:12:31.812288',NULL,1,2,4,7,45.00,'减',360.00,NULL,2,NULL,NULL),(8,NULL,67.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:12:31.813752',NULL,1,2,4,10,67.00,'减',134.00,NULL,2,NULL,NULL),(9,NULL,34.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:12:31.815290',NULL,1,2,4,11,34.00,'减',68.00,NULL,1,NULL,NULL),(10,NULL,3435.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:13:21.776450','调拨',1,3,4,6,3435.00,'加',0.00,NULL,1,NULL,NULL),(11,NULL,3435.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:13:21.778737','调拨',1,3,4,6,3435.00,'减',0.00,NULL,2,NULL,NULL),(12,NULL,228000.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:13:21.780899','调拨',1,3,4,11,456.00,'加',0.00,NULL,1,NULL,NULL),(13,NULL,228000.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:13:21.782751','调拨',1,3,4,11,456.00,'减',0.00,NULL,2,NULL,NULL),(14,NULL,912.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:13:21.786578','调拨',1,3,4,10,456.00,'加',0.00,NULL,1,NULL,NULL),(15,NULL,912.00,NULL,NULL,'2024-05-14',NULL,'2024-05-14 15:13:21.788984','调拨',1,3,4,10,456.00,'减',0.00,NULL,2,NULL,NULL),(16,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 14:56:40.320024',NULL,1,4,3,17,69.00,'加',115.00,1.67,5,NULL,NULL),(17,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 14:56:40.321217',NULL,1,4,3,16,1000.00,'加',6000.00,6.00,7,NULL,NULL),(18,NULL,2.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 14:57:49.002154',NULL,1,5,3,17,2.00,'减',112.00,0.00,7,0.00,15),(19,NULL,3.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:05:48.959347',NULL,1,6,3,17,3.00,'减',168.00,1.67,5,5.01,16),(20,NULL,56.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:23:36.586068',NULL,1,7,3,17,56.00,'减',3136.00,1.67,5,93.52,17),(21,NULL,32.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:24:14.907499',NULL,1,8,3,17,32.00,'减',1792.00,1.67,5,53.44,18),(22,NULL,23.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:24:14.908960',NULL,1,8,3,16,23.00,'减',69.00,6.00,7,138.00,19),(23,NULL,34.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:28:02.927495',NULL,1,9,3,17,34.00,'减',1904.00,1.67,5,56.78,20),(24,NULL,45.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:29:02.175387',NULL,1,10,3,17,45.00,'减',2520.00,1.67,5,75.15,21),(25,NULL,23.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:30:27.554242',NULL,1,11,3,17,23.00,'减',1288.00,1.67,5,38.41,22),(26,NULL,34.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:31:31.273406',NULL,1,12,3,17,34.00,'减',1904.00,1.67,5,56.78,23),(27,NULL,78.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:32:51.819055',NULL,1,13,3,17,78.00,'减',4368.00,1.67,5,130.26,24),(28,NULL,45.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:34:19.582099',NULL,1,14,3,13,45.00,'减',225.00,0.00,5,0.00,25),(29,NULL,45.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:36:59.511645',NULL,1,15,3,17,45.00,'减',2520.00,1.67,5,75.15,26),(30,NULL,23.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:42:12.414107',NULL,1,16,3,17,23.00,'减',1288.00,1.67,5,38.41,27),(31,NULL,30.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:43:34.525530',NULL,1,17,3,17,30.00,'减',1680.00,1.11,5,33.40,28),(32,NULL,56.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:50:22.635304',NULL,1,18,3,17,56.00,'减',4368.00,1.67,5,93.52,29),(33,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:15.138737',NULL,1,19,3,16,345.00,'加',2070.00,6.00,7,NULL,NULL),(34,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:15.141730',NULL,1,19,3,17,543.00,'加',30408.00,56.00,7,NULL,NULL),(35,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:15.142643',NULL,1,19,3,15,456.00,'加',2280.00,5.00,7,NULL,NULL),(36,NULL,345.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:15.143590',NULL,1,19,3,14,345.00,'加',2070.00,6.00,7,NULL,NULL),(37,NULL,455.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:15.145916',NULL,1,19,3,13,456.00,'加',3192.00,7.00,7,NULL,NULL),(38,NULL,1.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:52.083578',NULL,1,20,3,13,1.00,'减',5.00,7.00,7,7.00,35),(39,NULL,1.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:52.085470',NULL,1,20,3,16,1.00,'减',3.00,6.00,7,6.00,36),(40,NULL,1000.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 15:57:52.086892',NULL,1,20,3,17,1000.00,'减',46000.00,25.59,7,25592.00,37),(41,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:00:36.850777',NULL,1,23,3,15,1.00,'加',12.00,12.00,7,NULL,NULL),(42,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:00:39.182210',NULL,1,22,3,15,67.00,'加',670.00,10.00,7,NULL,NULL),(43,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:00:41.747451',NULL,1,21,3,15,54.00,'加',432.00,8.00,7,NULL,NULL),(44,NULL,1000.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:01:09.416325',NULL,1,24,3,15,1000.00,'减',9000.00,3.38,7,3376.00,41),(45,NULL,1.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:04:42.576002',NULL,1,25,3,15,1.00,'减',2.00,8.00,7,8.00,42),(46,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:10:36.259337',NULL,1,26,3,16,5.00,'加',25.00,5.00,7,NULL,NULL),(47,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:10:36.261285',NULL,1,26,3,16,10.00,'加',100.00,10.00,7,NULL,NULL),(48,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:10:36.262547',NULL,1,26,3,16,20.00,'加',400.00,20.00,7,NULL,NULL),(49,NULL,50.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:10:57.408746',NULL,1,27,3,16,50.00,'减',3500.00,6.00,7,300.00,46),(50,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:14:29.247619',NULL,1,28,3,16,1.00,'加',1.00,1.00,7,NULL,NULL),(51,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:14:29.249849',NULL,1,28,3,16,1.00,'加',4.00,4.00,7,NULL,NULL),(52,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:14:29.252032',NULL,1,28,3,16,1.00,'加',8.00,8.00,7,NULL,NULL),(53,NULL,5.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:16:46.627266',NULL,1,29,3,16,5.00,'减',25.00,6.00,7,30.00,50),(54,NULL,1300.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:18:34.477993',NULL,1,30,3,16,1300.00,'减',3900.00,0.29,7,380.00,51),(55,NULL,6.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:22:57.535938',NULL,1,31,3,16,6.00,'减',18.00,2.67,7,16.00,52),(56,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:25:24.537653',NULL,1,32,3,16,3.00,'加',3.00,1.00,7,NULL,NULL),(57,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:25:24.538973',NULL,1,32,3,16,3.00,'加',6.00,2.00,7,NULL,NULL),(58,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:25:24.539966',NULL,1,32,3,16,3.00,'加',9.00,3.00,7,NULL,NULL),(59,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:25:24.540867',NULL,1,32,3,16,3.00,'加',12.00,4.00,7,NULL,NULL),(60,NULL,15.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:31:09.184727',NULL,1,33,3,16,15.00,'减',75.00,0.80,7,12.00,57),(61,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:31:54.117428',NULL,1,34,3,16,6.00,'加',30.00,5.00,7,NULL,NULL),(62,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:31:54.118627',NULL,1,34,3,16,1.00,'加',6.00,6.00,7,NULL,NULL),(63,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:31:54.119499',NULL,1,34,3,16,1.00,'加',7.00,7.00,7,NULL,NULL),(64,NULL,0.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:31:54.120338',NULL,1,34,3,16,1.00,'加',8.00,8.00,7,NULL,NULL),(65,NULL,6.00,NULL,NULL,'2024-05-15',NULL,'2024-05-15 16:32:40.317290',NULL,1,35,3,16,6.00,'减',18.00,7.00,7,42.00,62),(66,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-16 11:17:18.524017',NULL,1,36,3,19,10.00,'加',200.00,20.00,7,NULL,NULL),(67,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-16 11:17:18.527351',NULL,1,36,3,19,2.00,'加',50.00,25.00,7,NULL,NULL),(68,NULL,62.00,NULL,NULL,'2024-05-16',NULL,'2024-05-16 11:17:18.528263',NULL,1,36,3,19,500.00,'加',12000.00,24.00,7,NULL,NULL),(69,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-16 11:51:26.834987',NULL,1,37,3,19,50.00,'减',1500.00,23.24,7,1162.00,66),(70,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-16 11:56:05.388848',NULL,1,38,3,19,400.00,'减',12000.00,23.93,7,9572.00,67),(71,NULL,1.00,NULL,NULL,'2024-05-08',NULL,'2024-05-17 15:42:26.289355',NULL,1,41,3,19,1.00,'加',12000.00,13000.00,7,NULL,NULL),(72,NULL,1.00,NULL,NULL,'2024-02-03',NULL,'2024-05-17 15:42:53.139438',NULL,1,46,3,18,1.00,'加',0.00,0.00,7,NULL,NULL),(73,NULL,1.00,NULL,NULL,'2024-02-10',NULL,'2024-05-17 15:42:55.496729',NULL,1,45,3,19,1.00,'加',12000.00,1200.00,7,NULL,NULL),(74,NULL,1.00,NULL,NULL,'2024-02-10',NULL,'2024-05-17 15:42:58.064746',NULL,1,47,3,19,1.00,'加',12000.00,7.00,7,NULL,NULL),(75,NULL,0.00,NULL,NULL,'2024-05-17',NULL,'2024-05-17 16:14:10.085250',NULL,1,48,3,15,456.00,'加',5472.00,12.00,7,NULL,NULL),(76,NULL,0.00,NULL,NULL,'2024-05-17',NULL,'2024-05-17 16:14:41.579569',NULL,1,49,3,16,456.00,'加',3648.00,8.00,7,NULL,NULL),(77,NULL,0.00,NULL,NULL,'2024-05-17',NULL,'2024-05-17 16:22:05.665750',NULL,1,50,3,16,1.00,'加',8.00,8.00,7,NULL,NULL),(78,NULL,0.00,NULL,NULL,'2024-05-17',NULL,'2024-05-17 16:22:05.668722',NULL,1,50,3,15,1.00,'加',12.00,12.00,7,NULL,NULL),(79,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-17 16:22:37.342990',NULL,1,40,3,16,4.00,'减',7.92,0.02,7,0.08,71),(80,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-17 16:22:37.346278',NULL,1,40,3,15,4.00,'减',4.40,0.35,7,1.40,72),(81,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-17 16:22:37.347681',NULL,1,40,3,15,500.00,'减',44.50,0.35,7,175.00,73),(82,NULL,0.00,NULL,NULL,'2024-05-16',NULL,'2024-05-17 16:22:37.348632',NULL,1,40,3,16,456.00,'减',1368.00,0.02,7,9.12,74),(83,NULL,0.00,NULL,NULL,'2024-05-18',NULL,'2024-05-18 08:21:17.898470',NULL,1,51,1,20,1.00,'减',2.00,0.00,8,0.00,88),(84,NULL,0.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 10:51:24.148546',NULL,1,54,1,20,45.00,'平',0.00,0.00,9,0.00,93),(85,NULL,43.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 10:51:24.152403',NULL,1,54,1,20,45.00,'平',0.00,0.00,8,0.00,93),(86,NULL,0.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 11:10:03.896658',NULL,1,56,1,21,4.00,'平',0.00,0.00,9,0.00,95),(87,NULL,4.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 11:10:03.897659',NULL,1,56,1,21,4.00,'平',0.00,0.00,8,0.00,95),(88,NULL,0.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 11:11:11.978483',NULL,1,57,1,22,45.00,'平',0.00,0.00,9,0.00,96),(89,NULL,45.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 11:11:11.979314',NULL,1,57,1,22,45.00,'平',0.00,0.00,8,0.00,96),(90,NULL,0.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 11:13:02.312444',NULL,1,55,1,20,100.00,'平',0.00,0.00,9,0.00,94),(91,NULL,100.00,NULL,NULL,'2024-05-21',NULL,'2024-05-21 11:13:02.313933',NULL,1,55,1,20,100.00,'平',0.00,0.00,8,0.00,94),(92,NULL,10.00,NULL,NULL,'2024-05-22',NULL,'2024-05-22 14:35:26.895044',NULL,1,59,3,23,10.00,'加',50.00,5.00,7,NULL,NULL),(93,NULL,50.00,NULL,NULL,'2024-05-22',NULL,'2024-05-22 14:35:26.896547',NULL,1,59,3,23,50.00,'加',500.00,10.00,7,NULL,NULL),(94,NULL,100.00,NULL,NULL,'2024-05-22',NULL,'2024-05-22 14:35:26.897846',NULL,1,59,3,23,100.00,'加',500.00,10.00,7,NULL,NULL),(95,NULL,1000.00,NULL,NULL,'2024-05-22',NULL,'2024-05-22 14:35:26.898979',NULL,1,59,3,23,1000.00,'加',5000.00,10.00,7,NULL,NULL),(96,NULL,2000.00,NULL,NULL,'2024-05-22',NULL,'2024-05-22 14:35:26.901442',NULL,1,59,3,23,2000.00,'加',15000.00,5.00,7,NULL,NULL),(97,NULL,4000.00,NULL,NULL,'2024-05-22',NULL,'2024-05-22 14:35:26.902542',NULL,1,59,3,23,4000.00,'加',40000.00,20.00,7,NULL,NULL);
/*!40000 ALTER TABLE `s_stock_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_units`
--

DROP TABLE IF EXISTS `s_units`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_units` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_units_name` (`merchant_id`,`organization_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_units`
--

LOCK TABLES `s_units` WRITE;
/*!40000 ALTER TABLE `s_units` DISABLE KEYS */;
INSERT INTO `s_units` VALUES (15,1,'斤',1),(12,1,'个',3),(13,1,'件',3),(10,1,'千克',3),(11,1,'吨',3),(9,1,'斤',3),(14,1,'箱',3),(2,1,'千克',4),(3,1,'吨',4),(1,1,'斤',4);
/*!40000 ALTER TABLE `s_units` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user_account`
--

DROP TABLE IF EXISTS `s_user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_user_account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` int DEFAULT NULL,
  `organization_id` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user_account`
--

LOCK TABLES `s_user_account` WRITE;
/*!40000 ALTER TABLE `s_user_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_vendors`
--

DROP TABLE IF EXISTS `s_vendors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_vendors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `linkman` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `merchant_id` int NOT NULL COMMENT '商户ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int NOT NULL COMMENT '组织ID',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `vendors_category_id` int DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_vendors`
--

LOCK TABLES `s_vendors` WRITE;
/*!40000 ALTER TABLE `s_vendors` DISABLE KEYS */;
INSERT INTO `s_vendors` VALUES (1,'12312','货商联系人',1,'货商一',4,'13211321312',NULL,1,'浙江温州',NULL,_binary ''),(2,'12312','122',1,'货商二',4,'13211321312',NULL,1,'浙江温州','2024-05-07 11:43:54.945000',_binary ''),(3,'01','里斯',1,'货商一',3,'1234567890',NULL,2,'浙江温州','2024-05-15 14:46:22.272000',_binary ''),(4,'02','王五',1,'货商二',3,'12345678909',NULL,3,'史蒂夫','2024-05-15 14:46:58.887000',_binary '');
/*!40000 ALTER TABLE `s_vendors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_vendors_category`
--

DROP TABLE IF EXISTS `s_vendors_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_vendors_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `merchant_id` int NOT NULL COMMENT '商户ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int NOT NULL COMMENT '组织ID',
  `pid` int DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_vendorsCategory_code` (`merchant_id`,`organization_id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_vendors_category`
--

LOCK TABLES `s_vendors_category` WRITE;
/*!40000 ALTER TABLE `s_vendors_category` DISABLE KEYS */;
INSERT INTO `s_vendors_category` VALUES (1,'01',1,'货商分类一',4,NULL),(2,'01',1,'分类一',3,NULL),(3,'02',1,'分类二',3,NULL);
/*!40000 ALTER TABLE `s_vendors_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_warehouses`
--

DROP TABLE IF EXISTS `s_warehouses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `s_warehouses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `linkman` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `merchant_id` int NOT NULL COMMENT '商户ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organization_id` int NOT NULL COMMENT '组织ID',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `enabled` bit(1) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `is_default` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否默认',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uc_warehouses_code` (`merchant_id`,`organization_id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_warehouses`
--

LOCK TABLES `s_warehouses` WRITE;
/*!40000 ALTER TABLE `s_warehouses` DISABLE KEYS */;
INSERT INTO `s_warehouses` VALUES (1,'123',NULL,1,'仓库一',4,NULL,NULL,'2024-05-07 11:57:44.897000',_binary '','史蒂夫',_binary '\0'),(2,'1234',NULL,1,'仓库二',4,NULL,NULL,'2024-05-10 11:05:42.226000',_binary '','浙江温州',_binary ''),(5,'01',NULL,1,'仓库一',3,NULL,NULL,'2024-05-15 14:47:16.489000',_binary '',NULL,_binary '\0'),(6,'02',NULL,1,'仓库二',3,NULL,NULL,'2024-05-15 14:47:25.058000',_binary '',NULL,_binary '\0'),(7,'03',NULL,1,'仓库三',3,NULL,NULL,'2024-05-15 14:47:36.349000',_binary '',NULL,_binary ''),(8,'1',NULL,1,'1',1,NULL,NULL,'2024-05-18 08:21:03.492000',_binary '',NULL,_binary ''),(9,'2',NULL,1,'2',1,NULL,NULL,'2024-05-21 10:39:59.394000',_binary '',NULL,_binary '\0');
/*!40000 ALTER TABLE `s_warehouses` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-24 10:19:12
