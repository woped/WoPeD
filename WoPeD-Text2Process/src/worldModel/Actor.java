/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

public class Actor extends ExtractedObject{
	
	
	private boolean f_unreal = false;
	private boolean f_metaActor = false;
	private boolean f_passive = false;;

	/**
	 * 
	 */
	public Actor(T2PSentence origin,int wordInSentence, String name) {
		super(origin,wordInSentence,name);
	}
	
	@Override
	public String toString() {
		return (f_metaActor ? "Meta-":(f_unreal ? "Unreal-":""))+ "Actor - "+super.toString();
	}
	

	/**
	 * @param b
	 */
	public void setUnreal(boolean value) {
		f_unreal  = value;
	}
	
	public boolean isUnreal() {
		return f_unreal;
	}

	public void setMetaActor(boolean metaActor) {
		this.f_metaActor = metaActor;
	}

	public boolean isMetaActor() {
		return f_metaActor;
	}

	/**
	 * @param b
	 */
	public void setPassive(boolean value) {
		f_passive  = value;
	}
	
	/**
	 * tells whether this Actor was found in a
	 * passive or active sentence
	 * @return
	 */
	public boolean getPassive() {
		return f_passive;
	}

}
