package org.woped.qualanalysis.structure;

import org.junit.Test;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.tests.TestNetGenerator;

import java.util.Set;

import static org.junit.Assert.*;

public class StructuralAnalysisTest {
    @Test
    public void getArcWeightViolations_noViolations_returnsEmptySet() throws Exception {

        EditorVC editor = new TestNetGenerator().getDemoEditor();
        StructuralAnalysis cut = new StructuralAnalysis(editor);

        Set<ArcModel> arcWeightViolations = cut.getArcWeightViolations();
        assertTrue(arcWeightViolations.isEmpty());
    }

    @Test
    public void getArcWeightViolations_containsViolation_returnsViolation() throws Exception {

        EditorVC editor = new TestNetGenerator().getDemoEditor();
        ModelElementContainer container = editor.getModelProcessor().getElementContainer();
        ArcModel arc1 = container.findArc("p1", "t1");
        arc1.setInscriptionValue(2);

        StructuralAnalysis cut = new StructuralAnalysis(editor);
        Set<ArcModel> arcWeightViolations = cut.getArcWeightViolations();

        assertEquals("There should be only one violation", 1, arcWeightViolations.size());
        assertTrue("The violating arc should be contained", arcWeightViolations.contains(arc1));
    }

    @Test
    public void isWorkflowNet_wfNet_returnsTrue() throws Exception {
        EditorVC editor = new TestNetGenerator().getDemoEditor();
        StructuralAnalysis cut = new StructuralAnalysis(editor);

        assertTrue(cut.isWorkflowNet());
    }

    @Test
    public void isWorkflowNwt_arcViolation_returnsFalse() throws Exception {
        EditorVC editor = new TestNetGenerator().getDemoEditor();
        ModelElementContainer container = editor.getModelProcessor().getElementContainer();
        ArcModel arc1 = container.findArc("p1", "t1");
        arc1.setInscriptionValue(2);

        StructuralAnalysis cut = new StructuralAnalysis(editor);

        assertFalse(cut.isWorkflowNet());
    }
}