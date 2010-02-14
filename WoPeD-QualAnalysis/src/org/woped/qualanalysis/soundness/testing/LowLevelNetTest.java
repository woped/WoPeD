/**
 * 
 */
package org.woped.qualanalysis.soundness.testing;

import org.woped.qualanalysis.soundness.datamodel.AbstractNode;
import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * @author Sebastian Fuﬂ
 * 
 */
public class LowLevelNetTest {

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
        TransitionNode[] transitions = net.getTransitions();
        PlaceNode[] places = net.getPlaces();

        // System.out.println("places:");
        for (int i = 0; i < places.length; i++) {
            printNodeInformation(places[i]);
            System.out.println();
        }

        // System.out.println("transitions:");
        for (int i = 0; i < transitions.length; i++) {
            printNodeInformation(transitions[i]);
            System.out.println();
        }

    }

    /**
     * prints all predecessors and successors of node.
     * 
     * @param node node.
     */
    public static void printNodeInformation(AbstractNode node) {
        AbstractNode[] postNodes = node.getPostNodes();
        AbstractNode[] preNodes = node.getPreNodes();
        System.out.println(node);
        System.out.println(" predecessors:");
        for (int i = 0; i < preNodes.length; i++) {
            System.out.println("  " + preNodes[i]);
        }
        System.out.println(" successors:");
        for (int i = 0; i < postNodes.length; i++) {
            System.out.println("  " + postNodes[i]);
        }
    }

    public static void putOut(MarkingNet markingNet) {
        System.out.println(markingNet.placesToString());
        for (Marking marking : markingNet.getMarkings()) {
            System.out.println(marking);
        }
    }

    public LowLevelNetTest() {
        LowLevelPetriNet lolNet;
        MarkingNet markingNet;

        lolNet = createNewLowLevelPetriNet();

        markingNet = new MarkingNet(lolNet);

        putOut(markingNet);
    }

    public LowLevelPetriNet createNewLowLevelPetriNet() {
        LowLevelPetriNet lowlevelNet = new LowLevelPetriNet();

        PlaceNode p1 = lowlevelNet.getPlaceNode(new PlaceNode(2, "p1", null, null));
        PlaceNode p2 = lowlevelNet.getPlaceNode(new PlaceNode(0, "p2", null, null));
        PlaceNode p3 = lowlevelNet.getPlaceNode(new PlaceNode(0, "p3", null, null));
        PlaceNode p4 = lowlevelNet.getPlaceNode(new PlaceNode(0, "p4", null, null));
        PlaceNode p5 = lowlevelNet.getPlaceNode(new PlaceNode(0, "p5", null, null));

        TransitionNode t1 = lowlevelNet.getTransitionNode(new TransitionNode("t1", null, null));
        TransitionNode t2 = lowlevelNet.getTransitionNode(new TransitionNode("t2", null, null));
        TransitionNode t3 = lowlevelNet.getTransitionNode(new TransitionNode("t3", null, null));

        p1.addPostNode(t1);
        t1.addPostNode(p2);
        t1.addPostNode(p3);
        p2.addPostNode(t2);
        t2.addPostNode(p4);
        p3.addPostNode(t3);
        p4.addPostNode(t3);
        t3.addPostNode(p5);

        return lowlevelNet;

    }
}
