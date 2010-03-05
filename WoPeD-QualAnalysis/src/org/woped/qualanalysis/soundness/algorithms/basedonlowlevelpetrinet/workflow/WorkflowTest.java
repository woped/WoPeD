package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.workflow;

import java.util.HashSet;
import java.util.Set;

import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.AbstractLowLevelPetriNetTest;
import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * @see IWorkflowTest
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class WorkflowTest extends AbstractLowLevelPetriNetTest implements IWorkflowTest {

    /**
     * 
     * @param lolNetWithTStar LowLevelPetriNet (without t*) the algorithm is based on
     */
    public WorkflowTest(LowLevelPetriNet lolNetWithoutTStar) {
        super(lolNetWithoutTStar);
    }

    @Override
    public Set<PlaceNode> getSourcePlaces() {
        Set<PlaceNode> sourcePlaces = new HashSet<PlaceNode>();
        for (PlaceNode place : lolNet.getPlaces()) {
            if (place.getPreNodes().size() == 0) {
                sourcePlaces.add(place);
            }
        }
        return sourcePlaces;
    }

    @Override
    public Set<PlaceNode> getSinkPlaces() {
        Set<PlaceNode> sinkPlaces = new HashSet<PlaceNode>();
        for (PlaceNode place : lolNet.getPlaces()) {
            if (place.getPostNodes().size() == 0) {
                sinkPlaces.add(place);
            }
        }
        return sinkPlaces;
    }

    @Override
    public Set<TransitionNode> getSourceTransitions() {
        Set<TransitionNode> sourceTransitions = new HashSet<TransitionNode>();
        for (TransitionNode transition : lolNet.getTransitions()) {
            if (transition.getPreNodes().size() == 0) {
                sourceTransitions.add(transition);
            }
        }
        return sourceTransitions;
    }

    @Override
    public Set<TransitionNode> getSinkTransitions() {
        Set<TransitionNode> sinkTransitions = new HashSet<TransitionNode>();
        for (TransitionNode transition : lolNet.getTransitions()) {
            if (transition.getPostNodes().size() == 0) {
                sinkTransitions.add(transition);
            }
        }
        return sinkTransitions;
    }

    @Override
    public Set<AbstractNode> getNotConnectedNodes() {
        // first add all nodes to the set
        Set<AbstractNode> notConnectedNodes = new HashSet<AbstractNode>(lolNet.getTransitions());
        notConnectedNodes.addAll(lolNet.getPlaces());
        // only start, if there are any nodes in the set
        if (notConnectedNodes.size() != 0) {
            // select one node (random)
            AbstractNode node = notConnectedNodes.iterator().next();
            // start checking connections (all elements which are connected to the start element will be removed from the set)
            checkNodeConnection(node, notConnectedNodes);
        }
        // if the set is not empty, not all nodes are connected to every other (direction of arcs is ignored)
        // --> all nodes are not connected
        if (notConnectedNodes.size() != 0) {
            notConnectedNodes.addAll(lolNet.getTransitions());
            notConnectedNodes.addAll(lolNet.getPlaces());
        }
        return notConnectedNodes;
    }

    @Override
    public Set<AbstractNode> getNotStronglyConnected() {

        return new HashSet<AbstractNode>();
    }

    /**
     * 
     * @param node the current node to check connections of (will be removed from set in current recursive loop)
     * @param notConnectedNodes all nodes we have not visited yet
     */
    private void checkNodeConnection(AbstractNode node, Set<AbstractNode> notConnectedNodes) {
        // remove current node (we are visiting it now)
        if (notConnectedNodes.remove(node)) {
            // check connections of all succeeding nodes
            for (AbstractNode postNode : node.getPostNodes()) {
                checkNodeConnection(postNode, notConnectedNodes);
            }
            // check connections of all preceding nodes
            for (AbstractNode preNode : node.getPreNodes()) {
                checkNodeConnection(preNode, notConnectedNodes);
            }
        }
    }

}
