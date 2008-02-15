package org.woped.server.configuration;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Configuration File Loader
 * 
 * @author C. Krüger
 *
 */
public class PropertyLoader {

	// property File
	static private String propertyFile = "config.properties";
	
	static private Properties properties;
	
	static private boolean initialized = false;
	
	static public String getProperty(String value) {
		if (!initialized) {
			readProperties();
			if (properties == null) 
				return null;
		}
		return properties.getProperty(value);
	}

	static void readProperties() {
		InputStream is = PropertyLoader.class.getClassLoader().
				getResourceAsStream(propertyFile);
		
		properties = new Properties();
		try {
			properties.load(is);
			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
			properties = null;
		}
		
		
	}
	
	
}
