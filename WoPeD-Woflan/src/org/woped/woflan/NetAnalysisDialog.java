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
import javax.swing.tree.DefaultMutableTreeNode;

import org.processmining.framework.models.petrinet.algorithms.Woflan;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.GraphTreeModelSelector;
import org.woped.editor.controller.vc.NetInfo;
import org.woped.editor.controller.vc.NodeGroupNetInfo;
import org.woped.editor.controller.vc.StructuralAnalysis;
import org.woped.editor.utilities.Messages;

@SuppressWarnings("serial")
public class NetAnalysisDialog extends JFrame implements WindowListener{
	public NetAnalysisDialog(File temporaryFile, IEditor editor, AbstractApplicationMediator mediator)
	{		
		super(Messages.getString("Analysis.Dialog.Title"));
		
		// Remember a reference to our model
		// We need it to deal with selections
		m_currentEditor = editor;
		
		// Instantiate our analysis object
		m_structuralAnalysis = new StructuralAnalysis(m_currentEditor);
		
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
        	setIconImage(Messages.getImageIcon("Analysis.Dialog").getImage());
        	
        	getContentPane().setLayout(new GridLayout(1,1));
        	// Add tree control to display the output of our WOFLAN library
        	DefaultMutableTreeNode top =
                new NetInfo(Messages.getString("Analysis.Tree.Title"));
        	m_treeObject = new JTree(top);
    		m_treeObject.setCellRenderer(new NetInfoTreeRenderer());        	
        	m_treeObject.setShowsRootHandles(true);
        	getContentPane().add(new JScrollPane(m_treeObject));
        	
    		m_treeSelectionChangeHandler = new GraphTreeModelSelector(m_currentEditor, m_treeObject, mediator, true);
        	// We need to know about selection changes inside the tree
        	m_treeObject.addTreeSelectionListener(m_treeSelectionChangeHandler);        	
        	
        	BuildBasicInfo(top);
        	BuildStructuralAnalysis(top);
        	BuildSemanticalAnalysis(top);
    	}		
    	
    	// Listen to close event to be able to dispose of our temporary file
    	addWindowListener(this);
    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}	
	
	private void BuildBasicInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.BasicInfo"));
		parent.add(current);
				
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumPlaces") + ": " +
														m_structuralAnalysis.getNumPlaces(),
														m_structuralAnalysis.getPlacesIterator()));
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumTransitions")  + ": " + 
														m_structuralAnalysis.getNumTransitions(),
														m_structuralAnalysis.getTransitionsIterator()));
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumOperators")  + ": " + 
														m_structuralAnalysis.getNumOperators(),
														m_structuralAnalysis.getOperatorsIterator()));
		current.add(new NetInfo(Messages.getString("Analysis.Tree.NumArcs")  + ": " + 
														m_structuralAnalysis.getNumArcs()));
	}
	
	private void BuildSemanticalAnalysis(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Behaviour"));
		parent.add(current);
		
		BuildInvariantsInfo(current);
		BuildSoundnessInfo(current);
	}
	
	private void BuildSoundnessInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Soundness"));
		parent.add(current);
		
		BuildBoundednessInfo(current);	
		BuildLivenessInfo(current);
	}
	
	private void BuildBoundednessInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Boundedness"));
		parent.add(current);
		
		// Calculate boundedness information
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetUnb, 0, 0);

		// Show unbounded places
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.NumUnboundedPlaces") + ": ", 
    			m_myWofLan.InfoNofUnbP,
    			"",
    			m_myWofLan.InfoUnbPName,
    			-1,
    			0,
    			false));
    	
		// Display unbounded sequences
    	// that is, transition sequences that will 
    	// keep increasing the number of total tokens
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumUnboundedSeq") + ": ",
				m_myWofLan.InfoNofUnbS,
				m_myWofLan.InfoUnbSNofT,
				Messages.getString("Analysis.Tree.UnboundedSeqNumTrans") + ": ",
				"",
				m_myWofLan.InfoUnbSTName,			
				-1, 
				0, 
				false));		
	}
	
	private void BuildLivenessInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Liveness"));
		parent.add(current);

		// Calculate Liveness information
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetNLive, 0, 0);

		// Show dead transitions
		// (transitions that will not ever be active for any marking)
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.NumDeadTrans") + ": ", 
    			m_myWofLan.InfoNofDeadT,
    			"",
    			m_myWofLan.InfoDeadTName,  			
    			-1,
    			0,
    			false));
		// Show zombie transitions
    	// (transitions that ain't quite dead yet)
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.NumNonLiveTrans") + ": ", 
    			m_myWofLan.InfoNofNLiveT,
    			"",
    			m_myWofLan.InfoNLiveTName,  			
    			-1,
    			0,
    			false));
		// Display non-live sequences
    	// that is a sequence to "kill" a zombie transition
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumNonLiveSeq") + ": ", 
				m_myWofLan.InfoNofNLiveS,
				m_myWofLan.InfoNLiveSNofT,
				Messages.getString("Analysis.Tree.NonLiveSeqNumTrans") + ": ",
				"",
				m_myWofLan.InfoNLiveSTName,			
				-1, 
				0, 
				false));		    					
	}
	
	private void BuildInvariantsInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Invariants"));
		parent.add(current);
		
		// Calculate all invariants information we can get
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetPInv, 0, 0);
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetSPIn, 0, 0);
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetTInv, 0, 0);
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetSTIn, 0, 0);
		
		// Display the P-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumPInvariants") + ": ", 
				m_myWofLan.InfoNofPInv,
				m_myWofLan.InfoPInvNofP,
				Messages.getString("Analysis.Tree.PInvNumPlaces") + ": ",
				"",
				m_myWofLan.InfoPInvPName,				
				-1, 
				1, 
				true));

		// Show the places that are not covered by any P-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.PInvNumUncoveredPlaces") + ": ",  
    			m_myWofLan.InfoNofPotPInv ,
    			"",
    			m_myWofLan.InfoNotPInvPName,  			
    			-1,
    			0,
    			false));				

    	// Display the Semi-positive P-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumNonnegPInvariants") + ": ", 
				m_myWofLan.InfoNofSPIn,
				m_myWofLan.InfoSPInNofP,
				Messages.getString("Analysis.Tree.NonnegPInvNumPlaces") + ": ",
				"",
				m_myWofLan.InfoSPInPName,			
				-1, 
				1, 
				true));

		// Show the places that are not covered by any 
		// Semi-positive P-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.NonnegPInvNumUncoveredPlaces") + ": ", 
    			m_myWofLan.InfoNofPotSPIn ,
    			"",
    			m_myWofLan.InfoNotSPInPName,   			
    			-1,
    			0,
    			false));

/*		
		// Display the T-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumTInvariants") + ": ", 
				m_myWofLan.InfoNofTInv,
				m_myWofLan.InfoTInvNofP,
				Messages.getString("Analysis.Tree.TInvNumTrans") + ": ",
				"",
				m_myWofLan.InfoTInvPName,
				-1, 
				1, 
				true));

		// Show the places that are not covered by any T-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.TInvNumUncoveredTrans") + ": ", 
    			m_myWofLan.InfoNofPotTInv ,
    			"",
    			m_myWofLan.InfoNotTInvPName,			
    			-1,
    			0,
    			false));				

    	// Display the Semi-positive T-Invariants of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumNonnegTInvariants") + ": ", 
				m_myWofLan.InfoNofSTIn,
				m_myWofLan.InfoSTInNofP,
				Messages.getString("Analysis.Tree.NonnegTInvNumTrans") + ": ",
				"",
				m_myWofLan.InfoSTInPName,			
				-1, 
				1, 
				true));

		// Show the places that are not covered by any 
		// Semi-positive T-Invariant
		// of the net.
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.NonnegTInvNumUncoveredTrans") + ": ", 
    			m_myWofLan.InfoNofPotSTIn ,
    			"",
    			m_myWofLan.InfoNotSTInPName,  			
    			-1,
    			0,
    			false));    	*/
	}

	private void BuildStructuralAnalysis(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Structural"));
		parent.add(current);

    	BuildWorkflowInfo(current);
 
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumFreechoiceViolations") + ": " + 
    			m_structuralAnalysis.getNumFreeChoiceViolations(),
    			m_structuralAnalysis.getFreeChoiceViolations()) {
    		public String GetGroupDisplayString(int nIndex, Collection gorup) {
    			return Messages.getString("Analysis.Tree.NonFreeChoiceGroup") + " " + (nIndex+1);
    		}
    		// Free-choice violations are not good and should trigger an error
    		public int GetInfoState() {
    			if (getChildCount()>0)
    				return InfoStateERROR;
    			else
    				return InfoStateOK;
    		}
    	});

    	current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumWrongOperators") + ": " + 
    			m_structuralAnalysis.getNumMisusedOperators(),
    			m_structuralAnalysis.getMisusedOperatorsIterator()) {
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
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.SComponents"));
		parent.add(current);
		
		// Create S-Component information
		m_myWofLan.Info(m_netHandle, m_myWofLan.SetSCom, 0, 0);
		
		// Display the S-Components of this net
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumSComponents") + ": ",
				m_myWofLan.InfoNofSCom,
				m_myWofLan.InfoSComNofN,
				Messages.getString("Analysis.Tree.SComponentNumPlaces") + ": ",
				"",
				m_myWofLan.InfoSComNName,				
				-1, 
				-1, 
				true));

		// Show the places that are not covered by any S-Component
		// of the net. If such a place exists the net is not s-coverable
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			Messages.getString("Analysis.Tree.SCompUncoveredPlaces") + ": ", 
    			m_myWofLan.InfoNofNotSCom ,
    			"",
    			m_myWofLan.InfoNotSComNName,   			
    			-1,
    			0,
    			false));		
	}	
	private void BuildHandleInformation(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Wellstructuredness"));
		parent.add(current);

		// FIXME: We currently have to use Woflan for P/T and T/P handle
		// detection as our own algorithm is broken
		// This will be fixed soon (@see StructuralAnalysis.java for details)
				
		// Yes, create well-handledness info
    	m_myWofLan.Info(m_netHandle, 
    			m_myWofLan.SetPTH, 0, 0);
    	m_myWofLan.Info(m_netHandle, 
    			m_myWofLan.SetTPH, 0, 0);
		
		// Display conflicts and parallelizations that
		// are not well-handled
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumPTHandles") + ": ",
				m_myWofLan.InfoNofPTH,
				m_myWofLan.InfoPTHNofN1,
				Messages.getString("Analysis.Tree.HandlePath") + ": ",
				"",
				m_myWofLan.InfoPTHN1Name,		
				-1, 
				0, 
				false));
		current.add(new MultipleGroupsNetInfo(m_currentEditor,
				this,
				Messages.getString("Analysis.Tree.NumTPHandles") + ": ",
				m_myWofLan.InfoNofTPH,
				m_myWofLan.InfoTPHNofN1,
				Messages.getString("Analysis.Tree.HandlePath") + ": ",
				"",
				m_myWofLan.InfoTPHN1Name,
				-1, 
				0, 
				false));
		
		/*
		// FIXME: We currently have to use Woflan for P/T and T/P handle
		// detection as our own algorithm is broken
		// This will be fixed soon (@see StructuralAnalysis.java for details)
		// Jan. 14th 2007, AE
		
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumHandles") + ": " +
    			m_structuralAnalysis.getNumWellStructurednessViolations(),
    			m_structuralAnalysis.getWellStructurednessViolations()) {
    		public String GetGroupDisplayString(int nIndex, Collection gorup) {
    			return Messages.getString("Analysis.Tree.HandlePair") + " " + (nIndex+1);
    		}
    		// Free-choice violations are not good and should trigger an error
    		public int GetInfoState() {
    			if (getChildCount()>0)
    				return InfoStateERROR;
    			else
    				return InfoStateOK;
    		}
    	});
    	*/	
	}
	
	private void BuildWorkflowInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.WorkflowNet"));
		parent.add(current);
		
		
		Iterator i = m_structuralAnalysis.getNotStronglyConnectedNodes();
		LoggerManager.info(Constants.WOFLAN_LOGGER, Messages.getString("Analysis.Tree.NotStronglyConnected") + " {");
		while (i.hasNext())
		{
			AbstractElementModel element = (AbstractElementModel)i.next();
			LoggerManager.info(Constants.WOFLAN_LOGGER, element.getId() + "," + element.getNameValue());
		}
		LoggerManager.info(Constants.WOFLAN_LOGGER, "}");
		
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSourcePlaces") + ": " + 
				m_structuralAnalysis.getNumSourcePlaces(),
				m_structuralAnalysis.getSourcePlacesIterator()) {
			// We want exactly one source place
			public int GetInfoState() {
				if (getChildCount()!=1)
					return InfoStateERROR;
				else
					return InfoStateOK;
			}
		});				
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSinkPlaces") + ": " +
				m_structuralAnalysis.getNumSinkPlaces(),
				m_structuralAnalysis.getSinkPlacesIterator()) {
					// We want exactly one sink place
					public int GetInfoState() {
						if (getChildCount()!=1)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});	
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSourceTrans") + ": " + 
				m_structuralAnalysis.getNumSourceTransitions(),
				m_structuralAnalysis.getSourceTransitionsIterator()) {
					// Source transitions are not good and should trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});					
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumSinkTrans") + ": " + 
				m_structuralAnalysis.getNumSinkTransitions(),
				m_structuralAnalysis.getSinkTransitionsIterator()) {
					// Sink transitions are not good and should trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});	

		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumUnconnectedNodes") + ": " + + 
				m_structuralAnalysis.getNumNotConnectedNodes(),
				m_structuralAnalysis.getNotConnectedNodes()) {
					// Any unconnected nodes must trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});
		
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumNotStronglyConnectedNodes") + ": " 
				+ m_structuralAnalysis.getNumNotStronglyConnectedNodes(),
				m_structuralAnalysis.getNotStronglyConnectedNodes()) {
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
		// Before closing the window, deselect all tree elements
		// to clear highlighting 
		m_treeObject.clearSelection();
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
