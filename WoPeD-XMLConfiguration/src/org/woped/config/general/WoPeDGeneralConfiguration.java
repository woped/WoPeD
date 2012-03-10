package org.woped.config.general;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.woped.config.ConfigurationDocument;
import org.woped.config.Constants;
import org.woped.config.RecentFile;
import org.woped.config.WoPeDConfiguration;
import org.woped.config.metrics.WoPeDMetricsConfiguration;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IGeneralConfiguration;
import org.woped.core.utilities.LoggerManager;

/**
 * Class that provides access to the general WoPeD configuration settings. 
 * Access to it at runtime is to be gained through ConfigurationManager.
 * @see ConfigurationManager
 * @author Philip Allgaier
 *
 */
public class WoPeDGeneralConfiguration extends WoPeDConfiguration implements IGeneralConfiguration {

	private static org.woped.config.ConfigurationDocument confDoc = null;
	private static String CONFIG_FILE = "WoPeDconfig.xml";
	private static String CONFIG_BUILTIN_FILE = "/org/woped/config/general/WoPeDconfig.xml";

	private Locale locale = null;

	private static int RECENTFILES_SIZE = 10;
	private Vector<WoPeDRecentFile> runtimeRecentFiles = new Vector<WoPeDRecentFile>(RECENTFILES_SIZE);

	public String getConfigFilePath() {
		return getUserdir() + CONFIG_FILE;
	}
	
	protected ConfigurationDocument getConfDocument() {
		return confDoc;
	}

	public boolean initConfig() {
		if (!isLoaded()) {
			// Set XML Options
			xmlOptions.setUseDefaultNamespace();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(2);
			setDefaultHomedir(getUserdir() + "nets" + File.separator);
	
			if (!readConfig())
				return false;
			
			isLoaded = true;
						
			// <metrics> tag is not existing yet -> create it
			if (getConfDocument().getConfiguration().getMetrics() == null)
				getConfDocument().getConfiguration().addNewMetrics();
			
			// Check if metrics configuration should be loaded
			// and react accordingly
			if (getConfDocument().getConfiguration().getMetrics().getUseMetrics()) {
				WoPeDMetricsConfiguration metricsConfig = new WoPeDMetricsConfiguration();
				metricsConfig.initConfig();
				ConfigurationManager.setMetricsConfiguration(metricsConfig);
			}
		}
		
		return true;
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return indicates whether loading was successful
	 */
	public boolean readConfig() {
		boolean confOk = true;
		String fn = "";

		if (!startedAsApplet) {
			fn = getConfigFilePath();

			if (new File(fn).exists()) {
				LoggerManager.info(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.LoadingFrom") + ": " + fn
								+ ".");
				confOk = readConfig(new File(fn));
			} else {
				LoggerManager.warn(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.FileNotFound") + ": " + fn
								+ ". " + rb.getString("Init.Config.Fallback")
								+ ".");
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
			// if started as an applet, always use built-in config file
			confOk = readConfig(WoPeDConfiguration.class
					.getResourceAsStream(CONFIG_BUILTIN_FILE));
		}

		if (!confOk) {
			JOptionPane.showMessageDialog(null,
					rb.getString("Init.Config.Error") + ". ",
					rb.getString("Init.Config.Error"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		return confOk;
	}

	/**
	 * @param is
	 *            InputStream with config file content
	 * @param configType
	 *            type indicator for type of currently handled configuration
	 * @return indicates whether loading was successful
	 */
	public boolean readConfig(InputStream is) {
		try {
			confDoc = ConfigurationDocument.Factory.parse(is);
			return readConfig(confDoc);
		} catch (XmlException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.ParsingError"));
			return false;
		} catch (IOException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.ReadingError"));
			return false;
		}
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param file
	 * @param configType
	 *            type indicator for type of currently handled configuration
	 * @return
	 */
	public boolean readConfig(File file) {
		if (file.exists()) {
			try {
				// Parse the instance into the type generated from the XML
				// schema
				confDoc = ConfigurationDocument.Factory.parse(file);
				return readConfig(confDoc);
			} catch (XmlException e) {
				LoggerManager.error(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.ParsingError"));
				return false;
			} catch (IOException e) {
				LoggerManager.error(Constants.CONFIG_LOGGER,
						rb.getString("Init.Config.ReadingError"));
				return false;
			}
		} else {
			LoggerManager.error(
					Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.FileNotFound") + ": "
							+ file.getName() + ".");
			return false;
		}
	}

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param confDoc
	 * @return
	 */
	public boolean readConfig(XmlObject configDoc) {
		// Create an instance of a type generated from schema to hold the XML.
		ConfigurationDocument confDoc = (ConfigurationDocument)configDoc;
		ConfigurationDocument.Configuration config;

		if (confDoc != null && (config = confDoc.getConfiguration()) != null) {
			// MAC, WIN, Linux and XPlatform LookAndFeel
			String lafConf = config.getGui().getWindow().getLookAndFeel();
			setLookAndFeel(lafConf);

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
			for (int i = 0; i < getConfDocument().getConfiguration()
					.getGeneral().getRecentFilesArray().length; i++) {
				if (getConfDocument().getConfiguration().getGeneral()
						.getRecentFilesArray()[i].getName() != null)
					runtimeRecentFiles.addElement(new WoPeDRecentFile(
							getConfDocument().getConfiguration()
									.getGeneral().getRecentFilesArray()[i]
									.getName(), getConfDocument()
									.getConfiguration().getGeneral()
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
					rb.getString("Init.Config.LoadingSuccess") + ".");
			return true;
		} else {
			LoggerManager.info(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.LoadingError") + ".");
			return false;
		}
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
			getConfDocument().getConfiguration().getGeneral()
					.setRecentFilesArray(new RecentFile[0]);
			RecentFile xmlRecent;
			for (Iterator<WoPeDRecentFile> iter = runtimeRecentFiles.iterator(); iter
					.hasNext();) {
				WoPeDRecentFile recent = (WoPeDRecentFile) iter.next();
				xmlRecent = getConfDocument().getConfiguration()
						.getGeneral().addNewRecentFiles();
				xmlRecent.setName(recent.getName());
				xmlRecent.setPath(recent.getPath());
			}

			// getConfDocument().getConfiguration().getGeneral().setHomedir(homedir);
			getConfDocument().save(file, xmlOptions);
			LoggerManager.info(Constants.CONFIG_LOGGER, 
					rb.getString("Exit.Config.SavingSuccess") + ": " + file.getName());
			return true;
		} catch (IOException e) {
			LoggerManager.error(Constants.CONFIG_LOGGER,
					rb.getString("Exit.Config.SavingError") + ": " + file.getName());
			return false;
		}
	}

	/**
	 * Returns the editingOnCreation.
	 * 
	 * @return boolean
	 */
	public boolean isEditingOnCreation() {
		return getConfDocument().getConfiguration().getEditor()
				.getEditOnCreation();
	}

	/**
	 * Sets the editingOnCreation.
	 * 
	 * @param editingOnCreation
	 *            The editingOnCreation to set
	 */
	public void setEditingOnCreation(boolean editingOnCreation) {
		if (editingOnCreation != getConfDocument().getConfiguration()
				.getEditor().getEditOnCreation()) {
			getConfDocument().getConfiguration().getEditor()
					.setEditOnCreation(editingOnCreation);
		}
	}

	/**
	 * Returns the insertCOPYwhencopied.
	 * 
	 * @return boolean
	 */
	public boolean isInsertCOPYwhenCopied() {
		return getConfDocument().getConfiguration().getEditor()
				.getInsertCopy();
	}

	/**
	 * Sets the insertCOPYwhencopied.
	 * 
	 * @param insertCOPYwhencopied
	 *            The insertCOPYwhencopied to set
	 */
	public void setInsertCOPYwhenCopied(boolean insertCOPYwhenCopied) {
		getConfDocument().getConfiguration().getEditor()
				.setInsertCopy(insertCOPYwhenCopied);
	}

	/**
	 * Returns the exportToolspecific.
	 * 
	 * @return boolean
	 */
	public boolean isExportToolspecific() {
		return getConfDocument().getConfiguration().getTools()
				.getExporting().getToolspecific();
	}

	/**
	 * Returns the importToolspecific.
	 * 
	 * @return boolean
	 */
	public boolean isImportToolspecific() {
		return getConfDocument().getConfiguration().getTools()
				.getImporting().getToolspecific();
	}

	/**
	 * Sets the exportToolspecific.
	 * 
	 * @param exportToolspecific
	 *            The exportToolspecific to set
	 */
	public void setExportToolspecific(boolean exportToolspecific) {
		getConfDocument().getConfiguration().getTools().getExporting()
				.setToolspecific(exportToolspecific);
	}

	/**
	 * Sets the importToolspecific.
	 * 
	 * @param importToolspecific
	 *            The importToolspecific to set
	 */
	public void setImportToolspecific(boolean importToolspecific) {
		getConfDocument().getConfiguration().getTools().getImporting()
				.setToolspecific(importToolspecific);
	}

	/**
	 * Returns the recentFiles.
	 * 
	 * @return Vector
	 */
	public Vector<WoPeDRecentFile> getRecentFiles() {
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
				if (((WoPeDRecentFile) runtimeRecentFiles.get(idx)).getPath()
						.equals(path)) {
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
	 *            getConfDocument().getConfiguration().getGeneral
	 *            ().setRecentFilesArray ((RecentFile[])recentFiles.toArray());
	 *            }
	 */
	public void removeRecentFile(String name, String path) {
		// delete the old entry if exists.
		for (int idx = 0; idx < runtimeRecentFiles.size(); idx++) {
			if (((WoPeDRecentFile) runtimeRecentFiles.get(idx)).getPath()
					.equals(path)) {
				// delete the entry
				runtimeRecentFiles.remove(idx);
			}
		}
	}

	public void removeAllRecentFiles() {
		runtimeRecentFiles = new Vector<WoPeDRecentFile>(RECENTFILES_SIZE);
	}

	/**
	 * Returns if window is to be maximized on start-up.
	 * 
	 * @return Dimension
	 */
	public boolean isMaximizeWindow() {
		return getConfDocument().getConfiguration().getGui().getWindow()
				.getMaximize();
	}

	/**
	 * Returns the windowSize.
	 * 
	 * @return Dimension
	 */
	public Dimension getWindowSize() {
		return new Dimension(getConfDocument().getConfiguration().getGui()
				.getWindow().getWidth(), getConfDocument()
				.getConfiguration().getGui().getWindow().getHeight());
	}

	/**
	 * Returns the windowX.
	 * 
	 * @return int
	 */
	public int getWindowX() {
		return getConfDocument().getConfiguration().getGui().getWindow()
				.getX();
	}

	/**
	 * Returns the windowY.
	 * 
	 * @return int
	 */
	public int getWindowY() {
		return getConfDocument().getConfiguration().getGui().getWindow()
				.getY();
	}

	/**
	 * Sets the windowSize.
	 * 
	 * @param windowSize
	 *            The windowSize to set
	 */
	public void setWindowSize(Dimension windowSize) {
		getConfDocument().getConfiguration().getGui().getWindow()
				.setHeight((int) windowSize.getHeight());
		getConfDocument().getConfiguration().getGui().getWindow()
				.setWidth((int) windowSize.getWidth());
	}

	public void setMaximizeWindow(boolean maximized) {
		getConfDocument().getConfiguration().getGui().getWindow()
				.setMaximize(maximized);
	}

	/**
	 * Sets the windowX.
	 * 
	 * @param windowX
	 *            The windowX to set
	 */
	public void setWindowX(int windowX) {
		getConfDocument().getConfiguration().getGui().getWindow()
				.setX(windowX);
	}

	/**
	 * Sets the windowY.
	 * 
	 * @param windowY
	 *            The windowY to set
	 */
	public void setWindowY(int windowY) {
		getConfDocument().getConfiguration().getGui().getWindow()
				.setY(windowY);
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
	 * Returns the home directory.
	 * 
	 * @return String
	 */
	public String getHomedir() {
		return getConfDocument().getConfiguration().getGeneral()
				.getHomedir();
	}

	public boolean isHomedirSet() {
		String hd = getConfDocument().getConfiguration().getGeneral()
				.getHomedir();
		return hd != null && !hd.equals("");
	}

	/**
	 * Sets the home directory.
	 * 
	 * @param homedir
	 *            The home directory to set
	 */
	public void setHomedir(String hd) {
		LoggerManager.info(Constants.CONFIG_LOGGER,
				rb.getString("Init.Config.SettingHomeDir") + ": " + hd);
		getConfDocument().getConfiguration().getGeneral().setHomedir(hd);
	}

	/**
	 * Returns the useWoflan.
	 * 
	 * @return boolean
	 */
	public boolean isUseWoflan() {
		return getConfDocument().getConfiguration().getTools().getWoflan()
				.getUseWoflan();
	}

	/**
	 * Returns the woflanPath.
	 * 
	 * @return String
	 */
	public String getWoflanPath() {
		return getConfDocument().getConfiguration().getTools().getWoflan()
				.getWoflanPath();
	}

	/**
	 * Sets the useWoflan.
	 * 
	 * @param useWoflan
	 *            The useWoflan to set
	 */
	public void setUseWoflan(boolean useWoflan) {
		getConfDocument().getConfiguration().getTools().getWoflan()
				.setUseWoflan(useWoflan);
	}

	/**
	 * Sets the woflanPath.
	 * 
	 * @param woflanPath
	 *            The woflanPath to set
	 */
	public void setWoflanPath(String woflanPath) {
		getConfDocument().getConfiguration().getTools().getWoflan()
				.setWoflanPath(woflanPath);
	}

	/**
	 * Returns the smartEditing.
	 * 
	 * @return boolean
	 */
	public boolean isSmartEditing() {
		return getConfDocument().getConfiguration().getEditor()
				.getSmartEditing();
	}

	/**
	 * Sets the smartEditing.
	 * 
	 * @param smartEditing
	 *            The smartEditing to set
	 */
	public void setSmartEditing(boolean smartEditing) {
		getConfDocument().getConfiguration().getEditor()
				.setSmartEditing(smartEditing);
	}

	public boolean isTpnSaveElementAsName() {
		boolean b = getConfDocument().getConfiguration().getTools()
				.getExporting().getTpnExportElementAsName();
		return b;
	}
	
	public void setTpnSaveElementAsName(boolean b) {
		getConfDocument().getConfiguration().getTools().getExporting()
				.setTpnExportElementAsName(b);
	}

	public void setLookAndFeel(String className) {
		getConfDocument().getConfiguration().getGui().getWindow()
				.setLookAndFeel(className);
	}

	public String getLookAndFeel() {
		return getConfDocument().getConfiguration().getGui().getWindow()
				.getLookAndFeel();
	}

	public void setShowGrid(boolean showGrid) {
		getConfDocument().getConfiguration().getEditor()
				.setShowGrid(showGrid);
	}

	public boolean isShowGrid() {
		return getConfDocument().getConfiguration().getEditor()
				.getShowGrid();
	}

	public void setArrowWidth(int width) {
		getConfDocument().getConfiguration().getEditor()
				.setArrowWidth(width);
	}

	public int getArrowWidth() {
		if (getConfDocument().getConfiguration().getEditor()
				.isSetArrowWidth())
			return getConfDocument().getConfiguration().getEditor()
					.getArrowWidth();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getArrowWidth();
	}

	public void setArrowheadSize(int headSize) {
		getConfDocument().getConfiguration().getEditor()
				.setArrowheadSize(headSize);
	}

	public int getArrowheadSize() {
		if (getConfDocument().getConfiguration().getEditor()
				.isSetArrowheadSize())
			return getConfDocument().getConfiguration().getEditor()
					.getArrowheadSize();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getArrowheadSize();
	}

	public boolean isFillArrowHead() {
		if (getConfDocument().getConfiguration().getEditor()
				.isSetArrowFilledHead())
			return getConfDocument().getConfiguration().getEditor()
					.getArrowFilledHead();
		else
			return ConfigurationManager.getStandardConfiguration()
					.isFillArrowHead();
	}

	public void setFillArrowHead(boolean fill) {
		getConfDocument().getConfiguration().getEditor()
				.setArrowFilledHead(fill);
	}

	public boolean isRoundRouting() {
		if (getConfDocument().getConfiguration().getEditor()
				.isSetRoundRouting())
			return getConfDocument().getConfiguration().getEditor()
					.getRoundRouting();
		else
			return false;
	}
	
	public void setRoundRouting(boolean round) {
		getConfDocument().getConfiguration().getEditor()
				.setRoundRouting(round);
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
	 * @see
	 * org.woped.editor.config.IConfiguration#setLocaleLanguage(java.lang.String
	 * )
	 */
	public void setLocaleLanguage(String language) {
		getConfDocument().getConfiguration().getGui().getLocale()
				.setLanguage(language);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#getLocaleLanguage()
	 */
	public String getLocaleLanguage() {
		return getConfDocument().getConfiguration().getGui().getLocale()
				.getLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.woped.editor.config.IConfiguration#setLocaleCountry(java.lang.String)
	 */
	public void setLocaleCountry(String country) {
		getConfDocument().getConfiguration().getGui().getLocale()
				.setCountry(country);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#getLocaleCountry()
	 */
	public String getLocaleCountry() {
		return getConfDocument().getConfiguration().getGui().getLocale()
				.getCountry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.woped.editor.config.IConfiguration#setLocaleVariant(java.lang.String)
	 */
	public void setLocaleVariant(String variant) {
		getConfDocument().getConfiguration().getGui().getLocale()
				.setVariant(variant);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.editor.config.IConfiguration#getLocaleVariant()
	 */
	public String getLocaleVariant() {
		return getConfDocument().getConfiguration().getGui().getLocale()
				.getVariant();
	}

	public void setLocale() {
		String language = null;
		String country = null;
		String variant = null;
		Locale userLocale = null;

		// language is English if startedAsApplet, using default Resource File
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
		} else if (language != null) {
			userLocale = new Locale(language);
		} else {
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
		if (getConfDocument().getConfiguration().getColoring() != null)
			return getConfDocument().getConfiguration().getColoring()
					.getColorOn();
		else
			return false;
	}

	public void setColorOn(boolean b) {
		getConfDocument().getConfiguration().getColoring().setColorOn(b);
	}

	public void setColor1(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor1(rgb);
	}

	public int getColor1() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor1();
	}

	public void setColor2(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor2(rgb);
	}

	public int getColor2() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor2();
	}

	public void setColor3(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor3(rgb);
	}

	public int getColor3() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor3();
	}

	public void setColor4(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor4(rgb);
	}

	public int getColor4() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor4();
	}

	public void setColor5(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor5(rgb);
	}

	public int getColor5() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor5();
	}

	public void setColor6(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor6(rgb);
	}

	public int getColor6() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor6();
	}

	public void setColor7(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor7(rgb);
	}

	public int getColor7() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor7();
	}

	public void setColor8(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor8(rgb);
	}

	public int getColor8() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor8();
	}

	public void setColor9(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor9(rgb);
	}

	public int getColor9() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor9();
	}

	public void setColor10(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor10(rgb);
	}

	public int getColor10() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor10();
	}

	public void setColor11(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor11(rgb);
	}

	public int getColor11() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor11();
	}

	public void setColor12(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor12(rgb);
	}

	public int getColor12() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor12();
	}

	public void setColor13(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor13(rgb);
	}

	public int getColor13() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor13();
	}

	public void setColor14(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor14(rgb);
	}

	public int getColor14() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor14();
	}

	public void setColor15(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor15(rgb);
	}

	public int getColor15() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor15();
	}

	public void setColor16(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor16(rgb);
	}

	public int getColor16() {
		return getConfDocument().getConfiguration().getColoring()
				.getColor16();
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
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor1();
	}

	public int getDefaultColor2() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor2();
	}

	public int getDefaultColor3() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor3();
	}

	public int getDefaultColor4() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor4();
	}

	public int getDefaultColor5() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor5();
	}

	public int getDefaultColor6() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor6();
	}

	public int getDefaultColor7() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor7();
	}

	public int getDefaultColor8() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor8();
	}

	public int getDefaultColor9() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor9();
	}

	public int getDefaultColor10() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor10();
	}

	public int getDefaultColor11() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor11();
	}

	public int getDefaultColor12() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor12();
	}

	public int getDefaultColor13() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor13();
	}

	public int getDefaultColor14() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor14();
	}

	public int getDefaultColor15() {
		return getConfDocument().getConfiguration().getColoring()
				.getDefaultcolor15();
	}

	public int getDefaultColor16() {
		return getConfDocument().getConfiguration().getColoring()
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
		if (getConfDocument().getConfiguration().getColoring() != null)
			return getConfDocument().getConfiguration().getColoring()
					.getAlgorithmMode();
		else
			return 0;
	}

	public void setAlgorithmMode(int n) {
		getConfDocument().getConfiguration().getColoring()
				.setAlgorithmMode(n);
	}
	// End Understandability Coloring configuration

	public boolean isUseMetrics() {
		return getConfDocument().getConfiguration().getMetrics().getUseMetrics();
	}

	public void setUseMetrics(boolean loadMetrics) {
		getConfDocument().getConfiguration().getMetrics().setUseMetrics(loadMetrics);		
	}

	public int getAlgorithmDecimalPlaces() {
		return getConfDocument().getConfiguration().getMetrics().getAlgorithmDecimalPlaces();	
	}

	public int getVariableDecimalPlaces() {
		return getConfDocument().getConfiguration().getMetrics().getVariableDecimalPlaces();		
	}

	public void setAlgorithmDecimalPlaces(int n) {
		getConfDocument().getConfiguration().getMetrics().setAlgorithmDecimalPlaces(n);
	}

	public void setVariableDecimalPlaces(int n) {
		getConfDocument().getConfiguration().getMetrics().setVariableDecimalPlaces(n);	
	}

	public boolean isUseAlgorithmHighlighting() {
		return getConfDocument().getConfiguration().getMetrics().getUseAlgorithmHighlighting();
	}

	public void setUseAlgorithmHighlighting(boolean useHighlighting) {
		getConfDocument().getConfiguration().getMetrics().setUseAlgorithmHighlighting(useHighlighting);		
	}

	public boolean isShowNamesInBuilder() {
		return getConfDocument().getConfiguration().getMetrics().getShowNamesInBuilder();
	}

	public void setShowNamesInBuilder(boolean showNames) {
		getConfDocument().getConfiguration().getMetrics().setShowNamesInBuilder(showNames);
	}

	public boolean isShowAdvancedErrorMessages() {
		return getConfDocument().getConfiguration().getMetrics().getShowAdvancedErrorMessages();
	}

	public void setShowAdvancedErrorMessages(boolean showAdvanced) {
		getConfDocument().getConfiguration().getMetrics().setShowAdvancedErrorMessages(showAdvanced);
	}

	@Override
	public void setApromoreServer(String server) {
		getConfDocument().getConfiguration().getTools()
				.setAproServerName(server);
		
	}

	@Override
	public void setApromoreUsername(String user) {
		getConfDocument().getConfiguration().getTools()
				.setAproUserName(user);
	}

	@Override
	public void setApromoreProxyName(String proxyName) {
		getConfDocument().getConfiguration().getTools()
				.setAproProxyName(proxyName);
	}


	@Override
	public void setApromoreProxyPort(int port) {
		getConfDocument().getConfiguration().getTools()
				.setAproProxyPort(port);
	}

	public String getApromoreServer() {
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproServerName())
			return getConfDocument().getConfiguration().getTools()
					.getAproServerName();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreServer();
	}
	
	@Override
	public String getApromoreUsername() {
		
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproUserName())
			return getConfDocument().getConfiguration().getTools()
					.getAproUserName();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreUsername();
	}

	@Override
	public String getApromoreProxyName() {
		
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproProxyName())
			return getConfDocument().getConfiguration().getTools()
					.getAproProxyName();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreProxyName();
	}

	@Override
	public int getApromoreProxyPort() {
		
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproProxyPort())
			return getConfDocument().getConfiguration().getTools()
					.getAproProxyPort();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreProxyPort();
	}

	@Override
	public int getApromoreServerPort() {
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproServerPort())
			return getConfDocument().getConfiguration().getTools()
					.getAproServerPort();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreServerPort();
	}

	@Override
	public boolean getApromoreUseProxy() {
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproUseProxy())
			return getConfDocument().getConfiguration().getTools()
					.getAproUseProxy();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreUseProxy(); 
	}

	@Override
	public void setApromoreServerPort(int port) {
		getConfDocument().getConfiguration().getTools()
		.setAproServerPort(port);
		
	}

	@Override
	public void setApromoreUseProxy(boolean set) {
		getConfDocument().getConfiguration().getTools()
		.setAproUseProxy(set);

		
	}

	@Override
	public void setApromoreUse(boolean selected) {
		getConfDocument().getConfiguration().getTools()
		.setAproUse(selected);

	}

	@Override
	public boolean getApromoreUse() {
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproUse())
			return getConfDocument().getConfiguration().getTools()
					.getAproUse();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreUse(); 
	}
}
