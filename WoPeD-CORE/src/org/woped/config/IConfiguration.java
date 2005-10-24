package org.woped.config;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.Vector;

public interface IConfiguration
{
    //
    //
    public boolean initConfig();

    public boolean readConfig(File file);

    public boolean saveConfig(File file);

    public boolean saveConfig();

    //    
    public boolean isEditingOnCreation();

    //
    public int getModelProcessorType();
    
    //
    public void setModelProcessorType(int type);
    
    //    
    public void setEditingOnCreation(boolean editingOnCreation);

    //    
    public boolean isInsertCOPYwhenCopied();

    //    
    public void setInsertCOPYwhenCopied(boolean insertCOPYwhenCopied);

    //    
    public boolean isExportToolspecific();

    //    
    public boolean isImportToolspecific();

    //    
    public void setExportToolspecific(boolean exportToolspecific);

    //
    public void setImportToolspecific(boolean importToolspecific);

    //    
    public Vector getRecentFiles();

    //    
    public void addRecentFile(String name, String path);

    //
    public void removeRecentFile(String name, String path);

    //
    public void removeAllRecentFiles();

    //
    public Dimension getWindowSize();

    //    
    public int getWindowX();

    //
    public int getWindowY();

    //
    public void setWindowSize(Dimension windowSize);

    //
    public void setWindowX(int windowX);

    //    
    public void setWindowY(int windowY);

    //
    public String getHomedir();

    //
    public boolean isHomedirSet();

    //
    public void setHomedir(String homedir);

    //    
    public boolean isUseWoflan();

    //
    public String getWoflanPath();

    //    
    public void setUseWoflan(boolean useWoflan);

    //    
    public void setWoflanPath(String woflanPath);

    //
    public boolean isSmartEditing();

    //
    public void setSmartEditing(boolean smartEditing);

    //
    public boolean isTpnSaveElementAsName();

    //
    public void setTpnSaveElementAsName(boolean b);

    //    
    public void setLookAndFeel(String className);

    //
    public String getLookAndFeel();

    public void setShowGrid(boolean showGrid);

    public boolean isShowGrid();

    public void setArrowWidth(int width);

    public int getArrowWidth();

    public void setArrowheadSize(int headSize);

    public int getArrowheadSize();

    public boolean isFillArrowHead();

    public void setFillArrowHead(boolean fill);

    public boolean isRoundRouting();

    public void setRoundRouting(boolean round);

    public String getCurrentWorkingDir();

    public void setCurrentWorkingDir(String dir);
    
    public Color getSelectionColor();
    
    public void setSelectionColor(Color color);
    
    public Color getPortColor();
    
    public void setPortColor(Color color);
    
}
