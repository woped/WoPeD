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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.controller.vc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;
import org.woped.core.utilities.FileFilterImpl;
import org.woped.core.utilities.Utils;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.action.EditorViewEvent;
import org.woped.editor.gui.config.AbstractConfPanel;
import org.woped.editor.gui.config.ConfApromorePanel;
import org.woped.editor.gui.config.ConfEditorPanel;
import org.woped.editor.gui.config.ConfFilePanel;
import org.woped.editor.gui.config.ConfLanguagePanel;
import org.woped.editor.gui.config.ConfMetricsPanel;
import org.woped.editor.gui.config.ConfNLPToolsPanel;
import org.woped.editor.gui.config.ConfBusinessDashboardPanel;
import org.woped.editor.gui.config.ConfUnderstandabilityPanel;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 *         Main configuration window. <br>
 *         On the left you can add <code>AbstractConfPanel</code> s to the tree.
 *         Clicking on an node, the assisitaed Panel will show up in the right. <br>
 *         It's very easy to manage and configure the configure window. <br>
 *
 *         Created on: 26.11.2004 Last Change on: 26.11.2004
 */
@SuppressWarnings("serial")
public class ConfigVC extends JDialog implements IViewController {
	// ViewControll
	private Vector<IViewListener> viewListener = new Vector<IViewListener>(1, 3);
	private String id = null;
	public static final String ID_PREFIX = "CONFIG_VC_";
	private HashMap<String, AbstractConfPanel> confPanels = new HashMap<String, AbstractConfPanel>();
	public static final Dimension CONF_DIM = new Dimension(750, 600);
	public static final Dimension SCROLL_DIM = new Dimension(750, 600);
	public static final Color BACK_COLOR = new Color(255, 255, 255);
	private static JFileChooser jfc = null;
	private static Vector<String> xmlExtensions = new Vector<String>();
	// GUI Components
	private JTabbedPane tabbedPane = null;
	private AbstractConfPanel editorPanel = null;
	private AbstractConfPanel impexpPanel = null;
	private AbstractConfPanel langPanel = null;
	private AbstractConfPanel colorPanel = null;
	private AbstractConfPanel metricsPanel = null;
	private AbstractConfPanel p2tPanel = null;
	private AbstractConfPanel aproPanel = null;
	private AbstractConfPanel businessdashboardPanel = null;
	// ButtonPanel
	private JPanel buttonPanel = null;
	private WopedButton okButton = null;
	private WopedButton cancelButton = null;
	private WopedButton applyButton = null;

	public ConfigVC(boolean modal, String id) {
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
	public ConfigVC(JFrame owner, boolean modal, String id)
			throws HeadlessException {
		super(owner, modal);
		this.id = id;
		initialize();
	}

	/**
	 * Usage: <br>
	 * Just call
	 * <code>addConfNodePanel([name of the parent node], [your AbstractConfPanel]);</code>
	 */
	private void initialize() {
		/* init GUI */
		this.setTitle(Messages.getString("Configuration.Title"));
		this.setSize(CONF_DIM);
		this.setResizable(true);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		((java.awt.Frame) getOwner()).setIconImage(Messages.getImageIcon(
				"Application").getImage());
		// read Conf
		readConfiguration();
		xmlExtensions.add("xml");
	}

	private void resetConfiguration() {
		Iterator<String> iter = confPanels.keySet().iterator();
		while (iter.hasNext()) {
			confPanels.get(iter.next()).readConfiguration();
		}
	}

	private boolean applyConfiguration() {
		boolean confOK = true;
		Iterator<String> iter = confPanels.keySet().iterator();
		while (iter.hasNext()) {
			if (!((AbstractConfPanel) confPanels.get(iter.next()))
					.applyConfiguration())
				confOK = false;
		}
//		if (((ConfApromorePanel) aproPanel).getInputOK()){
//		if (aproPanel.getChangeServer() == true) {
//			aproPanel.changeApromoreServer(aproPanel.getApromoreServerID());
//		}
//		if (aproPanel.getAddServer() == true) {
//			aproPanel.addApromoreServer(aproPanel.getApromoreServerID());
//		}}
		if (confOK) {
			// ConfigurationManager.getConfiguration().saveConfig();
			fireViewEvent(new EditorViewEvent(this,
					AbstractViewEvent.VIEWEVENTTYPE_APPLICATION,
					AbstractViewEvent.UPDATE));
		}
		return confOK;
	}

	public void readConfiguration() {
		Iterator<String> iter = confPanels.keySet().iterator();
		while (iter.hasNext()) {
			((AbstractConfPanel) confPanels.get(iter.next()))
					.readConfiguration();
		}
	}

	/**
	 * TODO: Implement Export Configuration
	 */
	public void export() {
		if (ConfigurationManager.getConfiguration().getHomedir() != null) {
			jfc.setCurrentDirectory(new File(ConfigurationManager
					.getConfiguration().getHomedir()));
		}
		jfc.setDialogTitle("Export Configuration");
		jfc.showSaveDialog(null);
		if (jfc.getSelectedFile() != null) {
			String savePath = jfc
					.getSelectedFile()
					.getAbsolutePath()
					.substring(
							0,
							jfc.getSelectedFile().getAbsolutePath().length()
									- jfc.getSelectedFile().getName().length());
			if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.XMLFilter) {
				savePath = savePath
						+ Utils.getQualifiedFileName(jfc.getSelectedFile()
								.getName(), xmlExtensions);
			}
			ConfigurationManager.getConfiguration().saveConfig(
					new File(savePath));
		}
	}

	/**
	 * TODO: Implement Import Configuration
	 */
	public void importConf() {
		if (ConfigurationManager.getConfiguration().getHomedir() != null) {
			jfc.setCurrentDirectory(new File(ConfigurationManager
					.getConfiguration().getHomedir()));
		}
		jfc.setDialogTitle("Import Configuration");
		jfc.showOpenDialog(null);
		if (jfc.getSelectedFile() != null) {
			String readPath = jfc
					.getSelectedFile()
					.getAbsolutePath()
					.substring(
							0,
							jfc.getSelectedFile().getAbsolutePath().length()
									- jfc.getSelectedFile().getName().length());
			if (((FileFilterImpl) jfc.getFileFilter()).getFilterType() == FileFilterImpl.XMLFilter) {
				readPath = readPath
						+ Utils.getQualifiedFileName(jfc.getSelectedFile()
								.getName(), xmlExtensions);
			}
			ConfigurationManager.getConfiguration().readConfig(
					new File(readPath));
			readConfiguration();
			applyConfiguration();
		}
	}

	// ##################### Listener Methoden ############################# */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	// ######################## GUI COMPONENTS ############################ */
	/**
	 * @return Returns the splitPane.
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			editorPanel = new ConfEditorPanel(
					Messages.getString("Configuration.Editor.Title"));
			tabbedPane.addTab(Messages.getString("Configuration.Editor.Title"),
					editorPanel);
			confPanels.put(editorPanel.getPanelName(), editorPanel);
			impexpPanel = new ConfFilePanel(
					Messages.getString("Configuration.Files.Title"));
			tabbedPane.addTab(Messages.getString("Configuration.Files.Title"),
					impexpPanel);
			confPanels.put(impexpPanel.getPanelName(), impexpPanel);
			langPanel = new ConfLanguagePanel(
					Messages.getString("Configuration.Language.Title"));
			tabbedPane.addTab(
					Messages.getString("Configuration.Language.Title"),
					langPanel);
			confPanels.put(langPanel.getPanelName(), langPanel);
			colorPanel = new ConfUnderstandabilityPanel(
					Messages.getString("Configuration.ColorLayout.Title"));
			tabbedPane.addTab(Messages
					.getString("Configuration.ColorLayout.ColorPanel.Title"),
					colorPanel);
			confPanels.put(colorPanel.getPanelName(), colorPanel);
			metricsPanel = new ConfMetricsPanel(
					Messages.getString("Configuration.Metrics.Title"));
			tabbedPane.addTab(
					Messages.getString("Configuration.Metrics.Title"),
					metricsPanel);
			confPanels.put(metricsPanel.getPanelName(), metricsPanel);
			aproPanel = new ConfApromorePanel(
					Messages.getString("Configuration.Apromore.Title"));
//			((ConfApromorePanel) aproPanel).saveConfig();
			tabbedPane.addTab(
					Messages.getString("Configuration.Apromore.Title"),
					aproPanel);
			confPanels.put(aproPanel.getPanelName(), aproPanel);
			p2tPanel = new ConfNLPToolsPanel(
					Messages.getString("Configuration.P2T.Title"));
			tabbedPane.addTab(Messages.getString("Configuration.P2T.Title"),
					p2tPanel);
			confPanels.put(p2tPanel.getPanelName(), p2tPanel);
			
			businessdashboardPanel = new ConfBusinessDashboardPanel("Business Dashboard");
			tabbedPane.addTab("Business Dashboard", businessdashboardPanel);
			confPanels.put(businessdashboardPanel.getPanelName(), businessdashboardPanel);
			
			readConfiguration();
			/*
			 * tabbedPane.addChangeListener(new ChangeListener() { public void
			 * stateChanged(ChangeEvent changeEvent) { if
			 * (tabbedPane.getSelectedComponent() == aproPanel)
			 * aproPanel.setIsApromorePanelSelected(true); else
			 * aproPanel.setIsApromorePanelSelected(false); tabbedPane =
			 * (JTabbedPane) changeEvent.getSource(); if
			 * (tabbedPane.getSelectedComponent() == null) { return; }
			 * applyConfiguration(); } });
			 */
		}
		return tabbedPane;
	}

	// ########################### BUTTONPANEL ##########################*/
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.add(getOkButton());
			buttonPanel.add(getCancelButton());
			buttonPanel.add(getApplyButton());
		}
		return buttonPanel;
	}

	private JButton getApplyButton() {
		if (applyButton == null) {
			applyButton = new WopedButton();
			applyButton.setMnemonic(Messages.getMnemonic("Button.Apply"));
			applyButton.setText(Messages.getTitle("Button.Apply"));
			applyButton.setIcon(Messages.getImageIcon("Button.Apply"));
			applyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					applyConfiguration();
				}
			});
		}
		return applyButton;
	}

	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new WopedButton();
			cancelButton.setMnemonic(Messages.getMnemonic("Button.Cancel"));
			cancelButton.setText(Messages.getTitle("Button.Cancel"));
			cancelButton.setIcon(Messages.getImageIcon("Button.Cancel"));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					

					int result = JOptionPane.showConfirmDialog(
							tabbedPane,
							Messages.getString("Configuration.Dialog.DiscardChanges.Message"),
							Messages.getString("Configuration.Dialog.DiscardChanges.Title"),
							JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) {
						aproPanel.disableServerSettings();
						aproPanel.setButtonsToDefault();
						resetConfiguration();
						ConfigVC.this.dispose();
					} 
					
				}
			});
		}
		return cancelButton;
	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new WopedButton();
			okButton.setMnemonic(Messages.getMnemonic("Button.Ok"));
			okButton.setText(Messages.getTitle("Button.Ok"));
			okButton.setIcon(Messages.getImageIcon("Button.Ok"));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (applyConfiguration())
						if (tabbedPane.getSelectedComponent() == aproPanel
								&& (aproPanel.getNameAlreadyInUse() == true || !((ConfApromorePanel) aproPanel).getInputOK()) ) {
							return;
						} else
							ConfigVC.this.dispose();
				}
			});
		}
		return okButton;
	}

	public void addViewListener(IViewListener listener) {
		viewListener.addElement(listener);
	}

	public String getId() {
		return id;
	}

	public void removeViewListener(IViewListener listenner) {
		viewListener.removeElement(listenner);
	}

	public int getViewControllerType() {
		return ApplicationMediator.VIEWCONTROLLER_CONFIG;
	}

	/**
	 * Fires a ViewEvent to each listener as long as the event is not consumed.
	 * The event is also set with a reference to the current listener.
	 */
	public final void fireViewEvent(AbstractViewEvent viewevent) {
		if (viewevent == null)
			return;
		Vector<IViewListener> vector;
		synchronized (viewListener) {
			vector = (Vector<IViewListener>) viewListener.clone();
		}
		if (vector == null)
			return;
		int i = vector.size();
		for (int j = 0; !viewevent.isConsumed() && j < i; j++) {
			IViewListener viewlistener = (IViewListener) vector.elementAt(j);
			viewevent.setViewListener(viewlistener);
			viewlistener.viewEventPerformed(viewevent);
		}
	}
}