package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition;

import java.util.Set;

import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * interface to indicate dead transitions.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface IDeadTransitionTest {
    /**
     * 
     * @return a set of all dead transitions
     */
    Set<TransitionNode> getDeadTransitions();
}
