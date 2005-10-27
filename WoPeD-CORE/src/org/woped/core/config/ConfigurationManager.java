package org.woped.core.config;

import org.woped.core.Constants;


public class ConfigurationManager {
	private static IConfiguration configuration = null;

	private static DefaultStaticConfiguration staticConfiguration = new DefaultStaticConfiguration();

	public static IConfiguration getConfiguration() {
		LoggerManager.info(Constants.CORE_LOGGER, "START WOPED_LOGGER - LOGGING ACTIVE");
		if (configuration == null) {
			return staticConfiguration;
		} else {
			return configuration;
		}
	}

	public static IConfiguration getStandardConfiguration() {
		return staticConfiguration;
	}

	public static void setConfiguration(IConfiguration configuration) {
		ConfigurationManager.configuration = configuration;
	}
}
