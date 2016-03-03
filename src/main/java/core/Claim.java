package core;

public class Claim {

	private User user;
	private Double claim;
	
	public Claim(User user, Double claim) {
		super();
		this.user = user;
		this.claim = claim;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Double getClaim() {
		return claim;
	}
	public void setClaim(Double claim) {
		this.claim = claim;
	}
	
	public void incrementClaim(Double amount) {
		claim += amount;
	}
	
	
}
