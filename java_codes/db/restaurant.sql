-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 01, 2018 at 05:36 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `restaurant`
--

-- --------------------------------------------------------

--
-- Table structure for table `individual_orders`
--

CREATE TABLE IF NOT EXISTS `individual_orders` (
  `order_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `total_price` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `individual_orders`
--

INSERT INTO `individual_orders` (`order_id`, `item_id`, `quantity`, `total_price`) VALUES
(1, 2, 2, 110),
(1, 3, 4, 40),
(1, 1, 44, 1408),
(1, 2, 1, 55),
(1, 2, 3, 165),
(1, 1, 4, 128),
(1, 2, 4, 220),
(1, 2, 3, 165),
(2, 1, 2, 64),
(4, 3, 5, 50),
(4, 2, 52, 2860),
(5, 1, 3, 96),
(5, 3, 3, 30),
(5, 2, 3, 165),
(6, 1, 2, 64),
(6, 3, 3, 30),
(7, 3, 3, 30),
(7, 4, 3, 36),
(8, 2, 3, 165),
(8, 4, 3, 36),
(9, 2, 2, 110),
(10, 1, 3, 96),
(11, 1, 3, 96),
(12, 2, 3, 165),
(12, 4, 3, 36),
(13, 1, 7, 224),
(13, 3, 3, 30),
(13, 5, 3, 450),
(14, 1, 3, 96),
(15, 2, 2, 110),
(16, 2, 5, 275),
(16, 4, 5, 60),
(18, 2, 7, 385),
(18, 3, 7, 70),
(19, 2, 7, 385),
(20, 1, 6, 192),
(21, 1, 6, 192),
(21, 2, 6, 330),
(22, 1, 6, 192),
(22, 2, 6, 330),
(23, 1, 3, 96),
(24, 1, 3, 96),
(25, 1, 4, 128),
(25, 2, 4, 220),
(26, 1, 6, 192),
(27, 1, 5, 160),
(27, 4, 2, 24),
(27, 5, 2, 300),
(28, 5, 2, 300),
(28, 3, 12, 120),
(28, 1, 12, 384),
(29, 2, 3, 165),
(29, 3, 3, 30),
(30, 2, 2, 110),
(30, 4, 2, 24),
(31, 4, 2, 24),
(32, 4, 2, 24),
(32, 3, 2, 20),
(33, 3, 2, 20),
(34, 3, 2, 20),
(34, 1, 2, 64),
(35, 1, 2, 64);

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(30) NOT NULL,
  `item_type` varchar(30) NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id`, `item_name`, `item_type`, `price`) VALUES
(1, 'Doro Wot', 'Food', 32),
(2, 'Burger', 'Food', 55),
(3, 'Coke', 'Drink', 10),
(4, 'Habesha Beer', 'Drink', 12),
(5, 'bbq', 'Food', 150);

-- --------------------------------------------------------

--
-- Table structure for table `number_of_tables`
--

CREATE TABLE IF NOT EXISTS `number_of_tables` (
  `total` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `number_of_tables`
--

INSERT INTO `number_of_tables` (`total`) VALUES
(8);

-- --------------------------------------------------------

--
-- Table structure for table `orders_list`
--

CREATE TABLE IF NOT EXISTS `orders_list` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `table_number` int(11) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `total_price` float NOT NULL,
  `waiter_id` int(11) NOT NULL,
  `staff_member_id` int(11) NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=36 ;

--
-- Dumping data for table `orders_list`
--

INSERT INTO `orders_list` (`order_id`, `table_number`, `date`, `time`, `total_price`, `waiter_id`, `staff_member_id`) VALUES
(1, 1, '2017-10-28', '10:49:18', 165, 3, 4),
(2, 2, '2017-10-28', '10:51:24', 64, 3, 4),
(5, 4, '2017-10-28', '11:26:54', 291, 5, 2),
(6, 1, '2017-11-27', '04:54:18', 94, 1, 2),
(7, 1, '2017-12-26', '03:01:52', 66, 1, 2),
(8, 4, '2017-12-28', '17:59:22', 201, 1, 2),
(9, 3, '2018-01-19', '06:45:19', 110, 8, 2),
(10, 1, '2018-01-19', '13:09:13', 96, 8, 2),
(11, 1, '2018-01-19', '13:20:11', 96, 8, 2),
(12, 2, '2018-01-19', '13:59:50', 201, 8, 2),
(13, 2, '2018-01-20', '09:28:26', 704, 8, 2),
(14, 2, '2018-01-20', '15:07:09', 96, 8, 2),
(15, 3, '2018-01-20', '15:33:36', 110, 8, 2),
(16, 1, '2018-01-24', '11:53:10', 335, 8, 2),
(17, 1, '2018-01-24', '11:53:13', 0, 8, 2),
(18, 3, '2018-01-24', '11:56:40', 455, 8, 2),
(19, 5, '2018-01-24', '12:03:07', 385, 8, 2),
(20, 1, '2018-01-24', '12:05:04', 192, 8, 2),
(21, 1, '2018-01-24', '12:06:02', 522, 8, 2),
(22, 1, '2018-01-24', '12:06:48', 522, 8, 2),
(23, 1, '2018-01-24', '12:14:53', 96, 8, 2),
(24, 3, '2018-01-24', '12:15:09', 96, 8, 2),
(25, 2, '2018-01-24', '12:18:05', 348, 8, 2),
(26, 2, '2018-01-24', '12:23:18', 192, 8, 2),
(27, 1, '2018-01-26', '10:18:33', 484, 8, 2),
(28, 2, '2018-01-26', '10:26:43', 804, 8, 2),
(29, 1, '2018-01-26', '11:18:49', 195, 8, 2),
(30, 1, '2018-01-26', '11:35:23', 134, 8, 2),
(31, 1, '2018-01-26', '11:40:20', 158, 8, 2),
(32, 1, '2018-01-26', '11:47:02', 202, 8, 2),
(33, 1, '2018-01-26', '11:49:09', 222, 8, 2),
(34, 1, '2018-01-26', '11:59:05', 306, 8, 2),
(35, 1, '2018-01-26', '12:04:45', 370, 8, 2);

-- --------------------------------------------------------

--
-- Table structure for table `staff_members`
--

CREATE TABLE IF NOT EXISTS `staff_members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `isManager` tinyint(1) NOT NULL,
  `last_login` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `staff_members`
--

INSERT INTO `staff_members` (`id`, `user_name`, `password`, `isManager`, `last_login`) VALUES
(1, 'root', 'root', 1, '2018-01-19 10:26:41'),
(2, 'minas', 'minas', 0, '2018-01-26 12:04:37'),
(4, 'Belay', 'belay', 0, '2017-10-28 11:25:27'),
(5, 'Abel', 'abel', 1, '2017-10-27 15:27:27'),
(6, 'Test', 'test', 2, '2018-01-18 15:05:18'),
(11, 'asdfa', 'sdf', 2, '2018-01-18 14:40:15'),
(13, 'here', 'here', 2, '2018-01-26 11:39:32');

-- --------------------------------------------------------

--
-- Table structure for table `waiters`
--

CREATE TABLE IF NOT EXISTS `waiters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `waiters`
--

INSERT INTO `waiters` (`id`, `first_name`, `last_name`, `time`) VALUES
(1, 'Melat', 'Temesgen', '2017-10-27 15:15:02'),
(2, 'Yared', 'Ejigu', '2017-10-27 15:15:36'),
(3, 'Hana', 'Teshale', '2017-10-27 15:15:46'),
(4, 'Samrawit', 'Mekonnen', '2017-10-27 15:16:03'),
(5, 'Birhane', 'Welde Mariam', '2017-10-28 10:54:55'),
(6, 'a', '', '2018-01-18 14:39:19'),
(7, 'asdfa', '', '2018-01-18 14:40:15'),
(8, 'here', '', '2018-01-18 15:06:08');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
