package org.woped.qualanalysis.coverabilitygraph.model;

import org.junit.Before;
import org.junit.Test;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.tests.DemoGraphGenerator;
import org.woped.tests.MarkingGenerator;

import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class CoverabilityGraphNodeTest {

    private IMarking marking;
    private CoverabilityGraphNode cut;

    @Before
    public void setup() {
        marking = new MarkingGenerator().createDemoMarking();
        cut = new CoverabilityGraphNode(marking);
    }

    @Test
    public void getDirectAncestors_noParent_returnsEmptyList() throws Exception {

        assertTrue(cut.getDirectAncestors().isEmpty());
    }

    @Test
    public void getDirectAncestors_oneParent_containsParent() throws Exception {

        CoverabilityGraphNode parent = new CoverabilityGraphNode(marking);
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge = new CoverabilityGraphEdge(parent, cut, t1);

        CoverabilityGraph graph = createGraph();
        graph.getGraphLayoutCache().insert(new Object[]{parent, cut, edge});

        Collection<CoverabilityGraphNode> ancestors = cut.getDirectAncestors();

        assertTrue("There should be only one ancestor", ancestors.size() == 1);
        assertTrue("Parent should be contained", ancestors.contains(parent));
    }

    @Test
    public void getDirectAncestors_twoParentsOne_containsBoth() throws Exception {
        CoverabilityGraph graph = createGraph();

        CoverabilityGraphNode parent1 = new CoverabilityGraphNode(marking);
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge1 = new CoverabilityGraphEdge(parent1, cut, t1);
        graph.getGraphLayoutCache().insert(new Object[]{parent1, cut, edge1});

        CoverabilityGraphNode parent2 = new CoverabilityGraphNode(marking);
        TransitionNode t2 = new TransitionNode("t2", "t2", "t2", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge2 = new CoverabilityGraphEdge(parent2, cut, t2);
        graph.getGraphLayoutCache().insert(new Object[]{parent2, edge2});

        Collection<CoverabilityGraphNode> ancestors = cut.getDirectAncestors();
        assertTrue("There should be two ancestors", ancestors.size() == 2);
        assertTrue("First Parent should be contained", ancestors.contains(parent1));
        assertTrue("Second Parent should be contained", ancestors.contains(parent2));
    }

    @Test
    public void getDirectAncestors_oneParentOneChild_doesNotContainChild() throws Exception {
        CoverabilityGraph graph = createGraph();

        CoverabilityGraphNode parent = new CoverabilityGraphNode(marking);
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge1 = new CoverabilityGraphEdge(parent, cut, t1);

        CoverabilityGraphNode child = new CoverabilityGraphNode(marking);
        TransitionNode t2 = new TransitionNode("t2", "t2", "t2", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge2 = new CoverabilityGraphEdge(cut, child, t2);

        graph.getGraphLayoutCache().insert(new Object[]{parent, cut, child, edge1, edge2});
        Collection<CoverabilityGraphNode> ancestors = cut.getDirectAncestors();

        assertTrue("There should be only one parent", ancestors.size() == 1);
        assertTrue("Child should not be contained", !ancestors.contains(child));
        assertTrue("Parent should be contained", ancestors.contains(parent));
    }

    @Test
    public void getDirectDescendants_noChildren_returnsEmptyList() throws Exception {
        Collection<CoverabilityGraphNode> childrenNodes = cut.getDirectDescendants();
        assertTrue(childrenNodes.isEmpty());
    }

    @Test
    public void getDirectDescendants_oneParentOneChild_doesNotContainParent() throws Exception {
        CoverabilityGraph graph = createGraph();

        CoverabilityGraphNode parent = new CoverabilityGraphNode(marking);
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge1 = new CoverabilityGraphEdge(parent, cut, t1);

        CoverabilityGraphNode child = new CoverabilityGraphNode(marking);
        TransitionNode t2 = new TransitionNode("t2", "t2", "t2", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge2 = new CoverabilityGraphEdge(cut, child, t2);

        graph.getGraphLayoutCache().insert(new Object[]{parent, cut, child, edge1, edge2});
        Collection<CoverabilityGraphNode> childrenNodes = cut.getDirectDescendants();

        assertTrue("There should be only one child", childrenNodes.size() == 1);
        assertTrue("Parent should not be contained", !childrenNodes.contains(parent));
        assertTrue("Child should be contained", childrenNodes.contains(child));
    }

    @Test
    public void getDirectDescendants_twoChildren_bothContained() throws Exception {
        CoverabilityGraph graph = createGraph();

        CoverabilityGraphNode child1 = new CoverabilityGraphNode(marking);
        TransitionNode t1 = new TransitionNode("t1", "t1", "t1", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge1 = new CoverabilityGraphEdge(cut, child1, t1);

        CoverabilityGraphNode child2 = new CoverabilityGraphNode(marking);
        TransitionNode t2 = new TransitionNode("t2", "t2", "t2", AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        CoverabilityGraphEdge edge2 = new CoverabilityGraphEdge(cut, child2, t2);

        graph.getGraphLayoutCache().insert(new Object[]{cut, child1, child2, edge1, edge2});
        Collection<CoverabilityGraphNode> childrenNodes = cut.getDirectDescendants();

        assertTrue("There should be two children", childrenNodes.size() == 2);
        assertTrue("First child should be contained", childrenNodes.contains(child1));
        assertTrue("Second child should be contained", childrenNodes.contains(child1));
    }

    private CoverabilityGraph createGraph() {
        return new DemoGraphGenerator().createGraph();
    }
}