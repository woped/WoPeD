// FIXME: This object must create a local copy of the
// petri-net or inhibit editing (become a modal dialog etc.)

package org.woped.qualanalysis;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.translations.Messages;

@SuppressWarnings("serial")
public class NetAnalysisDialog extends JDialog implements WindowListener{
	public NetAnalysisDialog(
			JFrame owner, 
			File temporaryFile, IEditor editor, AbstractApplicationMediator mediator)
	{	
		// Ignore the dialog owner and set our own instead to be able to change the dialog icon
		super(owner, Messages.getString("Analysis.Dialog.Title"), true);
		
		// Remember a reference to our model
		// We need it to deal with selections
		m_currentEditor = editor;
		
		// Instantiate our analysis object
		m_structuralAnalysis = new StructuralAnalysis(m_currentEditor);
		m_woflanAnalysis = new WoflanAnalysis(m_currentEditor, temporaryFile);
		
		
		setSize(640,480);
		// Center the window on the desktop
		setLocationRelativeTo(null);
		
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
		
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumUnboundedPlaces") + ": " + 
				m_woflanAnalysis.getNumUnboundedPlaces(),
				m_woflanAnalysis.getUnboundedPlacesIterator()) {
					// Unbounded places are not good and should trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});					
		
    	
		// Display unbounded sequences
    	// that is, transition sequences that will 
    	// keep increasing the number of total tokens
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumUnboundedSeq") + ": " +
    			m_woflanAnalysis.getNumUnboundedSequences(),
    			m_woflanAnalysis.getUnboundedSequencesIterator()) {
    		public String GetGroupDisplayString(int nIndex, Collection group) {
    			return Messages.getString("Analysis.Tree.UnboundedSeqNumTrans") + ":" + group.size();
    		}
    		// Unbounded sequences are not good and should trigger an error
    		public int GetInfoState() {
    			if (getChildCount()>0)
    				return InfoStateERROR;
    			else
    				return InfoStateOK;
    		}
    	});
	}
	
	private void BuildLivenessInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Liveness"));
		parent.add(current);
		// Show dead transitions
		// (transitions that will not ever be active for any marking)
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumDeadTrans") + ": " + 
				m_woflanAnalysis.getNumDeadTransitions(),
				m_woflanAnalysis.getDeadTransitionsIterator()) {
					// dead transitions are not good and should trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});					
		// Show zombie transitions
    	// (transitions that ain't quite dead yet)
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NumNonLiveTrans") + ": " + 
				m_woflanAnalysis.getNumNonLiveTransitions(),
				m_woflanAnalysis.getNonLiveTransitionsIterator()) {
					// non-live transitions are not good and should trigger an error
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});					
		// Display non-live sequences
    	// that is a sequence to "kill" a zombie transition
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumNonLiveSeq") + ": " +
    			m_woflanAnalysis.getNumNonLiveSequences(),
    			m_woflanAnalysis.getNonLiveSequencesIterator()) {
    		public String GetGroupDisplayString(int nIndex, Collection group) {
    			return Messages.getString("Analysis.Tree.NonLiveSeqNumTrans") + ":" + group.size();
    		}
    		// non-live sequences are not good and should trigger an error
    		public int GetInfoState() {
    			if (getChildCount()>0)
    				return InfoStateERROR;
    			else
    				return InfoStateOK;
    		}
    	});
	}
	
	private void BuildInvariantsInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Invariants"));
		parent.add(current);
		
		// Display the P-Invariants of this net
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumPInvariants") + ": " +
    			m_woflanAnalysis.getNumPInvariants(),
    			m_woflanAnalysis.getPInvariantsIterator()) {
    		public String GetGroupDisplayString(int nIndex, Collection group) {
    			return Messages.getString("Analysis.Tree.PInvNumPlaces") + ":" + group.size();
    		}
    		public int GetInfoState() {
    			return InfoStateInfo;
    		}
    	});				
		// Show the places that are not covered by any P-Invariant
		// of the net.
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.PInvNumUncoveredPlaces") + ": " + 
				m_woflanAnalysis.getNumNotPInvariantCovered(),
				m_woflanAnalysis.getNotPInvariantCoveredIterator()) {
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});			    	

    	// Display the Semi-positive P-Invariants of this net
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumNonnegPInvariants") + ": " +
    			m_woflanAnalysis.getNumNonNegPInvariants(),
    			m_woflanAnalysis.getNonNegPInvariantsIterator()) {
    		public String GetGroupDisplayString(int nIndex, Collection group) {
    			return Messages.getString("Analysis.Tree.NonnegPInvNumPlaces") + ":" + group.size();
    		}
    		public int GetInfoState() {
    			return InfoStateInfo;
    		}
    	});			
		// Show the places that are not covered by any 
		// Semi-positive P-Invariant
		// of the net.
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.NonnegPInvNumUncoveredPlaces") + ": " + 
				m_woflanAnalysis.getNumNotNonNegPInvariantCovered(),
				m_woflanAnalysis.getNotNonNegPInvariantCoveredIterator()) {
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});			    	
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
		
		// Display the S-Components of this net
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumSComponents") + ": " +
    			m_woflanAnalysis.getNumSComponents(),
    			m_woflanAnalysis.getSComponentsIterator()) {
    		public String GetGroupDisplayString(int nIndex, Collection group) {
    			return Messages.getString("Analysis.Tree.SComponentNumPlaces") + ":" + group.size();
    		}
    		public int GetInfoState() {
    			return InfoStateInfo;
    		}
    	});				

		// Show the places that are not covered by any S-Component
		// of the net. If such a place exists the net is not s-coverable
		current.add(new NodeGroupNetInfo(Messages.getString("Analysis.Tree.SCompUncoveredPlaces") + ": " + 
				m_woflanAnalysis.getNumNotSCovered(),
				m_woflanAnalysis.getNotSCoveredIterator()) {
					public int GetInfoState() {
						if (getChildCount()>0)
							return InfoStateERROR;
						else
							return InfoStateOK;
					}
				});	    	
	}	
	
	private void BuildHandleInformation(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo(Messages.getString("Analysis.Tree.Wellstructuredness"));
		parent.add(current);

		// FIXME: We currently have to use Woflan for P/T and T/P handle
		// detection as our own algorithm is broken
		// This will be fixed soon (@see StructuralAnalysis.java for details)
		
		// Display conflicts and parallelizations that
		// are not well-handled
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumPTHandles") + ": " +
    			m_woflanAnalysis.getNumPTHandles(),
    			m_woflanAnalysis.getPTHandlesIterator()) {
    		public String GetGroupDisplayString(int nIndex, Collection group) {
    			return Messages.getString("Analysis.Tree.PTHandlePair") + " #" + (nIndex+1);
    		}
    		public int GetInfoState() {
				if (getChildCount()>0)
					return InfoStateERROR;
				else
					return InfoStateOK;
    		}
    	});	
    	
    	current.add(new NodeGroupListNetInfo(Messages.getString("Analysis.Tree.NumTPHandles") + ": " +
    			m_woflanAnalysis.getNumTPHandles(),
    			m_woflanAnalysis.getTPHandlesIterator()) {
    		public String GetGroupDisplayString(int nIndex, Collection group) {
    			return Messages.getString("Analysis.Tree.TPHandlePair") + " #" + (nIndex+1);
    		}
    		public int GetInfoState() {
				if (getChildCount()>0)
					return InfoStateERROR;
				else
					return InfoStateOK;
    		}
    	});
    	
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
		LoggerManager.info(Constants.QUALANALYSIS_LOGGER, Messages.getString("Analysis.Tree.NotStronglyConnected") + " {");
		while (i.hasNext())
		{
			AbstractElementModel element = (AbstractElementModel)i.next();
			LoggerManager.info(Constants.QUALANALYSIS_LOGGER, element.getId() + "," + element.getNameValue());
		}
		LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "}");
		
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
		Cleanup();
	}	
	protected void finalize()
	{
		// Call cleanup if we happen to receive a finalize() call from the garbage collector
		Cleanup();		
	}
	
	protected void Cleanup()
	{
		// Call the woflan object's cleanup method to get rid of temporary files etc.
		if (m_woflanAnalysis!=null)
		{
			m_woflanAnalysis.Cleanup();
			m_woflanAnalysis = null;
		}
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
	//! Woflan analysis object
	//! that wraps around the thin woflan api
	private WoflanAnalysis m_woflanAnalysis;
	
	private GraphTreeModelSelector m_treeSelectionChangeHandler = null;
}
