/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import java.util.List;

import worldModel.Specifier.SpecifierType;

/**
 * data object or object of an action, the outcome
 *
 */
public class Resource extends ExtractedObject {
		

	public Resource(T2PSentence origin, int wordInSentence, String word) {
		super(origin, wordInSentence,word.toLowerCase());
	}


	/**
	 * returns the full resource name, including NN specifiers in the front of it
	 * @return
	 */
	public String getCompoundResourceName() {
		//print nns now
		StringBuilder _b = new StringBuilder();
		List<Specifier> _nns = getSpecifiers(SpecifierType.NN);
		for(Specifier s:_nns) {
			_b.append(s.getPhrase());
			_b.append(" ");
		}	
		_b.append(getName());
		return _b.toString();
	}
	
	
	
	@Override
	public String toString() {
		return "Resource - "+super.toString();
	}
	
}
