package dataModel.dsynt;


import org.w3c.dom.Document;

import dataModel.intermediate.ExecutableFragment;

public abstract class DSynTSentence {
	
	protected Document doc;
	protected ExecutableFragment eFrag;

	public static final int TYPE_IF = 0;
	public static final int TYPE_AS_LONG_AS = 1;
	public static final int TYPE_ONCE = 2;
	public static final int TYPE_WHETHER = 3;
	public static final int TYPE_NONE = 4;
	public static final int TYPE_WHEN = 5;
	
	public boolean bo_replaceWithPronoun = false; //	private boolean boIsPronoun = false;
	public boolean bo_isSubject = false;//	private boolean boIsSubject = false;
	public boolean bo_isPlural = false;	//	private boolean boIsPlural = false;
	public boolean bo_hasArticle = true; //	private boolean boHasArticle = true;
	public boolean bo_hasIndefArticle = false; //	private boolean boHasIndefArticle = false;
	
	public boolean verb_isPassive = false;//	private boolean verbIsPassive = false;
	public boolean verb_isParticiple = false; //	private boolean verbIsParticiple = false;
	public boolean verb_isNegated = false;	//	private boolean negation = false;
	public boolean verb_isPast = false; //	private boolean isPast = false;
	
	public boolean sen_isCoord = true;//	private boolean isCoord = true;
	public boolean sen_hasConnective = false;
	public boolean sen_hasBullet = false;
	
	public Document getDSynT() {
		return doc;
	}
	
	public ExecutableFragment getExecutableFragment() {
		return eFrag;
	}
	
//	public void mapFragmentAttributes(AbstractFragment f) {
//		bo_replaceWithPronoun = f.boIsPronoun;
//		bo_isSubject = f.boIsSubject;
//		bo_isPlural =f.boIsPlural;
//		bo_hasArticle  = f.boHasArticle;
//		bo_hasIndefArticle = f.boHasIndefArticle;
//		
//		verb_isPassive = f.verbIsPassive;
//		verb_isParticiple = f.verbIsParticiple;
//		verb_isNegated = f.negation;
//		verb_isPast = f.isPast;
//		
//		sen_isCoord = f.isCoord ;
//		sen_hasConnective = false;
//		
//	}

}
