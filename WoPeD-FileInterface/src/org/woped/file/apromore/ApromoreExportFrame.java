package org.woped.file.apromore;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.apromore.model.FolderType;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ApromoreExportFrame extends AbstractApromoreFrame {

	private static final long serialVersionUID = 1L;
	private JPanel processDataPanel = null;
	private JLabel processIDLabel = null;
	private JTextField processIDText = null;
	private JLabel processNameLabel = null;
	private JTextField processNameText = null;
	private JLabel processOwnerLabel = null;
	private JTextField processOwnerText = null;
	private JLabel processVersionLabel = null;
	private JTextField processVersionText = null;
	private JLabel processTypeLabel = null;
	private JTextField processTypeText = null;
	private JLabel processFolderLabel = null;
	private JLabel processPrivacyLabel = null;
	private JComboBox<String> processPrivacyText = null;

	private ExportUpdateWorker exportUpdateWorker;
	private ExportWorker exportWorker;

	public ApromoreExportFrame(AbstractApplicationMediator mediator) {
		super(mediator);
		setTitle(Messages.getString("Apromore.UI.Export.Title"));
		initialize();

	}

	private void initialize() {

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 0, 5, 0); // Abstand nach oben/unten von 5px

		getContentPane().add(getDataPanel(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 10, 0); // Abstand nach oben/unten von 10px
		getContentPane().add(getButtonPanel(), c);
		getContentPane().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				processList.clearSelection();
				prepareExportFields();
			}
		});

		// Export Event
		processList.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					String version = checkCurrentVersion(getProcessVersionText()
							.getText());
					if (version != null) {

						if (aproAccess.checkIfServerAvailable()) {

							if (exportUpdateWorker != null) {
								exportUpdateWorker.cancel(true);
							}
							exportUpdateWorker = new ExportUpdateWorker(
									ApromoreExportFrame.this, version);
							exportUpdateWorker.execute();

						}

					}
				}
				prepareUpdateFields();
				updateDataFields();
				getUpdateButton().setEnabled(true);
			}
		});
		this.pack();
		this.setModal(true);
		initDataFields();
		executeProgressBarWorker();
		setVisible(true);

	}

	/**
	 * Aktuallisiert die Prozess-Datenfelder mit dem ausgew‰hlten
	 * Tabellenelement
	 */
	private void updateDataFields() {

		JTable dataTable = processList.getTable();
		int ind = dataTable.getSelectedRow();

		if (ind != -1) {
			getProcessNameText().setText(dataTable.getValueAt(ind, 0) + "");
			getProcessIDText().setText(dataTable.getValueAt(ind, 1) + "");
			getProcessOwnerText().setText(dataTable.getValueAt(ind, 2) + "");
			getProcessTypeText().setText(dataTable.getValueAt(ind, 3) + "");
			getProcessVersionText().setText(dataTable.getValueAt(ind, 5) + "");

		}

	}

	private void initDataFields() {
		getProcessNameText().setText(
				mediator.getUi().getEditorFocus().getName()
						.replace(".pnml", ""));
		getProcessIDText().setText("");
		getProcessOwnerText().setText(
				ConfigurationManager.getConfiguration().getApromoreUsername());
		getProcessTypeText().setText("PNML 1.3.2");
		getProcessVersionText().setText("1.0");
	}

	public String getProcessUiVersion() {
		return Messages.getString("Apromore.UI.Version");
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.UI.ButoonPanelExport")),
					BorderFactory.createEmptyBorder(0, 2, 0, 2)));

			buttonPanel.add(getExportButton());
			buttonPanel.add(getUpdateButton());
			buttonPanel.add(getCancelButton());
			buttonPanel.add(getWopedProgressBar());

		}

		return buttonPanel;
	}

	private WopedButton getExportButton() {

		if (exportButton == null) {
			exportButton = new WopedButton();
			exportButton.setText(Messages.getTitle("Button.Export"));
			exportButton.setIcon(Messages.getImageIcon("Button.Export"));
			exportButton.setMnemonic(Messages.getMnemonic("Button.Export"));
			exportButton.setPreferredSize(new Dimension(130, 25));
			exportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String version = checkCurrentVersion(processVersionText
							.getText());
					if (version != null) {
						if (aproAccess.checkIfServerAvailable()) {
							if (exportWorker != null) {
								exportWorker.cancel(true);
							}
							exportWorker = new ExportWorker(
									ApromoreExportFrame.this, version);
							exportWorker.execute();
						}
					}
				}
			});
		}

		return exportButton;
	}

	private JPanel getDataPanel() {

		if (processDataPanel == null) {
			processDataPanel = new JPanel();
			GridBagConstraints c = new GridBagConstraints();
			processDataPanel.setLayout(new GridBagLayout());

			processDataPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.UI.ProcessData")),
					BorderFactory.createEmptyBorder(0, 2, 0, 2)));

			c.anchor = GridBagConstraints.NORTH;
			Insets inset = new Insets(5, 0, 10, 0);

			// -------------------------label
			c = getConstraits(c, 0, 0, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessIDLabel(), c);

			c = getConstraits(c, 1, 0, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessNameLabel(), c);
			c = getConstraits(c, 2, 0, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessOwnerLabel(), c);

			c = getConstraits(c, 3, 0, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessTypeLabel(), c);

			c = getConstraits(c, 4, 0, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessFolderLabel(), c);

			c = getConstraits(c, 5, 0, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessVersionLabel(), c);

			c = getConstraits(c, 6, 0, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessPrivacyLabel(), c);

			// -------------------------Text
			c = getConstraits(c, 0, 1, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessIDText(), c);

			c = getConstraits(c, 1, 1, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessNameText(), c);

			c = getConstraits(c, 2, 1, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessOwnerText(), c);

			c = getConstraits(c, 3, 1, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessTypeText(), c);

			c = getConstraits(c, 4, 1, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessFolderBox(), c);

			c = getConstraits(c, 5, 1, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessVersionText(), c);

			c = getConstraits(c, 6, 1, 1, 1, 1, 1, inset,
					GridBagConstraints.HORIZONTAL);
			processDataPanel.add(getProcessPrivacyText(), c);
		}
		prepareExportFields();

		return processDataPanel;

	}

	private GridBagConstraints getConstraits(GridBagConstraints c, int x,
			int y, int width, int height, int weightX, int weightY,
			Insets inset, int fillConstrait) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		c.weightx = weightX;
		c.weighty = weightY;
		c.insets = inset;
		c.fill = fillConstrait;
		return c;
	}

	private JTextField getProcessIDText() {

		if (processIDText == null) {
			processIDText = new JTextField(5);
		}

		return processIDText;
	}

	private JLabel getProcessIDLabel() {

		if (processIDLabel == null) {
			processIDLabel = new JLabel(Messages.getString("Apromore.UI.ID"));
		}

		return processIDLabel;
	}

	private JLabel getProcessNameLabel() {

		if (processNameLabel == null) {
			processNameLabel = new JLabel(
					Messages.getString("Apromore.UI.ProcessName"));
		}

		return processNameLabel;
	}

	private JTextField getProcessNameText() {

		if (processNameText == null) {
			processNameText = new JTextField(30);
		}

		return processNameText;
	}

	private JLabel getProcessOwnerLabel() {

		if (processOwnerLabel == null) {
			processOwnerLabel = new JLabel(
					Messages.getString("Apromore.UI.Owner"));
		}

		return processOwnerLabel;
	}

	private JTextField getProcessOwnerText() {

		if (processOwnerText == null) {
			processOwnerText = new JTextField(10);
		}

		return processOwnerText;
	}

	private JLabel getProcessTypeLabel() {

		if (processTypeLabel == null) {
			processTypeLabel = new JLabel(
					Messages.getString("Apromore.UI.Type"));
		}

		return processTypeLabel;
	}

	private JTextField getProcessTypeText() {

		if (processTypeText == null) {
			processTypeText = new JTextField(10);
		}

		return processTypeText;
	}

	private JLabel getProcessFolderLabel() {
		if (processFolderLabel == null) {
			processFolderLabel = new JLabel(
					Messages.getString("Apromore.UI.Foldername"));
		}
		return processFolderLabel;
	}

	private JComboBox<String> getProcessPrivacyText() {

		if (processPrivacyText == null) {
			processPrivacyText = new JComboBox<String>();
			processPrivacyText.addItem("Public");
			processPrivacyText.addItem("Private");

		}

		return processPrivacyText;
	}

	private JLabel getProcessPrivacyLabel() {
		if (processPrivacyLabel == null) {
			processPrivacyLabel = new JLabel(
					Messages.getString("Apromore.UI.Privacy"));
		}
		return processPrivacyLabel;
	}

	private JComboBox<String> getProcessFolderBox() {
		if (processFolderBox == null) {

			processFolderBox = new JComboBox<String>();

		}
		return processFolderBox;
	}

	private JLabel getProcessVersionLabel() {

		if (processVersionLabel == null) {
			processVersionLabel = new JLabel(
					Messages.getString("Apromore.UI.Version"));
		}

		return processVersionLabel;
	}

	private JTextField getProcessVersionText() {

		if (processVersionText == null) {
			processVersionText = new JTextField(10);
		}

		return processVersionText;
	}

	private void prepareUpdateFields() {
		processIDLabel.setVisible(true);
		processIDText.setVisible(true);
		processIDText.setEditable(false);
		processFolderLabel.setVisible(false);
		processFolderBox.setVisible(false);
	}

	private void prepareExportFields() {
		processIDLabel.setVisible(false);
		processIDText.setVisible(false);
		processFolderLabel.setVisible(true);
		processFolderBox.setVisible(true);
	}

	private void showDialog(String message, String titel, Integer type) {
		JOptionPane.showMessageDialog(this, message, titel, type);
	}

	private String checkCurrentVersion(String versionString) {
		if (versionStringToDouble(versionString) != null) {
			return versionStringToDouble(versionString).substring(0, 3);
		}
		return null;
	}

	private String versionStringToDouble(String versionString) {
		if (!versionString.contains(".") && !versionString.contains(",")) {
			try {
				Integer.parseInt(versionString);
				versionString = versionString + ".0";
				return versionString;
			} catch (NumberFormatException e) {
				showDialog(
						Messages.getString("Apromore.UI.Error.VersionFormat"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else if (versionString.contains(".") && !versionString.contains(",")) {
			try {
				Double version = Double.valueOf(versionString);
				return version.toString();
			} catch (NumberFormatException e) {
				showDialog(
						Messages.getString("Apromore.UI.Error.VersionFormat"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else if (!versionString.contains(".") && versionString.contains(",")) {
			try {
				versionString = versionString.replace(",", ".");
				Double version = Double.valueOf(versionString);
				return version.toString();
			} catch (NumberFormatException e) {
				showDialog(
						Messages.getString("Apromore.UI.Error.VersionFormat"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else {
			showDialog(Messages.getString("Apromore.UI.Error.VersionFormat"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

	}

	class ExportUpdateWorker extends SwingWorker<Void, Void> {

		private AbstractApromoreFrame parent;
		private boolean importSuccess = false;
		private String version;

		public ExportUpdateWorker(ApromoreExportFrame apromoreExportFrame,
				String version) {
			this.version = version;
		}

		@Override
		protected Void doInBackground() throws Exception {

			wopedPorgressBar.setIndeterminate(true);
			wopedPorgressBar.setIndeterminate(false);
			if (serverDropdown != null) {
				serverDropdown.setEnabled(false);
			}
			if (exportButton != null) {
				exportButton.setEnabled(false);
			}

			processList.updateAction(Integer.valueOf(getProcessIDText()
					.getText()), getProcessOwnerText().getText(),
					getProcessTypeText().getText(), getProcessNameText()
							.getText(), version);
			processList.updateTable(processList.getTable(), processList
					.getTable().getSelectedRow(), Integer
					.valueOf(getProcessIDText().getText()));
			showDialog(Messages.getString("Apromore.UI.UpdateDialog.Succes"),
					Messages.getString("Apromore.UI.UpdateDialog.Title"),
					JOptionPane.INFORMATION_MESSAGE);

			return null;
		}

		@Override
		public void done() {

			wopedPorgressBar.setIndeterminate(false);

			if (exportButton != null) {
				exportButton.setEnabled(true);
			}

			if (serverDropdown != null) {
				serverDropdown.setEnabled(true);
			}

		}
	}

	class ExportWorker extends SwingWorker<Void, Void> {

		private AbstractApromoreFrame parent;
		private boolean importSuccess = false;
		private String version;

		public ExportWorker(ApromoreExportFrame apromoreExportFrame,
				String version) {
			this.version = version;
		}

		@Override
		protected Void doInBackground() throws Exception {
			try {
				wopedPorgressBar.setIndeterminate(true);
				wopedPorgressBar.setIndeterminate(false);
				if (serverDropdown != null) {
					serverDropdown.setEnabled(false);
				}
				if (exportButton != null) {
					exportButton.setEnabled(false);
				}

				if (!processList.getProcessNames().contains(
						getProcessNameText().getText())) {

					processList.exportAction(getProcessOwnerText().getText(),
							getProcessFolderBox().getSelectedItem().toString(),
							getProcessNameText().getText(), version);
					processList.addNewRow(processList.getTable());
					showDialog(
							Messages.getString("Apromore.UI.ExportDialog.Succes"),
							Messages.getString("Apromore.UI.ExportDialog.Title"),
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					showDialog(
							Messages.getString("Apromore.UI.Error.NameExists"),
							Messages.getString("Apromore.UI.Error.Title"),
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception exc) {
			}

			return null;
		}

		@Override
		public void done() {

			wopedPorgressBar.setIndeterminate(false);

			if (exportButton != null) {
				exportButton.setEnabled(true);
			}

			if (serverDropdown != null) {
				serverDropdown.setEnabled(true);
			}

		}
	}

}