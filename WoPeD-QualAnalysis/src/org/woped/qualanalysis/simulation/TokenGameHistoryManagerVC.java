package org.woped.qualanalysis.simulation;

import javax.swing.*;

import org.woped.qualanalysis.simulation.controller.*;
import org.woped.core.gui.*;
import java.awt.*;
import java.util.Vector;

import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.translations.Messages; 

public class TokenGameHistoryManagerVC extends JDialog
{
	//Declaration of the Panel(s)
	private JPanel       HistoryManager     = null;
	
	//Declaration of the Entry-Field
	private JTextField   NameEntry          = null;
	
	//Declaration of the Listbox
	private JList        SavedHistory       = null;
	private JScrollPane  ScrollSavedHistory = null;
	private DefaultListModel SavedHistoryContent = null;
	
	//Declaration of the Buttons
	private JButton      SaveNewHistory     = null;
	private JButton      DeleteSelected     = null;
	private JButton      OverwriteSelected  = null;
	private JButton      OpenSelected       = null;	
	
	//Declare Layout Items
	private GridBagConstraints hmgbc        = null;
	private int          ButtonSizeX        = 50;
	private int          ButtonSizeY        = 25;
	private int          ListSizeX          = 200;
	private int          ListSizeY          = 200;
	
	//Declare Reference-Variables
	private TokenGameBarVC           RemoteControl    = null;
	private SimulationModel          HistoryData      = null; 
	private ListSelectionModel       SelectedItems    = null;
	

	//History-From-File
	private Vector<SimulationModel> HistoryFromFile = null;      
	
	//Standard-Constructor
	public TokenGameHistoryManagerVC(IUserInterface mediator, TokenGameBarVC RC)
	{
		super((JFrame)mediator, Messages.getTitle("Tokengame.HistoryManager"), true);
		RemoteControl = RC;
		this.add(addHistoryManagerPanel());
		this.setLocationRelativeTo(null);
		this.setSize(300,300);
		this.setResizable(false);
	}
	
	//Panel Definition and Creation
	private JPanel addHistoryManagerPanel()
	{
		//Build Buttons
	    SaveNewHistory    = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.SaveHistory"));
		DeleteSelected    = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.DeleteSelected"));
		OverwriteSelected = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.OverwriteSelected"));
	    OpenSelected      = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.OpenSelected"));
	    
	    //Set ToolTips
	    SaveNewHistory.setToolTipText(Messages.getTitle("Tokengame.HistoryManager.SaveHistory"));
	    DeleteSelected.setToolTipText(Messages.getTitle("Tokengame.HistoryManager.DeleteSelected"));
	    OverwriteSelected.setToolTipText(Messages.getTitle("Tokengame.HistoryManager.OverwriteSelected"));
	    OpenSelected.setToolTipText(Messages.getTitle("Tokengame.HistoryManager.OpenSelected"));
	    	    
	    //Set Button-Size
	    SaveNewHistory.setPreferredSize(new Dimension(ButtonSizeX, ButtonSizeY));
	    DeleteSelected.setPreferredSize(new Dimension(ButtonSizeX, ButtonSizeY));
	    OverwriteSelected.setPreferredSize(new Dimension(ButtonSizeX, ButtonSizeY));
	    OpenSelected.setPreferredSize(new Dimension(ButtonSizeX, ButtonSizeY));
	    
	    //Set ButtonActions
	    SaveNewHistory.addActionListener(new TokenGameBarListener(TokenGameBarListener.HM_SAVE_HISTORY, RemoteControl, this));
	    DeleteSelected.addActionListener(new TokenGameBarListener(TokenGameBarListener.HM_DELETE_SELECTED, RemoteControl, this));
	    OverwriteSelected.addActionListener(new TokenGameBarListener(TokenGameBarListener.HM_OVERWRITE_SELECTED, RemoteControl, this));
	    OpenSelected.addActionListener(new TokenGameBarListener(TokenGameBarListener.HM_OPEN_SELECTED, RemoteControl, this));
	    
	    
	    //Build Entry-Field
	    NameEntry          = new JTextField();
	    NameEntry.setPreferredSize(new Dimension(ListSizeX, ButtonSizeY));
	    
	    //Build Listbox
	    SavedHistoryContent = new DefaultListModel();
	    SavedHistory       = new JList(SavedHistoryContent);
	    ScrollSavedHistory = new JScrollPane(SavedHistory);
	    ScrollSavedHistory.setPreferredSize(new Dimension(ListSizeX, ListSizeY));
	    ScrollSavedHistory.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ScrollSavedHistory.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Define Panel
		HistoryManager     = new JPanel();
		HistoryManager.setLayout(new GridBagLayout());
		
		//Set Layout
        hmgbc              = new GridBagConstraints();
        
		hmgbc.gridx = 0;
		hmgbc.gridy = 0;
		hmgbc.insets       = new Insets (0,0,5,10);
		HistoryManager.add(SaveNewHistory, hmgbc);

		hmgbc.gridx = 0;
		hmgbc.gridy = 1;
		hmgbc.insets       = new Insets (50,0,5,10);
		HistoryManager.add(DeleteSelected, hmgbc);

		hmgbc.gridx = 0;
		hmgbc.gridy = 2;
		hmgbc.insets       = new Insets (0,0,5,10);
		HistoryManager.add(OverwriteSelected, hmgbc);

		hmgbc.gridx = 0;
		hmgbc.gridy = 3;
		hmgbc.insets       = new Insets (0,0,5,10);
		HistoryManager.add(OpenSelected, hmgbc);

		hmgbc.gridx = 1;
		hmgbc.gridy = 0;
		HistoryManager.add(NameEntry, hmgbc);
		
		hmgbc.gridx = 1;
		hmgbc.gridy = 1;
		hmgbc.gridwidth = 1;
		hmgbc.gridheight = 3;
		HistoryManager.add(ScrollSavedHistory, hmgbc);
		
		return HistoryManager;
	}
	
	public void saveNewHistory()
	{
		if (RemoteControl.isNewHistory())
		{
		 HistoryData = RemoteControl.getHistoryData();
	     SavedHistoryContent.addElement(NameEntry.getText());
	     HistoryData.setName(NameEntry.getText());
	     RemoteControl.saveHistory();
	     RemoteControl.setToOldHistory();
	     this.setVisible(false);
		}
		else
		{
			JOptionPane.showMessageDialog(this, Messages.getTitle("Tokengame.HistoryManager.ErrorNoNewHistory"), Messages.getTitle("Tokengame.HistoryManager.SaveError"),  JOptionPane.ERROR_MESSAGE);

		}
	}
	
	/**
	 * Loads the selected Simulation from the List of saved Simulations directly into the
	 * HistoryBox of the TokenGameBar 
	 */
	public void openSelected()
	{
	  int LoadIndex = 0;
	  
	  //Specify which File, that has to be opened
	  SelectedItems = SavedHistory.getSelectionModel();	
	  if(SelectedItems.isSelectionEmpty())
	  {
		 LoadIndex = 0; 
	  }
	  else
	  {
		  LoadIndex = SelectedItems.getMinSelectionIndex();		  
	  }
	  
	  //Load From Array
	  if(SavedHistoryContent.size() != 0)
	  {
	 	 RemoteControl.loadHistory(LoadIndex);
		 this.setVisible(false);
	  }	  
	}
	
	public int getSelection()
	{
		SelectedItems = SavedHistory.getSelectionModel();
		if(SelectedItems.isSelectionEmpty())
		{
			 return -1; 
		}
		else
		{
		     return SelectedItems.getMinSelectionIndex();	
		}
	}
	
	/**
	 * Checks if the chosen Name is Empty or already exists
	 * and throws Messageboxes for the certain cases
	 * 
	 * If no History has been recorded, it will not save as well
	 * @return true / false
	 */
	public boolean checkSaveName()
	{				
		if(NameEntry.getText() == "")
		{
		   JOptionPane.showMessageDialog(this, Messages.getTitle("Tokengame.HistoryManager.ErrorNameEmpty"), Messages.getTitle("Tokengame.HistoryManager.SaveError"),  JOptionPane.ERROR_MESSAGE);
		   return false;
		}
		else
		{
		   if(SavedHistoryContent.contains(NameEntry.getText()))
		   {
			   JOptionPane.showMessageDialog(this, Messages.getTitle("Tokengame.HistoryManager.ErrorNameDouble"), Messages.getTitle("Tokengame.HistoryManager.SaveError"),  JOptionPane.ERROR_MESSAGE);
  		       return false;
		   }
		   else
		   {
 		       return true;
		   }
		}
		
	}
	
	
	
	public TokenGameBarVC getRemoteControl()
	{
		return RemoteControl;
	}
	
	
	public void resetStates()
	{
		NameEntry.setText("");
		if(SavedHistory != null)
		{
		    SavedHistory.getSelectionModel().clearSelection();		
	    }
    }
	
	/**
	 * this method fills the history-List with the already existing Simulations 
	 */
	public void addLoadListItem(String item)
	{
		SavedHistoryContent.addElement(item);	
	}
	
	/**
	 * removes a possibly deleted History-Item from the Load-List
	 * @param index
	 */
	public void removeLoadListItem(int index)
	{
		SavedHistoryContent.remove(index);
	}
}
