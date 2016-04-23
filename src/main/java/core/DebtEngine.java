package core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import dao.CurrencyDao;
import dao.ExpenseDao;
import dao.UserDao;

public class DebtEngine {
	
	private static Logger logger = Logger.getLogger(DebtEngine.class);
	
	public List<Debt> compute() {
		
		User first = UserDao.getFirstUser();
		User second = UserDao.getSecondUser();
		
		List<Debt> debts = new ArrayList<Debt>();
		
		Map<String, BigDecimal> firstUserExpenses = ExpenseDao.geUserExpenses(first);
		Map<String, BigDecimal> secondUserExpenses = ExpenseDao.geUserExpenses(second);
		
		logger.info("Expenses of " + first.getName() + " = " + firstUserExpenses);
		logger.info("Expenses of " + second.getName() + " = " + secondUserExpenses);
		
		
		for (Entry<String, BigDecimal> entry : firstUserExpenses.entrySet())
		{
			BigDecimal firstUserExpense = entry.getValue();
			BigDecimal secondUserExpense = secondUserExpenses.get(entry.getKey());
			
			if(firstUserExpense.compareTo(secondUserExpense) == 1) {
				logger.info(second.getName() + " must pay " + firstUserExpense.subtract(secondUserExpense) + " " + entry.getKey());
				debts.add( new Debt(second, first, firstUserExpense.subtract(secondUserExpense), CurrencyDao.getCurrency(entry.getKey())) );
			} else {
				logger.info(first.getName() + " must pay " + secondUserExpense.subtract(firstUserExpense) + " " + entry.getKey());
				debts.add( new Debt(first, second, secondUserExpense.subtract(firstUserExpense), CurrencyDao.getCurrency(entry.getKey())) );
			} 
		}
		
		return debts;
	}

}
