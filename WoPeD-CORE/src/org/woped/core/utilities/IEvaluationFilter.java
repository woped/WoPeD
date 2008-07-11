package org.woped.core.utilities;

import javax.swing.JTextField;

public interface IEvaluationFilter {
	
	/**
	 * You must save this parameter in your implementation. This Method will be run
	 * when you are added the specific evaluation filter to an object of the class
	 * JTextFieldEvalution. 
	 * @param TextField
	 */
	public void setJTextFieldSource(JTextField TextField);
	public boolean testInput();
	public void doingByFault();
	public void doingByAccept();

}
