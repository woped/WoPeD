package org.woped.qualanalysis.sidebar.components;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;

import org.woped.gui.translations.Messages;

/**
 * Superclass for the analysis sidebar
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 *
 */
@SuppressWarnings("serial")
public abstract class CloseableTabbedPane extends JTabbedPane implements
		MouseListener {

	private Rectangle closeRect = null;

	private Rectangle refreshRect = null;

	/**
	 * paint method override for painting a close and a refresh icon in upper right corner
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		closeRect = new Rectangle(this.getWidth() - 20, 3, 16, 16);
		g.drawImage(Messages.getImageSource("AnalysisSideBar.Cancel"),
				this.getWidth() - 20, 3, this);
		if (!getAutoRefreshStatus()) {
			refreshRect = new Rectangle(this.getWidth() - 40, 3, 16, 16);
			g.drawImage(Messages
					.getImageSource("AnalysisSideBar.Refresh"), this
					.getWidth() - 40, 3, this);
		}
	}

	/**
	 * listener for refreh and close icon
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// close the analysis sidebar if the user click into the closerectangle
		if (closeRect.contains(e.getPoint())) {
			// cleanup woflanAnalysis (temp files ...)
			clean();
		}
		if (refreshRect != null && !getAutoRefreshStatus()) {
			if (refreshRect.contains(e.getPoint())) {
				refresh();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * method called if closeicon clicked
	 */
	abstract protected void clean();

	/**
	 * method called if refresh icon clicked 
	 */
	abstract public void refresh();

	/**
	 * show refresh button for manual refresh or not
	 * @return autorefreshstatus
	 */
	abstract public boolean getAutoRefreshStatus();

}
