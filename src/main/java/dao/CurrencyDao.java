package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import core.Currency;

@Repository
public class CurrencyDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Currency> getCurrencies() {
		
		return jdbcTemplate.query("SELECT * FROM currency", new RowMapper<Currency>() {
			@Override
			public Currency mapRow(ResultSet r, int rowNum) throws SQLException {
				return new Currency(r.getString("id"), r.getString("name"), r.getString("shortname"), r.getString("icon"));
			}
		});
	}
	
	public Currency getCurrency(String id) throws SQLException {
		
		String sql = "SELECT * FROM currency WHERE id=?";
		
		return jdbcTemplate.queryForObject(sql, new RowMapper<Currency>() {
			@Override
			public Currency mapRow(ResultSet r, int rowNum) throws SQLException {
				return new Currency(r.getString("id"), r.getString("name"), r.getString("shortname"), r.getString("icon"));
			}
		}, id);
	}
}
