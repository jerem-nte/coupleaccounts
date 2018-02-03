package services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.Currency;
import dao.CurrencyDao;
import dto.CurrencyHtmlSelectDto;

@RestController
@RequestMapping("/currency")
public class CurrencyService {

	@Autowired
	CurrencyDao dao;
	
	@RequestMapping("listhtmlselect")
    public List<CurrencyHtmlSelectDto> listhtmlselect() throws SQLException {
    	
		List<Currency> currencies = dao.getCurrencies();
		return CurrencyHtmlSelectDto.convert(currencies);
    	
    }
}
