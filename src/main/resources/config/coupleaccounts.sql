CREATE TABLE IF NOT EXISTS currency (
	id int(11) auto_increment NOT NULL PRIMARY KEY,
  	name varchar(20) NOT NULL,
  	shortname varchar(10) NOT NULL,
  	icon varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
	id int(11) auto_increment NOT NULL PRIMARY KEY ,
  	name varchar(20) NOT NULL,
  	gender char(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
	id int(11) auto_increment NOT NULL PRIMARY KEY,
	user_id int(11) NOT NULL,
  	currency_id int(11) NOT NULL,
  	label varchar(255) NOT NULL,
  	amount decimal(10,2) NOT NULL,
  	scope varchar(50) NOT NULL,
	archived tinyint(1) NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (currency_id) REFERENCES currency(id),
);