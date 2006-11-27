package org.woped.woflan;

import java.util.*;

import org.woped.core.utilities.LoggerManager;
import org.woped.core.controller.*;
import org.woped.editor.controller.vc.NetInfo;

public class MultipleGroupsNetInfo extends UnaryNetInfo {
	public MultipleGroupsNetInfo(
			IEditor currentEditor,
			NetAnalysisDialog parent,
			String displayString,
			int numGroupsInfo, 
			int numGroupMembers,
			String groupDisplayString,
			String subItemDisplayString,
			int groupIterator,
			
			int minGroupCount,
			int maxGroupCount,
			boolean infoOnly)
	{
		// First of all, initialize the element itself
		super(currentEditor, parent, displayString, numGroupsInfo, 0, 0);
		
		m_nMinGroupCount = minGroupCount;
		m_nMaxGroupCount = maxGroupCount;
		m_bInfoOnly = infoOnly;
		
		// Get number of groups
		String groupCount = parent.m_myWofLan.Info(parent.m_netHandle, 
				numGroupsInfo, 0, 0);
		int nElementCount = Integer.parseInt(groupCount);
		for (int i=0;i<nElementCount;++i)
			add(new SubGroupNetInfo(currentEditor, parent, 
					groupDisplayString,
					numGroupMembers, i,
					subItemDisplayString,
					groupIterator));
					
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
		if (m_bInfoOnly==false)
		{
			boolean isError = false;
			int numChildren = getChildCount();
			if ((m_nMinGroupCount!=-1)&&
				(numChildren<m_nMinGroupCount))
				isError = true;
			if ((m_nMaxGroupCount!=-1)&&
					(numChildren>m_nMaxGroupCount))
					isError = true;
			result = ((isError)?InfoStateERROR:InfoStateOK);
		}		
		return result;
	};	
	
	//! This information is required to calculate the
	//! correct info state for this item
	private int m_nMinGroupCount;
	private int m_nMaxGroupCount;
	private boolean m_bInfoOnly;
}