package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.ExpenseDao;
import dao.UserDao;

public class DebtEngine {
	
	private List<Claim> claims;
	
	
	public DebtEngine() {
		claims = new ArrayList<Claim>();
		initUserClaims();
	}


	public Debt compute() {
		
		for (Map.Entry<User, Double> entry : ExpenseDao.getSharedExpensesSum().entrySet())
		{
			Claim userClaim = getUserClaim(entry.getKey());
			userClaim.incrementClaim(Math.floor(entry.getValue() / 2));
		}
		
		for (Map.Entry<User, Double> entry : ExpenseDao.getNotSharedExpensesSum().entrySet())
		{
			Claim userClaim = getUserClaim(entry.getKey());
			userClaim.incrementClaim(entry.getValue());
		}
		
		
		if(claims.get(0).getClaim() > claims.get(1).getClaim()) {
			return new Debt(claims.get(1).getUser(), claims.get(0).getUser(), Math.floor(claims.get(0).getClaim()-claims.get(1).getClaim()));
		} else {
			return new Debt(claims.get(0).getUser(), claims.get(1).getUser(), Math.floor(claims.get(1).getClaim()-claims.get(0).getClaim()));
		}
		
	}
	
	
	private void initUserClaims() {
		
		List<User> users = UserDao.getUsers();
		
		for (User user : users) {
			claims.add(new Claim(user, 0.0));
		}
	}
	
	private Claim getUserClaim(User user) {
		for (Claim claim : claims) {
			if(claim.getUser().equals(user))
				return claim;
		}
		return null;
	}
	
	

}
