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

        p1.addPostNode(t1);
        t1.addPreNode(p1);
        t1.addPostNode(pC);

        p2.addPostNode(t2);
        t2.addPreNode(p2);
        t2.addPostNode(pC);

        pC.addPreNode(t1);
        pC.addPreNode(t2);
        pC.addPostNode(t3);
        pC.addPostNode(t4);

        t3.addPreNode(pC);
        t3.addPostNode(p3);
        p3.addPreNode(t3);

        t4.addPreNode(pC);
        t4.addPostNode(p4);
        p4.addPreNode(t4);

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

        p1.addPostNode(t1);
        t1.addPreNode(p1);
        t1.addPostNode(p3);
        p3.addPreNode(t1);

        p2.addPostNode(t2);
        t2.addPreNode(p2);
        t2.addPostNode(p3);
        p3.addPreNode(t2);

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

        p1.addPostNode(t1);
        t1.addPreNode(p1);
        t1.addPostNode(p2);
        p2.addPreNode(t1);

        p1.addPostNode(t2);
        t2.addPreNode(p1);
        t2.addPostNode(p3);
        p3.addPreNode(t2);

        return net;
    }

}
