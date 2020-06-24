package org.woped.file.controller.vep;

import org.junit.Test;
import org.woped.core.model.ArcModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.tests.TestNetGenerator;

import static org.junit.Assert.*;

public class FileEventProcessorTest {

    @Test
    public void containsArcWeights_hasArcWeights_returnsTrue() throws Exception {

        FileEventProcessor cut = new FileEventProcessor(null);
        EditorVC demoEditor = new TestNetGenerator().getDemoEditor();
        PetriNetModelProcessor processor = demoEditor.getModelProcessor();
        ArcModel arc = processor.getElementContainer().findArc("p1", "t1");
        arc.setInscriptionValue(2);

        assertTrue(cut.containsArcWeights(demoEditor));
    }    @Test

    public void containsArcWeights_hasNoArcWeights_returnsTrue() throws Exception {

        FileEventProcessor cut = new FileEventProcessor(null);
        EditorVC demoEditor = new TestNetGenerator().getDemoEditor();

        assertFalse(cut.containsArcWeights(demoEditor));
    }

}