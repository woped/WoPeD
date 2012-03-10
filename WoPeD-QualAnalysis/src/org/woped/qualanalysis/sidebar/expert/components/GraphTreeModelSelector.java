package org.woped.qualanalysis.sidebar.expert.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.SubProcessModel;

public class GraphTreeModelSelector implements TreeSelectionListener, GraphSelectionListener {

	public GraphTreeModelSelector(IEditor e, JTree treeObject, AbstractApplicationMediator mediator,
			boolean highlightOnly) {
		m_currentEditor = e;
		m_treeObject = treeObject;
		m_mediator = mediator;
		this.highlightOnly = highlightOnly;
	}

	private boolean valueChangedActive = false;

	// ! Called when the selection of the tree changes
	// ! Note that we will not add/remove selections but
	// ! rebuild all of them from scratch
	public void valueChanged(TreeSelectionEvent e) {
		if (valueChangedActive)
			return;

		// We now create a set of selected graph elements
		// (some elements may be selected twice so we have to
		// eliminate those double entries)
		TreePath selection[] = m_treeObject.getSelectionPaths();
		HashSet<Object> processedSelection = new HashSet<Object>();
		for (int i = 0; (selection != null) && (i < selection.length); ++i) {
			// One tree node can reference more than one petri-net
			// element that is to be selected
			NetInfo current = (NetInfo) selection[i].getLastPathComponent();
			Object[] sme = current.getReferencedElements();
			for (int j = 0; j < sme.length; ++j)
				processedSelection.add(sme[j]);
		}
		AbstractGraph currentGraph = m_currentEditor.getGraph();

		// Finally, select all elements selected in the tree view
		ArrayList<AbstractPetriNetElementModel> newSelection = new ArrayList<AbstractPetriNetElementModel>();
		for (Iterator<Object> i = processedSelection.iterator(); i.hasNext();) {
			AbstractPetriNetElementModel currentObject = (AbstractPetriNetElementModel) i.next();
			// Comparing only the IDs is too risky because we may crash in this
			// case if by
			// accident an id name is not unique.
			// Rather, look up by ID and see if the references match. This if
			// proof
			// that we're actually talking about the same element
			if (m_currentEditor.getModelProcessor().getElementContainer().getElementById(((currentObject).getId())) == currentObject)
				// The selected element is part of the currently edited net
				newSelection.add(currentObject);
			else {
				// The selected element is not part of the currently edited net
				// We will try to open a corresponding sub-net and
				// see if we can select it there
				AbstractPetriNetElementModel parent = currentObject.getRootOwningContainer().getOwningElement();
				if (parent instanceof SubProcessModel) {
					IEditor newEditor = m_mediator.createSubprocessEditor(true, m_currentEditor, (SubProcessModel) parent);
					// Select in the new editor
					newEditor.getGraph().addSelectionCell(currentObject);
				}
			}
		}
		if (highlightOnly == false) {
			valueChangedActive = true;
			// Need to have an intermediary for our new selection
			// as for some reason addSelection will clear all
			// previously selected items
			currentGraph.setSelectionCells(newSelection.toArray());
			valueChangedActive = false;
		} else {
			highlightElements(newSelection.iterator());
		}
	}

	public void valueChanged(GraphSelectionEvent arg0) {
		if (valueChangedActive)
			// Do not call ourselves endlessly
			// We have to make a call to setSelectionCells()
			// which once again would trigger this method call
			// This is by design.
			return;

		// Before doing anything else,
		// select all PetriNetModelElement objects
		// in the tree view
		Object cells[] = m_currentEditor.getGraph().getSelectionCells();

		// The first thing we do is creating a mapping from
		// petri net elements to tree items
		// We will use it below to select the tree item that
		// corresponds to the selected petri net element
		TreeModel treemodel = m_treeObject.getModel();
		Map<AbstractPetriNetElementModel, NodeNetInfo> treeItemMap = new HashMap<AbstractPetriNetElementModel, NodeNetInfo>();
		Object treeRoot = treemodel.getRoot();
		int nNumItems = treemodel.getChildCount(treeRoot);
		for (int i = 0; i < nNumItems; ++i) {
			Object currentChild = treemodel.getChild(treeRoot, i);
			if (currentChild instanceof NodeNetInfo) {
				NodeNetInfo currentNode = (NodeNetInfo) currentChild;
				Object selElements[] = currentNode.getReferencedElements();
				if ((selElements.length > 0) && (selElements[0] instanceof AbstractPetriNetElementModel)) {
					// Assign node to first selected element
					treeItemMap.put((AbstractPetriNetElementModel) selElements[0], currentNode);
				}
			}
		}

		ArrayList<TreePath> newSelection = new ArrayList<TreePath>();
		// Temporary selection path for adding / removing to the tree selection
		// path
		// The first element in the path is always the tree root
		// The second element will be set as needed
		Object selectionPath[] = new Object[2];
		selectionPath[0] = treeRoot;

		for (int i = 0; i < cells.length; ++i) {
			Object currentCell = cells[i];
			// Ignore everything but group model selections
			if (currentCell instanceof GroupModel)
				currentCell = ((GroupModel) currentCell).getMainElement();
			if (currentCell instanceof AbstractPetriNetElementModel) {
				AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) currentCell;
				NodeNetInfo correspondingTreeItem = treeItemMap.get(current);
				if (correspondingTreeItem != null) {
					// We found a corresponding tree item
					// Build a selection tree
					selectionPath[1] = correspondingTreeItem;
					TreePath treePath = new TreePath(selectionPath);
					// Either add or remove this selection from the tree
					newSelection.add(treePath);
				}
			}
		}
		valueChangedActive = true;
		TreePath newPaths[] = new TreePath[1];
		newPaths = (newSelection.toArray(newPaths));
		m_treeObject.setSelectionPaths(newPaths);
		valueChangedActive = false;
	}

	public void highlightElements(Iterator<AbstractPetriNetElementModel> j) {
		// First, de-highlight all elements
		Iterator<AbstractPetriNetElementModel> i = m_currentEditor.getModelProcessor().getElementContainer().getRootElements().iterator();
		while (i.hasNext()) {
			AbstractPetriNetElementModel current = (AbstractPetriNetElementModel) i.next();
			current.setHighlighted(false);
		}
		// Then, highlight the selected one
		while (j.hasNext()) {
			AbstractPetriNetElementModel currentHigh = j.next();
			currentHigh.setHighlighted(true);
		}
		m_currentEditor.repaint();
	}

	// ! Remember a pointer to the currently active editor
	// ! (the one for which this window was created)
	// ! This is the central access point for model, graph etc.
	private IEditor m_currentEditor;
	// ! Remember a pointer to the tree
	private JTree m_treeObject;
	// ! Required to be able to open sub processes as required
	private AbstractApplicationMediator m_mediator;

	// ! If specified, highlight elements rather than selecting them
	private boolean highlightOnly = false;
}
