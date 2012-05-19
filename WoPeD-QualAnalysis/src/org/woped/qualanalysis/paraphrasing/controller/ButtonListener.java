package org.woped.qualanalysis.paraphrasing.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;

import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.paraphrasing.Constants;
import org.woped.qualanalysis.paraphrasing.editing.TextualDescriptionDialog;
import org.woped.qualanalysis.paraphrasing.view.ParaphrasingPanel;
import org.woped.translations.Messages;

public class ButtonListener implements ActionListener{

	private ParaphrasingPanel paraphrasingPanel = null;
	private WebServiceThread webService = null;
	
	public ButtonListener(ParaphrasingPanel paraphrasingPanel){
		this.paraphrasingPanel = paraphrasingPanel;
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		//New Button
		if(e.getSource() == this.paraphrasingPanel.getNewButton()){
			
			if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.New.Question.Content"), Messages.getString("Paraphrasing.New.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){
				//delete existing table and create a new one
				this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().setRowCount(0);
				this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().fireTableDataChanged();
				this.paraphrasingPanel.getParaphrasingOutput().updateElementContainer();
				
				LoggerManager.info(Constants.PARAPHRASING_LOGGER, " New description table created.");	
				
			}			
		}

		//Call Webservice Button
		if(e.getSource() == this.paraphrasingPanel.getLoadButton()){
			
			if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Load.Question.Content"), Messages.getString("Paraphrasing.Load.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){
				paraphrasingPanel.getParaphrasingOutput().setAnimationVisible();
				 
				webService = new WebServiceThread(paraphrasingPanel);
				webService.start();
				webService = null;
			}
		}
		
		//Delete Button
		if(e.getSource() == this.paraphrasingPanel.getDeleteButton()){
			int selectedRow = this.paraphrasingPanel.getParaphrasingOutput().getTable().getSelectedRow();
			if(selectedRow != -1){
				if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Delete.Question.Notification"), Messages.getString("Paraphrasing.Delete.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){

					this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().removeRow(selectedRow);
					this.paraphrasingPanel.getParaphrasingOutput().updateElementContainer();
					
					LoggerManager.info(Constants.PARAPHRASING_LOGGER, "   ... Description deleted.");	
					
				}
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Delete.Notification"));
			}
			
		}
		
		
		//Add Button
		if(e.getSource() == this.paraphrasingPanel.getAddButton()){
			JTable table = this.paraphrasingPanel.getParaphrasingOutput().getTable();
			/*TextualDescriptionDialog textualdescriptionDialog = */ new TextualDescriptionDialog(this.paraphrasingPanel.getEditor() , table, this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel(), "new");
//			textualdescriptionDialog = null;
		}
		
		
		//Up Button
		if(e.getSource() == this.paraphrasingPanel.getUpButton()){
			int selectedRow = this.paraphrasingPanel.getParaphrasingOutput().getTable().getSelectedRow();
			if(selectedRow != -1){
				if(this.paraphrasingPanel.getParaphrasingOutput().getTable().getSelectedRowCount() == 1){
					//Only swap row if a row except the first is selected
					if(selectedRow > 0){
						this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().moveRow(selectedRow, selectedRow, selectedRow-1);
						this.paraphrasingPanel.getParaphrasingOutput().getTable().setRowSelectionInterval(selectedRow-1, selectedRow-1);
						this.paraphrasingPanel.getParaphrasingOutput().updateElementContainer();
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Up.Notification"));
			}
		}
		
		//Down Button
		if(e.getSource() == this.paraphrasingPanel.getDownButton()){
			int selectedRow = this.paraphrasingPanel.getParaphrasingOutput().getTable().getSelectedRow();
			if(selectedRow != -1){
				if(this.paraphrasingPanel.getParaphrasingOutput().getTable().getSelectedRowCount() == 1){
					//Only swap row if a row except the last is selected
					if(selectedRow < this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getRowCount()-1){
						this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().moveRow(selectedRow, selectedRow, selectedRow+1);
						this.paraphrasingPanel.getParaphrasingOutput().getTable().setRowSelectionInterval(selectedRow+1, selectedRow+1);
						this.paraphrasingPanel.getParaphrasingOutput().updateElementContainer();
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Down.Notification"));
			}
		}
		
		
		//Export Button
		if(e.getSource() == this.paraphrasingPanel.getExportButton()){
			
			boolean fileTypeOk = false;
			if (this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getRowCount() > 0){
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileFilter(new FileFilter() {
	            public boolean accept(File f) {
	                return f.getName().toLowerCase().endsWith(".txt");
	            }
	            public String getDescription() {
	                return "*.txt File";
	            }
	        });
				while(!fileTypeOk){
					if(jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
						String path = jFileChooser.getSelectedFile().getAbsolutePath();
						if(!path.endsWith(".txt")){
							path = path + ".txt";
						}

						try{
							fileTypeOk = true;
							File file = new File(path);
								
							FileWriter out = new FileWriter(file);
							
							out.write("Created with Workflow PetriNet Designer Version " + Messages.getString("Application.Version") +" (woped.org) \n\n  ");
//									new java.sql.Timestamp(new java.util.Date().getTime()) + );
							//Export Ids
							if(JOptionPane.showConfirmDialog(null, Messages.getString("Paraphrasing.Export.Question.Message"), Messages.getString("Paraphrasing.Export.Question.Title"), JOptionPane.YES_NO_OPTION)  == JOptionPane.YES_OPTION){
								for(int i = 0; i < this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getRowCount(); i++){
									//out.write(this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getValueAt(i,0) + " - " + this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getValueAt(i,1) + "\n");
									out.write(this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getValueAt(i,0) + "\n");
									out.write(this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getValueAt(i,1) + "\n\n");

								}
							}				            	
							else{
								for(int i = 0; i < this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getRowCount(); i++){
									out.write(this.paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel().getValueAt(i,1) + "\n\n");
								}
							}
							LoggerManager.info(Constants.PARAPHRASING_LOGGER, " Description exported to: " + path);	
							
							out.close();
						}
						catch(Exception ex){
							System.out.println("Fehler: " + ex);
							
							JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Export.Error.Message") + "\n" + ex.getMessage(),
									Messages.getString("Paraphrasing.Export.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
						}
						
					
					}
					else{
						fileTypeOk = true;
					}
					
				}
				
			}
			else{
				JOptionPane.showMessageDialog(null,Messages.getString("Paraphrasing.Export.Numberelements.Message"),Messages.getString("Paraphrasing.Export.Numberelements.Title"), JOptionPane.INFORMATION_MESSAGE);
			}	
		}		
	}
}
