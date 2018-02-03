package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import core.DebtEngine;
import dao.CurrencyDao;
import dao.ExpenseDao;
import dao.UserDao;

@Configuration
public class BeanConfiguration {
	
	
	@Bean
	public CurrencyDao getCurrencyDao() {
		
		return new CurrencyDao();
	}
	
	@Bean
	public ExpenseDao getExpenseDao() {
		
		return new ExpenseDao();
	}
	
	@Bean
	public UserDao getUserDao() {
		
		return new UserDao();
	}
	
	@Bean
	public DebtEngine getDebtEngine() {
		
		return new DebtEngine();
	}

}
