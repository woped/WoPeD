package org.woped.file.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.woped.apromore.ApromoreAccess;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.utilities.LoggerManager;
import org.woped.file.Constants;
import org.woped.file.PNMLImport;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ApromoreImportFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JTextField 			txtName; 
	private JTextField 			txtID; 
    private JTextField 			txtType; 
	private JTextField 			txtOwner; 
	private JLabel 				lblFilterBy; 
	private JLabel 				lblPnmlName; 
	private JLabel 				lblID; 
	private JLabel 				lblType; 
	private JLabel 				lblOwner; 
	private JLabel 				lblResults; 
	private JLabel 				lblSelectProcess; 
	 
	private String[][] 			rowData;
	private final String[] 		columnNames = {
									Messages.getString("Apromore.Import.UI.ProcessName"),
									Messages.getString("Apromore.Import.UI.ID"),
									Messages.getString("Apromore.Import.UI.Owner"),
									Messages.getString("Apromore.Import.UI.Type"),
									Messages.getString("Apromore.Import.UI.Versions") };
	private DefaultTableModel 	tabModel;
	private JTable 				table;
	private WopedButton 		importButton = null;
	private WopedButton 		cancelButton = null;
	private WopedButton 		filterButton = null;
	private PNMLImport 			pLoader;
	private ApromoreAccess 		aproAccess;
	private JPanel 				contentPanel = null;
	private JPanel 				buttonPanel = null;
	private JScrollPane 		scrollableProcessTable = null;
	private JPanel 				filterPanel = null;
	private JLabel 				serverIDLabel;

	private JLabel lblSelectVersion;

	private JComboBox<String> comboBox;

	public ApromoreImportFrame(PNMLImport pr) {

		pLoader = pr;
		aproAccess = new ApromoreAccess();
		setModal(true);
		initialize();
	}

	private void initialize() {

		try {
			aproAccess.connect();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null,
					Messages.getString("Apromore.Export.UI.Error.AproNoConn"));
			return;
		}

		setTitle(Messages.getString("Apromore.Import.UI.Title"));
		Dimension frameSize = new Dimension(800, 600);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int top = (screenSize.height - frameSize.height) / 2;
		int left = (screenSize.width - frameSize.width) / 2;
		setSize(frameSize);
		setLocation(top, left);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getContentPanel(), BorderLayout.NORTH);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		setVisible(true);
	}

	private JPanel getContentPanel() {

		if (contentPanel == null) {
			contentPanel = new JPanel(new BorderLayout());
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
//			contentPanel.add(getFilterPanel(), BorderLayout.NORTH);
			contentPanel.add(getServerIDLabel(), BorderLayout.NORTH);
			contentPanel.add(getScrollableProcessTable(), BorderLayout.SOUTH);
			this.setCursor(Cursor.getDefaultCursor());
		}
		return contentPanel;
	}

/*	private JPanel getFilterPanel() {
		
		if (filterPanel == null) {
			filterPanel = new JPanel();	
			lblFilterBy = new JLabel(Messages.getString("Apromore.Import.UI.FilterBy")); 
		
			lblFilterBy.setFont(DefaultStaticConfiguration.DEFAULT_LABEL_BOLDFONT);
			lblFilterBy.setBounds(10, 0, 220, 20);
			filterPanel.add(lblFilterBy);
		
			lblPnmlName = new JLabel(Messages.getString("Apromore.Import.UI.Name"));
			lblPnmlName.setBounds(25, 10, 90, 20);
			filterPanel.add(lblPnmlName);
		
			txtName = new JTextField(); 
			txtName.setBounds(120, 25, 90, 20);
			filterPanel.add(txtName); 
		
			lblID = new JLabel(Messages.getString("Apromore.Import.UI.ID"));
			lblID.setBounds(10, 50, 90, 20); 
			filterPanel.add(lblID);
		
			txtID = new JTextField(); 
			txtID.setBounds(120, 50, 90, 20);
			filterPanel.add(txtID); 
		
			lblType = new JLabel(Messages.getString("Apromore.Import.UI.Type"));
			lblType.setBounds(10, 75, 90, 20); 
			filterPanel.add(lblType);
		
			txtType = new JTextField(); 
			txtType.setBounds(120, 75, 90, 20);
			filterPanel.add(txtType); 
		
			lblOwner = new JLabel(Messages.getString("Apromore.Import.UI.Owner"));
			lblOwner.setBounds(10, 100, 90, 20); 
			filterPanel.add(lblOwner);
		
			txtOwner = new JTextField();
			txtOwner.setBounds(120, 100, 90, 20); 
			filterPanel.add(txtOwner);
		
			lblResults = new JLabel(Messages.getString("Apromore.Import.UI.Results")); 
			lblResults.setFont(DefaultStaticConfiguration.DEFAULT_HUGELABEL_BOLDFONT);
			lblResults.setBounds(10, 125, 90, 20);
			filterPanel.add(lblResults);
		
			lblSelectProcess = new JLabel(Messages.getString("Apromore.Import.UI.SelectProcess"));
			lblSelectProcess.setBounds(120, 125, 90, 20);
			filterPanel.add(lblSelectProcess);
		 
			filterPanel.add(getFilterButton());
		
			lblSelectVersion = new JLabel();
			lblSelectVersion.setForeground(Color.RED);
			lblSelectVersion.setBounds(10, 150, 90, 20);
//			lblSelectVersion.setVisible(false);
			filterPanel.add(lblSelectVersion);
		
			comboBox = new JComboBox<String>(); 
			comboBox.setBounds(120,150, 90, 20); 
//			comboBox.setVisible(false); 
			filterPanel.add(comboBox);
		}
		return filterPanel;
	}*/

	private JScrollPane getScrollableProcessTable() {

		if (scrollableProcessTable == null) {
			tabModel = new DefaultTableModel(null, columnNames);
			rowData = aproAccess.getProcessList();

			for (String[] s : rowData) {
				tabModel.addRow(s);
			}

			table = new JTable(tabModel);
			table.setShowVerticalLines(false);

			scrollableProcessTable = new JScrollPane(table);
			scrollableProcessTable.setBounds(10, 252, 764, 194);
		}

		return scrollableProcessTable;
	}

	private JLabel getServerIDLabel() {
		if (serverIDLabel == null) {
			serverIDLabel = new JLabel(Messages
					.getString("Apromore.Import.UI.Serversource")
					+ " "
					+ ConfigurationManager.getConfiguration()
							.getApromoreServerURL());
			serverIDLabel.setFont(DefaultStaticConfiguration.DEFAULT_HUGELABEL_FONT);
		}
		
		return serverIDLabel;
	}
	
	
	private JPanel getButtonPanel() {

		if (buttonPanel == null) {
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(getImportButton());
			buttonPanel.add(getCancelButton());
		}
		return buttonPanel;
	}
	
	private WopedButton getFilterButton() {
		
		if (filterButton == null) {
			filterButton = new WopedButton(Messages.getString("Apromore.Import.UI.Filter"));
			filterButton.addActionListener(new ActionListener() {
	
				public void actionPerformed(ActionEvent e) { 
					filterAction();
				}
			});
	
			filterButton.setBounds(10, 164, 89, 23);
		}
		
		return filterButton;
	}

	private WopedButton getImportButton() {

		if (importButton == null) {
			importButton = new WopedButton();
			importButton.setText(Messages.getTitle("Button.Import"));
			importButton.setIcon(Messages.getImageIcon("Button.Import"));
			importButton.setMnemonic(Messages.getMnemonic("Button.Import"));
			importButton.setPreferredSize(new Dimension(100, 25));
			importButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					importAction();
				}
			});
		}

		return importButton;
	}

	private WopedButton getCancelButton() {

		if (cancelButton == null) {
			cancelButton = new WopedButton();
			cancelButton.setText(Messages.getTitle("Button.Cancel"));
			cancelButton.setIcon(Messages.getImageIcon("Button.Cancel"));
			cancelButton.setMnemonic(Messages.getMnemonic("Button.Cancel"));
			cancelButton.setPreferredSize(new Dimension(100, 25));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelAction();
				}
			});
		}
		return cancelButton;
	}

	private void filterAction() {
		try { 
			if (txtID.getText().equals("")) 
				tabModel.setDataVector(aproAccess.filterProcessList(txtName.getText(), 
																	0,
																	txtOwner.getText(), 
																	txtType.getText()), 
																	columnNames); 
			else
				tabModel.setDataVector(aproAccess.filterProcessList(txtName.getText(), 
																	Integer.parseInt(txtID.getText()),
																	txtOwner.getText(), 
																	txtType.getText()), 
																	columnNames); 
			} 
		catch (Exception e) { 
				JOptionPane.showMessageDialog( null,
							Messages.getString("Apromore.Import.UI.Error.IDFieldNotInteger"),
							Messages.getString("Apromore.Import.UI.Error.IDFieldNotIntegerTitle"),
							JOptionPane.ERROR_MESSAGE); 
			} 
		tabModel.fireTableStructureChanged();
	}
	
	private void importAction() {
		try {
			/*
			 * lblSelectVersion.setText(Messages
			 * .getString("Apromore.Import.UI.SelectVersionMsg"));
			 * lblSelectVersion.setVisible(false); comboBox.setVisible(false);
			 * 
			 * int row = table.getSelectedRow();
			 * 
			 * comboBox.removeAllItems(); String versionen = "" +
			 * table.getModel().getValueAt(row, 4); StringTokenizer st = new
			 * StringTokenizer(versionen, ";"); String[] comboItems = new
			 * String[st.countTokens()]; int i = 0; while (st.hasMoreTokens()) {
			 * comboItems[i] = st.nextToken().trim(); i++; } for (int j = i - 1;
			 * j >= 0; j--) { comboBox.addItem(comboItems[j]); } if (i > 1) {
			 * lblSelectVersion.setVisible(true); comboBox.setVisible(true); }
			 * else { lblSelectVersion.setText(Messages
			 * .getString("Apromore.Import.UI.ImportThisID") + " " +
			 * table.getModel().getValueAt(row, 1) + "?");
			 * lblSelectVersion.setVisible(true); }
			 */
			int ind = getSelectedID();
			if (ind != -1) {
				if (pLoader.run(aproAccess.importProcess(ind))) {
					LoggerManager.info(Constants.FILE_LOGGER,
							"Model description loaded from Apromore");
					dispose();
				} else {
					JOptionPane
							.showMessageDialog(null, Messages.getString(
									"File.Error.FileOpen.Text" + ": AProMoRe Import"), Messages
									.getString("File.Error.FileOpen.Title"),
									JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("Apromore.Import.UI.Error.NoRowSelected"),
								Messages.getString("Apromore.Import.UI.Error.NoRowSelectedTitle"),
								JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.Import.UI.Error.Import"),
					Messages.getString("Apromore.Import.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancelAction() {
		dispose();
	}

	private int getSelectedID() {
		return table.getSelectedRow();
	}
}
