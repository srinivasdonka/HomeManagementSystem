/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.18 : Database - HomeManagementSystem
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`HomeManagement` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `HomeManagementSystem`;

/*Table structure for table `Auth_Access_Token` */

DROP TABLE IF EXISTS `Auth_Access_Token`;

CREATE TABLE `Auth_Access_Token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(256) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `Auth_Access_Token` */



/*Table structure for table `oauth_approvals` */

DROP TABLE IF EXISTS `oauth_approvals`;

CREATE TABLE `oauth_approvals` (
  `userId` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `clientId` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `scope` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `status` varchar(10) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `expiresAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastModifiedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

/*Data for the table `oauth_approvals` */

/*Table structure for table `oauth_client_details` */

DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) COLLATE utf8_general_mysql500_ci NOT NULL,
  `resource_ids` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `client_secret` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `scope` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `authorized_grant_types` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `authorities` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `autoapprove` varchar(256) COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

/*Data for the table `oauth_client_details` */

insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`) values ('bankudit-client-id','bankaudit','***','read,write','password,refresh_token',NULL,'ADM',18000,18000,NULL,NULL);

/*Table structure for table `oauth_refresh_token` */

DROP TABLE IF EXISTS `oauth_refresh_token`;

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `User` */

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `email_id` varchar(254) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `first_name` varchar(250) DEFAULT NULL,
  `designation` varchar(200) DEFAULT NULL,
  `last_name` varchar(250) DEFAULT NULL,
  `address` varchar(400) DEFAULT NULL,
  `company_name` varchar(200) DEFAULT NULL,
  `phone` varchar(13) DEFAULT NULL,
  `maker_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_id` (`email_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4470 DEFAULT CHARSET=latin1;

/*Data for the table `User` */

insert  into `User`(`id`,`email_id`,`password`,`first_name`,`designation`,`last_name`,`address`,`company_name`,`phone`,`maker_timestamp`) values (4468,'srinivas@gmail.com','***','srinivas','Manager','D','hyderabad','TechDS','7780443926','2018-04-09 07:09:47'),(4469,'srinivas@gmail.com','***','Srinivas','Manager','D','hyderabad','TechDS','7780443926','2018-04-09 07:09:47');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
