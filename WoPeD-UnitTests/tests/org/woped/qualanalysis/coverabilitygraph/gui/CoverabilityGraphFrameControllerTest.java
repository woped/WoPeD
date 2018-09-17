package org.woped.qualanalysis.coverabilitygraph.gui;

import org.junit.Before;
import org.junit.Test;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.editor.controller.vc.EditorPanel;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.starter.DefaultEditorFrame;
import org.woped.tests.TestNetGenerator;
import org.woped.qualanalysis.understandability.NetColorScheme;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CoverabilityGraphFrameControllerTest {

    private CoverabilityGraphFrameController cut;
    private JDesktopPane desktop;

    @Before
    public void setUp() throws Exception {
        IUserInterface ui = mock(IUserInterface.class);
        desktop = mock(JDesktopPane.class);
        when(ui.getPropertyChangeSupportBean()).thenReturn(desktop);
        cut = CoverabilityGraphFrameController.getInstance(ui);
    }

    @Test
    public void containsGraphForNet_graphNotGenerated_returnsFalse() throws Exception {

        IEditor editor = mock(IEditor.class);

        boolean actual = cut.containsGraphForNet(editor);

        assertEquals(false, actual);
    }

    @Test
    public void containsGraphForNet_graphGenerated_returnsTrue() throws Exception {

        EditorVC editor = createDemoEditor();
        cut.showGraph(editor);

        boolean actual = cut.containsGraphForNet(editor);

        assertEquals(true, actual);
    }

    @Test
    public void removeHighlightingFromGraph_containsGraph_callsRemoveHighlightingOnViewController() throws Exception {
        Map<IEditor, CoverabilityGraphVC> testViewController = new HashMap<>();
        EditorVC demoEditor = createDemoEditor();
        CoverabilityGraphVC graphVC = mock(CoverabilityGraphVC.class);
        testViewController.put(demoEditor, graphVC);

        cut.setViewControllerForTesting(testViewController);

        cut.removeHighlightingFromGraph(demoEditor);

        verify(graphVC, times(1)).removeHighlighting();
    }

    @Test
    public void removeHighlightingFromGraph_doesNotContainGraph_doesNotThrowNullPointerException() throws Exception {
        cut.removeHighlightingFromGraph(null);
    }


    private EditorVC createDemoEditor() {
        NetColorScheme scheme;
        scheme = mock(NetColorScheme.class);
        EditorPanel panel = mock(EditorPanel.class);
        when(panel.getUnderstandColoring()).thenReturn(scheme);

        EditorVC editor = new EditorVC();
        editor.setEditorPanel(panel);
        editor.setModelProcessor(new TestNetGenerator().createSimpleNet());

        DefaultEditorFrame editorFrame = mock(DefaultEditorFrame.class);
        when(editorFrame.getEditor()).thenReturn(editor);
        when(desktop.getAllFrames()).thenReturn(new JInternalFrame[]{editorFrame});

        return editor;
    }

}