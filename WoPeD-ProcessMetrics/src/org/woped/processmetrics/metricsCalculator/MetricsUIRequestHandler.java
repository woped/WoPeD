package org.woped.processmetrics.metricsCalculator;

import org.woped.core.model.ModelElementContainer;

public class MetricsUIRequestHandler {

	public void showInitialData(ModelElementContainer mec){
		MetricsCalculator mc = new MetricsCalculator(mec);
		System.out.println("=== Start of variables");
		System.out.println("Nodes(N): "+mc.calculate("N"));
		System.out.println("Places(P): "+mc.calculate("P"));
		System.out.println("Transitions(T): "+mc.calculate("T"));
		System.out.println("Arcs(A): "+mc.calculate("A"));
		System.out.println("SequenceNodes(SeqN): "+mc.calculate("SeqN"));
		System.out.println("SequencePlaces(SeqP): "+mc.calculate("SeqP"));
		System.out.println("SequenceTransitions(SeqT): "+mc.calculate("SeqT"));
		System.out.println("RoutingNodes(RN): "+mc.calculate("RN"));
		System.out.println("RoutingPlaces(RP): "+mc.calculate("RP"));
		System.out.println("RoutingTransitions(RT): "+mc.calculate("RT"));
		System.out.println("CycleNodes(CycN): "+mc.calculate("CycN"));
		System.out.println("=== Start of calculated values");
		System.out.println("Density(D): "+mc.calculate("D"));
		System.out.println("CoefficientOfConnectivity(CNC): "+mc.calculate("CNC"));
		System.out.println("AvDegreeNodes(DeN): "+mc.calculate("DeN"));
		System.out.println("AvDegreePlaces(DeP): "+mc.calculate("DeP"));
		System.out.println("AvDegreeTransitions(DeT): "+mc.calculate("DeT"));
		System.out.println("Sequentiality(Seq): "+mc.calculate("Seq"));
		System.out.println("RoutingRatio(RR): "+mc.calculate("RR"));
		System.out.println("Heterogenity(CH): "+mc.calculate("CH"));
		System.out.println("Cyclicity(Cyc): "+mc.calculate("Cyc"));
	}
	
	
}
