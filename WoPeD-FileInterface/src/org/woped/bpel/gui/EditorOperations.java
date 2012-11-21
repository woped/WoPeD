package org.woped.bpel.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.woped.bpel.BPEL;
import org.woped.core.controller.IEditor;

/**
 * @author Lavinia Posler
 * 
 * This is the class for BPEL Preview.
 * 
 * Created on 15.01.2008
 */

public class EditorOperations extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2080496591289814680L;

	private JTextArea _textarea = null;
	
	public EventListenerEditorOperations _listener = null;

	private IEditor editor;

	public IEditor getEditor() {
		return editor;
	}

	public EditorOperations(IEditor editor) {
		this.editor = editor;
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.BOTH;

		this._textarea = this.createTextArea();
		JScrollPane scroll = new JScrollPane();
		scroll.setHorizontalScrollBar(scroll.createHorizontalScrollBar());
		scroll.setVerticalScrollBar(scroll.createVerticalScrollBar());
		scroll.setViewportView(this._textarea);

		this.add(scroll, c);
		this.setFocusable(true);
		this.addFocusListener(this.getEventListener());
		this.addComponentListener(this.getEventListener());
		this.setVisible(true);
	}

	private JTextArea createTextArea() {
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setVisible(true);
		return text;

	}

	public EventListenerEditorOperations getEventListener() {
		if(this._listener == null)
			this._listener = new EventListenerEditorOperations(this);
		return this._listener;
	}
	
	public void setTextOnPreview()
	{
		String text = BPEL.getBPELMainClass().genPreview(this.editor);
		this._textarea.setText(text);	
	}

}

class EventListenerEditorOperations implements FocusListener,ChangeListener,java.awt.event.ComponentListener {

	EditorOperations _adaptee;
	public EventListenerEditorOperations(EditorOperations Adaptee) {
		this._adaptee = Adaptee;
	}

	public void focusGained(FocusEvent e) {
		this._adaptee.setTextOnPreview();
	}

	public void focusLost(FocusEvent e) {
	}

	public void stateChanged(ChangeEvent e) {
		this._adaptee.setTextOnPreview();
		
	}

	public void componentHidden(ComponentEvent e) {
		
	}

	public void componentMoved(ComponentEvent e) {
		
	}

	public void componentResized(ComponentEvent e) {
		
	}

	public void componentShown(ComponentEvent e) {
		this._adaptee.setTextOnPreview();
		
	}
}
