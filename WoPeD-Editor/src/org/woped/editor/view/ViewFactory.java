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
package org.woped.editor.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractViewFactory;
import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.view.petrinet.NameView;
import org.woped.editor.view.petrinet.PlaceView;
import org.woped.editor.view.petrinet.SubProcessView;
import org.woped.editor.view.petrinet.TransAndJoinView;
import org.woped.editor.view.petrinet.TransAndJoinXOrSplitView;
import org.woped.editor.view.petrinet.TransAndSplitJoinView;
import org.woped.editor.view.petrinet.TransAndSplitView;
import org.woped.editor.view.petrinet.TransOrSplitView;
import org.woped.editor.view.petrinet.TransSimpleView;
import org.woped.editor.view.petrinet.TransXOrJoinView;
import org.woped.editor.view.petrinet.TransXOrSplitJoinView;
import org.woped.editor.view.petrinet.TransXOrSplitView;
import org.woped.editor.view.petrinet.TransXorJoinAndSplitView;
import org.woped.editor.view.petrinet.TransitionResourceView;
import org.woped.editor.view.petrinet.TriggerExtView;
import org.woped.editor.view.petrinet.TriggerResView;
import org.woped.editor.view.petrinet.TriggerTimeView;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Created on: 29.01.2005 Last Change on: 29.01.2005
 */

@SuppressWarnings("serial")
public class ViewFactory extends AbstractViewFactory
{
	private IEditor editor;
	
	public ViewFactory(IEditor editor)
	{
		super();
		this.editor = editor;
	}

    public CellView createView(GraphModel model, Object cell)
    {
    	
        CellView view = null;
        if (cell instanceof DefaultPort) view = createPortView(cell);
        else if (model.isEdge(cell)) view = new ArcView(cell);
        else view = createVertexView(cell);
        // cm.putMapping(cell, view);
        // view.refresh(true); // Create Dependent Views
        view.update(editor.getGraph().getGraphLayoutCache());
        return view;
    }

    protected EdgeView createEdgeView(Object cell)
    {
        ArcView view = new ArcView(cell);
        view.update(editor.getGraph().getGraphLayoutCache());
        return view;
    }

    protected PortView createPortView(Object cell)
    {
        return new WoPeDPortView(cell);
//        return new PortView(cell);
    }

    protected VertexView createVertexView(Object cell)
    {
        if (cell instanceof NameModel)
        {
            return new NameView(cell);
        } else if (cell instanceof PlaceModel)
        {
            return new PlaceView(cell, editor);
        } else if (cell instanceof SubProcessModel)
        {
            return new SubProcessView(cell, editor);
        } else if (cell instanceof OperatorTransitionModel)
        {

            OperatorTransitionModel aTCell = (OperatorTransitionModel) cell;
            if (aTCell.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE)
            {

                return new TransAndSplitView(cell, editor);
            } else if (aTCell.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE)
            {

                return new TransAndJoinView(cell, editor);
            } else if (aTCell.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
            {
            	return new TransAndSplitJoinView(cell, editor);
            
            } else if (aTCell.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)
            {

                return new TransXOrSplitView(cell, editor);
            } else if (aTCell.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
            {

                return new TransXOrJoinView(cell, editor);

            } else if (aTCell.getOperatorType() == OperatorTransitionModel.OR_SPLIT_TYPE)
            {

                return new TransOrSplitView(cell, editor);

            } else if (aTCell.getOperatorType() == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE)
            {

                return new TransXOrSplitJoinView(cell, editor);

            } else if (aTCell.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)
            {
                return new TransAndJoinXOrSplitView(cell, editor);

            } else if (aTCell.getOperatorType() == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)
            {
                return new TransXorJoinAndSplitView(cell, editor);

            } else
            {
                LoggerManager.error(Constants.EDITOR_LOGGER, " Not known Aalst ViewType" + cell.getClass().toString());
                // return null;
                return super.createVertexView(cell);
            }
        } else if (cell instanceof TransitionModel)
        {
            return new TransSimpleView(cell, editor);
        } else if (cell instanceof TransitionResourceModel)
        {
            return new TransitionResourceView(cell);
        } else if (cell instanceof TriggerModel)
        {
            TriggerModel aTCell = (TriggerModel) cell;
            /* Hier die �berpr�fung ob Besondere View ? */
            if (aTCell.getTriggertype() == TriggerModel.TRIGGER_MESSAGE)
            {

                return new TriggerExtView(cell);

            } else if (aTCell.getTriggertype() == TriggerModel.TRIGGER_RESOURCE)
            {

                return new TriggerResView(cell);

            } else if (aTCell.getTriggertype() == TriggerModel.TRIGGER_TIME)
            {

                return new TriggerTimeView(cell);

            } else
            {
                LoggerManager.error(Constants.EDITOR_LOGGER, "Not known Trigger ViewType " + cell.getClass().toString());
                // return null;
                return super.createVertexView(cell);
            }

        } else if (cell instanceof GroupModel)
        {
            // return super.createVertexView(cell);
        	return new VertexView(cell) {
        		private VertexRenderer renderer = new VertexRenderer()
        		{
        			protected void paintSelectionBorder(Graphics g) {
        				if (selected)
        				{
        					Dimension d = getSize();
        					Color primary = ConfigurationManager.getConfiguration().getSelectionColor(); 
        					Color borderColor = new Color(209, 209, 255);
        					Color backgroundColor = new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 22);
        					g.setColor(backgroundColor);
        					g.fillRect(0, 0, d.width - 1, d.height - 1);
        					g.setColor(borderColor);
        					g.drawRect(0, 0, d.width - 1, d.height - 1);
        				}
        			}
        		};
        		
        	    public CellViewRenderer getRenderer()
        	    {
        	    	return renderer;
        	    	
        	    }
        	    
        	};
        
        } else
        {
            LoggerManager.error(Constants.EDITOR_LOGGER, "Not known ViewType " + cell.getClass().getName().toString());
            // return null;
            return super.createVertexView(cell);
        }
    }
}