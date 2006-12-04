package org.woped.editor.controller.vc;

import org.woped.core.model.*;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.InnerElementContainer;
import org.woped.editor.controller.vc.NetAlgorithms;

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
		
		// Generic approach to detect whether this element has any children:
		// Elements with children implement InnerElementContainer
		if (addChildren && (node instanceof InnerElementContainer))
		{
			// Add sub-elements as tree items
			InnerElementContainer operator = (InnerElementContainer)node;
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
		Iterator relevantNodes = NetAlgorithms.GetDirectlyConnectedNodes(node, connectionType).iterator();
		boolean isFirst = true;
		while (relevantNodes.hasNext())
		{
			AbstractElementModel currentNode 
				= (AbstractElementModel)relevantNodes.next();
			// If superOnly is specified
			// only nodes that are contained in the root container are relevant for us
			// FIXME: We will need to change this later to be the "next higher"
			// Level container once sub-processes are implemented
			if ((!superOnly)||(GetNodeOwner(currentNode)==null))
			{
				if (isFirst)
					isFirst = false;
				else
					result.append(", ");
				result.append(currentNode.getId());
			}			
		}
		if (result.length()>0)
			result = new StringBuffer("(" + result.toString() + ")");
		return result.toString();
	}
	
	private static String getNodeString(AbstractElementModel node,
			AbstractElementModel nodeOwner)
	{
		// FIXME: Translate
		String predecessors = getConnectedNodesString(node,NetAlgorithms.connectionTypeINBOUND,true);
		String successors = getConnectedNodesString(node,NetAlgorithms.connectionTypeOUTBOUND,true);
		String result = "Node ID: " + ((nodeOwner!=null)?predecessors:"") +
			node.getId() + ((nodeOwner!=null)?successors:"");
		// Specify name only if it exists
		if (node.getNameValue()!=null)
			result = result + ", Name: " + node.getNameValue();
		
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
