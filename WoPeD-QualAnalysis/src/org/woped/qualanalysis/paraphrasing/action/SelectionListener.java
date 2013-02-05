package org.woped.qualanalysis.paraphrasing.action;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.qualanalysis.paraphrasing.editing.TextualDescriptionDialog;
import org.woped.qualanalysis.paraphrasing.view.ParaphrasingOutput;
import org.woped.gui.translations.Messages;


public class SelectionListener implements MouseListener, KeyListener {
	
	private JTable table = null;
	private IEditor editor = null;
	private DefaultTableModel defaultTableModel = null;
	private ParaphrasingOutput paraphrasingOutput = null;
	
	
		
	public SelectionListener(ParaphrasingOutput paraphrasingOutput){
		this.editor = paraphrasingOutput.getEditor();
		this.table = paraphrasingOutput.getTable();
		this.defaultTableModel = paraphrasingOutput.getDefaultTableModel();
		this.paraphrasingOutput = paraphrasingOutput;
		
	}
	

	/**
	 * Removes all highlighting from the editor
	 * and the table
	 * 
	 * @author Martin Meitz
	 * 
	 */
	public void clearAllHighlighting(){	
		clearHighlightingEditor();	
		this.table.clearSelection();
		this.table.repaint();
	}
	
	
	private void clearHighlightingEditor(){
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
                    }
                }    
            }
		}
		this.editor.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
		//Click into the table
		if (e.getSource() == this.table){
			this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			int row = table.rowAtPoint(e.getPoint());

			if(SwingUtilities.isLeftMouseButton(e)){
				//Highlight
				if(e.getClickCount() == 1){
					if(row != -1){				
						highlightElementInEditor(row);
						this.editor.repaint(); 
					    this.table.repaint(); 
					}
					else{
						clearAllHighlighting();
					}
				}
				
				//Edit
				if(e.getClickCount() == 2){
					new TextualDescriptionDialog(this.editor, this.table, this.defaultTableModel, "edit", row);
				}
			}
			
			
			if(SwingUtilities.isRightMouseButton(e)){
				if(row != -1){
					paraphrasingOutput.getTable().setRowSelectionInterval(row, row);

					paraphrasingOutput.getParaphrasingPanel().getMenu().removeAll();
					paraphrasingOutput.getParaphrasingPanel().getMenu().add(paraphrasingOutput.getParaphrasingPanel().getPropertiesItem());
					paraphrasingOutput.getParaphrasingPanel().getMenu().add(paraphrasingOutput.getParaphrasingPanel().getUpItem());
					paraphrasingOutput.getParaphrasingPanel().getMenu().add(paraphrasingOutput.getParaphrasingPanel().getDownItem());
					paraphrasingOutput.getParaphrasingPanel().getMenu().add(paraphrasingOutput.getParaphrasingPanel().getDeleteItem());
					paraphrasingOutput.getParaphrasingPanel().getMenu().show(e.getComponent(), e.getX(), e.getY());
				}
				else{
					clearAllHighlighting();
					paraphrasingOutput.getParaphrasingPanel().getMenu().removeAll();
					paraphrasingOutput.getParaphrasingPanel().getMenu().add(paraphrasingOutput.getParaphrasingPanel().getAddItem());
					paraphrasingOutput.getParaphrasingPanel().getMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
				
		}

		
		//Click into the editor
		if(e.getSource() == this.editor.getGraph()){
			
			Object object = this.editor.getGraph().getFirstCellForLocation(e.getX(), e.getY());
			this.table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			if(object instanceof GroupModel){
				AbstractPetriNetElementModel model = ((GroupModel)object).getMainElement();
				

				String selection= model.getId();		
				
				clearHighlightingEditor();
//				model.setHighlighted(true);

				this.table.clearSelection();

				if (model.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
	            {
	            	OperatorTransitionModel operatorModel = (OperatorTransitionModel) model;
	                Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel.getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).values().iterator();
	                while (simpleTransIter.hasNext())
	                {
	                    AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter.next();
	                    if (simpleTransModel != null 
	                            && operatorModel.getSimpleTransContainer().getElementById(simpleTransModel.getId()).getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
	                    {
	                        if(highlightElementInTable(simpleTransModel.getId()) == true){
	                        	model.setHighlighted(true);
	                        }
	                    }

	                }
	            }
		
				else{
					if(highlightElementInTable(selection) == true){
						model.setHighlighted(true);
					}
				}
			    this.editor.repaint(); 	

			}
			else if(object instanceof ArcModel){
				clearAllHighlighting();
			}
			else{
				clearAllHighlighting();
			}
			this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		

		}
		
		
		setEditButtonsStatusApproriately();
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == this.table){
			int row = table.rowAtPoint(e.getPoint());
			
			if(row != -1){
				//int row = table.rowAtPoint(e.getPoint());
				this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				highlightElementInEditor(this.table.rowAtPoint(e.getPoint()));
			}
			else{
				clearAllHighlighting();
			}
		}
		setEditButtonsStatusApproriately();
	}

	
	@Override
	public void keyPressed(KeyEvent k) {
		//press ENTER in the table
		if (k.getSource() == this.table){
			
			int row = this.table.getSelectedRow();
			
				//row selected and enter button pressed
				if(k.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(row != -1){
						new TextualDescriptionDialog(this.editor, this.table, this.defaultTableModel, "edit", row);
						this.table.setRowSelectionInterval(row-1,row-1);
					}
					else{
						JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Delete.Notification"));
					}
				}
			
				//row selected and delete button pressed
				if(k.getKeyCode() == KeyEvent.VK_DELETE){
					if(row != -1){
						if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Delete.Question.Notification"), Messages.getString("Paraphrasing.Delete.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION)
						{
							defaultTableModel.removeRow(row);
							clearAllHighlighting();
							paraphrasingOutput.updateElementContainer();
						}
					}
					else{
						JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Delete.Notification"));
					
				}
			}	
		}
		
		//row selected and esc button pressed
		if(k.getKeyCode() == KeyEvent.VK_ESCAPE){
			clearAllHighlighting();
		}
		setEditButtonsStatusApproriately();
	}

	
	@Override
	public void keyReleased(KeyEvent k) {
		
		//Click into the table
		if (k.getSource() == this.table){
			if(k.getKeyCode() == KeyEvent.VK_DOWN || k.getKeyCode() == KeyEvent.VK_UP)
			{
				highlightElementInEditor(this.table.getSelectedRow());
			}
		}
		setEditButtonsStatusApproriately();
	}

	
	@Override
	public void keyTyped(KeyEvent k) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retrieves the rownumber and highlights the
	 * appropriate elements in the editor
	 * 
	 * @author Martin Meitz
	 * 
	 */
	private void highlightElementInEditor(int row){
		String[] selection = ((String)this.table.getValueAt(row,0)).split(",");
				
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
            					simpleTransModel.setHighlighted(true);
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
	    this.table.repaint(); 
	}
	
	/**
	 * Retrieves the textids and highlights the
	 * appropriate elements in the editor
	 * 
	 * @author Martin Meitz
	 * 
	 */
	private boolean highlightElementInTable(String selection){
		boolean ret = false;
		for(int row=0; row < this.table.getRowCount(); row++){
			
			String[] value = ((String)this.table.getValueAt(row,0)).split(",");
			for(String id : value){
				
				if(id.equals(selection)){
					this.table.addRowSelectionInterval(row, row);
					ret = true;
				}
			}
		}
		this.table.repaint();
		return ret;
	}
	
	/**
	 * Activates the edit buttons of the output panel
	 * if there is at least one element in
	 * the table
	 * 
	 * @author Martin Meitz
	 * 
	 */
	private void setEditButtonsStatusApproriately(){

		if(this.table.getSelectedRowCount() > 1){
			this.paraphrasingOutput.getParaphrasingPanel().enableEditButtons(false);
		}
		else{
			this.paraphrasingOutput.getParaphrasingPanel().enableEditButtons(true);
		}
	}
	
}


