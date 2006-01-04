/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.editor.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractMarqueeHandler;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.gui.PopupMenuPetrinet;
import org.woped.editor.utilities.Cursors;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The idea of the marquee handler is to act as a high - level mouse
 *         handler, with additional painting capabilites
 * 
 * 29.04.2003
 */
public class PetriNetMarqueeHandler extends AbstractMarqueeHandler
{

    /**
     * Constructor, the editor must not be <code>null</code>.
     * 
     * @param editor
     */
    public PetriNetMarqueeHandler(IEditor editor)
    {
        super(editor);
    }

    /**
     * The mousePressed method is used to display the popup menu, or to initiate
     * the connection establishment, if the global port variable has been set.
     * 
     * @see BasicMarqueeHandler#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(final MouseEvent e)
    {

        // Fixing Startpoint
        if (port != null && !e.isConsumed() && getEditor().getGraph().isPortsVisible())
        {
            // Remember Start Location
            start = getEditor().getGraph().toScreen(port.getLocation(null));
            // Remember First Port
            firstPort = port;
            // Consume Event
            // e.consume();
        }

        Point2D l = getEditor().getGraph().snap(e.getPoint());

        // If Right Mouse Button
        if (SwingUtilities.isRightMouseButton(e))
        {

            if (getEditor().getGraph().getFirstCellForLocation(l.getX(), l.getY()) != getEditor().getGraph().getSelectionCell()) getEditor().getGraph().clearSelection();
            // super.mousePressed(e);
            getEditor().getGraph().setSelectionCell(getEditor().getGraph().getFirstCellForLocation(l.getX(), l.getY()));
            if (getEditor().getGraph().getSelectionCell() instanceof GroupModel)
            {
                // if (((GroupModel)
                // getGraph().getSelectionCell()).isUngroupable())
                // {} else{
                // getGraph().setSelectionCell(getGraph().getNextCellForLocation(getGraph().getSelectionCell(),
                // l.x, l.y));}
            }
            // e.consume();setDrawingMode(false);turnOffDrawingMode();

        }
        // Check for Shortcut-drawing
        else if (getEditor().isDrawingMode())
        {
            CreationMap map = CreationMap.createMap();
            //map.setPosition((int) l.getX() - 7, (int) l.getY() - 7);
            //            map.setEditOnCreation(false);
            getEditor().setLastMousePosition(e.getPoint());
            if (getEditor().getCreateElementType() > 100 && getEditor().getCreateElementType() < 110)
            {
                map.setType(PetriNetModelElement.TRANS_OPERATOR_TYPE);
                map.setOperatorType(getEditor().getCreateElementType());
                getEditor().createElement(map);
            } else
            {
                map.setType(getEditor().getCreateElementType());
                getEditor().createElement(map);
            }
            getEditor().setCreateElementType(-1);
            getEditor().setDrawingMode(false);
            // e.consume();
        }
        super.mousePressed(e);

    }

    /**
     * The mouseDragged method is messaged repeatedly, before the mouseReleased
     * method is invoked. The method is used to provide the livepreview, that
     * is, to draw a line between the source and target port for visual
     * feedback.
     * 
     * @see BasicMarqueeHandler#mouseDragged(java.awt.event.MouseEvent)
     */
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

    /**
     * The method mouseReleased is called when the mouse button is released. If
     * a valid source and target port exist, the connection is established using
     * the editor � s connect method
     * 
     * @see BasicMarqueeHandler#mouseReleased(java.awt.event.MouseEvent)s
     */
    public void mouseReleased(MouseEvent e)
    {
        if (e != null)// can be called when a key is pressed
        {
            // Scale From Screen to Model
            getEditor().setLastMousePosition(e.getPoint());
            // Reset Mouse Cursor
            getEditor().getGraph().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            // If Right Mouse Button
            if (SwingUtilities.isRightMouseButton(e))
            {
                // graph.setSelectionCells(null);
                // Find Cell in Model Coordinates
                Object c = getEditor().getGraph().getFirstCellForLocation(getEditor().getLastMousePosition().getX(), getEditor().getLastMousePosition().getY());
                // Display PopupMenu
                PopupMenuPetrinet.getInstance().show(getEditor().getGraph(), (int) getEditor().getLastMousePosition().getX(), (int) getEditor().getLastMousePosition().getY());
                // Else if in ConnectMode and Remembered Port is Valid
                getEditor().setDrawingMode(false);
            } else if (e.getClickCount() == 2)
            {
                getEditor().fireViewEvent(new EditorViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.OPEN_PROPERTIES));
            } else
            {
                if (ConfigurationManager.getConfiguration().isSmartEditing() && port == null && firstPort != null && firstPort != port)
                {
                    DefaultPort source = (DefaultPort) firstPort.getCell();
                    CreationMap map = CreationMap.createMap();
                    if (source.getParent() instanceof TransitionModel)
                    {
                        map.setType(PetriNetModelElement.PLACE_TYPE);
                        getEditor().createElement(map);
                    } else if (source.getParent() instanceof PlaceModel)
                    {
                        map.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
                        getEditor().createElement(map);
                    }
                    port = getTargetPortAt(getEditor().getLastMousePosition());
                }
                // If Valid Event, Current and First Port
                if (e != null && !e.isConsumed() && port != null && firstPort != port)
                {
                    // Fetch the Underlying Source Port
                    Port source = (Port) firstPort.getCell();
                    // Fetch the Underlying Target Port
                    Port target = (Port) port.getCell();
                    // CHECK if connection is valid
                    CreationMap map = CreationMap.createMap();
                    map.setArcSourceId(((AbstractElementModel) ((DefaultPort) source).getParent()).getId());
                    map.setArcTargetId(((AbstractElementModel) ((DefaultPort) target).getParent()).getId());
                    getEditor().createArc(map);
                    e.consume();
                }
                if (getEditor().getGraph().getFirstCellForLocation(e.getX(), e.getY()) instanceof ArcModel && e.isShiftDown())
                {
                    getEditor().setLastMousePosition(e.getPoint());
                    getEditor().addPointToSelectedArc();
                }
                super.mouseReleased(e);

                // Consume Event
                // e.consume();
                // Else Repaint the Graph
                getEditor().getGraph().repaint();
            } // Reset Global Vars

            firstPort = port = null;
            start = current = null;
        }
        getEditor().getGraph().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * The marquee handler also implements the mouseMoved method, which is
     * messaged independently of the others, to change the mouse pointer when
     * over a port
     * 
     * @see BasicMarqueeHandler#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e)
    {
        //        System.out.println("moved");
        if (e != null && getEditor().isDrawingMode())
        {
            getEditor().getGraph().setCursor(Cursors.getElementCreationCursor(getEditor().getCreateElementType()));
            e.consume();
        } else
        {
            getEditor().getGraph().setCursor(Cursor.getDefaultCursor());
            Object cell = getEditor().getGraph().getFirstCellForLocation(e.getX(), e.getY());
            if (e != null)
            {
                if (getEditor().getGraph().getPortForLocation(e.getPoint().getX(), e.getPoint().getY()) != null && getEditor().getGraph().isPortsVisible())
                {
                    // Set Cusor on Graph (Automatically Reset)
                    getEditor().getGraph().setCursor(new Cursor(Cursor.HAND_CURSOR));
                    // Consume Event
                    e.consume();
                } else if (cell instanceof GroupModel || cell instanceof AbstractElementModel || cell instanceof NameModel)
                {
                    // Set Cusor on Graph (Automatically Reset)
                    getEditor().getGraph().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    // Consume Event
                    e.consume();
                }
            }
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