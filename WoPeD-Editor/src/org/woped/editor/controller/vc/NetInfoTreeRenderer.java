package org.woped.editor.controller.vc;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.editor.utilities.Messages;

@SuppressWarnings("serial")
public class NetInfoTreeRenderer extends DefaultTreeCellRenderer
{	
	Icon place = null;
	Icon transition = null;
	Icon subprocess = null;
	Icon xorSplit = null;
	Icon xorJoin = null;
	Icon andSplit = null;
	Icon andJoin = null;	
	Icon andJoinXorSplit = null;
	
	public NetInfoTreeRenderer()
	{	
	    place = Messages.getImageIcon("Popup.Add.Place");
	    transition = Messages.getImageIcon("Popup.Add.Transition");
	    subprocess = Messages.getImageIcon("Popup.Add.Subprocess");
	    xorSplit = Messages.getImageIcon("Popup.Add.XorSplit");
	    xorJoin = Messages.getImageIcon("Popup.Add.XorJoin");
	    andSplit = Messages.getImageIcon("Popup.Add.AndSplit");
	    andJoin = Messages.getImageIcon("Popup.Add.AndJoin");	    
	    andJoinXorSplit = Messages.getImageIcon("Popup.Add.AndJoinXorSplit");
	}
	
	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
		
		super.getTreeCellRendererComponent(
				tree, value, sel,
				expanded, leaf, row,
				hasFocus);
		Icon newIcon = getIconForItem(value);
		if (newIcon != null) {
		    setIcon(newIcon);		 	
		}
		
		return this;
	}	
	
    private Icon getIconForItem(Object value) 
    {
	Icon result = null;
	try 
	{
	    if (value instanceof NodeNetInfo) 
	    {
		
		NodeNetInfo node = (NodeNetInfo) value;

		switch (node.getTypeId()) 
		{
		case AbstractPetriNetModelElement.PLACE_TYPE:
		    result = place;
		    break;
		case AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE:
		    result = transition;
		    break;
		case AbstractPetriNetModelElement.SUBP_TYPE:
		    result = subprocess;
		    break;
		case OperatorTransitionModel.AND_JOIN_TYPE:
		    result = andJoin;
		    break;
		case OperatorTransitionModel.AND_SPLIT_TYPE:
		    result = andSplit;
		    break;
		case OperatorTransitionModel.XOR_JOIN_TYPE:
		    result = xorJoin;
		    break;
		case OperatorTransitionModel.XOR_SPLIT_TYPE:
		    result = xorSplit;
		    break;
		case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
		    result = andJoinXorSplit;
		    break;
		}
	    }

	} catch (Exception e) 
	{
	}
	
	return result;
    }
}
