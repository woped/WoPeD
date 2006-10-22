package org.woped.woflan;

import java.util.*;
import org.woped.core.model.*;
import org.woped.core.utilities.LoggerManager;

public class NodeGroupNetInfo extends NetInfo {
	public NodeGroupNetInfo(
			String displayString,
			Iterator elementIterator)
	{
		// First of all, initialize the element itself
		// but not yet with the real name
		super(displayString);

		while (elementIterator.hasNext())
		{
			try
			{			
				AbstractElementModel current = (AbstractElementModel)elementIterator.next();
				add(new NodeNetInfo(current));
			}
			catch (ClassCastException e)
			{
				// Ignore all nodes that are not at least AbstractElementModel
			}
		}
	}
	public Object[] getReferencedElements() {
		// This is a group item
		// Retrieve all subitems and get their referenced elements
		ArrayList collectedItems = new ArrayList();
		// Iterate through all children
		for (Enumeration e = children();e.hasMoreElements();)
		{
			Object current = e.nextElement();
			NetInfo myInfo = null;
			try 
			{
				myInfo = (NetInfo)current;
			}
			catch (Exception exception)
			{
				LoggerManager.error(Constants.WOFLAN_LOGGER, "Illegal element in tree model for Petri-Net analysis!");
			}
			if (myInfo!=null)
			{
				// Add the returned items to our collector
				Object[] references = myInfo.getReferencedElements();
				for (int i=0;i<references.length;++i)
					collectedItems.add(references[i]);
			}			
		}
		return collectedItems.toArray();
	};	
}
