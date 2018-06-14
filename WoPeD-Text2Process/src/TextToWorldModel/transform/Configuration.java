package TextToWorldModel.transform;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Configuration {

    /** The name of the config file */
    public final static String CONFIGFILE = "ProcessEditor.xml";
    /** The location of the external dot layouter */
    public final static String PROP_DOT_LOCATION = "DotLocation";
    /** The semicolon separated list of recent files */
    public final static String PROP_RECENT_FILES = "RecentFiles";
    /** A flag if the system look and feel should be used */
    public final static String PROP_USE_SYSTEM_LOOK_AND_FEEL = "SystemLookAndFeel";
    /** The instance */
    private static Configuration INSTANCE = null;
    /** The internal properties */
    private static Properties props = new Properties();

    /** 
     * Creates a new configuration.
     */
    public Configuration() {
        // empty
    }

    /**
     * Returns the instance.
     * @return
     */
    public static Configuration getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new Configuration();
        INSTANCE.loadConfig();
        return INSTANCE;
    }

    /**
     * Returns a propery
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String result = props.getProperty(key);
        if (result==null) return "";
        return result;
    }

    /**
     * Returns a property
     * @param key
     * @param defaultValue The default value to be returned if the key does not exist.
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String result = props.getProperty(key);
        if (result==null) return defaultValue;
        return result;
    }

    /**
     * Sets a property (and stores the updated file)
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        props.setProperty(key, value);
        //storeConfig();
    }

    /**
     * @ !not! deprecated This method should not be used, since the direct
     * access to the properties is encapsulated via the Configuration
     * singleton!
     * - yes but this will create dependencies to the Configuration class
     * which we do not want e.g. when we transfer the layouter classes to the IS
     * @return
     */
    public static Properties getProperties() {
    	return props;
    	
    }

    /**
     * Reads the configuration from a file. All default initializations are
     * done here!
     */
    private void loadConfig() {
        // Read configuration
        try {
            File configFile = new File(System.getProperty("user.home")+File.separator+CONFIGFILE);
            props.loadFromXML(new FileInputStream(configFile));
        } catch (Exception e) {
            // Write new default properties
            props.setProperty(PROP_DOT_LOCATION, "/usr/local/bin/dot");
            props.setProperty(PROP_USE_SYSTEM_LOOK_AND_FEEL, "0");
            //storeConfig();
        }
    }
}
