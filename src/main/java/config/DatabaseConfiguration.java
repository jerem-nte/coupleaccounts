package config;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class DatabaseConfiguration {
	
	private static Logger logger = Logger.getLogger(DatabaseConfiguration.class);
	
	private static DatabaseConfiguration instance = new DatabaseConfiguration();
	private Configuration config;
	
	private DatabaseConfiguration() {
		
		logger.info("Init database config");
		
		try {
			config = new PropertiesConfiguration(new File("src/main/resources/database.properties"));
		} catch (ConfigurationException e) {
			logger.error("Cannot read property configuration file", e);
		}
	}
	
	public static DatabaseConfiguration getInstance() {
		
		return instance;
	}
	
	public String getProperty(String prop) {
		
		return config.getString(prop);
	}
}
