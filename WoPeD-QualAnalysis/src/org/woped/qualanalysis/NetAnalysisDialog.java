// FIXME: This object must create a local copy of the
// petri-net or inhibit editing (become a modal dialog etc.)

package org.woped.qualanalysis;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.sidebar.expert.ExpertPage;
import org.woped.gui.translations.Messages;

@SuppressWarnings("serial")
public class NetAnalysisDialog extends JDialog implements WindowListener {
	// ! Remember a pointer to the currently active editor
	// ! (the one for which this window was created)
	// ! This is the central access point for model, graph etc.
	private IEditor m_currentEditor;

	private ExpertPage expertPage;

	private IQualanalysisService m_qualanService;

	public NetAnalysisDialog(JFrame owner, File temporaryFile, IEditor editor,
			AbstractApplicationMediator mediator) {
		// Ignore the dialog owner and set our own instead to be able to change
		// the dialog icon
		super(owner, Messages.getString("Analysis.Dialog.Title"), true);

		// Remember a reference to our model
		// We need it to deal with selections
		m_currentEditor = editor;

		m_qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(m_currentEditor);

		setSize(640, 480);
		// Center the window on the desktop
		setLocationRelativeTo(null);
		getContentPane().add(
				expertPage = new ExpertPage(m_currentEditor, mediator,
						m_qualanService));

		// Listen to close event to be able to dispose of our temporary file
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	protected void finalize() {
		// Call cleanup if we happen to receive a finalize() call from the
		// garbage collector
		m_qualanService.cleanup();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
		m_qualanService.cleanup();
	}

	public void windowClosing(WindowEvent e) {
		// Before closing the window, deselect all tree elements
		// to clear highlighting
		expertPage.getTreeObject().clearSelection();
		// When receiving a windowClosing() event we will
		// initiate immediate disposal of the affected dialog
		dispose();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	// ! @}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowGainedFocus(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowLostFocus(WindowEvent e) {
	}

	// ! @{
	// ! Some dummy implementations to fulfill the requirements of the
	// WindowListener interface
	public void windowOpened(WindowEvent e) {
	}

	public void windowStateChanged(WindowEvent e) {
	}
}
