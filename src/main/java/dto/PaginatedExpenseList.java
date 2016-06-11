package dto;

import java.util.List;

import core.Expense;

public class PaginatedExpenseList {
	
	private int pageMax;
	private List<Expense> expenses;
	
	public PaginatedExpenseList(int pageMax, List<Expense> expenses) {
		this.pageMax = pageMax;
		this.expenses = expenses;
	}
	
	public int getPageMax() {
		return pageMax;
	}
	public void setPageMax(int pageMax) {
		this.pageMax = pageMax;
	}
	public List<Expense> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
	
	

}
