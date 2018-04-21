-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 25, 2017 at 10:20 AM
-- Server version: 5.6.24
-- PHP Version: 5.6.8

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
  `quantity` float NOT NULL,
  `total_price` int(11) NOT NULL
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
(6, 1, 4, 128),
(7, 2, 2, 110),
(8, 1, 10, 320),
(9, 4, 1, 12),
(10, 5, 10, 260),
(11, 5, 20, 520),
(12, 1, 100, 3200),
(13, 2, 11, 605),
(14, 1, 15, 480),
(16, 1, 14, 448);

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `id` int(11) NOT NULL,
  `item_name` varchar(30) NOT NULL,
  `item_type` varchar(30) NOT NULL,
  `price` float NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id`, `item_name`, `item_type`, `price`) VALUES
(1, 'Doro Wot', 'Food', 32),
(2, 'Burger', 'Food', 55),
(3, 'Coke', 'Drink', 10),
(4, 'Habesha Beer', 'Drink', 12),
(5, 'Special', 'Food', 26);

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
(10);

-- --------------------------------------------------------

--
-- Table structure for table `orders_list`
--

CREATE TABLE IF NOT EXISTS `orders_list` (
  `order_id` int(11) NOT NULL,
  `table_number` int(11) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `total_price` float NOT NULL,
  `waiter_id` int(11) NOT NULL,
  `staff_member_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `orders_list`
--

INSERT INTO `orders_list` (`order_id`, `table_number`, `date`, `time`, `total_price`, `waiter_id`, `staff_member_id`) VALUES
(1, 1, '2017-10-28', '10:49:18', 165, 3, 4),
(2, 2, '2017-10-28', '10:51:24', 64, 3, 4),
(3, 2, '2017-10-28', '10:57:27', 14062200000, 1, 4),
(4, 3, '2017-10-28', '11:25:44', 2910, 4, 4),
(5, 4, '2017-10-28', '11:26:54', 291, 5, 2),
(6, 8, '2017-11-27', '19:50:08', 128, 3, 2),
(7, 4, '2017-12-22', '21:32:35', 110, 3, 4),
(8, 7, '2017-12-22', '21:34:25', 320, 2, 4),
(9, 3, '2017-12-23', '11:09:40', 12, 1, 4),
(10, 10, '2017-12-23', '11:12:05', 260, 1, 4),
(11, 9, '2017-12-23', '11:17:58', 520, 5, 4),
(12, 3, '2017-12-23', '12:28:46', 3200, 2, 4),
(13, 2, '2017-12-23', '12:30:36', 605, 2, 4),
(14, 2, '2017-12-23', '20:36:24', 480, 1, 4),
(15, 2, '2017-12-23', '20:36:50', 0, 1, 4),
(16, 3, '2017-12-25', '11:02:22', 448, 1, 4);

-- --------------------------------------------------------

--
-- Table structure for table `staff_members`
--

CREATE TABLE IF NOT EXISTS `staff_members` (
  `id` int(11) NOT NULL,
  `user_name` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `isManager` tinyint(1) NOT NULL,
  `last_login` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `staff_members`
--

INSERT INTO `staff_members` (`id`, `user_name`, `password`, `isManager`, `last_login`) VALUES
(1, 'root', 'root', 1, '2017-11-27 19:53:13'),
(2, 'minas', 'minas', 0, '2017-11-27 19:57:53'),
(4, 'Belay', 'belay', 0, '2017-12-25 11:49:05'),
(5, 'Abel', 'abel', 1, '2017-12-23 10:18:23'),
(6, 'Belayneh', 'belayneh', 1, '2017-12-25 11:00:48');

-- --------------------------------------------------------

--
-- Table structure for table `waiters`
--

CREATE TABLE IF NOT EXISTS `waiters` (
  `id` int(11) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `waiters`
--

INSERT INTO `waiters` (`id`, `first_name`, `last_name`, `time`) VALUES
(1, 'Melat', 'Temesgen', '2017-10-27 15:15:02'),
(2, 'Yared', 'Ejigu', '2017-10-27 15:15:36'),
(3, 'Hana', 'Teshale', '2017-10-27 15:15:46'),
(4, 'Samrawit', 'Mekonnen', '2017-10-27 15:16:03'),
(5, 'Birhane', 'Welde Mariam', '2017-10-28 10:54:55');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders_list`
--
ALTER TABLE `orders_list`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `staff_members`
--
ALTER TABLE `staff_members`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `waiters`
--
ALTER TABLE `waiters`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `orders_list`
--
ALTER TABLE `orders_list`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `staff_members`
--
ALTER TABLE `staff_members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `waiters`
--
ALTER TABLE `waiters`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
