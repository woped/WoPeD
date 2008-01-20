package org.woped.core.qualanalysis;

import org.jgraph.JGraph;
import org.woped.core.controller.IEditor;

public interface IReachabilityGraph {
	public JGraph getJGraph(IEditor editor);        
}
