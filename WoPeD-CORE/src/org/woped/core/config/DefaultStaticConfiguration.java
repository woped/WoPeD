package org.woped.core.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.Locale;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.woped.core.model.AbstractModelProcessor;

public class DefaultStaticConfiguration implements IConfiguration
{
    // Default values
    public static int             DEFAULT_ARROW_WIDTH        = 1;
    public static int             DEFAULT_ARROW_HEADSIZE     = 7;
    public static boolean         DEFAULT_ARROW_FILLHEAD     = false;
    public static final ImageIcon DEFAULTEDITORFRAMEICON     = null;
    public static Color           DEFAULT_SELECTION_COLOR    = Color.BLUE;
    public static Color           DEFAULT_INVERSE_COLOR    	 = Color.WHITE;
    public static Color           DEFAULT_PORT_COLOR         = Color.RED;
    public static Color           DEFAULT_UI_BACKGROUND_COLOR = Color.GRAY;
    public static Color           DEFAULT_CELL_BACKGROUND_COLOR = new Color(225,225,225);
    public static Color           DEFAULT_HEADER_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    public static Color           DEFAULT_TIME_COLOR 		 = new Color(0, 128, 0);
    public static Color		  	  DEFAULT_SUBPROCESS_FRAME_COLOR = new Color(255, 151, 5);
    public static Font            DEFAULT_LABEL_FONT         = new Font("Verdana", Font.PLAIN, 10);
    public static Font            DEFAULT_SMALLLABEL_FONT    = new Font("Verdana", Font.PLAIN, 9);
    public static Font            DEFAULT_TOKENGAME_FONT     = new Font("Verdana", Font.PLAIN, 9);
    public static Font            DEFAULT_RESOURCE_ROLE_FONT = new Font("Verdana", Font.PLAIN, 9);
    public static Font            DEFAULT_RESOURCE_ORG_FONT  = new Font("Verdana", Font.ITALIC, 9);
    public static Font            DEFAULT_TOKEN_FONT         = new Font("Verdana", Font.ITALIC, 19);
    public static Font            DEFAULT_TABLE_FONT         = new Font("Verdana", Font.PLAIN, 11);
    public static Font            DEFAULT_TABLE_BOLDFONT     = new Font("Verdana", Font.BOLD, 11);
    public static Font			  DEFAULT_TOOLTIP_FONT		 = new Font("Verdana", Font.PLAIN, 10);
    public static String          DEFAULT_LANGUAGE           = "en";
    public static String          DEFAULT_COUNTRY            = "";
    public static String          DEFAULT_VARIANT            = "";
    // File
    private String                homedir                    = "";
    private String                logdir                     = "";
    private String                defaultHomedir             = "";
    private String                currentWorkingdir          = "";
    // Editor
    private boolean               insertCopy                 = false;
    private boolean               importToolspec             = true;
    private boolean               exportToolspec             = true;
    private boolean               editoncreation             = true;
    private boolean               showgrid                   = true;
    private boolean               roundRouting               = false;
    private boolean               smartediting               = true;
    private int                   headsize                   = DEFAULT_ARROW_HEADSIZE;
    private int                   arrowwidth                 = DEFAULT_ARROW_WIDTH;
    private boolean               fillArrow                  = false;
    private int                   modelProcessor             = AbstractModelProcessor.MODEL_PROCESSOR_PETRINET;
    private Color                 selectionColor             = DEFAULT_SELECTION_COLOR;
    private Color                 portColor                  = DEFAULT_PORT_COLOR;
    // GUI
    private String                lnf                        = null;
    // Language
    public Locale                 locale                     = null;
    private String                language                   = DEFAULT_LANGUAGE;
    private String                country                    = DEFAULT_COUNTRY;
    private String                variant                    = DEFAULT_VARIANT;

    // Booleans for alpha-functions (TEST) later integration in conf&gui
    public static boolean         ACTIVATE_NET_ROUTING       = false;
    public static boolean         ACTIVATE_ANNEALING_LAYOUT  = false;

    // Understandability Coloring
    private boolean               colorOn                 	 = false;
    private int					  coloringAlgorithmMode		 = 0;	
    private int					  color1				     = -256;
    private int					  color2				     = -16711936;
    private int					  color3				     = -65536;
    private int					  color4				     = -65281;
    private int					  color5				     = -16776961;
    private int					  color6				     = -6710887;
    private int					  color7				     = -16724737;
    private int					  color8				     = -39424;
    private int					  color9				     = -13421569;
    private int					  color10				     = -16764109;
    private int					  color11				     = -13421824;
    private int					  color12				     = -10092544;
    private int					  color13				     = -6684775;
    private int					  color14				     = -13434829;
    private int					  color15				     = -13421773;
    private int					  color16				     = -16751104;
    private Color[]               UnderstandColorArray       = new Color[16];
    private int					  defaultcolor1				 = -256;
    private int					  defaultcolor2				 = -16711936;
    private int					  defaultcolor3				 = -65536;
    private int					  defaultcolor4				 = -65281;
    private int					  defaultcolor5				 = -16776961;
    private int					  defaultcolor6				 = -6710887;
    private int					  defaultcolor7				 = -16724737;
    private int					  defaultcolor8				 = -39424;
    private int					  defaultcolor9				 = -13421569;
    private int					  defaultcolor10			 = -16764109;
    private int					  defaultcolor11			 = -13421824;
    private int					  defaultcolor12			 = -10092544;
    private int					  defaultcolor13			 = -6684775;
    private int					  defaultcolor14			 = -13434829;
    private int					  defaultcolor15			 = -13421773;
    private int					  defaultcolor16			 = -16751104;
    private Color[]               defaultUnderstandColorArray= new Color[16];
    
    public void addRecentFile(String name, String path)
    {
    // 	// NOT SUPPORTED
    }

    public int getArrowheadSize()
    {
        return headsize;
    }

    public int getArrowWidth()
    {
        return arrowwidth;
    }

    public Color getSelectionColor()
    {
        return selectionColor;
    }

    public void setSelectionColor(Color color)
    {
        selectionColor = color;
    }

    public String getHomedir()
    {
        return homedir;
    }

    public String getDefaultHomedir()
    {
        return defaultHomedir;
    }

    public String getCurrentWorkingdir()
    {
        return currentWorkingdir;
    }

    public String getLogdir()
    {
        return logdir;
    }

    //Start Understandability Coloring
    public boolean getColorOn()
    {
        return colorOn;
    }

    public void setColorOn(boolean b)
    {
        colorOn = b;
    }
    
    public int getColor1()
    {
        return color1;
    }
    
    public int getColor2()
    {
        return color2;
    }
    
    public int getColor3()
    {
        return color3;
    }
    
    public int getColor4()
    {
        return color4;
    }
    
    public int getColor5()
    {
        return color5;
    }
    
    public int getColor6()
    {
        return color6;
    }
    
    public int getColor7()
    {
        return color7;
    }
    
    public int getColor8()
    {
        return color8;
    }
    
    public int getColor9()
    {
        return color9;
    }
    
    public int getColor10()
    {
        return color10;
    }
    
    public int getColor11()
    {
        return color11;
    }
    
    public int getColor12()
    {
        return color12;
    }
    
    public int getColor13()
    {
        return color13;
    }
    
    public int getColor14()
    {
        return color14;
    }
    
    public int getColor15()
    {
        return color15;
    }
    
    public int getColor16()
    {
        return color16;
    }
    
    public Color[] getUnderstandColors(){
    	return UnderstandColorArray;
    }
    
    public int getDefaultColor1()
    {
        return defaultcolor1;
    }
    
    public int getDefaultColor2()
    {
        return defaultcolor2;
    }
    
    public int getDefaultColor3()
    {
        return defaultcolor3;
    }
    
    public int getDefaultColor4()
    {
        return defaultcolor4;
    }
    
    public int getDefaultColor5()
    {
        return defaultcolor5;
    }
    
    public int getDefaultColor6()
    {
        return defaultcolor6;
    }
    
    public int getDefaultColor7()
    {
        return defaultcolor7;
    }
    
    public int getDefaultColor8()
    {
        return defaultcolor8;
    }
    
    public int getDefaultColor9()
    {
        return defaultcolor9;
    }
    
    public int getDefaultColor10()
    {
        return defaultcolor10;
    }
    
    public int getDefaultColor11()
    {
        return defaultcolor11;
    }
    
    public int getDefaultColor12()
    {
        return defaultcolor12;
    }
    
    public int getDefaultColor13()
    {
        return defaultcolor13;
    }
    
    public int getDefaultColor14()
    {
        return defaultcolor14;
    }
    
    public int getDefaultColor15()
    {
        return defaultcolor15;
    }
    
    public int getDefaultColor16()
    {
        return defaultcolor16;
    }
    
    public Color[] getDefaultUnderstandColors(){
    	return defaultUnderstandColorArray;
    }
    
    public void setColor1(int rgb)
    {
        color1 = rgb;
    }
    
    public void setColor2(int rgb)
    {
        color2 = rgb;
    }
    
    public void setColor3(int rgb)
    {
        color3 = rgb;
    }
    
    public void setColor4(int rgb)
    {
        color4 = rgb;
    }
    
    public void setColor5(int rgb)
    {
        color5 = rgb;
    }
    
    public void setColor6(int rgb)
    {
        color6 = rgb;
    }
    
    public void setColor7(int rgb)
    {
        color7 = rgb;
    }
    
    public void setColor8(int rgb)
    {
        color8 = rgb;
    }
    
    public void setColor9(int rgb)
    {
        color9 = rgb;
    }
    
    public void setColor10(int rgb)
    {
        color10 = rgb;
    }
    
    public void setColor11(int rgb)
    {
        color11 = rgb;
    }
    
    public void setColor12(int rgb)
    {
        color12 = rgb;
    }
    
    public void setColor13(int rgb)
    {
        color13 = rgb;
    }
    
    public void setColor14(int rgb)
    {
        color14 = rgb;
    }
    
    public void setColor15(int rgb)
    {
        color15 = rgb;
    }
    
    public void setColor16(int rgb)
    {
        color16 = rgb;
    }
    
	public int getAlgorithmMode() {
		return coloringAlgorithmMode;
	}
	
	public void setAlgorithmMode(int n) {
		coloringAlgorithmMode = n;
	}
    
    
    //End Understandability Coloring
    
    public String getLookAndFeel()
    {
        return lnf;
    }

    public Vector getRecentFiles()
    {
        return new Vector();
    }

    public Dimension getWindowSize()
    {
        return new Dimension(800, 600);
    }

    public int getWindowX()
    {
        return 0;
    }

    public int getWindowY()
    {
        return 0;
    }

    public String getWoflanPath()
    {
        return "";
    }

    public boolean isEditingOnCreation()
    {
        return editoncreation;
    }

    public boolean isExportToolspecific()
    {
        return exportToolspec;
    }

    public boolean isFillArrowHead()
    {
        return fillArrow;
    }

    public boolean isHomedirSet()
    {
        return getHomedir() != null && !getHomedir().equals("");
    }

    public boolean isCurrentWorkingdirSet()
    {
        return getCurrentWorkingdir() != null && !getCurrentWorkingdir().equals("");
    }

    public boolean isDefaultHomedirSet()
    {
        return getDefaultHomedir() != null && !getDefaultHomedir().equals("");
    }

    public boolean isImportToolspecific()
    {
        return importToolspec;
    }

    public boolean isInsertCOPYwhenCopied()
    {
        return insertCopy;
    }

    public boolean isRoundRouting()
    {
        return roundRouting;
    }

    public boolean isShowGrid()
    {
        return showgrid;
    }

    public boolean isSmartEditing()
    {
        return smartediting;
    }

    public boolean isTpnSaveElementAsName()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isUseWoflan()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeAllRecentFiles()
    {
    // TODO Auto-generated method stub

    }

    public void removeRecentFile(String name, String path)
    {
    // TODO Auto-generated method stub

    }

    public void setArrowheadSize(int headSize)
    {
        this.headsize = headSize;
    }

    public void setArrowWidth(int width)
    {
        this.arrowwidth = width;
    }

    public void setEditingOnCreation(boolean editingOnCreation)
    {
        this.editoncreation = editingOnCreation;
    }

    public void setExportToolspecific(boolean exportToolspecific)
    {
        this.exportToolspec = exportToolspecific;

    }

    public void setFillArrowHead(boolean fill)
    {
        this.fillArrow = fill;
    }

    public void setHomedir(String homedir)
    {
        this.homedir = homedir;
    }

    public void setDefaultHomedir(String homedir)
    {
    	this.defaultHomedir = homedir;
    }

    public void setCurrentWorkingdir(String homedir)
    {
    	this.defaultHomedir = homedir;
    }

    public void setLogdir(String logdir)
    {
        this.logdir = logdir;
    }

    public void setImportToolspecific(boolean importToolspecific)
    {
        this.importToolspec = importToolspecific;
    }

    public void setInsertCOPYwhenCopied(boolean insertCOPYwhenCopied)
    {
        insertCopy = insertCOPYwhenCopied;
    }

    public void setLookAndFeel(String className)
    {
        lnf = className;

    }

    public void setRoundRouting(boolean round)
    {
        this.roundRouting = round;
    }

    public void setShowGrid(boolean showGrid)
    {
        this.showgrid = showGrid;
    }

    public void setSmartEditing(boolean smartEditing)
    {
        this.smartediting = smartEditing;
    }

    public void setTpnSaveElementAsName(boolean b)
    {
    // TODO Auto-generated method stub

    }

    public void setUseWoflan(boolean useWoflan)
    {
    // TODO Auto-generated method stub

    }

    public void setWindowSize(Dimension windowSize)
    {
    // TODO Auto-generated method stub

    }

    public void setWindowX(int windowX)
    {
    // TODO Auto-generated method stub

    }

    public void setWindowY(int windowY)
    {
    // TODO Auto-generated method stub

    }

    public void setWoflanPath(String woflanPath)
    {
    // TODO Auto-generated method stub

    }

    public DefaultStaticConfiguration()
    {
        initConfig();
    }

    public boolean initConfig()
    {
        return true;
    }

    public boolean readConfig(File file)
    {
        return false;
    }

    public boolean saveConfig()
    {
        return true;
    }

    public boolean saveConfig(File file)
    {
        return false;
    }

    public int getModelProcessorType()
    {
        return modelProcessor;
    }

    public void setModelProcessorType(int type)
    {
        modelProcessor = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#getPortColor()
     */
    public Color getPortColor()
    {
        return portColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#setPortColor(java.awt.Color)
     */
    public void setPortColor(Color color)
    {
        portColor = color;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#getLocaleLanguage()
     */
    public String getLocaleLanguage()
    {
        return language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#setLocaleLanguage(java.lang.String)
     */
    public void setLocaleLanguage(String language)
    {
        this.language = language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#getLocaleCountry()
     */
    public String getLocaleCountry()
    {
        return country;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#setLocaleCountry(java.lang.String)
     */
    public void setLocaleCountry(String country)
    {
        this.country = country;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#getLocaleVariant()
     */
    public String getLocaleVariant()
    {
        return variant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.woped.config.IConfiguration#setLocaleVariant(java.lang.String)
     */
    public void setLocaleVariant(String variant)
    {
        this.variant = variant;
    }

    public void setLocale()
    {
        String language = null;
        String country = null;
        String variant = null;

        Locale userLocale = null;

        if (getLocaleLanguage() != null)
        {
            language = getLocaleLanguage();
        }
        if (getLocaleCountry() != null)
        {
            country = getLocaleCountry();
        }
        if (getLocaleVariant() != null)
        {
            variant = getLocaleVariant();
        }

        if (language != null && country != null && variant != null)
        {
            userLocale = new Locale(language, country, variant);
        } else if (language != null && country != null)
        {
            userLocale = new Locale(language, country);
        } else
        {
            userLocale = new Locale(language);
        }
        if (userLocale == null)
        {
            userLocale = Locale.ENGLISH;
            setLocaleLanguage(this.locale.getLanguage());
        }

        this.locale = userLocale;
    }

    public Locale getLocale()
    {
        return this.locale;
    }
}