package core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import dao.ExpenseDao;
import dao.UserDao;

public class DebtEngine {
	
	private static Logger logger = Logger.getLogger(DebtEngine.class);
	
	public Debt compute() {
		
		User first = UserDao.getFirstUser();
		User second = UserDao.getSecondUser();
		
		BigDecimal firstUserExpenses = ExpenseDao.geUserExpenses(first);
		BigDecimal secondUserExpenses = ExpenseDao.geUserExpenses(second);
		
		logger.info("Expenses of " + first.getName() + " = " + firstUserExpenses);
		logger.info("Expenses of " + second.getName() + " = " + secondUserExpenses);
		
		if(firstUserExpenses.compareTo(secondUserExpenses) == 1) {
			logger.info(second.getName() + " must pay " + firstUserExpenses.subtract(secondUserExpenses));
			return new Debt(second, first, firstUserExpenses.subtract(secondUserExpenses));
		} else {
			logger.info(first.getName() + " must pay " + secondUserExpenses.subtract(firstUserExpenses));
			return new Debt(first, second, secondUserExpenses.subtract(firstUserExpenses));
		} 
	}

}
