package org.woped.core.config;


public class ConfigurationManager
{
	private static boolean								runAsApplet					= false;
    private static IGeneralConfiguration             	configuration       		= null;
    private static IMetricsConfiguration				metricsConfiguration 		= null;
    private static DefaultStaticConfiguration 			staticConfiguration 		= new DefaultStaticConfiguration();
    private static DefaultStaticMetricsConfiguration	staticMetricsConfiguration 	= new DefaultStaticMetricsConfiguration();
    
    public ConfigurationManager(boolean runAsApplet) {
    	ConfigurationManager.runAsApplet = runAsApplet;  	
    }
    
    public static boolean isApplet() {
    	return runAsApplet;
    }
        
    public static IGeneralConfiguration getConfiguration()
    {
        if (configuration == null)
        	return staticConfiguration;
        else
        	return configuration;
    }
    
    public static IMetricsConfiguration getMetricsConfiguration()
    {
    	if (metricsConfiguration == null)
    		return staticMetricsConfiguration;
    	else
    		return metricsConfiguration;
    }

    public static IGeneralConfiguration getStandardConfiguration()
    {
        return staticConfiguration;
    }

    public static void setConfiguration(IGeneralConfiguration configuration)
    {
        ConfigurationManager.configuration = configuration;
    }
    
    public static void setMetricsConfiguration(IMetricsConfiguration metricsConfiguration)
    {
    	ConfigurationManager.metricsConfiguration = metricsConfiguration;
    }
}
