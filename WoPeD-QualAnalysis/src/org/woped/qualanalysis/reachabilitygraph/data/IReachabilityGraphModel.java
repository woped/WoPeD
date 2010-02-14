package org.woped.qualanalysis.reachabilitygraph.data;

import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;


public interface IReachabilityGraphModel {
	// Possible layouts
	public static final int HIERARCHIC = 0;
	public static final int CIRCLE = 1;
	
	
	public ReachabilityJGraph getGraph();	
	public String getLegendByID();
	public String getLegendByName();
	

}
