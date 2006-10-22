package org.woped.woflan;

import org.woped.core.model.*;

//! This class implements a tree node representing
//! one single node of a petri-net that is currently begin
//! analyzed
public class NodeNetInfo extends NetInfo {
	public NodeNetInfo(AbstractElementModel node)
	{
		// Initialize the display of this item
		super(getNodeString(node));    
		m_nodeObject = node;
	}
	private static String getNodeString(AbstractElementModel node)
	{
		// FIXME: Translate
		String result = "Node ID: " + node.getId() + ", Name: " + node.getNameValue();
		return result;
	}
	public Object[] getReferencedElements() {
		Object[] result = new Object[1];
		result[0] = m_nodeObject;
		return result;
	};	
	//! Store the node object this tree item represents
	private AbstractElementModel m_nodeObject;
}
