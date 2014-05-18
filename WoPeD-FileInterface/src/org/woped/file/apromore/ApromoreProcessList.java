package org.woped.file.apromore;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.woped.apromore.ApromoreAccess;
import org.woped.apromore.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.PNMLExport;
import org.woped.file.PNMLImport;
import org.woped.gui.translations.Messages;

public class ApromoreProcessList {

	private JScrollPane scrollableProcessTable = null;
	private JPanel filterPanel = null;
	private JLabel serverIDLabel = null;
	private JLabel nameFilterLabel = null;
	private JTextField nameFilterText = null;
	private JLabel idFilterLabel = null;
	private JTextField idFilterText = null;
	private JTextField ownerFilterText = null;
	private JLabel ownerFilterLabel = null;
	private JTextField typeFilterText = null;
	private JLabel typeFilterLabel = null;
	private JTextField domainFilterText = null;
	private JLabel domainFilterLabel = null;
	private JPanel processListExportPanel = null;
	private JPanel processListImportPanel = null;
	private JCheckBox beautifyCheckBox = null;
	private String[][] rowData = null;
	private final String[] columnNames = {
			Messages.getString("Apromore.UI.ProcessName"),
			Messages.getString("Apromore.UI.ID"),
			Messages.getString("Apromore.UI.Owner"),
			Messages.getString("Apromore.UI.Type"),
			Messages.getString("Apromore.UI.Domain"),
			Messages.getString("Apromore.UI.Version"),
			Messages.getString("Apromore.UI.Foldername") };
	private DefaultTableModel tabModel = null;
	private JTable table = null;
	private ApromoreAccess aproAccess = null;
	private JDialog frame = null;
	private AbstractApplicationMediator mediator;

	ApromoreProcessList(ApromoreAccess aproAccess, JDialog frame,
			AbstractApplicationMediator mediator) {
		this.aproAccess = aproAccess;
		this.frame = frame;
		this.mediator = mediator;
	}

	public JPanel getProcessListImportPanel(boolean showPnmlOnly) {

		if (processListImportPanel == null) {
			processListImportPanel = new JPanel(new GridBagLayout());

			processListImportPanel.setBorder(BorderFactory
					.createCompoundBorder(BorderFactory
							.createTitledBorder(Messages
									.getString("Apromore.UI.ProcessList")),
							BorderFactory.createEmptyBorder(5, 5, 5, 5)));

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 0;
			processListImportPanel.add(getServerIDLabel(), c);
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 1;
			/*
			 * processListImportPanel.add(getBeautifyCheckBox(), c); c.insets =
			 * new Insets(0, 5, 0, 0); c.gridx = 0; c.gridy = 2;
			 */
			processListImportPanel.add(getScrollableProcessTable(showPnmlOnly),
					c);
		}

		return processListImportPanel;
	}

	public JPanel getProcessListExportPanel(boolean showPnmlOnly) {

		if (processListExportPanel == null) {
			processListExportPanel = new JPanel(new GridBagLayout());

			processListExportPanel.setBorder(BorderFactory
					.createCompoundBorder(BorderFactory
							.createTitledBorder(Messages
									.getString("Apromore.UI.ProcessList")),
							BorderFactory.createEmptyBorder(5, 5, 5, 5)));

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 0;
			processListExportPanel.add(getServerIDLabel(), c);
			c.insets = new Insets(0, 5, 0, 0);
			c.gridx = 0;
			c.gridy = 1;
			processListExportPanel.add(getScrollableProcessTable(showPnmlOnly),
					c);
		}

		return processListExportPanel;
	}

	public JPanel getFilterPanel() {

		if (filterPanel == null) {
			filterPanel = new JPanel();
			GridBagConstraints c = new GridBagConstraints();
			filterPanel.setLayout(new GridBagLayout());
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;

			filterPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.UI.FilterBy")), BorderFactory
							.createEmptyBorder(5, 5, 5, 5)));

			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 5, 0, 0);
			filterPanel.add(getNameFilterLabel(), c);

			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 0);
			filterPanel.add(getNameFilterText(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.insets = new Insets(0, 5, 0, 0);
			filterPanel.add(getIDFilterLabel(), c);

			c.gridx = 1;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 0);
			filterPanel.add(getIDFilterText(), c);

			c.gridx = 2;
			c.gridy = 0;
			c.insets = new Insets(0, 5, 0, 0);
			filterPanel.add(getOwnerFilterLabel(), c);

			c.gridx = 2;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 0);
			filterPanel.add(getOwnerFilterText(), c);

			c.gridx = 3;
			c.gridy = 0;
			c.insets = new Insets(0, 5, 0, 0);
			filterPanel.add(getTypeFilterLabel(), c);

			c.gridx = 3;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 0);
			filterPanel.add(getTypeFilterText(), c);

			c.gridx = 4;
			c.gridy = 0;
			c.insets = new Insets(0, 5, 0, 0);
			filterPanel.add(getDomainFilterLabel(), c);

			c.gridx = 4;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 0);
			filterPanel.add(getDomainFilterText(), c);
		}

		return filterPanel;
	}

	public JTable getTable() {
		return table;
	}

	private JLabel getNameFilterLabel() {

		if (nameFilterLabel == null) {
			nameFilterLabel = new JLabel(
					Messages.getString("Apromore.UI.ProcessName"));
		}

		return nameFilterLabel;
	}

	private JTextField getNameFilterText() {

		if (nameFilterText == null) {
			nameFilterText = new JTextField(6);
			nameFilterText.getDocument().addDocumentListener(
					new DocumentListener() {

						public void insertUpdate(DocumentEvent e) {
							filterAction();
						}

						public void removeUpdate(DocumentEvent e) {
							filterAction();
						}

						public void changedUpdate(DocumentEvent e) {
							filterAction();
						}
					});
		}

		return nameFilterText;
	}

	private JLabel getIDFilterLabel() {

		if (idFilterLabel == null) {
			idFilterLabel = new JLabel(Messages.getString("Apromore.UI.ID"));
		}

		return idFilterLabel;
	}

	private JTextField getIDFilterText() {

		if (idFilterText == null) {
			idFilterText = new JTextField(6);
			idFilterText.getDocument().addDocumentListener(
					new DocumentListener() {

						public void insertUpdate(DocumentEvent e) {
							filterAction();
						}

						public void removeUpdate(DocumentEvent e) {
							filterAction();
						}

						public void changedUpdate(DocumentEvent e) {
							filterAction();
						}

					});
		}

		return idFilterText;
	}

	private JLabel getTypeFilterLabel() {

		if (typeFilterLabel == null) {
			typeFilterLabel = new JLabel(Messages.getString("Apromore.UI.Type"));
		}

		return typeFilterLabel;
	}

	private JTextField getTypeFilterText() {

		if (typeFilterText == null) {
			typeFilterText = new JTextField(6);
			typeFilterText.getDocument().addDocumentListener(
					new DocumentListener() {

						public void insertUpdate(DocumentEvent e) {
							filterAction();
						}

						public void removeUpdate(DocumentEvent e) {
							filterAction();
						}

						public void changedUpdate(DocumentEvent e) {
							filterAction();
						}

					});
		}

		return typeFilterText;
	}

	private JLabel getOwnerFilterLabel() {

		if (ownerFilterLabel == null) {
			ownerFilterLabel = new JLabel(
					Messages.getString("Apromore.UI.Owner"));
		}

		return ownerFilterLabel;
	}

	private JTextField getOwnerFilterText() {

		if (ownerFilterText == null) {
			ownerFilterText = new JTextField(6);
			ownerFilterText.getDocument().addDocumentListener(
					new DocumentListener() {

						public void insertUpdate(DocumentEvent e) {
							filterAction();
						}

						public void removeUpdate(DocumentEvent e) {
							filterAction();
						}

						public void changedUpdate(DocumentEvent e) {
							filterAction();
						}

					});
		}

		return ownerFilterText;
	}

	private JLabel getDomainFilterLabel() {

		if (domainFilterLabel == null) {
			domainFilterLabel = new JLabel(
					Messages.getString("Apromore.UI.Domain"));
		}

		return domainFilterLabel;
	}

	private JTextField getDomainFilterText() {

		if (domainFilterText == null) {
			domainFilterText = new JTextField(6);
			domainFilterText.getDocument().addDocumentListener(
					new DocumentListener() {

						public void insertUpdate(DocumentEvent e) {
							filterAction();
						}

						public void removeUpdate(DocumentEvent e) {
							filterAction();
						}

						public void changedUpdate(DocumentEvent e) {
							filterAction();
						}

					});
		}

		return domainFilterText;
	}

	@SuppressWarnings("serial")
	private class NonEditableTableModel extends DefaultTableModel {

		public NonEditableTableModel(String[] cols) {
			super(null, cols);
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	public JScrollPane getScrollableProcessTable(boolean showPnmlOnly) {

		if (scrollableProcessTable == null) {
			tabModel = new NonEditableTableModel(columnNames);

			table = new JTable();
			table.setShowVerticalLines(true);
			table.setFillsViewportHeight(true);
			scrollableProcessTable = new JScrollPane(table);

			ApplicationMediator.getDisplayUI().setCursor(
					Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				rowData = aproAccess.getProcessList();
				for (String[] s : rowData) {
					if (showPnmlOnly) {
						if (s[3].toLowerCase().contains("pnml")) {
							tabModel.addRow(s);
						}
					} else {
						tabModel.addRow(s);
					}
				}
				table.setModel(tabModel);
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
						tabModel);
				sorter.setComparator(1, new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						return Integer.parseInt(o1) - Integer.parseInt(o2);
					}
				});

				table.setRowSorter(sorter);

				table.getColumnModel().getColumn(0).setPreferredWidth(200);
				table.getColumnModel().getColumn(1).setPreferredWidth(50);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Apromore.UI.Error.GetProcesses"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				frame.dispose();
			}

			int width = table.getPreferredSize().width + 80;
			int height = table.getRowHeight() * table.getRowCount() + 20;
			scrollableProcessTable.setPreferredSize(new Dimension(width,
					height > 180 ? 180 : height));
			ApplicationMediator.getDisplayUI().setCursor(
					new Cursor(Cursor.DEFAULT_CURSOR));
		}

		return scrollableProcessTable;
	}

	private JLabel getServerIDLabel() {
		if (serverIDLabel == null) {
			serverIDLabel = new JLabel(
					Messages.getString("Apromore.UI.CurrentServer")
							+ " "
							+ ConfigurationManager.getConfiguration()
									.getApromoreServerURL());
		}

		return serverIDLabel;
	}

	public JCheckBox getBeautifyCheckBox() {
		if (beautifyCheckBox == null) {
			beautifyCheckBox = new JCheckBox(
					Messages.getString("Apromore.UI.Beautify"));
		}
		return beautifyCheckBox;
	}

	public void clearSelection() {
		table.clearSelection();
	}

	private void filterAction() {
		try {
			if (getIDFilterText().getText().equals(""))
				tabModel.setDataVector(aproAccess.filterProcessList(
						getNameFilterText().getText(), null,
						getOwnerFilterText().getText(), getTypeFilterText()
								.getText(), getDomainFilterText().getText()),
						columnNames);
			else
				tabModel.setDataVector(aproAccess.filterProcessList(
						getNameFilterText().getText(), getIDFilterText()
								.getText(), getOwnerFilterText().getText(),
						getTypeFilterText().getText(), getDomainFilterText()
								.getText()), columnNames);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.IDFieldNotInteger"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}

		tabModel.fireTableStructureChanged();
	}

	public void importAction(boolean beautify) {
		int ind = table.getSelectedRow();
		try {
			if (ind != -1) {
				String processName = (String) table.getModel().getValueAt(ind,
						0);
				PNMLImport pLoader = new PNMLImport(mediator);
				ByteArrayInputStream is = aproAccess.importProcess(ind);
				if (pLoader.run(is, processName + ".pnml")) {
					LoggerManager
							.info(Constants.APROMORE_LOGGER,
									"Model description successfully loaded from Apromore");

					EditorVC editor = (EditorVC) mediator.getUi()
							.getEditorFocus();
					if (beautify) {
						editor.startBeautify(0, 0, 0);
					}
					frame.dispose();
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Apromore.UI.Error.NoRowSelected"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Import"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void exportAction(String owner, String folder, String name,
			String version) {
		try {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PNMLExport pExport = new PNMLExport(mediator);
			pExport.saveToStream((EditorVC) mediator.getUi().getEditorFocus(),
					os);
			aproAccess
					.exportProcess(owner, folder, name, os, "", version, true);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Export"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void updateAction(Integer id, String username, String nativeType,
			String processName, String newVersionNumber) {
		try {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PNMLExport pExport = new PNMLExport(mediator);
			pExport.saveToStream((EditorVC) mediator.getUi().getEditorFocus(),
					os);
			aproAccess.updateProcess(id, username, nativeType, processName,
					newVersionNumber, os);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Export"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void cancelAction() {
		frame.dispose();
	}

	public void updateTable(JTable table, Integer rowid, Integer processId) {
		try {
			tabModel = (DefaultTableModel) table.getModel();
			tabModel.removeRow(rowid);
			rowData = aproAccess.getProcessList();

			for (String[] row : rowData) {
				if (row[1].equals(processId.toString())) {
					tabModel.addRow(row);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNewRow(JTable table) {
		try {
			tabModel = (DefaultTableModel) table.getModel();
			rowData = aproAccess.getProcessList();
			String[] row = rowData[rowData.length - 1];
			tabModel.addRow(row);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getProcessNames() {
		ArrayList<String> names = new ArrayList<String>();
		try {
			rowData = aproAccess.getProcessList();
			for (String[] row : rowData) {
				names.add(row[0]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names;

	}
}