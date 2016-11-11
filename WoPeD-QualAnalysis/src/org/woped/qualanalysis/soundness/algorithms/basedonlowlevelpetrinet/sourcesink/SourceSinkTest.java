package org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink;

import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.AbstractLowLevelPetriNetTest;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

import java.util.HashSet;
import java.util.Set;

/**
 * @see ISourceSinkTest
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class SourceSinkTest extends AbstractLowLevelPetriNetTest implements ISourceSinkTest {

    /**
     *
     * @param lolNetWithoutTStar LowLevelPetriNet (without t*) the algorithm is based on
     */
    public SourceSinkTest(ILowLevelPetriNet lolNetWithoutTStar) {
        super(lolNetWithoutTStar);
    }

    @Override
    public Set<PlaceNode> getSourcePlaces() {
        Set<PlaceNode> sourcePlaces = new HashSet<PlaceNode>();
        for (PlaceNode place : lolNet.getPlaces()) {
            if ( place.getPredecessorNodes().size() == 0 ) {
                sourcePlaces.add(place);
            }
        }
        return sourcePlaces;
    }

    @Override
    public Set<PlaceNode> getSinkPlaces() {
        Set<PlaceNode> sinkPlaces = new HashSet<PlaceNode>();
        for (PlaceNode place : lolNet.getPlaces()) {
            if ( place.getSuccessorNodes().size() == 0 ) {
                sinkPlaces.add(place);
            }
        }
        return sinkPlaces;
    }

    @Override
    public Set<TransitionNode> getSourceTransitions() {
        Set<TransitionNode> sourceTransitions = new HashSet<TransitionNode>();
        for (TransitionNode transition : lolNet.getTransitions()) {
            if ( transition.getPredecessorNodes().size() == 0 ) {
                sourceTransitions.add(transition);
            }
        }
        return sourceTransitions;
    }

    @Override
    public Set<TransitionNode> getSinkTransitions() {
        Set<TransitionNode> sinkTransitions = new HashSet<TransitionNode>();
        for (TransitionNode transition : lolNet.getTransitions()) {
            if ( transition.getSuccessorNodes().size() == 0 ) {
                sinkTransitions.add(transition);
            }
        }
        return sinkTransitions;
    }

}
