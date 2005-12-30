package org.woped.core.config;

public class ConfigurationManager
{
    private static IConfiguration             configuration       = null;

    private static DefaultStaticConfiguration staticConfiguration = new DefaultStaticConfiguration();

    public static IConfiguration getConfiguration()
    {
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
