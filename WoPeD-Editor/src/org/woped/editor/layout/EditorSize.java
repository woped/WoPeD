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
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class EditorSize {

	private IEditor editor = null;
	private Dimension maxEditorSize = null;
	private Dimension modelSize = null;
	private Dimension newEditorSize = null;

	public final int SIDEBAR_WIDTH = 300;

	/**
	 * 
	 * @param editor
	 *            the editor to size and check
	 */
	public EditorSize(IEditor editor) {
		this.editor = editor;
	}

	/**
	 * resizes the editor basing on the size of the model and the maximum editor size
	 */
	public void resize() {
		calculateMaxEditorSize();
		calculateModelSize();
		calculateNewEditorSize();
		setSize(newEditorSize);
	}

	/**
	 * resizes the editor-window
	 * 
	 * @param dim
	 *            the dimension to set as new size
	 */
	private void setSize(Dimension dim) {
		Container parentContainer = ((EditorVC) editor).getParent().getParent().getParent().getParent().getParent();
		parentContainer.setSize(dim);
		parentContainer.setLocation(0, 0);
	}

	/**
	 * calculates the maximum editor size the editor should always fit in the main window
	 */
	private void calculateMaxEditorSize() {
		maxEditorSize = ((EditorVC) editor).getParent().getParent().getParent().getParent().getParent().getParent()
				.getSize();
	}

	/**
	 * calculates the size of the model basing on model-position and -width/-height
	 */
	private void calculateModelSize() {
		ModelElementContainer elements = editor.getModelProcessor().getElementContainer();
		AbstractElementModel element = null;

		// set minimum editor size
		// width should not fall below a value of 600 because of
		// displaying the statusbar
		if (editor.isRotateSelected()) // vertical
			modelSize = new Dimension(600, 800);
		else
			// horizontal
			modelSize = new Dimension(800, 600);

		for (String elementId : elements.getIdMap().keySet()) {
			element = elements.getElementById(elementId);
			int elementNameWidth = element.getNameModel().getNameValue().length() * 7;
			// TODO: The best would be taking the size of the blue box itself
			if (element.getX() + element.getWidth() + elementNameWidth > modelSize.width) {
				modelSize.width = element.getX() + element.getWidth() + elementNameWidth;
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
	 * calculates the new editor size. the editor should not exceed the maxEditorSize if analysisSideBar is visible, the
	 * sideBar-width will be added to the editor-width
	 */
	private void calculateNewEditorSize() {
		newEditorSize = new Dimension(modelSize);
		if (editor.getAnalysisBarVisible()) {
			newEditorSize.width += SIDEBAR_WIDTH;
			newEditorSize.height = maxEditorSize.height;
		}
		if (newEditorSize.width > maxEditorSize.width)
			newEditorSize.width = maxEditorSize.width;
		if (newEditorSize.height > maxEditorSize.height)
			newEditorSize.height = maxEditorSize.height;
	}

}
