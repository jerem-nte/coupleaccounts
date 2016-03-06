package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import core.Currency;
import core.Expense;
import core.User;
import db.MysqlConnection;

public class ExpenseDao {
	
	private static Logger logger = Logger.getLogger(ExpenseDao.class);

	public static List<Expense> getExpenses(boolean archived) {
		
		List<Expense> expenseList = new ArrayList<Expense>();

		String sql = "SELECT trs.id as trsId, trs.label, trs.amount, trs.scope, trs.archived, usrs.id as userId, trs.currency_id, usrs.name, usrs.gender FROM transactions trs JOIN users usrs on trs.user_id = usrs.id WHERE trs.archived=" + archived;
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);
		
			while (r.next()) {
				Expense e = new Expense(r.getString("trsId"), r.getString("label"), r.getDouble("amount"), r.getString("scope"), r.getBoolean("archived"), new User(r.getString("userId"), r.getString("name"), r.getString("gender")), CurrencyDao.getCurrency(r.getString("currency_id")));
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
	
	
	public static void addExpense(String userId, String label, Double amount, String scope, String currencyId) throws Exception {
		
		String sql = "INSERT INTO transactions (user_id, label, amount, scope, archived, currency_id) values (%s, '%s', %s, '%s', false, '%s')";
		sql = String.format(sql, userId, label, amount, scope, currencyId);
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

	
	public static Map<String, BigDecimal> geUserExpenses(User u) {
		
		Map<String, BigDecimal> expenses = new HashMap<String, BigDecimal>();
		
		// Init a value for each currency. TODO: handle that in SQL request
		for (Currency curr : CurrencyDao.getCurrencies()) {
			expenses.put(curr.getShortname(), new BigDecimal(0));
		}
		
		String sql = "SELECT COALESCE(sum(amount), 0) AS sum_amount, curr.shortname AS currency FROM transactions trs JOIN currency curr on trs.currency_id=curr.id WHERE scope='1' AND archived=false AND user_id=" + u.getId() + " GROUP BY currency_id";
		String sql_shared = "SELECT COALESCE(sum(amount)/2, 0) AS sum_amount, curr.shortname AS currency FROM transactions trs JOIN currency curr on trs.currency_id=curr.id WHERE scope='0' AND archived=false AND user_id=" + u.getId() + " GROUP BY currency_id";
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			
			r = c.createStatement().executeQuery(sql);
			//For each currency
			while(r.next()) {
				expenses.put(r.getString("currency"), expenses.get(r.getString("currency")).add(r.getBigDecimal("sum_amount")));
			}
			r = c.createStatement().executeQuery(sql_shared);
			//For each currency
			while(r.next()) {
				expenses.put(r.getString("currency"), expenses.get(r.getString("currency")).add(r.getBigDecimal("sum_amount")).setScale(2, BigDecimal.ROUND_HALF_UP));
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
		
		return expenses;
	}
	
}