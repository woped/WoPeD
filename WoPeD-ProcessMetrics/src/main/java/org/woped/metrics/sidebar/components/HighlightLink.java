package org.woped.metrics.sidebar.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import org.woped.core.controller.IEditor;
import org.woped.metrics.metricsCalculation.MetricsUIRequestHandler;

/**
 * @author Mathias Gruschinske
 * Label with mouse listener to highlight the net elements which are affected by the metric
 */
public class HighlightLink extends JLabel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param uiHandler highlight the net elements with the uiHandler.setHighlight
	 * @param editor needs editor to refresh the graph
	 * @param metricsID id of the metric, which should highlight
	 */
	public HighlightLink(final MetricsUIRequestHandler uiHandler, final IEditor editor, final String metricsID) {
		super(" " + metricsID + ":");
				
		setForeground(Color.BLUE);
		
		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {	
			}

			public void mouseEntered(MouseEvent arg0) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));			
			}

			public void mouseExited(MouseEvent arg0) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));				
			}

			public void mousePressed(MouseEvent arg0) {
				uiHandler.setHighlight(metricsID);
				editor.getGraph().refreshNet();
				editor.getGraph().repaint();
			}

			public void mouseReleased(MouseEvent arg0) {
			}});
	}
}
