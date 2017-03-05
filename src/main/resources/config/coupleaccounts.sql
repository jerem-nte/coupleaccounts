/*SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
*/
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

DROP TABLE IF EXISTS currency;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS `currency` (
`id` int(11) auto_increment NOT NULL,
  `name` varchar(20) NOT NULL,
  `shortname` varchar(10) NOT NULL,
  `icon` varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `transactions` (
`id` int(11) auto_increment NOT NULL,
  `user_id` int(11) NOT NULL,
  `currency_id` int(11) NOT NULL,
  `label` varchar(255) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `scope` varchar(50) NOT NULL,
  `archived` tinyint(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS `users` (
`id` int(11) auto_increment NOT NULL,
  `name` varchar(20) NOT NULL,
  `gender` char(1) NOT NULL
);


ALTER TABLE currency
 ADD PRIMARY KEY (`id`);

ALTER TABLE `transactions`
 ADD PRIMARY KEY (`id`);

ALTER TABLE `users`
 ADD PRIMARY KEY (`id`);

