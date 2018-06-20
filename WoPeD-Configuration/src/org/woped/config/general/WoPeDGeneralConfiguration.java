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
import org.woped.config.BusinessDashboard;
import org.woped.config.ConfigurationDocument;
import org.woped.config.Constants;
import org.woped.config.RecentFile;
import org.woped.config.Registration;
import org.woped.config.ApromoreServer;
import org.woped.config.WoPeDConfiguration;
import org.woped.config.metrics.WoPeDMetricsConfiguration;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IGeneralConfiguration;
import org.woped.core.utilities.LoggerManager;

/**
 * Class that provides access to the general WoPeD configuration settings.
 * Access to it at runtime is to be gained through ConfigurationManager.
 * 
 * @see ConfigurationManager
 * @author Philip Allgaier
 *
 */
public class WoPeDGeneralConfiguration extends WoPeDConfiguration implements
		IGeneralConfiguration {

	private static org.woped.config.ConfigurationDocument confDoc = null;
	private static String CONFIG_FILE = "WoPeDconfig.xml";
	private static String CONFIG_BUILTIN_FILE = "/org/woped/config/general/WoPeDconfig.xml";

	private Locale locale = null;

	private static int RECENTFILES_SIZE = 10;
	private Vector<WoPeDRecentFile> runtimeRecentFiles = new Vector<WoPeDRecentFile>(
			RECENTFILES_SIZE);

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
				
			// <business dashboard> tag is not existing yet -> create it
			if (getConfDocument().getConfiguration().getBusinessdashboard() == null)
				getConfDocument().getConfiguration().addNewBusinessdashboard();

			// <p2t> tag is not existing yet -> create it
			if (getConfDocument().getConfiguration().getP2T() == null)
				getConfDocument().getConfiguration().addNewP2T();

			// <t2p> tag is not existing yet -> create it
			if (getConfDocument().getConfiguration().getT2P() == null)
				getConfDocument().getConfiguration().addNewT2P();

			// <metrics> tag is not existing yet -> create it
			if (getConfDocument().getConfiguration().getMetrics() == null)
				getConfDocument().getConfiguration().addNewMetrics();

			// Check if metrics configuration should be loaded
			// and react accordingly
			if (getConfDocument().getConfiguration().getMetrics()
					.getUseMetrics()) {
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

		fn = getConfigFilePath();

		if (new File(fn).exists()) {
			LoggerManager.info(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.LoadingFrom") + ": " + fn + ".");
			confOk = readConfig(new File(fn));
		} else {
			LoggerManager.warn(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.FileNotFound") + ": " + fn + ". "
							+ rb.getString("Init.Config.Fallback") + ".");
			confOk = readConfig(WoPeDConfiguration.class
					.getResourceAsStream(CONFIG_BUILTIN_FILE));

			if (confOk) {
				setLocaleLanguage(Locale.getDefault().getLanguage());
				setLocaleCountry(Locale.getDefault().getCountry());
				setLocaleVariant(Locale.getDefault().getVariant());
				setLocale();
			}
		}

		if (!confOk) {
			JOptionPane.showMessageDialog(null,
					rb.getString("Init.Config.Error") + ". ",
					rb.getString("Init.Config.Error"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		File ud = new File(getUserdir());
		if (!ud.exists())
			ud.mkdir();

		File hd = new File(getHomedir());
		if (!hd.exists())
			hd.mkdir();

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
		ConfigurationDocument confDoc = (ConfigurationDocument) configDoc;
		ConfigurationDocument.Configuration config;

		if (confDoc != null && (config = confDoc.getConfiguration()) != null) {

			// MAC, WIN, Linux and XPlatform LookAndFeel
			setLookAndFeel(config.getGui().getWindow().getLookAndFeel());

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
							getConfDocument().getConfiguration().getGeneral()
									.getRecentFilesArray()[i].getName(),
							getConfDocument().getConfiguration().getGeneral()
									.getRecentFilesArray()[i].getPath()));
			}

			// Homedir
			if (isHomedirSet())
				setHomedir(config.getGeneral().getHomedir());
			else
				setHomedir(getDefaultHomedir());
			
			LoggerManager.info(Constants.CONFIG_LOGGER,
					rb.getString("Init.Config.SettingHomeDir") + ": " + getHomedir());

			// Current working dir for file operations
			setCurrentWorkingdir(config.getGeneral().getCurrentWorkingdir());
			if (!isCurrentWorkingdirSet())
				setCurrentWorkingdir(config.getGeneral().getHomedir());

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

			setRegistered(config.getRegistration().getRegistered());

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
				xmlRecent = getConfDocument().getConfiguration().getGeneral()
						.addNewRecentFiles();
				xmlRecent.setName(recent.getName());
				xmlRecent.setPath(recent.getPath());
			}

			getConfDocument().getConfiguration().getGeneral()
					.setCurrentWorkingdir(currentWorkingdir);

			if (getConfDocument().getConfiguration().getRegistration() != null) {
				int n = getConfDocument().getConfiguration().getRegistration()
						.getLaunchCounter();
				getConfDocument().getConfiguration().getRegistration()
						.setLaunchCounter(n + 1);
			} else {
				Registration reg = Registration.Factory.newInstance();
				reg.setEmailAddress("");
				reg.setShowOnStartup(true);
				reg.setRegistered(false);
				reg.setLaunchCounter(0);
				getConfDocument().getConfiguration().setRegistration(reg);
			}

			getConfDocument().save(file, xmlOptions);
			return true;
		} catch (IOException e) {
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
		return getConfDocument().getConfiguration().getEditor().getInsertCopy();
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
		return getConfDocument().getConfiguration().getTools().getExporting()
				.getToolspecific();
	}

	/**
	 * Returns the importToolspecific.
	 * 
	 * @return boolean
	 */
	public boolean isImportToolspecific() {
		return getConfDocument().getConfiguration().getTools().getImporting()
				.getToolspecific();
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
	 *            recentFiles) { getConfDocument().getConfiguration().getGeneral
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
				.getWindow().getWidth(), getConfDocument().getConfiguration()
				.getGui().getWindow().getHeight());
	}

	/**
	 * Returns the windowX.
	 * 
	 * @return int
	 */
	public int getWindowX() {
		return getConfDocument().getConfiguration().getGui().getWindow().getX();
	}

	/**
	 * Returns the windowY.
	 * 
	 * @return int
	 */
	public int getWindowY() {
		return getConfDocument().getConfiguration().getGui().getWindow().getY();
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
		getConfDocument().getConfiguration().getGui().getWindow().setX(windowX);
	}

	/**
	 * Sets the windowY.
	 * 
	 * @param windowY
	 *            The windowY to set
	 */
	public void setWindowY(int windowY) {
		getConfDocument().getConfiguration().getGui().getWindow().setY(windowY);
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
		ConfigurationDocument confDoc = getConfDocument();
		return confDoc.getConfiguration().getGeneral().getHomedir();
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
		getConfDocument().getConfiguration().getEditor().setShowGrid(showGrid);
	}

	public boolean isShowGrid() {
		return getConfDocument().getConfiguration().getEditor().getShowGrid();
	}

	public void setArrowWidth(int width) {
		getConfDocument().getConfiguration().getEditor().setArrowWidth(width);
	}

	public int getArrowWidth() {
		if (getConfDocument().getConfiguration().getEditor().isSetArrowWidth())
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
		getConfDocument().getConfiguration().getEditor().setRoundRouting(round);
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
		String language = getConfDocument().getConfiguration().getGui()
				.getLocale().getLanguage();
		if (language.equals("")) {
			language = Locale.getDefault().getLanguage();
		}
		return language;
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
		String country = getConfDocument().getConfiguration().getGui()
				.getLocale().getCountry();
		if (country.equals("")) {
			country = Locale.getDefault().getCountry();
		}
		return country;
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
		String variant = getConfDocument().getConfiguration().getGui()
				.getLocale().getVariant();
		if (variant.equals("")) {
			variant = Locale.getDefault().getVariant();
		}
		return variant;
	}

	public void setLocale() {
		this.locale = new Locale(getLocaleLanguage(), getLocaleCountry(),
				getLocaleVariant());
		Locale.setDefault(this.locale);
		LoggerManager.info(Constants.CONFIG_LOGGER,
				(this.locale.equals(Locale.GERMANY) ? "Setze Locale auf "
						: "Setting Locale to ") + this.locale);
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
		return getConfDocument().getConfiguration().getColoring().getColor1();
	}

	public void setColor2(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor2(rgb);
	}

	public int getColor2() {
		return getConfDocument().getConfiguration().getColoring().getColor2();
	}

	public void setColor3(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor3(rgb);
	}

	public int getColor3() {
		return getConfDocument().getConfiguration().getColoring().getColor3();
	}

	public void setColor4(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor4(rgb);
	}

	public int getColor4() {
		return getConfDocument().getConfiguration().getColoring().getColor4();
	}

	public void setColor5(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor5(rgb);
	}

	public int getColor5() {
		return getConfDocument().getConfiguration().getColoring().getColor5();
	}

	public void setColor6(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor6(rgb);
	}

	public int getColor6() {
		return getConfDocument().getConfiguration().getColoring().getColor6();
	}

	public void setColor7(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor7(rgb);
	}

	public int getColor7() {
		return getConfDocument().getConfiguration().getColoring().getColor7();
	}

	public void setColor8(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor8(rgb);
	}

	public int getColor8() {
		return getConfDocument().getConfiguration().getColoring().getColor8();
	}

	public void setColor9(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor9(rgb);
	}

	public int getColor9() {
		return getConfDocument().getConfiguration().getColoring().getColor9();
	}

	public void setColor10(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor10(rgb);
	}

	public int getColor10() {
		return getConfDocument().getConfiguration().getColoring().getColor10();
	}

	public void setColor11(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor11(rgb);
	}

	public int getColor11() {
		return getConfDocument().getConfiguration().getColoring().getColor11();
	}

	public void setColor12(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor12(rgb);
	}

	public int getColor12() {
		return getConfDocument().getConfiguration().getColoring().getColor12();
	}

	public void setColor13(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor13(rgb);
	}

	public int getColor13() {
		return getConfDocument().getConfiguration().getColoring().getColor13();
	}

	public void setColor14(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor14(rgb);
	}

	public int getColor14() {
		return getConfDocument().getConfiguration().getColoring().getColor14();
	}

	public void setColor15(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor15(rgb);
	}

	public int getColor15() {
		return getConfDocument().getConfiguration().getColoring().getColor15();
	}

	public void setColor16(int rgb) {
		getConfDocument().getConfiguration().getColoring().setColor16(rgb);
	}

	public int getColor16() {
		return getConfDocument().getConfiguration().getColoring().getColor16();
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
		getConfDocument().getConfiguration().getColoring().setAlgorithmMode(n);
	}

	// End Understandability Coloring configuration

	public boolean isUseMetrics() {
		return getConfDocument().getConfiguration().getMetrics()
				.getUseMetrics();
	}

	public void setUseMetrics(boolean loadMetrics) {
		getConfDocument().getConfiguration().getMetrics()
				.setUseMetrics(loadMetrics);
	}

	public int getAlgorithmDecimalPlaces() {
		return getConfDocument().getConfiguration().getMetrics()
				.getAlgorithmDecimalPlaces();
	}

	public int getVariableDecimalPlaces() {
		return getConfDocument().getConfiguration().getMetrics()
				.getVariableDecimalPlaces();
	}

	public void setAlgorithmDecimalPlaces(int n) {
		getConfDocument().getConfiguration().getMetrics()
				.setAlgorithmDecimalPlaces(n);
	}

	public void setVariableDecimalPlaces(int n) {
		getConfDocument().getConfiguration().getMetrics()
				.setVariableDecimalPlaces(n);
	}

	public boolean isUseAlgorithmHighlighting() {
		return getConfDocument().getConfiguration().getMetrics()
				.getUseAlgorithmHighlighting();
	}

	public void setUseAlgorithmHighlighting(boolean useHighlighting) {
		getConfDocument().getConfiguration().getMetrics()
				.setUseAlgorithmHighlighting(useHighlighting);
	}

	public boolean isShowNamesInBuilder() {
		return getConfDocument().getConfiguration().getMetrics()
				.getShowNamesInBuilder();
	}

	public void setShowNamesInBuilder(boolean showNames) {
		getConfDocument().getConfiguration().getMetrics()
				.setShowNamesInBuilder(showNames);
	}

	public boolean isShowAdvancedErrorMessages() {
		return getConfDocument().getConfiguration().getMetrics()
				.getShowAdvancedErrorMessages();
	}

	public void setShowAdvancedErrorMessages(boolean showAdvanced) {
		getConfDocument().getConfiguration().getMetrics()
				.setShowAdvancedErrorMessages(showAdvanced);
	}

	@Override
	public void setApromoreServerURL(String server) {
		getConfDocument().getConfiguration().getTools()
				.setAproServerURL(server);

	}

	@Override
	public void setApromoreUsername(String user) {
		getConfDocument().getConfiguration().getTools().setAproUserName(user);
	}

	@Override
	public void setApromoreProxyName(String proxyName) {
		getConfDocument().getConfiguration().getTools()
				.setAproProxyName(proxyName);
	}

	@Override
	public void setApromoreProxyPort(int port) {
		getConfDocument().getConfiguration().getTools().setAproProxyPort(port);
	}

	@Override
	public void setApromoreServerName(String name) {
		getConfDocument().getConfiguration().getTools().setAproServerName(name);
	}

	@Override
	public String getApromoreServerName() {
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproServerName())
			return getConfDocument().getConfiguration().getTools()
					.getAproServerName();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreServerName();
	}

	public String getApromoreServerURL() {
		if (getConfDocument().getConfiguration().getTools()
				.isSetAproServerURL())
			return getConfDocument().getConfiguration().getTools()
					.getAproServerURL();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreServerURL();
	}

	@Override
	public String getApromoreManagerPath() {
		// ttet

		if (getConfDocument().getConfiguration().getTools()
				.isSetAproManagerPath())
			return getConfDocument().getConfiguration().getTools()
					.getAproManagerPath();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreManagerPath();
	}

	@Override
	public String getApromoreUsername() {

		if (getConfDocument().getConfiguration().getTools().isSetAproUserName())
			return getConfDocument().getConfiguration().getTools()
					.getAproUserName();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreUsername();
	}

	@Override
	public String getApromorePassword() {

		if (getConfDocument().getConfiguration().getTools().isSetAproPassword())
			return getConfDocument().getConfiguration().getTools()
					.getAproPassword();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromorePassword();
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
		if (getConfDocument().getConfiguration().getTools().isSetAproUseProxy())
			return getConfDocument().getConfiguration().getTools()
					.getAproUseProxy();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreUseProxy();
	}

	@Override
	public void setApromoreServerPort(int port) {
		getConfDocument().getConfiguration().getTools().setAproServerPort(port);
	}

	@Override
	public void setApromoreUseProxy(boolean set) {
		getConfDocument().getConfiguration().getTools().setAproUseProxy(set);
	}

	@Override
	public void setApromoreManagerPath(String path) {
		getConfDocument().getConfiguration().getTools()
				.setAproManagerPath(path);
	}

	@Override
	public void setApromoreUse(boolean selected) {
		getConfDocument().getConfiguration().getTools().setAproUse(selected);
	}

	@Override
	public void setApromorePassword(String pwd) {
		getConfDocument().getConfiguration().getTools().setAproPassword(pwd);

	}

	@Override
	public boolean getApromoreUse() {
		if (getConfDocument().getConfiguration().getTools().isSetAproUse())
			return getConfDocument().getConfiguration().getTools().getAproUse();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getApromoreUse();
	}

	@Override
	public int getCurrentApromoreIndex() {
		if (getConfDocument().getConfiguration().getTools().isSetAproUse())
			return getConfDocument().getConfiguration().getTools()
					.getAproCurrentIndex();
		else
			return ConfigurationManager.getStandardConfiguration()
					.getCurrentApromoreIndex();
	}

	@Override
	public void setCurrentApromoreIndex(int index) {
		getConfDocument().getConfiguration().getTools()
				.setAproCurrentIndex(index);
	}

	@Override
	public boolean isSetApromorePassword() {
		return getConfDocument().getConfiguration().getTools()
				.isSetAproPassword();
	}

	@Override
	public boolean isRegistered() {
		return getConfDocument().getConfiguration().getRegistration()
				.getRegistered();
	}

	@Override
	public void setRegistered(boolean b) {
		getConfDocument().getConfiguration().getRegistration().setRegistered(b);
	}

	@Override
	public boolean isShowOnStartup() {
		return getConfDocument().getConfiguration().getRegistration()
				.getShowOnStartup();
	}

	@Override
	public void setShowOnStartup(boolean selected) {
		getConfDocument().getConfiguration().getRegistration()
				.setShowOnStartup(selected);
	}

	@Override
	public String getRegistrationEmail() {
		return getConfDocument().getConfiguration().getRegistration()
				.getEmailAddress();
	}

	@Override
	public void setRegistrationEmail(String address) {
		getConfDocument().getConfiguration().getRegistration()
				.setEmailAddress(address);
	}

	@Override
	public int getLaunchCounter() {
		return getConfDocument().getConfiguration().getRegistration()
				.getLaunchCounter();
	}

	@Override
	public void setLaunchCounter(int value) {
		getConfDocument().getConfiguration().getRegistration()
				.setLaunchCounter(value);
	}

	@Override
	public boolean isSetApromoreServers() {
		boolean result = getConfDocument().getConfiguration()
				.isSetApromoreServers();
		return result;
	}

	@Override
	public String[] getApromoreServerNames() {
		String[] serverNames = new String[getApromoreServerListLength()];
		ApromoreServer[] servers = getConfDocument().getConfiguration()
				.getApromoreServers().getApromoreServerArray();
		for (ApromoreServer server : servers) {
			for (int i = 0; i < servers.length; i++)
				serverNames[i] = server.getApromoreServerName();
		}
		return serverNames;
	}

	@Override
	public int getApromoreServerListLength() {
		return getConfDocument().getConfiguration().getApromoreServers()
				.sizeOfApromoreServerArray();
	}

	@Override
	public ApromoreServer[] getApromoreServers() {
		return getConfDocument().getConfiguration().getApromoreServers()
				.getApromoreServerArray();
	}

	@Override
	public void addApromoreServer(int ID, String name, String url, int port,
			String path, String user, String pwd, boolean useProxy,
			String proxyUrl, int proxyPort) {
		if (isSetApromoreServers()) {
			setServerAttribute(ID, name, url, port, path, user, pwd, useProxy,
					proxyUrl, proxyPort);
		} else {
			addServersList();
			setServerAttribute(ID, name, url, port, path, user, pwd, useProxy,
					proxyUrl, proxyPort);
		}
	}

	private void setServerAttribute(int ID, String name, String url, int port,
			String path, String user, String pwd, boolean useProxy,
			String proxyUrl, int proxyPort) {
		getConfDocument().getConfiguration().getApromoreServers()
				.addNewApromoreServer();
		int i = getApromoreServerListLength();
		i = i - 1;
		ApromoreServer[] servers = getApromoreServers();
		servers[i].setApromoreServerID(ID);
		servers[i].setApromoreServerName(name);
		servers[i].setApromoreServerURL(url);
		servers[i].setApromoreServerPort(port);
		servers[i].setApromoreManagerPath(path);
		servers[i].setApromoreUserName(user);
		servers[i].setApromorePassword(pwd);
		servers[i].setApromoreUseProxy(useProxy);
		servers[i].setApromoreProxyName(proxyUrl);
		servers[i].setApromoreProxyPort(proxyPort);
	}

	private void addServersList() {
		getConfDocument().getConfiguration().addNewApromoreServers();

	}

	@Override
	public void changeApromoreServerSettings(int ID, String name, String url,
			int port, String path, String user, String pwd, boolean useProxy,
			String proxyUrl, int proxyPort) {
		ApromoreServer[] servers = getApromoreServers();
		for (ApromoreServer server : servers) {
			if (server.getApromoreServerID() == ID) {
				server.setApromoreServerName(name);
				server.setApromoreServerURL(url);
				server.setApromoreServerPort(port);
				server.setApromoreManagerPath(path);
				server.setApromoreUserName(user);
				server.setApromorePassword(pwd);
				server.setApromoreUseProxy(useProxy);
				server.setApromoreProxyName(proxyUrl);
				server.setApromoreProxyPort(proxyPort);
			}
		}
	}

	@Override
	public void removeApromoreServer(int index) {
		getConfDocument().getConfiguration().getApromoreServers()
				.removeApromoreServer(index);
	}

	// Process 2 Text
	@Override
	public String getProcess2TextServerHost() {
		if (getConfDocument().getConfiguration().getP2T().isSetP2TServerHost()) {
			return getConfDocument().getConfiguration().getP2T()
					.getP2TServerHost();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getProcess2TextServerHost();
	}

	@Override
	public String getText2ProcessServerHost() {
		if (getConfDocument().getConfiguration().getT2P().isSetT2PServerHost()) {
			return getConfDocument().getConfiguration().getT2P()
					.getT2PServerHost();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getText2ProcessServerHost();
	}

	@Override
	public void setProcess2TextServerHost(String host) {
		getConfDocument().getConfiguration().getP2T().setP2TServerHost(host);
	}

	@Override
	public int getProcess2TextServerPort() {
		if (getConfDocument().getConfiguration().getP2T().isSetP2TServerPort()) {
			return getConfDocument().getConfiguration().getP2T()
					.getP2TServerPort();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getProcess2TextServerPort();
	}

	@Override
	public void setProcess2TextServerPort(int port) {
		getConfDocument().getConfiguration().getP2T().setP2TServerPort(port);
	}

	@Override
	public String getProcess2TextServerURI() {
		if (getConfDocument().getConfiguration().getP2T().isSetP2TServerURI()) {
			return getConfDocument().getConfiguration().getP2T()
					.getP2TServerURI();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getProcess2TextServerURI();
	}

	@Override
	public void setProcess2TextServerURI(String uri) {
		getConfDocument().getConfiguration().getP2T().setP2TServerURI(uri);
	}

	@Override
	public boolean getProcess2TextUse() {
		if (getConfDocument().getConfiguration().getP2T().isSetP2TUse()) {
			return getConfDocument().getConfiguration().getP2T().getP2TUse();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getProcess2TextUse();
	}

	@Override
	public void setProcess2TextUse(boolean selected) {
		getConfDocument().getConfiguration().getP2T().setP2TUse(selected);
	}

	// Text 2 Process
	@Override
	public void setText2ProcessServerHost(String host) {
		getConfDocument().getConfiguration().getT2P().setT2PServerHost(host);
	}

	@Override
	public int getText2ProcessServerPort() {
		if (getConfDocument().getConfiguration().getT2P().isSetT2PServerPort()) {
			return getConfDocument().getConfiguration().getT2P()
					.getT2PServerPort();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getText2ProcessServerPort();
	}

	@Override
	public void setText2ProcessServerPort(int port) {
		getConfDocument().getConfiguration().getT2P().setT2PServerPort(port);
	}

	@Override
	public String getText2ProcessServerURI() {
		if (getConfDocument().getConfiguration().getP2T().isSetP2TUse()) {
			return getConfDocument().getConfiguration().getT2P()
					.getT2PServerURI();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getText2ProcessServerURI();
	}

	@Override
	public void setText2ProcessServerURI(String uri) {
		getConfDocument().getConfiguration().getT2P().setT2PServerURI(uri);
	}

	@Override
	public boolean getText2ProcessUse() {
		if (getConfDocument().getConfiguration().getT2P().isSetT2PUse()) {
			return getConfDocument().getConfiguration().getT2P().getT2PUse();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getText2ProcessUse();
	}

	@Override
	public void setText2ProcessUse(boolean selected) {
		getConfDocument().getConfiguration().getT2P().setT2PUse(selected);
	}

	@Override
	public int getBusinessDashboardServerPort() {
		
		BusinessDashboard bd = getConfDocument().getConfiguration().getBusinessdashboard();
		
		if (bd.isSetServerport()) {
			return getConfDocument().getConfiguration().getBusinessdashboard()
					.getServerport();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getBusinessDashboardServerPort();
	}

	@Override
	public void setBusinessDashboardServerPort(int port) {
		
		getConfDocument().getConfiguration().getBusinessdashboard().setServerport(port);
		
	}

	@Override
	public boolean getBusinessDashboardUseByDefault() {
		BusinessDashboard bd = getConfDocument().getConfiguration().getBusinessdashboard();
		
		if (bd.isSetUsebydefault()) {
			return getConfDocument().getConfiguration().getBusinessdashboard()
					.getUsebydefault();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getBusinessDashboardUseByDefault();
	}

	@Override
	public void setBusinessDashboardUseByDefault(boolean bAutoStart) {
		getConfDocument().getConfiguration().getBusinessdashboard().setUsebydefault(bAutoStart);
		
	}

	@Override
	public int getBusinessDashboardMaxValues() {
		BusinessDashboard bd = getConfDocument().getConfiguration().getBusinessdashboard();
		
		if (bd.isSetMaxvalues()) {
			return getConfDocument().getConfiguration().getBusinessdashboard()
					.getMaxvalues();
		} else
			return ConfigurationManager.getStandardConfiguration()
					.getBusinessDashboardMaxValues();
	}

	@Override
	public void setBusinessDashboardMaxValues(int maxvalues) {
		getConfDocument().getConfiguration().getBusinessdashboard().setMaxvalues(maxvalues);
		
	}
}
