package org.woped.woflan;

import java.io.File;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.processmining.framework.models.petrinet.algorithms.Woflan;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.model.*;

import org.woped.core.controller.*;
import org.woped.woflan.NetInfoTreeRenderer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class NetAnalysisDialog extends JFrame implements WindowListener, TreeSelectionListener{
	public NetAnalysisDialog(File temporaryFile, IEditor editor)
	{		
		super("Analysis Dialog");
		
		// Remember a reference to our model
		// We need it to deal with selections
		m_currentEditor = editor;
    	// This code will try to talk to WofLan
    	// through the JNI
		m_tempFile = temporaryFile;
		m_myWofLan = new Woflan();
		
    	// Open previously exported petri-net
    	m_netHandle = m_myWofLan.Open( m_tempFile.getAbsolutePath());
    	if (m_netHandle!=-1)
    	{
        	setSize(640,480);
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
    	}		
    	
    	// Listen to close event to be able to dispose of our temporary file
    	addWindowListener(this);
    	// We need to know about selection changes inside the tree
    	m_treeObject.addTreeSelectionListener(this);
    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}	
	private void BuildBasicInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Basic net information");
		parent.add(current);
				
    	// Now get some information about the net
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of places: ", 
    			m_myWofLan.InfoNofP, 
    			"",
    			m_myWofLan.InfoPName,-1,-1,true));
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of transitions: ", 
    			m_myWofLan.InfoNofT,
    			"",
    			m_myWofLan.InfoTName,-1,-1,true));			
    	current.add(new UnaryNetInfo(m_currentEditor, this,
    			"Number of arcs: ", 
    			m_myWofLan.InfoNofC, 0, 0));			
	}
	private void BuildStructuralAnalysis(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Structural Analysis");
		parent.add(current);

    	BuildWorkflowInfo(current);
    	// Enable the creation of non-free choice info
    	m_myWofLan.Info(m_netHandle, 
    			m_myWofLan.SetNFCC, 0, 0);
    	current.add(new MultipleGroupsNetInfo(m_currentEditor,
    			this, 
    			"Number of non-free choice clusters: ",
    			m_myWofLan.InfoNofNFCC,
    			m_myWofLan.InfoNFCCNofN,
    			"Non-free choice cluster, Number of elements: ",
    			"",
    			m_myWofLan.InfoNFCCNName,
    			
    			-1,
    			0,
    			false));   
    	BuildHandleInformation(current);
		
	}
	private void BuildHandleInformation(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Well-Handledness");
		parent.add(current);

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
		
	}
	
	private void BuildWorkflowInfo(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode current = new NetInfo("Workflow Analysis");
		parent.add(current);
				
    	// Enumerate source places
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of source places: ", 
    			m_myWofLan.InfoNofSrcP ,
    			"",
    			m_myWofLan.InfoSrcPName,
    			
    			1,
    			1,
    			false
    	));
    	// Enumerate sink places
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of sink places: ", 
    			m_myWofLan.InfoNofSnkP ,
    			"",
    			m_myWofLan.InfoSnkPName,
    			
    			1,
    			1,
    			false
    	));
    	// Enumerate source transitions
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of source transitions: ", 
    			m_myWofLan.InfoNofSrcT ,
    			"",
    			m_myWofLan.InfoSrcTName,
    			0,0,false));
    	// Enumerate sink transitions
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of sink transitions: ", 
    			m_myWofLan.InfoNofSnkT ,
    			"",
    			m_myWofLan.InfoSnkTName,0,0,false));

    	// Determine connectedness
    	m_myWofLan.Info(m_netHandle, 
    			m_myWofLan.SetSUnc, 0, 0);
    	
    	// Show nodes that are not connected to the source 
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of unconnected nodes: ",
    			m_myWofLan.InfoNofUncN,
    			"",
    			m_myWofLan.InfoUncNName,-1,0,false));
    	// Show nodes that are not strongly connected to the source
    	current.add(new GroupNetInfo(m_currentEditor, this,
    			"Number of not strongly connected nodes: ",
    			m_myWofLan.InfoNofSncN,
    			"",
    			m_myWofLan.InfoSncNName,-1,0,false));    	
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
	
	//! Called when the selection of the tree changes
	//! Note that we will not add/remove selections but
	//! rebuild all of them from scratch
	public void valueChanged(TreeSelectionEvent e) {
		// We need to know the current selection
		TreeSelectionModel currentSelection = m_treeObject.getSelectionModel();
		LoggerManager.info(Constants.WOFLAN_LOGGER, "Selection changed");
		// We now create a set of selected graph elements
		// (some elements may be selected twice so we have to 
		// eliminate those double entries)
		TreePath selection[] = currentSelection.getSelectionPaths();
		HashSet processedSelection = new HashSet();
		for (int i=0;(selection!=null)&&(i<selection.length);++i)
		{
			// One tree node can reference more than one petri-net
			// element that is to be selected
			NetInfo current = (NetInfo)selection[i].getLastPathComponent();
			Object[] sme = current.getReferencedElements();
			for (int j=0;j<sme.length;++j)
				processedSelection.add(sme[j]);			
		}
		AbstractGraph currentGraph = m_currentEditor.getGraph();
		// First, clear the current selection 
		currentGraph.clearSelection();
		// Finally, select all elements selected in the tree view
		ModelElementContainer elements
			= m_currentEditor.getModelProcessor().getElementContainer();
		ArrayList newSelection = new ArrayList();
		LoggerManager.info(Constants.WOFLAN_LOGGER, "New Selection: {");
		for (Iterator i=processedSelection.iterator();i.hasNext();)
		{
			Object currentObject = i.next();
			newSelection.add(currentObject);
			LoggerManager.info(Constants.WOFLAN_LOGGER, currentObject.toString());
		}
		LoggerManager.info(Constants.WOFLAN_LOGGER, "}\n");
		// Need to have an intermediary for our new selection
		// as for some reason addSelection will clear all
		// previously selected items
		currentGraph.setSelectionCells(newSelection.toArray());
	}
	
	//! Clean up when done...
	protected void Cleanup()
	{
    	m_myWofLan.Close(m_netHandle);
    	m_netHandle = -1;
    	// Delete the temporary file containing the petri-net dump
    	m_tempFile.delete();
    	
        LoggerManager.info(Constants.WOFLAN_LOGGER, "Deleted temporary file for Workflow analysis.");
    	
	}
	protected void finalize()
	{
		// Call cleanup if we happen to receive a finalize() call from the garbage collector
		Cleanup();		
	}
	//! Stores a private reference to the WOFLAN analysis DLL
	public Woflan m_myWofLan;
	//! Remember a handle to the petri-net that is currently open
	public int m_netHandle;
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
}
