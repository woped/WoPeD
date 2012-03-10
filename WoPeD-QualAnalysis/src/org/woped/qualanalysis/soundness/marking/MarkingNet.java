package org.woped.qualanalysis.soundness.marking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * this class represents a marking net
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */

public class MarkingNet implements IMarkingNet {

    /** source lowLevel petri net. */
    private final LowLevelPetriNet lolNet;
    /** all places of the source lowLevel petri net. ->static order! */
    private final PlaceNode[] places;
    /** all transitions of the source lowLevel petri net. */
    private final TransitionNode[] transitions;
    /** set of all markings. */
    private final Set<Marking> markings = new HashSet<Marking>();
    /** initial marking. */
    private Marking initialMarking;

    private List<Marking> markingList = new ArrayList<Marking>();

    /**
     * 
     * @param lolNet the LowLevelPetriNet on that this marking net is builded
     */
    public MarkingNet(LowLevelPetriNet lolNet) {

        this.lolNet = lolNet;
        this.places = lolNet.getPlaces().toArray(new PlaceNode[lolNet.getPlaces().size()]);
        this.transitions = lolNet.getTransitions().toArray(new TransitionNode[lolNet.getTransitions().size()]);
        initialMarking = (Marking) BuilderFactory.createCurrentMarking(this.lolNet, false);
        initialMarking.setInitial(true);
        markings.add(initialMarking);
        markingList.add(initialMarking);
    }

    /**
     * @param marking the marking to check transitions with
     * @return an array of transitions which are activated
     */
    public TransitionNode[] getActivatedTransitions(Marking marking) {
        // declaration
        Set<TransitionNode> activatedTransitions = new HashSet<TransitionNode>();
        Integer[] tokens = marking.getTokens(); // tokens of the given marking
        Boolean[] placeUnlimited = marking.getPlaceUnlimited();
        Boolean activated; // flag if transition is activated or not

        for (int i = 0; i < transitions.length; i++) {
            // transition
            activated = true; // initialize flag for current transition
            for (AbstractNode preNode : transitions[i].getPreNodes()) {
                for (int k = 0; k < places.length && activated; k++) {
                    if (preNode == places[k]) {
                        if (tokens[k] <= 0 && !placeUnlimited[k]) { // current PrePlace without token?
                            activated = false;
                        }
                    }
                }
            }
            if (activated) {
                activatedTransitions.add(transitions[i]); // add transition to
                // set if activated
            }
        }
        // return all activated transitions as an array
        return activatedTransitions.toArray(new TransitionNode[0]);
    }

    /**
     * @return the initialMarking
     */
    public Marking getInitialMarking() {
        return initialMarking;
    }

    /**
     * @return the set of markings
     */
    public Set<Marking> getMarkings() {
        return this.markings;
    }

    /**
     * @return the places as an array
     */
    public PlaceNode[] getPlaces() {
        return this.places;
    }

    /**
     * @return the transitions as an array
     */
    public TransitionNode[] getTransitions() {
        return transitions;
    }

    /**
     * @return the places name and id as string
     */
    public String placesToString() {
        String line = "";
        for (int i = 0; i < this.places.length; i++) {
            line += this.places[i] + ",";
        }
        return line.substring(0, line.length() - 1);
    }

    /**
     * @return the places id as string
     */
    public String placesToStringId() {
        String line = "";
        for (int i = 0; i < this.places.length; i++) {
            line += this.places[i].getId() + ",";
        }
        return line.substring(0, Math.max(line.length() - 1, 0));
    }

    /**
     * @return the places name as string
     */
    public String placesToStringName() {
        String line = "";
        for (int i = 0; i < this.places.length; i++) {
            line += this.places[i].getName() + ",";
        }
        return line.substring(0, Math.max(line.length() - 1, 0));
    }

    /**
     * calculate the succeeding marking for a specified Transition Node switched with a specific marking
     * 
     * @param parentMarking
     * @param transition the transition to switch
     * @return a new Marking with the tokens after the transition is switched
     */
    public Marking calculateSucceedingMarking(Marking parentMarking, TransitionNode transition) {
        Integer[] tokens = new Integer[parentMarking.getTokens().length];

        // copy tokens from given marking (call by value)
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = parentMarking.getTokens()[i];
        }

        // decrease all tokenCounts for prePlaces of given transition
        // and increase all tokenCounts for postPlaces of given transition
        for (int i = 0; i < places.length; i++) {
            for (AbstractNode preNode : transition.getPreNodes()) {
                if (preNode == places[i]) {
                    tokens[i]--;
                }
            }
            for (AbstractNode postNode : transition.getPostNodes()) {
                if (postNode == places[i]) {
                    tokens[i]++;
                }
            }
        }
        return new Marking(tokens, places, parentMarking.getPlaceUnlimited());
    }

    @Override
    public Set<Marking> getAllContainedNodes() {
        return new HashSet<Marking>(markings);
    }

}
