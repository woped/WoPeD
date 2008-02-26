package org.woped.qualanalysis.simulation.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class TokenGameEditorFrameListener implements InternalFrameListener {

	private TokenGameBarController tgbc = null;

	public TokenGameEditorFrameListener(TokenGameBarController tgbc) {
		this.tgbc = tgbc;
	}

	public void internalFrameActivated(InternalFrameEvent e) {
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		tgbc.removeControlElements();
	}

	public void internalFrameClosing(InternalFrameEvent e) {
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	public void internalFrameIconified(InternalFrameEvent e) {
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}

}
