package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import core.User;
import db.MysqlConnection;

public class UserDao {
	
	private static Logger logger = Logger.getLogger(UserDao.class);
	
	public static List<User> getUsers() {
		
		List userList = new ArrayList<User>();

		String sql = "SELECT * FROM users";
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);

			while (r.next()) {
			   User u = new User(r.getString("id"), r.getString("name"), r.getString("gender"));
			   userList.add(u);
			}
		} catch (SQLException e) {
			logger.error("Cannot retreive users from database", e);
			return null;
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
			}
		}
		
		return userList;
	}

}
