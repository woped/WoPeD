package org.woped.editor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.editor.controller.AllTestsEditorController;
import org.woped.editor.graphbeautifier.AllTestsEditorGraphBeautifier;
import org.woped.editor.orientation.AllTestsEditorOrientation;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsEditorController.class, AllTestsEditorGraphBeautifier.class, AllTestsEditorOrientation.class})
public class AllTestsEditor {
}
