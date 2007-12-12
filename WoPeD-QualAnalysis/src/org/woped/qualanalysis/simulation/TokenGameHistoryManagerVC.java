package org.woped.qualanalysis.simulation;

import javax.swing.*;

import org.woped.qualanalysis.simulation.controller.*;
import org.woped.core.gui.*;
import java.awt.*;

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
	private DefaultListModel         HistoryData      = null; 
	private String[]                 HistoryItems     = null;
	private TokenGameHistoryObject   HistoryObject    = null;
	private TokenGameHistoryObject[] HistoryArray     = null;
	private TokenGameHistoryObject[] HelpArray        = null;
	private ListSelectionModel       SelectedItems    = null;

	
	
	//Standard-Constructor
	public TokenGameHistoryManagerVC(IUserInterface mediator)
	{
		super((JFrame)mediator, Messages.getTitle("Tokengame.HistoryManager"), true);
		this.add(addHistoryManagerPanel());
		this.setLocationRelativeTo(null);
		this.setSize(300,300);
		this.setResizable(false);
	}
	
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
	    DeleteSelected.addActionListener(new TokenGameBarListener(TokenGameBarListener.HM_DELETE_SELECTED));
	    OverwriteSelected.addActionListener(new TokenGameBarListener(TokenGameBarListener.HM_OVERWRITE_SELECTED));
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
		//Declare some locale variables
		int OldSize, NewSize, NumberOfItems = 0;
		
		
		HistoryData = RemoteControl.getHistoryData();	
	    NumberOfItems = HistoryData.getSize();
	    HistoryItems = new String[NumberOfItems];
	    
	    //Fill created array with the History-Data
	    for(int i = 0; i < NumberOfItems; i++)
	    {
	    	HistoryItems[i] = (String)HistoryData.getElementAt(i);
	    }
        //Create history-Object and add the Items-Array
	    HistoryObject = new TokenGameHistoryObject(HistoryItems);
	    HistoryObject.setHistoryName(NameEntry.getText());
        //Attach the History Object into the List-Box with its name...
	    SavedHistoryContent.addElement(NameEntry.getText());
	    
	    //... and into the Array at the according Position
	    if(SavedHistoryContent.size() == 0)
	    {
	        HistoryArray = new TokenGameHistoryObject[1];
	        HistoryArray[0] = HistoryObject; 
	    }
	    else
	    {
	    	//Copy current Array to help Array
	    	OldSize = SavedHistoryContent.size()-1;
	    	NewSize = SavedHistoryContent.size();
	    	HelpArray = new TokenGameHistoryObject[OldSize];
	    	for(int i = 0; i < OldSize; i++)
	    	{
	    		HelpArray[i] = HistoryArray[i];
	    	}
	    	
	    	//enlarge current array (build new)
	    	HistoryArray = new TokenGameHistoryObject[NewSize];
	    	//copy back the Data
	    	for(int i = 0; i < OldSize; i++)
	    	{
	    		HistoryArray[i] = HelpArray[i];
	    	}
	    	//add the new Item at the very end
	    	HistoryArray[NewSize-1] = HistoryObject;
	    	disableSaveButton();
	    	
	    }
        
	}
	
	public void deleteSelected()
	{
		
	}
	
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
	  if (HistoryArray != null)
	  {
		  HistoryObject = HistoryArray[LoadIndex];
		  RemoteControl.addHistoryData(HistoryObject.getHistoryItems());
		  this.setVisible(false);
	  }
	  
	  
	  
	}
	
	public void overwriteSelected()
	{
		
	}
	
	public boolean checkSaveName()
	{
		//TODO Program the checkmechanism to... 
		//...determine if the TextField is empty
		return true;
		
	}
	
	
	
	public TokenGameBarVC getRemoteControl()
	{
		return RemoteControl;
	}
	
	public void disableSaveButton()
	{
		SaveNewHistory.setEnabled(false);
	}
	
	public void enableSaveButton()
	{
		SaveNewHistory.setEnabled(true);
	}
	
	public void resetStates()
	{
		NameEntry.setText("");
		if(SavedHistory != null)
		{
		    SavedHistory.getSelectionModel().clearSelection();		
	    }
    }
	
}
