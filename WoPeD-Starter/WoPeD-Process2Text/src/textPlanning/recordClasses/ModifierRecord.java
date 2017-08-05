package textPlanning.recordClasses;

import java.util.ArrayList;

import com.sun.tools.javac.util.Pair;

public class ModifierRecord {

	public static final int TARGET_VERB = 0;
	public static final int TARGET_ROLE = 1;
	public static final int TARGET_BO = 2;
	public static final int TARGET_ADD = 3;
	
	public static final int TYPE_ADV = 0;
	public static final int TYPE_ADJ = 1;
	public static final int TYPE_QUANT = 2;
	public static final int TYPE_PREP = 3;
	
	private int type;
	private int target;
	private ArrayList<Pair<String,String>> attributes;
	private String lemma;
	
	public ModifierRecord(int type, int target) {
		this.type = type;
		this.target = target;
		attributes = new ArrayList<Pair<String,String>>();
	}
	
	public void addAttribute(String attName, String attValue) {
		attributes.add(new Pair(attName, attValue));
	}
	
	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public int getType() {
		return type;
	}
	
	public int getTarget() {
		return target;
	}

	public ArrayList<Pair<String, String>> getAttributes() {
		return attributes;
	}
}
