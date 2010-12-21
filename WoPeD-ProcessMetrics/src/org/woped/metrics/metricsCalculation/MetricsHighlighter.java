package org.woped.metrics.metricsCalculation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetricsHighlighter {

	private static Map<String, MetricHighlighting> highlightMap = new HashMap<String, MetricHighlighting>();
	
	public static void addHighlight(String metricsKey, Set<String> nodeids, Set<String> arcids, boolean ignoreCall){
		if(ignoreCall) return;
		MetricHighlighting highlight = highlightMap.get(metricsKey);
		if(highlight == null)
			 highlight = new MetricHighlighting();
		highlight.addNodeIDs(nodeids);
		highlight.addArcIDs(arcids);
		highlightMap.put(metricsKey, highlight);
	}
	
	public static MetricHighlighting getHighlighting(String id){
		return highlightMap.get(id);
	}
	
}
