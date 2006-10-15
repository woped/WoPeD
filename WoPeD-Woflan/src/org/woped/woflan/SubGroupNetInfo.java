package org.woped.woflan;

import java.util.*;

import org.woped.core.utilities.LoggerManager;
import org.woped.core.controller.*;

public class SubGroupNetInfo extends UnaryNetInfo {
	public SubGroupNetInfo(
			IEditor currentEditor,
			NetAnalysisDialog parent,
			String displayString,
			int numElementsInfo, int numElementsIndex, 
			String subItemDisplayString,
			int elementInfo)
	{
		// First of all, initialize the element itself
		super(currentEditor, parent, displayString, 
				numElementsInfo, numElementsIndex, 0);
		// Get number of sub elements
		String elementCount = parent.m_myWofLan.Info(parent.m_netHandle, 
				numElementsInfo, numElementsIndex, 0);
		int nElementCount = Integer.parseInt(elementCount);
		for (int i=0;i<nElementCount;++i)
			add(new UnaryNetInfo(currentEditor, parent, subItemDisplayString, elementInfo, numElementsIndex, i));		
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
