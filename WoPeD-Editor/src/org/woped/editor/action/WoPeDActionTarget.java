package org.woped.editor.action;

import javax.swing.JComponent;

public class WoPeDActionTarget {
	
	public static final int TYPE_BUTTON 		= 1;
	public static final int TYPE_TOGGLEBUTTON 	= 2;
	public static final int TYPE_MENUITEM 		= 3;
	public static final int TYPE_CHECKBUTTON 	= 4;

	private JComponent 	component;
	private int 		type;

	public WoPeDActionTarget(JComponent component, int type) {
		this.component = component;
		this.type 	= type;
	}
	
	public JComponent getComponent() {
		return component;
	}

	public int getType() {
		return type;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}

	public void setType(int type) {
		this.type = type;
	}

}
