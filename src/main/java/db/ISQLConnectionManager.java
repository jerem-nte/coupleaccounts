package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public interface ISQLConnectionManager {
	
	public Connection getConnection();
	
	public void testConnection(String host, String port, String base, String user, String pass) throws Exception;
	
	public void importSQL(InputStream in) throws SQLException;

}
