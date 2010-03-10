package org.woped.qualanalysis.simulation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.woped.qualanalysis.simulation.controller.*;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.gui.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.woped.core.model.petrinet.SimulationModel;
import org.woped.translations.Messages; 

@SuppressWarnings("serial")
public class TokenGameHistoryManagerVC extends JDialog
{
	// 
	public static final int 	NAME_SAVE				= 100;
	public static final int 	NAME_UNSAVE_OVERWRITE	= 101;
	public static final int 	NAME_UNSAVE_CANCEL		= 102;
	
	//Declaration of the Panel(s)
	private JPanel       HistoryManager     = null;
	
	//Declaration of the Entry-Field
	private JTextField   NameEntry          = null;
	
	//Declaration of the Listbox
	private JTable        SavedHistory       = null;
	private JScrollPane  ScrollSavedHistory = null;
	private DefaultTableModel SavedHistoryContent = null;
	
	//Declaration of the Buttons
	private JButton      SaveNewHistory     = null;
	private JButton      DeleteSelected     = null;
	private JButton      OverwriteSelected  = null;
	private JButton      OpenSelected       = null;	
	
	//Declare Layout Items
	private GridBagConstraints hmgbc        = null;
	private int          ButtonSizeX        = 50;
	private int          ButtonSizeY        = 25;
	private int          ListSizeX          = 280;
	private int          ListSizeY          = 200;
	private int			 ColumnSizeDate		= 100;
	
	//Declare Variables For Resolving Buttonstatus
	private boolean		newHistory				= false;
	private boolean		savedHistorySelected	= false;
	
	//Declare Reference-Variables
	private TokenGameBarController   RemoteControl    = null;
	private SimulationModel          HistoryData      = null; 
	private ListSelectionModel       SelectedItems    = null;
	

	//Standard-Constructor
	public TokenGameHistoryManagerVC(IUserInterface mediator, TokenGameBarController RC)
	{
		super((JFrame)mediator, Messages.getTitle("Tokengame.HistoryManager"), true);
		RemoteControl = RC;
		this.add(addHistoryManagerPanel());
		this.initializeElementStatus();
		this.getRootPane().registerKeyboardAction(new TokenGameBarListener(TokenGameBarListener.HM_ESCAPE, RemoteControl, this), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		this.getRootPane().registerKeyboardAction(new TokenGameBarListener(TokenGameBarListener.HM_ENTER, RemoteControl, this), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		this.setLocationRelativeTo(null);
		this.setSize(380,300);
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
	    NameEntry.addCaretListener(new TokenGameBarListener(TokenGameBarListener.HM_NAME_CHANGED, RemoteControl, this));
	    
	    //Build Listbox
	    SavedHistoryContent = new DefaultTableModel()
		    {
		    	// Set all the Cells to not editable
	    		public boolean isCellEditable(int row, int column)
		    	{
		    		return false;
		    	}
		    };
		SavedHistoryContent.addColumn(Messages.getString("Tokengame.HistoryManager.TableHeader.Name"));
	    SavedHistoryContent.addColumn(Messages.getString("Tokengame.HistoryManager.TableHeader.Date"));
	    SavedHistory       = new JTable(SavedHistoryContent);
	    // Prevent that pressing enter passes the selection to the following table-row
	    SavedHistory.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "none");
	    
	    SavedHistory.getColumnModel().getColumn(0).setPreferredWidth(ListSizeX-ColumnSizeDate);
	    SavedHistory.getColumnModel().getColumn(1).setPreferredWidth(ColumnSizeDate);
	    SavedHistory.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    SavedHistory.getSelectionModel().addListSelectionListener(new TokenGameBarListener(TokenGameBarListener.HM_ELEMENT_SELECTED, RemoteControl, this));
	    SavedHistory.addMouseListener(new TokenGameBarListener(TokenGameBarListener.HM_TABLE_CLICKED,RemoteControl,this));
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
	
	/**
	 * initializes the status of the elements (such as buttons) to enabled or disabled 
	 */
	public void initializeElementStatus()
	{
		newHistory = RemoteControl.isNewHistory();
		SelectedItems = SavedHistory.getSelectionModel();	
		savedHistorySelected = !SelectedItems.isSelectionEmpty();
		
		if (newHistory)
		{
			NameEntry.setEnabled(true);
			if(NameEntry.getText().length() == 0)
				SaveNewHistory.setEnabled(false);
			else
				SaveNewHistory.setEnabled(true);
		}
		else
		{
			SaveNewHistory.setEnabled(false);
			NameEntry.setEnabled(false);
		}
		
		if (savedHistorySelected)
		{
			DeleteSelected.setEnabled(true);
		    OpenSelected.setEnabled(true);
		    if(newHistory)
		    	OverwriteSelected.setEnabled(true);
		    else
		    	OverwriteSelected.setEnabled(false);
		}
		else
		{
			DeleteSelected.setEnabled(false);
		    OverwriteSelected.setEnabled(false);
		    OpenSelected.setEnabled(false);
		}
		
		// set focus
		if(NameEntry.isEnabled())
		{
			NameEntry.requestFocus();
		}
		else
		{
			this.SavedHistory.requestFocus();
		}
	}
	
	public void saveHistory()
	{
		boolean save = false;
		int check_result = checkSaveName();
		if(check_result==NAME_SAVE)
		{
			save = true;
		}
		else if(check_result==NAME_UNSAVE_OVERWRITE)
		{
			if(getSelection() == -1)
			 {
				// save normal if there is no existing simulation selected 
				save = true;
			 }
			 else
			 {
				 RemoteControl.overwriteHistory(NameEntry.getText());
				 
				 // refresh the list of simulations
				 SavedHistoryContent.getDataVector().removeAllElements();
				 RemoteControl.initializeHistoryManagerSimulationlist(this);
				 
				 RemoteControl.setToOldHistory();
				 this.setVisible(false);
			 }
		}
		
		if(save)
		{
			if (newHistory)
			{
			 HistoryData = RemoteControl.getHistoryData();
			 Object[] HistoryDetails = {NameEntry.getText(),formatDate(RemoteControl.getHistoryData().getSavedDate())};
		     SavedHistoryContent.addRow(HistoryDetails);
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
	}
	
	/**
	 * Loads the selected Simulation from the List of saved Simulations directly into the
	 * HistoryBox of the TokenGameBar 
	 */
	public void openSelected()
	{
	  int LoadIndex = 0;
	  boolean load = true;
	  
	  // if a saved history is loaded and there exists an unsaved one which was currently recorded before opening the HistoryManager 
	  // then the user will be asked if he really wants to discard the currently recorded history
	  if(newHistory)
	  {
		  Object[] options = {Messages.getString("Dialog.Yes"),Messages.getString("Dialog.No")};
		  int answer = JOptionPane.showOptionDialog(this, Messages.getString("Tokengame.HistoryManager.WaringDiscardRecordedHistory"),Messages.getTitle("Tokengame.HistoryManager.WaringDiscardRecordedHistory"),  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		  if(answer != 0)
		  {
			  load = false;
		  }
	  }
	  
	  if(load)
	  {
		  //When a saved Simulation is loaded the mode must not be record-mode...
		  if(RemoteControl.isRecordSelected())
		  {
			  RemoteControl.getExpertView().doRecordClick();
		  }
		  
		  //Specify which Simulation has to be opened
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
		  if(SavedHistoryContent.getRowCount()!= 0)
		  {
		 	 boolean loaded = RemoteControl.loadHistory(LoadIndex);
		 	 if (loaded)
		 	 {
		 		 this.setVisible(false);
		 	 }
		  }
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
	public int checkSaveName()
	{				
		// no name set
		if(NameEntry.getText() == "")
		{
		   JOptionPane.showMessageDialog(this, Messages.getTitle("Tokengame.HistoryManager.ErrorNameEmpty"), Messages.getTitle("Tokengame.HistoryManager.SaveError"),  JOptionPane.ERROR_MESSAGE);
		   return TokenGameHistoryManagerVC.NAME_UNSAVE_CANCEL;
		}
		// name is set
		else
		{
			// there already exist saved historys
			if(!SavedHistoryContent.getDataVector().isEmpty())
			{
				// the selected name already exists => ask wether to overwrite or not
				boolean containsName = false;
				Vector<Vector> rows = (Vector)SavedHistoryContent.getDataVector();
				Iterator<Vector> iter = rows.iterator();
				while(iter.hasNext())
				{
					if(iter.next().elementAt(0).equals(NameEntry.getText()))
					{
						containsName = true;
					}
				}
				
				if(containsName)
				{
				   Object[] options = {Messages.getString("Dialog.Yes"),Messages.getString("Dialog.No")};
				   int answer = JOptionPane.showOptionDialog(this, Messages.getString("Tokengame.HistoryManager.WarningExistingName").replaceAll("##SIMULATIONNAME##", NameEntry.getText()),Messages.getTitle("Tokengame.HistoryManager.WarningExistingName"),  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				   // overwrite
				   if(answer == 0)
				   {
					   return NAME_UNSAVE_OVERWRITE;
				   }
				   // cancel, do not overwrite
				   else
				   {
					   return NAME_UNSAVE_CANCEL;
				   }
				}
				// the selected name doesn't already exist - save normally
				else
				{
	 		       return NAME_SAVE;
			   }
			}
			// there exist no other historys so far - save normally 
			else
			{
			   return NAME_SAVE;
		   }
		}
	}
		
	
	
	
	public TokenGameBarController getRemoteControl()
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
	
	/*
	 * sets the name of the selected simulation in the table to the NameEntry-field
	 */
	public void copySelectedNameToNameentry()
	{
		NameEntry.setText((String)SavedHistoryContent.getValueAt(SelectedItems.getMinSelectionIndex(), 0));
	}
	
	/*
	 * exposes the status (enabled or disabled) of the NameEntry-field
	 */
	public boolean isNameEntryEnabled()
	{
		return NameEntry.isEnabled();
	}
	
	/*
	 * exposes the status (enabled or disabled) of the SaveNewHistory-button which means a name is entered in the NameEnty-field.
	 */
	public boolean isSaveButtonEnabled()
	{
		return this.SaveNewHistory.isEnabled();
	}
	
	/**
	 * this method fills the history-List with the already existing Simulations 
	 */
	public void addLoadListItem(Object[] item)
	{
		item[1]=formatDate((Date)item[1]);
		SavedHistoryContent.addRow(item);	
	}
	
	/**
	 * removes a possibly deleted History-Item from the Load-List
	 * @param index
	 */
	public void removeLoadListItem(int index)
	{
		SavedHistoryContent.removeRow(index);
	}
	
	private String formatDate(Date d){
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, ConfigurationManager.getConfiguration().getLocale()).format(d);
	}
}
