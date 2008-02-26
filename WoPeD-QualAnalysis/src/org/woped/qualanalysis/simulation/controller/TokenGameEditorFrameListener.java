package org.woped.qualanalysis.simulation.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class TokenGameEditorFrameListener implements InternalFrameListener {

	private TokenGameBarController tgbc = null;

	public TokenGameEditorFrameListener(TokenGameBarController tgbc) {
		this.tgbc = tgbc;
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		tgbc.removeControlElements();
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
	}

}
