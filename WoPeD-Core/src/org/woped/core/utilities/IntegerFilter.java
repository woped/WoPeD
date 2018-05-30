/**
 * 
 */
package org.woped.core.utilities;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * @author Frank Schüler
 * 
 */
public class IntegerFilter implements IEvaluationFilter {

	private JTextField _source = null;
	private int _min = Integer.MIN_VALUE;
	private int _max = Integer.MAX_VALUE;
	private Color _bgcolor;

	/**
	 * 
	 */
	public IntegerFilter(int Min, int Max) {
		this._min = Min;
		this._max = Max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.core.utilities.IEvaluationFilter#setJTextFieldSource(javax.swing.JTextField)
	 */
	public void setJTextFieldSource(JTextField TextField) {
		this._source = TextField;
		this._bgcolor = this._source.getBackground();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.core.utilities.IEvaluationFilter#doingByAccept()
	 */
	public void doingByAccept(KeyEvent e) {
		this._source.setBackground(this._bgcolor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.core.utilities.IEvaluationFilter#doingByFault()
	 */
	public void doingByFault(KeyEvent e) {
		this._source.setBackground(Color.RED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.core.utilities.IEvaluationFilter#testInput()
	 */
	public boolean testInput(KeyEvent e) {
		if(this._source.getText().length() == 0) return false;
		try {
			int val = Integer.parseInt(this._source.getText());
			if (val >= this._min && val <= this._max)
				return true;
		} catch (NumberFormatException ex) {
			return false;
		}
		return false;
	}

}
