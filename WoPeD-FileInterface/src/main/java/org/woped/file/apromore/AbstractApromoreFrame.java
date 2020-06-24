package org.woped.file.apromore;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.woped.apromore.ApromoreAccess;
import org.woped.config.ApromoreServer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.file.apromore.processList.ApromoreProcessList;
import org.woped.file.apromore.tree.ApromoreFolderTree;
import org.woped.file.apromore.worker.ProcessListWorker;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.lookAndFeel.WopedProgressBar;
import org.woped.gui.translations.Messages;

/**
 * Abstract Class for Apromore Im/Export- Frames
 * 
 * @author David Rah�user Abstract Class-Template for Apromore In/Export
 *
 */
public abstract class AbstractApromoreFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	protected AbstractApplicationMediator mediator;
	protected ApromoreAccess aproAccess;

	protected ApromoreProcessList processList;

	protected boolean importing = false;

	protected JPanel serverSelectionPane;
	protected JComboBox<String> serverDropdown;
	protected JPanel processListPanel;
	protected JPanel buttonPanel;

	protected WopedButton cancelButton;
	protected WopedButton updateButton;
	protected WopedProgressBar wopedPorgressBar;
	protected JComboBox<String> processFolderBox;
	protected WopedButton importButton;

	protected ProcessListWorker progressBarWorker;

	protected WopedButton exportButton;
	protected JPanel createFolderPanel;
	protected WopedButton createFolderButton;
	protected JTextField textInput;
	protected ApromoreFolderTree tree;
	protected boolean firsttime = true;

	public AbstractApromoreFrame(AbstractApplicationMediator mediator) {

		this.mediator = mediator;
		this.aproAccess = new ApromoreAccess(this.getContentPane());
		this.processList = new ApromoreProcessList(aproAccess, this, mediator);

		initialize();
	}

	private void initialize() {

		int left = (int) ApplicationMediator.getDisplayUI().getLocation()
				.getX();
		int top = (int) ApplicationMediator.getDisplayUI().getLocation().getY();

		setLocation(left + 440, top + 180);
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(750, 500));
		setMinimumSize(new Dimension(750, 400));
		setModal(true);
		setResizable(true);
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(10, 0, 0, 0); // oben abstand
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		this.serverSelectionPane = getServerSelectionPane();
		getContentPane().add(serverSelectionPane, c);

		c.insets = new Insets(10, 0, 0, 0); // oben abstand
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 3;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		this.processListPanel = getProcessListPanel(false);
		getContentPane().add(processListPanel, c);
	}

	protected JPanel getCreateFolderPanel() {
		if (createFolderPanel == null) {
			createFolderPanel = new JPanel();

			createFolderPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.insets = new Insets(5, 5, 5, 5);
			textInput = new JTextField();
			createFolderPanel.add(textInput, c);

			c.insets = new Insets(0, 0, 0, 0);
			createFolderButton = new WopedButton("Create new Folder");
			createFolderButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// aproAccess.createNewFolder(textInput.getText());
				}
			});
			c.gridx = 1;
			createFolderPanel.add(createFolderButton);

		}
		createFolderPanel.setVisible(false);
		return createFolderPanel;
	}

	protected JPanel getProcessListPanel(boolean showPnmlOnly) {
		if (processListPanel == null) {
			processListPanel = new JPanel(new GridBagLayout());

			processListPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.UI.ProcessList")),
					BorderFactory.createEmptyBorder(2, 0, 0, 2)));

			GridBagConstraints c = new GridBagConstraints();

			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 0.3;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			processListPanel.add(getApromoreFoldertree().getTree(), c);

			c.gridx = 0;
			c.gridy = 1;
			c.weighty = 0.01;
			c.fill = GridBagConstraints.BOTH;
			processListPanel.add(getCreateFolderPanel(), c);

			c.fill = GridBagConstraints.BOTH;
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 2;
			c.gridheight = 2;
			processListPanel.add(
					processList.getScrollableProcessTable(showPnmlOnly), c);

		}
		return processListPanel;

	}

	private ApromoreFolderTree getApromoreFoldertree() {
		if (tree == null) {
			tree = new ApromoreFolderTree(this);
		}
		return tree;
	}

	protected JPanel getServerSelectionPane() {
		if (serverSelectionPane == null) {
			serverSelectionPane = new JPanel();
			serverSelectionPane.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.UI.ServerSelectionTitel")),
					BorderFactory.createEmptyBorder(2, 0, 0, 2)));

			serverSelectionPane.add(new JLabel(Messages
					.getString("Apromore.UI.CurrentServer")));

			serverDropdown = new JComboBox<String>();
			serverDropdown.setBorder(BorderFactory
					.createEmptyBorder(2, 0, 0, 2));
			serverDropdown.setPreferredSize(new Dimension(250, 20));

			ApromoreServer[] servers = aproAccess.getServers();
			if (servers != null) {

				for (int i = 0; i < servers.length; i++) {
					if (servers[i] != null) {
						serverDropdown.addItem(servers[i]
								.getApromoreServerName());
					}
				}
			}

			serverDropdown.setSelectedIndex(ConfigurationManager
					.getConfiguration().getCurrentApromoreIndex());
			aproAccess.setCurrentServer(serverDropdown.getSelectedIndex());

			serverDropdown.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {

						loadProcessList();

					}
				}

			});
			serverSelectionPane.add(serverDropdown);
		}

		return serverSelectionPane;
	}

	protected WopedProgressBar getWopedProgressBar() {
		wopedPorgressBar = new WopedProgressBar();
		wopedPorgressBar.connectToServerLoading();
		return wopedPorgressBar;
	}

	public WopedButton getUpdateButton() {

		if (updateButton == null) {
			updateButton = new WopedButton();
			updateButton.setText(Messages.getTitle("Button.Update"));
			updateButton.setIcon(Messages.getImageIcon("Button.Update"));
			updateButton.setMnemonic(Messages.getMnemonic("Button.Update"));
			updateButton.setPreferredSize(new Dimension(130, 25));
			updateButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					loadProcessList();
				}
			});
		}

		return updateButton;
	}

	protected WopedButton getCancelButton() {

		if (cancelButton == null) {
			cancelButton = new WopedButton();
			cancelButton.setText(Messages.getTitle("Button.Cancel"));
			cancelButton.setIcon(Messages.getImageIcon("Button.Cancel"));
			cancelButton.setMnemonic(Messages.getMnemonic("Button.Cancel"));
			cancelButton.setPreferredSize(new Dimension(130, 25));
			cancelButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					processList.cancelAction();
				}
			});
		}

		return cancelButton;
	}

	public AbstractApplicationMediator getMediator() {
		return mediator;
	}

	/**
	 * Methode zur Apromore Anbindung
	 */
	@Override
	public void dispose() {

		aproAccess = null;
		processList = null;
		progressBarWorker.cancel(true);
		progressBarWorker = null;
		super.dispose();
	}

	protected void loadProcessList() {
		wopedPorgressBar.connectToServerLoading();

		Thread queryThread = new Thread() {

			@Override
			public void run() {

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						AbstractApromoreFrame.this.getWopedPorgressBar()
								.setIndeterminate(true);

						AbstractApromoreFrame.this.setButtons(false);
					}
				});

				if (progressBarWorker != null) {
					progressBarWorker.cancel(true);
				}
				
				progressBarWorker = new ProcessListWorker(
						AbstractApromoreFrame.this);
				progressBarWorker.execute();
			}
		};
		queryThread.start();
	}

	public void setButtons(boolean b) {

		if (importButton != null) {
			importButton.setEnabled(b);
		}

		if (updateButton != null) {
			updateButton.setEnabled(b);
		}

		if (exportButton != null) {
			exportButton.setEnabled(b);
		}

		if (serverDropdown != null) {
			aproAccess.setCurrentServer(serverDropdown.getSelectedIndex());

		}
		
		if (createFolderButton != null) {
			createFolderButton.setEnabled(false);
			// createFolderButton.setEnabled(b);
		}
	}

	public void setListFilter(String[] filters) {
		processList.setFilter(filters);
	}

	public WopedButton getImportButton() {
		return importButton;
	}

	public WopedButton getExportButton() {
		return exportButton;
	}

	public WopedProgressBar getWopedPorgressBar() {
		return wopedPorgressBar;
	}

	public JComboBox<String> getServerDropdown() {
		return serverDropdown;
	}

	public ApromoreProcessList getProcessList() {
		return processList;
	}

	public JComboBox<String> getProcessFolderBox() {
		return processFolderBox;
	}

	public ApromoreAccess getAproAccess() {
		return aproAccess;
	}

	public boolean isImporting() {
		return importing;
	}

	public void setImporting(boolean importing) {
		this.importing = importing;
	}

	public void setApromorTree(ArrayList<String> subFolders) {
		tree.setSubFolders(subFolders);
	}

}