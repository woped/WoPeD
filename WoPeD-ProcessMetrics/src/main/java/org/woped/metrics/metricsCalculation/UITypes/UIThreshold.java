package org.woped.metrics.metricsCalculation.UITypes;

import org.woped.core.config.IMetricsConfiguration;

public class UIThreshold {

	private double from;
	private double to;
	private IMetricsConfiguration.MetricThresholdState threshold;
	
	public UIThreshold(double from, double to, IMetricsConfiguration.MetricThresholdState threshold){
		this.from = from;
		this . to = to;
		this.threshold = threshold;
	}

	public double getFrom() {
		return from;
	}

	public double getTo() {
		return to;
	}

	public IMetricsConfiguration.MetricThresholdState getThreshold() {
		return threshold;
	}
}
