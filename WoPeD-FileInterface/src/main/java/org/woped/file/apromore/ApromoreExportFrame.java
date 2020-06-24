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

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.file.apromore.worker.ExportWorker;
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
	private JComboBox<String> processPrivacyText;
	private ExportWorker exportWorker;

	public ApromoreExportFrame(AbstractApplicationMediator mediator) {
		super(mediator);
		setTitle(Messages.getString("Apromore.UI.ExportDialog.Title"));
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
			@Override
			public void mouseClicked(MouseEvent e) {
				processList.clearSelection();
				prepareExportFields();
			}
		});

		// Export Event
		processList.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					String version = checkCurrentVersion(getProcessVersionText()
							.getText());
					if (version != null) {

						exportToApromore(version, true);

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
		loadProcessList();
		setVisible(true);
	}

	/**
	 * Aktuallisiert die Prozess-Datenfelder mit dem ausgew�hlten
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

			buttonPanel.add(getExportButton());
			buttonPanel.add(getUpdateButton());
			buttonPanel.add(getCancelButton());
			buttonPanel.add(getWopedProgressBar());
		}

		return buttonPanel;
	}

	@Override
	public WopedButton getExportButton() {

		if (exportButton == null) {
			exportButton = new WopedButton();
			exportButton.setText(Messages.getTitle("Button.Export"));
			exportButton.setIcon(Messages.getImageIcon("Button.Export"));
			exportButton.setMnemonic(Messages.getMnemonic("Button.Export"));
			exportButton.setPreferredSize(new Dimension(130, 25));
			exportButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String version = checkCurrentVersion(processVersionText
							.getText());
					if (version != null) {

						if (!(processList.getTable().getSelectedRow() == -1)) {
							exportToApromore(version, true);
						} else {
							exportToApromore(version, false);

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

	public JTextField getProcessIDText() {

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

	public JTextField getProcessNameText() {

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

	public JTextField getProcessOwnerText() {

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

	public JTextField getProcessTypeText() {

		if (processTypeText == null) {
			processTypeText = new JTextField(10);
		}

		return processTypeText;
	}

	private JLabel getProcessFolderLabel() {
		if (processFolderLabel == null) {
			processFolderLabel = new JLabel(
					Messages.getString("Apromore.UI.FolderName"));
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

	@Override
	public JComboBox<String> getProcessFolderBox() {
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

	public void showDialog(String message, String titel, Integer type) {
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

	protected void exportToApromore(final String version, final boolean update) {
		Thread queryThread = new Thread() {
			@Override
			public void run() {

				if (exportWorker != null) {
					exportWorker.cancel(true);
				}

				getWopedPorgressBar().setIndeterminate(true);
				setButtons(false);
				exportWorker = new ExportWorker(ApromoreExportFrame.this,
						version, update);
				exportWorker.execute();
			}
		};
		queryThread.start();
	}

}