-- MySQL dump 10.13  Distrib 5.5.62, for Win64 (AMD64)
--
-- Host: 150.158.97.5    Database: qingxun
-- ------------------------------------------------------
-- Server version	8.0.27-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `byte_admin`
--

DROP TABLE IF EXISTS `byte_admin`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_admin`
(
    `id`              bigint       NOT NULL,
    `username`        varchar(255) NOT NULL,
    `password`        varchar(255) NOT NULL,
    `created_time`    datetime DEFAULT NULL,
    `last_login_time` datetime DEFAULT NULL,
    `admin`           bit(1)   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_card`
--

DROP TABLE IF EXISTS `byte_card`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_card`
(
    `cardId`       bigint                            NOT NULL AUTO_INCREMENT,
    `cardname`     varchar(64) CHARACTER SET utf8mb4 NOT NULL,
    `description`  varchar(255) CHARACTER SET utf8mb4                     DEFAULT NULL,
    `listId`       bigint                                                 DEFAULT NULL,
    `productId`    bigint                                                 DEFAULT NULL,
    `closed`       bit(1)                            NOT NULL             DEFAULT b'0' COMMENT '是否归档 0 否，1是',
    `pos`          float                             NOT NULL             DEFAULT '0',
    `deadline`     datetime                                               DEFAULT NULL,
    `tag`          bit(1)                            NOT NULL             DEFAULT b'0' COMMENT '是否存在标签',
    `executor`     bit(1)                            NOT NULL             DEFAULT b'0' COMMENT '是否有执行的人',
    `begintime`    datetime                                               DEFAULT NULL,
    `expired`      bit(1)                            NOT NULL             DEFAULT b'0' COMMENT '是否过期 0 否，1是',
    `created_time` datetime                                               DEFAULT NULL COMMENT '创建时间',
    `creator`      bigint                                                 DEFAULT '1' COMMENT '创建者id',
    `background`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '卡片背景颜色',
    `completed`    bit(1)                                                 DEFAULT b'0' COMMENT '是否完成卡片 0否 1是',
    PRIMARY KEY (`cardId`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 563
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_card_tag`
--

DROP TABLE IF EXISTS `byte_card_tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_card_tag`
(
    `id`     bigint NOT NULL AUTO_INCREMENT,
    `cardId` bigint NOT NULL COMMENT '卡片id',
    `tagId`  bigint NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `inx_tagId_cardId` (`tagId`, `cardId`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 471
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_card_user`
--

DROP TABLE IF EXISTS `byte_card_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_card_user`
(
    `id`     bigint NOT NULL AUTO_INCREMENT,
    `cardId` bigint NOT NULL COMMENT '卡片id',
    `userId` bigint NOT NULL COMMENT '用户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `inx_card_user` (`cardId`, `userId`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 373
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_list`
--

DROP TABLE IF EXISTS `byte_list`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_list`
(
    `id`               bigint                                                 NOT NULL AUTO_INCREMENT,
    `productId`        bigint                                                          DEFAULT NULL COMMENT '项目Id',
    `pos`              float(10, 2)                                           NOT NULL DEFAULT '0.00' COMMENT '位置权重',
    `closed`           bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '0 未关闭',
    `listName`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '列的名称',
    `background_color` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '背景颜色',
    `created_time`     datetime                                                        DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `inx_productId` (`productId`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 531
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_mail`
--

DROP TABLE IF EXISTS `byte_mail`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_mail`
(
    `mailId`   bigint NOT NULL,
    `from`     varchar(64)  DEFAULT NULL,
    `to`       varchar(64)  DEFAULT NULL,
    `subject`  varchar(64)  DEFAULT NULL,
    `text`     varchar(255) DEFAULT NULL,
    `sendtime` datetime     DEFAULT NULL,
    PRIMARY KEY (`mailId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_pro_user`
--

DROP TABLE IF EXISTS `byte_pro_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_pro_user`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `productId`    bigint NOT NULL COMMENT '项目id',
    `userId`       bigint NOT NULL COMMENT '用户id',
    `inviteUserId` bigint   DEFAULT NULL COMMENT '邀请人id',
    `createdTime`  datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `inx_product_user` (`productId`, `userId`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 247
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_product`
--

DROP TABLE IF EXISTS `byte_product`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_product`
(
    `id`           bigint                                                 NOT NULL AUTO_INCREMENT,
    `ownerId`      bigint                                                          DEFAULT NULL,
    `isPrivate`    bit(1)                                                 NOT NULL DEFAULT b'0' COMMENT '0 他人不可见',
    `productName`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL,
    `background`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL,
    `created_time` datetime                                                        DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `ownerId` (`ownerId`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 215
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_tag`
--

DROP TABLE IF EXISTS `byte_tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_tag`
(
    `id`        bigint                                                 NOT NULL AUTO_INCREMENT,
    `productId` bigint                                                          DEFAULT NULL,
    `tagName`   varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `color`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'fff',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 139
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `byte_user`
--

DROP TABLE IF EXISTS `byte_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `byte_user`
(
    `userId`          bigint                                                 NOT NULL AUTO_INCREMENT,
    `username`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `avatar`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL,
    `password`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `fullname`        varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `created_time`    datetime                                                        DEFAULT NULL,
    `last_login_time` datetime                                                        DEFAULT NULL,
    `token`           varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci         DEFAULT NULL,
    `isNews`          bit(1)                                                          DEFAULT NULL COMMENT '是否为新用户',
    PRIMARY KEY (`userId`) USING BTREE,
    UNIQUE KEY `UK_USERNAME` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 46
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'qingxun'
--
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2022-06-18 21:11:01
