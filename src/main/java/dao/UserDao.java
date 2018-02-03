package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import core.User;

@Repository
public class UserDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<User> getUsers() throws SQLException {
		
		return jdbcTemplate.query("SELECT * FROM users", new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet r, int rowNum) throws SQLException {
				return new User(r.getString("id"), r.getString("name"), r.getString("gender"));
			}
		});
	}
	
	public User getUser(String userId) throws SQLException {
		
		return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?", new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet r, int rowNum) throws SQLException {
				return new User(r.getString("id"), r.getString("name"), r.getString("gender"));
			}
		}, userId);
	}
	
	public void update(User user) throws Exception {
		
		jdbcTemplate.update("UPDATE users SET name=? WHERE id=?", user.getName(), user.getId());
	}
	
	public User getFirstUser() throws SQLException {
		
		return getUser("1");
	}
	
	public User getSecondUser() throws SQLException {
		
		return getUser("2");
	}
}
