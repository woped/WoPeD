/**
 * 
 */
package org.woped.core.utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

/**
 * @author Frank Schüler
 * 
 * Discription
 * 
 * This Class can be used to evaluate input on a JTextfield
 * 
 */
public class JTextFieldEvalution implements KeyListener {

	private JTextField _adaptee;
	private IEvaluationFilter _evaluateByKeyPressed = null;
	private IEvaluationFilter _evaluateByKeyReleased = null;
	private IEvaluationFilter _evaluateByKeyTyped = null;
	private boolean _acceptedInput = false;

	/**
	 * 
	 */
	public JTextFieldEvalution(JTextField Adaptee,
			IEvaluationFilter EvaluateByKeyPressed,
			IEvaluationFilter EvaluateByKeyReleased,
			IEvaluationFilter EvaluateByKeyTyped) {
		this._adaptee = Adaptee;
		this.setEvaluateByKeyPressed(EvaluateByKeyPressed);
		this.setEvaluateByKeyReleased(EvaluateByKeyReleased);
		this.setEvaluateByKeyTyped(EvaluateByKeyTyped);
	}
	
	/**
	 * 
	 * @param EvaluateByKeyPressed
	 */
	public void setEvaluateByKeyPressed(IEvaluationFilter EvaluateByKeyPressed)
	{
		if(EvaluateByKeyPressed != null)
		{
			this._evaluateByKeyPressed = EvaluateByKeyPressed;
			this._evaluateByKeyPressed.setJTextFieldSource(this._adaptee);
		}
	}
	
	/**
	 * 
	 * @param EvaluateByKeyReleased
	 */
	public void setEvaluateByKeyReleased(IEvaluationFilter EvaluateByKeyReleased)
	{
		if(EvaluateByKeyReleased != null)
		{
			this._evaluateByKeyReleased = EvaluateByKeyReleased;
			this._evaluateByKeyReleased.setJTextFieldSource(this._adaptee);
		}
	}
	
	/**
	 * 
	 * @param EvaluateByKeyTyped
	 */
	public void setEvaluateByKeyTyped(IEvaluationFilter EvaluateByKeyTyped)
	{
		if(EvaluateByKeyTyped != null)
		{
			this._evaluateByKeyTyped = EvaluateByKeyTyped;
			this._evaluateByKeyTyped.setJTextFieldSource(this._adaptee);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public final void keyPressed(KeyEvent e) {
		if (this._evaluateByKeyPressed == null)
			return;
		this._acceptedInput = this._evaluateByKeyPressed.testInput(e);
		this.doing(this._evaluateByKeyPressed, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public final void keyReleased(KeyEvent e) {
		if (this._evaluateByKeyReleased == null)
			return;
		this._acceptedInput = this._evaluateByKeyReleased.testInput(e);
		this.doing(this._evaluateByKeyReleased,e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public final void keyTyped(KeyEvent e) {
		if (this._evaluateByKeyTyped == null)
			return;
		this._acceptedInput = this._evaluateByKeyTyped.testInput(e);
		this.doing(this._evaluateByKeyTyped,e);
	}
	
	/**
	 * 
	 * @param filter
	 */
	private final void doing(IEvaluationFilter filter, KeyEvent e)
	{
		if(this._acceptedInput) filter.doingByAccept(e);
		else filter.doingByFault(e);
	}
	
	/**
	 * 
	 * @return
	 */
	public final boolean isInputAccepted()
	{
		return this._acceptedInput;
	}

}
