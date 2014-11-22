package org.woped.qualanalysis.soundness.builder.markingnet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.Marking;

/**
 * creates marking net based on algorithm page 121 "Business Process and WorkflowManagement" author Cornelia Richter von Hagen, Wolfried Stucky.
 * 
 * @see AbstractMarkingNetBuilder
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class MarkingNetBuilderBook extends AbstractMarkingNetBuilder {

    /**
     * 
     * @param lolNet source low level petri net.
     */
    public MarkingNetBuilderBook(LowLevelPetriNet lolNet) {
        super(lolNet);
    }

    /**
     * creates marking net.
     */
    @Override
    public void createMarkingNet() {
        calculateSucceedingMarkings(mNet.getInitialMarking());
    }

    /**
     * method for building the markingNet
     * 
     * @param marking the marking to start calculating succeeding markings with
     */
    private void calculateSucceedingMarkings(Marking marking) {
        // We use a map here to be able to find markings by hash. Unfortunately, Set<> does not
        // allow us to access a matching object within the set, but this is exactly what we want
        // to do here.
        Map<Integer, Marking> markingsToCheck = new HashMap<Integer, Marking>();
        Map<Integer, Marking> markings = new HashMap<Integer, Marking>();
        TransitionNode[] transitions;
        Marking currentMarking;
        Marking newMarking;
        Marking compareMarking;
        Marking equalMarking = null;

        markingsToCheck.put(new Integer(marking.hashCode()), marking);

        while (!markingsToCheck.isEmpty()) {
        	Integer currentKey = markingsToCheck.keySet().iterator().next();
            currentMarking = markingsToCheck.get(currentKey);
            markingsToCheck.remove(currentKey);
            markings.put(currentKey, currentMarking);

            transitions = mNet.getActivatedTransitions(currentMarking);

            for (int i = 0; i < transitions.length; i++) {
                newMarking = mNet.calculateSucceedingMarking(currentMarking, transitions[i]);
                compareMarking = currentMarking;
                while (compareMarking != null && !compareMarking.smallerEquals(newMarking)) {
                    compareMarking = compareMarking.getPredecessor();
                }
                if (compareMarking != null) {

                    for (int position = 0; position < newMarking.getTokens().length; position++) {
                        if ((newMarking.getTokens()[position].intValue() != compareMarking.getTokens()[position]
                                .intValue())) {
                            newMarking.setPlaceUnlimited(position);
                        }
                    }
                }

                // Check if this marking exists either in the check queue or has been added
                // to the marking graph already. In this case, use the existing object and
                // discard the new one.
                equalMarking = markingsToCheck.get(newMarking.hashCode());
                if (equalMarking == null) {
                	equalMarking = markings.get(newMarking.hashCode());
                }

                if (equalMarking == null) {
                    currentMarking.addSuccessor(new Arc(newMarking, transitions[i]));
                    newMarking.setPredecessor(currentMarking);
                    markingsToCheck.put(newMarking.hashCode(), newMarking);
                } else {
                    currentMarking.addSuccessor(new Arc(equalMarking, transitions[i]));
                }

            }
        }
        mNet.getMarkings().addAll(markings.values());
    }
}
