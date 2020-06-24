/*
 * Created on 27.09.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.core.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;

/**
 * @author lai
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractMarqueeHandler extends BasicMarqueeHandler {

    private	  IEditor  editor = null;
    protected PortView port, firstPort;
    protected Point2D  start, current, tempStart;

    /**
     * Constructor, the editor must not be <code>null</code>.
     * 
     * @param editor
     */
    public AbstractMarqueeHandler(IEditor editor) {
        this.editor = editor;
    }
    
    public void clear(){
    	editor = null;
    	port = null;
    	firstPort = null;
    	start = null;
    	current = null;
    	tempStart = null;
    }

    public IEditor getEditor() {
        return editor;
    }

    public AbstractGraph getGraph() {
    	return editor.getGraph();
    }
    
    public void cancelSmartArcDrawing() {
        port = null;
        firstPort = null;
        start = null;
        current = null;
        tempStart = null;
    }

    /**
     * The isForceMarqueeEvent method is used to fetch the subsequent mousePressed, mouseDragged and mouseReleased events.Thus, the marquee handler may be used
     * to gain control over the mouse. The argument to the method is the event that triggered the call, namely the mousePressed event. (The graph�s
     * portsVisible flag is used to toggle the connect mode.)
     * 
     * @see BasicMarqueeHandler#isForceMarqueeEvent(java.awt.event.MouseEvent)s
     */
    @Override
    public boolean isForceMarqueeEvent(MouseEvent e) {
        // Wants to Display the PopupMenu
        if (SwingUtilities.isRightMouseButton(e)) {
            return true;
        }
        // We want to be able to catch double clicks in order to display
        // the properties dialog
        if (e.getClickCount() == 2) {
            return true;
        }
        // Find and Remember Port
        port = getSourcePortAt(e.getPoint());
        // If Port Found and in ConnectMode (=Ports Visible)
        if (port != null && getEditor().getGraph().isPortsVisible()) {
            return true;
        }

        // Else Call Superclass
        boolean result = super.isForceMarqueeEvent(e);
        return result;
    }

    /**
     * simply used to retrieve the port at a specified position.
     */
    protected PortView getSourcePortAt(Point2D point) {
        // Find a Port View in Model Coordinates and Remember
        return getEditor().getGraph().getPortViewAt(point.getX(), point.getY());
    }

    /**
     * checks if there is a cell under the mouse pointer, and if one is found, it returns its default port(first port)
     */
    protected PortView getTargetPortAt(Point2D p) {
        // Find Cell at point (No scaling needed here)
        Object cell = getEditor().getGraph().getFirstCellForLocation(p.getX(), p.getY());

        // return port view of cell element.
        if (cell != null && cell instanceof GroupModel) {
            AbstractPetriNetElementModel aem = ((GroupModel) cell).getMainElement();
            Object result = getEditor().getGraph().getGraphLayoutCache().getMapping(aem.getPort(), false);

            if (result instanceof PortView && firstPort != result) {
                return (PortView) result;
            }
        }

        // Shortcut Variable
        GraphModel model = getEditor().getGraph().getModel();
        // Loop Children to find first PortView
        for (int i = 0; i < model.getChildCount(cell); i++) {
            // Get Child from Model
            Object tmp = getEditor().getGraph().getModel().getChild(cell, i);
            // Map Cell to View
            // ??? lai
            tmp = getEditor().getGraph().getGraphLayoutCache().getMapping(tmp, false);
            // If is Port View and not equal to First Port
            if (tmp instanceof PortView && tmp != firstPort) {
                // PortView
                return (PortView) tmp;
            }
        } // No Port View found
        return getSourcePortAt(p);
    }

    /**
     * The paintConnector method displays a preview of the edge to be inserted. (The paintPort method is not shown.)
     */
    public void paintConnector(Color fg, Color bg, Graphics g) {
        // Set Foreground
        g.setColor(fg);
        // Set Xor-Mode Color
        g.setXORMode(bg);
        // Highlight the Current Port
        paintPort(getEditor().getGraph().getGraphics());
        // If Valid First Port, Start and Current Point
        if (firstPort != null && start != null && current != null) {
            // Then Draw A Line From Start to Current Point
            g.drawLine((int) start.getX(), (int) start.getY(), (int) current.getX(), (int) current.getY());
        }
    }

    // Use the Preview Flag to Draw a Highlighted Port
    abstract public void paintPort(Graphics g);

}
