/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import text.T2PSentence;

public class OriginatedElement {
	
	private T2PSentence f_origin;
	
	/**
	 * 
	 */
	public OriginatedElement(T2PSentence origin) {
		f_origin = origin;
	}

	
	/**
	 * returns the sentence from which this Action was extracted
	 * @return the f_origin
	 */
	public T2PSentence getOrigin() {
		return f_origin;
	}
}
