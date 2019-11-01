-- Database `library`
DROP DATABASE IF EXISTS `library`;
CREATE DATABASE `library`
  CHARACTER SET 'utf8'
  COLLATE 'utf8_general_ci';

USE `library`;

-- Table `bookcase`
DROP TABLE IF EXISTS `bookcase`;
CREATE TABLE `bookcase`
(
  `bc_system_id` BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT
    COMMENT '表的id',
  `bc_location`  CHAR(5)          NOT NULL
    COMMENT '1_1代表一楼一号区域，以此类推',
  `bc_number`    INT UNSIGNED     NOT NULL
    COMMENT '书包柜的编号，每个区域的编号',
  `bc_user_id`   INT UNSIGNED              DEFAULT NULL
    COMMENT '书包柜对应的学生的id',
  `bc_status`    TINYINT UNSIGNED NOT NULL DEFAULT '0'
    COMMENT '书包柜的状态 默认0为可选',
  PRIMARY KEY (`bc_system_id`),
  INDEX `idx_l` (`bc_location`),
  INDEX `idx_u` (`bc_user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table `config`
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`
(
  `config_system_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
    COMMENT '配置的id',
  `config_key`       VARCHAR(15)  DEFAULT NULL
    COMMENT '配置的key',
  `config_value`     VARCHAR(255) DEFAULT NULL
    COMMENT '配置的value（时间）',
  PRIMARY KEY (`config_system_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table `user`
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
  `user_system_id`    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
    COMMENT 'user表的id',
  `user_student_id`   CHAR(10)        NOT NULL
    COMMENT '学生端为学号，教师端为账号',
  `user_password`     VARCHAR(6)      NOT NULL
    COMMENT '密码',
  `user_student_name` VARCHAR(20)     NOT NULL
    COMMENT '学生姓名',
  `user_type`         TINYINT UNSIGNED DEFAULT '0'
  COMMENT '用户的状态，黑名单的同学为2，老师设置为1，学生设置为0，默认为0 只负责查询的老师设置为：3',
  `user_token`        VARCHAR(255)     DEFAULT NULL
    COMMENT '令牌',
  PRIMARY KEY (`user_system_id`),
  UNIQUE KEY (`user_student_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;