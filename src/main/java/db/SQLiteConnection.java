package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class SQLiteConnection implements ISQLConnectionManager {

	private static Logger logger = Logger.getLogger(SQLiteConnection.class);

	public Connection getConnection() {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite::memory::");
		} catch (Exception e) {
			logger.error("Cannot create connection ", e);
		}
		return c;
	}

	public void testConnection(String host, String port, String base, String user, String pass) throws Exception {
		logger.error("Connection test not implemented in memory mode.");
	}

	public void importSQL(InputStream in) throws SQLException {
		Scanner s = new Scanner(in);
		s.useDelimiter("(;(\r)?\n)|(--\n)");
		Statement st = null;
		Connection conn = getConnection();
		try {
			st = conn.createStatement();
			while (s.hasNext()) {
				String line = s.next();
				if (line.startsWith("/*!") && line.endsWith("*/")) {
					int i = line.indexOf(' ');
					line = line.substring(i + 1, line.length() - " */".length());
				}

				if (line.trim().length() > 0) {
					logger.debug("Execute line " + line);
					st.execute(line);
				}
			}
		} finally {
			if (st != null)
				st.close();
			conn.close();
			s.close();
		}
	}

}
