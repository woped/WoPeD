package org.woped.editor.controller.vc;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.sidebar.expert.components.NodeNetInfo;

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
	Icon xorSplitJoin = null;
	Icon andSplitJoin = null;
	Icon andJoinXorSplit = null;
	Icon xorJoinAndSplit = null;
	
	public NetInfoTreeRenderer()
	{	
	    place = Messages.getImageIcon("Popup.Add.Place");
	    transition = Messages.getImageIcon("Popup.Add.Transition");
	    subprocess = Messages.getImageIcon("Popup.Add.Subprocess");
	    xorSplit = Messages.getImageIcon("Popup.Add.XorSplit");
	    xorJoin = Messages.getImageIcon("Popup.Add.XorJoin");
	    andSplit = Messages.getImageIcon("Popup.Add.AndSplit");
	    andJoin = Messages.getImageIcon("Popup.Add.AndJoin");	    
	    xorSplitJoin = Messages.getImageIcon("Popup.Add.XorSplitJoin");
	    andSplitJoin = Messages.getImageIcon("Popup.Add.AndSplitJoin");
	    andJoinXorSplit = Messages.getImageIcon("Popup.Add.AndJoinXorSplit");
	    xorJoinAndSplit = Messages.getImageIcon("Popup.Add.XorJoinAndSplit");
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
	    	case AbstractPetriNetElementModel.PLACE_TYPE:
	    		result = place;
	    		break;
	    	case AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE:
	    		result = transition;
	    		break;
	    	case AbstractPetriNetElementModel.SUBP_TYPE:
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
	    	case OperatorTransitionModel.AND_SPLITJOIN_TYPE:
	    		result = andSplitJoin;
	    		break;
                case OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE:
                    result = xorSplitJoin;
	    		break;
	    	case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
	    		result = andJoinXorSplit;
	    		break;
	    	case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
	    		result = xorJoinAndSplit;
	    		break;		    
	    	}
	    }

	} catch (Exception e) 
	{
	}
	
	return result;
    }
}
