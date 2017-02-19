package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import core.User;

@Repository
public class UserDao {
	
	private static Logger logger = Logger.getLogger(UserDao.class);
	
	@Autowired
	private DataSource dataSource;
	
	public List<User> getUsers() throws SQLException {
		
		List<User> userList = new ArrayList<User>();

		String sql = "SELECT * FROM users";
		
		Connection c = dataSource.getConnection();
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
	
	
	public User getUser(String userId) throws SQLException {
		
		User u = null;
		
		String sql = "SELECT * FROM users WHERE id=" + userId;
		
		Connection c = dataSource.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);

			if(r.next()) {
				u = new User(r.getString("id"), r.getString("name"), r.getString("gender"));
			}
		} catch (SQLException e) {
			logger.error("Cannot retreive user from database", e);
			return null;
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
			}
		}
		
		return u;
	}
	
	public void update(User user) throws Exception {
		
		Connection c = dataSource.getConnection();
		PreparedStatement prep;
		
		try {
			prep = c.prepareStatement("UPDATE users SET name = ? WHERE id = ?");
			prep.setString(1, user.getName());
			prep.setString(2, user.getId());
			logger.debug("Update = " + prep);
		} catch (SQLException e) {
			logger.error("Cannot prepare SQL query for updating a user", e);
			throw new Exception("Cannot prepare SQL query for updating a user");
		} 
		
		try {
			prep.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error while updating user", e);
			throw new Exception("Error while updating user");
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
				throw new Exception("Error while updating user");
			}
		}
		
	}
	
	public User getFirstUser() throws SQLException {
		
		return getUser("1");
	}
	
	public User getSecondUser() throws SQLException {
		
		return getUser("2");
	}
	
	

}
