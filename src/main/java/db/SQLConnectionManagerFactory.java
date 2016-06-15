package db;

public class SQLConnectionManagerFactory {
	
	private static SQLConnectionManagerFactory instance = new SQLConnectionManagerFactory();
	
	private boolean memoryMode = false;
	
	private SQLConnectionManagerFactory() {}
	
	public ISQLConnectionManager create() {
		
		if(memoryMode) {
			return new SQLiteConnection();	
		}
		else {
			return new MysqlConnection();	
		}
	}

	public static SQLConnectionManagerFactory getInstance() {
		return instance;
	}
	
	public void activateMemoryMode() {
		this.memoryMode = true;
	}
}
