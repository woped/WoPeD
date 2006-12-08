package org.woped.editor.controller.vc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;

public class GraphTreeModelSelector implements TreeSelectionListener {

	public GraphTreeModelSelector(IEditor e) {
		m_currentEditor = e;
	}

	//! Called when the selection of the tree changes
	//! Note that we will not add/remove selections but
	//! rebuild all of them from scratch
	public void valueChanged(TreeSelectionEvent e) {
		if (e.getSource() instanceof JTree)
		{
			// We need to know the current selection
			TreeSelectionModel currentSelection = ((JTree)e.getSource()).getSelectionModel();
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
			ArrayList newSelection = new ArrayList();
			for (Iterator i=processedSelection.iterator();i.hasNext();)
			{
				Object currentObject = i.next();
				newSelection.add(currentObject);
			}
			// Need to have an intermediary for our new selection
			// as for some reason addSelection will clear all
			// previously selected items
			currentGraph.setSelectionCells(newSelection.toArray());
		}
	}
	//! Remember a pointer to the currently active editor
	//! (the one for which this window was created)
	//! This is the central access point for model, graph etc.
	private IEditor m_currentEditor;	
}
