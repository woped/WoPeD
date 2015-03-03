package org.woped.file.apromore;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apromore.model.FolderType;
import org.woped.apromore.ApromoreAccess;
import org.woped.config.ApromoreServer;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.ApplicationMediator;
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

	public ApromoreAccess getAproAccess() {
		return aproAccess;
	}

	protected ApromoreProcessList processList;

	protected JPanel serverSelectionPane;
	protected JComboBox<String> serverDropdown;
	protected JPanel processListPanel;
	protected JPanel buttonPanel = null;

	protected WopedButton cancelButton = null;
	protected WopedButton updateButton = null;
	protected WopedProgressBar wopedPorgressBar;
	protected JComboBox<String> processFolderBox = null;
	protected WopedButton importButton = null;

	protected ProgressbarWorker progressBarWorker;

	protected WopedButton exportButton = null;

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
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			processListPanel.add(
					processList.getScrollableProcessTable(showPnmlOnly), c);
		}
		return processListPanel;

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

			// aproAccess.setCurrentServer(ConfigurationManager.getConfiguration()
			// .getCurrentApromoreIndex());

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

			serverDropdown.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == ItemEvent.SELECTED) {
						// String item = (String)e.getItem();

						executeProgressBarWorker();
					}
				}

			});
			serverSelectionPane.add(serverDropdown);

		}

		return serverSelectionPane;
	}

	protected WopedProgressBar getWopedProgressBar() {

		return wopedPorgressBar = new WopedProgressBar();

	}

	protected WopedButton getUpdateButton() {

		if (updateButton == null) {
			updateButton = new WopedButton();
			updateButton.setText(Messages.getTitle("Button.Update"));
			updateButton.setIcon(Messages.getImageIcon("Button.Update"));
			updateButton.setMnemonic(Messages.getMnemonic("Button.Update"));
			updateButton.setPreferredSize(new Dimension(130, 25));
			updateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					executeProgressBarWorker();

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

	class ProgressbarWorker extends SwingWorker<String[][], Void> {

		private AbstractApromoreFrame parent;

		public ProgressbarWorker(AbstractApromoreFrame parent) {
			this.parent = parent;
		}

		@Override
		protected String[][] doInBackground() throws Exception {

			aproAccess.setCurrentServer(ConfigurationManager.getConfiguration()
					.getCurrentApromoreIndex());

			if (processFolderBox != null) {

				processFolderBox.removeAllItems();
				List<FolderType> folders = aproAccess
						.getFoldersForCurrentUser();

				for (FolderType folder : folders) {
					processFolderBox.addItem(folder.getFolderName());
				}
				processFolderBox.addItem("User");
			}

			String[][] rowStrings = processList.getProcessList(false);

			return rowStrings;
		}

		public void done() {

			try {
				String[][] rows = this.get();
				if (rows != null) {

					processList.setProcessList(rows);

					setButtonsEnabled(true);

					wopedPorgressBar.connectingToSeverSuccess();
				} else {
					if (serverDropdown != null) {
						serverDropdown.setEnabled(true);
					}
					if (updateButton != null) {
						updateButton.setEnabled(true);
					}
					wopedPorgressBar.connectingToSeverFailed();
				}

			} catch (InterruptedException | ExecutionException ex) {
				ex.printStackTrace();
			}

		}
	}

	public void executeProgressBarWorker() {

		wopedPorgressBar.connectToServerLoading();
		if (progressBarWorker == null) {
			wopedPorgressBar.setIndeterminate(true);
			new ProgressbarWorker(this).execute();
		}
		setButtonsEnabled(false);

	}

	protected void setButtonsEnabled(boolean enabled) {
		if (importButton != null) {
			importButton.setEnabled(enabled);
		}

		if (exportButton != null) {
			exportButton.setEnabled(enabled);

		}
		if (updateButton != null) {
			updateButton.setEnabled(enabled);
		}

		if (serverDropdown != null) {
			serverDropdown.setEnabled(enabled);
		}
	}

}
