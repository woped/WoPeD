package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event.NodeChangedEvent;
import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event.NodeChangedEventListener;
import org.woped.qualanalysis.reachabilitygraph.data.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.tests.DemoGraphGenerator;
import org.woped.tests.MarkingGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MpNodeTest {

    private MpNode cut;
    private IMarking marking;

    @Before
    public void setup() {
        marking = new MarkingGenerator().createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});
        cut = new MpNode(marking);
    }

    @Test
    public void setState_newState_notifiesListeners() throws Exception {
        NodeChangedEventListener listener = mock(NodeChangedEventListener.class);
        ArgumentCaptor<NodeChangedEvent> argumentCaptor = ArgumentCaptor.forClass(NodeChangedEvent.class);

        cut.addNodeChangedListener(listener);

        MpNodeState previousState = cut.getState();
        MpNodeState newState = MpNodeState.ACTIVE;
        cut.setState(newState);

        verify(listener).NodeChanged(argumentCaptor.capture());
        NodeChangedEvent event = argumentCaptor.getValue();

        assertEquals("Node should be the class under test", cut, event.getNode());
        assertEquals("Previous state should be " + previousState, previousState, event.getPreviousState());
        assertEquals("new state should be " + newState, newState, event.getNewState());
    }

    @Test
    public void setState_sameState_doesNotNotifyListeners() throws Exception {
        NodeChangedEventListener listener = mock(NodeChangedEventListener.class);

        cut.addNodeChangedListener(listener);

        MpNodeState currentState = cut.getState();
        cut.setState(currentState);

        verify(listener, never()).NodeChanged((NodeChangedEvent) any());
    }

    @Test
    public void getParentNode_noParent_returnsNull() throws Exception {
        assertNull(cut.getParentNode());
    }

    @Test
    public void getParentNode_oneParent_returnsParent() throws Exception {
        ReachabilityJGraph graph = new DemoGraphGenerator().createGraph();

        MpNode parent = new MpNode(marking);
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge = new CoverabilityGraphEdge(parent, cut, t1);
        graph.getGraphLayoutCache().insert(new Object[]{parent, cut, edge});

        MpNode actual = cut.getParentNode();
        assertEquals(parent, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void getParentNode_twoParents_throwsException() throws Exception {
        ReachabilityJGraph graph = new DemoGraphGenerator().createGraph();

        MpNode parent1 = new MpNode(marking);
        MpNode parent2 = new MpNode(marking);
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        TransitionNode t2 = new TransitionNode("t2", "t2", "t2", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge1 = new CoverabilityGraphEdge(parent1, cut, t1);
        CoverabilityGraphEdge edge2 = new CoverabilityGraphEdge(parent2, cut, t2);
        graph.getGraphLayoutCache().insert(new Object[]{parent1, parent2, cut, edge1, edge2});

        cut.getParentNode();
    }
}