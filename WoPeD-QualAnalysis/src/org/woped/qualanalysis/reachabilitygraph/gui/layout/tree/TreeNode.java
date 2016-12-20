package org.woped.qualanalysis.reachabilitygraph.gui.layout.tree;

import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPortModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class TreeNode  extends AbstractTreeForTreeLayout<ReachabilityPlaceModel>{

    public TreeNode(ReachabilityPlaceModel root) {
        super(root);
    }

    /**
     * Returns the parent of a node, if it has one.
     * <p>
     * Time Complexity: O(1)
     *
     * @param place &nbsp;
     * @return [nullable] the parent of the node, or null when the node is a
     * root.
     */
    @Override
    public ReachabilityPlaceModel getParent(ReachabilityPlaceModel place) {

        Collection<ReachabilityEdgeModel> incomingEdges = getEdges(place, Direction.Incoming);

        if(incomingEdges.size() == 0) return null;

        ReachabilityPortModel parentPort = (ReachabilityPortModel) incomingEdges.iterator().next().getTarget();
        return (ReachabilityPlaceModel) parentPort.getParent();
    }

    /**
     * Return the children of a node as a {@link List}.
     * <p>
     * Time Complexity: O(1)
     * <p>
     * Also the access to an item of the list must have time complexity O(1).
     * <p>
     * A client must not modify the returned list.
     *
     * @param place &nbsp;
     * @return the children of the given node. When node is a leaf the list is
     * empty.
     */
    @Override
    public List<ReachabilityPlaceModel> getChildrenList(ReachabilityPlaceModel place) {
        List<ReachabilityPlaceModel> children = new LinkedList<>();
        Collection<ReachabilityEdgeModel> edges = getEdges(place, Direction.Outgoing);

        for(ReachabilityEdgeModel edge: edges){
            ReachabilityPortModel childPort = (ReachabilityPortModel) edge.getTarget();
            children.add((ReachabilityPlaceModel) childPort.getParent());
        }

        return children;
    }

    private Collection<ReachabilityEdgeModel> getEdges(ReachabilityPlaceModel place, Direction direction){

        List<ReachabilityEdgeModel> edges = new LinkedList<>();

        ReachabilityPortModel port = (ReachabilityPortModel) place.getFirstChild();
        if(port == null) return edges;

        for(Object elem : port.getEdges()){
            if(!(elem instanceof ReachabilityEdgeModel)) continue;
            ReachabilityEdgeModel edge = (ReachabilityEdgeModel) elem;

            ReachabilityPlaceModel source = (ReachabilityPlaceModel) ((ReachabilityPortModel)edge.getSource()).getParent();
            ReachabilityPlaceModel target = (ReachabilityPlaceModel) ((ReachabilityPortModel)edge.getTarget()).getParent();
            if(source == null || target == null) continue;

            if(source.equals(place) && (direction == Direction.Outgoing || direction == Direction.Both)){
                edges.add(edge);
            }

            if(target.equals(place) && (direction== Direction.Incoming || direction == Direction.Both)){
                edge.add(edge);
            }
        }

        return edges;
    }

    private enum Direction {
        Incoming, Outgoing, Both
    }
}
