package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions;

import java.util.Set;

import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * indicates all non live transiton in a marking net.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public interface INonLiveTranstionTest {
    /**
     * 
     * @return set of all non live transtions.
     */
    Set<TransitionNode> getNonLiveTransitions();
}
