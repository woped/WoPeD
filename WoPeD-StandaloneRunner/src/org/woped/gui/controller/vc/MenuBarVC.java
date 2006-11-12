/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.gui.controller.vc;

/*
 * Created on 31.07.2004
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.woped.config.WoPeDRecentFile;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.core.gui.IEditorAware;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.utilities.Messages;
import org.woped.gui.Constants;
import org.woped.gui.controller.DefaultApplicationMediator;
import org.woped.gui.controller.ViewEvent;

/**
 * Represents the Manubar TODO: DOCUMENTATION (xraven)
 * 
 * @author Thomas Pohl
 */
public class MenuBarVC extends JMenuBar implements IViewController, IEditorAware
{
    // ViewControll
    private Vector             viewListener             = new Vector(1, 3);
    private String             id                       = null;
    public static final String ID_PREFIX                = "MENUBAR_VC_";

    private Hashtable          m_editorMenuItems        = new Hashtable();
    private JMenuItem          m_windowEmptyItem        = null;

    // private UserInterface m_containingWindow;

    private JMenu              m_fileMenu               = null;
    private JMenuItem          m_newMenuItem            = null;
    private JMenuItem          m_openMenuItem           = null;
    private JMenuItem          m_closeMenuItem          = null;
    private JMenuItem          m_saveMenuItem           = null;
    private JMenuItem          m_saveAsMenuItem         = null;
    private JMenuItem          m_printMenuItem          = null;
    private JMenuItem          m_exportMenuItem         = null;
    private JMenu              m_recentMenu             = null;
    private JMenuItem          m_quitMenuItem           = null;

    private JMenu              m_editMenu               = null;
    private JMenuItem          m_undoMenuItem           = null;
    private JMenuItem          m_redoMenuItem           = null;
    private JMenuItem          m_cutMenuItem            = null;
    private JMenuItem          m_copyMenuItem           = null;
    private JMenuItem          m_pasteMenuItem          = null;
    private JMenuItem          m_screenshotMenuItem     = null;
    private JMenuItem          m_editMenuItem           = null;

    private JMenu              m_viewMenu               = null;
    private JMenuItem          m_stretchMenuItem        = null;
    private JMenuItem          m_pressMenuItem          = null;
    private JMenuItem          m_groupMenuItem          = null;
    private JMenuItem          m_ungroupMenuItem        = null;
    private JMenuItem          m_zoomInMenuItem         = null;
    private JMenuItem          m_zoomOutMenuItem        = null;

    private JMenu              m_analyseMenu            = null;
    private JMenuItem          m_stateSpaceMenuItem     = null;
    private JMenuItem          m_woflanMenuItem         = null;
    private JMenuItem		   m_wopedMenuItem			= null;
    private JMenu              m_optionsMenu            = null;
    private JMenuItem          m_optionsMenuItem        = null;

    private JMenu              m_windowMenu             = null;
    private JMenuItem          m_cascadeWindowsMenuItem = null;
    private JMenuItem          m_arrangeWindowsMenuItem = null;
    private JMenu              m_framesMenu             = null;

    private JMenu              m_helpMenu               = null;
    private JMenuItem          m_indexMenuItem          = null;
    private JMenuItem          m_contentsMenuItem       = null;
    private JMenu              m_sampleMenu             = null;
    private JMenuItem          m_aboutMenuItem          = null;
    private JMenuItem          m_bugReportMenuItem      = null;

    /**
     * Creates a new MenuBar for a PWTUI.
     * 
     * @param containingWindow
     *            Specifies the Window which contains this MenuBar.
     */
    public MenuBarVC(String id)
    {
        this.id = id;
        // m_containingWindow = containingWindow;
        add(getFileMenu());
        add(getEditMenu());
        add(getViewMenu());
        add(getAnalyseMenu());
        add(getOptionsMenu());
        add(getWindowMenu());
        // containingWindow.getSdp().registerMenuBar(this);
        add(Box.createHorizontalGlue());
        add(getHelpMenu());

    }

    /**
     * Creates the analyseMenu if necessary.
     * 
     * @return Returns the analyseMenu.
     */
    public JMenu getAnalyseMenu()
    {
        if (m_analyseMenu == null)
        {
            m_analyseMenu = new JMenu(Messages.getString("Menu.Analyse.Title")); //$NON-NLS-1$
            m_analyseMenu.setMnemonic(KeyEvent.VK_A);

            // m_analyseMenu.add(getStateSpaceMenuItem());
            m_analyseMenu.add(getWoflanMenuItem());
            getWoflanMenuItem().setEnabled(ConfigurationManager.getConfiguration().isUseWoflan());
            m_analyseMenu.add(getWopedMenuItem());
        }
        return m_analyseMenu;
    }

    // /**
    // * @return Returns the containingWindow.
    // */
    // public UserInterface getContainingWindow()
    // {
    // return m_containingWindow;
    // }

    /**
     * Creates the editMenu if necessary.
     * 
     * @return Returns the editMenu.
     */
    public JMenu getEditMenu()
    {
        if (m_editMenu == null)
        {
            m_editMenu = new JMenu(Messages.getString("Menu.Edit.Title")); //$NON-NLS-1$
            m_editMenu.setMnemonic(KeyEvent.VK_E);

            m_editMenu.add(getUndoMenuItem());
            m_editMenu.add(getRedoMenuItem());
            
            m_editMenu.addSeparator();
            m_editMenu.add(getCutMenuItem());
            m_editMenu.add(getCopyMenuItem());
            m_editMenu.add(getPasteMenuItem());
            //            m_editMenu.addSeparator();
            // deactivated because we have jpeg export
            // m_editMenu.add(getScreenshotMenuItem());
            //            m_editMenu.add(getEditMenuItem());
        }
        return m_editMenu;
    }

    /**
     * Creates the fileMenu if necessary.
     * 
     * @return Returns the fileMenu.
     */
    public JMenu getFileMenu()
    {
        if (m_fileMenu == null)
        {
            m_fileMenu = new JMenu(Messages.getString("Menu.File.Title")); //$NON-NLS-1$
            m_fileMenu.setMnemonic(KeyEvent.VK_F);

            m_fileMenu.add(getNewMenuItem());
            m_fileMenu.add(getOpenMenuItem());
            m_fileMenu.add(getCloseMenuItem());
            m_fileMenu.add(getSaveMenuItem());
            m_fileMenu.add(getSaveAsMenuItem());
            m_fileMenu.addSeparator();
            m_fileMenu.add(getPrintMenuItem());
            m_fileMenu.add(getExportMenuItem());
            m_fileMenu.addSeparator();
            m_fileMenu.add(getRecentMenu());
            m_fileMenu.addSeparator();
            m_fileMenu.add(getQuitMenuItem());

        }
        return m_fileMenu;
    }

    /**
     * Creates the helpMenu if necessary.
     * 
     * @return Returns the helpMenu.
     */
    public JMenu getHelpMenu()
    {
        if (m_helpMenu == null)
        {
            m_helpMenu = new JMenu(Messages.getString("Menu.Help.Title")); //$NON-NLS-1$
            m_helpMenu.setMnemonic(KeyEvent.VK_H);
            
            m_helpMenu.add(getIndexMenuItem());
            m_helpMenu.add(getContextMenuItem());
            m_helpMenu.addSeparator();
            m_helpMenu.add(getSampleMenu());
            m_helpMenu.addSeparator();
            m_helpMenu.add(getAboutMenuItem());
            m_helpMenu.add(getBugReportMenuItem());
        }
        return m_helpMenu;
    }

    /**
     * Creates the optionsMenu if necessary.
     * 
     * @return Returns the optionsMenu.
     */
    public JMenu getOptionsMenu()
    {
        if (m_optionsMenu == null)
        {
            m_optionsMenu = new JMenu(Messages.getString("Menu.Options.Title")); //$NON-NLS-1$
            m_optionsMenu.setMnemonic(KeyEvent.VK_O);
            m_optionsMenu.add(getOptionsMenuItem());
        }
        return m_optionsMenu;
    }

    /**
     * Creates the viewMenu if necessary.
     * 
     * @return Returns the viewMenu.
     */
    public JMenu getViewMenu()
    {
        if (m_viewMenu == null)
        {
            m_viewMenu = new JMenu(Messages.getString("Menu.View.Title")); //$NON-NLS-1$
            m_viewMenu.setMnemonic(KeyEvent.VK_V);

            // m_viewMenu.add(getStretchMenuItem());
            // m_viewMenu.add(getPressMenuItem());
            // m_viewMenu.addSeparator();
            m_viewMenu.add(getGroupMenuItem());
            m_viewMenu.add(getUngroupMenuItem());
            m_viewMenu.addSeparator();
            m_viewMenu.add(getZoomInMenuItem());
            m_viewMenu.add(getZoomOutMenuItem());
        }
        return m_viewMenu;
    }

    /**
     * @return Returns the newMenuItem.
     */
    public JMenuItem getNewMenuItem()
    {
        if (m_newMenuItem == null)
        {
            /* create constants via reflection */
            m_newMenuItem = // new WoPeDMenuItem(am, "Menu.File.New",
            // WoPeDActionListener.NEW_EDITOR);
            new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_NEW));

        }
        return m_newMenuItem;
    }

    /**
     * @return Returns the closeMenuItem.
     */
    public JMenuItem getCloseMenuItem()
    {
        if (m_closeMenuItem == null)
        {
            m_closeMenuItem = // new WoPeDMenuItem(("Menu.File.Close"),
            // WoPeDActionListener.CLOSE_EDITOR,
            // VisualController.WITH_EDITOR);
            new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_CLOSE));
        }
        return m_closeMenuItem;
    }

    /**
     * @return Returns the openMenuItem.
     */
    public JMenuItem getOpenMenuItem()
    {
        if (m_openMenuItem == null)
        {
            m_openMenuItem = // new WoPeDMenuItem("Menu.File.Open",
            // WoPeDActionListener.OPEN_EDITOR);
            new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_OPEN));
        }
        return m_openMenuItem;
    }

    /**
     * @return Returns the saveMenuItem.
     */
    public JMenuItem getSaveMenuItem()
    {
        if (m_saveMenuItem == null)
        {
            m_saveMenuItem = // new WoPeDMenuItem("Menu.File.Save",
            // WoPeDActionListener.SAVE,
            // VisualController.WITH_EDITOR);
            new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SAVE));
        }
        return m_saveMenuItem;
    }

    /**
     * @return Returns the quitMenuItem.
     */
    public JMenuItem getQuitMenuItem()
    {
        if (m_quitMenuItem == null)
        {
            m_quitMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_EXIT));
        }
        return m_quitMenuItem;
    }

    /**
     * @return Returns the printMenuItem.
     */
    public JMenuItem getPrintMenuItem()
    {
        if (m_printMenuItem == null)
        {
            m_printMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_PRINT));
        }
        return m_printMenuItem;
    }

    /**
     * @return Returns the exportMenuItem.
     */
    public JMenuItem getExportMenuItem()
    {
        if (m_exportMenuItem == null)
        {
            m_exportMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_EXPORT));
            // new ExportAction(m_containingWindow);
        }
        return m_exportMenuItem;
    }

    public void updateRecentMenu()
    {
        Vector v = ConfigurationManager.getConfiguration().getRecentFiles();
        getRecentMenu().removeAll();
        if (v.size() != 0)
        {
            for (int idx = 0; idx < v.size(); idx++)
            {
                String name = ((WoPeDRecentFile) v.get(idx)).getName();
                String path = ((WoPeDRecentFile) v.get(idx)).getPath();

                JMenuItem recentMenuItem = new JMenuItem(name);
                recentMenuItem.setActionCommand(path);
                recentMenuItem.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        fireViewEvent(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN, new File(e.getActionCommand())));
                    }

                });
                m_recentMenu.add(recentMenuItem);
            }
        } else
        {
            JMenuItem emptyItem = new JMenuItem(Messages.getString("Menu.File.RecentMenu.empty"));
            emptyItem.setEnabled(false);
            m_recentMenu.add(emptyItem);
        }
    }

    /**
     * @return Returns the recentMenu.
     */
    public JMenu getRecentMenu()
    {
        if (m_recentMenu == null)
        {
            m_recentMenu = new JMenu(Messages.getString("Menu.File.RecentMenu.Title"));
            m_recentMenu.setMnemonic(KeyEvent.VK_R);
            updateRecentMenu();
        }
        return m_recentMenu;
    }

    /**
     * @return Returns the saveAsMenuItem.
     */
    public JMenuItem getSaveAsMenuItem()
    {
        if (m_saveAsMenuItem == null)
        {
            m_saveAsMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SAVEAS));
        }
        return m_saveAsMenuItem;
    }

    /**
     * @return Returns the copyMenuItem.
     */
    public JMenuItem getCopyMenuItem()
    {
        if (m_copyMenuItem == null)
        {
            m_copyMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_COPY));
        }
        return m_copyMenuItem;
    }

    /**
     * @return Returns the cutMenuItem.
     */
    public JMenuItem getCutMenuItem()
    {
        if (m_cutMenuItem == null)
        {
            m_cutMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_CUT));
        }
        return m_cutMenuItem;
    }

    /**
     * @return Returns the pasteMenuItem.
     */
    public JMenuItem getPasteMenuItem()
    {
        if (m_pasteMenuItem == null)
        {
            m_pasteMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_PASTE));
        }
        return m_pasteMenuItem;
    }

    /**
     * @return Returns the redoMenuItem.
     */
    public JMenuItem getRedoMenuItem()
    {
        if (m_redoMenuItem == null)
        {
            m_redoMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REDO));
        }
        return m_redoMenuItem;
    }

    /**
     * @return Returns the undoMenuItem.
     */
    public JMenuItem getUndoMenuItem()
    {
        if (m_undoMenuItem == null)
        {
            m_undoMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_UNDO));
        }
        return m_undoMenuItem;
    }

    /**
     * @return Returns the optionsMenuItem.
     */
    public JMenuItem getOptionsMenuItem()
    {
        if (m_optionsMenuItem == null)
        {
            m_optionsMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SHOWCONFIG));
        }
        return m_optionsMenuItem;
    }

    /**
     * @return Returns the indexMenuItem.
     */
    public JMenuItem getIndexMenuItem()
    {
        if (m_indexMenuItem == null)
        {
            m_indexMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SHOWHELPINDEX));
        }
        return m_indexMenuItem;
    }

    public JMenuItem getContextMenuItem()
    {
        if (m_contentsMenuItem == null)
        {
            m_contentsMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SHOWHELPCONTENTS));
        }
        return m_contentsMenuItem;
    }

    /**
     * @return Returns the groupMenuItem.
     */
    public JMenuItem getGroupMenuItem()
    {
        if (m_groupMenuItem == null)
        {
            m_groupMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_GROUP));
        }
        return m_groupMenuItem;
    }

    /**
     * @return Returns the pressMenuItem.
     */
    public JMenuItem getPressMenuItem()
    {
        if (m_pressMenuItem == null)
        {
            m_pressMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_PRESS));
        }
        return m_pressMenuItem;
    }

    /**
     * @return Returns the stretchMenuItem.
     */
    public JMenuItem getStretchMenuItem()
    {
        if (m_stretchMenuItem == null)
        {
            m_stretchMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_STRETCH));
        }
        return m_stretchMenuItem;
    }

    /**
     * @return Returns the ungroupMenuItem.
     */
    public JMenuItem getUngroupMenuItem()
    {
        if (m_ungroupMenuItem == null)
        {
            m_ungroupMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_UNGROUP));
        }
        return m_ungroupMenuItem;
    }

    /**
     * @return Returns the zoomInMenuItem.
     */
    public JMenuItem getZoomInMenuItem()
    {
        if (m_zoomInMenuItem == null)
        {
            m_zoomInMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ZOOMIN));
        }
        return m_zoomInMenuItem;
    }

    /**
     * @return Returns the zoomOutMenuItem.
     */
    public JMenuItem getZoomOutMenuItem()
    {
        if (m_zoomOutMenuItem == null)
        {
            m_zoomOutMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ZOOMOUT));
        }
        return m_zoomOutMenuItem;
    }

    /**
     * @return Returns the woflanMenuItem.
     */
    public JMenuItem getWoflanMenuItem()
    {
        if (m_woflanMenuItem == null)
        {
            m_woflanMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_WOFLAN));
        }
        return m_woflanMenuItem;
    }
    
    /**
     * @return Returns the wopedMenuItem.
     */
    public JMenuItem getWopedMenuItem()
    {
    	if (m_wopedMenuItem == null)
    	{
    		m_wopedMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_WOPED));    		
    	}
    	return m_wopedMenuItem;
    }

    /**
     * @return Returns the sampleMenu.
     */
    public JMenu getSampleMenu()
    {
        if (m_sampleMenu == null)
        {

            m_sampleMenu = new JMenu(Messages.getString("Menu.Help.SampleNets.Title"));
            m_sampleMenu.setMnemonic(KeyEvent.VK_S);

            try
            {
                String innerPath = "org/woped/file/samples/";
                String path = this.getClass().getResource("/" + innerPath).toExternalForm();
                // Jar file access
                if (path.indexOf("jar:file:") != -1)
                {
                    String fn = path.replaceAll("jar:file:", "");
                    // find jar start
                    int n = fn.indexOf("!");
                    if (n == -1) n = fn.length();
                    // read Jar File Name
                    fn = fn.substring(0, n);
                    // Replace all whitespaces in filename
                    fn = fn.replaceAll("%20", " ");
                    JarFile jf = new JarFile(fn);
                    Enumeration e = jf.entries();
                    ZipEntry ze;
                    // process entries
                    while (e.hasMoreElements())
                    {
                        ze = (ZipEntry) e.nextElement();
                        String name;
                        String samplepath;
                        if (ze.getName().indexOf(innerPath) == 0 && ze.getName().length() > innerPath.length())
                        {
                            samplepath = "/" + ze.getName();
                            name = ze.getName().substring(ze.getName().lastIndexOf("/") + 1);
                            JMenuItem sampleItem = new JMenuItem(name);
                            sampleItem.setActionCommand(samplepath);
                            sampleItem.addActionListener(new ActionListener()
                            {

                                public void actionPerformed(ActionEvent e)
                                {
                                    fireViewEvent(new ViewEvent(MenuBarVC.this, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN_SAMPLE, new File(e.getActionCommand())));
                                }

                            });
                            m_sampleMenu.add(sampleItem);
                        }
                    }
                }
                // Normal dir access
                else
                {
                    path = "../WoPeD-FileInterface/bin/" + innerPath;
                    File sampleDir = new File(path);
                    if (sampleDir.isDirectory())
                    {
                        for (int idx = 0; idx < sampleDir.listFiles().length; idx++)
                        {
                            if (!sampleDir.listFiles()[idx].isDirectory())
                            {
                                JMenuItem sampleItem = new JMenuItem(sampleDir.listFiles()[idx].getName());
                                sampleItem.setActionCommand(sampleDir.listFiles()[idx].getAbsolutePath());
                                sampleItem.addActionListener(new ActionListener()
                                {

                                    public void actionPerformed(ActionEvent e)
                                    {
                                        fireViewEvent(new ViewEvent(MenuBarVC.this, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN_SAMPLE, new File(e.getActionCommand())));
                                    }

                                });

                                m_sampleMenu.add(sampleItem);
                            }
                        }
                    } else
                    {
                        LoggerManager.error(Constants.GUI_LOGGER, "No sample nets found in directory " + path);
                    }
                }
            } catch (Exception ex)
            {
                LoggerManager.error(Constants.GUI_LOGGER, "Cannot find sample files");
                ex.printStackTrace();
            }
        }
        return m_sampleMenu;
    }

    /**
     * @return Returns the aboutMenuItem.
     */
    public JMenuItem getAboutMenuItem()
    {
        if (m_aboutMenuItem == null)
        {
            m_aboutMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SHOWABOUT));
        }
        return m_aboutMenuItem;
    }

    /**
     * @return Returns the bugReportMenuItem.
     */
    public JMenuItem getBugReportMenuItem()
    {
        if (m_bugReportMenuItem == null)
        {
            m_bugReportMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SHOWBUGREPORT));
        }
        return m_bugReportMenuItem;
    }

    /**
     * @return Returns the screenshotMenuItem.
     */
    /*
     * public JMenuItem getScreenshotMenuItem() { if (m_screenshotMenuItem ==
     * null) { m_screenshotMenuItem = new
     * JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_SCREENSHOT)); }
     * return m_screenshotMenuItem; }
     */

    public JMenuItem getCascadeWindowsMenuItem()
    {
        if (m_cascadeWindowsMenuItem == null)
        {
            m_cascadeWindowsMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_CASCADE));
        }
        return m_cascadeWindowsMenuItem;
    }

    /**
     * 
     * @return
     */
    public JMenuItem getArrangeWindowsMenuItem()
    {
        if (m_arrangeWindowsMenuItem == null)
        {
            m_arrangeWindowsMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ARRANGE));
        }
        return m_arrangeWindowsMenuItem;
    }

    public JMenu getWindowMenu()
    {
        if (m_windowMenu == null)
        {
            m_windowMenu = new JMenu(Messages.getTitle("Menu.Window"));
            m_windowMenu.setMnemonic(KeyEvent.VK_W);

            m_windowMenu.add(getCascadeWindowsMenuItem());
            m_windowMenu.add(getArrangeWindowsMenuItem());
            m_windowMenu.addSeparator();
            m_windowEmptyItem = new JMenuItem(Messages.getString("Menu.Window.Frames.empty"));
            m_windowEmptyItem.setEnabled(false);
            m_windowMenu.add(m_windowEmptyItem);

        }
        return m_windowMenu;
    }

    public JMenuItem getEditMenuItem()
    {
        if (m_editMenuItem == null)
        {
            m_editMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_EDIT));
        }
        return m_editMenuItem;
    }

    public JMenu getFramesMenu()
    {
        if (m_framesMenu == null)
        {
            updateFrameMenu();
        }
        return m_framesMenu;
    }

    public void updateFrameMenu()
    {
        if (m_framesMenu == null)
        {
            m_framesMenu = new JMenu(Messages.getTitle("Menu.Window.Frames"));
        }
        // TODO: Window Menu
        // m_framesMenu.removeAll();
        // DefaultEditorFrame[] frames =
        // OLDUserInterface.getInstance().getEditorFrames();
        // if (frames.length == 0)
        // {
        // JMenuItem emptyItem = new
        // JMenuItem(Messages.getString("Menu.Window.Frames.empty"));
        // emptyItem.setEnabled(false);
        // m_framesMenu.add(emptyItem);
        // } else
        // {
        // for (int i = 0; i < frames.length; i++)
        // {
        // JMenuItem frameItem = new
        // JMenuItem(SelectFrameAction.getInstance(frames[i]));
        // frameItem.setSelected(false);
        // m_framesMenu.add(frameItem);
        // }
        // }
    }

    public void addViewListener(IViewListener listener)
    {
        viewListener.addElement(listener);
    }

    public String getId()
    {
        return id;
    }

    public void removeViewListener(IViewListener listenner)
    {
        viewListener.removeElement(listenner);
    }

    public int getViewControllerType()
    {
        return DefaultApplicationMediator.VIEWCONTROLLER_MENU;
    }

    /**
     * Fires a ViewEvent to each listener as long as the event is not consumed.
     * The event is also set with a reference to the current listener.
     */
    public final void fireViewEvent(AbstractViewEvent viewevent)
    {
        if (viewevent == null) return;
        java.util.Vector vector;
        synchronized (viewListener)
        {
            vector = (java.util.Vector) viewListener.clone();
        }
        if (vector == null) return;
        int i = vector.size();
        for (int j = 0; !viewevent.isConsumed() && j < i; j++)
        {
            IViewListener viewlistener = (IViewListener) vector.elementAt(j);
            viewevent.setViewListener(viewlistener);
            viewlistener.viewEventPerformed(viewevent);
        }
    }

    public void addEditor(IEditor editor)
    {
        m_windowMenu.remove(m_windowEmptyItem);
        m_editorMenuItems.put(editor, m_windowMenu.add(new JCheckBoxMenuItem(ActionFactory.getSelectEditorAction(editor))));
    }

    public void removeEditor(IEditor editor)
    {
        JMenuItem menuItem = (JMenuItem) m_editorMenuItems.get(editor);
        if (menuItem != null)
        {
            m_windowMenu.remove(menuItem);
            m_editorMenuItems.remove(editor);
            if (m_editorMenuItems.isEmpty())
            {
                m_windowMenu.add(m_windowEmptyItem);
            }
        }
    }

    public void selectEditor(IEditor editor)
    {
        JMenuItem menuItem = (JMenuItem) m_editorMenuItems.get(editor);
        Iterator itr = m_editorMenuItems.values().iterator();
        while (itr.hasNext())
        {
            JMenuItem curElement = ((JMenuItem) itr.next());
            curElement.setSelected(menuItem == curElement);
        }
    }
}