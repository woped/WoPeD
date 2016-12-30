package org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions;

import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.AbstractMarkingNetTest;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

import java.util.HashSet;
import java.util.Set;

/**
 * indicates all non live transtions with exp. costs.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class NonLiveTransitionTest extends AbstractMarkingNetTest implements INonLiveTranstionTest {

	/**
	 * 
	 * @param markingNet MarkingNet the algorithm is based on
	 */
    public NonLiveTransitionTest(IMarkingNet markingNet) {
        super(markingNet);
    }

    /**
     * @see INonLiveTranstionTest#getNonLiveTransitions()
     */
    @Override
    public Set<TransitionNode> getNonLiveTransitions() {
        Set<TransitionNode> nonLiveTransitions = new HashSet<TransitionNode>();
        Set<IMarking> markingsTemp;
        
        for (int i = 0; i < mNet.getTransitions().length; i++) {
            for (IMarking marking : mNet.getMarkings()) {
                markingsTemp = new HashSet<>();
                markingsTemp.add(marking);
                if (!marking.isTransitionReachable(mNet.getTransitions()[i], markingsTemp)) {
                    nonLiveTransitions.add(mNet.getTransitions()[i]);
                    break;
                }
            }
        }
        return nonLiveTransitions;

    }
}
