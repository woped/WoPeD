package org.woped.qualanalysis.soundness.marking;

import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * this class represents a marking net
 *
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */

public class MarkingNet implements IMarkingNet {

    /**
     * source lowLevel petri net.
     */
    private final ILowLevelPetriNet lolNet;

    /**
     * all places of the source lowLevel petri net. ->static order!
     */
    private final PlaceNode[] places;

    /**
     * all transitions of the source lowLevel petri net.
     */
    private final TransitionNode[] transitions;

    /**
     * set of all markings.
     */
    private final Set<Marking> markings = new HashSet<>();

    /**
     * initial marking.
     */
    private Marking initialMarking;

    /**
     * @param lolNet the LowLevelPetriNet on that this marking net is builded
     */
    public MarkingNet(ILowLevelPetriNet lolNet) {

        this.lolNet = lolNet;
        this.places = lolNet.getPlaces().toArray(new PlaceNode[lolNet.getPlaces().size()]);
        this.transitions = lolNet.getTransitions().toArray(new TransitionNode[lolNet.getTransitions().size()]);
        initialMarking = (Marking) BuilderFactory.createCurrentMarking(this.lolNet, false);
        initialMarking.setInitial(true);
        markings.add(initialMarking);
    }

    /**
     * @param marking the marking to check transitions with
     * @return an array of transitions which are activated
     */
    public TransitionNode[] getActivatedTransitions(Marking marking) {

        List<TransitionNode> activatedTransitions = new ArrayList<>();

        for ( TransitionNode transition : transitions ) {

            if ( transitionIsActivated(marking, transition) ) {
                activatedTransitions.add(transition);
            }
        }

        return activatedTransitions.toArray(new TransitionNode[activatedTransitions.size()]);
    }

    private boolean transitionIsActivated(Marking marking, TransitionNode transition) {

        boolean activated = true;
        int[] tokens = marking.getTokens();
        boolean[] placeUnlimited = marking.getPlaceUnlimited();

        for ( AbstractNode preNode : transition.getPredecessorNodes() ) {
            Integer weight = preNode.getWeightTo(transition);
            int k = marking.getIndexByPlace((PlaceNode) preNode);
            int tokenCount = tokens[k];

            boolean notEnoughTokens = tokenCount < weight;
            boolean placeNotUnlimited = !placeUnlimited[k];

            if ( notEnoughTokens && placeNotUnlimited ) {
                activated = false;
                break;
            }
        }
        return activated;
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
        for ( PlaceNode place : this.places ) {
            line += place + ",";
        }
        return line.substring(0, line.length() - 1);
    }

    /**
     * @return the places id as string
     */
    public String placesToStringId() {
        String line = "";
        for ( PlaceNode place : this.places ) {
            line += place.getId() + ",";
        }
        return line.substring(0, Math.max(line.length() - 1, 0));
    }

    /**
     * @return the places name as string
     */
    public String placesToStringName() {
        String line = "";
        for ( PlaceNode place : this.places ) {
            line += place.getName() + ",";
        }
        return line.substring(0, Math.max(line.length() - 1, 0));
    }

    /**
     * calculate the succeeding marking for a specified Transition Node switched with a specific marking
     * 
     * @param parentMarking
     * @param transition the transition to fire
     * @return a new Marking with the tokens after the transition is fired
     */
    public Marking calculateSucceedingMarking(Marking parentMarking, TransitionNode transition) {
        int[] tokens = new int[parentMarking.getTokens().length];

        // copy tokens from given marking (call by value)
        System.arraycopy(parentMarking.getTokens(), 0, tokens, 0, tokens.length);

        // decrease all tokenCounts for prePlaces of given transition
        // and increase all tokenCounts for postPlaces of given transition
        for ( int i = 0; i < places.length; i++ ) {
            for ( AbstractNode preNode : transition.getPredecessorNodes() ) {
                if ( preNode == places[i] ) {
                    tokens[i] -= transition.getWeightFrom(preNode);
                }
            }
            for ( AbstractNode postNode : transition.getSuccessorNodes() ) {
                if ( postNode == places[i] ) {
                    tokens[i] += transition.getWeightTo(postNode);
                }
            }
        }
        return new Marking(tokens, places, parentMarking.getPlaceUnlimited());
    }

    @Override
    public Set<Marking> getAllContainedNodes() {
        return new HashSet<>(markings);
    }

}
