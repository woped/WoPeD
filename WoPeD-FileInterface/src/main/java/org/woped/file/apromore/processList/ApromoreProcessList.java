package org.woped.file.apromore.processList;

import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.woped.apromore.ApromoreAccess;
import org.woped.apromore.Constants;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.PNMLExport;
import org.woped.file.PNMLImport;
import org.woped.file.apromore.tree.ApromoreFolderTree;
import org.woped.gui.translations.Messages;

import net.coderazzi.filters.Filter;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

/**
 */
public class ApromoreProcessList {
	private final String[] columnNames = {
			Messages.getString("Apromore.UI.ProcessName"),
			Messages.getString("Apromore.UI.ID"),
			Messages.getString("Apromore.UI.Owner"),
			Messages.getString("Apromore.UI.Type"),
			Messages.getString("Apromore.UI.Domain"),
			Messages.getString("Apromore.UI.Version"),
			Messages.getString("Apromore.UI.FolderName") };

	private JDialog frame = null;
	private JCheckBox beautifyCheckBox = null;

	private AbstractApplicationMediator mediator = null;;
	private ApromoreAccess aproAccess = null;
	private String[][] rowData = null;

	private JTable table = null;
	private NonEditableTableModel tabModel = null;

	private JScrollPane scrollableProcessTable;

	private TableFilterHeader filterHeader;

	private Filter userFilter;

	public ApromoreProcessList(ApromoreAccess aproAccess, JDialog frame,
							   AbstractApplicationMediator mediator) {
		this.aproAccess = aproAccess;
		this.frame = frame;
		this.mediator = mediator;
	}

	public boolean getModelFromServer(boolean beautify) {

		int ind = table.getSelectedRow();
		try {
			if (ind != -1) {
				new PNMLImport(mediator);
			} else {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Apromore.UI.Error.NoRowSelected"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			aproAccess.importProcess(ind, false, true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Import"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
			return false;

		}
		return beautify;
	}

	public boolean importAction(boolean beautify, boolean edgesToPlaces, boolean tasksToTransitions) {

		int ind = table.getSelectedRow();
		try {
			if (ind != -1) {
				String processName = (String) table.getModel().getValueAt(ind + 1,
						0);
				PNMLImport pLoader = new PNMLImport(mediator);
				ByteArrayInputStream is = aproAccess.importProcess(ind, edgesToPlaces, tasksToTransitions);

				if (pLoader.run(is, processName + ".pnml")) {
					LoggerManager
							.info(Constants.APROMORE_LOGGER,
									"Model description successfully loaded from Apromore");

					EditorVC editor = (EditorVC) mediator.getUi()
							.getEditorFocus();

					if (beautify) {
						editor.startBeautify(0, 0, 0);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Apromore.UI.Error.NoRowSelected"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				return false;

			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Import"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
			return false;

		}
		return true;
	}

	public boolean getImportModelFromServer(boolean beautify) {
		int ind = table.getSelectedRow();
		try {
			if (ind != -1) {
				new PNMLImport(mediator);
				aproAccess.importProcess(ind,false,true);
			} else {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Apromore.UI.Error.NoRowSelected"),
						Messages.getString("Apromore.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				return false;

			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Import"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
			return false;

		}
		return true;
	}

	public JScrollPane getScrollableProcessTable(boolean showPnmlOnly) {

		if (scrollableProcessTable == null) {
			ApplicationMediator.getDisplayUI().setCursor(
					Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			tabModel = new NonEditableTableModel(columnNames);
			table = new JTable();
			table.setShowVerticalLines(true);
			table.setFillsViewportHeight(true);
			table.setModel(tabModel); // set table header

			scrollableProcessTable = new JScrollPane(table);
			filterHeader = new TableFilterHeader(table, AutoChoices.ENABLED);

			String[] defaultList = new String[1];
			defaultList[0] = ApromoreFolderTree.TOP_NODE_NAME;
			setFilter(defaultList);
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

			// Hide Foldername Column
			table.getColumnModel().getColumn(6).setMinWidth(0);
			table.getColumnModel().getColumn(6).setMaxWidth(0);
			table.getColumnModel().getColumn(6).setWidth(0);
		}

		ApplicationMediator.getDisplayUI().setCursor(
				new Cursor(Cursor.DEFAULT_CURSOR));

		return scrollableProcessTable;
	}

	@SuppressWarnings("serial")
	private class NonEditableTableModel extends DefaultTableModel {

		public NonEditableTableModel(String[] cols) {
			super(null, cols);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	public void clearProccesList() {
		int rows = tabModel.getRowCount();
		for (int i = rows - 1; i >= 0; i--) {
			tabModel.removeRow(i);
		}
	}

	public String[][] getRowData() {
		try {
			return aproAccess.getProcessList();
		} catch (Exception e) {
			return null;
		}
	}

	public void setList(String rowData[][]) {
		clearProccesList();
		if (rowData != null) {
			tabModel.addRow(new String[tabModel.getColumnCount()]);
			for (String[] s : rowData) {

				if (!s[0].equals("")) {
					tabModel.addRow(s);
				}
			}
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
							 String processName, String newVersionNumber) throws Exception {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PNMLExport pExport = new PNMLExport(mediator);
		pExport.saveToStream((EditorVC) mediator.getUi().getEditorFocus(), os);
		aproAccess.updateProcess(id, username, nativeType, processName,
				newVersionNumber, os);
	}

	public void updateTable(JTable table, Integer rowid, Integer processId) {
		try {
			tabModel = (NonEditableTableModel) table.getModel();
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

	public JTable getTable() {
		return table;
	}

	public void cancelAction() {
		frame.dispose();
	}

	public void clearSelection() {
		table.clearSelection();
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

	public void addNewRow(JTable table) {
		try {
			tabModel = (NonEditableTableModel) table.getModel();
			rowData = aproAccess.getProcessList();
			String[] row = rowData[rowData.length - 1];
			tabModel.addRow(row);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFilter(final String[] filterFolder) {

		if (userFilter != null) {
			filterHeader.removeFilter(userFilter);
		}
		userFilter = new Filter() {
			@Override
			public boolean include(Entry entry) {

				boolean found = false;
				for (int i = 0; i < filterFolder.length; i++) {

					if (entry.getValue(0) != null && entry.getValue(6) != null) {

						if (entry.getValue(6).toString()
								.equals(filterFolder[i])
								|| filterFolder[i]
								.equalsIgnoreCase(ApromoreFolderTree.TOP_NODE_NAME)) {
							found = true;
							break;
						} else {
							found = false;
						}

					} else {
						found = false;
					}
				}

				return found;
			}
		};

		filterHeader.addFilter(userFilter);
		userFilter.setEnabled(true);
	}

}