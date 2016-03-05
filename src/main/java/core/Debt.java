package core;

import java.math.BigDecimal;

public class Debt {
	
	private User debit;
	private User credit;
	private BigDecimal debt;
	
	
	public Debt(User debit, User credit, BigDecimal debt) {
		super();
		this.debit = debit;
		this.credit = credit;
		this.debt = debt;
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
	
	

}
