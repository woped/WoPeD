package org.woped.editor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.woped.editor.controller.AllTestsEditorController;
import org.woped.editor.graphbeautifier.AllTestsEditorGraphBeautifier;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllTestsEditorController.class, AllTestsEditorGraphBeautifier.class})
public class AllTestsEditor {
}
