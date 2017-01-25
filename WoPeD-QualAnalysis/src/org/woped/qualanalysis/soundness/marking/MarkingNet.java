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
    private final Set<IMarking> markings = new HashSet<>();

    /**
     * initial marking.
     */
    private Marking initialMarking;

    /**
     * @param lolNet the LowLevelPetriNet on that this marking net is built
     */
    public MarkingNet(ILowLevelPetriNet lolNet) {

        this.places = lolNet.getPlaces().toArray(new PlaceNode[lolNet.getPlaces().size()]);
        this.transitions = lolNet.getTransitions().toArray(new TransitionNode[lolNet.getTransitions().size()]);
        initialMarking = (Marking) BuilderFactory.createCurrentMarking(lolNet, false);
        initialMarking.setInitial(true);
        markings.add(initialMarking);
    }

    /**
     * @param marking the marking to check transitions with
     * @return an array of transitions which are activated
     */
    public TransitionNode[] getActivatedTransitions(IMarking marking) {

        List<TransitionNode> activatedTransitions = new ArrayList<>();

        for ( TransitionNode transition : transitions ) {

            if ( transitionIsActivated(marking, transition) ) {
                activatedTransitions.add(transition);
            }
        }

        return activatedTransitions.toArray(new TransitionNode[activatedTransitions.size()]);
    }

    private boolean transitionIsActivated(IMarking marking, TransitionNode transition) {

        boolean activated = true;

        for ( AbstractNode preNode : transition.getPredecessorNodes() ) {
            if(!(preNode instanceof PlaceNode)) continue;
            PlaceNode place = (PlaceNode) preNode;

            Integer weight = preNode.getWeightTo(transition);
            int tokenCount = marking.getTokens(place);

            boolean notEnoughTokens = tokenCount < weight;
            boolean placeNotUnlimited = !marking.isPlaceUnbound(place);

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
    public IMarking getInitialMarking() {
        return initialMarking;
    }

    /**
     * @return the set of markings
     */
    public Set<IMarking> getMarkings() {
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
     * @param parentMarking the marking before the transition is fired
     * @param transition the transition to fire
     * @return the marking after the transition has fired
     */
    public IMarking calculateSucceedingMarking(IMarking parentMarking, TransitionNode transition) {
        IMarking resultingMarking = parentMarking.copy();

        for(AbstractNode node: transition.getPredecessorNodes()) {
            if(!(node instanceof PlaceNode)) continue;

            PlaceNode place = (PlaceNode) node;

            int tokens = resultingMarking.getTokens(place);
            int weight = transition.getWeightFrom(place);
            resultingMarking.setTokens(place, tokens - weight);
        }

        for(AbstractNode node: transition.getSuccessorNodes()){
            if(!(node instanceof PlaceNode)) continue;

            PlaceNode place = (PlaceNode) node;
            int tokens = resultingMarking.getTokens(place);
            int weight = transition.getWeightTo(place);

            resultingMarking.setTokens(place, tokens + weight);
        }

        return resultingMarking;
    }

    @Override
    public Set<IMarking> getAllContainedNodes() {
        return new HashSet<>(markings);
    }

}
