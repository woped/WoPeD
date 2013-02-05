package org.woped.quantana.gui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.woped.gui.translations.Messages;

public class TreeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID	= 1L;

	Icon protocol = null;
	Icon process = null;
	Icon server = null;
	Icon subprocess = null;

	public TreeRenderer(){	
		protocol = Messages.getImageIcon("QuantAna.Simulation.Output.TreeView.Protocol");
		process = Messages.getImageIcon("QuantAna.Simulation.Output.TreeView.Process");
		server = Messages.getImageIcon("QuantAna.Simulation.Output.TreeView.Server");
		subprocess = Messages.getImageIcon("QuantAna.Simulation.Output.TreeView.Subprocess");
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
		String val = (String)(((DefaultMutableTreeNode)value).getUserObject());
		try 
		{
			int start = val.indexOf("(") + 1;
			if (val.equals("Protocol")){
				result = protocol;
			} else if (val.equals("Process")){
				result = process;
			} else if (val.substring(start, start + 3).equals("sub")){
				result = subprocess;
			} else {
				result = server;
			}
		} catch (Exception e) 
		{

		}

		return result;
	}
}
