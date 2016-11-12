package org.woped.qualanalysis.soundness.datamodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.tests.LowLevelPetriNetGenerator;

import static junit.framework.Assert.assertEquals;

public class PlaceNodeTest {
    private PlaceNode cut;
    private LowLevelPetriNetGenerator netGenerator;

    @Before
    public void setUp() throws Exception {
        cut = new PlaceNode(0, 0, "cut", "Class under test", "cut");
        netGenerator = new LowLevelPetriNetGenerator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void toString_defaultPlace_returnsName() throws Exception {
        String expected = cut.getName();
        String actual = cut.toString();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSuccessorNode_weightIs0_throwsException() throws Exception {
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        cut.addSuccessorNode(t1, 0);
    }

    @Test(expected = RuntimeException.class)
    public void addSuccessorNode_addPlace_throwsException() throws Exception {
        AbstractNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        cut.addSuccessorNode(p1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPredecessorNode_weightIs0_throwsException() throws Exception {
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        cut.addPredecessorNode(t1, 0);
    }

    @Test(expected = RuntimeException.class)
    public void addPredecessorNode_addPlace_throwsException() throws Exception {
        AbstractNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        cut.addPredecessorNode(p1);
    }

    @Test
    public void getWeightFrom_connectionExists_returnsWeight() throws Exception {
        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        cut = net.getPlaceNode(new PlaceNode(0, 0, "p2", null, null));
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        int expected = 1;
        int actual = cut.getWeightFrom(t1);

        assertEquals(expected, actual);
    }

    @Test
    public void getWeightFrom_connectionNotExists_returnZero() throws Exception {
        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        cut = net.getPlaceNode(new PlaceNode(0, 0, "p1", null, null));
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        int expected = 0;
        int actual = cut.getWeightFrom(t1);

        assertEquals(expected, actual);
    }

    @Test
    public void getWeightTo_connectionExists_returnsWeight() throws Exception {
        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        cut = net.getPlaceNode(new PlaceNode(0, 0, "p1", null, null));
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        int expected = 1;
        int actual = cut.getWeightTo(t1);

        assertEquals(expected, actual);
    }

    @Test
    public void getWeightTo_connectionNotExists_returnsZero() throws Exception {
        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        cut = net.getPlaceNode(new PlaceNode(0, 0, "p2", null, null));
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        int expected = 0;
        int actual = cut.getWeightTo(t1);

        assertEquals(expected, actual);
    }

    @Test
    public void getWeightFrom_connectionExists_sourceHasSameWeight() throws Exception {

        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        net.addNode(cut);
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        t1.addSuccessorNode(cut, 2);

        int expected = cut.getWeightFrom(t1);
        int actual = t1.getWeightTo(cut);

        assertEquals(expected, actual);
    }

    @Test
    public void getWeightFrom_connectionReplaced_sourceHasSameWeight() throws Exception {

        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        cut = net.getPlaceNode(new PlaceNode(0, 0, "p2", "", ""));
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        t1.addSuccessorNode(cut, 2);

        int expected = cut.getWeightFrom(t1);
        int actual = t1.getWeightTo(cut);

        assertEquals(expected, actual);
    }

    @Test
    public void getWeightTo_connectionExists_sourceHasSameWeight() throws Exception {

        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        net.addNode(cut);
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        cut.addSuccessorNode(t1, 2);

        int expected = cut.getWeightTo(t1);
        int actual = t1.getWeightFrom(cut);

        assertEquals(expected, actual);
    }

    @Test
    public void getWeightTo_connectionReplaced_sourceHasSameWeight() throws Exception {

        ILowLevelPetriNet net = netGenerator.createSimpleNet();
        cut = net.getPlaceNode(new PlaceNode(0, 0, "p2", "", ""));
        TransitionNode t1 = net.getTransitionNode(new TransitionNode("t1", null, null, 0));

        cut.addSuccessorNode(t1, 2);

        int expected = cut.getWeightTo(t1);
        int actual = t1.getWeightFrom(cut);

        assertEquals(expected, actual);
    }
}