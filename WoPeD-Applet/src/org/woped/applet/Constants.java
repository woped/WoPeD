package org.woped.applet;

public class Constants
{
    public final static String APPLET_LOGGER = "APPLET_LOGGER";
	
    /**
     * Location of the modell files
     */
    public static final String SERVER_MODELLS = "modells";

	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";

	public static final String SQL_MODEL_LIST = "SELECT * FROM `modelle` WHERE `userid`=%s";
	
	public static final String SQL_SHARED_MODEL_LIST = "SELECT * FROM `modelle` WHERE `userid`=%s OR `shared`=1";
	
	public static final String SQL_INSERT_MODEL = "INSERT INTO `modelle` (userid,titel,lastedit) VALUES (%s,'%s',NOW())";
	
	public static final String SQL_EXISTS_MODEL = "SELECT * FROM `modelle` WHERE `modellid`=%s";

	public static final String ERROR_FILE_NOT_EXISTS = "File does'nt exist";

	public static final String ERROR_NO_MATH_FOUND_FOR_MODELID = "No match found for modelID =%s";

	public static final String ERROR_NOT_ENOUGH_MEMORY = "Not enough memory to save model";
	
	public static final String RMI_MEMORY = "rmiMemory";
}
