package org.woped.woflan;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.*;
import java.awt.Component;

public class NetInfoTreeRenderer extends DefaultTreeCellRenderer
{
	Icon iconInfo = null;
	Icon iconOK = null;
	Icon iconERROR = null;
	
	public NetInfoTreeRenderer()
	{
		iconOK = new ImageIcon(getClass().getResource("/org/woped/editor/gui/images/apply16.gif"));
		iconInfo = new ImageIcon(getClass().getResource("/org/woped/editor/gui/images/italic16.gif"));
		iconERROR = new ImageIcon(getClass().getResource("/org/woped/editor/gui/images/lightning16.gif"));		
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
		Icon newIcon = GetIconForItem(value);
		if (newIcon!=null)		
			setIcon(newIcon);		 	
		return this;
	}	
	private Icon GetIconForItem(Object value)
	{
		Icon result = null;
		try 
		{
			// Cast to NetInfo
			// If this doesn't work we have
			// a problem.
			// We will return the default icon in this case
			NetInfo myNetInfo = (NetInfo)value;
			int infoType = myNetInfo.GetInfoState();
			switch (infoType)
			{
			case NetInfo.InfoStateERROR:
				result = iconERROR;
				break;
			case NetInfo.InfoStateOK:
				result = iconOK;
				break;
			case NetInfo.InfoStateInfo:
				result = iconInfo;
				break;
			};
		}
		catch (Exception e)
		{
		}
		return result;
	}
}
