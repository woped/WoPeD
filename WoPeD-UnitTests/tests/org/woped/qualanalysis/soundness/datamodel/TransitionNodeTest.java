package org.woped.qualanalysis.soundness.datamodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.tests.LowLevelPetriNetGenerator;

import java.util.Observer;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransitionNodeTest {

    private TransitionNode cut;
    private LowLevelPetriNetGenerator netGenerator;

    @Before
    public void setUp() throws Exception {
        cut = new TransitionNode("cut", "Class under test", "cut", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        netGenerator = new LowLevelPetriNetGenerator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void toString_simpleTransition_returnsName() throws Exception {

        String expected = cut.getName();
        String actual = cut.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void toString_operatorTransition_returnsName() throws Exception {

        ILowLevelPetriNet net = netGenerator.createXorJoinSplitNet();
        TransitionNode cut = net.getTransitionNode(new TransitionNode("t1_op_1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));

        String expected = "(p1)t1(P_CENTER_t1)";
        String actual = cut.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void toString_xorJoinOperator_returnsExpectedValue() throws Exception {
        ILowLevelPetriNet net = netGenerator.createXorJoinNet();
        cut = net.getTransitionNode(new TransitionNode("t1_op_1", "t1", "t1", OperatorTransitionModel.XOR_JOIN_TYPE));

        String expected = "(p1)t1";
        String actual = cut.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void toString_xorSplitOperator_returnsExpectedValue() throws Exception {
        ILowLevelPetriNet net = netGenerator.createXorSplitNet();
        cut = net.getTransitionNode(new TransitionNode("t1_op_1", null, null, 0));

        String expected = "t1(p2)";
        String actual = cut.toString();

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPredecessorNode_negativeWeight_throwsException() throws Exception {
        AbstractNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        cut.addPredecessorNode(p1, -1);
    }

    @Test(expected = RuntimeException.class)
    public void addPredecessorNode_addTransition_throwsException() throws Exception {
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        cut.addPredecessorNode(t1);
    }

    @Test
    public void addPredecessorNode_newNode_updatesObservers() throws Exception {

        Observer connectionAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(connectionAddedListener);

        PlaceNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        NodeAddedArgs args = new NodeAddedArgs(cut, p1, ConnectionType.INCOMING, 1);

        cut.addPredecessorNode(p1);
        verify(connectionAddedListener).update(cut.nodeAdded(), args);
    }

    @Test

    public void addPredecessorNode_newNodeWithWeight_updatesObservers() throws Exception {

        Observer connectionAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(connectionAddedListener);

        int weight = 3;
        PlaceNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        NodeAddedArgs args = new NodeAddedArgs(cut, p1, ConnectionType.INCOMING, weight);

        cut.addPredecessorNode(p1, weight);
        verify(connectionAddedListener).update(cut.nodeAdded(), args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSuccessorNode_weightIs0_throwsException() throws Exception {
        AbstractNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        cut.addSuccessorNode(p1, 0);
    }

    @Test(expected = RuntimeException.class)
    public void addSuccessorNode_addTransition_throwsException() throws Exception {
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        cut.addSuccessorNode(t1);
    }

    @Test
    public void addSuccessorNode_newNode_updatesObservers() throws Exception {
        Observer nodeAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(nodeAddedListener);

        PlaceNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        NodeAddedArgs args = new NodeAddedArgs(cut, p1, ConnectionType.OUTGOING, 1);

        cut.addSuccessorNode(p1);
        verify(nodeAddedListener).update(cut.nodeAdded(), args);
    }

    @Test
    public void addSuccessorNode_newNodeWithWeight_updatesObservers() throws Exception {
        Observer nodeAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(nodeAddedListener);

        int weight = 4;
        PlaceNode p1 = new PlaceNode(0, 0, "p1", "p1", "p1");
        NodeAddedArgs args = new NodeAddedArgs(cut, p1, ConnectionType.OUTGOING, weight);

        cut.addSuccessorNode(p1, weight);
        verify(nodeAddedListener).update(cut.nodeAdded(), args);
    }
}