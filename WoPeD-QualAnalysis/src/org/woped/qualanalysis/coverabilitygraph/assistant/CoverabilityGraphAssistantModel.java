package org.woped.qualanalysis.coverabilitygraph.assistant;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.*;
import org.woped.qualanalysis.coverabilitygraph.model.AbstractReachabilityGraphModel;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.events.EdgeEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewFactory;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.AbstractLowLevelPetriNetBuilder;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * This class should be used as graph model for coverability graph assistants.
 * <p>
 * It can either be used directly by calling the methods to add/modify/remove nodes or edges,
 * or the graph listener can be used do react on the related graph events.
 */
public class CoverabilityGraphAssistantModel extends AbstractReachabilityGraphModel {

    private GraphListener assistantListener;
    private MarkingNet markingNet;

    private HashSet<DefaultGraphCell> newCells;
    private HashMap<DefaultGraphCell, AttributeMap> modifiedCells;
    private HashSet<DefaultGraphCell> removedCells;

    /**
     * Constructs a new graph model that can be used with an coverability graph assistant.
     *
     * @param editor the editor of the underlying petri net
     * @param viewFactory the view factory to create graph element views
     */
    CoverabilityGraphAssistantModel(IEditor editor, CoverabilityGraphViewFactory viewFactory) {
        super(editor, viewFactory);
        initialize();
    }

    @Override
    public String getLegendByID() {
        return markingNet.placesToStringId();
    }

    @Override
    public String getLegendByName() {
        return markingNet.placesToStringName();
    }

    @Override
    public void reset() {
        clearGraph();

        newCells.clear();
        modifiedCells.clear();
        removedCells.clear();

        refresh();
    }

    /**
     * Adds the edge to the graph the next time the method {@link #refresh()} is called.
     *
     * @param edge the edge to add
     */
    @SuppressWarnings("WeakerAccess")
    public void addEdge(CoverabilityGraphEdge edge){

        if(edge.getSourceNode().equals(edge.getTargetNode())){
            setEdgeLoopLabelPosition(edge.getAttributes());
        }

        newCells.add(edge);
    }

    /**
     * Removes the edge from the graph the next time the method {@link #refresh()} is called.
     *
     * @param edge the edge to remove
     */
    @SuppressWarnings("WeakerAccess")
    public void removeEdge(CoverabilityGraphEdge edge) {
        removedCells.add(edge);
    }

    /**
     * Reconnects the edge to the new destination immediately.
     *
     * @param edge the edge to reconnect
     * @param newDestination the new node to which the edge is connected
     * @param sourceReconnected indicator if the reconnected end is the source of the edge
     */
    @SuppressWarnings("WeakerAccess")
    public void reconnectEdge(CoverabilityGraphEdge edge, CoverabilityGraphNode newDestination, boolean sourceReconnected){
        ConnectionSet connectionSet = new ConnectionSet(new HashSet());
        connectionSet.disconnect(edge, sourceReconnected);
        connectionSet.connect(edge, newDestination.getFirstChild(), sourceReconnected);

        Map<CoverabilityGraphEdge, AttributeMap> changeMap = new HashMap<>();
        if((newDestination.equals(edge.getSourceNode()) && !sourceReconnected) || (newDestination.equals(edge.getTargetNode()) && sourceReconnected)){
            AttributeMap map = new AttributeMap(1);
            setEdgeLoopLabelPosition(map);
            changeMap.put(edge, map);
        }

        getView().edit(changeMap, connectionSet, null, null);
    }

    /**
     * Modifies the edge the next time the method {@link #refresh()} is called.
     *
     * @param edge the edge to modify
     * @param modifiedAttributes the modified attributes of the edge
     */
    @SuppressWarnings("WeakerAccess")
    public void modifyEdge(CoverabilityGraphEdge edge, AttributeMap modifiedAttributes){
        modifyCell(edge, modifiedAttributes);
    }

    /**
     * Adds the node to the graph the next time the method {@link #refresh()} is called.
     *
     * @param node the node to add
     */
    public void addNode(CoverabilityGraphNode node){
        newCells.add(node);
    }

    /**
     * Removes the node from the graph the next time the method {@link #refresh()} is called.
     *
     * @param node the node to remove
     */
    @SuppressWarnings("WeakerAccess")
    public void removeNode(CoverabilityGraphNode node){
        removedCells.add(node);

        for (CoverabilityGraphEdge e : node.getEdges()) {
            removedCells.add(e);
        }

        for(Object child: node.getChildren()){
            removedCells.add((DefaultGraphCell) child);
        }

        removedCells.add(node);
    }

    /**
     * Modifies the node the next time the method {@link #refresh()} is called.
     *
     * @param node the node to modify
     * @param modifiedAttributes the modified attributes of the node
     */
    @SuppressWarnings("WeakerAccess")
    public void modifyNode(CoverabilityGraphNode node, AttributeMap modifiedAttributes){
        modifyCell(node, modifiedAttributes);
    }

    /**
     * Adds, updates and removes all elements that has been added/modified/removed since the last call of this method
     * and applies the configured layout to the graph afterwards.
     */
    public void refresh() {
        insertElements();
        updateElements();
        removeElements();

        applyLayout();
    }

    /**
     * Gets the graph listener that can be added to an {@link CoverabilityGraphAssistant} to call the appropriate methods
     * automatically when graph events occur.
     *
     * @return the graph listener
     */
    public CoverabilityGraphListener getGraphListener(){
        return assistantListener;
    }

    @Override
    protected void initialize() {
        super.initialize();

        AbstractLowLevelPetriNetBuilder builder = BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(getEditor());
        ILowLevelPetriNet lowLevelPetriNet = builder.getLowLevelPetriNet();

        assistantListener = new GraphListener();
        markingNet = new MarkingNet(lowLevelPetriNet);

        newCells = new HashSet<>();
        modifiedCells  = new HashMap<>();
        removedCells = new HashSet<>();
        refresh();
    }

    private void modifyCell(DefaultGraphCell cell, AttributeMap modifiedAttributes){
        AttributeMap attributes = new AttributeMap();

        // Check if there are already modifications
        if(modifiedCells.containsKey(cell)){
            attributes = modifiedCells.get(cell);
        }

        //noinspection unchecked
        attributes.putAll(modifiedAttributes);

        modifiedCells.put(cell, attributes);
    }

    private void insertElements() {
        if (!newCells.isEmpty()) getView().insert(newCells.toArray());
        newCells.clear();
    }

    private void updateElements() {
        if (!modifiedCells.isEmpty()) {
            getView().edit(modifiedCells);
            modifiedCells.clear();
        }
    }

    private void setEdgeLoopLabelPosition(AttributeMap map) {
        GraphConstants.setLabelPosition(map, new Point2D.Double(GraphConstants.PERMILLE * 0.45, -10));
    }

    private void removeElements() {
        if (removedCells.isEmpty()) return;

        getView().remove(removedCells.toArray());
        removedCells.clear();
    }

    /**
     * This class calls the appropriate methods if graph events occur.
     */
    private class GraphListener extends CoverabilityGraphAdapter {

        @Override
        public void edgeAdded(EdgeEvent event) {
            addEdge(event.getEdge());
        }

        @Override
        public void edgeRemoved(EdgeEvent event) {
            removeEdge(event.getEdge());
        }

        @Override
        public void edgeReconnected(EdgeReconnectedEvent event) {
            reconnectEdge(event.getEdge(), event.getNewNode(), event.isSourceOfEdge());
        }

        @Override
        public void edgeModified(EdgeModifiedEvent event) {
           modifyEdge(event.getEdge(), event.getModifiedAttributes());
        }

        @Override
        public void nodeAdded(NodeEvent event) {
            addNode(event.getNode());
        }

        @Override
        public void nodeRemoved(NodeEvent event) {
            removeNode(event.getNode());
        }

        @Override
        public void nodeModified(NodeModifiedEvent event) {
            modifyNode(event.getNode(), event.getModifiedAttributes());
        }

        @Override
        public void refreshRequested() {
            refresh();
        }

        @Override
        public void prepareRestartRequested() {
            reset();
        }
    }
}
