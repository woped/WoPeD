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
package org.woped.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JToolTip;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.ParentMap;
import org.jgraph.plaf.GraphUI;
import org.woped.gui.EditorToolTip;
import org.woped.model.AbstractElementModel;
import org.woped.model.AbstractModelProcessor;
import org.woped.model.ArcModel;
import org.woped.model.petrinet.AbstractPetriNetModelElement;
import org.woped.model.petrinet.GroupModel;
import org.woped.model.petrinet.PetriNetModelElement;
import org.woped.model.petrinet.TransitionModel;
import org.woped.model.uml.AbstractUMLElementModel;
import org.woped.utilities.WoPeDLogger;
import org.woped.view.ViewFactory;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The WoPeDJGraph is a representation of <code>com.jgraph.JGraph</code>. It
 * has its own VertexViews, Element creation and connection rules.
 * 
 * 29.04.2003
 */
public class WoPeDJGraph extends AbstractGraph implements WoPeDLogger
{

    public WoPeDJGraph(DefaultGraphModel model, BasicMarqueeHandler editorMarquee, ViewFactory viewFactory, int modelPorcessorType)
    {
        this(model, editorMarquee, null, null, viewFactory, modelPorcessorType);
    }

    /**
     * Constructor for PWTJGraph.
     * 
     * @param model
     */
    public WoPeDJGraph(DefaultGraphModel model, BasicMarqueeHandler editorMarquee, WoPeDUndoManager undoManager, ViewFactory viewFactory, int modelPorcessorType)
    {
        this(model, editorMarquee, undoManager, null, viewFactory, modelPorcessorType);
    }

    /**
     * Constructor for PWTJGraph.
     * 
     * @param model
     */
    public WoPeDJGraph(DefaultGraphModel model, BasicMarqueeHandler editorMarquee, WoPeDUndoManager undoManager, GraphUI ui, ViewFactory viewFactory, int modelPorcessorType)
    {
        super(model, editorMarquee, undoManager, viewFactory, modelPorcessorType);
        if (ui != null) setUI(ui);

        // Tell the Graph to Select new Cells upon Insertion
        // TODO: setSelectNewCells(true);
        // Make Ports Visible by Default
        this.setPortsVisible(true);
        // Use the Grid (but don't make it Visible)
        this.setGridEnabled(true);
        // Set the Grid Size to 10 Pixel
        this.setGridSize(10);
        // Set the Snap Size to 2 Pixel
        this.setMinimumMove(1);
        this.setEditClickCount(1);
        this.setAntiAliased(true);
        this.setGridMode(JGraph.DOT_GRID_MODE);
        this.setGridEnabled(true);
        this.setGridSize(10.00);
        this.setInvokesStopCellEditing(true);
        this.setEditable(true);
        this.setSize(300, 500);
        this.setHighlightColor(Color.BLUE);

        // TODO: setAutoSizeOnValueChange(true);
        // this.m_undoManager = new GraphUndoManager()
        // {
        // public void undoableEditHappened(UndoableEditEvent e)
        // {
        // super.undoableEditHappened(e);
        // updateHistoryButtons();
        // }
        // };
        // this.getModel().addUndoableEditListener(m_undoManager);
    }

    protected void updateHistoryButtons()
    {
        VisualController.getInstance().checkUndoRedo();
    }

    /**
     * @param sourceCell
     * @param targetCell
     * @return
     */
    public boolean isValidConnection(AbstractElementModel sourceCell, AbstractElementModel targetCell)
    {
        // Keine Gleichartigen Verbindungen
        if (getModelPorcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
        {
            if (sourceCell.getType() != targetCell.getType())
            {
                // und keine aalst <-> simpletrans verbundungen
                if ((sourceCell.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE && targetCell.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
                        || (sourceCell.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE && targetCell.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE))
                {
                    logger.warn(sourceCell.getId() + "->" + targetCell.getId() + ") Not a valid Connection! Arc not created!");
                    return false;
                } else
                {
                    return true;
                }
            } else
            {
                logger.warn(sourceCell.getId() + "->" + targetCell.getId() + ") Not a valid Connection! Arc not created!");
                return false;
            }
        } else
        {
            return true;
        }
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
                logger.error("Could not redo.");
                logger.debug("Exception", ex);
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
                logger.error("Could not undo.");
                logger.debug("Exception:", ex);
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
        } else if (arg0 instanceof PetriNetModelElement)
        {
            super.startEditingAtCell(((PetriNetModelElement) arg0).getNameModel());
        } else
        {
            super.startEditingAtCell(arg0);
        }
    }

    public void drawNet(AbstractModelProcessor processor)
    {
        if (processor.getProcessorType() != getModelPorcessorType())
        {
            logger.error("Wrong ModelProcessor Type!");
        } else
        {
            if (getModelPorcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
            {
                for (Iterator iter = processor.getElementContainer().getRootElements().iterator(); iter.hasNext();)
                {
                    PetriNetModelElement element = (PetriNetModelElement) iter.next();
                    GroupModel group = groupName(element, (element.getNameModel()));
                    if (element.getType() == AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE || element.getType() == AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE)
                    {
                        if (((TransitionModel) element).hasTrigger())
                        {
                            ParentMap pm = new ParentMap();
                            pm.addEntry(((TransitionModel) element).getToolSpecific().getTrigger(), group);
                            HashMap hm = new HashMap();
                            hm.put(group, group.getAttributes());
                            getModel().insert(new Object[] { ((TransitionModel) element).getToolSpecific().getTrigger() }, hm, null, pm, null);
                        }
                    }
                    getGraphLayoutCache().insertGroup(group, new Object[] { element, ((PetriNetModelElement) element).getNameModel() });
                }
                for (Iterator iter = processor.getElementContainer().getArcMap().values().iterator(); iter.hasNext();)
                {
                    ArcModel arc = (ArcModel) iter.next();
                    connect(arc);
                }
            } else if (getModelPorcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_UML)
            {
                for (Iterator iter = processor.getElementContainer().getRootElements().iterator(); iter.hasNext();)
                {
                    AbstractUMLElementModel element = (AbstractUMLElementModel) iter.next();
                    getGraphLayoutCache().insert(element);
                }
                for (Iterator iter = processor.getElementContainer().getArcMap().values().iterator(); iter.hasNext();)
                {
                    ArcModel arc = (ArcModel) iter.next();
                    connect(arc);
                }
            }

        }

    }

}