-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: petstore
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `managers`
--

DROP TABLE IF EXISTS `managers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `managers` (
  `manager_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`manager_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `managers`
--

LOCK TABLES `managers` WRITE;
/*!40000 ALTER TABLE `managers` DISABLE KEYS */;
INSERT INTO `managers` VALUES (1,'manager1','123456','Manager One','manager1@example.com','101-202-3030','123 Corporate Blvd, Citytown, CT 12345','2025-05-06 11:04:43'),(2,'manager2','123456','Manager Two','manager2@example.com','202-303-4040','456 Business Park, Townville, TV 67890','2025-05-06 11:04:43'),(3,'manager3','123456','Manager Three','manager3@example.com','303-404-5050','789 Enterprise Rd, Greenfield, GF 23456','2025-05-06 11:04:43'),(4,'manager4','123456','Manager Four','manager4@example.com','404-505-6060','101 Industrial St, Lakeside, LS 34567','2025-05-06 11:04:43'),(5,'manager5','123456','Manager Five','manager5@example.com','505-606-7070','202 Commerce Dr, Hilltown, HT 45678','2025-05-06 11:04:43');
/*!40000 ALTER TABLE `managers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pets`
--

DROP TABLE IF EXISTS `pets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pets` (
  `pet_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `species` varchar(30) DEFAULT NULL,
  `breed` varchar(50) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `gender` enum('Male','Female') DEFAULT NULL,
  `description` text,
  `price` double DEFAULT NULL,
  `available` tinyint(1) DEFAULT '1',
  `owner_id` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `vaccinated` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pet_id`),
  KEY `owner_id` (`owner_id`),
  CONSTRAINT `pets_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pets`
--

LOCK TABLES `pets` WRITE;
/*!40000 ALTER TABLE `pets` DISABLE KEYS */;
INSERT INTO `pets` VALUES (1,'Max','Dog','Labrador Retriever',3,'Male','Friendly and energetic, loves outdoor activities.',500,1,NULL,'Dog1.jpeg',1),(2,'Bella','Dog','German Shepherd',5,'Female','Loyal, intelligent, and protective.',700,1,NULL,'Dog2.jpg',1),(3,'Charlie','Dog','Bulldog',4,'Male','Calm, affectionate, and easy-going.',600,1,NULL,'Dog3.jpeg',1),(4,'Luna','Dog','Golden Retriever',2,'Female','Playful, loyal, and great with kids.',800,1,NULL,'Dog4.jpg',0),(5,'Milo','Dog','Beagle',6,'Male','Active, curious, and loves to explore.',450,1,NULL,'Dog5.jpg',1),(6,'Fluffy','Cat','Persian',4,'Female','Calm, affectionate, loves to lounge.',1000,0,11,'Cat1.jpeg',1),(7,'Shadow','Cat','Maine Coon',3,'Male','Playful, social, and loves attention.',1200,0,1,'Cat2.jpg',1),(8,'Sophie','Cat','Siamese',2,'Female','Talkative, active, and very affectionate.',900,1,NULL,'Cat3.jpeg',0),(9,'Chester','Cat','British Shorthair',5,'Male','Independent, calm, and loves to nap.',1100,1,NULL,'Cat4.jpeg',1),(10,'Cleo','Cat','Ragdoll',2,'Female','Gentle, playful, and follows you around.',1300,1,NULL,'Cat5.jpeg',1);
/*!40000 ALTER TABLE `pets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `price` double NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `stock_quantity` int DEFAULT '0',
  `image` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `image` (`image`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (11,'Pet Toy','Toy',5.99,'A fun and colorful toy for pets to play with.',29,'p1.jpg'),(12,'Squeaky Ball','Toy',7.99,'A rubber ball with a squeaker inside, perfect for dogs.',16,'p2.jpg'),(13,'Catnip Toy','Toy',3.99,'A soft, catnip-filled toy that cats will love to chase and bat around.',29,'p3.jpg'),(14,'Interactive Feather Toy','Toy',9.99,'An interactive toy with feathers, ideal for stimulating a cat’s hunting instincts.',73,'p4.jpg'),(15,'Pet Bed','Accessory',19.99,'A soft, comfortable bed for your pet to rest in.',29,'p5.jpg'),(16,'Pet Bowl','Accessory',12.99,'A stainless steel bowl for feeding your pet.',20,'p6.jpg'),(17,'Squeaky Plush Toy','Toy',6.99,'A soft, plush toy with a squeaker, perfect for cuddling and playing.',49,'p7.jpg'),(18,'Dog Leash','Accessory',14.99,'A durable leash for walking your dog comfortably.',59,'p8.jpg'),(19,'Pet Shampoo','Accessory',11.99,'A gentle shampoo to keep your pet clean and fresh.',40,'p9.jpg'),(20,'Catnip','Accessory',2.99,'Dried catnip that can be sprinkled on toys or scratching posts to attract cats.',99,'p10.jpg');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `transaction_type` enum('adoption','product') DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `price` double DEFAULT NULL,
  `amount` int DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,11,'adoption','2025-05-11 07:13:37',1000,1),(2,1,'adoption','2025-05-11 07:14:36',1200,1);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_inventory`
--

DROP TABLE IF EXISTS `user_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_inventory` (
  `inventory_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `product_name` varchar(100) DEFAULT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`inventory_id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `user_inventory_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_inventory_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_inventory`
--

LOCK TABLES `user_inventory` WRITE;
/*!40000 ALTER TABLE `user_inventory` DISABLE KEYS */;
INSERT INTO `user_inventory` VALUES (8,11,15,'Pet Bed',1,19.99,'p5.jpg'),(9,11,18,'Dog Leash',1,14.99,'p8.jpg'),(12,11,12,'Squeaky Ball',1,7.99,'p2.jpg'),(13,11,17,'Squeaky Plush Toy',1,6.99,'p7.jpg'),(14,11,14,'Interactive Feather Toy',1,9.99,'p4.jpg'),(15,11,11,'Pet Toy',1,5.99,'p1.jpg');
/*!40000 ALTER TABLE `user_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'username1','123456','User One','user1@example.com','100-200-3000','123 Main St, Cityville, CV 12345','2025-05-06 11:00:23'),(2,'username2','123456','User Two','user2@example.com','200-300-4000','456 Oak Ave, Townsville, TS 67890','2025-05-06 11:00:23'),(3,'username3','123456','User Three','user3@example.com','300-400-5000','789 Pine Rd, Greenfield, GF 23456','2025-05-06 11:00:23'),(4,'username4','123456','User Four','user4@example.com','400-500-6000','101 Birch Blvd, Lakeside, LS 34567','2025-05-06 11:00:23'),(5,'username5','123456','User Five','user5@example.com','500-600-7000','202 Maple Dr, Hilltown, HT 45678','2025-05-06 11:00:23'),(6,'username6','123456','User Six','user6@example.com','600-700-8000','303 Cedar Ln, Riverdale, RD 56789','2025-05-06 11:00:23'),(7,'username7','123456','User Seven','user7@example.com','700-800-9000','404 Elm St, Forestville, FV 67890','2025-05-06 11:00:23'),(8,'username8','123456','User Eight','user8@example.com','800-900-0000','505 Spruce St, Meadowbrook, MB 78901','2025-05-06 11:00:23'),(9,'username9','123456','User Nine','user9@example.com','900-000-1111','606 Fir Ct, Westfield, WF 89012','2025-05-06 11:00:23'),(10,'username10','123456','User Ten','user10@example.com','100-200-3001','707 Redwood Rd, Eastwood, EW 90123','2025-05-06 11:00:23'),(11,'trungtoto','123123','Nguyen Quang Trung','tn66638@gmail.com','0949151103','Co Loa, Dong Anh, Ha Noi','2025-05-08 10:05:18');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-11 17:06:14
