package org.woped.qualanalysis.paraphrasing.editing;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.translations.Messages;


public class TextualDescriptionDialog extends JDialog implements ActionListener, KeyListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel = null;
	private JLabel idLabel = null;
	private JLabel textLabel = null;
	private JTextField id = null;
	private JTextArea text = null;
	private JButton buttonOk = null;
	private JButton buttonCancel = null;
	JScrollPane textScrollPane = null;
	private IEditor editor = null;
	private JTable table = null;
	DefaultTableModel defaultTableModel = null;
	private String type = null;  //either new or edit
	private int row = -1;
	private JTable availableIdsTable = null;
	private DefaultTableModel availableIdsTableModel = null;
	private ImageIcon cancelIcon = Messages.getImageIcon("Paraphrasing.TextualDescriptionDialog.Notification.Cancel");
	
	public TextualDescriptionDialog(IEditor editor, JTable table, DefaultTableModel defaultTableModel, String type){
		super();
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.editor = editor;
		this.table = table;
		this.defaultTableModel = defaultTableModel;
		this.type = type;
		initalizeWindow();
	}
	
	
	public TextualDescriptionDialog(IEditor editor, JTable table, DefaultTableModel defaultTableModel, String type, int row){
		super();
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.editor = editor;
		this.table = table;
		this.defaultTableModel = defaultTableModel;
		this.type = type;
		this.row = row;
		initalizeWindow();
		this.pack();
	}
	
	
	private void initalizeWindow(){
	
		this.panel = new JPanel();	
		this.setSize(new Dimension(400,280));
		this.setMinimumSize(new Dimension(400,280));
		this.setPreferredSize(new Dimension(400,280));
		this.setMaximumSize(new Dimension(400,280));
		this.setResizable(false);
		this.getContentPane().add(this.panel);
		this.panel.setLayout(new BorderLayout());
		
		
		//NORTH
		JPanel north = new JPanel();
		north.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.panel.add(north, BorderLayout.NORTH);
		
		
		//CENTER
		JPanel center = new JPanel();
		center.setLayout(null);
		center.setBorder(BorderFactory.createTitledBorder("Settings"));
		
		this.idLabel = new JLabel("Id");
		this.idLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.idLabel.setBounds((260/2)-120, 10, 240, 20);
		center.add(this.idLabel);
		
		this.id = new JTextField();
		this.id.setEditable(false);
		this.id.setBounds((260/2)-120, 30, 240, 20);
		this.id.setFocusable(false);
		center.add(this.id);

		this.textLabel = new JLabel("Text");
		this.textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.textLabel.setBounds((260/2)-120, 55, 240, 20);
		center.add(this.textLabel);
		
		this.text = new JTextArea();
		this.text.setLineWrap(true);
		this.textScrollPane = new JScrollPane(this.text);
		this.textScrollPane.setBounds((260/2)-120, 75, 240, 115);	
		center.add(this.textScrollPane);

		this.panel.add(center, BorderLayout.CENTER);

		
		//EAST
		JPanel west = new JPanel();
		west.setBorder(BorderFactory.createTitledBorder("Available Ids"));
		west.setSize(new Dimension(140,280));
		west.setMinimumSize(new Dimension(140,280));
		west.setPreferredSize(new Dimension(140,280));
		west.setMaximumSize(new Dimension(140,280));
		west.setLayout(new BorderLayout());
		
		String[] columnNames = {"Ids","Selected","delete"};
		this.availableIdsTableModel = new DefaultTableModel();
		this.availableIdsTableModel.setColumnIdentifiers(columnNames);
		this.availableIdsTable = new JTable(this.availableIdsTableModel){  
			  public boolean isCellEditable(int row,int column){  
				    return false;  
				  }  
			  public Class getColumnClass(int col) {   
			        if (col == 0 || col == 1)       //second column accepts only String values   
			            return String.class;   
			        if(col == 2) 
			        	return ImageIcon.class;  //other columns accept String values  
			        else
			        	return String.class;
			        }  
		};
		this.availableIdsTable.setTableHeader(null);
		this.availableIdsTable.setShowGrid(false);
		this.availableIdsTable.setFillsViewportHeight(true);
		
		this.availableIdsTable.getColumnModel().getColumn(1).setMaxWidth(0);
		this.availableIdsTable.getColumnModel().getColumn(1).setMinWidth(0);
		this.availableIdsTable.getColumnModel().getColumn(1).setPreferredWidth(0);
		
		this.availableIdsTable.getColumnModel().getColumn(2).setMaxWidth(17);
		this.availableIdsTable.getColumnModel().getColumn(2).setMinWidth(17);
		this.availableIdsTable.getColumnModel().getColumn(2).setPreferredWidth(17);
		this.availableIdsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollP= new JScrollPane(this.availableIdsTable);
		west.add(scrollP, BorderLayout.CENTER);	
		fillTable();

		this.panel.add(west, BorderLayout.EAST);
		
		
		//SOUTH
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		
		this.buttonOk = new JButton("OK");
		south.add(this.buttonOk);
		
		this.buttonCancel = new JButton("Cancel");
		south.add(this.buttonCancel);
		
		this.panel.add(south, BorderLayout.SOUTH);
		
		this.addKeyListener(this);
		this.buttonCancel.addActionListener(this);
		this.buttonOk.addActionListener(this);
		this.text.addKeyListener(this);
		this.availableIdsTable.addMouseListener(this);
		this.availableIdsTable.addKeyListener(this);
		this.id.addMouseListener(this);
		this.id.addKeyListener(this);
		
		if(type.equals("edit")){
			this.setTitle(Messages.getString("Paraphrasing.TextualDescriptionDialog.Header.Edit"));
			this.id.setText((String)this.table.getValueAt(this.row, 0));
			loadData((String)this.table.getValueAt(this.row, 0));
			//fill JTextArea with text from table
			this.text.setText((String) this.table.getValueAt(row,1));
			
		}
		else{
			this.setTitle(Messages.getString("Paraphrasing.TextualDescriptionDialog.Header.New"));
		}
		this.setVisible(true);
	}
	
	/**
	 * Reads all available elements in the editor
	 * and writes it to the dialog table
	 * 
	 * @author Martin Meitz
	 * 
	 */
	private void fillTable(){
		
		Iterator<AbstractPetriNetElementModel> i = this.editor.getModelProcessor().getElementContainer().getRootElements().iterator();
		ArrayList<String> idList = new ArrayList<String>();
		
		while (i.hasNext()) {
			AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) i.next();

			if (current.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
            {
            	OperatorTransitionModel operatorModel = (OperatorTransitionModel) current;
                Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel.getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).values().iterator();
                while (simpleTransIter.hasNext())
                {
                    AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter.next();
                    if (simpleTransModel != null 
                            && operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
                    {
                      idList.add(((TransitionModel) operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId())).getId());
                    }

                }
                
            }
			else{
				idList.add(current.getId());
			}

			
		}
		Collections.sort(idList);
		
		for(int r=0; r<idList.size(); r++){
			Object[] row = {idList.get(r),"0", new ImageIcon()};
			this.availableIdsTableModel.addRow(row);
		}
	}
	
	/**
	 * Writes the data from both fields to the
	 * description table
	 * 
	 * @author Martin Meitz
	 * 
	 */
	private void writeData(){
		if(!this.text.getText().equals("") && !this.id.getText().equals("")){
			
			String input = this.id.getText().trim();
			int length = input.length();
			
			if(input.endsWith(",")){
				input = input.substring(0, length-1);
			}
			
			
			//edit
			if(row != -1){
				this.defaultTableModel.setValueAt(input, row, 0);
				this.defaultTableModel.setValueAt(this.text.getText(), row, 1);
				this.defaultTableModel.fireTableDataChanged();
			}
			//new
			else {
				String[] text = {input ,this.text.getText()};	
				this.defaultTableModel.addRow(text);
				row = defaultTableModel.getRowCount()-1;
			}
			updateElementContainer();
			table.setRowSelectionInterval(row, row);
			
			
			highlightElement(row);
			
			this.setVisible(false);
			this.dispose();
		
		}
		else{
			JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.TextualDescriptionDialog.Notification.EmptyFields"),
					Messages.getString("Paraphrasing.TextualDescriptionDialog.Notification.Title"), JOptionPane.INFORMATION_MESSAGE);		
		}
	}
	
		
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == buttonOk){
			writeData();
		}
		
		
		if(e.getSource() == buttonCancel){
			this.setVisible(false);
			this.dispose();	
			if(this.row != -1){
				highlightElement(this.row);
				table.setRowSelectionInterval(row, row);
			}
			else{
				clearHighlighting(this.editor, this.table);
			
			}
		}
	}
	
	
	@Override
	public void keyPressed(KeyEvent k) {
		//Escape Key
		if(k.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.setVisible(false);
			this.dispose();	
			if(this.row != -1){
				highlightElement(this.row);
				table.setRowSelectionInterval(row, row);
			}
			else{
				clearHighlighting(this.editor, this.table);
			
			}
			
		}
		
		if(k.getKeyCode() == KeyEvent.VK_ENTER)
		{
			writeData();
		}
		
		if(k.getKeyCode() == KeyEvent.VK_TAB){
			
			transferFocus();  
			k.consume();
		}		
	}

	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(e.getSource() == availableIdsTable){
			int row = availableIdsTable.rowAtPoint(e.getPoint());
			int column = availableIdsTable.columnAtPoint(e.getPoint());
			
			
			if(availableIdsTableModel.getValueAt(row,1).equals("0")){
				availableIdsTableModel.setValueAt("1", row,1);
				availableIdsTableModel.setValueAt(cancelIcon, row,2);
				
			}
			else if(availableIdsTableModel.getValueAt(row,1).equals("1") && column == 2){
				availableIdsTableModel.setValueAt("0", row,1);
				availableIdsTableModel.setValueAt(null, row,2);
				
			}
			
			String output = "";
			for(int i = 0; i < availableIdsTable.getRowCount(); i++){
				
				if(availableIdsTableModel.getValueAt(i, 1).equals("1")){
					output = output + availableIdsTable.getValueAt(i,0) + "," ;
				}				
			}
			
			Iterator<AbstractPetriNetElementModel> i = this.editor.getModelProcessor().getElementContainer().getRootElements().iterator();
			
			
			while (i.hasNext()) {
				AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) i.next();

					if (current.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
		            {
		            	OperatorTransitionModel operatorModel = (OperatorTransitionModel) current;
		                Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel.getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).values().iterator();
		               
		                int highlightcounter = 0;
		                while (simpleTransIter.hasNext())
		                {
		                    AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter.next();
		                    if (simpleTransModel != null // Sometimes the iterator
		                            // returns null...
		                            && operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
		                    {
		                       if(availableIdsTableModel.getValueAt(row,1).equals("0") && availableIdsTableModel.getValueAt(row,0).equals(simpleTransModel.getId()) && column == 2){
									simpleTransModel.setHighlighted(false);
								}

								else if(availableIdsTableModel.getValueAt(row,1).equals("1") && availableIdsTableModel.getValueAt(row,0).equals(simpleTransModel.getId())){
									simpleTransModel.setHighlighted(true);
								}
		                    	
		                    	if(simpleTransModel.isHighlighted() == true){
		                    		highlightcounter++;
		                    	}
		                    
		                    }

		                }
		                
		                if(highlightcounter > 0){
		                	current.setHighlighted(true);
		                }
		                else{
		                	current.setHighlighted(false);
		                }
		            }  
		            
					else{
						if(availableIdsTableModel.getValueAt(row,1).equals("0") && availableIdsTableModel.getValueAt(row,0).equals(current.getId()) && column == 2){
							current.setHighlighted(false);
						}
						else if(availableIdsTableModel.getValueAt(row,1).equals("1") && availableIdsTableModel.getValueAt(row,0).equals(current.getId())){
							current.setHighlighted(true);
						}
					}					
			}
			this.editor.repaint(); 
			id.setText(output);
		}	
	}

	
	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == id){
			id.setToolTipText(id.getText());
		}
		
		if(e.getSource() == text){
			text.setToolTipText(text.getText());
		}
	}

	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void highlightElement(int row){
		String[] selection = ((String)table.getValueAt(row,0)).split(",");

		Iterator<AbstractPetriNetElementModel> i = this.editor.getModelProcessor().getElementContainer().getRootElements().iterator();

		while (i.hasNext()) {
			AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) i.next();
			current.setHighlighted(false);
			if (current.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
            {
            	OperatorTransitionModel operatorModel = (OperatorTransitionModel) current;
                Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel.getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).values().iterator();
                while (simpleTransIter.hasNext())
                {
                    AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter.next();
                    if (simpleTransModel != null 
                            && operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
                    {
                    	simpleTransModel.setHighlighted(false);
                    	for(String id : selection){
            				if (simpleTransModel.getId().equals(id)){
            					current.setHighlighted(true);
            				}
            			}
                    }
                }
                
            }
			else{
				for(String id : selection){
					if (current.getId().equals(id)){
						current.setHighlighted(true);
					}
				}
			}
		}
		this.editor.repaint(); 
	}

	
	private void updateElementContainer(){
		this.editor.getModelProcessor().getElementContainer().getParaphrasingModel().deleteValues();
		for(int i = 0; i < defaultTableModel.getRowCount(); i++){
			this.editor.getModelProcessor().getElementContainer().getParaphrasingModel().addElement((String)this.defaultTableModel.getValueAt(i, 0), (String)this.defaultTableModel.getValueAt(i, 1));		
		}
	}
	
	
	/**
	 * Preselects the selected ids
	 * 
	 * @author Martin Meitz
	 * 
	 */
	private void loadData(String input){
		String[] inputArr = input.split(",");
		
		for(String element : inputArr){
			for (int i = 0; i < availableIdsTableModel.getRowCount(); i++){
				if(element.equals(availableIdsTableModel.getValueAt(i, 0))){
					availableIdsTableModel.setValueAt("1", i, 1);
					availableIdsTableModel.setValueAt(cancelIcon, i,2);
				}
			}
		}
	}
	
	private void clearHighlighting(IEditor editor, JTable table){

		Iterator<AbstractPetriNetElementModel> i = editor.getModelProcessor().getElementContainer().getRootElements().iterator();
		
		while (i.hasNext()) {
			AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) i.next();
			current.setHighlighted(false);
			if (current.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
            {
            	OperatorTransitionModel operatorModel = (OperatorTransitionModel) current;
                Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel.getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).values().iterator();
                while (simpleTransIter.hasNext())
                {
                    AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter.next();
                    if (simpleTransModel != null 
                            && operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
                    {
                    	simpleTransModel.setHighlighted(false);
                    }
                }
            }
		}
		table.clearSelection();
		editor.repaint();		
		table.repaint();
	}

}
