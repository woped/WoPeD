package org.woped.woflan;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import org.processmining.framework.models.petrinet.algorithms.Woflan;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.controller.*;
import org.woped.core.model.petrinet.*;
import org.woped.editor.controller.vc.NetInfo;

//! This class implements a tree node that displays
//! one single piece of information extracted from 
//! a woflan net object
public class UnaryNetInfo extends NetInfo {
	public UnaryNetInfo(
			IEditor currentEditor,
			NetAnalysisDialog parent,
			String displayString,
			int theInfo, int theIndex, int theSubIndex)
	{		
    	// Retrieve the requested information about the net
		// and set it as the display text
    	super(displayString + parent.m_myWofLan.Info(parent.m_netHandle, theInfo, theIndex, theSubIndex));
    	
		m_currentEditor = currentEditor;
	}
	public Object[] getReferencedElements() {
		Object translatedObject = TranslateToNetObject(toString());		
		// Return a list containing only the single element that is ourselves
		// or an empty list if we have no matching in the net
		Object[] result = new Object[(translatedObject!=null)?1:0];
		if (translatedObject!=null)		
			result[0] = translatedObject;
		return result; 
	};	
	//! Translate a string identifier as returned by the WOFLAN
	//! analyser DLL into an object within the net that is currently
	//! opened in the editor
	private Object TranslateToNetObject(String name) {
		ModelElementContainer elements
		= m_currentEditor.getModelProcessor().getElementContainer();
		String objectID = null;
		int nOperator = name.lastIndexOf(OperatorTransitionModel.OPERATOR_SEPERATOR);
		if (nOperator!=-1)
		{
			// Cut any operator numbers at the end (operator
			// numbers are used for complex transitions such as
			// xor split / xor join
			objectID = name.substring(0,nOperator);
			int nRealIDIndex = objectID.lastIndexOf(OperatorTransitionModel.INNERID_SEPERATOR);
			if (nRealIDIndex!=-1)			
				objectID = objectID.substring(nRealIDIndex+1);
		}
		else
		{
			// This is an ordinary element, just take its ID
			int nRealIDIndex = name.lastIndexOf(OperatorTransitionModel.INNERID_SEPERATOR);
			if (nRealIDIndex!=-1)			
				objectID = name.substring(nRealIDIndex+1);
		}		
		Object current =
			((objectID!=null)?elements.getElementById(objectID):null);
		return current;
	}
	private IEditor m_currentEditor;
}
