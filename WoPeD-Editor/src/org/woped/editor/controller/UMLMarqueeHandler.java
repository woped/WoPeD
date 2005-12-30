package org.woped.editor.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;
import org.woped.core.controller.AbstractMarqueeHandler;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.gui.PopupMenuUML;

public class UMLMarqueeHandler extends AbstractMarqueeHandler
{
    /**
     * Constructor, the editor must not be <code>null</code>.
     * 
     * @param editor
     */
    public UMLMarqueeHandler(EditorVC editor)
    {
        super(editor);
    }

    /* ######################################################### */
    public void mousePressed(MouseEvent e)
    {
        // Fixing Startpoint
        if (port != null && !e.isConsumed() && getEditor().getGraph().isPortsVisible())
        {
            // Remember Start Location
            start = getEditor().getGraph().toScreen(port.getLocation(null));
            // Remember First Port
            firstPort = port;
        }
        super.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (e != null)// can be called when a key is pressed
        {
            getEditor().setLastMousePosition(e.getPoint());
            if (SwingUtilities.isRightMouseButton(e))
            {
                // Find Cell in Model Coordinates
                Object c = getEditor().getGraph().getFirstCellForLocation(getEditor().getLastMousePosition().getX(), getEditor().getLastMousePosition().getY());
                // Display PopupMenu
                PopupMenuUML.getInstance().show(getEditor().getGraph(), (int) getEditor().getLastMousePosition().getX(), (int) getEditor().getLastMousePosition().getY());
                // Else if in ConnectMode and Remembered Port is Valid
                getEditor().setDrawingMode(false);
                e.consume();
            } else
            {
                // Create Arc when released on an port
                if (!e.isConsumed() && port != null && firstPort != port)
                {
                    Port source = (Port) firstPort.getCell();
                    Port target = (Port) port.getCell();
                    CreationMap map = CreationMap.createMap();
                    map.setArcSourceId(((AbstractElementModel) ((DefaultPort) source).getParent()).getId());
                    map.setArcTargetId(((AbstractElementModel) ((DefaultPort) target).getParent()).getId());
                    getEditor().createArc(map);
                    e.consume();
                }
                // Add Point for Shortcut
                if (getEditor().getGraph().getFirstCellForLocation(e.getX(), e.getY()) instanceof ArcModel && e.isShiftDown())
                {
                    getEditor().setLastMousePosition(e.getPoint());
                    getEditor().addPointToSelectedArc();
                    e.consume();
                }
            }
            firstPort = port = null;
            start = current = null;
            getEditor().getGraph().repaint();
        }
        super.mouseReleased(e);
    }

    public void mouseDragged(MouseEvent e)
    {
        // If remembered Start Point is Valid
        if (start != null && !e.isConsumed())
        {
            // Fetch Graphics from Graph
            Graphics g = getEditor().getGraph().getGraphics();
            // Xor-Paint the old Connector (Hide old Connector)
            paintConnector(Color.black, getEditor().getGraph().getBackground(), g);
            // Reset Remembered Port
            port = getTargetPortAt(e.getPoint());
            // If Port was found then Point to Port Location
            if (port != null) current = getEditor().getGraph().toScreen(port.getLocation(null));
            // Else If no Port found Point to Mouse Location
            else current = getEditor().getGraph().snap(e.getPoint());

            // Xor-Paint the new Connector
            paintConnector(getEditor().getGraph().getBackground(), Color.black, g);
            // Consume Event
            e.consume();
        }
        // Call Superclass
        else super.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e)
    {
        // Check Mode and Find Port
        if (e != null && getSourcePortAt(e.getPoint()) != null && !e.isConsumed() && getEditor().getGraph().isPortsVisible())
        {
            // Set Cusor on Graph (Automatically Reset)
            getEditor().getGraph().setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Consume Event
            e.consume();
        }
        super.mouseMoved(e);
    }

    public void paintPort(Graphics g)
    { // If Current Port is Valid
        if (port != null)
        {
            // If Not Floating Port...
            boolean o = (GraphConstants.getOffset(port.getAttributes()) != null);
            // ...Then use Parent's Bounds
            Rectangle2D r = (o) ? port.getBounds() : port.getParentView().getBounds();
            // Scale from Model to Screen
            r = getEditor().getGraph().toScreen(r.getBounds());
            // Add Space For the Highlight Border
            r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 5, r.getHeight() + 5);
            // Paint Port in Preview (=Highlight) Mode
            getEditor().getGraph().getUI().paintCell(g, port, r, true);
        }
    }
}