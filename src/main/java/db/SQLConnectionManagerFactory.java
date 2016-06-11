package db;

public class SQLConnectionManagerFactory {
	
	public static ISQLConnectionManager create() {
		return new MysqlConnection();
	}

}
