/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.woped.file.yawl;

import java.util.ArrayList;

import org.woped.file.yawl.wfnet.*;
import org.woped.file.yawl.wfnet.Transition.JoinSplitType;

/**
 *
 * @author Chris
 */
public class YawlToPnmlTransform {

    private WfNet wfNet;

    public YawlToPnmlTransform(WfNet wfNet) {
        this.wfNet = wfNet;
    }

    public WfNet Transform() {

        // NOTE: Don't change the sequence of the method calls.

        insertPlacesBetweenTransitions();

        transformCombinedOrSplitsAndOrJoins();
        transformOrSplits();
        transformOrJoins();

        transformAndSplits();

        transformAndJoins();

        transformAndJoinAndSplits();

        transformXorSplits();

        transformXorJoins();

        transformXorJoinXorSplits();

        transformXorJoinAndSplits();

        transformAndJoinXorSplits();



        return this.wfNet;
    }

    private void insertPlacesBetweenTransitions() {

    	ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();

        for (WfNetNode node : wfNet.getNodes()) {
            
            if (!(node instanceof Transition)) {
                continue;
            }

            Transition t = (Transition) node;
            
            for(int i=t.getOutputNodeCount()-1; i>=0; i--) {
            //for (WfNetNode output_node : t.getOutputNodes()) {
                WfNetNode output_node = t.getOutputNode(i);
                if (!(output_node instanceof Transition)) {
                    continue;
                }
                t.removeOutputNode(output_node);

                Place p = wfNet.createPlace();
                nodes_to_add.add(p);
                t.copyAttributesTo(p, false);

                p.addInputNode(t);
                p.addOutputNode(output_node);
            }
        }

        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    private void transformCombinedOrSplitsAndOrJoins() {
        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            // make sure it has at least one OR split or join
            if (t_old.getJoinType() != Transition.JoinSplitType.Or
                    && t_old.getSplitType() != Transition.JoinSplitType.Or) {
                continue;
            }
            // make sure it is a combined join/split transition
            if (t_old.getJoinType() == Transition.JoinSplitType.None
                    || t_old.getSplitType() == Transition.JoinSplitType.None) {
                continue;
            }
            
            nodes_to_remove.add(t_old);

            Transition t_new_1 = wfNet.createTransition();
            nodes_to_add.add(t_new_1);
            t_old.copyAttributesTo(t_new_1, false);
            t_new_1.setSplitType(JoinSplitType.None);
            for (WfNetNode input_node : t_old.getInputNodes()) {
                t_new_1.addInputNode(input_node);
            }
            
            Place p_new = wfNet.createPlace();
            nodes_to_add.add(p_new);
            t_old.copyAttributesTo(p_new, false);
            p_new.addInputNode(t_new_1);
            
            Transition t_new_2 = wfNet.createTransition();
            nodes_to_add.add(t_new_2);
            t_old.copyAttributesTo(t_new_2, false);
            t_new_2.setJoinType(JoinSplitType.None);
            for (WfNetNode output_node : t_old.getOutputNodes()) {
                t_new_2.addOutputNode(output_node);
            }
            t_new_2.addInputNode(p_new);
        }
        
        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }
    }

    private void transformXorSplits() {

        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            if (t_old.getJoinType() != Transition.JoinSplitType.None
                    || t_old.getSplitType() != Transition.JoinSplitType.Xor) {
                continue;
            }

            String old_id = t_old.getId();
            int op_id = 0;

            String operatorCode = Transition.getWoPedOperatorCode(
                    t_old.getJoinType(), t_old.getSplitType());

            for (WfNetNode output_node : t_old.getOutputNodes()) {
                op_id++;

                // Generate a new Transition for each of the output
                // nodes and connect the two

                Transition t_new = wfNet.createTransition(old_id + "_op_" + op_id);
                nodes_to_add.add(t_new);

                // Woped-PNML-related attributes
                t_new.setWopedOperatorId(old_id);
                t_new.setWopedOperatorCode(operatorCode);
                t_new.setWopedOrientation("1");

                // Copy attributes like name, layout data, join/split type, but not
                // the input and output node references
                t_old.copyAttributesTo(t_new, false);


                t_new.addOutputNode(output_node);

                // Link each of the input nodes of the existing transition
                // (legally there should be only one since it's got no join type)
                // to each of the newly generated transitions
                for (WfNetNode in : t_old.getInputNodes()) {
                    in.addOutputNode(t_new);
                }

            }

            // remove the old transition after finishing 
            // iterating through the nodes
            nodes_to_remove.add(t_old);
        }

        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    private void transformXorJoins() {

        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();


        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            if (t_old.getJoinType() != Transition.JoinSplitType.Xor
                    || t_old.getSplitType() != Transition.JoinSplitType.None) {
                continue;
            }

            String old_id = t_old.getId();
            int op_id = 0;

            String operatorCode = Transition.getWoPedOperatorCode(
                    t_old.getJoinType(), t_old.getSplitType());

            for (WfNetNode input_node : t_old.getInputNodes()) {
                op_id++;

                // Generate a new Transition for each of the input
                // nodes and connect the two

                Transition t_new = wfNet.createTransition(
                        old_id + "_op_" + op_id);
                nodes_to_add.add(t_new);


                // Woped-PNML-related attributes
                t_new.setWopedOperatorId(old_id);
                t_new.setWopedOperatorCode(operatorCode);
                t_new.setWopedOrientation("3");

                // Copy attributes like name, layout data, join/split type, but not
                // the input and output node references
                t_old.copyAttributesTo(t_new, false);

                t_new.addInputNode(input_node);

                // Link each of the output nodes of the existing transition
                // (legally there should be only one since it's got no split type)
                // to each of the newly generated transitions
                for (WfNetNode on : t_old.getOutputNodes()) {
                    on.addInputNode(t_new);
                }

            }

            // remove the old transition after finishing 
            // iterating through the nodes
            nodes_to_remove.add(t_old);
        }


        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    private void transformXorJoinXorSplits() {

        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor join/split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            if (t_old.getJoinType() != Transition.JoinSplitType.Xor
                    || t_old.getSplitType() != Transition.JoinSplitType.Xor) {
                continue;
            }

            String old_id = t_old.getId();
            int op_id = 0;


            String operatorCode = Transition.getWoPedOperatorCode(
                    t_old.getJoinType(), t_old.getSplitType());
            // Create the P_CENTER_... place
            Place p_center = wfNet.createPlace("P_CENTER_" + old_id);

            // These attribtes will be included in the "toospecific" part of
            // the WoPeD-PNML-XML-code
            p_center.setWopedOperatorId(old_id);
            p_center.setWopedOperatorCode(operatorCode);

            nodes_to_add.add(p_center);

            for (WfNetNode output_node : t_old.getOutputNodes()) {
                op_id++;

                // Generate a new Transition for each of the output
                // nodes and connect the two

                Transition t_new = wfNet.createTransition(old_id + "_op_" + op_id);
                nodes_to_add.add(t_new);

                // Woped-PNML-related attributes
                t_new.setWopedOperatorId(old_id);
                t_new.setWopedOperatorCode(operatorCode);
                t_new.setWopedOrientation("3");

                // Copy attributes like name, layout data, join/split type, but not
                // the input and output node references
                t_old.copyAttributesTo(t_new, false);


                t_new.addOutputNode(output_node);

                // Link the new center place to the new nodes
                p_center.addOutputNode(t_new);

            }

            for (WfNetNode input_node : t_old.getInputNodes()) {
                op_id++;

                // Generate a new Transition for each of the input
                // nodes and connect the two

                Transition t_new = wfNet.createTransition(
                        old_id + "_op_" + op_id);
                nodes_to_add.add(t_new);

                // Woped-PNML-related attributes
                t_new.setWopedOperatorId(old_id);
                t_new.setWopedOperatorCode(operatorCode);
                t_new.setWopedOrientation("3");

                // Copy attributes like name, layout data, join/split type, but not
                // the input and output node references
                t_old.copyAttributesTo(t_new, false);

                t_new.addInputNode(input_node);

                // Link the input nodes to the center place
                p_center.addInputNode(t_new);

            }

            // remove the old transition after finishing 
            // iterating through the nodes
            nodes_to_remove.add(t_old);
        }

        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    private void transformAndSplits() {
        ArrayList<Transition> nodes_to_modify = new ArrayList<Transition>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition trans = (Transition) node;
            if (trans.getJoinType() != Transition.JoinSplitType.None
                    || trans.getSplitType() != Transition.JoinSplitType.And) {
                continue;
            }

            nodes_to_modify.add(trans);
        }

        for (Transition node : nodes_to_modify) {
            // Woped-PNML-related attributes
            node.setWopedOperatorId(node.getId());
            node.setWopedOperatorCode(
                    Transition.getWoPedOperatorCode(
                    JoinSplitType.None, JoinSplitType.And));
            node.setWopedOrientation("1");

            // modify node id
            wfNet.setNodeId(node, node.getId() + "_op_1");
        }
    }

    private void transformAndJoins() {
        ArrayList<Transition> nodes_to_modify = new ArrayList<Transition>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition trans = (Transition) node;
            if (trans.getJoinType() != Transition.JoinSplitType.And
                    || trans.getSplitType() != Transition.JoinSplitType.None) {
                continue;
            }

            nodes_to_modify.add(trans);
        }

        for (Transition node : nodes_to_modify) {
            // Woped-PNML-related attributes
            node.setWopedOperatorId(node.getId());
            node.setWopedOperatorCode(
                    Transition.getWoPedOperatorCode(
                    JoinSplitType.And, JoinSplitType.None));
            node.setWopedOrientation("3");

            wfNet.setNodeId(node, node.getId() + "_op_1");
            node.setWopedOrientation("3");

        }
    }

    private void transformAndJoinAndSplits() {
        ArrayList<Transition> nodes_to_modify = new ArrayList<Transition>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition trans = (Transition) node;
            if (trans.getJoinType() != Transition.JoinSplitType.And
                    || trans.getSplitType() != Transition.JoinSplitType.And) {
                continue;
            }

            nodes_to_modify.add(trans);
        }

        for (Transition node : nodes_to_modify) {
            // Woped-PNML-related attributes
            node.setWopedOperatorId(node.getId());
            node.setWopedOperatorCode(
                    Transition.getWoPedOperatorCode(
                    JoinSplitType.And, JoinSplitType.And));
            node.setWopedOrientation("3");

            // modify node id
            wfNet.setNodeId(node, node.getId() + "_op_1");
        }
    }

    private void transformXorJoinAndSplits() {

        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();


        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            if (t_old.getJoinType() != Transition.JoinSplitType.Xor
                    || t_old.getSplitType() != Transition.JoinSplitType.And) {
                continue;
            }

            String old_id = t_old.getId();
            int op_id = 0;

            String operatorCode = Transition.getWoPedOperatorCode(
                    t_old.getJoinType(), t_old.getSplitType());

            for (WfNetNode input_node : t_old.getInputNodes()) {
                op_id++;

                // Generate a new Transition for each of the input
                // nodes and connect the two

                Transition t_new = wfNet.createTransition(
                        old_id + "_op_" + op_id);
                nodes_to_add.add(t_new);


                // Woped-PNML-related attributes
                t_new.setWopedOperatorId(old_id);
                t_new.setWopedOperatorCode(operatorCode);
                t_new.setWopedOrientation("3");

                // Copy attributes like name, layout data, join/split type, but not
                // the input and output node references
                t_old.copyAttributesTo(t_new, false);

                t_new.addInputNode(input_node);

                // Link each of the output nodes of the existing transition
                for (WfNetNode on : t_old.getOutputNodes()) {
                    on.addInputNode(t_new);
                }

            }

            // remove the old transition after finishing 
            // iterating through the nodes
            nodes_to_remove.add(t_old);
        }


        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    private void transformAndJoinXorSplits() {

        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an Xor split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            if (t_old.getJoinType() != Transition.JoinSplitType.And
                    || t_old.getSplitType() != Transition.JoinSplitType.Xor) {
                continue;
            }

            String old_id = t_old.getId();
            int op_id = 0;

            String operatorCode = Transition.getWoPedOperatorCode(
                    t_old.getJoinType(), t_old.getSplitType());

            for (WfNetNode output_node : t_old.getOutputNodes()) {
                op_id++;

                // Generate a new Transition for each of the output
                // nodes and connect the two

                Transition t_new = wfNet.createTransition(old_id + "_op_" + op_id);
                nodes_to_add.add(t_new);

                // Woped-PNML-related attributes
                t_new.setWopedOperatorId(old_id);
                t_new.setWopedOperatorCode(operatorCode);
                t_new.setWopedOrientation("1");

                // Copy attributes like name, layout data, join/split type, but not
                // the input and output node references
                t_old.copyAttributesTo(t_new, false);


                t_new.addOutputNode(output_node);

                // Link each of the input nodes of the existing transition
                // (legally there should be only one since it's got no join type)
                // to each of the newly generated transitions
                for (WfNetNode in : t_old.getInputNodes()) {
                    in.addOutputNode(t_new);
                }

            }

            // remove the old transition after finishing 
            // iterating through the nodes
            nodes_to_remove.add(t_old);
        }

        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    private void transformOrSplits() {

        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an OR split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            if (t_old.getJoinType() != Transition.JoinSplitType.None
                    || t_old.getSplitType() != Transition.JoinSplitType.Or) {
                continue;
            }

            // Generate an xor transition node that will act as an
            // entry point into our OR split construct. 
            // IMPORTANT: the transformXorSplits() call will have to
            // happen AFTER this method
            Transition xor_trans = wfNet.createTransition();
            nodes_to_add.add(xor_trans);
            t_old.copyAttributesTo(xor_trans, false);
            xor_trans.setSplitType(JoinSplitType.Xor);
            // link each input node of the old Transition to the 
            // newly generated Transition
            for (WfNetNode input_node : t_old.getInputNodes()) {
                xor_trans.addInputNode(input_node);
            }

            int output_count = t_old.getOutputNodeCount();

            // WoPeD does not know OR splits. In order to properly implement
            // one we have to create a transition for each possible combination
            // of output places. If there are n output places the number
            // of generated transitions will be (2^n - 1). Example: if there are
            // three output places p1, p2 and p3 there will be seven transitions
            // linked to the output places as follows:
            // (p1), (p2), (p3), (p1 p2), (p1 p3), (p2 p3), (p1 p2 p3)
            for (int pcount = 1; pcount <= output_count; pcount++) {
                // create a new pointer array and initialize it
                int[] pointers = new int[pcount];
                for (int ip = 0; ip < pointers.length; ip++) {
                    pointers[ip] = ip;
                }

                do {
                    // Create a new Transition and link it to the output
                    // places that are being referred to in the pointer array
                    Transition t_new = wfNet.createTransition();
                    t_old.copyAttributesTo(t_new, false);
                    nodes_to_add.add(t_new);
                    t_new.setSplitType(JoinSplitType.And);
                    for (int ip = 0; ip < pointers.length; ip++) {
                        t_new.addOutputNode(t_old.getOutputNode(
                                pointers[ip]));
                    }

                    // Create a new place, link it to the entry XOR split
                    // and set it as an input place for the new transition
                    Place p_new = wfNet.createPlace();
                    nodes_to_add.add(p_new);
                    t_old.copyAttributesTo(p_new, false);
                    p_new.addInputNode(xor_trans);
                    p_new.addOutputNode(t_new);

                } while (UpdatePointers(pointers, output_count));

            }

            // remove the old transition after finishing 
            // iterating through the nodes
            nodes_to_remove.add(t_old);
        }

        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    private void transformOrJoins() {

        ArrayList<WfNetNode> nodes_to_add = new ArrayList<WfNetNode>();
        ArrayList<WfNetNode> nodes_to_remove = new ArrayList<WfNetNode>();

        for (WfNetNode node : wfNet.getNodes()) {

            // Make sure the node is a Transition and is an OR split
            if (!(node instanceof Transition)) {
                continue;
            }
            Transition t_old = (Transition) node;
            if (t_old.getJoinType() != Transition.JoinSplitType.Or
                    || t_old.getSplitType() != Transition.JoinSplitType.None) {
                continue;
            }

            // Generate an xor transition node that will act as an
            // exit point into our OR join construct. 
            // IMPORTANT: the transformXorJoins() call will have to
            // happen AFTER this method
            Transition xor_trans = wfNet.createTransition();
            nodes_to_add.add(xor_trans);
            t_old.copyAttributesTo(xor_trans, false);
            xor_trans.setJoinType(JoinSplitType.Xor);
            // link each output node of the old Transition to the 
            // newly generated Transition
            for (WfNetNode output_node : t_old.getOutputNodes()) {
                xor_trans.addOutputNode(output_node);
            }

            int input_count = t_old.getInputNodeCount();

            // WoPeD does not know OR splits. In order to properly implement
            // one we have to create a transition for each possible combination
            // of output places. If there are n output places the number
            // of generated transitions will be (2^n - 1). Example: if there are
            // three output places p1, p2 and p3 there will be seven transitions
            // linked to the output places as follows:
            // (p1), (p2), (p3), (p1 p2), (p1 p3), (p2 p3), (p1 p2 p3)
            for (int pcount = 1; pcount <= input_count; pcount++) {
                // create a new pointer array and initialize it
                int[] pointers = new int[pcount];
                for (int ip = 0; ip < pointers.length; ip++) {
                    pointers[ip] = ip;
                }

                do {
                    // Create a new Transition and link it to the input
                    // places that are being referred to in the pointer array
                    Transition t_new = wfNet.createTransition();
                    t_old.copyAttributesTo(t_new, false);
                    nodes_to_add.add(t_new);
                    t_new.setJoinType(JoinSplitType.And);
                    for (int ip = 0; ip < pointers.length; ip++) {
                        t_new.addInputNode(t_old.getInputNode(
                                pointers[ip]));
                    }

                    // Create a new place, link it to the XOR split
                    // and set it as an output place for the new transition
                    Place p_new = wfNet.createPlace();
                    nodes_to_add.add(p_new);
                    t_old.copyAttributesTo(p_new, false);
                    p_new.addOutputNode(xor_trans);
                    p_new.addInputNode(t_new);

                } while (UpdatePointers(pointers, input_count));

            }

            // remove the old transition after finishing 
            // iterating through the nodes
            nodes_to_remove.add(t_old);
        }

        for (WfNetNode node : nodes_to_remove) {
            wfNet.removeNode(node);
        }
        for (WfNetNode node : nodes_to_add) {
            wfNet.addNode(node);
        }

    }

    /**
     * Updates an array of indexes (or pointers) into another (target) array or
     * list that is equal or larger in size (see 'target_size' parameter).
     * Initially, the pointer array must be initialized with pointers[i] = i.
     * Subsequent calls update the pointer array so that all combinations of
     * pointers into the target list/array are being set. When no additional
     * combinations are available, the function returns 'false'.
     *
     * @param pointers
     * @param target_size
     * @return
     */
    private boolean UpdatePointers(int[] pointers, int target_size) {
        for (int i = pointers.length - 1; i >= 0; i--) {
            int max_index = target_size + (i - pointers.length + 1) - 1;
            if (pointers[i] < max_index) {
                pointers[i]++;
                for (int j = i + 1; j < pointers.length; j++) {
                    pointers[j] = pointers[j - 1] + 1;
                }
                return true;
            }
        }
        return false;
    }
}
