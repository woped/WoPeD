package org.woped.qualanalysis.soundness.datamodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.tests.LowLevelPetriNetGenerator;

import static junit.framework.Assert.assertEquals;

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
}