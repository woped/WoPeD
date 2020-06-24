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
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractMarqueeHandler;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.ViewEvent;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.action.EditorViewEvent;
import org.woped.editor.gui.PopupMenuPetrinet;
import org.woped.editor.utilities.Cursors;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 *         The idea of the marquee handler is to act as a high - level mouse handler, with additional painting capabilites
 * 
 *         29.04.2003
 */
public class PetriNetMarqueeHandler extends AbstractMarqueeHandler {
	
    private static int SCROLL_BORDER = 18;
    private static int SCROLL_SIZE_INCREMENT = 25;

    private AbstractApplicationMediator mediator;

    /**
     * Constructor, the editor must not be <code>null</code>.
     * 
     * @param editor
     */
    public PetriNetMarqueeHandler(IEditor editor, AbstractApplicationMediator mediator) {
        super(editor);
        this.mediator = mediator;
    }

    /**
     * The mousePressed method is used to display the popup menu, or to initiate the connection establishment, 
     * if the global port variable has been set.
     * 
     * @see BasicMarqueeHandler#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e) {  

    	if (port != null && !e.isConsumed() && getGraph().isPortsVisible()) {
            start = getGraph().toScreen(port.getLocation(null));
            firstPort = port;
        }

        if (getGraph().getSelectionCell() instanceof NameModel)
         	getGraph().clearSelection();
	    
        super.mousePressed(e);
    }

    /**
     * The mouseDragged method is messaged repeatedly, before the mouseReleased method is invoked. 
     * The method is used to provide the live preview, that is, to
     * draw a line between the source and target port for visual feedback.
     * 
     * @see BasicMarqueeHandler#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    	
    	getEditor().setDrawingMode(false);
         // If remembered Start Point is Valid
        if (start != null && !e.isConsumed()) {
            Dimension graphSize = getGraph().getSize();
            int graphWidth = graphSize.width;
            int graphHeight = graphSize.height;
            boolean changedSize = false;

            // Are we in reach of the right border?
            if ((graphWidth - e.getX()) < SCROLL_BORDER) {
                // Make the graph wider.
                getGraph().setMinPreferredWidth(graphWidth + SCROLL_SIZE_INCREMENT);
                changedSize = true;
            }

            if ((graphHeight - e.getY()) < SCROLL_BORDER) {
                // Make the graph higher.
                getGraph().setMinPreferredHeight(graphHeight + SCROLL_SIZE_INCREMENT);
                changedSize = true;
            }

            // Xor-Paint the old Connector (Hide old Connector)
            paintConnector(Color.black, getGraph().getBackground(), getGraph().getGraphics());

            // Resize the graph if necessary.
            if (changedSize) {
                getGraph().revalidate();
                getGraph().scrollPointToVisible(e.getPoint());
            }

            // Reset Remembered Port
            port = getTargetPortAt(e.getPoint());
            // end point of line is the current mouse location
            current = getGraph().snap(e.getPoint());

            // Xor-Paint the new Connector
            paintConnector(getGraph().getBackground(), Color.black, getGraph().getGraphics());
            // Consume Event
            e.consume();
        }
        else 
        	super.mouseDragged(e);
    }

    /**
     * The method mouseReleased is called when the mouse button is released. 
     * If a valid source and target port exist, the connection is established using the
     * editor's connect method
     * 
     * @see BasicMarqueeHandler#mouseReleased(java.awt.event.MouseEvent)
     */
	@Override
	public void mouseReleased(MouseEvent e) {
		
		boolean rightButtonClickedInDrawingMode = false;
		
		if (e == null) {
			return;
		}
		
		// Undo setting of minimum preferred size during mouse dragging 
		getGraph().setMinPreferredWidth(0);
		getGraph().setMinPreferredHeight(0);
		getEditor().setLastMousePosition(e.getPoint());

        if (getEditor().isDrawingMode() && (SwingUtilities.isRightMouseButton(e) || e.getClickCount() == 2)) {
        	getEditor().setDrawingMode(false);
        	if (SwingUtilities.isRightMouseButton(e)) {
        		rightButtonClickedInDrawingMode = true;
        	} 
  	    }
		
		// If in drawing mode, create new node at current position
		if (getEditor().isDrawingMode() && firstPort == null) {
			CreationMap map = CreationMap.createMap();
			if (getEditor().getCreateElementType() > 100 && getEditor().getCreateElementType() < 110) {
				map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
				map.setOperatorType(getEditor().getCreateElementType());
				getEditor().create(map, true);
			} 
			else {
				map.setType(getEditor().getCreateElementType());
				getEditor().create(map, true);
			}
			
			return;
		}

		// Set normal cursor and determine current position of mouse click
		getGraph().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		Object currentCell = getGraph().getFirstCellForLocation(e.getX(), e.getY());
		
		// Set selection to object under cursor unless group selection is active
		if (currentCell != null && getGraph().getSelectionCount() <= 1)
			getGraph().setSelectionCell(currentCell);

		// Handle right mouse button behaviour
		if (SwingUtilities.isRightMouseButton(e)) {

			if (!getEditor().isDrawingMode() || rightButtonClickedInDrawingMode) {
				VisualController.getInstance().setArcpointSelection(isArcPoint(e));
				e.consume();
				PopupMenuPetrinet.setMediator(mediator);
				PopupMenuPetrinet.getInstance().show(currentCell, getGraph(),
						(int) (getEditor().getLastMousePosition().getX()),
						(int) (getEditor().getLastMousePosition().getY()));
			}
	                 
			return;
		} 
		else {
			if (e.getClickCount() == 2) {
				// Handle left mouse double click behaviour
				while (currentCell instanceof GroupModel) {
					currentCell = ((GroupModel) currentCell).getMainElement();
				}
			
				// Double click on subprocess element opens subprocess editor
				if (currentCell instanceof SubProcessModel) {
					getEditor().fireViewEvent(
						new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI,
											AbstractViewEvent.OPEN_SUBPROCESS));
				}
				// Double click on normal element opens property dialog
				else {
					getEditor().setLastMousePosition(e.getPoint());
					getEditor().fireViewEvent(
						new EditorViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_EDIT,
												  AbstractViewEvent.OPEN_PROPERTIES));

				}
			} 
			else {
				// Handle single left mouse button click behaviour
				if (ConfigurationManager.getConfiguration().isSmartEditing()
						&& port == null && firstPort != null && firstPort != port) {
					// Smart edit is enabled, i. e. create elements on the fly
					CreationMap[] maps = new CreationMap[2];
					boolean allowConnection = true;
					Object element = ((firstPort != null) ? ((DefaultPort) firstPort.getCell()).getParent() : null);
					
					if (element instanceof AbstractPetriNetElementModel) {
						allowConnection = ((AbstractPetriNetElementModel) element).getAllowOutgoingConnections();
					}
					
					// Check if connection is between valid nodes
					if (allowConnection) {
						DefaultPort source = (DefaultPort) firstPort.getCell();
						CreationMap map = CreationMap.createMap();
						if (source.getParent() instanceof TransitionModel) {
							map.setType(AbstractPetriNetElementModel.PLACE_TYPE);
							map.setId(getEditor().getModelProcessor().getNewElementId(map.getType()));
							maps[0] = map;
						} 
						else {
							if (source.getParent() instanceof PlaceModel) {
								map.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
								map.setId(getEditor().getModelProcessor().getNewElementId(map.getType()));
								maps[0] = map;
							}
						}

						String targetId = map.getId();
						map = CreationMap.createMap();
						map.setArcSourceId(((AbstractPetriNetElementModel) (source).getParent()).getId());
						map.setArcTargetId(targetId);
						maps[1] = map;
					}

					GraphCell[] result = getEditor().createAll(maps);
					getGraph().startEditingAtCell(result[0]);
				} 
				else {
					// No smart editing, just draw connections
					if (e != null && !e.isConsumed() && port != null && firstPort != null && firstPort != port) {
						// Fetch the underlying source and target port
						Port source = (Port) firstPort.getCell();
						Port target = (Port) port.getCell();

						// Check if connection is valid
						CreationMap map = CreationMap.createMap();
						map.setArcSourceId(((AbstractPetriNetElementModel) ((DefaultPort) source).getParent()).getId());
						map.setArcTargetId(((AbstractPetriNetElementModel) ((DefaultPort) target).getParent()).getId());
						getEditor().create(map, true);
						e.consume();
					}
				}
			
				// Handle special case for arc point selection
				if (getGraph().getFirstCellForLocation(e.getX(), e.getY()) instanceof ArcModel) {
					getEditor().setLastMousePosition(e.getPoint());
					getEditor().addPointToSelectedArc();
				}
			}


			super.mouseReleased(e);
			e.consume();
			getGraph().repaint();
		}

		// Reset global variables
		firstPort = port = null;
		start = current = null;
		getGraph().setCursor(Cursor.getDefaultCursor());
	}

    /**
     * The marquee handler also implements the mouseMoved method, which is messaged independently of the others, 
     * to change the mouse pointer when over a port
     * 
     * @see BasicMarqueeHandler#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    	getGraph().setCursor(Cursor.getDefaultCursor());
    	Object cell = getGraph().getFirstCellForLocation(e.getX(), e.getY());
    	if (e != null) {
    		if (getGraph().getPortForLocation(e.getPoint().getX(), e.getPoint().getY()) != null && getGraph().isEnabled()) {
    			// Set Cursor on Graph (Automatically Reset)
    			getGraph().setPortsVisible(true);
    			getGraph().setCursor(new Cursor(Cursor.HAND_CURSOR));
    			e.consume();
    		} else if ((cell instanceof GroupModel || cell instanceof AbstractPetriNetElementModel || cell instanceof NameModel)
    				&& getGraph().isEnabled()) {
    			// Set Cursor on Graph (Automatically Reset)
    			getGraph().setPortsVisible(false);
    			getGraph().setCursor(new Cursor(Cursor.MOVE_CURSOR));
     			e.consume();
    		}
    		else if (getEditor().isDrawingMode()) {
    			getGraph().setCursor(Cursors.getElementCreationCursor(getEditor().getCreateElementType()));
    			e.consume();
    		} 
    	}

    	super.mouseMoved(e);
    }

    @Override
    public void paintPort(Graphics g) { // If Current Port is Valid
        if (port != null) {
            // If Not Floating Port...
            boolean o = (GraphConstants.getOffset(port.getAttributes()) != null);
            // ...Then use Parent's Bounds
            Rectangle2D r = (o) ? port.getBounds() : port.getParentView().getBounds();
            // Scale from Model to Screen
            r = getGraph().toScreen(r.getBounds());
            // Add Space For the Highlight Border
            r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 5, r.getHeight() + 5);
            // Paint Port in Preview (=Highlight) Mode
            getGraph().getUI().paintCell(g, port, r, true);
        }
    }

	public boolean isArcPoint(MouseEvent e) {
		Object selectedCell = getGraph().getSelectionCell();

		if (selectedCell instanceof ArcModel)
		{
			Point2D clickPoint = getGraph().fromScreen(e.getPoint());
			Point2D[] points = ((ArcModel) selectedCell).getPoints();
			double minDist = Double.MAX_VALUE;
			int pos = -1;
			for (int i = 1; i < points.length - 1; i++)
			{
				if (clickPoint.distance(points[i]) < minDist)
				{
					pos = i;
					minDist = clickPoint.distance(points[i]);
				}
			}
			if (pos != -1 && minDist < 10)
			{
				return true;
			}
		}	
		return false;
	}
	
	public boolean isUngroupable(Object object) {
		return (object == null ? false : object instanceof GroupModel && ((GroupModel)object).isUngroupable()); 
	}
}