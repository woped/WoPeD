package org.woped.woflan;

import java.util.*;

import org.woped.core.utilities.LoggerManager;
import org.woped.core.controller.*;
import org.woped.editor.controller.vc.NetInfo;

public class GroupNetInfo extends UnaryNetInfo {
	public GroupNetInfo(
			IEditor currentEditor,
			NetAnalysisDialog parent,
			String displayString,
			int numElementsInfo,
			String subItemDisplayString,
			int elementInfo,
			
			int minElements,
			int maxElements,
			boolean infoOnly
			)
	{
		// First of all, initialize the element itself
		super(currentEditor, parent, displayString, 
				numElementsInfo, 0, 0);
		m_minElements = minElements;
		m_maxElements = maxElements;
		m_infoOnly = infoOnly;
		
		// Get number of sub elements
		String elementCount = parent.m_myWofLan.Info(parent.m_netHandle, 
				numElementsInfo, 0, 0);
		int nElementCount = Integer.parseInt(elementCount);
		for (int i=0;i<nElementCount;++i)
			add(new UnaryNetInfo(currentEditor, parent, subItemDisplayString, elementInfo, i, 0));		
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
	public int GetInfoState() {
		int result = InfoStateInfo; 
		if (m_infoOnly==false)
		{
			boolean isError = false;
			int numChildren = getChildCount();
			if ((m_minElements!=-1)&&
				(numChildren<m_minElements))
				isError = true;
			if ((m_maxElements!=-1)&&
					(numChildren>m_maxElements))
					isError = true;
			result = ((isError)?InfoStateERROR:InfoStateOK);
		}		
		return result;
	};	
	
	//! Specifies the minimum number of elements that need to be
	//! present in this group or -1
	private int m_minElements;
	//! Specifies the maximum number of elements that need to be
	//! present in this group or -1
	private int m_maxElements;	
	//! true if this group is serving information purposes only
	private boolean m_infoOnly;

}
