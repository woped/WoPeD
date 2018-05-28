/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package etc;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {
	
	public static final String CONF_GENERATE_BB_POOLS = "T2P_GenerateBBPools";
	public static final String CONF_GENERATE_DATA_OBJECTS = "T2P_GenerateDO";
	public static final String CONF_GENERATE_ADD_UNKNOWN_PT = "T2P_GenerateAdUnknownPT";
	public static final String CONF_GENERATE_REMOVE_LOW_ENT_NODES = "T2P_GenerateRemLowEntNodes";
	public static final String CONF_GENERATE_HIGHLIGHT_META_ACTIONS = "T2P_GenerateHighlightMetaActors";
	public static final String CONF_LANESO_ALWAYS_CREATE_MSG_EVT = "T2P_SplitOff_AlwaysMsgEvt";
	public static final String CONF_LANESO_CREATE_EXTRA_COMM_LINKS = "T2P_SplitOff_ExtraCommLinks";
    public static String CONF_GENERATE_IGNORE_SBAR_ON_NP = "T2P_GenerateIgnoreSBAROnNP";

    /* switches for debug output*/
	public static final boolean DEBUG_FINAL_ACTIONS_RESULT = true;
	public static final boolean DEBUG_MARKING = true;
	public static final boolean DEBUG_REFERENCE_RESOLUTION = true;
	public static final boolean DEBUG_EXTRACTION = true;
	public static final boolean DEBUG_EXTRACTION_FINAL = true;
	public static final boolean DEBUG_FRAME_ASSIGNMENT = true;
//	public static final boolean DEBUG_FINAL_ACTIONS_RESULT = true;
//	public static final boolean DEBUG_MARKING = true;
//	public static final boolean DEBUG_REFERENCE_RESOLUTION = true;
//	public static final boolean DEBUG_EXTRACTION = true;
//	public static final boolean DEBUG_EXTRACTION_FINAL = true;
//	public static final boolean DEBUG_FRAME_ASSIGNMENT = true;
	
	public static ArrayList<String> f_relativeResolutionTags = new ArrayList<String>(2);
	public static ArrayList<String> f_relativeResolutionWords = new ArrayList<String>(2);
	
	public static ArrayList<String> f_acceptedAMODforLoops = new ArrayList<String>(3);
	public static ArrayList<String> f_acceptedForForwardLink = new ArrayList<String>(3);
	
	
	public static ArrayList<String> f_personCorrectorList = new ArrayList<String>(15);
	
	public static ArrayList<String> f_wantedDeterminers = new ArrayList<String>(4);
	
	public static ArrayList<String> f_metaActorsDeterminers = new ArrayList<String>(2);
	public static ArrayList<String> f_realActorDeterminers = new ArrayList<String>(3);
	public static ArrayList<String> f_dataObjectDeterminers = new ArrayList<String>(3);
	public static ArrayList<String> f_frequencyWords = new ArrayList<String>(8);

	public static ArrayList<String> f_weakVerbs = new ArrayList<String>(5);
	public static HashMap<String,String> f_weakVerbTo3rdPSing = new HashMap<String, String>();

	public static ArrayList<String> f_conditionIndicators = new ArrayList<String>(8);
	public static ArrayList<String> f_parallelIndicators = new ArrayList<String>(5);
	public static ArrayList<String> f_sequenceIndicators = new ArrayList<String>(4);
	public static ArrayList<String> f_exceptionIndicators = new ArrayList<String>(1);
	
	public static ArrayList<String> f_exampleIndicators = new ArrayList<String>(4);
	
	public static ArrayList<String> f_realActorPPIndicators = new ArrayList<String>(2);
	public static ArrayList<String> f_interactionVerbs  = new ArrayList<String>(5);

	
	static {
		f_personCorrectorList.add("resource provisioning");
		f_personCorrectorList.add("customer service");
		f_personCorrectorList.add("support");
		f_personCorrectorList.add("support office");
		f_personCorrectorList.add("support officer");
		f_personCorrectorList.add("client service back office");
		f_personCorrectorList.add("master");
		f_personCorrectorList.add("masters");
		f_personCorrectorList.add("assembler ag");
		f_personCorrectorList.add("acme ag");
		f_personCorrectorList.add("acme financial accounting");
		f_personCorrectorList.add("secretarial office");
		f_personCorrectorList.add("office");
		f_personCorrectorList.add("registry");
		f_personCorrectorList.add("head");
		f_personCorrectorList.add("storehouse");
		f_personCorrectorList.add("atm");
		f_personCorrectorList.add("crs");
		f_personCorrectorList.add("company");
		f_personCorrectorList.add("garage");
		f_personCorrectorList.add("kitchen");
		f_personCorrectorList.add("department");
		f_personCorrectorList.add("ec");
		f_personCorrectorList.add("sp");
		f_personCorrectorList.add("mpo");
		f_personCorrectorList.add("mpoo");
		f_personCorrectorList.add("mpon");
		f_personCorrectorList.add("msp");
		f_personCorrectorList.add("mspo");
		f_personCorrectorList.add("mspn");
		f_personCorrectorList.add("go");
		f_personCorrectorList.add("pu");
		f_personCorrectorList.add("ip");
		f_personCorrectorList.add("inq");
		f_personCorrectorList.add("sp\\/pu\\/go");
		f_personCorrectorList.add("fault detector");
		
		
		f_relativeResolutionTags.add("DT");
		f_relativeResolutionTags.add("PRP");
		f_relativeResolutionTags.add("WP");
		
		f_relativeResolutionWords.add("someone");
		//f_relativeResolutionWords.add("somebody");

		//edited by jana
		f_acceptedAMODforLoops.add("next");
		f_acceptedAMODforLoops.add("back");
		f_acceptedAMODforLoops.add("again");
		f_acceptedAMODforLoops.add("following");
		f_acceptedAMODforLoops.add("once again");
		f_acceptedAMODforLoops.add("adjacent");
		f_acceptedAMODforLoops.add("side by side");
		f_acceptedAMODforLoops.add("future");
		f_acceptedAMODforLoops.add("succeeding");
		f_acceptedAMODforLoops.add("hind");
		f_acceptedAMODforLoops.add("hinder");
		f_acceptedAMODforLoops.add("backwards");
		f_acceptedAMODforLoops.add("backward");
		f_acceptedAMODforLoops.add("rearward");
		f_acceptedAMODforLoops.add("rearwards");
		f_acceptedAMODforLoops.add("once more");
		f_acceptedAMODforLoops.add("over again");






		//edited by jana
		f_acceptedForForwardLink.add("finally");
		f_acceptedForForwardLink.add("eventually");
		f_acceptedForForwardLink.add("ultimately");
		f_acceptedForForwardLink.add("in the end");
		f_acceptedForForwardLink.add("at last");
		f_acceptedForForwardLink.add("at long last");
		f_acceptedForForwardLink.add("lastly");
		f_acceptedForForwardLink.add("in conclusion");

		
		f_wantedDeterminers.add("a");
		f_wantedDeterminers.add("an");
		f_wantedDeterminers.add("no");
		f_wantedDeterminers.add("the");
		
		f_realActorDeterminers.add("person");
		f_realActorDeterminers.add("social_group");
		f_realActorDeterminers.add("software system");

		//edited by jana
		f_dataObjectDeterminers.add("written_material");
		f_dataObjectDeterminers.add("record");
		f_dataObjectDeterminers.add("message");
		f_dataObjectDeterminers.add("design");
		f_dataObjectDeterminers.add("sheet");
		f_dataObjectDeterminers.add("data");
		f_dataObjectDeterminers.add("list");
		f_dataObjectDeterminers.add("email");
		f_dataObjectDeterminers.add("file");
		
		f_metaActorsDeterminers.add("step");
		f_metaActorsDeterminers.add("process");
		f_metaActorsDeterminers.add("case");
		f_metaActorsDeterminers.add("state");
			
		//do not consider definite times (always, never, ever)
		//taken from Swan2005
		f_frequencyWords.add("usually");
		f_frequencyWords.add("normally");
		f_frequencyWords.add("often"); 
		f_frequencyWords.add("frequently");
		f_frequencyWords.add("sometimes");
		f_frequencyWords.add("occasionally");
		f_frequencyWords.add("rarely"); 
		f_frequencyWords.add("seldom"); 

		f_weakVerbs.add("be");
		f_weakVerbs.add("have");
		f_weakVerbs.add("do");
		f_weakVerbs.add("achieve");
		f_weakVerbs.add("start");
		f_weakVerbs.add("exist");
		f_weakVerbs.add("base");

		f_weakVerbTo3rdPSing.put("be", "is");
		f_weakVerbTo3rdPSing.put("have", "has");
		f_weakVerbTo3rdPSing.put("do", "does");

		// exclusive gateways (XOR)
		f_conditionIndicators.add("if");
		f_conditionIndicators.add("whether");
		f_conditionIndicators.add("in case of");
		f_conditionIndicators.add("in the case of");
		f_conditionIndicators.add("in case");
		f_conditionIndicators.add("for the case");
		f_conditionIndicators.add("whereas");
		f_conditionIndicators.add("otherwise");
		f_conditionIndicators.add("optionally");
		// Author: Simon
		f_conditionIndicators.add("else");
		
		// parallel gateway (AND)
		f_parallelIndicators.add("while");
		f_parallelIndicators.add("meanwhile");
		f_parallelIndicators.add("in parallel");
		f_parallelIndicators.add("concurrently");
		f_parallelIndicators.add("meantime");
		f_parallelIndicators.add("in the meantime");
		// Author: Simon
		f_parallelIndicators.add("and");
		f_parallelIndicators.add("as well as");
		
		// for continuation of a branch of a gateway
		f_sequenceIndicators.add("then");
		f_sequenceIndicators.add("after");
		f_sequenceIndicators.add("afterward");
		f_sequenceIndicators.add("afterwards");
		f_sequenceIndicators.add("subsequently");
		f_sequenceIndicators.add("based on this");
		f_sequenceIndicators.add("thus");
		
		// for Error Intermediate Event
		f_exceptionIndicators.add("except");
		
		f_exampleIndicators.add("for instance");
		f_exampleIndicators.add("for example");
		f_exampleIndicators.add("e.g.");
		
		f_realActorPPIndicators.add("in");
		f_realActorPPIndicators.add("of");
		
		f_interactionVerbs.add("inform");
		f_interactionVerbs.add("affirm");
		f_interactionVerbs.add("respond");
		f_interactionVerbs.add("send");
		f_interactionVerbs.add("receive");
		f_interactionVerbs.add("phone");
		f_interactionVerbs.add("ask");
		f_interactionVerbs.add("report");
	}

}
