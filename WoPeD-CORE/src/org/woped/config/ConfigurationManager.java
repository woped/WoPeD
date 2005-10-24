package org.woped.config;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.woped.utilities.WoPeDLogger;

public class ConfigurationManager implements WoPeDLogger
{
    private static IConfiguration             configuration       = null;
    private static DefaultStaticConfiguration staticConfiguration = new DefaultStaticConfiguration();
    private static Logger                     logger              = null;

    public static void createLogger()
    {
        // Configure Logger
        try
        {
            DOMConfigurator.configure(ConfigurationManager.class.getResource("/org/woped/config/log4j.xml"));
            logger.info("START WoPeD - LOGGING ACTIVE");
        } catch (Exception e)
        {
            System.err.println("ERROR ACTIVATING LOGGER");
        }
    }

    public static IConfiguration getConfiguration()
    {
        if (logger == null)
        {
            DOMConfigurator.configure(ConfigurationManager.class.getResource("log4j.xml"));
            logger = Logger.getLogger("WOPED_LOGGER");
            logger.info("START WOPED_LOGGER - LOGGING ACTIVE");
        }
        if (configuration == null)
        {
            return staticConfiguration;
        } else
        {
            return configuration;
        }
    }

    public static IConfiguration getStandardConfiguration()
    {
        return staticConfiguration;
    }

    public static void setConfiguration(IConfiguration configuration)
    {
        ConfigurationManager.configuration = configuration;
    }
}
