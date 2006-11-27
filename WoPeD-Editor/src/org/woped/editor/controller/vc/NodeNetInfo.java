package org.woped.editor.controller.vc;

import org.woped.core.model.*;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;

import java.util.Iterator;

//! This class implements a tree node representing
//! one single node of a petri-net that is currently begin
//! analyzed
public class NodeNetInfo extends NetInfo {
	//! Instantiate this class with an element model node and a flag
	//! specifying whether children of that node (sub-processes,
	//! inner transitions) should be added to the tree item
	public NodeNetInfo(AbstractElementModel node, boolean addChildren)
	{
		// Initialize the display of this item
		super("");    
		m_nodeObject = node;
		m_nodeOwner = GetNodeOwner(m_nodeObject);
		setUserObject(getNodeString(m_nodeObject, m_nodeOwner));
		
		if (addChildren &&
				(node.getType()==AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE))
		{
			// Currently, only operators support sub-elements
			// Add them as tree items
			OperatorTransitionModel operator = (OperatorTransitionModel)node;
			ModelElementContainer simpleTransContainer =
				operator.getSimpleTransContainer();
			// Recursively call ourselves to add inner nodes
			Iterator innerIterator =simpleTransContainer.getRootElements().iterator();			
			while (innerIterator.hasNext())
			{
				try
				{			
					AbstractElementModel current = (AbstractElementModel)innerIterator.next();
					if (operator.equals(GetNodeOwner(current)))
						add(new NodeNetInfo(current, true));
				}
				catch (ClassCastException e)
				{
					// Ignore all nodes that are not at least AbstractElementModel
				}
			}			
		}
	}
	//! Return a node owner only if the node is not a root node
	//! (A node may be contained in more than one element container)
	private static AbstractElementModel GetNodeOwner(AbstractElementModel node)
	{
		AbstractElementModel result = null;
		boolean isRootNode = false;
		Iterator i = node.getOwningContainers();
		while (i.hasNext()&&(isRootNode==false)){
			ModelElementContainer current = 
				(ModelElementContainer)i.next();
			result = current.getOwningElement();
			if (result == null)
				isRootNode = true;
		}
		return result;
	}
	private static String getNodeString(AbstractElementModel node,
			AbstractElementModel nodeOwner)
	{
		// FIXME: Translate
		String result = "Node ID: " + node.getId() + ", Name: " + node.getNameValue();
		if (nodeOwner!=null)
			result = result + " ,Owner: (" + getNodeString(nodeOwner,null) + ")"; 
		return result;
	}
	public Object[] getReferencedElements() {
		Object[] result = new Object[1];
		result[0] =((m_nodeOwner!=null)?m_nodeOwner:m_nodeObject);
		return result;
	};	
	//! Store the node object this tree item represents
	private AbstractElementModel m_nodeObject;
	//! Sometimes it is better to reference the node owner
	//! instead of the node itself (inner transitions and places
	//! of a complex operator)
	private AbstractElementModel m_nodeOwner=null;
}
