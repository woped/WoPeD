package org.woped.metrics.metricsCalculation.UITypes;

import java.util.List;

import org.woped.metrics.metricsCalculation.StringPair;

public class UIMetricsGroup {

	private String name;
	private List<StringPair> values;
	
	public UIMetricsGroup(String name, List<StringPair> values){
		this.name = name;
		this.values = values;
	}
	
	public String getName(){
		return name;
	}
	
	public List<StringPair> getValues(){
		return values;
	}
	
}
