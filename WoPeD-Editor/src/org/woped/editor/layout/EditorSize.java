package org.woped.editor.layout;

import java.awt.Container;
import java.awt.Dimension;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.editor.controller.vc.EditorVC;

/**
 * 
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller,
 *         Sebastian Fuss
 * @edit Lennart Oess
 * 
 */
public class EditorSize {

	private IEditor editor = null;
	private Dimension maxEditorSize = null;
	private Dimension modelSize = null;
	private Dimension newEditorSize = null;
	private Dimension editorSizeBeforeSidebar = null;

	public final int SIDEBAR_WIDTH = 320;

	/**
	 * 
	 * @param editor
	 *            the editor to size and check
	 */
	public EditorSize(IEditor editor) {
		this.editor = editor;
	}

	/**
	 * resizes the editor basing on the size of the model and the maximum editor
	 * size
	 */
	public void resize(boolean downSize) {
		calculateMaxEditorSize();
		calculateModelSize();
		calculateNewEditorSize();
		setSize(newEditorSize, downSize);
	}

	/**
	 * resizes the editor-window
	 * 
	 * @param dim
	 *            the dimension to set as new size
	 */
	private void setSize(Dimension dim, boolean downSize) {
		Container parentContainer = null;
		if (editor.isSubprocessEditor())
			parentContainer = editor.getParent().getParent().getParent()
					.getParent();
		else
			parentContainer = editor.getParent().getParent().getParent()
					.getParent().getParent();
		Dimension currentSize = parentContainer.getSize();
		if (editor.isAnalysisBarVisible())
			editorSizeBeforeSidebar = currentSize;
		else if (editorSizeBeforeSidebar != null) {
			parentContainer.setSize(editorSizeBeforeSidebar);
			parentContainer.setLocation(0, 0);
			return;
		}
		if (!downSize) {
			if (currentSize.height > dim.height)
				dim.height = currentSize.height;
			if (currentSize.width > dim.width)
				dim.width = currentSize.width;
		}
		if (currentSize.height != dim.height || currentSize.width != dim.width) {
			parentContainer.setSize(dim);
			parentContainer.setLocation(0, 0);
		}
	}

	/**
	 * calculates the maximum editor size the editor should always fit in the
	 * main window
	 */
	private void calculateMaxEditorSize() {
		if (editor.isSubprocessEditor())
			maxEditorSize = editor.getParent().getParent().getParent()
					.getParent().getParent().getSize();
		else
			maxEditorSize = editor.getParent().getParent().getParent()
					.getParent().getParent().getParent().getSize();
	}

	/**
	 * calculates the size of the model basing on model-position and
	 * -width/-height
	 */
	private void calculateModelSize() {
		ModelElementContainer elements = editor.getModelProcessor()
				.getElementContainer();
		AbstractElementModel element = null;

		modelSize = new Dimension(100, 100);

		for (String elementId : elements.getIdMap().keySet()) {
			element = elements.getElementById(elementId);
			int elementNameWidth = element.getNameModel().getNameValue()
					.length() * 7;
			// TODO: The best would be taking the size of the blue box itself
			if (element.getX() + element.getWidth() + elementNameWidth > modelSize.width) {
				modelSize.width = element.getX() + element.getWidth()
						+ elementNameWidth;
			}
			if (element.getY() + element.getHeight() > modelSize.height) {
				modelSize.height = element.getY() + element.getHeight();
			}
		}
		// Adding some offset for editor-frame, statusbar, scrollbar, etc.
		modelSize.width += 60;
		modelSize.height += 120;
	}

	/**
	 * calculates the new editor size.<br />
	 * the editor should not exceed the maxEditorSize and should not fall under
	 * the minEditorSize <br />
	 * if analysisSideBar is visible, the sideBar-width will be added to the
	 * editor-width
	 */
	private void calculateNewEditorSize() {
		// set newEditorSize to modelSize
		newEditorSize = new Dimension(modelSize);
		// add sideBar-width if sideBar is displayed
		if (editor.isAnalysisBarVisible()) {
			newEditorSize.width += SIDEBAR_WIDTH;
			newEditorSize.height = ((EditorVC) editor).getAnalysisSideBar()
					.getHeight();
		}
		// check if newEditorSize is bigger than maxEditorSize and fix it
		if (newEditorSize.width > maxEditorSize.width)
			newEditorSize.width = maxEditorSize.width;
		if (newEditorSize.height > maxEditorSize.height)
			newEditorSize.height = maxEditorSize.height;
	}

}
