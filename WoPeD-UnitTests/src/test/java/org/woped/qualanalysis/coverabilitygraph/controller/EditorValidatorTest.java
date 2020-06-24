package org.woped.qualanalysis.coverabilitygraph.controller;

import org.junit.Test;
import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.qualanalysis.coverabilitygraph.gui.EditorValidator;
import org.woped.tests.TestNetGenerator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditorValidatorTest {

    @Test
    public void hasChanged_noChange_returnsFalse() throws Exception {
        IEditor editor = mock(IEditor.class);
        PetriNetModelProcessor simpleNet = new TestNetGenerator().createSimpleNet();
        when(editor.getModelProcessor()).thenReturn(simpleNet);
        EditorValidator cut = new EditorValidator(editor);

        assertFalse(cut.hasChanged());
    }

    @Test
    public void hasChanged_tokensChanged_returnTrue() throws Exception {

        IEditor editor = mock(IEditor.class);
        PetriNetModelProcessor simpleNet = new TestNetGenerator().createSimpleNet();
        when(editor.getModelProcessor()).thenReturn(simpleNet);
        EditorValidator cut = new EditorValidator(editor);

        PlaceModel p1 = (PlaceModel) simpleNet.getElementContainer().getElementById("p1");
        p1.addToken();

        assertTrue(cut.hasChanged());
    }

    @Test
    public void acceptChanges_netChanged_updatesValidState() throws Exception {
        IEditor editor = mock(IEditor.class);
        PetriNetModelProcessor simpleNet = new TestNetGenerator().createSimpleNet();
        when(editor.getModelProcessor()).thenReturn(simpleNet);
        EditorValidator cut = new EditorValidator(editor);

        PlaceModel p1 = (PlaceModel) simpleNet.getElementContainer().getElementById("p1");
        p1.addToken();
        assertTrue("The new state should be invalid before the accept",cut.hasChanged());

        cut.acceptChanges();
        assertFalse("The new state should be valid after the accept", cut.hasChanged());
    }
}