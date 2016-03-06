package core;

import java.math.BigDecimal;

public class Debt {
	
	private User debit;
	private User credit;
	private BigDecimal debt;
	private String currency;
	
	
	public Debt(User debit, User credit, BigDecimal debt, String currency) {
		super();
		this.debit = debit;
		this.credit = credit;
		this.debt = debt;
		this.currency = currency;
	}
	
	
	public User getDebit() {
		return debit;
	}
	public void setDebit(User debit) {
		this.debit = debit;
	}
	public User getCredit() {
		return credit;
	}
	public void setCredit(User credit) {
		this.credit = credit;
	}
	public BigDecimal getDebt() {
		return debt;
	}
	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	

}
