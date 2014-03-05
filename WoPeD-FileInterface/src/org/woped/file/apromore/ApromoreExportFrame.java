package org.woped.file.apromore;

import java.awt.Cursor;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.apromore.ApromoreAccess;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ApromoreExportFrame extends JDialog {
	private static final long serialVersionUID = 1L;

	private WopedButton 				exportButton = null;
	private WopedButton 				updateButton = null;
	private WopedButton 				cancelButton = null;
	private ApromoreAccess 				aproAccess = null;
	private ApromoreProcessList			processList = null;
	private JPanel 						buttonPanel = null;
	private JPanel 						processDataPanel = null;
	private JLabel 						processIDLabel = null;
	private JTextField 					processIDText = null;
	private JLabel 						processNameLabel = null;
	private JTextField 					processNameText = null;
	private JLabel 						processOwnerLabel = null;
	private JTextField 					processOwnerText = null;
	private JLabel 						processVersionLabel = null;
	private JTextField 					processVersionText = null;
	private JLabel 						processTypeLabel = null;
	private JTextField 					processTypeText = null;
	private AbstractApplicationMediator mediator = null;

	public ApromoreExportFrame(AbstractApplicationMediator mediator) {

		this.mediator = mediator;
		aproAccess = new ApromoreAccess();
		processList = new ApromoreProcessList(aproAccess, this, mediator);
		setModal(true);
		setResizable(false);
		initialize();
		setVisible(true);
	}

	private void initialize() {

		ApplicationMediator.getDisplayUI().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			aproAccess.connect();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null,
					Messages.getString("Apromore.UI.Error.Connect"));
			return;
		}

		ApplicationMediator.getDisplayUI().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setTitle(Messages.getString("Apromore.UI.Export.Title"));
		setSize(new Dimension(660, 500));
		int left = (int) ApplicationMediator.getDisplayUI().getLocation().getX();
		int top = (int) ApplicationMediator.getDisplayUI().getLocation().getY();
		setLocation(left + 440, top + 180);
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(processList.getFilterPanel(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(processList.getProcessListPanel(), c);
		processList.getTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					processList.updateAction();
				}
				updateDataFields();
				getUpdateButton().setEnabled(true);
				getExportButton().setEnabled(false);
			}
		});

		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(getProcessDataPanel(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(getButtonPanel(), c);
		getContentPane().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				processList.clearSelection();
				initDataFields();
				getUpdateButton().setEnabled(true);
				getExportButton().setEnabled(true);
			}
		});

		initDataFields();
	}

	private JPanel getProcessDataPanel() {

		if (processDataPanel == null) {
			processDataPanel = new JPanel();
			GridBagConstraints c = new GridBagConstraints();
			processDataPanel.setLayout(new GridBagLayout());
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.weightx = 1;

			processDataPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.UI.ProcessData")),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(5, 0, 0, 0);
			c.fill = GridBagConstraints.NONE;
			processDataPanel.add(getProcessIDLabel(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 0, 0, 0);
			processDataPanel.add(getProcessIDText(), c);

			c.gridx = 3;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(5, 0, 0, 20);
			processDataPanel.add(getProcessOwnerLabel(), c);

			c.gridx = 4;
			c.gridy = 0;
			c.gridwidth = 2;
			c.insets = new Insets(0, 0, 0, 0);
			c.fill = GridBagConstraints.HORIZONTAL;
			processDataPanel.add(getProcessOwnerText(), c);

			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.insets = new Insets(5, 0, 0, 0);
			c.fill = GridBagConstraints.NONE;
			processDataPanel.add(getProcessNameLabel(), c);

			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 5;
			c.insets = new Insets(0, 0, 0, 0);
			c.fill = GridBagConstraints.HORIZONTAL;
			processDataPanel.add(getProcessNameText(), c);

			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			c.insets = new Insets(5, 0, 0, 20);
			c.fill = GridBagConstraints.NONE;
			processDataPanel.add(getProcessTypeLabel(), c);

			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 1;
			c.insets = new Insets(0, 0, 0, 0);
			c.fill = GridBagConstraints.HORIZONTAL;
			processDataPanel.add(getProcessTypeText(), c);

			c.gridx = 3;
			c.gridy = 2;
			c.gridwidth = 1;
			c.insets = new Insets(5, 0, 0, 20);
			c.fill = GridBagConstraints.NONE;
			processDataPanel.add(getProcessVersionLabel(), c);

			c.gridx = 4;
			c.gridy = 2;
			c.gridwidth = 2;
			c.insets = new Insets(0, 0, 0, 0);
			c.fill = GridBagConstraints.HORIZONTAL;
			processDataPanel.add(getProcessVersionText(), c);
		}

		return processDataPanel;
	}

	private JLabel getProcessIDLabel() {

		if (processIDLabel == null) {
			processIDLabel = new JLabel(Messages.getString("Apromore.UI.ID"));
		}

		return processIDLabel;
	}

	private JTextField getProcessIDText() {

		if (processIDText == null) {
			processIDText = new JTextField(5);
		}

		return processIDText;
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

	private JLabel getProcessVersionLabel() {

		if (processVersionLabel == null) {
			processVersionLabel = new JLabel(Messages.getString("Apromore.UI.Version"));
		}

		return processVersionLabel;
	}

	private JTextField getProcessVersionText() {

		if (processVersionText == null) {
			processVersionText = new JTextField(10);
		}

		return processVersionText;
	}


	private JPanel getButtonPanel() {

		if (buttonPanel == null) {
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(getExportButton());
			buttonPanel.add(getUpdateButton());
			getUpdateButton().setEnabled(false);
			buttonPanel.add(getCancelButton());
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
					processList.exportAction(getProcessOwnerText().getText(), getProcessNameText().getText());
				}
			});
		}

		return exportButton;
	}

	private WopedButton getUpdateButton() {

		if (updateButton == null) {
			updateButton = new WopedButton();
			updateButton.setText(Messages.getTitle("Button.Update"));
			updateButton.setIcon(Messages.getImageIcon("Button.Update"));
			updateButton.setMnemonic(Messages.getMnemonic("Button.Update"));
			updateButton.setPreferredSize(new Dimension(130, 25));
			exportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					processList.updateAction();
				}
			});
		}

		return updateButton;
	}

	private WopedButton getCancelButton() {

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

	private void updateDataFields() {
		int ind = processList.getTable().getSelectedRow();
		getProcessNameText().setText("" + processList.getTable().getModel().getValueAt(ind, 0));
		getProcessIDText().setText("" + processList.getTable().getModel().getValueAt(ind, 1));
		getProcessOwnerText().setText("" + processList.getTable().getModel().getValueAt(ind, 2));
		getProcessTypeText().setText("" + processList.getTable().getModel().getValueAt(ind, 3));
	}

	private void initDataFields() {
		getProcessNameText().setText(mediator.getUi().getEditorFocus().getName());
		getProcessIDText().setText("");
		getProcessOwnerText().setText("");
		getProcessTypeText().setText("PNML 1.3.2");
	}
}