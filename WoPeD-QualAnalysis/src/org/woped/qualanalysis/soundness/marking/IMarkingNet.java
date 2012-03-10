package org.woped.qualanalysis.soundness.marking;

import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

public interface IMarkingNet extends INodeNet<Marking> {
	
	public TransitionNode[] getActivatedTransitions(Marking marking);
	
	public Marking getInitialMarking();
	
	public Set<Marking> getMarkings();
	
	public PlaceNode[] getPlaces();
	
	public TransitionNode[] getTransitions();
	
	public String placesToString();
	
	public String placesToStringId();
	
	public String placesToStringName();
	
	public Marking calculateSucceedingMarking(Marking parentMarking, TransitionNode transition);
}
