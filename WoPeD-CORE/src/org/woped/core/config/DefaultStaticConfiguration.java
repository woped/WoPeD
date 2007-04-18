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
    public static String          DEFAULT_LANGUAGE           = "en";
    public static String          DEFAULT_COUNTRY            = "";
    public static String          DEFAULT_VARIANT            = "";
    // File
    private String                homedir                    = "";
    private String                workingDir                 = "";
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

    public String getCurrentWorkingDir()
    {
        return workingDir;
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

    public void setCurrentWorkingDir(String dir)
    {
        this.workingDir = dir;
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