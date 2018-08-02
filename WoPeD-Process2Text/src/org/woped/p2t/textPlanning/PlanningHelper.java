package org.woped.p2t.textPlanning;

import org.woped.p2t.dataModel.process.EventType;
import org.woped.p2t.dataModel.process.ProcessModel;
import de.hpi.bpt.graph.algo.rpst.RPST;
import de.hpi.bpt.graph.algo.rpst.RPSTNode;
import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Event;
import de.hpi.bpt.process.Gateway;
import de.hpi.bpt.process.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PlanningHelper {
    /**
     * Creates an order for the top level of a given RPST Tree.
     */
    static ArrayList<RPSTNode<ControlFlow, Node>> sortTreeLevel(RPSTNode<ControlFlow, Node> lnode, Node startElem, RPST<ControlFlow, Node> rpst) {
        if (PlanningHelper.isSplit(lnode, rpst)) {
            ArrayList<RPSTNode<ControlFlow, Node>> unordered = new ArrayList<>();

            if (rpst.getChildren((lnode)).size() != 2) {
                unordered.addAll(rpst.getChildren((lnode)));
                return unordered;
            } else {
                unordered.addAll(rpst.getChildren((lnode)));
                if (getDepth(unordered.get(0), rpst) > getDepth(unordered.get(1), rpst)) {
                    ArrayList<RPSTNode<ControlFlow, Node>> ordered = new ArrayList<>();
                    ordered.add(unordered.get(1));
                    ordered.add(unordered.get(0));
                    return ordered;
                } else {
                    return new ArrayList<>(rpst.getChildren((lnode)));
                }
            }
        }

        Collection<RPSTNode<ControlFlow, Node>> topNodes = rpst.getChildren((lnode));
        ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes = new ArrayList<>();

        if (isRigid(lnode)) {
            return orderedTopNodes;
        }

        Node currentElem = startElem;
        while (orderedTopNodes.size() < topNodes.size()) {
            for (RPSTNode<ControlFlow, Node> node : topNodes) {
                if (node.getEntry().equals(currentElem)) {
                    orderedTopNodes.add(node);
                    currentElem = node.getExit();
                    break;
                }
            }
        }
        return orderedTopNodes;
    }

    /**
     * Returns String representation of node.
     */
    private static String getNodeRepresentation(Node n) {
        String s = "";
        if (PlanningHelper.isEvent(n)) {
            s = "Event + (" + n.getId() + ")";
        } else if (PlanningHelper.isGateway(n)) {
            Gateway g = (Gateway) n;
            if (g.isAND()) {
                s = "AND (" + n.getId() + ")";
            }
            if (g.isXOR()) {
                s = g.getName() + "(XOR," + n.getId() + ")";
            }
            if (g.isOR()) {
                s = g.getName() + "(OR," + n.getId() + ")";
            }
        } else {
            s = n.toString();
        }
        return s;
    }

    /**
     * Returns amount of nodes of the next level in the RPST.
     */
    private static int getSubLevelCount(RPSTNode<ControlFlow, Node> node, RPST<ControlFlow, Node> rpst) {
        return rpst.getChildren(node).size();
    }

    /**
     * Compute depth of a given component.
     */
    public static int getDepth(RPSTNode<ControlFlow, Node> node, RPST<ControlFlow, Node> rpst) {
        int depth = getDepthHelper(node, rpst);
        if (depth > 1) {
            return depth - 1;
        } else {
            return depth;
        }
    }

    /**
     * Helper for depth computation.
     */
    private static int getDepthHelper(RPSTNode<ControlFlow, Node> node, RPST<ControlFlow, Node> rpst) {
        if (node.getName().startsWith("T")) {
            return 0;
        }
        ArrayList<Integer> depthValues = new ArrayList<>();
        for (RPSTNode<ControlFlow, Node> n : rpst.getChildren(node)) {
            depthValues.add(getDepthHelper(n, rpst) + 1);
        }
        return Collections.max(depthValues);
    }

    /**
     * Returns type of given bond.
     */
    private static String getBondType(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        if (isEventSplit(bond, rpst)) {
            return "EVENTBASED";
        }
        if (isANDSplit(bond, rpst)) {
            return "AND";
        }
        if (isXORSplit(bond, rpst)) {
            return "XOR";
        }
        if (isORSplit(bond, rpst)) {
            return "OR";
        }
        if (isSkip(bond, rpst)) {
            return "Skip";
        }
        if (isLoop(bond, rpst)) {
            return "Loop";
        }
        return "";
    }

    /**
     * Decides whether a given bond is a loop (Arc from exit gateway to entry gateway).
     */
    static boolean isLoop(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        if (isBond(bond) && isGateway(bond.getEntry()) && ((Gateway) bond.getEntry()).isXOR()) {
            for (RPSTNode<ControlFlow, Node> node : rpst.getChildren((bond))) {
                if (isTrivial(node) && node.getEntry().equals(bond.getExit()) && node.getExit().equals(bond.getEntry()) && isGateway(node.getExit())) {
                    return true;
                }
                if (bond.getEntry().equals(node.getExit()) && isGateway(node.getExit())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Decides whether a given bond is a skip (Arc from entry gateway to exit gateway).
     */
    static boolean isSkip(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        if (isBond(bond) && isGateway(bond.getEntry()) && ((Gateway) bond.getEntry()).isXOR()) {
            for (RPSTNode<ControlFlow, Node> node : rpst.getChildren((bond))) {
                if (isTrivial(node) && node.getEntry().equals(bond.getEntry()) && node.getExit().equals(bond.getExit()) && isGateway(node.getExit())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Decides whether a given bond is a skip (All arcs are outgoing).
     */
    private static boolean isSplit(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        for (RPSTNode<ControlFlow, Node> node : rpst.getChildren((bond))) {
            if (node.getEntry() != bond.getEntry()) {
                return false;
            }
        }
        return true;
    }

    static boolean isEventSplit(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        if (isBond(bond) && isGateway(bond.getEntry())) {
            return ((Gateway) bond.getEntry()).isEventBased() && isSplit(bond, rpst);
        }
        return false;
    }

    /**
     * Decides whether a given bond is an AND split.
     */
    static boolean isANDSplit(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        if (isBond(bond) && isGateway(bond.getEntry())) {
            return ((Gateway) bond.getEntry()).isAND() && isSplit(bond, rpst);
        }
        return false;
    }

    /**
     * Decides whether a given bond is an XOR split.
     */
    static boolean isXORSplit(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        if (isBond(bond) && isGateway(bond.getEntry())) {
            return ((Gateway) bond.getEntry()).isXOR() && isSplit(bond, rpst) && !isSkip(bond, rpst);
        }
        return false;
    }

    /**
     * Decides whether a given bond is an OR split.
     */
    static boolean isORSplit(RPSTNode<ControlFlow, Node> bond, RPST<ControlFlow, Node> rpst) {
        if (isBond(bond) && isGateway(bond.getEntry())) {
            return ((Gateway) bond.getEntry()).isOR() && isSplit(bond, rpst);
        }
        return false;
    }

    /**
     * Decides whether a given component is a Bond.
     */
    static boolean isBond(RPSTNode<ControlFlow, Node> node) {
        return node.getName().startsWith("B");
    }

    /**
     * Decides whether a given component is a trivial one.
     */
    private static boolean isTrivial(RPSTNode<ControlFlow, Node> node) {
        return node.getName().startsWith("T");
    }

    /**
     * Decides whether a given component is a Rigid.
     */
    private static boolean isRigid(RPSTNode<ControlFlow, Node> node) {
        return node.getName().startsWith("R");
    }

    /**
     * Decides whether a given node is a gateway.
     */
    private static boolean isGateway(Node node) {
        return node.getClass().toString().equals("class de.hpi.bpt.process.Gateway");
    }

    /**
     * Decides whether considered event is an end event
     */
    static boolean isEndEvent(Object o, ProcessModel process) {
        if (o.getClass().toString().equals("class de.hpi.bpt.process.Event")) {
            org.woped.p2t.dataModel.process.Event event = process.getEvents().get(Integer.valueOf(((Event) o).getId()));
            return event.getType() == EventType.END_EVENT;
        }
        return false;
    }

    /**
     * Return true if o is an HPI event
     */
    static boolean isEvent(Object o) {
        return o.getClass().toString().equals("class de.hpi.bpt.process.Event");
    }

    /**
     * Returns true if o is a HPI task
     */
    static boolean isTask(Object o) {
        return o.getClass().toString().equals("class de.hpi.bpt.process.Task");
    }

    /**
     * Return next activity.
     */
    static RPSTNode<ControlFlow, Node> getNextActivity(RPSTNode<ControlFlow, Node> root, RPST<ControlFlow, Node> rpst) {
        ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes = PlanningHelper.sortTreeLevel(root, root.getEntry(), rpst);
        for (RPSTNode<ControlFlow, Node> node : orderedTopNodes) {
            int depth = PlanningHelper.getDepth(node, rpst);
            if (depth == 0 && PlanningHelper.isTrivial(node)) {
                return node;
            } else {
                return getNextActivity(node, rpst);
            }
        }
        return null;
    }

    /**
     * Determines activity count in RPST.
     */
    static int getActivityCount(RPSTNode<ControlFlow, Node> root, RPST<ControlFlow, Node> rpst) {
        int c = 0;
        ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes = PlanningHelper.sortTreeLevel(root, root.getEntry(), rpst);
        for (RPSTNode<ControlFlow, Node> node : orderedTopNodes) {
            int depth = PlanningHelper.getDepth(node, rpst);
            if (depth == 0 && (PlanningHelper.isTask(node.getEntry()))) {
                c++;
            } else {
                c = c + getActivityCount(node, rpst);
            }
        }
        return c;
    }

    /**
     * Print a given RPST Tree.
     */
    static void printTree(RPSTNode<ControlFlow, Node> root, int level, RPST<ControlFlow, Node> rpst) {
        ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes = PlanningHelper.sortTreeLevel(root, root.getEntry(), rpst);
        for (RPSTNode<ControlFlow, Node> node : orderedTopNodes) {
            int depth = PlanningHelper.getDepth(node, rpst);
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }

            // Determine type of node for presentation purposes
            String entryString = PlanningHelper.getNodeRepresentation(node.getEntry());
            String exitString = PlanningHelper.getNodeRepresentation(node.getExit());

            if (PlanningHelper.isBond(node)) {
                System.out.println(node.getName() + " (" + PlanningHelper.getBondType(node, rpst) + "," + depth + ", " + PlanningHelper.getSubLevelCount(node, rpst) + ") [" + entryString + " --> " + exitString + "]");
            } else {
                System.out.println(node.getName() + " (" + depth + ", " + PlanningHelper.getSubLevelCount(node, rpst) + ") [" + entryString + " --> " + exitString + "]");
            }

            if (depth > 0) {
                printTree(node, level + 1, rpst);
            }
        }
    }

    public static boolean containsRigid(RPSTNode<ControlFlow, Node> root, RPST<ControlFlow, Node> rpst) {
        if (root == null) {
            return false;
        }

        ArrayList<RPSTNode<ControlFlow, Node>> orderedTopNodes = PlanningHelper.sortTreeLevel(root, root.getEntry(), rpst);
        for (RPSTNode<ControlFlow, Node> node : orderedTopNodes) {
            if (isRigid(node)) {
                return true;
            }
        }
        return false;
    }
}