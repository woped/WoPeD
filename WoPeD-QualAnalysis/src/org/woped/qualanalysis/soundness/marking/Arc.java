package org.woped.qualanalysis.soundness.marking;

import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 *
 * this class represents the Arc to a successor of a marking
 * contains the succeeding marking and the triggering transition
 */
public class Arc {
	// declaration
	private IMarking target;
	private TransitionNode trigger;
	
	/**
	 * 
	 * @param target the target of this Arc
	 * @param trigger the transition that was switched to reach the target
	 */
	public Arc(IMarking target, TransitionNode trigger){
		this.target = target;
		this.trigger = trigger;
	}
	
	/**
	 * @return the Trigger of an succeeding mark
	 */
	public TransitionNode getTrigger(){
		return this.trigger;
	}
	
	/**
	 * @return the succeeding mark
	 */
	public IMarking getTarget(){
		return this.target;
	}
}
