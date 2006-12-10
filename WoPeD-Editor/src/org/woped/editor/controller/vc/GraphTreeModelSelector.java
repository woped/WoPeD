package org.woped.editor.controller.vc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.SubProcessModel;

public class GraphTreeModelSelector implements TreeSelectionListener {

	public GraphTreeModelSelector(IEditor e, AbstractApplicationMediator mediator) {
		m_currentEditor = e; 
		m_mediator = mediator;
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
			HashSet<Object> processedSelection = new HashSet<Object>();
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
			ArrayList<AbstractPetriNetModelElement> newSelection = new ArrayList<AbstractPetriNetModelElement>();
			for (Iterator i=processedSelection.iterator();i.hasNext();)
			{
				AbstractPetriNetModelElement currentObject = (AbstractPetriNetModelElement)i.next();
				// Comparing only the IDs is too risky because we may crash in this case if by
				// accident an id name is not unique.
				// Rather, look up by ID and see if the references match. This if proof
				// that we're actually talking about the same element
				if (m_currentEditor.getModelProcessor().getElementContainer().getElementById(
						((currentObject).getId()))==currentObject)
					// The selected element is part of the currently edited net
					newSelection.add(currentObject);
				else
				{
					// The selected element is not part of the currently edited net
					// We will try to open a corresponding sub-net and 
					// see if we can select it there
					AbstractElementModel parent = currentObject.getRootOwningContainer().getOwningElement();
					if (parent instanceof SubProcessModel)
					{
						IEditor newEditor = m_mediator.createSubprocessEditor(m_currentEditor.getModelProcessor().getProcessorType(),
								true, m_currentEditor, (SubProcessModel)parent);
						// Select in the new editor
						newEditor.getGraph().addSelectionCell(currentObject);
					}
				}
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
	//! Required to be able to open sub processes as required
	private AbstractApplicationMediator m_mediator;
}
