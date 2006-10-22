package org.woped.woflan;

import java.util.*;

public abstract class NodeGroupListNetInfo extends NetInfo {
	//! Construct node group list net info object
	//! @param displayString name of the tree node to be displayed
	//! @param nodeGroupIterator specifies an iterator enumerating objects of type Collection
	NodeGroupListNetInfo(String displayString,
			Iterator nodeGroupIterator)		
	{
		super(displayString);
		int x = 0;
		while (nodeGroupIterator.hasNext())
		{
			try
			{			
				Collection current = (Collection)nodeGroupIterator.next();
				add(new NodeGroupNetInfo(GetGroupDisplayString(x,current),
						current.iterator()));
				++x;
			}
			catch (ClassCastException e)
			{
				// Ignore all nodes that are not at least AbstractElementModel
			}
		}						
	}
	//! Must be implemented to display a string for each
	//! node group
	//! @param nGroupIndex specifies the index of the node group (counting from zero)
	//! @param nodeGroup specifies the node group itself
	abstract String GetGroupDisplayString(
			int nGroupIndex,
			Collection nodeGroup);
}