/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

public class Action extends SpecifiedElement{
	
	private String f_baseForm;
	
	private Actor f_actorFrom = null;
	private ExtractedObject f_object = null;
	
	private Action f_xcomp = null;
	

	private String f_prt = null;
	private String f_cop = null;
	private int f_copIndex = -1;
	private String f_aux = null;
		
	private String f_mod = null; //modifier e.g. leave alone
	private int f_modPos = -1;
	
	private String f_marker = null;
	private boolean f_markerFromPP = false;;
	private String f_preAdvMod = null;
	private int f_preAdvModPos = -1;
	private String f_prepc = null;
	private boolean f_negated;
	
	private Action f_link = null;
	private ActionLinkType f_linkType = null;
	private boolean f_transient;


	private String finalLabel;
	
	public enum ActionLinkType{
		FORWARD,
		JUMP,
		LOOP,
		NONE
	}
	
	/**
	 * 
	 */
	public Action(T2PSentence origin, int wordInSentece, String verb) {
		super(origin,wordInSentece,verb);
	}

	public void setBaseForm(String verbBaseForm){
		f_baseForm = verbBaseForm;
	}

	public Action clone() {
		Action _clone = new Action(getOrigin(),getWordIndex(),getName());
		_clone.f_baseForm = f_baseForm;
		_clone.f_actorFrom = f_actorFrom;
		_clone.f_object = f_object;
		
		_clone.f_prt = f_prt;
		_clone.f_cop = f_cop;
		_clone.f_copIndex = f_copIndex;
		_clone.f_aux = f_aux;
		_clone.f_xcomp = f_xcomp;
		
		_clone.f_mod = f_mod;
		_clone.f_modPos = f_modPos;
		
		_clone.f_preAdvMod = f_preAdvMod;
		_clone.f_preAdvModPos = f_preAdvModPos;
		_clone.f_marker = f_marker;
		_clone.f_markerFromPP = f_markerFromPP;
		_clone.f_negated = f_negated;
		_clone.f_link = f_link;
		for(Specifier s:this.getSpecifiers()) {
			_clone.addSpecifiers(s);
		}		
		return _clone;
	}
	
	/**
	 * @return the f_actorFrom
	 */
	public Actor getActorFrom() {
		return f_actorFrom;
	}

	/**
	 * @return the f_actorTo
	 */
	public ExtractedObject getObject() {
		return f_object;
	}

	/**
	 * returns the root form of the verb
	 * @return the f_verb
	 */
	public String getVerb() {
		return f_baseForm;
	}

	/**
	 * @param from the f_actorFrom to set
	 */
	public void setActorFrom(Actor from) {
		f_actorFrom = from;
	}

	/**
	 * @param to the f_actorTo to set
	 */
	public void setObject(ExtractedObject to) {
		f_object = to;
	}
	
	@Override
	public String toString() {
		StringBuilder _b = new StringBuilder();
		if(f_aux != null) {
			_b.append("("+f_aux+")");
			_b.append(" ");
		}
		if(f_negated) {
			_b.append("!");
		}
		_b.append(getName());
		if(f_prt != null) {
			_b.append(" ");
			_b.append("["+f_prt+"]");
		}
		if(f_cop != null) {
			_b.append(" ");
			_b.append("["+f_cop+"]");
		}
		if(f_mod != null) {
			_b.append(" ");
			_b.append("["+f_mod+"]");
		}
		if(f_marker != null) {
			_b.append("---marker:"+f_marker+"-"+f_preAdvMod+"-"+f_prepc+"---");
		}
		return "Action - "+super.toString(null,_b.toString());
	}
	
	private String toFullString(String prefix) {
		StringBuffer _b = new StringBuffer();
		_b.append(prefix+toString());_b.append("\n");
		if(getXcomp() != null) {
			_b.append(prefix+getXcomp().toFullString("\t"));_b.append("\n");
		}
		_b.append(prefix+"FROM: "+f_actorFrom); _b.append("\n");
		_b.append(prefix+"TO: "+f_object); 
		return _b.toString();
	}
	
	public String toFullString() {
		return toFullString("");
	}

	public void setXcomp(Action xcomp) {
		this.f_xcomp = xcomp;
	}

	public Action getXcomp() {
		return f_xcomp;
	}

	public void setAux(String f_aux) {
		this.f_aux = f_aux;
	}

	public String getAux() {
		return f_aux;
	}
	
	/**
	 * @param _marker
	 */
	public void setMarker(String marker) {
		f_marker = marker;
	}

	public String getMarker() {
		return f_marker;
	}
	
	public void setMod(String f_mod) {
		this.f_mod = f_mod;
	}

	public String getMod() {
		return f_mod;
	}

	/**
	 * @param negated
	 */
	public void setNegated(boolean negated) {
		f_negated = negated;
	}
	
	public boolean isNegated() {
		return f_negated;
	}

	public void setLink(Action link) {
		this.f_link = link;
	}

	public Action getLink() {
		return f_link;
	}

	public void setCop(String cop,int copIndex) {
		this.f_cop = cop;
		this.f_copIndex = copIndex;
	}

	public String getCop() {
		return f_cop;
	}
	
	public int getCopIndex() {
		return f_copIndex;
	}

	public void setPreAdvMod(String preAdvMod,int posInSentence) {
		if(f_preAdvMod == null || f_preAdvModPos > posInSentence) {
			this.f_preAdvMod = preAdvMod;
			f_preAdvModPos = posInSentence;
		}
	}

	public String getPreAdvMod() {
		return f_preAdvMod;
	}

	public void setPrepc(String prepc) {
		this.f_prepc = prepc;
	}

	public String getPrepc() {
		return f_prepc;
	}

	public void setModPos(int modPos) {
		this.f_modPos = modPos;
	}

	public int getModPos() {
		return f_modPos;
	}

	public void setPrt(String prt) {
		this.f_prt = prt;
	}

	public String getPrt() {
		return f_prt;
	}

	/**
	 * @param b
	 */
	public void setMarkerFromPP(boolean value) {
		f_markerFromPP = value;
	}
	
	public boolean isMarkerFromPP() {
		return f_markerFromPP;
	}

	public void setLinkType(ActionLinkType linkType) {
		this.f_linkType = linkType;
	}

	public ActionLinkType getLinkType() {
		return f_linkType;
	}

	/**
	 * @param b
	 */
	public void setTransient(boolean value) {
		f_transient = value;
	}
	
	/**
	 * tells e.g. the ProcessModelBuilder, that this node
	 * is not directly used in the flow creation (e.g. in case of a JUMP)
	 * and no corresponding ProcessNode is needed.
	 * @return
	 */
	public boolean getTransient() {
		return f_transient;
	}

	public String getFinalLabel() {
		return finalLabel;
	}

	public void setFinalLabel(String finalLabel) {
		this.finalLabel = finalLabel;
	}
	
}
