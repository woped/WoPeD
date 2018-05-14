/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package etc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import bpmn.Lane;
import bpmn.Message;
import bpmn.MessageFlow;
import bpmn.Pool;
import bpmn.Task;
import edu.stanford.nlp.trees.TypedDependency;
import epc.Connector;
import epc.EPCRepairer;
import epc.Function;
import epc.Organisation;
import epc.SequenceFlow;
import export.BPMNExporter;
import export.EPCExporter;
import models.BPMNModel;
import models.EPCModel;
import nodes.Cluster;
import nodes.FlowObject;
import nodes.ProcessEdge;
import nodes.ProcessNode;
import nodes.ProcessObject;
import processing.T2PStanfordWrapper;
import text.T2PSentence;
import text.Text;
import textModel.SentenceNode;
import textModel.TextModel;
import textModel.TextModelControler;
import transform.BPMNModelBuilder;
import transform.EPCModelBuilder;
import transform.TextAnalyzer;
import transform.TextModelBuilder;
import worldModel.Action;
import worldModel.SpecifiedElement;
import worldModel.WorldModel;

/**
 * wraps all of the functionality to create processes from text.
 * Load and analyze a text using "parseText".
 * To reanalyze a text simple use "analyzeText".
 *
 */
public class TextToProcess {
	
private T2PStanfordWrapper f_stanford = new T2PStanfordWrapper();
	
	private Text f_text;
	
	private TextModelControler f_textModelControler = null;
	private TextAnalyzer f_analyzer = new TextAnalyzer();
	private TextModelBuilder f_builder = new TextModelBuilder();
    private BPMNModel f_generatedModelBPMN = null;
    private EPCModel f_generatedModelEPC = null;
    
	private HashMap<Action, FlowObject> f_elementsMap = new HashMap<Action, FlowObject>();
	private HashMap<FlowObject, Action> f_elementsMapInv = new HashMap<FlowObject, Action>();
	
	//for EPC export
	private ArrayList<Function> f_functions = new ArrayList<Function>();
	private ArrayList<SequenceFlow> f_flows = new ArrayList<SequenceFlow>();
	private ArrayList<epc.Event> f_events = new ArrayList<epc.Event>();
	private ArrayList<Organisation> f_orgs = new ArrayList<Organisation>();
	
	//for BPMN export
	private ArrayList<Pool> f_pools = new ArrayList<Pool>();
	private ArrayList<Lane> f_lanes = new ArrayList<Lane>();
	private ArrayList<bpmn.Task> f_tasks = new ArrayList<bpmn.Task>();
	private ArrayList<bpmn.SequenceFlow> f_bflows = new ArrayList<bpmn.SequenceFlow>();
	private ArrayList<bpmn.Message> f_messages = new ArrayList<bpmn.Message>();
	private ArrayList<bpmn.MessageFlow> f_messageFlows = new ArrayList<bpmn.MessageFlow>();
	/**
	 * 
	 */
	public TextToProcess() {
		
	}
	
	/**
	 * 
	 */
	public TextToProcess(TextModelControler tmControler) {
		 f_textModelControler = tmControler;
	}
	
	
	 /**
     * (Re-)starts analyzing the loaded text and creates a process model
     */
	
	public WorldModel getWorldModel (String text){
		
		f_text = f_stanford.createText(text);
		f_analyzer.clear();
		f_analyzer.analyze(f_text);
		return f_analyzer.getWorld();
		
	}
	
	
	public void analyzeText(boolean rebuildTextModel, boolean bpmn, File outputFile) {
		boolean f_bpmn = bpmn;
		f_analyzer.analyze(f_text);
        if(rebuildTextModel) {
			TextModel _model = f_builder.createModel(f_analyzer);		
			if(f_textModelControler != null)
				f_textModelControler.setModels(this, f_analyzer,f_builder,_model);
        }
        if (f_bpmn) {
        	BPMNModelBuilder _builder = new BPMNModelBuilder(this);
        	f_generatedModelBPMN = (BPMNModel) _builder.createProcessModel(f_analyzer.getWorld());
        	BPMNExporter exp = new BPMNExporter(f_generatedModelBPMN);
        	for (Cluster c : new ArrayList<Cluster>(f_generatedModelBPMN.getClusters())){
        		if (c instanceof Pool){
        			Pool pool = (Pool) c;
        			f_pools.add(pool);
        		} else
        		if (c instanceof Lane){
        			Lane lane = (Lane) c;
        			f_lanes.add(lane);
        		}
        	}
        	f_generatedModelBPMN.getEvents();
        	extractBPMNFlowObjects(f_generatedModelBPMN);
        	extractBPMNFlows(f_generatedModelBPMN);
        	f_generatedModelBPMN.extractGateways();
//        	exp.addLanes(f_lanes);
        	exp.addFlowObjects(f_tasks);
        	exp.addGateways(f_generatedModelBPMN.getComplexGateways(), f_generatedModelBPMN.getEventBasedGateways(),
        			f_generatedModelBPMN.getExclusiveGateways(), f_generatedModelBPMN.getInclusiveGateways(),
        			f_generatedModelBPMN.getParallelGateways());
        	exp.addFlows(f_bflows);
        	
        	exp.addPools(f_pools);
        	exp.end();
        	//exp.export(outputFile);
			System.out.println(exp.export());
        } else {
        	// epc: new (Text2EPC)
        	EPCModelBuilder _builder = new EPCModelBuilder(this);
        	f_generatedModelEPC = (EPCModel) _builder.createProcessModel(f_analyzer.getWorld());
        	EPCRepairer rep = new EPCRepairer(f_generatedModelEPC);
            rep.repairModel();
            EPCExporter exp = new EPCExporter(f_generatedModelEPC);
            exp.addFunctions(rep.getFunctions());
            exp.addEvents(rep.getEvents());
            exp.addOrgs(rep.getOrgs());
            ArrayList<Connector> and = new ArrayList<Connector>();
            ArrayList<Connector> or = new ArrayList<Connector>();
            ArrayList<Connector> xor = new ArrayList<Connector>();
            and.addAll(rep.getAndJoins());
            and.addAll(rep.getAndSplits());
            or.addAll(rep.getOrJoins());
            or.addAll(rep.getOrSplits());
            xor.addAll(rep.getXorJoins());
            xor.addAll(rep.getXorSplits());
            exp.addConnectors(and, or, xor);
            exp.addFlows(rep.getFlows());
            exp.end();
            //exp.export(outputFile);
			System.out.println(exp.export());
        }
        
	}
	
	private void extractBPMNFlowObjects (BPMNModel bpmn){
		for (ProcessNode pn : new ArrayList<ProcessNode>(f_generatedModelBPMN.getFlowObjects())){
			if(pn instanceof bpmn.Task){
				Task task = (Task) pn;
				f_tasks.add(task);
			}
			if(pn instanceof bpmn.Message){
				Message mess = (Message) pn;
				f_messages.add(mess);
			}
		}
	}
	
	private void extractBPMNFlows (BPMNModel bpmn){
		for (ProcessEdge pe : new ArrayList<ProcessEdge>(f_generatedModelBPMN.getEdges())){
    		if (pe instanceof bpmn.SequenceFlow){
    			bpmn.SequenceFlow f = (bpmn.SequenceFlow) pe;
    			f_bflows.add(f);
    		}
    		if (pe instanceof MessageFlow){
    			MessageFlow f = (MessageFlow) pe;
    			f_messageFlows.add(f);
    		}
    	}
	}
	
	public void parseText(String text, boolean bpmn, File outputFile) {
		f_text = f_stanford.createText(text);
		f_analyzer.clear();
		analyzeText(true, bpmn, outputFile);
	}
	
	public void parseFile(File file, boolean bpmn, File outputFile) throws IOException {
		f_text = f_stanford.createText(file);
		f_analyzer.clear();
		analyzeText(true, bpmn, outputFile);
	}
	
	
	/**
	 * Sets the element map which comes from the ProcessModelBuilder
	 * and can be used to map actions to task nodes etc.
	 * @param map
	 */
	public void setElementMapping(HashMap<Action, FlowObject> map) {
		f_elementsMap = map;
		for(Entry<Action, FlowObject> e:f_elementsMap.entrySet()) {
			//building inverted list
			f_elementsMapInv.put(e.getValue(), e.getKey());			
		}
	}

	/**
	 * @param o
	 */
	public void textModelElementClicked(ProcessObject o) {
		if(o instanceof SentenceNode) {
    		SentenceNode n = (SentenceNode) o;
	    	T2PSentence _sentence = f_text.getSentences().get(n.getIndex());
	       	if(_sentence != null) {
	    		
	    		Collection<TypedDependency> _list = _sentence.getGrammaticalStructure().typedDependenciesCollapsed();
	    		
	    		StringBuffer _depText = new StringBuffer();
	    		for(TypedDependency td:_list) {
	    			_depText.append(td.toString());
	    		}
	    		
	    		f_analyzer.analyzeSentence(_sentence,1,true);
	    	}
    	}
    	else {
    		if(f_elementsMapInv.containsKey(o)) {
    			Action _ac = f_elementsMapInv.get(o);
    			if(f_textModelControler != null)
    				f_textModelControler.highlightAction(_ac);    			
    		}
    	}
	}

	/**
	 * @return
	 */
	public TextStatistics getTextStatistics() {
		TextStatistics _result = new TextStatistics();
		_result.setNumberOfSentences(f_text.getSize());
		_result.setAvgSentenceLength(f_text.getAvgSentenceLength());
		_result.setNumOfReferences(f_analyzer.getNumberOfReferences());
		_result.setNumOfLinks(f_analyzer.getNumberOfLinks());
		return _result;
	}

	/**
	 * @return
	 */
	public TextAnalyzer getAnalyzer() {
		return f_analyzer;
	}

	/**
	 * @param _element
	 */
	public void textElementClicked(SpecifiedElement _element) {
		if(_element instanceof Action) {
			FlowObject _corr = f_elementsMap.get(_element);
			if(_corr != null) {							
			}
		}
	}

	/**
	 * @param sentenceWordID
	 * @param sentenceWordID2
	 */
	public void addManualReferenceResolution(SentenceWordID sentenceWordID,	SentenceWordID sentenceWordID2) {
		f_analyzer.addManualReference(sentenceWordID, sentenceWordID2);
	}

	
}
