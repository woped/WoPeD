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

	private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!componentsAlreadyAdded) {
			beginnerPanel.addComponents();
			componentsAlreadyAdded = true;
		}
		JPanel beginnerContainer = sideBar.getBeginnerContainer();
		beginnerContainer.removeAll();
		beginnerContainer.add(beginnerPanel);
		beginnerContainer.updateUI();
		beginnerPanel.cleanHiglights();
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
