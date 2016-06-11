package db;

import java.sql.Connection;

public interface ISQLConnectionManager {
	
	public Connection getConnection();
	
	public void testConnection(String host, String port, String base, String user, String pass) throws Exception;


}
