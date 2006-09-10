package org.woped.woflan;

import javax.swing.tree.DefaultMutableTreeNode;
import org.processmining.framework.models.petrinet.algorithms.Woflan;

//! This class implements a tree node that displays
//! one single piece of information extracted from 
//! a woflan net object
public class UnaryNetInfo extends NetInfo {
	UnaryNetInfo(NetAnalysisDialog parent,
			String displayString,
			int theInfo, int theIndex, int theSubIndex)
	{
    	// Retrieve the requested information about the net
		// and set it as the display text
    	super(displayString + parent.m_myWofLan.Info(parent.m_netHandle, theInfo, theIndex, theSubIndex));
	}
	Object[] getReferencedElements() {
		// Return a list containing only the single element that is ourselves
		String[] result = new String[1];
		result[0] = toString();
		return result; 
		};	
}
