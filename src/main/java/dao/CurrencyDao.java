package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import core.Currency;
import db.MysqlConnection;

public class CurrencyDao {

	private static Logger logger = Logger.getLogger(CurrencyDao.class);
	
	public static List<Currency> getCurrencies() {
		
		List<Currency> currencyList = new ArrayList<Currency>();

		String sql = "SELECT * FROM currency";
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);

			while (r.next()) {
			   Currency curr = new Currency(r.getString("id"), r.getString("name"), r.getString("shortname"), r.getString("icon"));
			   currencyList.add(curr);
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
		
		return currencyList;
	}
	
	public static Currency getCurrency(String id) {
		
		String sql = "SELECT * FROM currency WHERE id="+id;
		
		Connection c = MysqlConnection.getConnection();
		ResultSet r;
		
		try {
			r = c.createStatement().executeQuery(sql);

			if(r.next()) {
			   return new Currency(r.getString("id"), r.getString("name"), r.getString("shortname"), r.getString("icon"));
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
		return null;
	}
}
