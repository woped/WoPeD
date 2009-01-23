package org.woped.core.gui;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.AbstractButton;

import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewListener;

public interface IToolBar {

	public static final String ID_PREFIX = "TOOLBAR_VC_";

	public AbstractButton getAnalysisButton();
	
	public AbstractButton getTokenGameButton();
	
	public AbstractButton getWoflanButton();
	
	public AbstractButton getQuantCapButton();
	
	public AbstractButton getQuantSimButton();
	
	public AbstractButton getReachabilityGraphButton();
	
	public void addViewListener(IViewListener listener);

	public String getId();

	public void removeViewListener(IViewListener listener);
	
	public void removeAnalysisButtons();
	
	public void addAnalysisButtons();
	
	public Point getLocation();
	
	public Rectangle getBounds();
	

	/**
	 * Fires a ViewEvent to each listener as long as the event is not consumed.
	 * The event is also set with a reference to the current listener.
	 */
	public void fireViewEvent(AbstractViewEvent viewevent);

}