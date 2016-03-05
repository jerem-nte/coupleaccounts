package core;

public class Currency {
	
	private String id;
	private String name;
	private String shortname;
	private String icon;
	
	
	public Currency(String id, String name, String shortname, String icon) {
		this.id = id;
		this.name = name;
		this.shortname = shortname;
		this.icon = icon;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	

}
