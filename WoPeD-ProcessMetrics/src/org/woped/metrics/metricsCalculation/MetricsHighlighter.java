package org.woped.metrics.metricsCalculation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetricsHighlighter {

	private Map<String, MetricHighlighting> highlightMap = new HashMap<String, MetricHighlighting>();
	/**
	 * The Highlighter can be inverted to remove rather than add highlights
	 */
	private boolean inverted = false;
	
	public void setInverted(boolean inv){
		inverted = inv;
	}
	
	/**
	 * Adds (or if flagged removes) highlights
	 * 
	 * @param metricsKey	ID of the metric containing the highlight
	 * @param nodeids		IDs of nodes to be highlighted
	 * @param arcids		IDs of arcs to be highlighted
	 * @param ignoreCall	Whether or not this call should be ignored (No highlighting required)
	 */
	public void addHighlight(String metricsKey, Set<String> nodeids, Set<String> arcids, boolean ignoreCall){
		if(ignoreCall) return;
		MetricHighlighting highlight = highlightMap.get(metricsKey);
		if(highlight == null)
			 highlight = new MetricHighlighting();
		if(!inverted){
			highlight.addNodeIDs(nodeids);
			highlight.addArcIDs(arcids);
		}
		else{
			highlight.removeNodeIDs(nodeids);
			highlight.removeArcIDs(arcids);
		}
		highlightMap.put(metricsKey, highlight);
	}
	
	/**
	 * Merges the highlights of two metrics into one
	 * 
	 * @param metricsKey	The first of the IDs and the one that should contain the merged result
	 * @param copySource	The second of the IDs
	 * @param ignorecall	Whether or not this call should be ignored (No highlighting required)
	 */
	public void mergeHighlights(String metricsKey, String copySource, boolean ignorecall){
		MetricHighlighting source = highlightMap.get(copySource);
		addHighlight(metricsKey, source.getNodeIDs(), source.getArcIDs(), ignorecall);
	}
	
	/**
	 * 
	 * Merges the highlights of one metric into multiple others
	 * 
	 * @param stack			All the metrics that should receive the merge
	 * @param copySource	The merger source ID
	 * @param ignorecall	Whether or not this call should be ignored (No highlighting required)
	 */
	public void mergeHighlights(Set<String> stack, String copySource, boolean ignorecall){
		MetricHighlighting source = highlightMap.get(copySource);
		if(source == null) return;
		for(String key:stack)
			addHighlight(key, source.getNodeIDs(), source.getArcIDs(), ignorecall);
	}
	
	public void addHighlight(Set<String> stack, Set<String> nodeids, Set<String> arcids, boolean ignoreCall){
		for(String key:stack)
			addHighlight(key, nodeids, arcids, ignoreCall);
	}
	
	public MetricHighlighting getHighlighting(String id){
		return highlightMap.get(id);
	}
	
}
