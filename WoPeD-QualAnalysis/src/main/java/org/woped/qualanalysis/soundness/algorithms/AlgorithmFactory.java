package org.woped.qualanalysis.soundness.algorithms;

import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.scomponent.ISComponentTest;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.scomponent.SComponentTest;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink.ISourceSinkTest;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.sourcesink.SourceSinkTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition.DeadTransitionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition.IDeadTransitionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions.INonLiveTranstionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions.NonLiveTransitionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces.IUnboundedPlacesTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces.UnboundPlacesTest;
import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;
import org.woped.qualanalysis.soundness.algorithms.generic.cc.ConnectedComponentTestGen;
import org.woped.qualanalysis.soundness.algorithms.generic.cc.IConnectedComponentTestGen;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.IStronglyConnectedComponentTestGen;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.StronglyConnectedComponentTestGenTarjan;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

/**
 * algorithm factory class.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * @update Jascha Kanngiesser
 * @todo jascha kanngiesser
 */
public class AlgorithmFactory {

    /**
     * 
     * @param iMarkingNet without tStar.
     * @return
     */
    public static IDeadTransitionTest createDeadTransitionTest(IMarkingNet markingNet) {
        return new DeadTransitionTest(markingNet);
    }

    /**
     * 
     * @param iMarkingNet without tStar.
     * @return
     */
    public static INonLiveTranstionTest createNonLiveTranstionTest(IMarkingNet markingNet) {
        return new NonLiveTransitionTest(markingNet);
    }

    /**
     * 
     * @param iMarkingNet without tStar.
     * @return
     */
    public static IUnboundedPlacesTest createUnboundedPlacesTest(IMarkingNet markingNet) {
        return new UnboundPlacesTest(markingNet);
    }

    /**
     * 
     * @param lolNet with tStar.
     * @return
     */
    public static ISComponentTest createSComponentTest(LowLevelPetriNet lolNet) {
        return new SComponentTest(lolNet);
    }

    /**
     * 
     * @param lolNet without tStar.
     * @return
     */
    public static ISourceSinkTest createSourceSinkTest(LowLevelPetriNet lolNet) {
        return new SourceSinkTest(lolNet);
    }

    /**
     * 
     * @param <K> node type.
     * @param net node net.
     * @return test algorithm.
     */
    public static <K extends INode<K>> IStronglyConnectedComponentTestGen<K> createSccTest(INodeNet<K> net) {
        return new StronglyConnectedComponentTestGenTarjan<K>(net);

    }

    /**
     * 
     * @param <K> node type.
     * @param net node net.
     * @return test algorithm.
     */
    public static <K extends INode<K>> IConnectedComponentTestGen<K> createCcTest(INodeNet<K> net) {
        return new ConnectedComponentTestGen<K>(net);

    }
}
