package org.woped.qualanalysis.soundness.algorithms;

import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.scomponent.ISComponentTest;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.scomponent.SComponentTest;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.workflow.IWorkflowTest;
import org.woped.qualanalysis.soundness.algorithms.basedonlowlevelpetrinet.workflow.WorkflowTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition.DeadTransitionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.deadtransition.IDeadTransitionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions.INonLiveTranstionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.nonliveTransitions.NonLiveTransitionTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces.IUnboundedPlacesTest;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.unboundplaces.UnboundPlacesTest;
import org.woped.qualanalysis.soundness.algorithms.generic.INode;
import org.woped.qualanalysis.soundness.algorithms.generic.INodeNet;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.IStronglyConnectedComponentTestGen;
import org.woped.qualanalysis.soundness.algorithms.generic.scc.StronglyConnectedComponentTestGenTarjan;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * algorithm factory class.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 */
public class AlgorithmFactory {

    /**
     * 
     * @param mNet without tStar.
     * @return
     */
    public static IDeadTransitionTest createDeadTransitionTest(MarkingNet mNet) {
        return new DeadTransitionTest(mNet);
    }

    /**
     * 
     * @param mNet without tStar.
     * @return
     */
    public static INonLiveTranstionTest createNonLiveTranstionTest(MarkingNet mNet) {
        return new NonLiveTransitionTest(mNet);
    }

    /**
     * 
     * @param mNet without tStar.
     * @return
     */
    public static IUnboundedPlacesTest createUnboundedPlacesTest(MarkingNet mNet) {
        return new UnboundPlacesTest(mNet);
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
    public static IWorkflowTest createWorkflowTest(LowLevelPetriNet lolNet) {
        return new WorkflowTest(lolNet);
    }

    public static <K extends INode<K>> IStronglyConnectedComponentTestGen<K> createSccTest(INodeNet<K> net) {
        return new StronglyConnectedComponentTestGenTarjan<K>(net);

    }
}
