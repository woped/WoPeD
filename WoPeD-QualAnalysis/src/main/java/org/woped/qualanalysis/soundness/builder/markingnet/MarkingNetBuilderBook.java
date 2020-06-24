package org.woped.qualanalysis.soundness.builder.markingnet;

import java.util.HashMap;
import java.util.Map;

import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.Arc;
import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * creates marking net based on algorithm page 121 "Business Process and WorkflowManagement" author Cornelia Richter von Hagen, Wolfried Stucky.
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * @see AbstractMarkingNetBuilder
 */
public class MarkingNetBuilderBook extends AbstractMarkingNetBuilder {

    /**
     * @param lolNet source low level petri net.
     */
    public MarkingNetBuilderBook(ILowLevelPetriNet lolNet) {
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
    private void calculateSucceedingMarkings(IMarking marking) {
        // We use a map here to be able to find markings instances. Unfortunately, Set<> does not
        // allow us to access a matching object within the set, but this is exactly what we want
        // to do here, because a marking has additional properties that do not count into equality.
        Map<IMarking, IMarking> markingsToCheck = new HashMap<>();
        Map<IMarking, IMarking> markings = new HashMap<>();
        TransitionNode[] transitions;
        IMarking currentMarking;
        IMarking newMarking;
        IMarking compareMarking;
        IMarking equalMarking = null;

        markingsToCheck.put(marking, marking);

        while (!markingsToCheck.isEmpty()) {
            currentMarking = markingsToCheck.keySet().iterator().next();
            markingsToCheck.remove(currentMarking);
            markings.put(currentMarking, currentMarking);

            transitions = mNet.getActivatedTransitions(currentMarking);

            for (int i = 0; i < transitions.length; i++) {
                newMarking = mNet.calculateSucceedingMarking(currentMarking, transitions[i]);
                compareMarking = currentMarking;
                while (compareMarking != null && !compareMarking.lessOrEqual(newMarking)) {
                    compareMarking = compareMarking.getPredecessor();
                }
                if (compareMarking != null) {

                    for (PlaceNode place : newMarking.getPlaces()) {

                        if (newMarking.getTokens(place) > compareMarking.getTokens(place)) {
                            newMarking.setPlaceUnbound(place, true);
                        }
                    }
                }

                // Check if this marking exists either in the check queue or has been added
                // to the marking graph already. In this case, use the existing object and
                // discard the new one.
                equalMarking = markingsToCheck.get(newMarking);
                if (equalMarking == null) {
                    equalMarking = markings.get(newMarking);
                }

                if (equalMarking == null) {
                    currentMarking.addSuccessor(new Arc(newMarking, transitions[i]));
                    newMarking.setPredecessor(currentMarking);
                    markingsToCheck.put(newMarking, newMarking);
                } else {
                    currentMarking.addSuccessor(new Arc(equalMarking, transitions[i]));
                }

            }
        }
        mNet.getMarkings().addAll(markings.values());
    }
}
