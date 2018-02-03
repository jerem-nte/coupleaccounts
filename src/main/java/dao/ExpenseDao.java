package dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import core.Currency;
import core.Expense;
import core.User;

@Repository
public class ExpenseDao {
	
	private static final int PAGE_SIZE = 15;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	CurrencyDao currencyDao;

	public List<Expense> getExpenses(boolean archived, int page) {
		
		StringBuilder sqlSb = new StringBuilder("SELECT trs.id as trsId, trs.label, trs.amount, trs.scope, trs.archived, usrs.id as userId, trs.currency_id, usrs.name, usrs.gender");
		sqlSb.append(" FROM transactions trs");
		sqlSb.append(" JOIN users usrs on trs.user_id = usrs.id ");
		sqlSb.append(" WHERE trs.archived=").append(archived ? "1" : "0");
		sqlSb.append(" ORDER BY trs.id DESC");
		if(page > 0)
			sqlSb.append(" LIMIT ").append(PAGE_SIZE).append(" OFFSET ").append((page-1)*PAGE_SIZE);
		
		return jdbcTemplate.query(sqlSb.toString(), new RowMapper<Expense>() {
			@Override
			public Expense mapRow(ResultSet r, int rowNum) throws SQLException {
				return new Expense(r.getString("trsId"), r.getString("label"), r.getDouble("amount"), r.getString("scope"), r.getBoolean("archived"), new User(r.getString("userId"), r.getString("name"), r.getString("gender")), currencyDao.getCurrency(r.getString("currency_id")));
			}
		});
	}
	
	
	public Integer getPageMax(boolean archived) {
		
		Integer nbTrs = 1;

		StringBuilder sqlSb = new StringBuilder("SELECT count(trs.id) as nbTrs");
		sqlSb.append(" FROM transactions trs");
		sqlSb.append(" WHERE trs.archived=").append(archived ? "1" : "0");
		
		nbTrs = jdbcTemplate.queryForObject(sqlSb.toString(), Integer.class);
		
		return (int)Math.ceil((double)nbTrs/PAGE_SIZE);
	}
	
	
	public void addExpense(String userId, String label, Double amount, String scope, String currencyId) {
		
		String sql = "INSERT INTO transactions (user_id, label, amount, scope, archived, currency_id) values (?, ?, ?, ?, 0, ?)";
		jdbcTemplate.update(sql, userId, label, amount, scope, currencyId);
	}
	
	public void deleteExpense(String id) {
		
		String sql = "DELETE FROM transactions WHERE id=?";
		jdbcTemplate.update(sql, id);
	}
	
	 
	public void archiveExpense(String id) {
		
		String sql = "UPDATE transactions SET archived=1 WHERE id=?";
		jdbcTemplate.update(sql, id);
	}

	
	public Map<String, BigDecimal> geUserExpenses(User u) throws SQLException {
		
		final Map<String, BigDecimal> expenses = new HashMap<String, BigDecimal>();
		
		// Init a value for each currency. TODO: handle that in SQL request
		for (Currency curr : currencyDao.getCurrencies()) {
			expenses.put(curr.getId(), new BigDecimal(0));
		}
		
		String sql = "SELECT COALESCE(sum(amount), 0) AS sum_amount, currency_id FROM transactions trs WHERE scope='1' AND archived=0 AND user_id=" + u.getId() + " GROUP BY currency_id";
		String sql_shared = "SELECT COALESCE(sum(amount)/2, 0) AS sum_amount, currency_id FROM transactions trs WHERE scope='0' AND archived=0 AND user_id=" + u.getId() + " GROUP BY currency_id";
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet r) throws SQLException {
				expenses.put(r.getString("currency_id"), expenses.get(r.getString("currency_id")).add(r.getBigDecimal("sum_amount")));
			}
		});
		
		jdbcTemplate.query(sql_shared, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet r) throws SQLException {
				expenses.put(r.getString("currency_id"), expenses.get(r.getString("currency_id")).add(r.getBigDecimal("sum_amount")).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		});
		
		return expenses;
	}


	public boolean isSimilarExpenseExist(String userId, Double amount, String scope, String currencyId) throws SQLException {
		String sql = "SELECT count(*) as nb_transactions FROM transactions WHERE archived=0 AND user_id=" + userId + " AND amount=" + amount + " AND scope=" + scope + " AND currency_id=" + currencyId;
		
		Integer nbSimilarExpense = jdbcTemplate.queryForObject(sql, Integer.class);
		
		return nbSimilarExpense > 0;
	}
	
}