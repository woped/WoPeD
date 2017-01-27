package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model;

import org.junit.Before;
import org.junit.Test;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.MarkingFormatter;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.MultiSetMarkingFormatter;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.tests.MarkingGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MpNodeTextFormatterTest {

    private MpNodeTextFormatter cut;
    private MarkingFormatter markingFormatter;
    private IMarking demoMarking;

    @Before
    public void setup() {
        markingFormatter = new MultiSetMarkingFormatter();

        cut = new MpNodeTextFormatter();
        cut.setMarkingFormatter(markingFormatter);

        demoMarking = new MarkingGenerator().createDemoMarking(new int[]{1, 0, 0}, new boolean[]{false, false, false});
    }

    @Test
    public void getText_noMpNode_returnsDefaultText() throws Exception {
        CoverabilityGraphNode node = new CoverabilityGraphNode(demoMarking);

        String expected = markingFormatter.getText(demoMarking);
        String actual = cut.getText(node);

        assertEquals(expected, actual);
    }

    @Test
    public void getText_NodeUnprocessed_ReturnsQuestionMark() throws Exception {

        MpNode node = new MpNode(demoMarking);

        String expected = "?";
        String actual = cut.getText(node);

        assertEquals(expected, actual);
    }

    @Test
    public void getText_NodeProcessedAndActive_hasProcessStepPrefix() throws Exception {
        MpNode node = new MpNode(demoMarking, MpNodeState.ACTIVE);
        node.setProcessedInStep(1);

        String text = cut.getText(node);
        assertTrue(text.startsWith("1 : "));
    }

    @Test
    public void getText_nodeInactive_hasProcessStepPrefix() throws Exception {
        MpNode node = new MpNode(demoMarking, MpNodeState.INACTIVE);
        node.setProcessedInStep(1);
        node.setDeactivatedInStep(2);

        String text = cut.getText(node);
        assertTrue(text.startsWith("1 : "));
    }

    @Test
    public void getText_nodeInactive_hasDeactivatedInStepPostfix() throws Exception {
        MpNode node = new MpNode(demoMarking, MpNodeState.INACTIVE);
        node.setProcessedInStep(1);
        node.setDeactivatedInStep(2);

        String text = cut.getText(node);
        assertTrue(text.endsWith(" : 2"));
    }
}