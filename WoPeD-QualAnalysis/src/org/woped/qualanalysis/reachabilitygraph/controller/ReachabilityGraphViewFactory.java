package org.woped.qualanalysis.reachabilitygraph.controller;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;


@SuppressWarnings("serial")
public class ReachabilityGraphViewFactory extends DefaultCellViewFactory
{

/*    public CellView createView(GraphModel model, Object cell)
    {
        return view;
    }
*/
	@Override
    protected EdgeView createEdgeView(Object cell)
    {
        return new ReachabilityEdgeView(cell);
    }
	
	@Override
    protected PortView createPortView(Object cell)
    {
    	return new PortView(cell);
    }

	@Override
    protected VertexView createVertexView(Object cell)
    {
        return new ReachabilityPlaceView(cell);
    }
}