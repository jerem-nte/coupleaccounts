package services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import core.Debt;
import core.DebtEngine;
import core.Expense;
import dao.ExpenseDao;
import dto.ResponseDto;

@Path("/expense")
public class ExpenseService {
	
	
	@GET
    @Path("list/notarchived")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Expense> listAll() {
    	
		List<Expense> expenses = ExpenseDao.getExpenses(false, -1);
		return expenses; 
    }
	
	@GET
    @Path("list/archived")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Expense> listArchived(@QueryParam("page") String pageParam) {
		
		Integer page = null;
		try {
    		page = new Integer(pageParam);
		} catch (Exception e) {
			page = new Integer(-1);
		}
		
		List<Expense> expenses = ExpenseDao.getExpenses(true, page);
		
		return expenses; 
    }
	
	@GET
    @Path("list/archived/pagemax")
    @Produces(MediaType.APPLICATION_JSON)
    public Integer pageMaxArchived() {
		
		Integer pageMax = ExpenseDao.getPageMax(true);
		
		return pageMax; 
    }
	
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseDto delete(JsonNode json) {
    	
    	if(json.get("ids").size() == 0)
    		return new ResponseDto(1, "Nothing to delete");
    	
    	try {
    		for (final JsonNode id : json.get("ids")) {
    			ExpenseDao.deleteExpense(id.asText());
    	    }
		} catch (Exception e) {
			return new ResponseDto(1, "Cannot delete expense : " + e.getMessage());
		}
    	
    	return new ResponseDto(0, "Expense successfully deleted");
    }
    
    @POST
    @Path("archive")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseDto archive(JsonNode json) {
    	
    	if(json.get("ids").size() == 0)
    		return new ResponseDto(1, "Nothing to archive");
    	
    	try {
    		for (final JsonNode id : json.get("ids")) {
    			ExpenseDao.archiveExpense(id.asText());
    	    }
		} catch (Exception e) {
			return new ResponseDto(1, "Cannot archive expense : " + e.getMessage());
		}
    	
    	return new ResponseDto(0, "Expense successfully archived");
    }
    
    
    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseDto add(JsonNode json) {
    	
    	Double amountDouble;
    	
    	try {
    		amountDouble = Double.parseDouble(json.get("amount").asText());
    	} catch(Exception e) {
    		return new ResponseDto(1, "Please enter a valid amount");
    	}
    	
    	if(amountDouble < 0) {
    		return new ResponseDto(1, "An expense cannot be negative");
    	}
    	
    	try {
			ExpenseDao.addExpense(json.get("userId").asText(), json.get("label").asText(), amountDouble, json.get("scope").asText(), json.get("currencyId").asText());
		} catch (Exception e) {
			return new ResponseDto(1, "Cannot add expense : " + e.getMessage());
		}
    	
    	return new ResponseDto(0, "Expense successfully added");
    	
    }
    
    @POST
    @Path("debt")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Debt> getUserDebt() {
    	
    	DebtEngine de = new DebtEngine();
    	return de.compute();
    }
    
    


}
  