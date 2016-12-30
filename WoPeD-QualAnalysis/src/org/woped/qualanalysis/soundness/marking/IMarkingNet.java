package org.woped.qualanalysis.soundness.marking;

import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

public interface IMarkingNet extends INodeNet<IMarking> {
	
	public TransitionNode[] getActivatedTransitions(IMarking marking);
	
	public IMarking getInitialMarking();
	
	public Set<IMarking> getMarkings();
	
	public PlaceNode[] getPlaces();
	
	public TransitionNode[] getTransitions();
	
	public String placesToString();
	
	public String placesToStringId();
	
	public String placesToStringName();
	
	IMarking calculateSucceedingMarking(IMarking parentMarking, TransitionNode transition);
}
