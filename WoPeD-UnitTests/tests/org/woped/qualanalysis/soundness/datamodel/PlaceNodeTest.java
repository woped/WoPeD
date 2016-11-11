package org.woped.qualanalysis.soundness.datamodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.tests.LowLevelPetriNetGenerator;

import java.util.Observer;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Test
    public void addSuccessorNode_newNode_updatesObservers() throws Exception {
        Observer nodeAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(nodeAddedListener);

        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        NodeAddedArgs args = new NodeAddedArgs(cut, t1, ConnectionType.OUTGOING, 1);

        cut.addSuccessorNode(t1);
        verify(nodeAddedListener).update(cut.nodeAdded(), args);
    }

    @Test
    public void addSuccessorNode_newNodeWithWeight_updatesObservers() throws Exception {
        Observer nodeAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(nodeAddedListener);

        int weight = 2;
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        NodeAddedArgs args = new NodeAddedArgs(cut, t1, ConnectionType.OUTGOING, weight);

        cut.addSuccessorNode(t1, weight);
        verify(nodeAddedListener).update(cut.nodeAdded(), args);
    }

    @Test
    public void addPredecessorNode_newNode_updatesObservers() throws Exception {

        Observer connectionAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(connectionAddedListener);

        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        NodeAddedArgs args = new NodeAddedArgs(cut, t1, ConnectionType.INCOMING, 1);
        cut.addPredecessorNode(t1);

        verify(connectionAddedListener).update(cut.nodeAdded(), args);
    }

    @Test
    public void addPredecessorNode_newNodeWithWeight_updatesObservers() throws Exception {
        Observer nodeAddedListener = mock(Observer.class);
        cut.nodeAdded().addObserver(nodeAddedListener);

        int weight = 2;
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        NodeAddedArgs args = new NodeAddedArgs(cut, t1, ConnectionType.INCOMING, weight);

        cut.addPredecessorNode(t1, weight);
        verify(nodeAddedListener).update(cut.nodeAdded(), args);
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
}