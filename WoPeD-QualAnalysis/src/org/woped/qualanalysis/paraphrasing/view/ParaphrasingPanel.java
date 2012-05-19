package org.woped.qualanalysis.paraphrasing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.paraphrasing.controller.ButtonListener;
import org.woped.translations.Messages;

public class ParaphrasingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JButton newButton = null;
	private JButton loadButton = null;
	private JButton addButton = null;
	private JButton deleteButton = null;
	private JButton upButton = null;
	private JButton downButton = null;
	private JButton exportButton = null;
	private IEditor editor = null;
	private ParaphrasingOutput paraphrasingOutput = null;
	private static boolean threadInProgress = false;
	
	public ParaphrasingPanel(IEditor editor){
		super();
		this.editor = editor;
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

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
		
		createOutputPanel();
		this.add(this.paraphrasingOutput.getMainPanel(), BorderLayout.CENTER);
	}
	
	private void createOutputPanel(){

		this.paraphrasingOutput = new ParaphrasingOutput(this);
		
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
	   return this.editor;
   	}
   
   
   	public void setThreadInProgress(boolean inProgress){
	   threadInProgress = inProgress;
   	}
   
   
   	public boolean getThreadInProgress(){
	   return threadInProgress;
   	}
   

}
