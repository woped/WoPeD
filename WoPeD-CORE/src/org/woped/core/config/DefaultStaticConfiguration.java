package org.woped.core.config;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.woped.core.model.AbstractModelProcessor;

public class DefaultStaticConfiguration implements IConfiguration
{
    public static int             DEFAULT_ARROW_WIDTH       = 1;
    public static int             DEFAULT_ARROW_HEADSIZE    = 7;
    public static boolean         DEFAULT_ARROW_FILLHEAD    = false;
    public static final ImageIcon DEFAULTEDITORFRAMEICON    = null;

    private boolean               insertCopy                = false;
    private boolean               importToolspec            = true;
    private boolean               exportToolspec            = true;
    private boolean               editoncreation            = true;
    private boolean               showgrid                  = true;
    private boolean               roundRouting              = false;
    private boolean               smartediting              = true;
    private int                   headsize                  = DEFAULT_ARROW_HEADSIZE;
    private int                   arrowwidth                = DEFAULT_ARROW_WIDTH;
    private boolean               fillArrow                 = false;
    private int                   modelProcessor            = AbstractModelProcessor.MODEL_PROCESSOR_PETRINET;
    private Color                 selectionColor            = DEFAULT_SELECTION_COLOR;
    private Color                 portColor                 = DEFAULT_PORT_COLOR;

    // Booleans for alpha-functions (TEST) later integration in conf&gui
    public static boolean         ACTIVATE_TREE_VIEW        = false;
    public static boolean         ACTIVATE_NET_ROUTING      = false;
    public static boolean         ACTIVATE_ANNEALING_LAYOUT = false;

    public static Color           DEFAULT_SELECTION_COLOR   = new Color(0, 103, 178);
    public static Color           DEFAULT_PORT_COLOR        = Color.RED;

    private String                homedir                   = "";
    private String                workingDir                = "";

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
        return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
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
        return getHomedir() != null && getHomedir() != "";
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
    // TODO Auto-generated method stub

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
}
