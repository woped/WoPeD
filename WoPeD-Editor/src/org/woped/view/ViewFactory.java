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
package org.woped.view;

import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;
import org.woped.controller.AbstractViewFactory;
import org.woped.model.PortCell;
import org.woped.model.petrinet.GroupModel;
import org.woped.model.petrinet.NameModel;
import org.woped.model.petrinet.OperatorTransitionModel;
import org.woped.model.petrinet.PlaceModel;
import org.woped.model.petrinet.SubProcessModel;
import org.woped.model.petrinet.TransitionModel;
import org.woped.model.petrinet.TransitionResourceModel;
import org.woped.model.petrinet.TriggerModel;
import org.woped.model.uml.ActivityModel;
import org.woped.model.uml.OperatorModel;
import org.woped.model.uml.StateModel;
import org.woped.utilities.WoPeDLogger;
import org.woped.view.petrinet.NameView;
import org.woped.view.petrinet.PlaceView;
import org.woped.view.petrinet.SubProcessView;
import org.woped.view.petrinet.TransAndJoinView;
import org.woped.view.petrinet.TransAndSplitView;
import org.woped.view.petrinet.TransOrSplitView;
import org.woped.view.petrinet.TransSimpleView;
import org.woped.view.petrinet.TransXOrJoinView;
import org.woped.view.petrinet.TransXOrSplitJoinView;
import org.woped.view.petrinet.TransXOrSplitView;
import org.woped.view.petrinet.TransitionResourceView;
import org.woped.view.petrinet.TriggerExtView;
import org.woped.view.petrinet.TriggerResView;
import org.woped.view.petrinet.TriggerTimeView;
import org.woped.view.uml.ActivityView;
import org.woped.view.uml.OperatorView;
import org.woped.view.uml.StateView;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Created on: 29.01.2005 Last Change on: 29.01.2005
 */
public class ViewFactory extends AbstractViewFactory implements WoPeDLogger
{

    public CellView createView(GraphModel model, Object cell)
    {
        CellView view = null;
        if (cell instanceof PortCell) view = createPortView(cell);
        else if (model.isEdge(cell)) view = new ArcView(cell);
        else view = createVertexView(cell);
        // cm.putMapping(cell, view);
        // view.refresh(true); // Create Dependent Views
        view.update();
        return view;
    }

    protected EdgeView createEdgeView(Object cell)
    {
        ArcView view = new ArcView(cell);
        view.update();
        return view;
    }

    protected PortView createPortView(Object cell)
    {
        return new WoPeDPortView(cell);
    }

    protected VertexView createVertexView(Object cell)
    {
        if (cell instanceof NameModel)
        {
            return new NameView(cell);
        } else if (cell instanceof PlaceModel)
        {
            return new PlaceView(cell);
        } else if (cell instanceof SubProcessModel)
        {
            return new SubProcessView(cell);
        } else if (cell instanceof OperatorTransitionModel)
        {

            OperatorTransitionModel aTCell = (OperatorTransitionModel) cell;
            if (aTCell.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE)
            {

                return new TransAndSplitView(cell);
            } else if (aTCell.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE)
            {

                return new TransAndJoinView(cell);
            } else if (aTCell.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE)
            {

                return new TransXOrSplitView(cell);
            } else if (aTCell.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE)
            {

                return new TransXOrJoinView(cell);

            } else if (aTCell.getOperatorType() == OperatorTransitionModel.OR_SPLIT_TYPE)
            {

                return new TransOrSplitView(cell);

            } else if (aTCell.getOperatorType() == OperatorTransitionModel.XOR_SPLITJOIN_TYPE)
            {

                return new TransXOrSplitJoinView(cell);

            } else
            {
                logger.error(" Not known Aalst ViewType" + cell.getClass().toString());
                // return null;
                return super.createVertexView(cell);
            }
        } else if (cell instanceof TransitionModel)
        {
            return new TransSimpleView(cell);
        } else if (cell instanceof TransitionResourceModel)
        {
            return new TransitionResourceView(cell);
        } else if (cell instanceof TriggerModel)
        {
            TriggerModel aTCell = (TriggerModel) cell;
            /* Hier die Überprüfung ob Besondere View ? */
            if (aTCell.getTriggertype() == TriggerModel.TRIGGER_EXTERNAL)
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
                logger.error("Not known Trigger ViewType " + cell.getClass().toString());
                // return null;
                return super.createVertexView(cell);
            }

        } else if (cell instanceof GroupModel)
        {
            return super.createVertexView(cell);
        } else if (cell instanceof ActivityModel)
        {
            return new ActivityView(cell);
        } else if (cell instanceof StateModel)
        {
            return new StateView(cell);
        } else if (cell instanceof OperatorModel)
        {
            return new OperatorView(cell);
        } else
        {
            logger.error("Not known ViewType " + cell.getClass().getName().toString());
            // return null;
            return super.createVertexView(cell);
        }
    }
}