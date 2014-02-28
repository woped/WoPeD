package org.woped.file.gui;

import java.awt.BorderLayout;
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
import java.io.InputStream;
import java.util.Comparator;

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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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
									Messages.getString("Apromore.UI.ProcessName"),
									Messages.getString("Apromore.UI.ID"),
									Messages.getString("Apromore.UI.Owner"),
									Messages.getString("Apromore.UI.Type"),
									Messages.getString("Apromore.UI.Version") };
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
					Messages.getString("Apromore.Error.Connect"));
			return;
		}

		ApplicationMediator.getDisplayUI().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setTitle(Messages.getString("Apromore.UI.Import.Title"));
		setSize(new Dimension(600, 410));
		int left = (int)ApplicationMediator.getDisplayUI().getLocation().getX();
		int top = (int)ApplicationMediator.getDisplayUI().getLocation().getY();
		setLocation(left+400, top+180);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getContentPanel(), BorderLayout.NORTH);
		getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		getContentPane().addMouseListener(new MouseAdapter() {
			   public void mouseClicked(MouseEvent e) {
				   table.clearSelection();
			   	}
				});
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
							.getString("Apromore.UI.ProcessList")),
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
			nameFilterLabel = new JLabel(Messages.getString("Apromore.UI.ProcessName"));
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
			idFilterLabel = new JLabel(Messages.getString("Apromore.UI.ID"));
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
			typeFilterLabel = new JLabel(Messages.getString("Apromore.UI.Type"));
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
			ownerFilterLabel = new JLabel(Messages.getString("Apromore.UI.Owner"));
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
							.getString("Apromore.UI.FilterBy")),
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

	@SuppressWarnings("serial")
	private class NonEditableTableModel extends DefaultTableModel {

		public NonEditableTableModel(String[] cols) {
			super(null, cols);
		}
	
		public boolean isCellEditable(int row, int column) {
	        return false;
	    }
	}
	
	private JScrollPane getScrollableProcessTable() {

		if (scrollableProcessTable == null) {
			tabModel = new NonEditableTableModel(columnNames);
			
			table = new JTable();
			table.setShowVerticalLines(true);
			table.setFillsViewportHeight(true);
			table.addMouseListener(new MouseAdapter() {
				   public void mouseClicked(MouseEvent e) {
					      if (e.getClickCount() == 2) {
					         JTable target = (JTable)e.getSource();
					         importAction(target.getSelectedRow());
					         }
					   }
					});

			scrollableProcessTable = new JScrollPane(table);
			
			ApplicationMediator.getDisplayUI().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				rowData = aproAccess.getProcessList();
				for (String[] s : rowData) {
					tabModel.addRow(s);
				}
				table.setModel(tabModel);
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tabModel);
				sorter.setComparator(1, new Comparator<String>() {
		             
		            @Override
		            public int compare(String o1, String o2)
		            {
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
				dispose();
			}
			
			int width = table.getPreferredSize().width + 80;
			int height = table.getRowHeight() * table.getRowCount() + 20;		
			scrollableProcessTable.setPreferredSize(new Dimension(width, height > 180 ? 180 : height));			
			ApplicationMediator.getDisplayUI().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		return scrollableProcessTable;
	}

	private JLabel getServerIDLabel() {
		if (serverIDLabel == null) {
			serverIDLabel = new JLabel(Messages.getString("Apromore.UI.CurrentServer") + 
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
			importButton.setPreferredSize(new Dimension(130, 25));
			importButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					importAction(table.getSelectedRow());
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
			cancelButton.setPreferredSize(new Dimension(130, 25));
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
				e.printStackTrace();
				JOptionPane.showMessageDialog( null,
							Messages.getString("Apromore.UI.Error.IDFieldNotInteger"),
							Messages.getString("Apromore.UI.Error.Title"),
							JOptionPane.ERROR_MESSAGE); 
		} 
		
		tabModel.fireTableStructureChanged();
	}
	
	private void importAction(int ind) {
		try {
			if (ind != -1) {
				String processName = (String)table.getModel().getValueAt(ind, 0);
				PNMLImport pLoader = new PNMLImport(mediator);
				InputStream is = aproAccess.importProcess(ind);
				if (pLoader.run(is, processName + ".pnml")) {
					LoggerManager.info(Constants.APROMORE_LOGGER, "Model description successfully loaded from Apromore");
					dispose();
					return;
				} 
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("Apromore.UI.Error.NoRowSelected"),
								Messages.getString("Apromore.UI.Error.Title"),
								JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			JOptionPane.showMessageDialog(null,
					Messages.getString("Apromore.UI.Error.Import"),
					Messages.getString("Apromore.UI.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancelAction() {
		dispose();
	}
}
