package services;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.Debt;
import core.DebtEngine;
import core.Expense;
import dao.ExpenseDao;
import dto.PaginatedExpenseList;
import dto.ResponseDto;

@RestController
@RequestMapping("/expense")
public class ExpenseService {
	
	private static Logger logger = Logger.getLogger(ExpenseService.class);
	@Autowired
	private ExpenseDao expenseDao;
	
	@Autowired
	private DebtEngine de;
	
	@RequestMapping("list/notarchived")
    public List<Expense> listAll() throws SQLException {
    	
		List<Expense> expenses = expenseDao.getExpenses(false, -1);
		return expenses; 
    }
	
	@RequestMapping("list/archived")
    public PaginatedExpenseList listArchived(@RequestParam("page") String pageParam) throws SQLException {
		
		Integer page = null;
		try {
    		page = new Integer(pageParam);
		} catch (Exception e) {
			page = new Integer(-1);
		}
		
		List<Expense> expenses = expenseDao.getExpenses(true, page);
		Integer pageMax = expenseDao.getPageMax(true);
		
		return new PaginatedExpenseList(pageMax, expenses);
    }
	
    @RequestMapping(value="delete", method = RequestMethod.POST)
    public ResponseDto delete(@RequestBody List<String> ids) {
    	
    	if(ids.size() == 0)
    		return new ResponseDto(1, "Nothing to delete");
    	
    	try {
    		for (final String id : ids) {
    			expenseDao.deleteExpense(id);
    	    }
		} catch (Exception e) {
			return new ResponseDto(1, "Cannot delete expense : " + e.getMessage());
		}
    	
    	return new ResponseDto(0, "Expense successfully deleted");
    }
    
    @RequestMapping(value="archive", method = RequestMethod.POST)
    public ResponseDto archive(@RequestBody List<String> ids) {
    	
    	if(ids.size() == 0)
    		return new ResponseDto(1, "Nothing to archive");
    	
    	try {
    		for (final String id : ids) {
    			expenseDao.archiveExpense(id);
    	    }
		} catch (Exception e) {
			return new ResponseDto(1, "Cannot archive expense : " + e.getMessage());
		}
    	
    	return new ResponseDto(0, "Expense successfully archived");
    }
    
    
    @RequestMapping(value="add", method = RequestMethod.POST)
    public ResponseDto add(@RequestBody Map<String, Object> json) {
    	
    	Double amountDouble;
    	
    	try {
    		amountDouble = Double.parseDouble(String.valueOf(json.get("amount")));
    	} catch(Exception e) {
    		logger.error("Please enter a valid amount");
    		return new ResponseDto(1, "Please enter a valid amount");
    	}
    	
    	if(amountDouble < 0) {
    		logger.error("An expense cannot be negative");
    		return new ResponseDto(1, "An expense cannot be negative");
    	}
    	
    	try {
    		expenseDao.addExpense((String)json.get("userId"), (String)json.get("label"), amountDouble, (String) json.get("scope"), (String)json.get("currencyId"));
		} catch (Exception e) {
			logger.error("Cannot add expense", e);
			return new ResponseDto(1, "Cannot add expense : " + e.getMessage());
		}
    	
    	return new ResponseDto(0, "Expense successfully added");
    	
    }
    
    @RequestMapping(value="debt", method = RequestMethod.POST)
    public List<Debt> getUserDebt() throws SQLException {
    	
    	return de.compute();
    }
    
    @RequestMapping(value="exist", method = RequestMethod.POST)
    public Boolean isSimilarExpenseExist(@RequestBody Map<String, Object> json, HttpServletResponse response) throws SQLException {
    	
    	Double amountDouble;
    	try {
    		amountDouble = Double.parseDouble(String.valueOf(json.get("amount")));
    	} catch(Exception e) {
    		logger.warn("Cannot check if similar transaction exist : Please enter a valid amount");
    		return false;
    	}
    	
    	return expenseDao.isSimilarExpenseExist((String)json.get("userId"), amountDouble, (String) json.get("scope"), (String)json.get("currencyId"));
    }
}
  