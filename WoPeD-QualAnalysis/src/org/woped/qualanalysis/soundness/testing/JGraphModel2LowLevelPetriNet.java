package org.woped.qualanalysis.soundness.testing;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.soundness.SoundnessCheckImplement;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.algorithms.basedonmarkingnet.scc.StronglyConnectedComponentTestTarjan;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

public class JGraphModel2LowLevelPetriNet {

    private IEditor editor;

    public JGraphModel2LowLevelPetriNet(IEditor editor) {
        this.editor = editor;

        // some test cases
        test();
        testTarjan();
    }

    private void test() {
        long time;
        MarkingNet mNet;
        System.out.println();

        time = System.currentTimeMillis();
        mNet = (new SoundnessCheckImplement(editor)).getMarkingNet();
        System.out.println("marking net created");
        System.out.println("time : " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();

        System.out.print("unlimited places: ");
        time = System.currentTimeMillis();
        System.out.println(AlgorithmFactory.createUnboundedPlacesTest(mNet).getUnboundedPlaces());
        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();

        System.out.print("dead transtions: ");
        time = System.currentTimeMillis();
        System.out.println(AlgorithmFactory.createDeadTransitionTest(mNet).getDeadTransitions());
        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();

        System.out.print("non live transtions: ");
        time = System.currentTimeMillis();
        System.out.println(AlgorithmFactory.createNonLiveTranstionTest(mNet).getNonLiveTransitions());
        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();
    }

    private void testTarjan() {
        long time;
        MarkingNet mNet;
        mNet = BuilderFactory.createMarkingNet(BuilderFactory.createLowLevelPetriNetWithTStarBuilder(editor)
                .getLowLevelPetriNet());
        StronglyConnectedComponentTestTarjan t = new StronglyConnectedComponentTestTarjan(mNet);

        System.out.println("strongly connected components: ");
        time = System.currentTimeMillis();
        System.out.println("petri net is sc: " + t.isStronglyConnected());
        System.out.println("count of scc: " + t.getStronglyConnectedComponents().size());
        System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println();
    }

}
