package org.woped.qualanalysis.reachabilitygraph.data;

import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;


public interface IReachabilityGraphModel {

    /**
     * Gets the graph object to display
     *
     * @return the graph object to display
     */
	ReachabilityJGraph getGraph();

    /**
     * Gets a string describing the positions of the places in a marking.
     * <p>
     * The id of a place is used for identification.
     *
     * @return a string describing the positions of a marking
     */
	String getLegendByID();

    /**
     * Gets a string describing the positions of the places in a marking.
     * <p>
     * The name of a place is used for identification.
     *
     * @return a string describing the positions of a marking
     */
    String getLegendByName();
}
