/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import TextToWorldModel.transform.ConjunctionElement.ConjunctionType;

public class DetermineConjResult {
	
	private ConjunctionType f_type;
	private DLRStatusCode f_statusCode;
	
	public enum DLRStatusCode {
		NOT_CONTAINED, //0
		ACTION, //1
		ACTOR_SUBJECT,
		ACTOR_OBJECT,
		RESOURCE
	}
	
	/**
	 * 
	 */
	public DetermineConjResult(ConjunctionType type, DLRStatusCode status) {
		this.setType(type);
		setStatusCode(status);
	}

	public void setType(ConjunctionType type) {
		this.f_type = type;
	}

	public ConjunctionType getType() {
		return f_type;
	}

	public void setStatusCode(DLRStatusCode statusCode) {
		this.f_statusCode = statusCode;
	}

	/**
	 * returns a status code to show the kind of relationship
	 * 0 - not contained
	 * 1 - contained as action
	 * 2 - contained as actor (subject)
	 * 3 - contained as actor (object)
	 * 4 - contained as resource 
	 * @param part
	 * @param resultList
	 * @return
	 */
	public DLRStatusCode getStatusCode() {
		return f_statusCode;
	}

}
