package org.woped.qualanalysis.paraphrasing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.qualanalysis.paraphrasing.action.ButtonListener;
import org.woped.gui.translations.Messages;

public class ParaphrasingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JButton newButton = null;
	private JButton loadButton = null;
	private JButton addButton = null;
	private JButton deleteButton = null;
	private JButton upButton = null;
	private JButton downButton = null;
	private JButton exportButton = null;
	private IEditor m_editor = null;
	private ParaphrasingOutput paraphrasingOutput = null;
	private static boolean threadInProgress = false;
	
	
	private JPopupMenu menu = new JPopupMenu();
	private JMenuItem menuAdd = null;
	private JMenuItem menuDelete = null;
	private JMenuItem menuUp = null;
	private JMenuItem menuDown = null;
	private JMenuItem menuProperties = null;
	
	public ParaphrasingPanel(IEditor editor){
		super();
		this.m_editor = editor;
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		this.paraphrasingOutput = new ParaphrasingOutput(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(getNewButton());
		buttonPanel.add(getLoadButton());
		buttonPanel.add(getAddButton());
		buttonPanel.add(getDeleteButton());
		buttonPanel.add(getUpButton());
		buttonPanel.add(getDownButton());
		buttonPanel.add(getExportButton());

		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(this.paraphrasingOutput.getMainPanel(), BorderLayout.CENTER);
	}
	
	// add listener to paraphrasing tool
	public void setParaphrasingListeners() {
		if (getParaphrasingOutput().getTable() != null) {

//			getParaphrasingOutput().addListeners();

			// if the table has no entries, load values from element container
			if (getParaphrasingOutput()
					.getDefaultTableModel().getRowCount() == 0) {
				for (int x = 0; x < m_editor.getModelProcessor()
						.getElementContainer().getParaphrasingModel()
						.getTableSize(); x++) {
					int elements = 0;
					// check ids if they are in the diagram
					String[] content = m_editor.getModelProcessor()
							.getElementContainer().getParaphrasingModel()
							.getElementByRow(x)[0].split(",");
					String ids = "";
					Iterator<AbstractPetriNetElementModel> i = m_editor
							.getModelProcessor().getElementContainer()
							.getRootElements().iterator();
					while (i.hasNext()) {
						AbstractPetriNetElementModel cur = (AbstractPetriNetElementModel) i
								.next();

						if (cur.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
							OperatorTransitionModel operatorModel = (OperatorTransitionModel) cur;
							Iterator<AbstractPetriNetElementModel> simpleTransIter = operatorModel
									.getSimpleTransContainer()
									.getElementsByType(
											AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
									.values().iterator();
							while (simpleTransIter.hasNext()) {
								AbstractPetriNetElementModel simpleTransModel = (AbstractPetriNetElementModel) simpleTransIter
										.next();
								if (simpleTransModel != null
										&& operatorModel
												.getSimpleTransContainer()
												.getElementById(
														simpleTransModel
																.getId())
												.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE) {
									for (String v : content) {
										if (v.equals(simpleTransModel.getId())) {
											ids = ids + v + ",";
											elements++;
										}
									}
								}

							}
						} else {
							for (String v : content) {
								if (v.equals(cur.getId())) {
									ids = ids + v + ",";
									elements++;
								}
							}
						}

					}

					if (elements > 0) {
						ids = ids.substring(0, ids.length() - 1);
						String[] test = {
								ids,
								m_editor.getModelProcessor()
										.getElementContainer()
										.getParaphrasingModel()
										.getElementByRow(x)[1] };
										getParaphrasingOutput()
								.addRow(test);
					}

				}
			}
		}
	}
		
	public void enableButtons(boolean value){
		this.newButton.setEnabled(value);
		this.loadButton.setEnabled(value);
		enableEditButtons(value);
	}
	
	public void enableEditButtons(boolean value){
		this.addButton.setEnabled(value);
		this.deleteButton.setEnabled(value);
		this.upButton.setEnabled(value);
		this.downButton.setEnabled(value);
		this.exportButton.setEnabled(value);
	}
	
	public JButton getNewButton(){
        if (newButton == null){
        	newButton = new JButton();
        	newButton.setIcon(Messages.getImageIcon("Paraphrasing.New"));
        	newButton.setToolTipText(Messages.getString("Paraphrasing.New.Title"));
        	newButton.addActionListener(new ButtonListener(this));
        	defineButtonSize(newButton);
        	newButton.setBorderPainted(false);
        }

        return newButton;
    }

	
   public JButton getLoadButton(){
        if (loadButton == null){
        	loadButton = new JButton();
        	loadButton.setIcon(Messages.getImageIcon("Paraphrasing.Load"));
        	loadButton.setToolTipText(Messages.getString("Paraphrasing.Load.Title"));
        	loadButton.setEnabled(true);
        	loadButton.addActionListener(new ButtonListener(this));
        	defineButtonSize(loadButton);
        	loadButton.setBorderPainted(false);
        }

        return loadButton;
    }
   
   
   	public JButton getAddButton(){
       if (addButton == null){
    	   addButton = new JButton();
    	   addButton.setIcon(Messages.getImageIcon("Paraphrasing.Add"));
    	   addButton.setToolTipText(Messages.getString("Paraphrasing.Add.Title"));
    	   addButton.setEnabled(true);
       	   addButton.addActionListener(new ButtonListener(this));
       	   defineButtonSize(addButton);
       	   addButton.setBorderPainted(false);
       }

       return addButton;
   	}
   
   	
   	public JButton getDeleteButton(){
       if (deleteButton == null){
    	   deleteButton = new JButton();
    	   deleteButton.setIcon(Messages.getImageIcon("Paraphrasing.Delete"));
    	   deleteButton.setToolTipText(Messages.getString("Paraphrasing.Delete.Title"));
       	   deleteButton.setEnabled(true);
       	   deleteButton.addActionListener(new ButtonListener(this));
       	   defineButtonSize(deleteButton);
       	   deleteButton.setBorderPainted(false);
       }

       return deleteButton;
   	}
   
   
   	public JButton getUpButton(){
       if (upButton == null){
    	   upButton = new JButton();
    	   upButton.setIcon(Messages.getImageIcon("Paraphrasing.Up"));
    	   upButton.setToolTipText(Messages.getString("Paraphrasing.Up.Title"));
       	   upButton.setEnabled(true);
    	   upButton.addActionListener(new ButtonListener(this));
    	   defineButtonSize(upButton);
    	   upButton.setBorderPainted(false);
       }

       return upButton;
   	}
   
   
   	public JButton getDownButton(){
       if (downButton == null){
    	   downButton = new JButton();
    	   downButton.setIcon(Messages.getImageIcon("Paraphrasing.Down"));
    	   downButton.setToolTipText(Messages.getString("Paraphrasing.Down.Title"));
       	   downButton.setEnabled(true);
    	   downButton.addActionListener(new ButtonListener(this));
    	   defineButtonSize(downButton);
    	   downButton.setBorderPainted(false);
       }

       return downButton;
   	}
   
  
   	public JButton getExportButton(){
       if (exportButton == null){
    	   exportButton = new JButton();
    	   exportButton.setIcon(Messages.getImageIcon("Paraphrasing.Export"));
    	   exportButton.setToolTipText(Messages.getString("Paraphrasing.Export.Title"));
    	   exportButton.setEnabled(true);
    	   exportButton.addActionListener(new ButtonListener(this));
    	   defineButtonSize(exportButton);
    	   exportButton.setBorderPainted(false);
       }

       return exportButton;
   	}
   
   
   	public void defineButtonSize(JButton button){
	   Dimension dim = new Dimension(20,20);
	   button.setSize(dim);
	   button.setMinimumSize(dim);
	   button.setMaximumSize(dim);
	   button.setPreferredSize(dim);
	   button.setBorderPainted(false);
   	}
    
   
   	public ParaphrasingOutput getParaphrasingOutput(){
	   return this.paraphrasingOutput;
   	}

  
   	public IEditor getEditor(){
	   return this.m_editor;
   	}
   
   
   	public void setThreadInProgress(boolean inProgress){
	   threadInProgress = inProgress;
   	}
   
   
   	public boolean getThreadInProgress(){
	   return threadInProgress;
   	}
   
   	
	public JPopupMenu getMenu(){
		return this.menu;
	}
	
	
	public JMenuItem getAddItem(){
		if (menuAdd == null){
			menuAdd = new JMenuItem("Add", Messages.getImageIcon("Paraphrasing.Add"));	
			this.menuAdd.addActionListener(new ButtonListener(this));					
		}
		return menuAdd;
	}
	
	public JMenuItem getDeleteItem(){
		if (menuDelete == null){
			menuDelete = new JMenuItem("Delete", Messages.getImageIcon("Paraphrasing.Delete"));
			menuDelete.addActionListener(new ButtonListener(this));
		}
		return menuDelete;
	}
	
	
	public JMenuItem getUpItem(){
		if (menuUp == null){
			menuUp = new JMenuItem("Up", Messages.getImageIcon("Paraphrasing.Up"));
			menuUp.addActionListener(new ButtonListener(this));
		}
		return menuUp;
	}
	
	
	public JMenuItem getDownItem(){
		if (menuDown == null){
			menuDown = new JMenuItem("Down", Messages.getImageIcon("Paraphrasing.Down"));
			menuDown.addActionListener(new ButtonListener(this));
		}
		return menuDown;
	}
	
	public JMenuItem getPropertiesItem(){
		if (menuProperties == null){
			menuProperties = new JMenuItem("Properties", Messages.getImageIcon("Popup.Properties"));
			menuProperties.addActionListener(new ButtonListener(this));
		}
		return menuProperties;
	}

}
