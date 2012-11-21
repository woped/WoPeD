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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JToolTip;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.Edge;
import org.jgraph.graph.ParentMap;
import org.jgraph.plaf.GraphUI;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.gui.EditorToolTip;
import org.woped.editor.view.ViewFactory;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The WoPeDJGraph is a representation of <code>com.jgraph.JGraph</code>. It
 * has its own VertexViews, Element creation and connection rules.
 * 
 * 29.04.2003
 */

@SuppressWarnings("serial")
public class WoPeDJGraph extends AbstractGraph
{
	private int minPreferredWidth = 0; /// Minimal preferred width. Used to override the preferred width of the JGraph.
	private int minPreferredHeight = 0; /// Minimal preferred height. Used to override the preferred height of the JGraph.
	
	
    public WoPeDJGraph(WoPeDJGraphGraphModel model, BasicMarqueeHandler editorMarquee, ViewFactory viewFactory)
    {
        this(model, editorMarquee, null, null, viewFactory);
    }

    /**
     * Constructor for PWTJGraph.
     * 
     * @param model
     */
    public WoPeDJGraph(WoPeDJGraphGraphModel model, BasicMarqueeHandler editorMarquee, WoPeDUndoManager undoManager, ViewFactory viewFactory)
    {
        this(model, editorMarquee, undoManager, null, viewFactory);
    }

    /**
     * Constructor for PWTJGraph.
     * 
     * @param model
     */
    public WoPeDJGraph(WoPeDJGraphGraphModel model, BasicMarqueeHandler editorMarquee, WoPeDUndoManager undoManager, GraphUI ui, ViewFactory viewFactory)
    {
        super(model, editorMarquee, undoManager, viewFactory);
        if (ui != null) setUI(ui);

        // Tell the Graph to Select new Cells upon Insertion
        // TODO: setSelectNewCells(true);
        // Make Ports Visible by Default
        //this.setPortsVisible(true);
        // Benjamin Joerger Port are only visible on mouseover now
        this.setPortsVisible(false);
        // Use the Grid (but don't make it Visible)
        this.setGridEnabled(true);
        // Set the Grid Size to 10 Pixel
        this.setGridMode(JGraph.DOT_GRID_MODE);
        this.setGridEnabled(true);
        this.setGridSize(10.00);
        // Set the Snap Size to 2 Pixel
        this.setMinimumMove(1);
        this.setEditClickCount(1);
        this.setAntiAliased(true);
        this.setInvokesStopCellEditing(true);
        this.setEditable(true);
        this.setSize(300, 500);
        this.setHighlightColor(ConfigurationManager.getConfiguration().getSelectionColor());

        // TODO: setAutoSizeOnValueChange(true);
        if (undoManager != null) this.getModel().addUndoableEditListener(undoManager);
    }

    protected void updateHistoryButtons()
    {
        VisualController.getInstance().checkUndoRedo();
    }

    
    //! This map contains all possible connections from a given node type
    private static final Map<Integer,Set<Integer> > connectionTypes;
    static
    {
    	Map<Integer, Set<Integer> >  tmp = new HashMap<Integer, Set<Integer> >();
    	
    	Set<Integer> currentSet = new HashSet<Integer>();
    	currentSet.add(new Integer(AbstractPetriNetElementModel.PLACE_TYPE));
    	// All these node types may only connect to places
    	tmp.put(new Integer(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE),
    			currentSet);
    	tmp.put(new Integer(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE),
    			currentSet);
    	tmp.put(new Integer(AbstractPetriNetElementModel.SUBP_TYPE),
    			currentSet);
    	
    	currentSet = new HashSet<Integer>();
    	currentSet.add(new Integer(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
    	currentSet.add(new Integer(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE));
    	currentSet.add(new Integer(AbstractPetriNetElementModel.SUBP_TYPE));
    	// Places may connect to all the node types above
    	tmp.put(new Integer(AbstractPetriNetElementModel.PLACE_TYPE),
    			currentSet);
    	connectionTypes = Collections.unmodifiableMap(tmp);
    }
    
    /**
     * @param sourceCell
     * @param targetCell
     * @return
     */
    public boolean isValidConnection(AbstractPetriNetElementModel sourceCell, AbstractPetriNetElementModel targetCell)
    {
    	if ((sourceCell == null) || (targetCell == null))
    		return false;
    	
    	boolean result = false;
    	Set<Integer> destinations = connectionTypes.get(new Integer(sourceCell.getType()));
    	if (destinations!=null)
    	{
    		result = (destinations.contains(new Integer(targetCell.getType())));
    	}
    	if (result == true)
    	{
    		// Apart from general connectability
    		// some elements require special attention:
    		// The sub-process element does not allow more than one input and output connection
    		// to be made. For sub-processes, this method will return true if that criteria is met
    		// or the connection to be made is one that already exists
    		// We check for actual JGraph connections here because we're being called multiple
    		// times during the creation of an arc and arc creation within the ModelElementContainer
    		// is the first thing to happen so we would return "not connectable" for the second call
    		// as NetAlgorithms already sees the connection even if it doesn't exist in the jgraph model yet
    		if (sourceCell.getType()==AbstractPetriNetElementModel.SUBP_TYPE)
    		{
    			int nNumOutgoing = 0;
    			for (Iterator<?> i = sourceCell.getPort().edges(); i.hasNext();)
    			{
    				Object o = i.next();
    				if (o instanceof Edge)
    				{        				
    					Edge e = (Edge)o;
    					if ((e.getSource()==sourceCell.getPort())&&
    							(e.getTarget()!=targetCell.getPort()))
    						++nNumOutgoing;
    				}
    			}
    			result = (nNumOutgoing == 0);
    		}
    		if (targetCell.getType()==AbstractPetriNetElementModel.SUBP_TYPE)
    		{
    			int nNumIncoming = 0;
    			for (Iterator<?> i = targetCell.getPort().edges(); i.hasNext();)
    			{
    				Object o = i.next();
    				if (o instanceof Edge)
    				{        				
    					Edge e = (Edge)o;
    					if ((e.getTarget()==targetCell.getPort())&&
    							(e.getSource()!=sourceCell.getPort()))
    						++nNumIncoming;
    				}
    			}
    			result = (nNumIncoming == 0);
    		}

    	}

    	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#createToolTip()
     */
    public JToolTip createToolTip()
    {
        return new EditorToolTip();

    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void redo()
    {
        if (getUndoManager() != null)
        {
            try
            {
                getUndoManager().redo(getGraphLayoutCache());
            } catch (Exception ex)
            {
                LoggerManager.error(Constants.EDITOR_LOGGER, "Could not redo.");
                LoggerManager.debug(Constants.EDITOR_LOGGER, "Exception" + ex);
            } finally
            {
                updateHistoryButtons();
            }
        }
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void undo()
    {
        if (getUndoManager() != null)
        {
            try
            {
                getUndoManager().undo(getGraphLayoutCache());
            } catch (Exception ex)
            {
                LoggerManager.error(Constants.EDITOR_LOGGER, "Could not undo.");
                LoggerManager.debug(Constants.EDITOR_LOGGER, "Exception:" + ex);
            } finally
            {
                updateHistoryButtons();
            }
        }
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @param c
     */
    public void toFront(Object[] c)
    {
        if (c != null && c.length > 0)
        // lai ???
        getGraphLayoutCache().toFront(getGraphLayoutCache().getMapping(c));
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @param c
     */
    public void toBack(Object[] c)
    {
        if (c != null && c.length > 0)
        // lai ???
        getGraphLayoutCache().toBack(getGraphLayoutCache().getMapping(c));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.print.Printable#print(java.awt.Graphics,
     *      java.awt.print.PageFormat, int)
     */
    public int print(Graphics g, PageFormat pageFormat, int page) throws PrinterException
    {
        double oldScale = getScale();
        boolean oldGridMode = isGridVisible();
        try
        {
            setGridVisible(false);
            setDoubleBuffered(false);

            double pageWidth = pageFormat.getImageableWidth();
            double pageHeight = pageFormat.getImageableHeight();
            double xScale = pageWidth / getWidth();
            double yScale = pageHeight / getHeight();
            double scale = Math.min(xScale, yScale);

            double tx = 0.0;
            double ty = 0.0;
            if (xScale > scale)
            {
                tx = 0.5 * (xScale - scale) * getWidth();
            } else
            {
                ty = 0.5 * (yScale - scale) * getHeight();
            }

            ((Graphics2D) g).translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
            ((Graphics2D) g).translate(tx, ty);
            ((Graphics2D) g).scale(scale, scale);

            if (page >= 1)
            {
                return NO_SUCH_PAGE;
            }
            paint(g);
        } finally
        {
            setDoubleBuffered(true);
            setGridVisible(oldGridMode);
            setScale(oldScale);

        }
        return PAGE_EXISTS;
    }

    public void startEditingAtCell(Object arg0)
    {
        if (arg0 instanceof GroupModel)
        {
            super.startEditingAtCell(((GroupModel) arg0).getMainElement().getNameModel());
        } else if (arg0 instanceof AbstractPetriNetElementModel)
        {
            super.startEditingAtCell(((AbstractPetriNetElementModel) arg0).getNameModel());
        } else
        {
            super.startEditingAtCell(arg0);
        }
    }

    public void drawNet(PetriNetModelProcessor processor)
    {
    	for (Iterator<AbstractPetriNetElementModel> iter = processor.getElementContainer().getRootElements().iterator(); iter.hasNext();)
    	{
    		AbstractPetriNetElementModel element = (AbstractPetriNetElementModel) iter.next();
    		GroupModel group = groupName(element, (element.getNameModel()));
    		// The combination of element and element name plus all additional 
    		// cells (resources) is in general not ungroupable
    		group.setUngroupable(false);
    		if (element.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE || 
    				element.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE ||
    				element.getType() == AbstractPetriNetElementModel.SUBP_TYPE)
    		{
    			// Restore display of trigger element if present
    			if (((TransitionModel) element).hasTrigger())
    			{
    				ParentMap pm = new ParentMap();
    				pm.addEntry(((TransitionModel) element).getToolSpecific().getTrigger(), group);
    				HashMap<GroupModel, AttributeMap> hm = new HashMap<GroupModel, AttributeMap>();
    				hm.put(group, group.getAttributes());
    				getModel().insert(new Object[] { ((TransitionModel) element).getToolSpecific().getTrigger() }, hm, null, pm, null);
    			}

    			// Restore display of associated resource if present
    			if (((TransitionModel) element).hasResource())
    			{
    				ParentMap pm = new ParentMap();
    				pm.addEntry(((TransitionModel) element).getToolSpecific().getTransResource(), group);
    				HashMap<GroupModel, AttributeMap> hm = new HashMap<GroupModel, AttributeMap>();
    				hm.put(group, group.getAttributes());
    				getModel().insert(new Object[] { ((TransitionModel) element).getToolSpecific().getTransResource() }, hm, null, pm, null);
    			}
    		}
    		getGraphLayoutCache().insertGroup(group, new Object[] { element, ((AbstractPetriNetElementModel) element).getNameModel() });
    	}
    	for (Iterator<ArcModel> iter = processor.getElementContainer().getArcMap().values().iterator(); iter.hasNext();)
    	{
    		ArcModel arc = iter.next();                    
    		connect(arc, true);
    	}
    } 
    
	/*
	 * The following methods are overridden as a bug fix for a rounding problem
	 * with high scaling factors:
	 * This varient will expect a point in model coordinates
	 * rather than screen coordinates which is really
	 * what JGraph expects all over the place as well
	 * FIXME: SO THIS IS REALLY A BUGFIX FOR JGRAPH 5.9.1
	 * WE MIGHT WANT TO REMOVE THIS ROUTINE WHEN UPDATING TO A FIXED VERSION!
	 * MARKED WITH A FIXME BECAUSE OF THAT.*/
    
	//
	// Grid and Scale
	//
	/**
	 * Returns the given point applied to the grid.
	 * 
	 * @param p
	 *            a point in model coordinates.
	 * @return the same point applied to the grid.
	 */
	public Point2D snap(Point2D p) {
		if (gridEnabled && p != null) {
			double sgs = gridSize /** getScale()*/;
			p.setLocation(Math.round(Math.round(p.getX() / sgs) * sgs), Math
					.round(Math.round(p.getY() / sgs) * sgs));
		}
		return p;
	}

	/**
	 * Returns the given rectangle applied to the grid.
	 * 
	 * @param r
	 *            a rectangle in model coordinates.
	 * @return the same rectangle applied to the grid.
	 */
	public Rectangle2D snap(Rectangle2D r) {
		if (gridEnabled && r != null) {
			double sgs = gridSize /** getScale()*/;
			r.setFrame(Math.round(Math.round(r.getX() / sgs) * sgs), Math
					.round(Math.round(r.getY() / sgs) * sgs), 1 + Math
					.round(Math.round(r.getWidth() / sgs) * sgs), 1 + Math
					.round(Math.round(r.getHeight() / sgs) * sgs));
		}
		return r;
	}

	/**
	 * Returns the given dimension applied to the grid.
	 * 
	 * @param d
	 *            a dimension in model coordinates to snap to.
	 * @return the same dimension applied to the grid.
	 */
	public Dimension2D snap(Dimension2D d) {
		if (gridEnabled && d != null) {
			double sgs = gridSize /** getScale()*/;
			d.setSize(1 + Math.round(Math.round(d.getWidth() / sgs) * sgs),
					1 + Math.round(Math.round(d.getHeight() / sgs) * sgs));
		}
		return d;
	}
	
	/**
	 * Sets the minimum preferred width returned by getPreferredSize()-
	 * Allows overriding of the graph's preferred width.
	 * @param width The minimum width. Set to zero to stop overriding the graph's value.
	 */
	public void setMinPreferredWidth(int width)
	{
		minPreferredWidth = width;
	}
	
	/**
	 * Sets the minimum preferred height returned by getPreferredSize().
	 * Allows overriding of the graph's preferred height.
	 * @param height The minimum height. Set to zero to stop overriding the graph's value.
	 */
	public void setMinPreferredHeight(int height)
	{
		minPreferredHeight = height;
	}
	
	/**
	 * Returns the preferred size of the graph.
	 * The returned values are those returned by super.getPreferredSize(),
	 * except when overridden by setMinPreferredWidth() and setMinPreferredHeight().
	 */
	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();
		size.width = Math.max(size.width, minPreferredWidth);
		size.height = Math.max(size.height, minPreferredHeight);
		return size;
	}
}