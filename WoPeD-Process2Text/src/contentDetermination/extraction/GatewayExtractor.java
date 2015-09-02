package contentDetermination.extraction;

import java.util.HashMap;

import textPlanning.recordClasses.ModifierRecord;
import contentDetermination.labelAnalysis.EnglishLabelHelper;
import de.hpi.bpt.process.Node;

public class GatewayExtractor {
	
	Node gateway;
	private String verb;
	private String bo;
	private HashMap<String, ModifierRecord> modList;
	private EnglishLabelHelper lHelper;
	
	public boolean bo_isPlural = false;
	public boolean bo_hasArticle = true;
	public boolean hasVerb = true;
	
	public GatewayExtractor(Node gateway, EnglishLabelHelper lHelper) {
		this.lHelper = lHelper;
		this.gateway = gateway;
		processGateway(gateway);
	}
	
	private void reset() {
		this.verb =  "";
		this.modList = new HashMap<String, ModifierRecord>();
		this.bo = "";
		modList.clear();
		 bo_isPlural = false;
		 bo_hasArticle = true;
	}
	
	private void processGateway(Node gateway) {
		// Reset variables
		reset();
		
		// Get name and check if gateway is labeled
		String s = gateway.getName().toLowerCase();
		if (s.equals("")) {
			return;
			
		// Gateway has label	
		} else {
			s= s.replace("?", "");
			String[] sSplit = s.split(" ");
			boolean extracted = false;
			
			if (extracted == false && sSplit.length == 1 && lHelper.isVerb(sSplit[0])) {
				verb = lHelper.getInfinitiveOfAction(sSplit[0]);
				extracted = true;
			}
			
			if (extracted == false && sSplit.length == 2 && lHelper.isNoun(sSplit[0]) && sSplit[1].equals("availble")) {
				verb = "be";
				bo = lHelper.getSingularOfNoun(sSplit[0]);
				extracted = true;
			}
			
			if (extracted == false && sSplit.length == 2 && lHelper.isNoun(sSplit[0]) && sSplit[1].endsWith("ed") && lHelper.isVerb(sSplit[1])) {
				verb = lHelper.getInfinitiveOfAction(sSplit[1]);
				bo = lHelper.getSingularOfNoun(sSplit[0]);
				extracted = true;
			}
			
			// Pattern 1: <V> <NN>? (e.g. Accept order?)
			if (extracted == false && sSplit.length == 2 && lHelper.isVerb(sSplit[0]) && lHelper.isNoun(sSplit[1])) {
				verb = lHelper.getInfinitiveOfAction(sSplit[0]);
				bo = lHelper.getSingularOfNoun(sSplit[1]);
				extracted = true;
			}
			
			// Pattern 2: <ADJ> <ADJ>? (e.g. available in-house?)
			if (extracted == false && sSplit.length == 2 && lHelper.isAdjective(sSplit[0]) && lHelper.isAdjective(sSplit[1])) {
				verb = "be";
				bo = "";
				modList.put(sSplit[0],new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB));
				modList.put(sSplit[1],new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB));
				extracted = true;
			}
			
			// Pattern 3: Any <NN> <V> <PARTICIPLE>? (e.g. any part left unchecked?)
			if (extracted == false && sSplit.length == 4 && sSplit[0].equals("any") && lHelper.isVerb(sSplit[2])) {
				verb = lHelper.getInfinitiveOfAction(sSplit[2]);
				bo = lHelper.getSingularOfNoun(sSplit[1]);
				
				ModifierRecord mr = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
				mr.addAttribute("rheme", "+");
				modList.put(sSplit[3],mr);
				
				ModifierRecord mr2 = new ModifierRecord(ModifierRecord.TYPE_QUANT, ModifierRecord.TARGET_ROLE);
				modList.put("ANY",mr2);
				extracted = true;
			}
			
			// Pattern 4: <NN> <V> <PARTICIPLE>? (e.g. alcoholic drinks ordered?)
			if (extracted == false && lHelper.isVerb(sSplit[0]) == false && sSplit.length > 2 && lHelper.isVerb(sSplit[2]) && sSplit[1].equals("of") == false) {
				verb = lHelper.getInfinitiveOfAction(sSplit[sSplit.length-1]);
				String n = "";
				for (int i = 0; i< (sSplit.length - 1); i++) {
					n = n + " " + sSplit[i];
				}
				n = n.trim();
				bo = n.substring(0, n.length()-1);
				String lastNoun = sSplit[sSplit.length-2].trim();
				if (lastNoun.endsWith("s") && lHelper.isNoun(lastNoun.substring(0, lastNoun.length()-1))) {
					bo_isPlural = true;
					bo_hasArticle = false;
					System.out.println("YES");
				}
				extracted = true;
			}
			
			// Pattern 5: <auxiliary verb> <verb participle> <NN>
			if (extracted == false && sSplit.length >= 2 && lHelper.isVerb(sSplit[0]) && lHelper.isVerb(sSplit[1])) {
				
				verb = lHelper.getInfinitiveOfAction(sSplit[1]);
				String n = "";
				for (int i = 2; i< (sSplit.length); i++) {
					n = n + " " + sSplit[i];
				}
				n = n.trim();
				bo = n;
				extracted = true;
			}
			
			// Pattern 6: <NN> of <NN>
			if (extracted == false && sSplit.length == 3 && lHelper.isVerb(sSplit[0]) == false && sSplit[1].equals("of")) {
//				verb = "be";
				String n = "";
				for (int i = 0; i< (sSplit.length); i++) {
					n = n + " " + sSplit[i];
				}
				n = n.trim();
				n = "of " + n;
				bo = n;
				String lastNoun = sSplit[sSplit.length-1];
				if (lastNoun.endsWith("s") && lHelper.isNoun(lastNoun.substring(0, lastNoun.length()-1))) {
					bo_hasArticle = false;
					hasVerb = false;
				}
				extracted = true;
			}
			
//			if (lHelper.isAdjective(bo.split(" ")[0]) || lHelper.isAdverb(sSplit[0])) {
//				System.out.println(s);
//			}
			
			if (extracted == false) {
				System.out.println("GatewayExtractor: Extraction pattern not covered: " + s);
			}
		}
	}
	
	public void negateGatewayLabel() {
		
		// Reset variables
		reset();
		
		// Get name and check if gateway is labeled
		String s = gateway.getName().toLowerCase();
		if (s.equals("")) {
			return;
			
		// Gateway has label	
		} else {
			
			s= s.replace("?", "");
			String[] sSplit = s.split(" ");
			boolean extracted = false;
			
			if (extracted == false && sSplit.length == 2 && lHelper.isNoun(sSplit[0]) && sSplit[1].endsWith("ed") && lHelper.isVerb(sSplit[1])) {
				verb = lHelper.getInfinitiveOfAction(sSplit[1]);
				bo = lHelper.getSingularOfNoun(sSplit[0]);
				
				ModifierRecord mr2 = new ModifierRecord(ModifierRecord.TYPE_QUANT, ModifierRecord.TARGET_ROLE);
				modList.put("no",mr2);
				extracted = true;
			}
		
			if (extracted == false && sSplit.length == 4 && sSplit[0].equals("any") && lHelper.isVerb(sSplit[2])) {
				verb = lHelper.getInfinitiveOfAction(sSplit[2]);
				bo = lHelper.getSingularOfNoun(sSplit[1]);
				
				ModifierRecord mr = new ModifierRecord(ModifierRecord.TYPE_ADV, ModifierRecord.TARGET_VERB);
				mr.addAttribute("rheme", "+");
				modList.put(sSplit[3],mr);
				
				ModifierRecord mr2 = new ModifierRecord(ModifierRecord.TYPE_QUANT, ModifierRecord.TARGET_ROLE);
				modList.put("no",mr2);
				extracted = true;
			}
		}	
	}
	
	public String getVerb() {
		return verb;
	}
	
	public HashMap<String, ModifierRecord> getModList() {
		return modList;
	}
	
	public String getObject() {
		return bo;
	}
}
