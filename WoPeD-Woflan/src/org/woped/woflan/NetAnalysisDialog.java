// FIXME: This object must create a local copy of the
// petri-net or inhibit editing (become a modal dialog etc.)

package org.woped.woflan;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.processmining.framework.models.petrinet.algorithms.Woflan;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.GraphTreeModelSelector;
import org.woped.editor.controller.vc.NetInfo;
import org.woped.editor.controller.vc.NodeGroupNetInfo;
import org.woped.editor.utilities.Messages;


public class NetAnalysisDialog extends JFrame implements WindowListener{
	public NetAnalysisDialog(File temporaryFile, IEditor editor)
	{		
		super("Analysis Dialog");
		
		// Remember a reference to our model
		// We need it to deal with selections
		m_currentEditor = editor;
		
		// Instantiate our analysis object
		m_structuralAnalysis = new StructuralAnalysis(m_currentEditor);
		m_treeSelectionChangeHandler = new GraphTreeModelSelector(m_currentEditor);
		
    	// This code will try to talk to WofLan
    	// through the JNI
		m_tempFile = temporaryFile;
		m_myWofLan = new Woflan();
		
    	// Open previously exported petri-net
    	m_netHandle = m_myWofLan.Open( m_tempFile.getAbsolutePath());
    	if (m_netHandle!=-1)
    	{
        	setSize(640,480);
        	// Center the window on the desktop
        	setLocationRelativeTo(null);
        	setIconImage(Messages.getImageIcon("ToolBar.Woped").getImage());
        	
        	getContentPane().setLayout(new GridLayout(1,1));
        	// Add tree control to display the output of our WOFLAN library
        	DefaultMutableTreeNode top =
                new NetInfo("Net Analysis");
        	m_treeObject = new JTree(top);
    		m_treeObject.setCellRenderer(new NetInfoTreeRenderer());        	
        	m_treeObject.setShowsRootHandles(true);
        	getContentPane().add(new JScrollPane(m_treeObject));
        	
        	BuildBasicInfo(top);
        	BuildStructuralAnalysis(top);
        	BuildSemanticalAnalysis(top);
    	}		
    	
    	// Listen to close event to be able to dispose of our temporary file
    	addWindowListener(this);
    	// We need to know about selection changes inside the tree
    	m_treeObject.addTreeSelectionListener(m_treeSelectionChangeHandler);
    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}	
	
	private void BuildBasicInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Basic net information");
		parent.add(current);
				
		current.add(new NodeGroupNetInfo("Number of places: " + m_structuralAnalysis.GetNumPlaces(),
				m_structuralAnalysis.GetPlacesIterator()));
		current.add(new NodeGroupNetInfo("Number of transitions: " + m_structuralAnalysis.GetNumTransitions(),
				m_structuralAnalysis.GetTransitionsIterator()));
		current.add(new NodeGroupNetInfo("Number of operators: " + m_structuralAnalysis.GetNumOperators(),
				m_structuralAnalysis.GetOperatorsIterator()));
		current.add(new NetInfo("Number of arcs: "+ m_structuralAnalysis.GetNumArcs()));
	}
	
	private void BuildSemanticalAnalysis(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Semantical Analysis");
		parent.add(current);
		
		BuildInvariantsInfo(current);
		BuildSoundnessInfo(current);
	}
	
	private void BuildSoundnessInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Soundness checks");
		parent.add(current);
		
		BuildBoundednessInfo(current);	
		BuildLivenessInfo(current);
	}
	
	private void BuildBoundednessInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Boundedness Info");
		parent.add(current);
		
		// Calculate boundedness information
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetUnb, 0, 0);

		// Show unbounded places
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of unbounded places: ", 
    			m_myWofLan.InfoNofUnbP,
    			"",
    			m_myWofLan.InfoUnbPName,
    			
    			-1,
    			0,
    			false
    	));
    	
		// Display unbounded sequences
    	// that is, transition sequences that will 
    	// keep increasing the number of total tokens
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of unbounded sequences: ",
				m_myWofLan.InfoNofUnbS,
				m_myWofLan.InfoUnbSNofT,
				"Unbounded sequence, Number of transitions: ",
				"",
				m_myWofLan.InfoUnbSTName,
				
				-1, 0, false));		
	}
	
	private void BuildLivenessInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Liveness Info");
		parent.add(current);

		// Calculate Liveness information
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetNLive, 0, 0);

		// Show dead transitions
		// (transitions that will not ever be active for any marking)
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of dead transitions: ", 
    			m_myWofLan.InfoNofDeadT,
    			"",
    			m_myWofLan.InfoDeadTName,
    			
    			-1,
    			0,
    			false
    	));
		// Show zombie transitions
    	// (transitions that ain't quite dead yet)
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of non-live transitions: ", 
    			m_myWofLan.InfoNofNLiveT,
    			"",
    			m_myWofLan.InfoNLiveTName,
    			
    			-1,
    			0,
    			false
    	));
		// Display non-live sequences
    	// that is a sequence to "kill" a zombie transition
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of non-live sequences: ",
				m_myWofLan.InfoNofNLiveS,
				m_myWofLan.InfoNLiveSNofT,
				"Non-live sequence, Number of transitions: ",
				"",
				m_myWofLan.InfoNLiveSTName,
				
				-1, 0, false));		    					
	}
	
	private void BuildInvariantsInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Invariants information");
		parent.add(current);
		
		// Calculate all invariants information we can get
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetPInv, 0, 0);
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetSPIn, 0, 0);
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetTInv, 0, 0);
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetSTIn, 0, 0);
		
		// Display the P-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of P-Invariants: ",
				m_myWofLan.InfoNofPInv,
				m_myWofLan.InfoPInvNofP,
				"P-Invariant, Number of elements: ",
				"",
				m_myWofLan.InfoPInvPName,
				
				-1, 1, true));

		// Show the places that are not covered by any P-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of places not covered by P-Invariant: ", 
    			m_myWofLan.InfoNofPotPInv ,
    			"",
    			m_myWofLan.InfoNotPInvPName,
    			
    			-1,
    			0,
    			false
    	));				

    	// Display the Semi-positive P-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of semi-positive P-Invariants: ",
				m_myWofLan.InfoNofSPIn,
				m_myWofLan.InfoSPInNofP,
				"Semi-positive P-Invariant, Number of elements: ",
				"",
				m_myWofLan.InfoSPInPName,
				
				-1, 1, true));

		// Show the places that are not covered by any 
		// Semi-positive P-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of places not covered by semi-positive P-Invariant: ", 
    			m_myWofLan.InfoNofPotSPIn ,
    			"",
    			m_myWofLan.InfoNotSPInPName,
    			
    			-1,
    			0,
    			false
    	));

		
		// Display the T-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of T-Invariants: ",
				m_myWofLan.InfoNofTInv,
				m_myWofLan.InfoTInvNofP,
				"T-Invariant, Number of elements: ",
				"",
				m_myWofLan.InfoTInvPName,
				
				-1, 1, true));

		// Show the places that are not covered by any T-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of transitions not covered by T-Invariant: ", 
    			m_myWofLan.InfoNofPotTInv ,
    			"",
    			m_myWofLan.InfoNotTInvPName,
    			
    			-1,
    			0,
    			false
    	));				

    	// Display the Semi-positive T-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of semi-positive T-Invariants: ",
				m_myWofLan.InfoNofSTIn,
				m_myWofLan.InfoSTInNofP,
				"Semi-positive T-Invariant, Number of elements: ",
				"",
				m_myWofLan.InfoSTInPName,
				
				-1, 1, true));

		// Show the places that are not covered by any 
		// Semi-positive P-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number places not covered by semi-positive T-Invariant: ", 
    			m_myWofLan.InfoNofPotSTIn ,
    			"",
    			m_myWofLan.InfoNotSTInPName,
    			
    			-1,
    			0,
    			false
    	));    	
	}

	private void BuildStructuralAnalysis(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Structural Analysis");
		parent.add(current);

    	BuildWorkflowInfo(current);
 
    	current.add(new NodeGroupListNetInfo("Number of free-choice violations: "+ m_structuralAnalysis.GetNumFreeChoiceViolations(),
    			m_structuralAnalysis.GetFreeChoiceViolations()) {
    		public String GetGroupDisplayString(int nIndex, Collection gorup) {
    			return "Non-free-choice group " + (nIndex+1);
    		}
    		// Free-choice violations are not good and should trigger an error
    		public int GetInfoState() {
    			if (getChildCount()>0)
    				return InfoStateERROR;
    			else
    				return InfoStateOK;
    		}
    	});

    	current.add(new NodeGroupNetInfo("Number of operators with wrong arc configuration: "+ m_structuralAnalysis.GetNumMisusedOperators(),
    			m_structuralAnalysis.GetMisusedOperatorsIterator()) {
			// We want exactly one source place
			public int GetInfoState() {
				if (getChildCount()>0)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
    	});
    	
    	BuildHandleInformation(current);
    	BuildSComponentInformation(current);
	}
	private void BuildSComponentInformation(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("S-Components");
		parent.add(current);
		
		// Create S-Component information
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetSCom, 0, 0);
		
		// Display the S-Components of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of S-Components: ",
				m_myWofLan.InfoNofSCom,
				m_myWofLan.InfoSComNofN,
				"S-Component, Number of elements: ",
				"",
				m_myWofLan.InfoSComNName,
				
				-1, -1, true));

		// Show the places that are not covered by any S-Component
		// of the net. If such a place exists the net is not s-coverable
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number places not covered by S-Component: ", 
    			m_myWofLan.InfoNofNotSCom ,
    			"",
    			m_myWofLan.InfoNotSComNName,
    			
    			-1,
    			0,
    			false
    	));		
	}	
	private void BuildHandleInformation(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Well-Structuredness");
		parent.add(current);

		/* Just commented out for now, might need it later
		// Yes, create well-handledness info
    	m_myWofLan.Info(m_netHandle, 
    			m_myWofLan.SetPTH, 0, 0);
    	m_myWofLan.Info(m_netHandle, 
    			m_myWofLan.SetTPH, 0, 0);
		
		// Display conflicts and parallelizations that
		// are not well-handled
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of PT-Handles: ",
				m_myWofLan.InfoNofPTH,
				m_myWofLan.InfoPTHNofN1,
				"Non-well-handled path, Number of elements: ",
				"",
				m_myWofLan.InfoPTHN1Name,
				
				-1, 0, false));
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				"Number of TP-Handles: ",
				m_myWofLan.InfoNofTPH,
				m_myWofLan.InfoTPHNofN1,
				"Non-well-handled path, Number of elements: ",
				"",
				m_myWofLan.InfoTPHN1Name,
				
				-1, 0, false));
		*/
		
    	current.add(new NodeGroupListNetInfo("Number of PT/TP handles: "+ m_structuralAnalysis.GetNumWellStructurednessViolations(),
    			m_structuralAnalysis.GetWellStructurednessViolations()) {
    		public String GetGroupDisplayString(int nIndex, Collection gorup) {
    			return "PT/TP handle group " + (nIndex+1);
    		}
    		// Free-choice violations are not good and should trigger an error
    		public int GetInfoState() {
    			if (getChildCount()>0)
    				return InfoStateERROR;
    			else
    				return InfoStateOK;
    		}
    	});
    	
		
	}
	
	private void BuildWorkflowInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Workflow-Property");
		parent.add(current);
		
		
		Iterator i = m_structuralAnalysis.GetNotStronglyConnectedNodes();
		LoggerManager.info(Constants.WOFLAN_LOGGER, "Not strongly connected {");
		while (i.hasNext())
		{
			AbstractElementModel element = (AbstractElementModel)i.next();
			LoggerManager.info(Constants.WOFLAN_LOGGER, element.getId() + "," + element.getNameValue());
		}
		LoggerManager.info(Constants.WOFLAN_LOGGER, "}");
		
		current.add(new NodeGroupNetInfo("Number of source places: " + m_structuralAnalysis.GetNumSourcePlaces(),
				m_structuralAnalysis.GetSourcePlacesIterator()) {
			// We want exactly one source place
			public int GetInfoState() {
				if (getChildCount()!=1)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		});				
		current.add(new NodeGroupNetInfo("Number of sink places: " + m_structuralAnalysis.GetNumSinkPlaces(),
				m_structuralAnalysis.GetSinkPlacesIterator()) {
					// We want exactly one sink place
					public int GetInfoState() {
						if (getChildCount()!=1)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});	
		current.add(new NodeGroupNetInfo("Number of source transitions: " + m_structuralAnalysis.GetNumSourceTransitions(),
				m_structuralAnalysis.GetSourceTransitionsIterator()) {
					// Source transitions are not good and should trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});					
		current.add(new NodeGroupNetInfo("Number of sink transitions: " + m_structuralAnalysis.GetNumSinkTransitions(),
				m_structuralAnalysis.GetSinkTransitionsIterator()) {
					// Sink transitions are not good and should trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});	

		current.add(new NodeGroupNetInfo("Number of unconnected nodes: " + m_structuralAnalysis.GetNumNotConnectedNodes(),
				m_structuralAnalysis.GetNotConnectedNodes()) {
					// Any unconnected nodes must trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});
		
		current.add(new NodeGroupNetInfo("Number of not strongly connected nodes: " + m_structuralAnalysis.GetNumNotStronglyConnectedNodes(),
				m_structuralAnalysis.GetNotStronglyConnectedNodes()) {
					// Any nodes that are not strongly connected must trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});	
	}
	public void windowClosing(WindowEvent e) {
		// When receiving a windowClosing() event we will
		// initiate immediate disposal of the affected dialog
		dispose();
	}	
	public void windowClosed(WindowEvent e) {
		// Call our cleanup method to get rid of temporary files etc.
		Cleanup();
	}	
	
	//! @{
	//! Some dummy implementations to fulfill the requirements of the WindowListener interface
	public void windowOpened(WindowEvent e) {}	
	public void windowIconified(WindowEvent e) {}	
	public void windowDeiconified(WindowEvent e) {}	
	public void windowActivated(WindowEvent e) {}	
	public void windowDeactivated(WindowEvent e) {}	
	public void windowGainedFocus(WindowEvent e) {}
	public void windowLostFocus(WindowEvent e) {}
	public void windowStateChanged(WindowEvent e) {}	
	//! @}
	

	
	//! Clean up when done...
	protected void Cleanup()
	{
		if (m_netHandle != -1) {
			m_myWofLan.Close(m_netHandle);
			m_netHandle = -1;
		}
    	// Delete the temporary file containing the petri-net dump
		if (m_tempFile!=null) {    	
			m_tempFile.delete();
			m_tempFile = null;
		}
    	
        LoggerManager.info(Constants.WOFLAN_LOGGER, "Deleted temporary file for Workflow analysis.");
    	
	}
	protected void finalize()
	{
		// Call cleanup if we happen to receive a finalize() call from the garbage collector
		Cleanup();		
	}
	//! Stores a private reference to the WOFLAN analysis DLL
	public Woflan m_myWofLan=null;
	//! Remember a handle to the petri-net that is currently open
	public int m_netHandle=-1;
	//! Remember a reference to the temporary file 
	//! containing a dump of our net
	//! It will be deleted when no longer needed (when this object is destroyed)
	private File   m_tempFile;

	//! Remember a pointer to the currently active editor
	//! (the one for which this window was created)
	//! This is the central access point for model, graph etc.
	private IEditor m_currentEditor;

	//! Remember a reference to the tree object
	private JTree m_treeObject;
	
	//! Structural analysis is performed
	//! without woflan.
	//! This object implements all the necessary algorithms
	private StructuralAnalysis m_structuralAnalysis;
	
	private GraphTreeModelSelector m_treeSelectionChangeHandler = null;
}
