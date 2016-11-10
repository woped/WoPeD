package org.woped.editor.graphbeautifier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.model.ArcModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.tests.TestNetGenerator;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SGYGraphTest {

    private TestNetGenerator netGenerator;

    @Before
    public void setUp() throws Exception {
        netGenerator = new TestNetGenerator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getArc_noArcExists_returnsNull() throws Exception {

        PetriNetModelProcessor processor = netGenerator.createSimpleNet();
        AbstractGraph graph = mock(AbstractGraph.class);
        when(graph.getSelectionCell()).thenReturn(new Object[0]);

        EditorVC editor = mock(EditorVC.class);
        when(editor.getModelProcessor()).thenReturn(processor);
        when(editor.getGraph()).thenReturn(graph);

        SGYGraph cut = new SGYGraph(editor);

        SGYElement source = new SGYElement(processor.getElementContainer().getElementById("p1"));
        SGYElement target = new SGYElement(processor.getElementContainer().getElementById("p2"));

        ArcModel arc = cut.getArc(source, target);

        assertNull(arc);
    }

    @Test
    public void getArc_arcExists_returnsArc() throws Exception {

        PetriNetModelProcessor processor = netGenerator.createSimpleNet();
        AbstractGraph graph = mock(AbstractGraph.class);
        when(graph.getSelectionCell()).thenReturn(new Object[0]);

        EditorVC editor = mock(EditorVC.class);
        when(editor.getModelProcessor()).thenReturn(processor);
        when(editor.getGraph()).thenReturn(graph);

        SGYGraph cut = new SGYGraph(editor);

        String sourceId = "p1";
        String targetId = "t1";
        SGYElement source = new SGYElement(processor.getElementContainer().getElementById(sourceId));
        SGYElement target = new SGYElement(processor.getElementContainer().getElementById(targetId));


        ArcModel expected = processor.getElementContainer().findArc(sourceId, targetId);
        ArcModel actual = cut.getArc(source, target);

        assertEquals(expected, actual);
    }

    @Test
    public void getArc_arcMapContainsNull_returnsNull() throws Exception {

        PetriNetModelProcessor processor = netGenerator.createSimpleNet();
        Map<String, ArcModel> arcMap = processor.getElementContainer().getArcMap();
        arcMap.put("ghostArc", null);

        AbstractGraph graph = mock(AbstractGraph.class);
        when(graph.getSelectionCell()).thenReturn(new Object[0]);

        EditorVC editor = mock(EditorVC.class);
        when(editor.getModelProcessor()).thenReturn(processor);
        when(editor.getGraph()).thenReturn(graph);

        SGYGraph cut = new SGYGraph(editor);

        SGYElement source = new SGYElement(processor.getElementContainer().getElementById("p1"));
        SGYElement target = new SGYElement(processor.getElementContainer().getElementById("p2"));

        ArcModel arc = cut.getArc(source, target);

        assertNull(arc);
    }
}