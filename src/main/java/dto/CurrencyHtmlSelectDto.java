package dto;

import java.util.ArrayList;
import java.util.List;

import core.Currency; 

public class CurrencyHtmlSelectDto {
	
	private String value;
	private String name;
	
	public CurrencyHtmlSelectDto(String id, String name) {
		this.value = id;
		this.name = name;
	}

	public static List<CurrencyHtmlSelectDto> convert(List<Currency> currencies) {
		
		List<CurrencyHtmlSelectDto> list = new ArrayList<CurrencyHtmlSelectDto>();
		
		for (Currency c : currencies) {
			list.add(new CurrencyHtmlSelectDto(c.getId(), c.getShortname()));
		}
		
		return list;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
