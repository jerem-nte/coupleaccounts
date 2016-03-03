package core;

public class Debt {
	
	private User debit;
	private User credit;
	private Double debt;
	
	
	public Debt(User debit, User credit, Double debt) {
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
	public Double getDebt() {
		return debt;
	}
	public void setDebt(Double debt) {
		this.debt = debt;
	}
	
	

}
