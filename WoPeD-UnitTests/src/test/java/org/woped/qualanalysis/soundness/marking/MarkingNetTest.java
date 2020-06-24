package org.woped.qualanalysis.soundness.marking;

import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MarkingNetTest {

    public void setup() {

    }

    public void tearDown() {

    }

    @Test
    public void getActivatedTransitions_demoNet_resultHasSizeOne() throws Exception {
        LowLevelPetriNet net = createDemoLowLevelNet();
        MarkingNet cut = new MarkingNet(net);

        TransitionNode[] activatedTransitions = cut.getActivatedTransitions(cut.getInitialMarking());

        assertTrue("Demo net contains one activated transition", activatedTransitions.length == 1);
        assertTrue("The activated transition should be t1", activatedTransitions[0].getId().equals("t1"));
    }

    @Test
    public void getActivatedTransitions_DemoNetP2IsUnlimited_transitionIsActivated() throws Exception {
        LowLevelPetriNet net = createDemoLowLevelNet();
        MarkingNet cut = new MarkingNet(net);
        Marking marking = (Marking) cut.getInitialMarking();

        PlaceNode p2 = cut.getPlaces()[1];
        int ndxP2 = marking.getIndexByPlace(p2);
        marking.setPlaceUnlimited(ndxP2);

        TransitionNode[] activatedTransitions = cut.getActivatedTransitions(marking);

        assertTrue("Demo net contains 2 activated transition", activatedTransitions.length == 2);
        assertTrue("The first activated transition should be t1", activatedTransitions[0].getId().equals("t1"));
        assertTrue("The second activated transition should be t2", activatedTransitions[1].getId().equals("t2"));
    }

    @Test
    public void calculateSucceedingMarking_demoNet_producesMarking010() throws Exception {
        LowLevelPetriNet net = createDemoLowLevelNet();
        MarkingNet cut = new MarkingNet(net);

        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
        IMarking succeedingMarking = cut.calculateSucceedingMarking(cut.getInitialMarking(), t1);

        assertTrue(succeedingMarking.toString().equals("( 0 1 0 )"));
    }

    @Test
    public void calculateSucceedingMarking_demoNetWithWeight_resultUsesWeight() throws Exception {
        LowLevelPetriNet net = createDemoLowLevelNet();
        MarkingNet cut = new MarkingNet(net);

        PlaceNode p2 = net.getPlaceNode(new PlaceNode(0,0,"p2", "", ""));
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));

        int arcWeight = 2;
        t1.addSuccessorNode(p2, arcWeight);
        p2.addPredecessorNode(t1, arcWeight);

        IMarking succeedingMarking = cut.calculateSucceedingMarking(cut.getInitialMarking(), t1);
        assertTrue(succeedingMarking.toString().equals("( 0 2 0 )"));
    }

    @Test
    public void calculateSuccedingMarking_reverseArcExists_doNotChangeTokens() throws Exception {
        LowLevelPetriNet net = new LowLevelPetriNet();

        int initialTokens = 2;
        PlaceNode p1 = new PlaceNode(initialTokens, 0,"p1", "p1", "p1");
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);

        net.addNode(p1);
        net.addNode(t1);

        p1.addSuccessorNode(t1);
        p1.addPredecessorNode(t1);
        t1.addPredecessorNode(p1);
        t1.addSuccessorNode(p1);

        MarkingNet cut = new MarkingNet(net);
        IMarking succeedingMarking = cut.calculateSucceedingMarking(cut.getInitialMarking(), t1);

        String expected = String.format("( %d )", initialTokens);
        String actual = succeedingMarking.toString();
        assertEquals(expected, actual);
    }


    /**
     * Creates a low level petri net for testing purposes.
     * <p>
     * Creates net: p1 -> t1 -> p2 -> t2 -> p3
     * with marking ( 1 0 0 )
     *
     * @return a demo low level petrinet
     */
    private LowLevelPetriNet createDemoLowLevelNet() {
        LowLevelPetriNet net = new LowLevelPetriNet();

        PlaceNode p1 = new PlaceNode(1, 1, "p1", "p1", "p1");
        PlaceNode p2 = new PlaceNode(0, 0, "p2", "p2", "p2");
        PlaceNode p3 = new PlaceNode(0, 0, "p3", "p3", "p3");

        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        TransitionNode t2 = new TransitionNode("t2", "t2", "t2", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);

        net.addNode(p1);
        net.addNode(p2);
        net.addNode(p3);
        net.addNode(t1);
        net.addNode(t2);

        p1.addSuccessorNode(t1);
        t1.addSuccessorNode(p2);
        p2.addSuccessorNode(t2);
        t2.addSuccessorNode(p3);
        p3.addPredecessorNode(t2);
        t2.addPredecessorNode(p2);
        p2.addPredecessorNode(t1);
        t1.addPredecessorNode(p1);
        return net;
    }
}
