package org.woped.qualanalysis.soundness.builder.markingnet;

import java.util.HashSet;
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
        Set<Marking> markingsToCheck = new HashSet<Marking>();
        Set<Marking> markings = new HashSet<Marking>();
        TransitionNode[] transitions;
        Marking currentMarking;
        Marking newMarking;
        Marking compareMarking;
        Marking equalMarking = null;

        markingsToCheck.add(marking);

        while (!markingsToCheck.isEmpty()) {
            currentMarking = markingsToCheck.iterator().next();
            markingsToCheck.remove(currentMarking);
            markings.add(currentMarking);

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

                // TODO(aeckleder): This is highly inefficient.
                Set<Marking> allMarkings = new HashSet<Marking>();
                allMarkings.addAll(markings);
                allMarkings.addAll(markingsToCheck);
                equalMarking = null;
                for (Marking m : allMarkings) {
                    if (m.equals(newMarking)) {
                        equalMarking = m;
                    }
                }

                if (equalMarking == null) {
                    currentMarking.addSuccessor(new Arc(newMarking, transitions[i]));
                    newMarking.setPredecessor(currentMarking);
                    markingsToCheck.add(newMarking);
                } else {
                    currentMarking.addSuccessor(new Arc(equalMarking, transitions[i]));
                }

            }
        }
        mNet.getMarkings().addAll(markings);
    }
}
