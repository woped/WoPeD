package org.woped.qualanalysis.sidebar.assistant.components;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JLabel;

import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;

/**
 * Class for the clickable Labels which highlight elements in the net
 * 
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 * 
 */
@SuppressWarnings("serial")
public class ClickLabel extends JLabel implements MouseListener {

	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	private static final Cursor DEFAULT_CURSOR = new Cursor(
			Cursor.DEFAULT_CURSOR);

	private Collection<AbstractElementModel> elements;

	private IEditor editor = null;

	/**
	 * adds all elements in the iterator to a local collection
	 * 
	 * @param string
	 *            - label text
	 * @param iter
	 *            - iterator of the elements to highlight
	 * @param editor
	 *            - reference to the editor
	 */
	public ClickLabel(String string, Iterator<AbstractElementModel> iter,
			IEditor editor) {
		super(string);
		elements = new ArrayList<AbstractElementModel>();
		if (iter != null && iter.hasNext()) {
			do {
				this.addMouseListener(this);
				Object aem = iter.next();
				if (aem instanceof AbstractElementModel)
					if (((AbstractElementModel) aem).getHierarchyLevel() == 0) {
						elements.add(((AbstractElementModel) aem));
					} else {
						elements.add(((AbstractElementModel) aem)
								.getRootOwningContainer().getOwningElement());
					}
			} while (iter.hasNext());
		}
		this.editor = editor;
	}

	/**
	 * 
	 * @param string
	 *            - label text
	 * @param elements
	 *            - collection of the elements to higlight
	 * @param editor
	 *            - reference to the editor
	 */
	public ClickLabel(String string, Collection<AbstractElementModel> elements,
			IEditor editor) {
		super(string);
		this.elements = elements;
		this.editor = editor;
		if (!elements.isEmpty())
			this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// First, dehighlight all elements in the net
		Iterator<AbstractElementModel> i = editor.getModelProcessor()
				.getElementContainer().getRootElements().iterator();
		while (i.hasNext()) {
			AbstractElementModel current = (AbstractElementModel) i.next();
			current.setHighlighted(false);
		}
		// Then, highlight all elements in the collection
		if (!elements.isEmpty()) {
			Iterator<AbstractElementModel> iterCopy = elements.iterator();
			while (iterCopy.hasNext()) {
				AbstractPetriNetModelElement currentHigh = (AbstractPetriNetModelElement) iterCopy
						.next();
				currentHigh.setHighlighted(true);
			}
		}
		editor.getGraph().repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		setCursor(HAND_CURSOR);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		setCursor(DEFAULT_CURSOR);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
