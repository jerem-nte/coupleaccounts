package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import core.Expense;
import core.User;
import db.MysqlConnection;

public class ExpenseDao {
	
	private static Logger logger = Logger.getLogger(ExpenseDao.class);

	public static List<Expense> getExpenses(boolean archived) {
		
		List<Expense> expenseList = new ArrayList<Expense>();

		String sql = "SELECT trs.id as trsId, trs.label, trs.amount, trs.scope, trs.archived, usrs.id as userId, usrs.name, usrs.gender FROM transactions trs JOIN users usrs on trs.user_id = usrs.id WHERE trs.archived=" + archived;
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);
		
			while (r.next()) {
				Expense e = new Expense(r.getString("trsId"), r.getString("label"), r.getDouble("amount"), r.getString("scope"), r.getBoolean("archived"), new User(r.getString("userId"), r.getString("name"), r.getString("gender")));
				expenseList.add(e);
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
		
		return expenseList;
	}
	
	
	public static void addExpense(String userId, String label, Double amount, String scope) throws Exception {
		
		String sql = "INSERT INTO transactions (user_id, label, amount, scope, archived) values (%s, '%s', %s, '%s', false)";
		sql = String.format(sql, userId, label, amount, scope);
		logger.info("Add = " + sql);
		
		Connection c = MysqlConnection.getConnection();
		
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
			}
		}
	}
	
	public static void deleteExpense(String id) throws Exception {
		
		String sql = "DELETE FROM transactions WHERE id = %s";
		sql = String.format(sql, id);
		logger.info("Delete = " + sql);
		
		Connection c = MysqlConnection.getConnection();
		
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
			}
		}
	}
	
	 
	public static void archiveExpense(String id) throws Exception {
		
		String sql = "UPDATE transactions SET archived=true WHERE id = %s";
		sql = String.format(sql, id);
		logger.info("Archive = " + sql);
		
		Connection c = MysqlConnection.getConnection();
		
		try {
			c.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
			}
		}
	}
	
	public static Map<User, Double> getSharedExpensesSum() {
		
		Map<User, Double> sharedExpenses = new HashMap<User, Double>();
		
		String sql = "SELECT usrs.id as id, name, gender, sum(amount) AS sum_amount FROM transactions trs JOIN users usrs ON usrs.id = trs.user_id WHERE scope='0' AND archived=false GROUP BY id";
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);
		
			while (r.next()) {
				User u = new User(r.getString("id"), r.getString("name"), r.getString("gender"));
				Double amount = r.getDouble("sum_amount");
				sharedExpenses.put(u, amount);
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
		
		return sharedExpenses;
	}
	
	public static Map<User, Double> getNotSharedExpensesSum() {
		
		Map<User, Double> notSharedExpenses = new HashMap<User, Double>();
		
		String sql = "SELECT usrs.id as id, name, gender, sum(amount) AS sum_amount FROM transactions trs JOIN users usrs ON usrs.id = trs.user_id WHERE scope='1' and archived=false GROUP BY id";
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);
		
			while (r.next()) {
				User u = new User(r.getString("id"), r.getString("name"), r.getString("gender"));
				Double amount = r.getDouble("sum_amount");
				notSharedExpenses.put(u, amount);
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
		
		return notSharedExpenses;
	}
	
}