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
package org.woped.editor.controller.vc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.Constants;
import org.woped.editor.action.DisposeWindowAction;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.EditorViewEvent;
import org.woped.editor.gui.config.AbstractConfPanel;
import org.woped.editor.gui.config.ConfPanelTree;
import org.woped.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         Main configuration window. <br>
 *         On the left you can add <code>AbstractConfPanel</code> s to the
 *         tree. Clicking on an node, the assisitaed Panel will show up in the
 *         right. <br>
 *         It's very easy to manage and configure the configure window. <br>
 * 
 * Created on: 26.11.2004 Last Change on: 26.11.2004
 */

@SuppressWarnings("serial")
public class ConfigVC extends JDialog implements TreeSelectionListener, IViewController
{
    // ViewControll
    private Vector<IViewListener> viewListener   = new Vector<IViewListener>(1, 3);
    private String                id             = null;
    public static final String    ID_PREFIX      = "CONFIG_VC_";

    private HashMap<String, AbstractConfPanel>               confPanels     = new HashMap<String, AbstractConfPanel>();

    public static final Dimension CONF_DIM       = new Dimension(650, 400);
    public static final Dimension SCROLL_DIM     = new Dimension(450, 370);
    public static final Dimension TREE_DIM       = new Dimension(140, 370);
    public static final Color     BACK_COLOR     = new Color(255, 255, 255);

    private static JFileChooser   jfc            = null;
    private static Vector<String> xmlExtensions  = new Vector<String>();
    // GUI Components
    private ConfPanelTree         confPanelTree;
    private JSplitPane            splitPane      = null;
    private JScrollPane           treeScrollPane = null;
    // ButtonPanel
    private JPanel                buttonPanel    = null;
    private JButton               okButton       = null;
    private JButton               cancelButton   = null;
    private JButton               applyButton    = null;

    public ConfigVC(boolean modal, String id)
    {
        this(null, modal, id);
    }

    /**
     * Contrstrutor for the configuration window.
     * 
     * @param owner
     *            Owner Frame.
     * @param modal
     *            true if in modal mode
     * @throws HeadlessException
     */
    public ConfigVC(JFrame owner, boolean modal, String id) throws HeadlessException
    {
        super(owner, modal);
        this.id = id;
        initialize();
    }

    /**
     * Usage: <br>
     * Just call
     * <code>addConfNodePanel([name of the parent node], [your AbstractConfPanel]);</code>
     */
    private void initialize()
    {
        /* init GUI */
        this.setTitle(Messages.getString("Configuration.Title"));
        this.setSize(CONF_DIM);
        this.setResizable(true);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getSplitPane(), BorderLayout.CENTER);
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        ((java.awt.Frame) getOwner()).setIconImage(Messages.getImageIcon("Application").getImage());
 
        // read Conf
        readConfiguration();
        xmlExtensions.add("xml");
    }

    public void addConfNodePanel(String parentNodePanelName, AbstractConfPanel nodePanel)
    {
        if (!confPanels.containsKey(nodePanel.getPanelName()))
        {
            confPanels.put(nodePanel.getPanelName(), nodePanel);
            getConfPanelTree().addConfNodePanel(parentNodePanelName, nodePanel.getPanelName());
            nodePanel.readConfiguration();
        } else
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "A Node-Panel with the name \"" + nodePanel.getPanelName() + "\" has already been added. Please rename.");
        }
    }

    public void setSelectedPanel(String panelName)
    {
        getConfPanelTree().setSelectedNode(panelName);
        refreshMainPanel(panelName);
        // Expand Root
        getConfPanelTree().expandPath(new TreePath((DefaultMutableTreeNode) getConfPanelTree().getModel().getRoot()));
    }

    private void refreshMainPanel(Object panelName)
    {
        if (confPanels.containsKey(panelName))
        {
            getSplitPane().setRightComponent((AbstractConfPanel) confPanels.get(panelName));
        } else
        {
            getSplitPane().setRightComponent(new JLabel("ERROR: Does Not exists."));
        }
    }

    private void resetConfiguration()
    {
        Iterator iter = confPanels.keySet().iterator();
        while (iter.hasNext())
        {
            confPanels.get(iter.next()).readConfiguration(); 
        }
    }

    private boolean applyConfiguration()
    {
        boolean confOK = true;
        Iterator iter = confPanels.keySet().iterator();
        while (iter.hasNext())
        {
            if (!((AbstractConfPanel) confPanels.get(iter.next())).applyConfiguration()) confOK = false;
        }
        if (confOK)
        {
            ConfigurationManager.getConfiguration().saveConfig();
            fireViewEvent(new EditorViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION, AbstractViewEvent.UPDATE));
        }
        return confOK;
    }

    public void readConfiguration()
    {
        Iterator iter = confPanels.keySet().iterator();
        while (iter.hasNext())
        {
            ((AbstractConfPanel) confPanels.get(iter.next())).readConfiguration();
        }
    }

    /**
     * TODO: Implement Export Configuration
     */
    public void export()
    {
        if (ConfigurationManager.getConfiguration().getHomedir() != null)
        {
            jfc.setCurrentDirectory(new File(ConfigurationManager.getConfiguration().getHomedir()));
        }
        jfc.setDialogTitle("Export Configuration");
        jfc.showSaveDialog(null);
        if (jfc.getSelectedFile() != null)
        {
            String savePath = jfc.getSelectedFile().getAbsolutePath().substring(0, jfc.getSelectedFile().getAbsolutePath().length() - jfc.getSelectedFile().getName().length());
            if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.XMLFilter)
            {
                savePath = savePath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), xmlExtensions);
            }
            ConfigurationManager.getConfiguration().saveConfig(new File(savePath));
        }
    }

    /**
     * TODO: Implement Import Configuration
     */
    public void importConf()
    {
        if (ConfigurationManager.getConfiguration().getHomedir() != null)
        {
            jfc.setCurrentDirectory(new File(ConfigurationManager.getConfiguration().getHomedir()));
        }
        jfc.setDialogTitle("Import Configuration");
        jfc.showOpenDialog(null);
        if (jfc.getSelectedFile() != null)
        {
            String readPath = jfc.getSelectedFile().getAbsolutePath().substring(0, jfc.getSelectedFile().getAbsolutePath().length() - jfc.getSelectedFile().getName().length());
            if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.XMLFilter)
            {
                readPath = readPath + Utils.getQualifiedFileName(jfc.getSelectedFile().getName(), xmlExtensions);
            }
            ConfigurationManager.getConfiguration().readConfig(new File(readPath));
            readConfiguration();
            applyConfiguration();
        }
    }

    // ##################### Listener Methoden ############################# */

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getConfPanelTree().getLastSelectedPathComponent();
        if (node == null) return;
        if (!node.isRoot()) refreshMainPanel(node.getUserObject());
    }

    // ######################## GUI COMPONENTS ############################ */

    /**
     * @return Returns the splitPane.
     */
    private JSplitPane getSplitPane()
    {
        if (splitPane == null)
        {
            splitPane = new javax.swing.JSplitPane();
            splitPane.setDividerSize(0);
            splitPane.setDividerLocation(140);
            splitPane.setLeftComponent(getTreeScrollPane());
        }
        splitPane.setDividerLocation(140);

        return splitPane;
    }

    /**
     * @return Returns the treeScrollPane.
     */
    private JScrollPane getTreeScrollPane()
    {
        if (treeScrollPane == null)
        {
            treeScrollPane = new JScrollPane(getConfPanelTree());
            treeScrollPane.setPreferredSize(TREE_DIM);
        }
        return treeScrollPane;
    }

    /**
     * @return Returns the confPanelTree.
     */
    private ConfPanelTree getConfPanelTree()
    {
        if (confPanelTree == null)
        {
            confPanelTree = new ConfPanelTree();
            confPanelTree.setBackground(BACK_COLOR);
            confPanelTree.addTreeSelectionListener(this);
        }
        return confPanelTree;
    }

    // ########################### BUTTONPANEL ##########################*/
    private JPanel getButtonPanel()
    {
        if (buttonPanel == null)
        {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2));
            JPanel innerPanelLeft = new JPanel();
            innerPanelLeft.setLayout(new FlowLayout(FlowLayout.LEFT));

            JPanel innerPanelRight = new JPanel();
            innerPanelRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
            innerPanelRight.add(getOkButton());
            innerPanelRight.add(getCancelButton());
            innerPanelRight.add(getApplyButton());

            buttonPanel.add(innerPanelLeft);
            buttonPanel.add(innerPanelRight);
        }
        return buttonPanel;
    }

    private JButton getApplyButton()
    {
        if (applyButton == null)
        {
            applyButton = new JButton();
            applyButton.setMnemonic(Messages.getMnemonic("Button.Apply"));
            applyButton.setText(Messages.getTitle("Button.Apply"));
            applyButton.setIcon(Messages.getImageIcon("Button.Apply"));
            applyButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    applyConfiguration();
                }
            });
        }
        return applyButton;
    }

    private JButton getCancelButton()
    {
        if (cancelButton == null)
        {
            cancelButton = new JButton();
            cancelButton.setMnemonic(Messages.getMnemonic("Button.Cancel"));
            cancelButton.setText(Messages.getTitle("Button.Cancel"));
            cancelButton.setIcon(Messages.getImageIcon("Button.Cancel"));
            cancelButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                	resetConfiguration();
                	ConfigVC.this.dispose();
                }
            });
        }
        return cancelButton;
    }

    private JButton getOkButton()
    {
        if (okButton == null)
        {
            okButton = new JButton();
            okButton.setText(Messages.getTitle("Button.Ok"));
            okButton.setIcon(Messages.getImageIcon("Button.Ok"));
            okButton.setMnemonic(Messages.getMnemonic("Button.Ok"));
            okButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    if (applyConfiguration()) ConfigVC.this.dispose();
                }
            });
        }
        return okButton;
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
        return ApplicationMediator.VIEWCONTROLLER_CONFIG;
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

}