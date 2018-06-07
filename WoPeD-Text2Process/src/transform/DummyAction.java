/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package transform;

import worldModel.Action;

public class DummyAction extends Action{

	/**
	 * @param action 
	 * @param origin
	 * @param wordInSentece
	 * @param verb
	 */
	public DummyAction(Action action) {
		super(action.getOrigin(), action.getWordIndex()+1, "Dummy Node");
		setBaseForm("Dummy Node");
	}

	public DummyAction() {
		super(null, -1, "Dummy Node");
		setBaseForm("Dummy Node");
	}

	
}
