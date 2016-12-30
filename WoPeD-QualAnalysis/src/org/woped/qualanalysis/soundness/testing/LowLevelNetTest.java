/**
 * 
 */
package org.woped.qualanalysis.soundness.testing;

import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * @author Sebastian Fu�
 * 
 */
public class LowLevelNetTest {

    public LowLevelNetTest() {
        LowLevelPetriNet lolNet;
        IMarkingNet markingNet;

        lolNet = createNewLowLevelPetriNet();

        markingNet = new MarkingNet(lolNet);

        putOut(markingNet);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new LowLevelNetTest();
    }

    /**
     * prints predecessor and successors of each node in net.
     *
     * @param net
     */
    public static void printLowLevelNetInformation(LowLevelPetriNet net) {

        // System.out.println("places:");
        for (PlaceNode place : net.getPlaces()) {
            printNodeInformation(place);
            System.out.println();
        }

        // System.out.println("transitions:");
        for (TransitionNode transition : net.getTransitions()) {
            printNodeInformation(transition);
            System.out.println();
        }

    }

    /**
     * prints all predecessors and successors of node.
     *
     * @param node node.
     */
    public static void printNodeInformation(AbstractNode node) {
        System.out.println(node);
        System.out.println(" predecessors:");
        for ( AbstractNode preNode : node.getPredecessorNodes() ) {
            System.out.println("  " + preNode);
        }
        System.out.println(" successors:");
        for ( AbstractNode postNode : node.getSuccessorNodes() ) {
            System.out.println("  " + postNode);
        }
    }

    public static void putOut(IMarkingNet markingNet) {
        System.out.println(markingNet.placesToString());
        for (IMarking marking : markingNet.getMarkings()) {
            System.out.println(marking);
        }
    }

    public LowLevelPetriNet createNewLowLevelPetriNet() {
        LowLevelPetriNet lowlevelNet = new LowLevelPetriNet();

        PlaceNode p1 = lowlevelNet.getPlaceNode(new PlaceNode(2, 2, "p1", null, null));
        PlaceNode p2 = lowlevelNet.getPlaceNode(new PlaceNode(0, 0, "p2", null, null));
        PlaceNode p3 = lowlevelNet.getPlaceNode(new PlaceNode(0, 0, "p3", null, null));
        PlaceNode p4 = lowlevelNet.getPlaceNode(new PlaceNode(0, 0, "p4", null, null));
        PlaceNode p5 = lowlevelNet.getPlaceNode(new PlaceNode(0, 0, "p5", null, null));

        TransitionNode t1 = lowlevelNet.getTransitionNode(new TransitionNode("t1", null, null,0));
        TransitionNode t2 = lowlevelNet.getTransitionNode(new TransitionNode("t2", null, null,0));
        TransitionNode t3 = lowlevelNet.getTransitionNode(new TransitionNode("t3", null, null,0));

        p1.addSuccessorNode(t1);
        t1.addSuccessorNode(p2);
        t1.addSuccessorNode(p3);
        p2.addSuccessorNode(t2);
        t2.addSuccessorNode(p4);
        p3.addSuccessorNode(t3);
        p4.addSuccessorNode(t3);
        t3.addSuccessorNode(p5);

        return lowlevelNet;

    }
}
