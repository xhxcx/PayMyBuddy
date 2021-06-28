CREATE DATABASE  IF NOT EXISTS `paymybuddy_test`;
USE `paymybuddy_test`;

DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `contact`;
DROP TABLE IF EXISTS `bank_account`;
DROP TABLE IF EXISTS `user_account`;

--
-- Table structure for table `user_account`
--

CREATE TABLE `user_account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(254) NOT NULL,
  `password` varchar(72) NOT NULL,
  `first_name` varchar(25) NOT NULL,
  `last_name` varchar(25) NOT NULL,
  `address` varchar(150) NOT NULL,
  `account_balance` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_account_idx` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `bank_account`
--

CREATE TABLE `bank_account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `iban` varchar(34) NOT NULL,
  `caption` varchar(50) NOT NULL,
  `holder_name` varchar(120) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_account_bank_account_fk` (`user_id`),
  CONSTRAINT `user_account_bank_account_fk` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `contact`
--

CREATE TABLE `contact` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contact_alias` varchar(50) NOT NULL,
  `user_id` int NOT NULL,
  `contact_user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_account_contact_friend_fk` (`user_id`),
  KEY `user_account_contact_owner_fk` (`contact_user_id`),
  CONSTRAINT `user_account_contact_friend_fk` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`),
  CONSTRAINT `user_account_contact_owner_fk` FOREIGN KEY (`contact_user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `description` varchar(150) NOT NULL,
  `transaction_type` varchar(30) NOT NULL,
  `fee_rate` decimal(5,2) NOT NULL,
  `amount` decimal(7,2) NOT NULL,
  `sender_user_id` int NOT NULL,
  `beneficiary_user_id` int NOT NULL,
  `bank_account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_account_transaction_sender_fk` (`sender_user_id`),
  KEY `user_account_transaction_beneficiary_fk1` (`beneficiary_user_id`),
  KEY `bank_account_transaction_fk_idx` (`bank_account_id`),
  CONSTRAINT `bank_account_transaction_fk` FOREIGN KEY (`bank_account_id`) REFERENCES `bank_account` (`id`),
  CONSTRAINT `user_account_transaction_beneficiary_fk1` FOREIGN KEY (`beneficiary_user_id`) REFERENCES `user_account` (`id`),
  CONSTRAINT `user_account_transaction_sender_fk` FOREIGN KEY (`sender_user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;