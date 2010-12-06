package org.woped.metrics.metricsCalculation;

import java.util.Set;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.model.ModelElementContainer;

public class MetricsUIRequestHandler {

	public void showInitialData(ModelElementContainer mec){
		
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
		Set<String> variables = metricsConfig.getVariableIDs();
		Set<String> algorithms = metricsConfig.getAlgorithmIDs();
		MetricsCalculator mc = new MetricsCalculator(mec);
		System.out.println("=== Start of variables");
		if(variables != null) for(String variable:variables)
			System.out.println(variable+": "+mc.calculate(variable));
		System.out.println("=== Start of calculated values");
		if(algorithms != null) for(String algorithm:algorithms)
			System.out.println(algorithm+": "+mc.calculate(algorithm));
	}
}