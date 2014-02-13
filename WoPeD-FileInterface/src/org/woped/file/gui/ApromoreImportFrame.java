package org.woped.file.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
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

import org.woped.apromore.ApromoreAccess;
import org.woped.apromore.Constants;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.file.PNMLImport;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class ApromoreImportFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private String[ ][ ] 		rowData = null;
	private final String[ ] 	columnNames = {
									Messages.getString("Apromore.Import.UI.ProcessName"),
									Messages.getString("Apromore.Import.UI.ID"),
									Messages.getString("Apromore.Import.UI.Owner"),
									Messages.getString("Apromore.Import.UI.Type"),
									Messages.getString("Apromore.Import.UI.Versions") };
	private DefaultTableModel 	tabModel = null;
	private JTable 				table = null;
	private WopedButton 		importButton = null;
	private WopedButton 		cancelButton = null;
	private ApromoreAccess 		aproAccess = null;
	private JPanel 				contentPanel = null;
	private JPanel 				buttonPanel = null;
	private JScrollPane 		scrollableProcessTable = null;
	private JPanel 				filterPanel = null;
	private JLabel 				serverIDLabel = null;
	private JLabel 				nameFilterLabel = null;
	private JTextField 			nameFilterText = null;
	private JLabel 				idFilterLabel = null;
	private JTextField 			idFilterText = null;
	private JTextField 			ownerFilterText = null;
	private JLabel 				ownerFilterLabel = null;
	private JTextField 			typeFilterText = null;
	private JLabel 				typeFilterLabel = null;
	private JPanel 				processListPanel = null;
	private AbstractApplicationMediator mediator;

	public ApromoreImportFrame(AbstractApplicationMediator mediator) {

		this.mediator = mediator;
		aproAccess = new ApromoreAccess();
		setModal(true);
		initialize();
	}

	private void initialize() {

		ApplicationMediator.getDisplayUI().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			aproAccess.connect();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null,
					Messages.getString("Apromore.Export.UI.Error.AproNoConn"));
			return;
		}

		ApplicationMediator.getDisplayUI().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setTitle(Messages.getString("Apromore.Import.UI.Title"));
		setSize(new Dimension(520, 390));
		int left = (int)ApplicationMediator.getDisplayUI().getLocation().getX();
		int top = (int)ApplicationMediator.getDisplayUI().getLocation().getY();
		setLocation(left+400, top+180);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getContentPanel(), BorderLayout.NORTH);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		setVisible(true);
	}

	private JPanel getContentPanel() {

		if (contentPanel == null) {
			contentPanel = new JPanel(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 4;
			c.gridheight = 2;
			contentPanel.add(getFilterPanel(), c);
			c.gridx = 0;
			c.gridy = 4;
			c.gridwidth = 4;
			c.gridheight = 8;
			contentPanel.add(getProcessListPanel(), c);
		}
		
		return contentPanel;
	}
	
	private JPanel getProcessListPanel() {
		
		if (processListPanel == null) {
			processListPanel = new JPanel(new GridBagLayout());

			processListPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.Import.UI.Serversource")),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(5, 5, 5, 5);
			c.gridx = 0;
			c.gridy = 0;
			processListPanel.add(getServerIDLabel(), c);
			c.insets = new Insets(0, 5, 0, 0);
			c.gridx = 0;
			c.gridy = 1;
			processListPanel.add(getScrollableProcessTable(), c);
		}
		
		return processListPanel;
	}

	private JLabel getNameFilterLabel() {
		
		if (nameFilterLabel == null) {
			nameFilterLabel = new JLabel(Messages.getString("Apromore.Import.UI.Name"));
		}
		
		return nameFilterLabel;
	}

	private JTextField getNameFilterText() {
		
		if (nameFilterText == null) {
			nameFilterText = new JTextField();
			nameFilterText.getDocument().addDocumentListener(new DocumentListener() {

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
			idFilterLabel = new JLabel(Messages.getString("Apromore.Import.UI.ID"));
		}
		
		return idFilterLabel;
	}

	private JTextField getIDFilterText() {
		
		if (idFilterText == null) {
			idFilterText = new JTextField();
			idFilterText.getDocument().addDocumentListener(new DocumentListener() {

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
			typeFilterLabel = new JLabel(Messages.getString("Apromore.Import.UI.Type"));
		}
		
		return typeFilterLabel;
	}

	private JTextField getTypeFilterText() {
		
		if (typeFilterText == null) {
			typeFilterText = new JTextField();
			typeFilterText.getDocument().addDocumentListener(new DocumentListener() {

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
			ownerFilterLabel = new JLabel(Messages.getString("Apromore.Import.UI.Owner"));
		}
		
		return ownerFilterLabel;
	}

	private JTextField getOwnerFilterText() {
		
		if (ownerFilterText == null) {
			ownerFilterText = new JTextField();
			ownerFilterText.getDocument().addDocumentListener(new DocumentListener() {

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

	private JPanel getFilterPanel() {
		
		if (filterPanel == null) {
			filterPanel = new JPanel();	
			GridBagConstraints c = new GridBagConstraints();
			filterPanel.setLayout(new GridBagLayout());
	        c.anchor = GridBagConstraints.FIRST_LINE_START;
	        c.fill = GridBagConstraints.HORIZONTAL;
	        c.weightx = 1;		
			
			filterPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Apromore.Import.UI.FilterBy")),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			
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

		}
		
		return filterPanel;
	}

	private JScrollPane getScrollableProcessTable() {

		if (scrollableProcessTable == null) {
			tabModel = new DefaultTableModel(null, columnNames);
			
			table = new JTable(tabModel);
			table.setShowVerticalLines(true);
			table.setFillsViewportHeight(true);

			scrollableProcessTable = new JScrollPane(table);
			
			int width = table.getPreferredSize().width + 80;
			int height = table.getRowHeight() * table.getRowCount() + 20;		
			scrollableProcessTable.setPreferredSize(new Dimension(width, height > 180 ? 180 : height));
			
			ApplicationMediator.getDisplayUI().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				rowData = aproAccess.getProcessList();
				for (String[] s : rowData) {
					tabModel.addRow(s);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("Apromore.GetProcesses.Error"),
						Messages.getString("Apromore.Import.UI.Error.Title"),
						JOptionPane.ERROR_MESSAGE);
				dispose();
			}
			ApplicationMediator.getDisplayUI().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		return scrollableProcessTable;
	}

	private JLabel getServerIDLabel() {
		if (serverIDLabel == null) {
			serverIDLabel = new JLabel(Messages.getString("Apromore.Import.UI.CurrentServer") + 
							ConfigurationManager.getConfiguration().getApromoreServerURL());
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
			if (getIDFilterText().getText().equals("")) 
				tabModel.setDataVector(aproAccess.filterProcessList(getNameFilterText().getText(), 
																	0,
																	getOwnerFilterText().getText(), 
																	getTypeFilterText().getText()), 
																	columnNames); 
			else
				tabModel.setDataVector(aproAccess.filterProcessList(getNameFilterText().getText(), 
																	Integer.parseInt(getIDFilterText().getText()),
																	getOwnerFilterText().getText(), 
																	getTypeFilterText().getText()), 
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
			int ind = table.getSelectedRow();
			String processName = (String)table.getModel().getValueAt(ind, 0);
			
			if (ind != -1) {
				PNMLImport pLoader = new PNMLImport(mediator);
				if (pLoader.run(aproAccess.importProcess(ind), processName + ".pnml")) {
					LoggerManager.info(Constants.APROMORE_LOGGER, "Model description loaded from Apromore");
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
}
