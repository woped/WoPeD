package org.woped.qualanalysis.sidebar.assistant.components;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class for the clickable Labels which highlight elements in the net
 *
 * @author Lennart Oess, Arthur Vetter, Jens Tessen, Heiko Herzog
 */
@SuppressWarnings("serial")
public class ClickLabel extends JLabel {

    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

    private Collection<AbstractPetriNetElementModel> elements;
    private Collection<ArcModel> arcs;
    private IEditor editor = null;

    /**
     * Constructs a new label that highlights the provided elements on mouse click.
     *
     * @param string          label text
     * @param elementIterator iterator of the elements to highlight
     * @param editor          reference to the editor
     */
    public ClickLabel(String string, Iterator<AbstractPetriNetElementModel> elementIterator, IEditor editor) {
        super(string);
        initialize(editor, elementIterator, null);
    }

    /**
     * Constructs a new label that highlights the provided elements on mouse click.
     *
     * @param string   label text
     * @param elements collection of the elements to highlight
     * @param editor   reference to the editor
     */
    ClickLabel(String string, Collection<AbstractPetriNetElementModel> elements, IEditor editor) {
        super(string);
        initialize(editor, elements.iterator(), null);
    }

    /**
     * Constructs a new label that highlights the provided arcs in the provided petri net on mouse click.
     *
     * @param title  the text of the label
     * @param editor the editor of the petri net
     * @param arcs   the arcs to highlight
     */
    ClickLabel(String title, IEditor editor, Collection<ArcModel> arcs) {
        super(title);
        initialize(editor, null, arcs.iterator());
    }

    private void initialize(IEditor editor, Iterator<AbstractPetriNetElementModel> elementIterator, Iterator<ArcModel> arcIterator) {
        elements = new ArrayList<>();
        arcs = new ArrayList<>();
        this.editor = editor;
        registerElements(elementIterator);
        registerArcs(arcIterator);

        addClickSupport();
    }

    private void registerElements(Iterator<AbstractPetriNetElementModel> elementIterator) {
        if (elementIterator == null || !elementIterator.hasNext()) return;

        while (elementIterator.hasNext()) {
            AbstractPetriNetElementModel element = elementIterator.next();

            if (element == null) continue;

            if (element.getHierarchyLevel() == 0) elements.add(element);
            else elements.add(element.getRootOwningContainer().getOwningElement());
        }
    }

    private void registerArcs(Iterator<ArcModel> arcIterator) {
        if (arcIterator == null || !arcIterator.hasNext()) return;

        while (arcIterator.hasNext()) {
            ArcModel arc = arcIterator.next();
            arcs.add(arc);
        }
    }

    private void addClickSupport() {
        if (elements.isEmpty() && arcs.isEmpty()) return;
        addMouseListener(new LabelClickListener());
    }

    private void removeHighlighting() {
        editor.getModelProcessor().removeHighlighting();
    }

    private void addHighlighting() {
        highlightElements();
        highlightArcs();
        editor.getGraph().repaint();
    }

    private void highlightElements() {
        for (AbstractPetriNetElementModel element : elements) {
            element.setHighlighted(true);
        }
    }

    private void highlightArcs() {
        for (ArcModel arc : arcs) {
            arc.setHighlighted(true);
        }
    }

    /**
     * Handles the mouse events related to the label
     */
    private class LabelClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            removeHighlighting();
            addHighlighting();
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            setCursor(HAND_CURSOR);
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            setCursor(DEFAULT_CURSOR);
        }
    }
}