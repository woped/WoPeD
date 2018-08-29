package org.woped.core.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.Locale;
import java.util.Vector;

import org.woped.config.ApromoreServer;

/**
 * Class that provides fallback configuration settings for the general WoPeD
 * configuration part
 * 
 * @author Philip Allgaier
 *
 */
@SuppressWarnings("JavadocReference")
public class DefaultStaticConfiguration implements IGeneralConfiguration {
	// Default values
	public static int DEFAULT_ARROW_WIDTH = 1;
	public static int DEFAULT_ARROW_HEADSIZE = 7;
	public static boolean DEFAULT_ARROW_FILLHEAD = false;
	public static Color DEFAULT_SELECTION_COLOR = Color.BLUE;
	public static Color DEFAULT_INVERSE_COLOR = Color.WHITE;
	public static Color DEFAULT_PORT_COLOR = Color.RED;
	public static Color DEFAULT_UI_BACKGROUND_COLOR = Color.GRAY;
	public static Color DEFAULT_CELL_BACKGROUND_COLOR = new Color(225, 225, 225);
	public static Color DEFAULT_HEADER_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static Color DEFAULT_TIME_COLOR = new Color(0, 128, 0);
	public static Color DEFAULT_SUBPROCESS_FRAME_COLOR = new Color(255, 151, 5);
	public static Font DEFAULT_HUGELABEL_FONT = new Font("Verdana", Font.PLAIN,
			14);
	public static Font DEFAULT_HUGELABEL_BOLDFONT = new Font("Verdana",
			Font.BOLD, 14);
	public static Font DEFAULT_HUGELABEL_ITALICFONT = new Font("Verdana",
			Font.ITALIC, 14);
	public static Font DEFAULT_BIGLABEL_FONT = new Font("Verdana", Font.PLAIN,
			12);
	public static Font DEFAULT_BIGLABEL_BOLDFONT = new Font("Verdana",
			Font.BOLD, 12);
	public static Font DEFAULT_BIGLABEL_ITALICFONT = new Font("Verdana",
			Font.ITALIC, 12);
	public static Font DEFAULT_LABEL_FONT = new Font("Verdana", Font.PLAIN, 11);
	public static Font DEFAULT_LABEL_BOLDFONT = new Font("Verdana", Font.BOLD,
			11);
	public static Font DEFAULT_LABEL_ITALICFONT = new Font("Verdana",
			Font.ITALIC, 11);
	public static Font DEFAULT_SMALLLABEL_FONT = new Font("Verdana",
			Font.PLAIN, 10);
	public static Font DEFAULT_SMALLLABEL_BOLDFONT = new Font("Verdana",
			Font.BOLD, 10);
	public static Font DEFAULT_SMALLLABEL_ITALICFONT = new Font("Verdana",
			Font.ITALIC, 10);
	public static Font DEFAULT_TINYLABEL_FONT = new Font("Verdana", Font.PLAIN,9);
	public static Font DEFAULT_TINYLABEL_BOLDFONT = new Font("Verdana",Font.BOLD, 9);
	public static Font DEFAULT_TINYLABEL_ITALICFONT = new Font("Verdana",	Font.ITALIC, 9);
	public static Font DEFAULT_RESOURCE_ROLE_FONT = new Font("Verdana",
			Font.PLAIN, 9);
	public static Font DEFAULT_RESOURCE_ORG_FONT = new Font("Verdana",
			Font.ITALIC, 9);
	public static Font DEFAULT_TOKEN_FONT = new Font("Verdana", Font.ITALIC, 20);
	public static Font UNBOUND_TOKEN_FONT = new Font("Verdana", Font.PLAIN , 20);
	public static Font DEFAULT_TABLE_FONT = new Font("Verdana", Font.PLAIN, 11);
	public static Font DEFAULT_TABLE_BOLDFONT = new Font("Verdana", Font.BOLD,
			11);
	public static Font DEFAULT_TOOLTIP_FONT = new Font("Verdana", Font.PLAIN,
			10);
	public static String DEFAULT_LANGUAGE = "";
	public static String DEFAULT_COUNTRY = "";
	public static String DEFAULT_VARIANT = "";
	public static int DEFAULT_APROMORE_CURRENTINDEX = 0;
	public static String DEFAULT_APROMORE_SERVERNAME = "";
	public static String DEFAULT_APROMORE_SERVERURL = "";
	public static int DEFAULT_APROMORE_SERVERPORT = 9000;
	public static String DEFAULT_APROMORE_MANAGERPATH = "manager/services/manager";
	public static String DEFAULT_APROMORE_USERNAME = "";
	public static boolean DEFAULT_APROMORE_ISSETPASSWORD = true;
	public static String DEFAULT_APROMORE_PASSWORD = "";
	public static String DEFAULT_APROMORE_PROXYNAME = "";
	public static int DEFAULT_APROMORE_PROXYPORT = 0;
	public static boolean DEFAULT_APROMORE_USEPROXY = false;
	public static boolean DEFAULT_APROMORE_USE = false;

	public static String DEFAULT_PROCESS2TEXT_HOST = "woped.dhbw-karlsruhe.de";
	public static int DEFAULT_PROCESS2TEXT_PORT = 8080;
	public static String DEFAULT_PROCESS2TEXT_URI = "/p2t";
	public static boolean DEFAULT_PROCESS2TEXT_USE = false;

	public static String DEFAULT_TEXT2PROCESS_HOST = "woped.dhbw-karlsruhe.de";
	public static int DEFAULT_TEXT2PROCESS_PORT = 8080;
	public static String DEFAULT_TEXT2PROCESS_URI = "/t2p";
	public static boolean DEFAULT_TEXT2PROCESS_USE = false;

	public static int DEFAULT_BUSINESSDASHBOARD_PORT = 2711;
	public static int DEFAULT_BUSINESSDASHBOARD_MAXVALUES = 1000;
	public static boolean DEFAULT_BUSINESSDASHBOARD_USEBYDEFAULT = false;
    // Booleans for alpha-functions (TEST) later integration in configuration &
    // GUI
    public static boolean ACTIVATE_NET_ROUTING = false;
    public static boolean ACTIVATE_ANNEALING_LAYOUT = false;
    // Language
    public Locale locale = Locale.getDefault();
    // File
	private String homedir = "";
	private String logdir = "";
	private String defaultHomedir = "";
	private String currentWorkingdir = "";
	private String userdir = "";
	// Editor
	private boolean insertCopy = false;
	private boolean importToolspec = true;
	private boolean exportToolspec = true;
	private boolean editoncreation = true;
	private boolean showgrid = true;
	private boolean roundRouting = false;
	private boolean smartediting = true;
	private int headsize = DEFAULT_ARROW_HEADSIZE;
	private int arrowwidth = DEFAULT_ARROW_WIDTH;
	private boolean fillArrow = false;
	private Color selectionColor = DEFAULT_SELECTION_COLOR;
	private Color portColor = DEFAULT_PORT_COLOR;
	// GUI
	private String lnf = null;
	private String language = DEFAULT_LANGUAGE;
	private String country = DEFAULT_COUNTRY;
	private String variant = DEFAULT_VARIANT;
	// SERVER
	private int apromore_currentIndex = DEFAULT_APROMORE_CURRENTINDEX;
	private String apromore_serverName = DEFAULT_APROMORE_SERVERNAME;
	private String apromore_serverURL = DEFAULT_APROMORE_SERVERURL;
	private int apromore_serverport = DEFAULT_APROMORE_SERVERPORT;
	private String apromore_managerPath = DEFAULT_APROMORE_MANAGERPATH;
	private String apromore_username = DEFAULT_APROMORE_USERNAME;
	private boolean apromore_isSetPassword = DEFAULT_APROMORE_ISSETPASSWORD;
	private String apromore_password = DEFAULT_APROMORE_PASSWORD;
	private String apromore_proxyname = DEFAULT_APROMORE_PROXYNAME;
	private int apromore_proxyport = DEFAULT_APROMORE_PROXYPORT;
	private boolean apromore_useproxy = DEFAULT_APROMORE_USEPROXY;
	private boolean apromore_use = DEFAULT_APROMORE_USE;

	// P2T
	private String process2text_serverHost = DEFAULT_PROCESS2TEXT_HOST;
	private int process2text_serverPort = DEFAULT_PROCESS2TEXT_PORT;
    private String process2text_serverUri = DEFAULT_PROCESS2TEXT_URI;
    private boolean process2text_use = DEFAULT_PROCESS2TEXT_USE;

	// T2P
	private String text2process_serverHost = DEFAULT_TEXT2PROCESS_HOST;
	private int text2process_serverPort = DEFAULT_TEXT2PROCESS_PORT;
    private String text2process_serverUri = DEFAULT_TEXT2PROCESS_URI;
    private boolean text2process_use = DEFAULT_TEXT2PROCESS_USE;

	//Dashboard
    private int businessdashboard_serverport = DEFAULT_BUSINESSDASHBOARD_PORT;
	private int businessdashboard_maxvalues = DEFAULT_BUSINESSDASHBOARD_MAXVALUES;
	private boolean businessdashboard_usebydefault = DEFAULT_BUSINESSDASHBOARD_USEBYDEFAULT;
	// Understandability Coloring
	private boolean colorOn = false;
	private int coloringAlgorithmMode = 0;
	private int color1 = -256;
	private int color2 = -16711936;
	private int color3 = -65536;
	private int color4 = -65281;
	private int color5 = -16776961;
	private int color6 = -10066330;
	private int color7 = -16724737;
	private int color8 = -39424;
	private int color9 = -13421569;
	private int color10 = -3355444;
	private int color11 = -10066432;
	private int color12 = -6750208;
	private int color13 = -10092442;
	private int color14 = -13312;
	private int color15 = -10053121;
	private int color16 = -16751104;
	private Color[] UnderstandColorArray = new Color[16];
	private int defaultcolor1 = -256;
	private int defaultcolor2 = -16711936;
	private int defaultcolor3 = -65536;
	private int defaultcolor4 = -65281;
	private int defaultcolor5 = -16776961;
	private int defaultcolor6 = -10066330;
	private int defaultcolor7 = -16724737;
	private int defaultcolor8 = -39424;
	private int defaultcolor9 = -13421569;
	private int defaultcolor10 = -3355444;
	private int defaultcolor11 = -10066432;
	private int defaultcolor12 = -6750208;
	private int defaultcolor13 = -10092442;
	private int defaultcolor14 = -13312;
	private int defaultcolor15 = -10053121;
	private int defaultcolor16 = -16751104;
	private Color[] defaultUnderstandColorArray = new Color[16];
	private boolean registered = false;
	private boolean showOnStartup;
	private String registrationEmail;
	private int launchCounter;

	public DefaultStaticConfiguration() {
		initConfig();
	}

	public boolean initConfig() {
		return true;
	}

	public boolean readConfig(File file) {
		return false;
	}

	public boolean saveConfig() {
		return true;
	}

	public boolean saveConfig(File file) {
		return false;
	}

	public void addRecentFile(String name, String path) {
		// NOT SUPPORTED
	}

	public int getArrowheadSize() {
		return headsize;
	}

    public void setArrowheadSize(int headSize) {
        this.headsize = headSize;
    }

	public int getArrowWidth() {
		return arrowwidth;
	}

    public void setArrowWidth(int width) {
        this.arrowwidth = width;
    }

	public Color getSelectionColor() {
		return selectionColor;
	}

	public void setSelectionColor(Color color) {
		selectionColor = color;
	}

	public String getHomedir() {
		return homedir;
	}

    public void setHomedir(String homedir) {
        this.homedir = homedir;
    }

	public String getDefaultHomedir() {
		return defaultHomedir;
	}

    public void setDefaultHomedir(String homedir) {
        this.defaultHomedir = homedir;
    }

	public String getCurrentWorkingdir() {
		return currentWorkingdir;
	}

    public void setCurrentWorkingdir(String homedir) {
        this.defaultHomedir = homedir;
    }

	public String getLogdir() {
		return logdir;
	}

    public void setLogdir(String logdir) {
        this.logdir = logdir;
    }

	// Start Understandability Coloring
	public boolean getColorOn() {
		return colorOn;
	}

	public void setColorOn(boolean b) {
		colorOn = b;
	}

	public int getColor1() {
		return color1;
	}

    public void setColor1(int rgb) {
        color1 = rgb;
    }

	public int getColor2() {
		return color2;
	}

    public void setColor2(int rgb) {
        color2 = rgb;
    }

	public int getColor3() {
		return color3;
	}

    public void setColor3(int rgb) {
        color3 = rgb;
    }

	public int getColor4() {
		return color4;
	}

    public void setColor4(int rgb) {
        color4 = rgb;
    }

	public int getColor5() {
		return color5;
	}

    public void setColor5(int rgb) {
        color5 = rgb;
    }

	public int getColor6() {
		return color6;
	}

    public void setColor6(int rgb) {
        color6 = rgb;
    }

	public int getColor7() {
		return color7;
	}

    public void setColor7(int rgb) {
        color7 = rgb;
    }

	public int getColor8() {
		return color8;
	}

    public void setColor8(int rgb) {
        color8 = rgb;
    }

	public int getColor9() {
		return color9;
	}

    public void setColor9(int rgb) {
        color9 = rgb;
    }

	public int getColor10() {
		return color10;
	}

    public void setColor10(int rgb) {
        color10 = rgb;
    }

	public int getColor11() {
		return color11;
	}

    public void setColor11(int rgb) {
        color11 = rgb;
    }

	public int getColor12() {
		return color12;
	}

    public void setColor12(int rgb) {
        color12 = rgb;
    }

	public int getColor13() {
		return color13;
	}

    public void setColor13(int rgb) {
        color13 = rgb;
    }

	public int getColor14() {
		return color14;
	}

    public void setColor14(int rgb) {
        color14 = rgb;
    }

	public int getColor15() {
		return color15;
	}

    public void setColor15(int rgb) {
        color15 = rgb;
    }

	public int getColor16() {
		return color16;
	}

    public void setColor16(int rgb) {
        color16 = rgb;
    }

	public Color[] getUnderstandColors() {
		return UnderstandColorArray;
	}

	public int getDefaultColor1() {
		return defaultcolor1;
	}

	public int getDefaultColor2() {
		return defaultcolor2;
	}

	public int getDefaultColor3() {
		return defaultcolor3;
	}

	public int getDefaultColor4() {
		return defaultcolor4;
	}

	public int getDefaultColor5() {
		return defaultcolor5;
	}

	public int getDefaultColor6() {
		return defaultcolor6;
	}

	public int getDefaultColor7() {
		return defaultcolor7;
	}

	public int getDefaultColor8() {
		return defaultcolor8;
	}

	public int getDefaultColor9() {
		return defaultcolor9;
	}

	public int getDefaultColor10() {
		return defaultcolor10;
	}

	public int getDefaultColor11() {
		return defaultcolor11;
	}

	public int getDefaultColor12() {
		return defaultcolor12;
	}

	public int getDefaultColor13() {
		return defaultcolor13;
	}

    // End Understandability Coloring

	public int getDefaultColor14() {
		return defaultcolor14;
	}

	public int getDefaultColor15() {
		return defaultcolor15;
	}

	public int getDefaultColor16() {
		return defaultcolor16;
	}

	public Color[] getDefaultUnderstandColors() {
		return defaultUnderstandColorArray;
	}

	public int getAlgorithmMode() {
		return coloringAlgorithmMode;
	}

	public void setAlgorithmMode(int n) {
		coloringAlgorithmMode = n;
	}

	public String getLookAndFeel() {
		return lnf;
	}

    public void setLookAndFeel(String className) {
        lnf = className;
    }

	public Vector<?> getRecentFiles() {
		return new Vector<Object>();
	}

	public Dimension getWindowSize() {
		return new Dimension(800, 600);
	}

    public void setWindowSize(Dimension windowSize) {
    }

	public boolean isMaximizeWindow() {
		return true;
	}

    public void setMaximizeWindow(boolean maximize) {
    }

	public int getWindowX() {
		return 0;
	}

    public void setWindowX(int windowX) {
    }

	public int getWindowY() {
		return 0;
	}

    public void setWindowY(int windowY) {
    }

	public String getWoflanPath() {
		return "";
	}

    public void setWoflanPath(String woflanPath) {
    }

	public boolean isEditingOnCreation() {
		return editoncreation;
	}

    public void setEditingOnCreation(boolean editingOnCreation) {
        this.editoncreation = editingOnCreation;
    }

	public boolean isExportToolspecific() {
		return exportToolspec;
	}

    public void setExportToolspecific(boolean exportToolspecific) {
        this.exportToolspec = exportToolspecific;
    }

	public boolean isFillArrowHead() {
		return fillArrow;
	}

    public void setFillArrowHead(boolean fill) {
        this.fillArrow = fill;
    }

	public boolean isHomedirSet() {
		return getHomedir() != null && !getHomedir().equals("");
	}

	public boolean isCurrentWorkingdirSet() {
		return getCurrentWorkingdir() != null
				&& !getCurrentWorkingdir().equals("");
	}

	public boolean isDefaultHomedirSet() {
		return getDefaultHomedir() != null && !getDefaultHomedir().equals("");
	}

	public boolean isImportToolspecific() {
		return importToolspec;
	}

    public void setImportToolspecific(boolean importToolspecific) {
        this.importToolspec = importToolspecific;
    }

	public boolean isInsertCOPYwhenCopied() {
		return insertCopy;
	}

    public void setInsertCOPYwhenCopied(boolean insertCOPYwhenCopied) {
        insertCopy = insertCOPYwhenCopied;
    }

	public boolean isRoundRouting() {
		return roundRouting;
	}

    public void setRoundRouting(boolean round) {
        this.roundRouting = round;
    }

	public boolean isShowGrid() {
		return showgrid;
	}

    public void setShowGrid(boolean showGrid) {
        this.showgrid = showGrid;
    }

	public boolean isSmartEditing() {
		return smartediting;
	}

    public void setSmartEditing(boolean smartEditing) {
        this.smartediting = smartEditing;
    }

	public boolean isTpnSaveElementAsName() {
		return false;
	}

    public void setTpnSaveElementAsName(boolean b) {
    }

    public boolean isUseWoflan() {
        return false;
	}

    public void setUseWoflan(boolean useWoflan) {
    }

    public boolean isUseWoflanDLL() {
        return false;
    }

    public void setUseWoflanDLL(boolean useWoflanDLL) {
    }

    public void removeAllRecentFiles() {
    }

    public void removeRecentFile(String name, String path) {
    }

	public String getUserdir() {
		return this.userdir;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#getPortColor()
	 */
	public Color getPortColor() {
		return portColor;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#setPortColor(java.awt.Color)
	 */
	public void setPortColor(Color color) {
		portColor = color;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#getLocaleLanguage()
	 */
	public String getLocaleLanguage() {
		return language;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#setLocaleLanguage(java.lang.String)
	 */
	public void setLocaleLanguage(String language) {
		this.language = language;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#getLocaleCountry()
	 */
	public String getLocaleCountry() {
		return country;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#setLocaleCountry(java.lang.String)
	 */
	public void setLocaleCountry(String country) {
		this.country = country;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#getLocaleVariant()
	 */
	public String getLocaleVariant() {
		return variant;
	}

	/**
	 * 
	 * @see org.woped.config.IGeneralConfiguration#setLocaleVariant(java.lang.String)
	 */
	public void setLocaleVariant(String variant) {
		this.variant = variant;
	}

	public void setLocale() {
		this.locale = Locale.getDefault();
		this.country = Locale.getDefault().getCountry();
		this.country = Locale.getDefault().getCountry();
		this.country = Locale.getDefault().getCountry();
	}

	public Locale getLocale() {
		return this.locale;
	}

	public boolean isUseMetrics() {
		return true;
	}

	public void setUseMetrics(boolean loadMetrics) {
	}

	public int getAlgorithmDecimalPlaces() {
		return 2;
	}

    public void setAlgorithmDecimalPlaces(int n) {
    }

	public int getVariableDecimalPlaces() {
		return 0;
	}

	public void setVariableDecimalPlaces(int n) {
	}

	public boolean isUseAlgorithmHighlighting() {
		return false;
	}

	public void setUseAlgorithmHighlighting(boolean useHighlighting) {
	}

	public boolean isShowNamesInBuilder() {
		return false;
	}

	public void setShowNamesInBuilder(boolean showNames) {
	}

	public boolean isShowAdvancedErrorMessages() {
		return false;
	}

	public void setShowAdvancedErrorMessages(boolean showAdvanced) {
	}

	@Override
	public int getCurrentApromoreIndex() {
		return apromore_currentIndex;
	}

	@Override
	public void setCurrentApromoreIndex(int index) {
		this.apromore_currentIndex = index;
	}
	
	@Override
	public boolean isSetApromorePassword() {
		return apromore_isSetPassword;
	}

	@Override
	public String getApromoreServerURL() {

		return apromore_serverURL;
	}

	@Override
	public void setApromoreServerURL(String url) {
		this.apromore_serverURL = url;

	}

	@Override
    public String getApromoreUsername() {

        return apromore_username;
    }

	@Override
    public void setApromoreUsername(String user) {
        this.apromore_username = user;

	}

	@Override
	public String getApromoreProxyName() {

		return apromore_proxyname;
	}

	@Override
	public void setApromoreProxyName(String name) {
		apromore_proxyname = name;

	}

	@Override
	public int getApromoreProxyPort() {

		return apromore_proxyport;
	}

	@Override
	public void setApromoreProxyPort(int port) {
		apromore_proxyport = port;

	}

	@Override
	public int getApromoreServerPort() {

		return apromore_serverport;
	}

	@Override
    public void setApromoreServerPort(int port) {
        apromore_serverport = port;
    }

    @Override
    public boolean getApromoreUseProxy() {

		return apromore_useproxy;
	}

	@Override
	public void setApromoreUseProxy(boolean set) {
		apromore_useproxy = set;

	}

	@Override
    public boolean getApromoreUse() {
        return apromore_use;
    }

	@Override
    public void setApromoreUse(boolean selected) {
        apromore_use = selected;
    }

	@Override
	public boolean isRegistered() {
		return registered;
	}

	@Override
	public void setRegistered(boolean selected) {
		registered = selected;
	}

	@Override
	public boolean isShowOnStartup() {
		return showOnStartup;
	}

	@Override
	public void setShowOnStartup(boolean selected) {
		showOnStartup = selected;
	}

	@Override
	public String getRegistrationEmail() {
		return registrationEmail;
	}

	@Override
	public void setRegistrationEmail(String address) {
		registrationEmail = address;
	}

	@Override
	public int getLaunchCounter() {
		return launchCounter;
	}

	@Override
	public void setLaunchCounter(int value) {
		launchCounter = value;
	}

	@Override
	public String getApromoreManagerPath() {
		return apromore_managerPath;
	}

	@Override
	public void setApromoreManagerPath(String path) {
		apromore_managerPath = path;

	}

	@Override
	public String getApromorePassword() {
		return apromore_password;
	}

	@Override
	public void setApromorePassword(String pwd) {
		apromore_password = pwd;

	}

	@Override
	public String getApromoreServerName() {
		return apromore_serverName;
	}

	@Override
	public void setApromoreServerName(String name) {
		apromore_serverName = name;

	}

	@Override
	public boolean isSetApromoreServers() {
		return false;
	}

	@Override
	public String[] getApromoreServerNames() {
		return null;
	}

	@Override
	public int getApromoreServerListLength() {
		return 0;
	}

	@Override
	public ApromoreServer[] getApromoreServers() {
		return null;
	}

	@Override
	public void addApromoreServer(int ID, String name, String url, int port,
			String path, String user, String pwd, boolean useProxy,
			String proxyUrl, int proxyPort) {

	}

	@Override
	public void changeApromoreServerSettings(int ID, String name, String url,
			int port, String path, String user, String pwd, boolean useProxy,
			String proxyUrl, int proxyPort) {

	}

	@Override
	public void removeApromoreServer(int index) {
	}

	// Process 2 Text
	@Override
	public String getProcess2TextServerHost() {
		return process2text_serverHost;
	}

	@Override
	public void setProcess2TextServerHost(String host) {
		this.process2text_serverHost = host;
	}

	@Override
	public int getProcess2TextServerPort() {
		return process2text_serverPort;
	}

	@Override
	public void setProcess2TextServerPort(int port) {
		this.process2text_serverPort = port;
	}

	@Override
	public String getProcess2TextServerURI() {
		return process2text_serverUri;
	}

	@Override
	public void setProcess2TextServerURI(String uri) {
		this.process2text_serverUri = uri;
	}

	@Override
	public boolean getProcess2TextUse() {
		return process2text_use;
	}

	@Override
	public void setProcess2TextUse(boolean selected) {
		this.process2text_use = selected;
	}

// Text 2 Process
	@Override
	public String getText2ProcessServerHost() {
		return text2process_serverHost;
	}

	@Override
	public void setText2ProcessServerHost(String host) {
		this.text2process_serverHost = host;
	}

	@Override
	public int getText2ProcessServerPort() {
		return text2process_serverPort;
	}

	@Override
	public void setText2ProcessServerPort(int port) {
		this.text2process_serverPort = port;
	}

	@Override
	public String getText2ProcessServerURI() {
		return text2process_serverUri;
	}

	@Override
	public void setText2ProcessServerURI(String uri) {
		this.process2text_serverUri = uri;
	}

	@Override
	public boolean getText2ProcessUse() {
		return text2process_use;
	}

	@Override
	public void setText2ProcessUse(boolean selected) {
		this.text2process_use = selected;
	}


	
	@Override
	public int getBusinessDashboardServerPort() {
		return this.businessdashboard_serverport;
	}

	@Override
	public void setBusinessDashboardServerPort(int port) {
		this.businessdashboard_serverport = port;
		
	}

	@Override
	public boolean getBusinessDashboardUseByDefault() {
		return this.businessdashboard_usebydefault;
	}

	@Override
	public void setBusinessDashboardUseByDefault(boolean bAutoStart) {
		this.businessdashboard_usebydefault = bAutoStart;	
	}

	@Override
	public int getBusinessDashboardMaxValues() {
		return this.businessdashboard_maxvalues;
	}

	@Override
	public void setBusinessDashboardMaxValues(int maxvalues) {
		this.businessdashboard_maxvalues = maxvalues;
		
	}



}
