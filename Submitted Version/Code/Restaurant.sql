
DROP DATABASE IF EXISTS restaurant2;

CREATE DATABASE restaurant2;

USE restaurant2;

CREATE TABLE IF NOT EXISTS staff_members(
	staff_Id	int(11) NOT NULL AUTO_INCREMENT,
	user_name	VARCHAR(30) NOT NULL,
	pWord	VARCHAR(30) NOT NULL,
	Is_manager	TINYINT NOT NULL,
	last_login_date	DATE NOT NULL,
	last_login_time	TIME NOT NULL,
	PRIMARY KEY(staff_Id)

);

CREATE TABLE IF NOT EXISTS menu(
	menu_Id	int(11) NOT NULL AUTO_INCREMENT,
	Item_name	VARCHAR(30) NOT NULL,
	Item_type	VARCHAR(30) NOT NULL,
	price	FLOAT NOT NULL,
	PRIMARY KEY(menu_Id)
);

CREATE TABLE IF NOT EXISTS waiter(
	waiter_Id	int(11) NOT NULL AUTO_INCREMENT,
	first_name	VARCHAR(30) NOT NULL,
	last_name	VARCHAR(30) NOT NULL,
	last_date_worked	DATE NOT NULL,
	Last_time_worked	TIME NOT NULL,
	PRIMARY KEY(waiter_Id)
);

CREATE TABLE IF NOT EXISTS orders_list(
	order_id	int(11) NOT NULL AUTO_INCREMENT,
	table_number	INT NOT NULL,
	empDate	DATE NOT NULL,
	empTime	TIME NOT NULL,
	Waiter_id	INT NOT NULL,
	staff_member_id	INT NOT NULL,
	PRIMARY KEY(order_id),
	FOREIGN KEY(Waiter_id) REFERENCES waiter (waiter_Id)
	  		ON DELETE CASCADE
			ON UPDATE CASCADE,
	FOREIGN KEY(staff_member_id) REFERENCES staff_members (staff_Id)
	  		ON DELETE CASCADE
			ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Individual_orders(
	Item_id	INT(11) NOT NULL AUTO_INCREMENT,
	#order_id int(11) NOT NULL,
	quantity	INT NOT NULL,
	total_price	FLOAT NOT NULL,
	#PRIMARY KEY(Item_id),
	FOREIGN KEY(Item_id) REFERENCES menu (menu_Id)
	  		ON DELETE CASCADE
			ON UPDATE CASCADE

);

CREATE TABLE IF NOT EXISTS number_of_tables(
	total INT
	#PRIMARY KEY(total)
);

