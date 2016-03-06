package services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import core.Currency;
import dao.CurrencyDao;
import dto.CurrencyHtmlSelectDto;

@Path("/currency")
public class CurrencyService {

	
	@GET
    @Path("listhtmlselect")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CurrencyHtmlSelectDto> listhtmlselect() {
    	
		List<Currency> currencies = CurrencyDao.getCurrencies();
		return CurrencyHtmlSelectDto.convert(currencies);
    	
    }
}
