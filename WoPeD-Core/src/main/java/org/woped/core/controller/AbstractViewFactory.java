/*
 * Created on 19.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.core.controller;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

/**
 * @author lai
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractViewFactory extends DefaultCellViewFactory
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	abstract public CellView createView(GraphModel model, Object cell);

    abstract protected EdgeView createEdgeView(Object cell);

    abstract protected PortView createPortView(Object cell);

    protected VertexView createVertexView(Object cell)
    {
        return super.createVertexView(cell);
    }
}
