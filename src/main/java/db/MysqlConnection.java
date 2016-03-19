package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import config.DatabaseConfiguration;



public class MysqlConnection {
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static String host;
	private static String port;
	private static String base;
	private static String user;
	private static String pass;
	
	private static Logger logger = Logger.getLogger(MysqlConnection.class);
	
	public static Connection getConnection() {
		
		host = DatabaseConfiguration.getInstance().getProperty("host");
		port = DatabaseConfiguration.getInstance().getProperty("port");
		base = DatabaseConfiguration.getInstance().getProperty("base");
		user = DatabaseConfiguration.getInstance().getProperty("user");
		pass = DatabaseConfiguration.getInstance().getProperty("pass");
		
		String url = "jdbc:mysql://" + host + ":" + port + "/" + base;
		
		Connection dbConnection = null;

		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("Cannot find database driver", e);
		}

		try {

			dbConnection = DriverManager.getConnection(url, user, pass);
			return dbConnection;

		} catch (SQLException e) {
			logger.error("Cannot connect to database, with url = " + url, e);
		}

		return dbConnection;
	}

}
