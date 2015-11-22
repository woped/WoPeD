package org.woped.config;

import java.io.File;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.woped.core.config.IConfiguration;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         Philip Allgaier <br>
 * 
 * All configuration data for WoPeD. Reads/saves the configuration permanently
 * from/to file. During runtime the configuration data is stored in the
 * generated XMLBeans objects.
 */
public abstract class WoPeDConfiguration implements IConfiguration {

	protected static ResourceBundle rb = PropertyResourceBundle.getBundle("org.woped.gui.translations.Messages");
	
	protected String defaultHomedir = "";
	protected String currentWorkingdir = "";
	
	protected static XmlOptions xmlOptions = new XmlOptions();
	
	protected boolean isLoaded;

	public WoPeDConfiguration() {
		this.isLoaded = false;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}

	public abstract boolean initConfig();

	public String getUserdir() {
		String fn = System.getProperty("user.home");
		fn = fn.replaceAll("file:", "");
		
		// extract version number of WoPeD in order to get the correct config file version
		String vs = "-" + rb.getString("Application.Version");
		fn = fn + File.separator + ".WoPeD" + vs + File.separator;
		return fn;
	}
	
	public abstract String getConfigFilePath();
	
	public String getDefaultHomedir() {
		return defaultHomedir;
	}

	public boolean isDefaultHomedirSet() {
		return defaultHomedir != null && !defaultHomedir.equals("");
	}

	public void setDefaultHomedir(String dhd) {
		LoggerManager.info(Constants.CONFIG_LOGGER, 
				rb.getString("Init.Config.SettingDfHomeDir") + ": " + dhd);
		defaultHomedir = dhd;
	}

	public String getCurrentWorkingdir() {
		return currentWorkingdir;
	}

	public boolean isCurrentWorkingdirSet() {
		return currentWorkingdir != null && !currentWorkingdir.equals("");
	}

	public void setCurrentWorkingdir(String cwd) {
		LoggerManager.info(Constants.CONFIG_LOGGER,
				rb.getString("Init.Config.SettingWorkDir") + ": " + cwd);
		currentWorkingdir = cwd;
	}
	
	public String getAppdir() {
		String fn = getClass().getResource("/org").toString();
		if (fn.indexOf("jar:") != -1) {
			fn = fn.replaceAll("jar:", "");
			// find jar start
			int n = fn.indexOf("!");
			if (n == -1)
				n = fn.length();
			// read Jar File Name
			fn = fn.substring(0, n);
			// Replace all white spaces in filename
			fn = fn.replaceAll("%20", " ");
			int i;
			for (i = fn.length() - 1; fn.charAt(i) != '/'; i--)
				;
			fn = fn.substring(0, i);
		}

		fn = fn.replaceAll("file:/", "");
		String fn1 = "";
		for (int i = 0; i < fn.length(); i++) {
			if (fn.charAt(i) == '/') {
				fn1 += File.separator;
			} else
				fn1 += fn.charAt(i);
		}

		if (fn1.charAt(1) != ':')
			fn1 = "/" + fn1;
		return fn1 + File.separator;
	}
	
	protected abstract XmlObject getConfDocument();

	/**
	 * Central method to read a configuration file
	 * 
	 * @return indicates whether loading was successful
	 */
	public abstract boolean readConfig();

	/** 
	 * @param is InputStream with config file content
	 * @param configType type indicator for type of currently handled configuration
	 * @return indicates whether loading was successful
	 */
	public abstract boolean readConfig(InputStream is);
	
	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param file
	 * @param configType type indicator for type of currently handled configuration
	 * @return
	 */
	public abstract boolean readConfig(File file);

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param confDoc
	 * @return
	 */
	public abstract boolean readConfig(XmlObject configDoc);
	
	/**
	 * Saves the configuration.
	 * 
	 * @return indicates whether saving was successful
	 */
	public boolean saveConfig() {
		boolean confOk = false;
		confOk = saveConfig(new File(getConfigFilePath()));
		if (confOk)
			LoggerManager.info(
					Constants.CONFIG_LOGGER,
					rb.getString("Exit.Config.SavingSuccess") + ": "
					+ getConfigFilePath());
		else		
			LoggerManager.error(
					Constants.CONFIG_LOGGER,
					rb.getString("Exit.Config.SavingError") + ": "
					+ getConfigFilePath());
		
		return confOk;
	}

	/**
	 * Save the configuration to a local file.
	 * 
	 * @param file
	 * @return indicates whether saving process ended successfully
	 */
	public abstract boolean saveConfig(File file);
}