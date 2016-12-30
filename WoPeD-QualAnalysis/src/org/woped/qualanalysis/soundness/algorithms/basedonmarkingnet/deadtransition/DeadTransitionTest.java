package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition;

import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.AbstractMarkingNetTest;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

import java.util.HashSet;
import java.util.Set;

/**
 * @see IDeadTransitionTest
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class DeadTransitionTest extends AbstractMarkingNetTest implements IDeadTransitionTest {

	/**
	 * 
	 * @param markingNet MarkingNet the algorithm is based on
	 */
    public DeadTransitionTest(IMarkingNet markingNet) {
        super(markingNet);
    }

    /**
     * @see IDeadTransitionTest#getDeadTransitions()
     */
    @Override
    public Set<TransitionNode> getDeadTransitions() {
        Set<TransitionNode> switchableTransitions = new HashSet<TransitionNode>();
        Set<TransitionNode> deadTransitions       = new HashSet<TransitionNode>();

        switchableTransitions = getSwitchableTransitions();

        // loop over all transitions. transition is not switchable -> dead transition
        if (mNet.getTransitions() != null) {
        	for (int i = 0; i < mNet.getTransitions().length; i++) {
            	if (!switchableTransitions.contains(mNet.getTransitions()[i])) {
                	deadTransitions.add(mNet.getTransitions()[i]);
            	}
        	}
        }
        return deadTransitions;

    }

	public Set<TransitionNode> getSwitchableTransitions() {
        Set<TransitionNode> switchableTransitions = new HashSet<TransitionNode>();
        
		// look for all switchable transitions
        for (IMarking marking : mNet.getMarkings()) {
            for (Arc arc : marking.getSuccessors()) {
            	switchableTransitions.add(arc.getTrigger());
            }
        }
        return switchableTransitions;
	}

}
