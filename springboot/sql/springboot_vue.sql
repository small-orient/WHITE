/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.26 : Database - spring-boot-blog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`spring-boot-blog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `spring-boot-blog`;

/*Table structure for table `h_blog` */

DROP TABLE IF EXISTS `h_blog`;

CREATE TABLE `h_blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '博客ID',
  `title` varchar(200) DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(400) DEFAULT NULL COMMENT '摘要',
  `content` mediumtext COMMENT '博客内容',
  `clickHit` int(20) DEFAULT '0' COMMENT '点击数',
  `commentHit` int(20) DEFAULT '0' COMMENT '评论数',
  `blogTypeId` int(20) DEFAULT NULL COMMENT '博客类型ID',
  `deleted` int(11) NOT NULL DEFAULT '0' COMMENT '逻辑删除：1待删除，0未删除',
  `userId` int(11) NOT NULL DEFAULT '1' COMMENT '博主',
  `published` int(11) DEFAULT '0' COMMENT '发布状态：0发布,1草稿',
  `commentOpen` int(11) DEFAULT '0' COMMENT '评论开启：0开启，1关闭',
  `appreciation` int(11) DEFAULT '0' COMMENT '赞赏开启：0开启，1关闭',
  `recommend` int(11) DEFAULT '0' COMMENT '是否推荐：0推荐，1不推荐',
  `shareStatement` int(11) DEFAULT '0' COMMENT '版权转载栏',
  `blogImg` varchar(300) DEFAULT NULL COMMENT '博客封面图片路径',
  `createSign` varchar(20) DEFAULT '原创' COMMENT '创作标识',
  `likes` bigint(20) DEFAULT '0' COMMENT '点赞数',
  `create_time` datetime DEFAULT NULL COMMENT '发表时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Table structure for table `h_blog_keyword` */

DROP TABLE IF EXISTS `h_blog_keyword`;

CREATE TABLE `h_blog_keyword` (
  `bId` bigint(20) NOT NULL COMMENT '博客ID',
  `kId` bigint(20) NOT NULL COMMENT '关键字Id',
  PRIMARY KEY (`bId`,`kId`),
  KEY `kId` (`kId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `h_blogtype` */

DROP TABLE IF EXISTS `h_blogtype`;

CREATE TABLE `h_blogtype` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '博客类型ID',
  `typeName` varchar(100) DEFAULT NULL COMMENT '博客类型名称',
  `blogNumber` bigint(100) DEFAULT '0' COMMENT '各类型博客数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Table structure for table `h_comment` */

DROP TABLE IF EXISTS `h_comment`;

CREATE TABLE `h_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '一条评论的id',
  `userId` int(20) DEFAULT NULL COMMENT '评论用户ID',
  `content` varchar(1000) DEFAULT NULL COMMENT '评论内容',
  `parentCommentId` int(30) DEFAULT '-1' COMMENT '父评论ID:-1,为绝对父评论',
  `blogId` int(20) DEFAULT NULL COMMENT '评论博客ID',
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

/*Table structure for table `h_keyword` */

DROP TABLE IF EXISTS `h_keyword`;

CREATE TABLE `h_keyword` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关键字ID',
  `keyword` varchar(50) DEFAULT NULL COMMENT '关键字名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

/*Table structure for table `h_link` */

DROP TABLE IF EXISTS `h_link`;

CREATE TABLE `h_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '友情链接ID',
  `linkUrl` varchar(200) DEFAULT NULL COMMENT '链接地址',
  `linkName` varchar(100) DEFAULT NULL COMMENT '链接名称',
  `linkImg` varchar(100) DEFAULT NULL COMMENT '链接图片地址',
  `create_time` datetime DEFAULT NULL COMMENT '添加链接时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新链接时间',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Table structure for table `h_user` */

DROP TABLE IF EXISTS `h_user`;

CREATE TABLE `h_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '博主ID',
  `userName` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(150) DEFAULT NULL COMMENT '密码',
  `nickName` varchar(50) DEFAULT NULL COMMENT '昵称',
  `sign` varchar(100) DEFAULT NULL COMMENT '个性签名',
  `avatar` varchar(300) DEFAULT NULL COMMENT '头像地址',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `locked` int(11) DEFAULT '0' COMMENT '冻结：0正常，1被冻结',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `last_time` datetime DEFAULT NULL COMMENT '上一次登陆时间',
  `login_time` datetime DEFAULT NULL COMMENT '最后一次登陆时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
