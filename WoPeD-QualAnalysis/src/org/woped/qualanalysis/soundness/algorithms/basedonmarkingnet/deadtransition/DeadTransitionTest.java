package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition;

import java.util.HashSet;
import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.AbstractMarkingNetTest;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * @see IDeadTransitionTest
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class DeadTransitionTest extends AbstractMarkingNetTest implements IDeadTransitionTest {

	/**
	 * 
	 * @param mNet MarkingNet the algorithm is based on
	 */
    public DeadTransitionTest(MarkingNet mNet) {
        super(mNet);
    }

    /**
     * @see IDeadTransitionTest#getDeadTransitions()
     */
    @Override
    public Set<TransitionNode> getDeadTransitions() {
        Set<TransitionNode> switchableTransitions = new HashSet<TransitionNode>();
        Set<TransitionNode> deadTransitions = new HashSet<TransitionNode>();

        // look for all switchable transitions
        for (Marking marking : mNet.getMarkings()) {
            for (Arc arc : marking.getSuccessors()) {
                switchableTransitions.add(arc.getTrigger());
            }
        }

        // loop over all transitions. transition is not switchable -> dead transition
        for (int i = 0; i < mNet.getTransitions().length; i++) {
            if (!switchableTransitions.contains(mNet.getTransitions()[i])) {
                deadTransitions.add(mNet.getTransitions()[i]);
            }
        }
        return deadTransitions;

    }

}
