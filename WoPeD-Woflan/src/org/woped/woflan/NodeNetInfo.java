package org.woped.woflan;

import org.woped.core.model.*;
import java.util.Iterator;

//! This class implements a tree node representing
//! one single node of a petri-net that is currently begin
//! analyzed
public class NodeNetInfo extends NetInfo {
	public NodeNetInfo(AbstractElementModel node)
	{
		// Initialize the display of this item
		super("");    
		m_nodeObject = node;
		m_nodeOwner = GetNodeOwner(m_nodeObject);
		setUserObject(getNodeString(m_nodeObject, m_nodeOwner));
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
