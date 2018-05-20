/*
Navicat MySQL Data Transfer

Source Server         : hufufeel
Source Server Version : 50719
Source Host           : www.hufufeel.com:3306
Source Database       : hufufeel

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-05-19 19:33:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rubick_product
-- ----------------------------
DROP TABLE IF EXISTS `rubick_product`;
CREATE TABLE `rubick_product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(255) NOT NULL,
  `is_battery` tinyint(1) NOT NULL,
  `origin` varchar(255) NOT NULL,
  `weight` decimal(10,2) NOT NULL,
  `length` decimal(10,2) NOT NULL,
  `width` decimal(10,2) NOT NULL,
  `heigh` decimal(10,2) NOT NULL,
  `status` int(11) NOT NULL COMMENT '审核状态',
  `product_url` varchar(255) NOT NULL,
  `deadline` timestamp NULL DEFAULT NULL,
  `is_danger` tinyint(1) NOT NULL,
  `quoted_price` decimal(10,2) NOT NULL,
  `quoted_name` varchar(255) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `product_sku` (`product_sku`) USING HASH,
  KEY `product_name` (`product_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `think_user`
MODIFY COLUMN `id`  bigint(11) NOT NULL AUTO_INCREMENT FIRST ;

INSERT INTO `hufufeel`.`authority` (`id`, `name`, `authority`) VALUES ('3', 'ROLE_DEFAULT', 'ROLE_DEFAULT');
