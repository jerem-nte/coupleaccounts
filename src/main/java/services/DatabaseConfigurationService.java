package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;

import config.DatabaseConfiguration;
import db.MysqlConnection;
import dto.DatabaseConfigurationDto;
import dto.ResponseDto;

@Path("/database")
public class DatabaseConfigurationService {
	
	
	@GET
    @Path("config")
    @Produces(MediaType.APPLICATION_JSON)
    public DatabaseConfigurationDto getConfiguration() {
    	
		DatabaseConfigurationDto dto = new DatabaseConfigurationDto();
		dto.setHost(DatabaseConfiguration.getInstance().getProperty("host"));
		dto.setPort(new Integer(DatabaseConfiguration.getInstance().getProperty("port")));
		dto.setBase(DatabaseConfiguration.getInstance().getProperty("base"));
		dto.setUser(DatabaseConfiguration.getInstance().getProperty("user"));
		dto.setPass(StringUtils.repeat("*", DatabaseConfiguration.getInstance().getProperty("user").length()));
		
		return dto;
    	
    }
	
	
	@POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public ResponseDto updateConfiguration(JsonNode node) {
    	
		String host;
		String port;
		String base;
		String user;
		String pass;
		
		try {
			JsonNode configuration = node.get("configuration");
			host = configuration.get("host").asText();
			port = configuration.get("port").asText();
			base = configuration.get("base").asText();
			user = configuration.get("user").asText();
			pass = configuration.get("pass").asText();
		} catch(Exception e) {
			return new ResponseDto(1, "Cannot test connection : internal problem with request parameter." + e.getMessage());
		}
		
		try {
			MysqlConnection.testConnection(host, port, base, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseDto(1, e.getMessage());
		}
		
		return new ResponseDto(0, "Connected to database");
			
    	
    }
	
	

}
