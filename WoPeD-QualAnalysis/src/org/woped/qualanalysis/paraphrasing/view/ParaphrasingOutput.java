package org.woped.qualanalysis.paraphrasing.view;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.paraphrasing.action.ButtonListener;
import org.woped.qualanalysis.paraphrasing.action.SelectionListener;
import org.woped.translations.Messages;

public class ParaphrasingOutput{

	private JPanel mainPanel;
	private IEditor editor = null;

	private JTable table = null;
	private DefaultTableModel defaultTableModel = null;
	private SelectionListener selectionListener = null;
	private JScrollPane scrollP = null;
	private JLabel labelLoading = null;
	ParaphrasingPanel paraphrasingPanel = null;

	/**
	 * Constructor for creating the table that contains
	 * all descriptions 
	 * 
	 * @author Martin Meitz
	 * 
	 */
	public ParaphrasingOutput(ParaphrasingPanel paraphrasingPanel){
		
		this.mainPanel = new JPanel();
		this.editor = paraphrasingPanel.getEditor();
		this.paraphrasingPanel = paraphrasingPanel;
		this.mainPanel.setBorder(BorderFactory
                 .createCompoundBorder(BorderFactory.createTitledBorder(
                 Messages.getString("Paraphrasing.Output.Description")), 
                 BorderFactory.createEmptyBorder()));
		this.mainPanel.setLayout(new BorderLayout());
		this.labelLoading = new JLabel("",Messages.getImageIcon("Paraphrasing.Output.Load.Animation"), 
				JLabel.CENTER);

		createTable();
	}
	
	public JPanel getMainPanel(){
		return this.mainPanel;
	}
	
	/**
	 * Creates an empty table and defines its appearance
	 * 
	 * @author Martin Meitz
	 * 
	 */
	private void createTable(){	
		String[] columnNames = {"Id", "Text"};
			
		this.defaultTableModel = new DefaultTableModel();
		this.defaultTableModel.setColumnIdentifiers(columnNames);
		
		this.table = new JTable(this.defaultTableModel){  
			public boolean isCellEditable(int row,int column){  
				    return false;  
				  }
		};
		this.table.setTableHeader(null);
		this.table.setShowGrid(false);
		this.table.setFillsViewportHeight(true);

		
		//hide first column
		this.table.getColumnModel().getColumn(0).setMaxWidth(0);
		this.table.getColumnModel().getColumn(0).setMinWidth(0);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(0);
	        
		
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // nur einzelne Zeilen
		this.table.setDefaultRenderer(Object.class, new CustomCellRenderer());

		this.selectionListener = new SelectionListener(this);
		
		scrollP= new JScrollPane(table);
		this.mainPanel.add(scrollP, BorderLayout.CENTER);
		

		this.mainPanel.validate();
		this.mainPanel.repaint();
		
		
	}
	
	/**
	 * Deactivates the output table and
	 * activates the loading animation
	 * 
	 * @author Martin Meitz
	 *
	 * 
	 */
	public void setAnimationVisible(){
		this.mainPanel.remove(scrollP);
		this.mainPanel.add(labelLoading, BorderLayout.CENTER);	
		this.mainPanel.validate();
		this.mainPanel.repaint();
	}
	
	
	/**
	 * Deactivates the loading animation and
	 * activates the output table
	 * 
	 * @author Martin Meitz
	 *
	 * 
	 */
	public void setTableVisible(){
		this.mainPanel.remove(labelLoading);
		this.mainPanel.add(scrollP, BorderLayout.CENTER);
		this.mainPanel.validate();
		this.mainPanel.repaint();
	}
	
	
	/**
	 * Activates all different actionlisteners
	 * for the table and editor
	 * 
	 * @author Martin Meitz
	 *
	 * 
	 */
	public void addListeners(){
		this.editor.getGraph().addMouseListener(this.selectionListener);
		this.editor.getGraph().addKeyListener(this.selectionListener);
		this.table.addMouseListener(this.selectionListener);
		this.table.addKeyListener(this.selectionListener);

	}
	
	
	/**
	 * deactivates all different actionlisteners
	 * for the table and editor
	 * 
	 * @author Martin Meitz
	 *
	 * 
	 */
	public void removeListeners(){
		this.editor.getGraph().removeMouseListener(this.selectionListener);
		this.editor.getGraph().removeKeyListener(this.selectionListener);
		this.table.removeMouseListener(this.selectionListener);
		this.table.removeKeyListener(this.selectionListener);
		this.selectionListener.clearHighlighting();
	}
	
	
	public IEditor getEditor(){
		return this.editor;
	}
	
	
	/**
	 * returns the current table
	 * 
	 * @author Martin Meitz
	 * @return JTable
	 *
	 * 
	 */
	public JTable getTable(){
		return this.table;
	}
	
	
	/**
	 * returns the current table model
	 * 
	 * @author Martin Meitz
	 * @return DefaultTableModel
	 * 
	 */
	public DefaultTableModel getDefaultTableModel(){
		return this.defaultTableModel;
	}
		
	public void addRow(String[] row){
		this.defaultTableModel.addRow(row);
	}
	
	/**
	 * Writes all descriptions from the table to the
	 * ElementContainer
	 * 
	 * @author Martin Meitz
	 * 
	 */
	public void updateElementContainer(){
		this.editor.getModelProcessor().getElementContainer().getParaphrasingModel().deleteValues();
		for(int i = 0; i < defaultTableModel.getRowCount(); i++){
			this.editor.getModelProcessor().getElementContainer().getParaphrasingModel().addElement((String)this.defaultTableModel.getValueAt(i, 0), (String)this.defaultTableModel.getValueAt(i, 1));	
		}
		
	}
	
	
	/**
	 * returns the corresponding paraphrasing panel
	 * 
	 * @author Martin Meitz
	 * @return ParaphrasingPanel
	 * 
	 */
	public ParaphrasingPanel getParaphrasingPanel(){
		return this.paraphrasingPanel;
	}
	
	
}
