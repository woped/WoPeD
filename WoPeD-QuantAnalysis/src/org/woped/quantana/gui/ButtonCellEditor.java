package org.woped.quantana.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 300L;

	JButton button;
	protected static final String EDIT = "edit";

	public ButtonCellEditor() {
		button = new JButton("...");
		button.setActionCommand(EDIT);
		button.addActionListener(this);
//		button.setBorderPainted(false);

		//Set up the dialog that the button brings up.

	}

	/**
	 * Handles events from the editor button and from
	 * the dialog's OK button.
	 */
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			

			//Make the renderer reappear.
			fireEditingStopped();

		} else { //User pressed dialog's "OK" button.

		}
	}

	//Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue() {
//		return button;
		
		return null;
	}

	//Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent(JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column) {
		
		return null;
	}
}
