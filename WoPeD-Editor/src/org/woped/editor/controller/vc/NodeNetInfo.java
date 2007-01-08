package org.woped.editor.controller.vc;

import java.util.Iterator;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.InnerElementContainer;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.editor.utilities.Messages;

//! This class implements a tree node representing
//! one single node of a petri-net that is currently begin
//! analyzed

@SuppressWarnings("serial")
public class NodeNetInfo extends NetInfo {
    
    	private int typeId = 0;
        
	public int getTypeId() {
	    return typeId;
	}
	
	public void setTypeId(int typeId) {
	    this.typeId = typeId;
	}
	
	//! Instantiate this class with an element model node and a flag
	//! specifying whether children of that node (sub-processes,
	//! inner transitions) should be added to the tree item
	public NodeNetInfo(AbstractElementModel node, boolean addChildren)
	{
		// Initialize the display of this item
		super("");    
		m_nodeObject = node;
		ModelElementContainer rootOwningContainer = m_nodeObject.getRootOwningContainer();
		m_nodeOwner = rootOwningContainer != null ? rootOwningContainer.getOwningElement() : null;
		setUserObject(getNodeString(m_nodeObject, m_nodeOwner));
		
		// set the type of the element
		typeId = node.getType();
		
		// if the element is a transition-operator
		if (node instanceof OperatorTransitionModel) {
		    OperatorTransitionModel otm = (OperatorTransitionModel) node;
		    typeId = otm.getOperatorType();
		}
		
		
		// Generic approach to detect whether this element has any children:
		// Elements with children implement InnerElementContainer
		if (addChildren && (node instanceof InnerElementContainer))
		{
			// Add sub-elements as tree items
			InnerElementContainer operator = (InnerElementContainer)node;
			ModelElementContainer simpleTransContainer =
				operator.getSimpleTransContainer();
			// Recursively call ourselves to add inner nodes
			Iterator innerIterator = simpleTransContainer.getRootElements().iterator();			
			while (innerIterator.hasNext())
			{
				try
				{			
					AbstractElementModel current = (AbstractElementModel)innerIterator.next();
					AbstractElementModel owningElement = current.getRootOwningContainer().getOwningElement();
					if ((owningElement != null) 
						&& (operator.equals(current.getRootOwningContainer().getOwningElement())))
					    add(new NodeNetInfo(current, true));
				}
				catch (ClassCastException e)
				{
					// Ignore all nodes that are not at least AbstractElementModel
				}
			}			
		}
	}
	//! Get a string with the following layout: "(node, node, ...)" 
	//! if any nodes are found matching the specified criteria, "" otherwise
	//! @param node Specifies the node whose connected nodes are to be enumerated
	//! @param connectionType specifies the type of connection as
	//! NetAlgorithms.connectionTypeINBOUND,
	//! NetAlgorithms.connectionTypeOUTBOUND or 
	//! NetAlgorithms..connectionTypeALL
	//! @param superOnly if true, only connections going to a super-net are going
	//! to be listed
	private static String getConnectedNodesString(AbstractElementModel node, int connectionType,
			boolean superOnly)
	{
		StringBuffer result = new StringBuffer();
		Iterator relevantNodes = NetAlgorithms.getDirectlyConnectedNodes(node, connectionType).iterator();
		int nodeHierarchyLevel = node.getHierarchyLevel();
		boolean isFirst = true;
		while (relevantNodes.hasNext())
		{
			AbstractElementModel currentNode 
				= (AbstractElementModel)relevantNodes.next();
			// If superOnly is specified
			// only nodes that are contained in the next lower level container are relevant for us
			if ((!superOnly)||(currentNode.getHierarchyLevel() < nodeHierarchyLevel))
			{
				if (isFirst)
					isFirst = false;
				else
					result.append(", ");
				result.append(currentNode.getId());
			}			
		}
		if (result.length() > 0)
			result = new StringBuffer("(" + result.toString() + ")");
		return result.toString();
	}
	
	private static String getNodeString(AbstractElementModel node,
		AbstractElementModel nodeOwner)
	{
	    String predecessors = getConnectedNodesString(node, NetAlgorithms.connectionTypeINBOUND, true);
       	    String successors = getConnectedNodesString(node, NetAlgorithms.connectionTypeOUTBOUND, true);
            String nodeId = Messages.getString("TreeView.Element.Node.ID");
            String nodeOwnerString = Messages.getString("TreeView.Element.Owner");
            String nodeNameString = Messages.getString("TreeView.Element.Name");
        	
            String result = nodeId + ": " + ((nodeOwner != null) ? predecessors : "") 
        		+ node.getId() + ((nodeOwner != null) ? successors : "");
        	
            // Specify name only if it exists
            if (node.getNameValue() != null) {
        	result += ", " + nodeNameString + ": " + node.getNameValue();
            }
        	
            if (nodeOwner != null) {
        	result += ", " + nodeOwnerString + ": (" + getNodeString(nodeOwner, null) + ")"; 
            }
            return result;
	}
	
	public Object[] getReferencedElements() {
		Object[] result = new Object[1];
		// If the owner is a sub-process
		// the object can be displayed and thus will be returned as the item to be selected
		result[0] = (((m_nodeOwner != null)
			&& (m_nodeOwner.getType() != AbstractPetriNetModelElement.SUBP_TYPE)) ? m_nodeOwner: m_nodeObject);
		return result;
	};	
	//! Store the node object this tree item represents
	private AbstractElementModel m_nodeObject;
	//! Sometimes it is better to reference the node owner
	//! instead of the node itself (inner transitions and places
	//! of a complex operator)
	private AbstractElementModel m_nodeOwner = null;
}
