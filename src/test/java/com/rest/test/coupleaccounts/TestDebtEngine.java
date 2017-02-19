package com.rest.test.coupleaccounts;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import core.Debt;
import core.DebtEngine;
import core.User;
import dao.ExpenseDao;
import dao.UserDao;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ExpenseDao.class, UserDao.class})
public class TestDebtEngine {
	
	@Autowired
	ExpenseDao expenseDao;
	
	@Autowired
	UserDao userDao;
	
	private User u1;
	private User u2;
	private Map<String, BigDecimal> firstUserExpenses = new HashMap<String, BigDecimal>();
	private Map<String, BigDecimal> secondUserExpenses = new HashMap<String, BigDecimal>();
	
	@Before
	public void initUsers() throws SQLException {
		
		u1 = new User("1", "Jeremy", "f");
		u2 = new User("2", "Chloe", "f");
		
		PowerMockito.mockStatic(UserDao.class);
		Mockito.when(userDao.getFirstUser()).thenReturn(u1);
		Mockito.when(userDao.getSecondUser()).thenReturn(u2);
		
		firstUserExpenses.clear();
		secondUserExpenses.clear();
		
		PowerMockito.mockStatic(ExpenseDao.class);
		Mockito.when(expenseDao.geUserExpenses(u1)).thenReturn(firstUserExpenses);
		Mockito.when(expenseDao.geUserExpenses(u2)).thenReturn(secondUserExpenses);
		
	}
	
	
	@Test
	public void computeUser1() throws SQLException {
		
		DebtEngine engine = new DebtEngine();
		List<Debt> debts;
		
		firstUserExpenses.put("EUR", new BigDecimal("10").setScale(2,  BigDecimal.ROUND_HALF_UP));
		secondUserExpenses.put("EUR", new BigDecimal("0").setScale(2,  BigDecimal.ROUND_HALF_UP));
		
		debts = engine.compute();
		assertTrue(debts.get(0).getDebt().compareTo(new BigDecimal("10")) == 0);
		assertTrue(debts.get(0).getCredit().getName().equals(u1.getName()));
	}
	
	@Test
	public void computeUser2() throws SQLException {
		
		DebtEngine engine = new DebtEngine();
		List<Debt> debts;
		
		firstUserExpenses.put("EUR", new BigDecimal("10").setScale(2,  BigDecimal.ROUND_HALF_UP));
		secondUserExpenses.put("EUR", new BigDecimal("20.025").setScale(2,  BigDecimal.ROUND_HALF_UP));
		
		debts = engine.compute();
		assertTrue(debts.get(0).getDebt().compareTo(new BigDecimal("10.03")) == 0);
		assertTrue(debts.get(0).getCredit().getName().equals(u2.getName()));
	}
	
	@Test
	public void computeEmptyWithExpense() throws SQLException {
		
		DebtEngine engine = new DebtEngine();
		List<Debt> debts;
		
		firstUserExpenses.put("EUR", new BigDecimal("20.03").setScale(2,  BigDecimal.ROUND_HALF_UP));
		secondUserExpenses.put("EUR", new BigDecimal("20.025").setScale(2,  BigDecimal.ROUND_HALF_UP));
		
		debts = engine.compute();
		assertTrue(debts.isEmpty());
	}
	
	@Test
	public void computeEmptyWithoutExpense() throws SQLException {
		
		DebtEngine engine = new DebtEngine();
		List<Debt> debts;
		
		firstUserExpenses.put("EUR", new BigDecimal("0").setScale(2,  BigDecimal.ROUND_HALF_UP));
		secondUserExpenses.put("EUR", new BigDecimal("0").setScale(2,  BigDecimal.ROUND_HALF_UP));
		
		debts = engine.compute();
		assertTrue(debts.isEmpty());
	}
	
	@Test
	public void compute1ct() throws SQLException {
		
		DebtEngine engine = new DebtEngine();
		List<Debt> debts;
		
		firstUserExpenses.put("EUR", new BigDecimal("10.05").setScale(2,  BigDecimal.ROUND_HALF_UP));
		secondUserExpenses.put("EUR", new BigDecimal("10.044").setScale(2,  BigDecimal.ROUND_HALF_UP));
		
		debts = engine.compute();
		assertTrue(debts.get(0).getDebt().compareTo(new BigDecimal("0.01")) == 0);
	}	
	
	@Test
	public void testRounding() {
		
		System.out.println("Test rounding");
		
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
