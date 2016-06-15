package db;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import config.DatabaseConfiguration;



public class MysqlConnection implements ISQLConnectionManager {
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private String host;
	private String port;
	private String base;
	private String user;
	private String pass;
	private Connection dbConnection;
	
	private static Logger logger = Logger.getLogger(MysqlConnection.class);
	
	public MysqlConnection() {
		host = DatabaseConfiguration.getInstance().getProperty("host");
		port = DatabaseConfiguration.getInstance().getProperty("port");
		base = DatabaseConfiguration.getInstance().getProperty("base");
		user = DatabaseConfiguration.getInstance().getProperty("user");
		pass = DatabaseConfiguration.getInstance().getProperty("pass");
		
		String url = "jdbc:mysql://" + host + ":" + port + "/" + base;
		
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("Cannot find database driver", e);
		}
		
		try {
			dbConnection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			logger.error("Cannot connect to database, with url = " + url, e);
		}
	}
	
	public Connection getConnection() {
		return dbConnection;
	}
	
	public void testConnection(String host, String port, String base, String user, String pass) throws Exception {
		
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("Cannot find database driver", e);
			throw new Exception("Cannot test connection as no database driver is defined");
		}
		
		String url = "jdbc:mysql://" + host + ":" + port + "/" + base;
		
		try {
			DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			logger.error("Cannot connect to database, with url = " + url, e);
			throw new Exception("Cannot connect to database : " + e.getMessage());
		}
		
	}

	public void importSQL(InputStream in) throws SQLException {
		logger.error("SQL import not implemented.");
	}

}
