package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.reachabilitygraph.controller.ParallelRouter;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPortModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.AbstractLowLevelPetriNetBuilder;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.qualanalysis.soundness.marking.Marking;

public class ReachabilityGraphModelUsingMarkingNet extends AbstractReachabilityGraphModel {
	private IMarkingNet markingNet;
	private HashSet<DefaultGraphCell> cellsList;

	public ReachabilityGraphModelUsingMarkingNet(IEditor editor) {
		super(editor);
		computeReachabilityGraph();
	}

	public void computeReachabilityGraph() {
		init();

		AbstractLowLevelPetriNetBuilder builder = BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(editor);
        LowLevelPetriNet lowLevelPetriNet = builder.getLowLevelPetriNet();
        markingNet = BuilderFactory.createMarkingNet(lowLevelPetriNet);
		transformMarkingNet2ReachabilityJGraph();
	}

	public ReachabilityJGraph transformMarkingNet2ReachabilityJGraph() {
		cellsList = new HashSet<>();


		for (Marking marking : markingNet.getMarkings()) {
			cellsList.add(getPlace(marking));
		}

		for (Marking marking : markingNet.getMarkings()) {
			for (Arc arc : marking.getSuccessors()) {
				getChildNode(arc, marking);
			}
		}
		graph.getGraphLayoutCache().insert(cellsList.toArray());
		return graph;
	}

	private void getChildNode(Arc arc, Marking parentMarking) {
		Marking marking = arc.getTarget();
		ReachabilityEdgeModel edge = getEdge(arc, parentMarking);

		ReachabilityPlaceModel src = getPlace(parentMarking);
		cellsList.add(src);

		DefaultGraphCell tar = getPlace(marking);
		cellsList.add(tar);

		edge.setSource(src.getChildAt(0));
		edge.setTarget(tar.getChildAt(0));
		cellsList.add(edge);
		// activate parallel routing
		GraphConstants.setRouting(edge.getAttributes(), ParallelRouter.getSharedInstance(view));

	}

	private ReachabilityEdgeModel getEdge(Arc arc, Marking parentMarking) {
		Iterator<DefaultGraphCell> iterCellsList = cellsList.iterator();
		ReachabilityEdgeModel returnEdge = new ReachabilityEdgeModel(arc.getTrigger());
		int differ = 1;
		if (cellsList != null && !cellsList.isEmpty()) {
			while (iterCellsList.hasNext()) {
				DefaultGraphCell cell = iterCellsList.next();
				if (cell instanceof ReachabilityEdgeModel) {
					ReachabilityEdgeModel edge = (ReachabilityEdgeModel) cell;
					// Source
					ReachabilityPortModel srcRpom = (ReachabilityPortModel) edge.getSource();
					ReachabilityPlaceModel srcRplm = (ReachabilityPlaceModel) srcRpom.getParent();
					// Target
					ReachabilityPortModel tarRpom = (ReachabilityPortModel) edge.getTarget();
					ReachabilityPlaceModel tarRplm = (ReachabilityPlaceModel) tarRpom.getParent();
					if (parentMarking == srcRplm.getUserObject() && arc.getTarget() == tarRplm.getUserObject()) {
						GraphConstants.setLabelPosition(edge.getAttributes(), new Point2D.Double(
								GraphConstants.PERMILLE * (6 - (0.6 * differ++)) / 8, -20));
					}
				}
			}
		}
		return returnEdge;
	}

	private ReachabilityPlaceModel getPlace(Marking marking) {
		Iterator<DefaultGraphCell> iter = cellsList.iterator();
		while (iter.hasNext()) {
			DefaultGraphCell comp = iter.next();
			if (comp.getUserObject() != null && comp.getUserObject().equals(marking)) {
				return (ReachabilityPlaceModel) comp;
			}
		}
		ReachabilityPlaceModel place = new ReachabilityPlaceModel(marking);
		// (x,y,w,h)
		GraphConstants.setBounds(place.getAttributes(), new Rectangle2D.Double(0, 0, 80, 20));
		ReachabilityPortModel port = new ReachabilityPortModel();
		place.add(port);
		port.setParent(place);
		return place;
	}

	@Override
	public ReachabilityJGraph getGraph() {
		computeReachabilityGraph();
		return graph;
	}

	@Override
	public String getLegendByID() {
		return markingNet.placesToStringId();
	}

	@Override
	public String getLegendByName() {
		return markingNet.placesToStringName();
	}
	
	public IMarkingNet getMarkingNet(){
		return markingNet;
	}

}
