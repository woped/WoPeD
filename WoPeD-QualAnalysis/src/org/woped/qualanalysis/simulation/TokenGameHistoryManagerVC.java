package org.woped.qualanalysis.simulation;

import javax.swing.*;
import org.woped.core.gui.*;
import java.awt.*;

import org.woped.translations.Messages; 

class TokenGameHistoryManagerVC extends JDialog
{
	//Declaration of the Panel(s)
	private JPanel       HistoryManager     = null;
	
	//Declaration of the Entry-Field
	private JTextField   NameEntry          = null;
	
	//Declaration of the Listbox
	private JList        SavedHistory       = null;
	private JScrollPane  ScrollSavedHistory = null;
	
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
	
	//Standard-Constructor
	public TokenGameHistoryManagerVC(IUserInterface mediator)
	{
		super((JFrame)mediator, "HistoryManager", true);
		this.add(addHistoryManagerPanel());
		this.setSize(300,300);
		this.setAlwaysOnTop(true);
		
	}
	
	//Panel Definition and Creation
	private JPanel addHistoryManagerPanel()
	{
		//Build Buttons
	    SaveNewHistory = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.SaveHistory"));
		DeleteSelected = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.DeleteSelected"));
		OverwriteSelected = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.OverwriteSelected"));
	    OpenSelected = new JButton(Messages.getImageIcon("Tokengame.HistoryManager.OpenSelected"));
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
	    
	    //Build Entry-Field
	    NameEntry = new JTextField();
	    NameEntry.setPreferredSize(new Dimension(ListSizeX, ButtonSizeY));
	    
	    //Build Listbox
	    SavedHistory = new JList();
	    ScrollSavedHistory = new JScrollPane(SavedHistory);
	    ScrollSavedHistory.setPreferredSize(new Dimension(ListSizeX, ListSizeY));
	    ScrollSavedHistory.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ScrollSavedHistory.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//Define Panel
		HistoryManager = new JPanel();
		HistoryManager.setLayout(new GridBagLayout());
		
		//Set Layout
        hmgbc = new GridBagConstraints();
        
		hmgbc.gridx = 0;
		hmgbc.gridy = 0;
		hmgbc.insets = new Insets (0,0,5,10);
		HistoryManager.add(SaveNewHistory, hmgbc);

		hmgbc.gridx = 0;
		hmgbc.gridy = 1;
		hmgbc.insets = new Insets (50,0,5,10);
		HistoryManager.add(DeleteSelected, hmgbc);

		hmgbc.gridx = 0;
		hmgbc.gridy = 2;
		hmgbc.insets = new Insets (0,0,5,10);
		HistoryManager.add(OverwriteSelected, hmgbc);

		hmgbc.gridx = 0;
		hmgbc.gridy = 3;
		hmgbc.insets = new Insets (0,0,5,10);
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
	
}
