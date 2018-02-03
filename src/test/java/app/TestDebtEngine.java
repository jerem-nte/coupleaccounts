package app;


import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import core.Debt;
import core.DebtEngine;
import dao.CurrencyDao;
import dao.ExpenseDao;
import dao.UserDao;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"../config/delete_transactions.sql"})
public class TestDebtEngine {
	
	@Autowired
	ExpenseDao expenseDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	CurrencyDao currencyDao;
	
	@Autowired
	DebtEngine engine;

	
	@Test
	public void computeUser1() throws Exception {
		
		List<Debt> debts;
		
		expenseDao.addExpense(userDao.getFirstUser().getId(), "test", 10.00, "1", "1");
		expenseDao.addExpense(userDao.getSecondUser().getId(), "test", 0.0, "1", "1");
		
		debts = engine.compute();
		assertTrue(debts.get(0).getDebt().compareTo(new BigDecimal("10")) == 0);
		assertTrue(debts.get(0).getCredit().getName().equals(userDao.getFirstUser().getName()));
	}
	
	@Test
	public void computeUser2() throws Exception {
		
		List<Debt> debts;
		
		expenseDao.addExpense(userDao.getFirstUser().getId(), "test", 10.00, "1", "1");
		expenseDao.addExpense(userDao.getSecondUser().getId(), "test", 20.025, "1", "1");
		
		debts = engine.compute();
		
		assertTrue(debts.get(0).getDebt().compareTo(new BigDecimal("10.03")) == 0);
		assertTrue(debts.get(0).getCredit().getName().equals(userDao.getSecondUser().getName()));
	}
	
	@Test
	public void computeEmptyWithExpense() throws Exception {
		
		List<Debt> debts;
		
		expenseDao.addExpense(userDao.getFirstUser().getId(), "test", 20.03, "0", "1");
		expenseDao.addExpense(userDao.getSecondUser().getId(), "test", 20.025, "0", "1");
		
		debts = engine.compute();
		assertTrue(debts.isEmpty());
	}
	
	@Test
	public void computeEmptyWithoutExpense() throws Exception {
		
		List<Debt> debts;
		
		debts = engine.compute();
		
		assertTrue(debts.isEmpty());
	}
	
	@Test
	public void compute1ct() throws Exception {
		
		List<Debt> debts;
		
		expenseDao.addExpense(userDao.getFirstUser().getId(), "test", 10.05, "1", "1");
		expenseDao.addExpense(userDao.getSecondUser().getId(), "test", 10.044, "1", "1");
		
		debts = engine.compute();
		
		assertTrue(debts.get(0).getDebt().compareTo(new BigDecimal("0.01")) == 0);
	}	
	
	@Test
	public void testRounding() {
		
		int scale = 2;
		int rounding = BigDecimal.ROUND_HALF_UP;
		
		BigDecimal a = new BigDecimal("0").setScale(scale, rounding);
		BigDecimal b = new BigDecimal("10").setScale(scale, rounding);
		BigDecimal c = new BigDecimal("10.046").setScale(scale, rounding);
		BigDecimal d = new BigDecimal("10.05").setScale(scale, rounding);
		BigDecimal e = new BigDecimal("10.051").setScale(scale, rounding);
		BigDecimal f = new BigDecimal("10.055").setScale(scale, rounding);
		BigDecimal g = new BigDecimal("10.056").setScale(scale, rounding);
		
		assertTrue(a.compareTo(new BigDecimal("0"))==0);
		assertTrue(b.compareTo(new BigDecimal("10"))==0);
		assertTrue(c.compareTo(new BigDecimal("10.05"))==0);
		assertTrue(d.compareTo(new BigDecimal("10.05"))==0);
		assertTrue(e.compareTo(new BigDecimal("10.05"))==0);
		assertTrue(f.compareTo(new BigDecimal("10.06"))==0);
		assertTrue(g.compareTo(new BigDecimal("10.06"))==0);
	}

}
