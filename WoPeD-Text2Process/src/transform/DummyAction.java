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

	private static int id=1;
	private int dummyID;


	public DummyAction(Action action) {
		super(action.getOrigin(), action.getWordIndex()+1, "Dummy Node");
		setBaseForm("Dummy Node");
		dummyID=id;
		id++;
	}

	public DummyAction() {
		super(null, -1, "Dummy Node");
		setBaseForm("Dummy Node");
	}

	public int getDummyID() {
		return dummyID;
	}

	public static void resetStaticContext(){
	    id=0;
    }
	
}
