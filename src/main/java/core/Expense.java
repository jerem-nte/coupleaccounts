package core;

public class Expense {
	
	private String id;
	private String label;
	private Double amount;
	private Currency currency;
	private String scope;
	private User user;
	private boolean archived;
	
	public Expense(String id, String label, Double amount, String scope, boolean archived, User user, Currency currency) {
		super();
		this.id = id;
		this.label = label;
		this.amount = amount;
		this.scope = scope;
		this.user = user;
		this.archived = archived;
		this.currency = currency;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getScope() { 
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	

	
}
