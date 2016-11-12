package org.woped.tests;

import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

public class LowLevelPetriNetGenerator {

    /**
     * Creates a low level petri net for testing purposes.
     * <p>
     * The generated net is the low level representation of a simple transition with 1 input and 1 output places.
     * It consists of 2 places (p1, p2) and 1 transitions (t1) with the marking (1 0 ) (p1 p2)
     *
     * @return a low level petri net for testing
     */
    public ILowLevelPetriNet createSimpleNet() {
        ILowLevelPetriNet net = new LowLevelPetriNet();

        PlaceNode p1 = new PlaceNode(1, 0, "p1", "p1", "p1");
        PlaceNode p2 = new PlaceNode(0, 0, "p2", "p2", "p2");

        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", OperatorTransitionModel.TRANS_SIMPLE_TYPE);

        net.addNode(p1);
        net.addNode(p2);
        net.addNode(t1);

        p1.addSuccessorNode(t1);
        t1.addPredecessorNode(p1);
        t1.addSuccessorNode(p2);
        p2.addPredecessorNode(t1);

        return net;
    }

    /**
     * Creates a low level petri net for testing purposes.
     * <p>
     * The generated net is the low level representation of a xor join split operator with 2 input and 2 output places.
     * It consists of 5 places (p1-p4, P_CENTER_t1) and 4 transitions (t1_op_1 - t1_op_4) with the marking (1 0 0 0 0) (p1 p2 pCenter p3 p4)
     *
     * @return a low level petri net for testing
     */
    public ILowLevelPetriNet createXorJoinSplitNet() {
        ILowLevelPetriNet net = new LowLevelPetriNet();

        PlaceNode p1 = new PlaceNode(1, 0, "p1", "p1", "p1");
        PlaceNode p2 = new PlaceNode(0, 0, "p2", "p2", "p2");
        PlaceNode p3 = new PlaceNode(0, 0, "p3", "p3", "p3");
        PlaceNode p4 = new PlaceNode(0, 0, "p4", "p4", "p4");
        PlaceNode pC = new PlaceNode(0, 0, "P_CENTER_t1", "t1", "t1");

        TransitionNode t1 = new TransitionNode("t1_op_1", "t1", "t1", OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        TransitionNode t2 = new TransitionNode("t1_op_2", "t1", "t1", OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        TransitionNode t3 = new TransitionNode("t1_op_3", "t1", "t1", OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        TransitionNode t4 = new TransitionNode("t1_op_4", "t1", "t1", OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);

        net.addNode(p1);
        net.addNode(p2);
        net.addNode(p3);
        net.addNode(p4);
        net.addNode(pC);
        net.addNode(t1);
        net.addNode(t2);
        net.addNode(t3);
        net.addNode(t4);

        p1.addSuccessorNode(t1);
        t1.addPredecessorNode(p1);
        t1.addSuccessorNode(pC);

        p2.addSuccessorNode(t2);
        t2.addPredecessorNode(p2);
        t2.addSuccessorNode(pC);

        pC.addPredecessorNode(t1);
        pC.addPredecessorNode(t2);
        pC.addSuccessorNode(t3);
        pC.addSuccessorNode(t4);

        t3.addPredecessorNode(pC);
        t3.addSuccessorNode(p3);
        p3.addPredecessorNode(t3);

        t4.addPredecessorNode(pC);
        t4.addSuccessorNode(p4);
        p4.addPredecessorNode(t4);

        return net;
    }

    /**
     * Creates a low level petri net for testing purposes.
     * <p>
     * The generated net is the low level representation of a xor join operator with 2 input and 1 output places.
     * It consists of 3 places (p1-p3) and 2 transitions (t1_op_1 - t1_op_2) with the marking (1 0 0) (p1 p2 p3)
     *
     * @return a low level petri net for testing
     */
    public ILowLevelPetriNet createXorJoinNet() {
        ILowLevelPetriNet net = new LowLevelPetriNet();

        PlaceNode p1 = new PlaceNode(1, 0, "p1", "p1", "p1");
        PlaceNode p2 = new PlaceNode(0, 0, "p2", "p2", "p2");
        PlaceNode p3 = new PlaceNode(0, 0, "p3", "p3", "p3");

        TransitionNode t1 = new TransitionNode("t1_op_1", "t1", "t1", OperatorTransitionModel.XOR_JOIN_TYPE);
        TransitionNode t2 = new TransitionNode("t1_op_2", "t1", "t1", OperatorTransitionModel.XOR_JOIN_TYPE);

        net.addNode(p1);
        net.addNode(p2);
        net.addNode(p3);
        net.addNode(t1);
        net.addNode(t2);

        p1.addSuccessorNode(t1);
        t1.addPredecessorNode(p1);
        t1.addSuccessorNode(p3);
        p3.addPredecessorNode(t1);

        p2.addSuccessorNode(t2);
        t2.addPredecessorNode(p2);
        t2.addSuccessorNode(p3);
        p3.addPredecessorNode(t2);

        return net;
    }

    /**
     * Creates a low level petri net for testing purposes.
     * <p>
     * The generated net is the low level representation of a xor split operator with 1 input and 2 output places.
     * It consists of 3 places (p1-p3) and 2 transitions (t1_op_1 - t1_op_2) with the marking (1 0 0) (p1 p2 p3)
     *
     * @return a low level petri net for testing
     */
    public ILowLevelPetriNet createXorSplitNet() {
        ILowLevelPetriNet net = new LowLevelPetriNet();

        PlaceNode p1 = new PlaceNode(1, 0, "p1", "p1", "p1");
        PlaceNode p2 = new PlaceNode(0, 0, "p2", "p2", "p2");
        PlaceNode p3 = new PlaceNode(0, 0, "p3", "p3", "p3");

        TransitionNode t1 = new TransitionNode("t1_op_1", "t1", "t1", OperatorTransitionModel.XOR_SPLIT_TYPE);
        TransitionNode t2 = new TransitionNode("t1_op_2", "t1", "t1", OperatorTransitionModel.XOR_SPLIT_TYPE);

        net.addNode(p1);
        net.addNode(p2);
        net.addNode(p3);
        net.addNode(t1);
        net.addNode(t2);

        p1.addSuccessorNode(t1);
        t1.addPredecessorNode(p1);
        t1.addSuccessorNode(p2);
        p2.addPredecessorNode(t1);

        p1.addSuccessorNode(t2);
        t2.addPredecessorNode(p1);
        t2.addSuccessorNode(p3);
        p3.addPredecessorNode(t2);

        return net;
    }

}
