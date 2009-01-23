package org.woped.config;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IConfiguration;
import org.woped.core.utilities.LoggerManager;
import org.woped.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * All configuration data for woped. Could Read/save the configuration permanent
 * from/to file. During runtime the configuration data is stored in the
 * generated XMLBeans Objects.
 * 
 * 10.10.2003
 */
public class WoPeDConfiguration implements IConfiguration {

	private static ConfigurationDocument confDoc = null;

	private static String CONFIG_FILE = "WoPeDconfig.xml";

	private static String CONFIG_BUILTIN_FILE = "/org/woped/config/WoPeDconfig.xml";

	private Locale locale = null;

	private static int RECENTFILES_SIZE = 10;

	private String defaultHomedir = "";

	private String currentWorkingdir = "";

	private static XmlOptions xmlOptions = new XmlOptions();

	private Vector<WoPeDRecentFile> runtimeRecentFiles = new Vector<WoPeDRecentFile>(
			RECENTFILES_SIZE);

	private boolean startedAsApplet;

	private String laf = null;
	String lafCurrentSys = UIManager.getSystemLookAndFeelClassName();
	String lafXPlatform = UIManager.getCrossPlatformLookAndFeelClassName();
	String lafDiscretePlatform = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	String lafMac = "com.apple.mrj.swing.MacLookAndFeel";
	String lafWin = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	public WoPeDConfiguration(boolean aStartedAsApplet) {
		startedAsApplet = aStartedAsApplet;
		initConfig();
	}

	private static ConfigurationDocument getDocument() {
		return confDoc;
	}

	public boolean initConfig() {
		// Set XML Options
		xmlOptions.setUseDefaultNamespace();
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSavePrettyPrintIndent(2);
		return readConfig();
	}

	private String getAppdir() {
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

	private String getUserdir() {
		String fn = System.getProperty("user.home");
		fn = fn.replaceAll("file:", "");
		
		// extract version number of WoPeD in order to get the correct config file version
		ResourceBundle rb = PropertyResourceBundle.getBundle("org.woped.translations.Messages");
		String vs = "-" + rb.getString("Application.Version");
		fn = fn + File.separator + ".WoPeD" + File.separator;
		return fn;
	}

	private String getUserConfigFile() {
		return getUserdir() + CONFIG_FILE;
	}

	public String getDefaultHomedir() {
		return defaultHomedir;
	}

	public boolean isDefaultHomedirSet() {
		return defaultHomedir != null && !defaultHomedir.equals("");
	}

	public void setDefaultHomedir(String dhd) {
		LoggerManager.info(Constants.CONFIG_LOGGER,
				"Setting default home dir: " + dhd);
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
				"Setting current working dir: " + cwd);
		currentWorkingdir = cwd;
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public boolean readConfig() {
		boolean confOk;

		if (!startedAsApplet) {
			String fn = getUserConfigFile();
			setDefaultHomedir(getUserdir() + "nets" + File.separator);

			if (new File(fn).exists()) {
				LoggerManager.info(Constants.CONFIG_LOGGER,
						"Loading configuration from " + fn + ".");
				confOk = readConfig(new File(fn));
			} else {
				LoggerManager
						.warn(Constants.CONFIG_LOGGER,
								"No configuration file found, using built-in settings.");
				confOk = readConfig(WoPeDConfiguration.class
						.getResourceAsStream(CONFIG_BUILTIN_FILE));
			}

			File ud = new File(getUserdir());
			if (!ud.exists())
				ud.mkdir();

			File hd = new File(getHomedir());
			if (!hd.exists())
				hd.mkdir();
		} else {
			// if started as applet always user built-in config file
			confOk = readConfig(WoPeDConfiguration.class
					.getResourceAsStream(CONFIG_BUILTIN_FILE));
		}

		if (!confOk) {
			JOptionPane.showMessageDialog(null, Messages
					.getString("Init.ConfigFileNotFound"), Messages
					.getString("Init.ConfigError"), JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// setColorOn(true);
		return confOk;
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param is
	 * @return
	 */
	public boolean readConfig(InputStream is) {
		try {
			confDoc = ConfigurationDocument.Factory.parse(is);
			return readConfig(confDoc);
		} catch (XmlException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					"ERROR during parsing file.");
			return false;
		} catch (IOException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					"ERROR during reading file.");
			return false;
		}
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param file
	 * @return
	 */
	public boolean readConfig(File file) {
		if (file.exists()) {
			try {
				// Parse the instance into the type generated from the schema.
				ConfigurationDocument c = ConfigurationDocument.Factory
						.parse(file);
				confDoc = c;
				return readConfig(confDoc);
			} catch (XmlException e) {
				LoggerManager.error(Constants.CONFIG_LOGGER,
						"ERROR during parsing file.");
				return false;
			} catch (IOException e) {
				LoggerManager.error(Constants.CONFIG_LOGGER,
						"ERROR during reading file.");
				return false;
			}
		} else {
			LoggerManager
					.error(Constants.CONFIG_LOGGER, "File does not exist.");
			return false;
		}
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param confDoc
	 * @return
	 */
	public boolean readConfig(ConfigurationDocument confDoc) {
		// Create an instance of a type generated from schema to hold the
		// XML.
		ConfigurationDocument.Configuration config;

		if (confDoc != null && (config = confDoc.getConfiguration()) != null) {
			// GUI

			// MAC, WIN, Linux and XPlatform LookAndFeel

			String lafConf = config.getGui().getWindow().getLookAndFeel();
			if (!lafConf.equals(lafCurrentSys)) {

				if (lafCurrentSys.contains("WindowsLookAndFeel")) {
					setLookAndFeel(lafWin);
				} else {
					setLookAndFeel(lafXPlatform);
				}
			} else {
				setLookAndFeel(lafConf);
			}
			setWindowX(config.getGui().getWindow().getX());
			setWindowY(config.getGui().getWindow().getY());
			setWindowSize(new Dimension(config.getGui().getWindow().getWidth(),
					config.getGui().getWindow().getHeight()));

			// Locale
			setLocaleLanguage(config.getGui().getLocale().getLanguage());
			setLocaleCountry(config.getGui().getLocale().getCountry());
			setLocaleVariant(config.getGui().getLocale().getVariant());
			setLocale();

			// Recent
			runtimeRecentFiles = new Vector<WoPeDRecentFile>();
			for (int i = 0; i < getDocument().getConfiguration().getGeneral()
					.getRecentFilesArray().length; i++) {
				if (getDocument().getConfiguration().getGeneral()
						.getRecentFilesArray()[i].getName() != null)
					runtimeRecentFiles.addElement(new WoPeDRecentFile(
							getDocument().getConfiguration().getGeneral()
									.getRecentFilesArray()[i].getName(),
							getDocument().getConfiguration().getGeneral()
									.getRecentFilesArray()[i].getPath()));
			}

			// Homedir
			if (isHomedirSet())
				setHomedir(config.getGeneral().getHomedir());
			else
				setHomedir(getDefaultHomedir());

			// Current working dir for file operations
			setCurrentWorkingdir(getHomedir());

			// Import
			setImportToolspecific(config.getTools().getImporting()
					.getToolspecific());

			// Export
			setExportToolspecific(config.getTools().getExporting()
					.getToolspecific());

			// TPN Export
			setTpnSaveElementAsName(config.getTools().getExporting()
					.getTpnExportElementAsName());

			// Tools
			setUseWoflan(config.getTools().getWoflan().getUseWoflan());
			setWoflanPath(config.getTools().getWoflan().getWoflanPath());

			// Editor
			setEditingOnCreation(config.getEditor().getEditOnCreation());
			setInsertCOPYwhenCopied(config.getEditor().getInsertCopy());
			setSmartEditing(config.getEditor().getSmartEditing());

			// Coloring
			setColorOn(config.getColoring().getColorOn());

			LoggerManager.info(Constants.CONFIG_LOGGER,
					"Configuration loaded successfully.");
			return true;
		} else {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					"Could not load configuration.");
			return false;
		}
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public boolean saveConfig() {
		boolean confOk;
		String fn = getUserConfigFile();

		confOk = saveConfig(new File(fn));

		if (confOk) {
			LoggerManager.info(Constants.CONFIG_LOGGER,
					"Saving configuration to " + fn + ".");
		} else {
			LoggerManager.warn(Constants.CONFIG_LOGGER,
					"Cannot save configuration to file " + fn + ".");
		}
		return confOk;
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param file
	 * @return
	 */
	public boolean saveConfig(File file) {
		try {
			// Map the recent file!
			getDocument().getConfiguration().getGeneral().setRecentFilesArray(
					new RecentFile[0]);
			RecentFile xmlRecent;
			for (Iterator iter = runtimeRecentFiles.iterator(); iter.hasNext();) {
				WoPeDRecentFile recent = (WoPeDRecentFile) iter.next();
				xmlRecent = getDocument().getConfiguration().getGeneral()
						.addNewRecentFiles();
				xmlRecent.setName(recent.getName());
				xmlRecent.setPath(recent.getPath());
			}

			// getDocument().getConfiguration().getGeneral().setHomedir(homedir);
			getDocument().save(file, xmlOptions);
			LoggerManager.info(Constants.CONFIG_LOGGER, "Configuration saved.");
			return true;
		} catch (IOException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					"COULD NOT SAVE CONFIGURATION");
			return false;
		}
	}

	/**
	 * Returns the editingOnCreation.
	 * 
	 * @return boolean
	 */
	public boolean isEditingOnCreation() {
		return getDocument().getConfiguration().getEditor().getEditOnCreation();
	}

	/**
	 * Sets the editingOnCreation.
	 * 
	 * @param editingOnCreation
	 *            The editingOnCreation to set
	 */
	public void setEditingOnCreation(boolean editingOnCreation) {
		if (editingOnCreation != getDocument().getConfiguration().getEditor()
				.getEditOnCreation()) {
			getDocument().getConfiguration().getEditor().setEditOnCreation(
					editingOnCreation);
		}
	}

	/**
	 * Returns the insertCOPYwhencopied.
	 * 
	 * @return boolean
	 */
	public boolean isInsertCOPYwhenCopied() {
		return getDocument().getConfiguration().getEditor().getInsertCopy();
	}

	/**
	 * Sets the insertCOPYwhencopied.
	 * 
	 * @param insertCOPYwhencopied
	 *            The insertCOPYwhencopied to set
	 */
	public void setInsertCOPYwhenCopied(boolean insertCOPYwhenCopied) {
		getDocument().getConfiguration().getEditor().setInsertCopy(
				insertCOPYwhenCopied);
	}

	/**
	 * Returns the exportToolspecific.
	 * 
	 * @return boolean
	 */
	public boolean isExportToolspecific() {
		return getDocument().getConfiguration().getTools().getExporting()
				.getToolspecific();
	}

	/**
	 * Returns the importToolspecific.
	 * 
	 * @return boolean
	 */
	public boolean isImportToolspecific() {
		return getDocument().getConfiguration().getTools().getImporting()
				.getToolspecific();
	}

	/**
	 * Sets the exportToolspecific.
	 * 
	 * @param exportToolspecific
	 *            The exportToolspecific to set
	 */
	public void setExportToolspecific(boolean exportToolspecific) {
		getDocument().getConfiguration().getTools().getExporting()
				.setToolspecific(exportToolspecific);
	}

	/**
	 * Sets the importToolspecific.
	 * 
	 * @param importToolspecific
	 *            The importToolspecific to set
	 */
	public void setImportToolspecific(boolean importToolspecific) {
		getDocument().getConfiguration().getTools().getImporting()
				.setToolspecific(importToolspecific);
	}

	/**
	 * Returns the recentFiles.
	 * 
	 * @return Vector
	 */
	public Vector getRecentFiles() {
		return runtimeRecentFiles;
	}

	/**
	 * TODO: optimize?
	 * 
	 * @param name
	 * @param path
	 */
	public void addRecentFile(String name, String path) {
		try {
			WoPeDRecentFile recent = new WoPeDRecentFile(name, path);
			// delete the old entry if exists.
			for (int idx = 0; idx < runtimeRecentFiles.size(); idx++) {
				if (((WoPeDRecentFile) runtimeRecentFiles
						.get(idx)).getPath().equals(path)) {
					// delete the entry
					runtimeRecentFiles.remove(idx);
				}
			}
			// add the the new one only if file exists
			if (new File(recent.getPath()).exists()) {
				if (runtimeRecentFiles.size() == RECENTFILES_SIZE)
					runtimeRecentFiles.remove(RECENTFILES_SIZE - 1);
				runtimeRecentFiles.add(0, recent);
			}
		} catch (NullPointerException ne) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					"Could not store Recentfile data.");
		}

	}

	/**
	 * Sets the recentFiles.
	 * 
	 * @param recentFiles
	 *            The recentFiles to set / public void setRecentFiles(Vector
	 *            recentFiles) {
	 *            getDocument().getConfiguration().getGeneral().setRecentFilesArray
	 *            ((RecentFile[])recentFiles.toArray()); }
	 */
	public void removeRecentFile(String name, String path) {
		// delete the old entry if exists.
		for (int idx = 0; idx < runtimeRecentFiles.size(); idx++) {
			if (((WoPeDRecentFile) runtimeRecentFiles.get(idx))
					.getPath().equals(path)) {
				// delete the entry
				runtimeRecentFiles.remove(idx);
			}
		}
	}

	public void removeAllRecentFiles() {
		runtimeRecentFiles = new Vector<WoPeDRecentFile>(RECENTFILES_SIZE);
	}

	/**
	 * Returns the windowSize.
	 * 
	 * @return Dimension
	 */
	public Dimension getWindowSize() {
		return new Dimension(getDocument().getConfiguration().getGui()
				.getWindow().getWidth(), getDocument().getConfiguration()
				.getGui().getWindow().getHeight());
	}

	/**
	 * Returns the windowX.
	 * 
	 * @return int
	 */
	public int getWindowX() {
		return getDocument().getConfiguration().getGui().getWindow().getX();
	}

	/**
	 * Returns the windowY.
	 * 
	 * @return int
	 */
	public int getWindowY() {
		return getDocument().getConfiguration().getGui().getWindow().getY();
	}

	/**
	 * Sets the windowSize.
	 * 
	 * @param windowSize
	 *            The windowSize to set
	 */
	public void setWindowSize(Dimension windowSize) {
		getDocument().getConfiguration().getGui().getWindow().setHeight(
				(int) windowSize.getHeight());
		getDocument().getConfiguration().getGui().getWindow().setWidth(
				(int) windowSize.getWidth());
	}

	/**
	 * Sets the windowX.
	 * 
	 * @param windowX
	 *            The windowX to set
	 */
	public void setWindowX(int windowX) {
		getDocument().getConfiguration().getGui().getWindow().setX(windowX);
	}

	/**
	 * Sets the windowY.
	 * 
	 * @param windowY
	 *            The windowY to set
	 */
	public void setWindowY(int windowY) {
		getDocument().getConfiguration().getGui().getWindow().setY(windowY);
	}

	/**
	 * Sets the logdir.
	 * 
	 * @param logdir
	 *            The logdir to set
	 */
	public void setLogdir(String logdir) {
		// Not yet contained in XML configuration
	}

	public String getLogdir() {
		return ".";
	}

	/**
	 * Returns the homedir.
	 * 
	 * @return String
	 */
	public String getHomedir() {
		return getDocument().getConfiguration().getGeneral().getHomedir();
	}

	public boolean isHomedirSet() {
		String hd = getDocument().getConfiguration().getGeneral().getHomedir();
		return hd != null && !hd.equals("");
	}

	/**
	 * Sets the homedir.
	 * 
	 * @param homedir
	 *            The homedir to set
	 */
	public void setHomedir(String hd) {
		LoggerManager.info(Constants.CONFIG_LOGGER, "Setting home dir: " + hd);
		getDocument().getConfiguration().getGeneral().setHomedir(hd);
	}

	/**
	 * Returns the useWoflan.
	 * 
	 * @return boolean
	 */
	public boolean isUseWoflan() {
		return getDocument().getConfiguration().getTools().getWoflan()
				.getUseWoflan();
	}

	/**
	 * Returns the woflanPath.
	 * 
	 * @return String
	 */
	public String getWoflanPath() {
		return getDocument().getConfiguration().getTools().getWoflan()
				.getWoflanPath();
	}

	/**
	 * Sets the useWoflan.
	 * 
	 * @param useWoflan
	 *            The useWoflan to set
	 */
	public void setUseWoflan(boolean useWoflan) {
		getDocument().getConfiguration().getTools().getWoflan().setUseWoflan(
				useWoflan);
	}

	/**
	 * Sets the woflanPath.
	 * 
	 * @param woflanPath
	 *            The woflanPath to set
	 */
	public void setWoflanPath(String woflanPath) {
		getDocument().getConfiguration().getTools().getWoflan().setWoflanPath(
				woflanPath);
	}

	/**
	 * Returns the smartEditing.
	 * 
	 * @return boolean
	 */
	public boolean isSmartEditing() {
		return getDocument().getConfiguration().getEditor().getSmartEditing();
	}

	/**
	 * Sets the smartEditing.
	 * 
	 * @param smartEditing
	 *            The smartEditing to set
	 */
	public void setSmartEditing(boolean smartEditing) {
		getDocument().getConfiguration().getEditor().setSmartEditing(
				smartEditing);
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public boolean isTpnSaveElementAsName() {
		boolean b = getDocument().getConfiguration().getTools().getExporting()
				.getTpnExportElementAsName();
		return b;
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param b
	 */
	public void setTpnSaveElementAsName(boolean b) {
		getDocument().getConfiguration().getTools().getExporting()
				.setTpnExportElementAsName(b);
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param className
	 */
	public void setLookAndFeel(String className) {
		getDocument().getConfiguration().getGui().getWindow().setLookAndFeel(
				className);
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public String getLookAndFeel() {
		return getDocument().getConfiguration().getGui().getWindow()
				.getLookAndFeel();
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param showGrid
	 */
	public void setShowGrid(boolean showGrid) {
		getDocument().getConfiguration().getEditor().setShowGrid(showGrid);
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public boolean isShowGrid() {
		return getDocument().getConfiguration().getEditor().getShowGrid();
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param width
	 */
	public void setArrowWidth(int width) {
		getDocument().getConfiguration().getEditor().setArrowWidth(width);
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public int getArrowWidth() {
		if (getDocument().getConfiguration().getEditor().isSetArrowWidth())
			return getDocument().getConfiguration().getEditor().getArrowWidth();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getArrowWidth();
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param headSize
	 */
	public void setArrowheadSize(int headSize) {
		getDocument().getConfiguration().getEditor().setArrowheadSize(headSize);
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public int getArrowheadSize() {
		if (getDocument().getConfiguration().getEditor().isSetArrowheadSize())
			return getDocument().getConfiguration().getEditor()
					.getArrowheadSize();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getArrowheadSize();
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public boolean isFillArrowHead() {
		if (getDocument().getConfiguration().getEditor().isSetArrowFilledHead())
			return getDocument().getConfiguration().getEditor()
					.getArrowFilledHead();
		else
			return ConfigurationManager.getStandardConfiguration()
					.isFillArrowHead();
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param fill
	 */
	public void setFillArrowHead(boolean fill) {
		getDocument().getConfiguration().getEditor().setArrowFilledHead(fill);
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public boolean isRoundRouting() {
		if (getDocument().getConfiguration().getEditor().isSetRoundRouting())
			return getDocument().getConfiguration().getEditor()
					.getRoundRouting();
		else
			return false;
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param round
	 */
	public void setRoundRouting(boolean round) {
		getDocument().getConfiguration().getEditor().setRoundRouting(round);
	}

	public Color getSelectionColor() {
		return ConfigurationManager.getStandardConfiguration()
				.getSelectionColor();
	}

	public void setSelectionColor(Color color) {
		ConfigurationManager.getStandardConfiguration()
				.setSelectionColor(color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#getPortColor()
	 */
	public Color getPortColor() {
		return ConfigurationManager.getStandardConfiguration().getPortColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#setPortColor(java.awt.Color)
	 */
	public void setPortColor(Color color) {
		ConfigurationManager.getStandardConfiguration().setPortColor(color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#setLocaleLanguage(java.lang.String )
	 */
	public void setLocaleLanguage(String language) {
		getDocument().getConfiguration().getGui().getLocale().setLanguage(
				language);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#getLocaleLanguage()
	 */
	public String getLocaleLanguage() {
		return getDocument().getConfiguration().getGui().getLocale()
				.getLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#setLocaleCountry(java.lang.String)
	 */
	public void setLocaleCountry(String country) {
		getDocument().getConfiguration().getGui().getLocale().setCountry(
				country);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#getLocaleCountry()
	 */
	public String getLocaleCountry() {
		return getDocument().getConfiguration().getGui().getLocale()
				.getCountry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#setLocaleVariant(java.lang.String)
	 */
	public void setLocaleVariant(String variant) {
		getDocument().getConfiguration().getGui().getLocale().setVariant(
				variant);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#getLocaleVariant()
	 */
	public String getLocaleVariant() {
		return getDocument().getConfiguration().getGui().getLocale()
				.getVariant();
	}

	public void setLocale() {
		String language = null;
		String country = null;
		String variant = null;

		Locale userLocale = null;

		// language is english if startedAsApplet, using default Resource File
		// required
		// Locale.setDefault not possible because of AccessControlException
		if (startedAsApplet) {
			setLocaleCountry(null);
			setLocaleLanguage(null);
			setLocaleVariant(null);
			language = "";
		}

		if (getLocaleLanguage() != null) {
			language = getLocaleLanguage();
		}
		if (getLocaleCountry() != null) {
			country = getLocaleCountry();
		}
		if (getLocaleVariant() != null) {
			variant = getLocaleVariant();
		}

		if (language != null && country != null && variant != null) {
			userLocale = new Locale(language, country, variant);
		} else if (language != null && country != null) {
			userLocale = new Locale(language, country);
		} else {
			userLocale = new Locale(language);
		}
		if (userLocale == null) {
			userLocale = Locale.ENGLISH;
			setLocaleLanguage(this.locale.getLanguage());
		}

		this.locale = userLocale;
	}

	public Locale getLocale() {
		return this.locale;
	}
	
	// Start Understandability Coloring configuration
	public boolean getColorOn() {
		if (getDocument().getConfiguration().getColoring() != null)
			return getDocument().getConfiguration().getColoring().getColorOn();
		else
			return false;
	}
	
	public void setColorOn(boolean b) {
		getDocument().getConfiguration().getColoring().setColorOn(b);
	}

	public void setColor1(int rgb) {
		getDocument().getConfiguration().getColoring().setColor1(rgb);
	}

	public int getColor1() {
		return getDocument().getConfiguration().getColoring().getColor1();
	}

	public void setColor2(int rgb) {
		getDocument().getConfiguration().getColoring().setColor2(rgb);
	}

	public int getColor2() {
		return getDocument().getConfiguration().getColoring().getColor2();
	}

	public void setColor3(int rgb) {
		getDocument().getConfiguration().getColoring().setColor3(rgb);
	}

	public int getColor3() {
		return getDocument().getConfiguration().getColoring().getColor3();
	}

	public void setColor4(int rgb) {
		getDocument().getConfiguration().getColoring().setColor4(rgb);
	}

	public int getColor4() {
		return getDocument().getConfiguration().getColoring().getColor4();
	}

	public void setColor5(int rgb) {
		getDocument().getConfiguration().getColoring().setColor5(rgb);
	}

	public int getColor5() {
		return getDocument().getConfiguration().getColoring().getColor5();
	}

	public void setColor6(int rgb) {
		getDocument().getConfiguration().getColoring().setColor6(rgb);
	}

	public int getColor6() {
		return getDocument().getConfiguration().getColoring().getColor6();
	}

	public void setColor7(int rgb) {
		getDocument().getConfiguration().getColoring().setColor7(rgb);
	}

	public int getColor7() {
		return getDocument().getConfiguration().getColoring().getColor7();
	}

	public void setColor8(int rgb) {
		getDocument().getConfiguration().getColoring().setColor8(rgb);
	}

	public int getColor8() {
		return getDocument().getConfiguration().getColoring().getColor8();
	}

	public void setColor9(int rgb) {
		getDocument().getConfiguration().getColoring().setColor9(rgb);
	}

	public int getColor9() {
		return getDocument().getConfiguration().getColoring().getColor9();
	}

	public void setColor10(int rgb) {
		getDocument().getConfiguration().getColoring().setColor10(rgb);
	}

	public int getColor10() {
		return getDocument().getConfiguration().getColoring().getColor10();
	}

	public void setColor11(int rgb) {
		getDocument().getConfiguration().getColoring().setColor11(rgb);
	}

	public int getColor11() {
		return getDocument().getConfiguration().getColoring().getColor11();
	}

	public void setColor12(int rgb) {
		getDocument().getConfiguration().getColoring().setColor12(rgb);
	}

	public int getColor12() {
		return getDocument().getConfiguration().getColoring().getColor12();
	}

	public void setColor13(int rgb) {
		getDocument().getConfiguration().getColoring().setColor13(rgb);
	}

	public int getColor13() {
		return getDocument().getConfiguration().getColoring().getColor13();
	}

	public void setColor14(int rgb) {
		getDocument().getConfiguration().getColoring().setColor14(rgb);
	}

	public int getColor14() {
		return getDocument().getConfiguration().getColoring().getColor14();
	}

	public void setColor15(int rgb) {
		getDocument().getConfiguration().getColoring().setColor15(rgb);
	}

	public int getColor15() {
		return getDocument().getConfiguration().getColoring().getColor15();
	}

	public void setColor16(int rgb) {
		getDocument().getConfiguration().getColoring().setColor16(rgb);
	}

	public int getColor16() {
		return getDocument().getConfiguration().getColoring().getColor16();
	}

	public Color[] getUnderstandColors() {
		Color[] ColorArray = new Color[16];
		ColorArray[0] = new Color(getColor1());
		ColorArray[1] = new Color(getColor2());
		ColorArray[2] = new Color(getColor3());
		ColorArray[3] = new Color(getColor4());
		ColorArray[4] = new Color(getColor5());
		ColorArray[5] = new Color(getColor6());
		ColorArray[6] = new Color(getColor7());
		ColorArray[7] = new Color(getColor8());
		ColorArray[8] = new Color(getColor9());
		ColorArray[9] = new Color(getColor10());
		ColorArray[10] = new Color(getColor11());
		ColorArray[11] = new Color(getColor12());
		ColorArray[12] = new Color(getColor13());
		ColorArray[13] = new Color(getColor14());
		ColorArray[14] = new Color(getColor15());
		ColorArray[15] = new Color(getColor16());

		return ColorArray;
	}

	public int getDefaultColor1() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor1();
	}

	public int getDefaultColor2() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor2();
	}

	public int getDefaultColor3() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor3();
	}

	public int getDefaultColor4() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor4();
	}

	public int getDefaultColor5() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor5();
	}

	public int getDefaultColor6() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor6();
	}

	public int getDefaultColor7() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor7();
	}

	public int getDefaultColor8() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor8();
	}

	public int getDefaultColor9() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor9();
	}

	public int getDefaultColor10() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor10();
	}

	public int getDefaultColor11() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor11();
	}

	public int getDefaultColor12() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor12();
	}

	public int getDefaultColor13() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor13();
	}

	public int getDefaultColor14() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor14();
	}

	public int getDefaultColor15() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor15();
	}

	public int getDefaultColor16() {
		return getDocument().getConfiguration().getColoring()
				.getDefaultcolor16();
	}

	public Color[] getDefaultUnderstandColors() {
		Color[] ColorArray = new Color[16];
		ColorArray[0] = new Color(getDefaultColor1());
		ColorArray[1] = new Color(getDefaultColor2());
		ColorArray[2] = new Color(getDefaultColor3());
		ColorArray[3] = new Color(getDefaultColor4());
		ColorArray[4] = new Color(getDefaultColor5());
		ColorArray[5] = new Color(getDefaultColor6());
		ColorArray[6] = new Color(getDefaultColor7());
		ColorArray[7] = new Color(getDefaultColor8());
		ColorArray[8] = new Color(getDefaultColor9());
		ColorArray[9] = new Color(getDefaultColor10());
		ColorArray[10] = new Color(getDefaultColor11());
		ColorArray[11] = new Color(getDefaultColor12());
		ColorArray[12] = new Color(getDefaultColor13());
		ColorArray[13] = new Color(getDefaultColor14());
		ColorArray[14] = new Color(getDefaultColor15());
		ColorArray[15] = new Color(getDefaultColor16());

		return ColorArray;
	}
	
	public int getAlgorithmMode() {
		if (getDocument().getConfiguration().getColoring() != null)
			return getDocument().getConfiguration().getColoring().getAlgorithmMode();
		else
			return 0;
	}
	
	public void setAlgorithmMode(int n) {
		getDocument().getConfiguration().getColoring().setAlgorithmMode(n);
	}
	// End Understandability Coloring configuration

}