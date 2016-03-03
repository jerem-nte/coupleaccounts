package dto;

import java.util.ArrayList;
import java.util.List;

import core.User;

public class UserHtmlSelectDto {
	
	private String value;
	private String name;
	
	public UserHtmlSelectDto(String id, String name) {
		this.value = id;
		this.name = name;
	}

	public static List<UserHtmlSelectDto> convert(List<User> users) {
		
		List<UserHtmlSelectDto> list = new ArrayList<UserHtmlSelectDto>();
		
		for (User u : users) {
			list.add(new UserHtmlSelectDto(u.getId(), u.getName()));
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
