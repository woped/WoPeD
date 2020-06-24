package org.woped.qualanalysis.sidebar.assistant.components;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.qualanalysis.sidebar.SideBar;

/**
 * Detailsmouselistener - Mouselistener for Details Icon in Analysissidebar
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
public class DetailsMouseListener implements MouseListener {

	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	private static final Cursor DEFAULT_CURSOR = new Cursor(
			Cursor.DEFAULT_CURSOR);

	private SideBar sideBar;

	private BeginnerPanel beginnerPanel = null;

	// variable to know if the components of the called beginnerpanel are added
	private boolean componentsAlreadyAdded = false;

	/**
	 * 
	 * @param sideBar
	 *            - reference to the sidebar
	 * @param beginnerPanel
	 *            - reference to the detailed - beginnerpanel
	 */
	public DetailsMouseListener(SideBar sideBar, BeginnerPanel beginnerPanel) {
		this.sideBar = sideBar;
		this.beginnerPanel = beginnerPanel;
	}

	public void mouseClicked(MouseEvent e) {
		if (!componentsAlreadyAdded) {
			beginnerPanel.addComponents();
			componentsAlreadyAdded = true;
		}

		// start of adaption
		// (Saskia Kurz, Dec 2011)

		/* user has navigated to Soundness Page
		 * -> second button has to be selected
		 * -> third button must be disabled
		 * if the petrinet doesn't fulfill the workflow net criteria,
		 * there's only one page for details
		 * -> same state of the buttons!
		 */
		if (beginnerPanel.getClass().toString().endsWith("SoundnessPage") ||
				beginnerPanel.getClass().toString().endsWith("WorkflowPage")) {
			
			// 2nd button selected
			beginnerPanel.j1.setSelected(false);
			beginnerPanel.j2.setSelected(true);
			beginnerPanel.j3.setSelected(false);
			
			// button 1 + 2 enabled
			beginnerPanel.j1.setEnabled(true);
			beginnerPanel.j2.setEnabled(true);
			beginnerPanel.j3.setEnabled(false);
			
			/* if the user has navigated from the Soundness Page
			 * to any of the pages with further details,
			 * all the buttons have to be enabled for backward navigation
			 * 
			 */
			
		} else {
			// 3rd button selected
			beginnerPanel.j1.setSelected(false);
			beginnerPanel.j2.setSelected(false);
			beginnerPanel.j3.setSelected(true);
			
			// all the buttons enabled
			beginnerPanel.j1.setEnabled(true);
			beginnerPanel.j2.setEnabled(true);
			beginnerPanel.j3.setEnabled(true);
		}

		// end of adaption

		JPanel beginnerContainer = sideBar.getBeginnerContainer();
		beginnerContainer.removeAll();
		beginnerContainer.add(beginnerPanel);
		beginnerContainer.updateUI();
		beginnerPanel.cleanHighlights();

		((JLabel) e.getSource()).setCursor(DEFAULT_CURSOR);

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		((JLabel) e.getSource()).setCursor(HAND_CURSOR);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		((JLabel) e.getSource()).setCursor(DEFAULT_CURSOR);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
