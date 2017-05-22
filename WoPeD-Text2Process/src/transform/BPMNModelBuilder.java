package transform;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nodes.Cluster;
import nodes.FlowObject;
import nodes.ProcessEdge;
import nodes.ProcessNode;
import models.BPMNModel;
import models.ProcessModel;
import bpmn.Association;
import bpmn.DataObject;
import bpmn.EndEvent;
import bpmn.ErrorIntermediateEvent;
import bpmn.EventBasedGateway;
import bpmn.ExclusiveGateway;
import bpmn.Gateway;
import bpmn.InclusiveGateway;
import bpmn.IntermediateEvent;
import bpmn.Lane;
import bpmn.LaneableCluster;
import bpmn.MessageFlow;
import bpmn.MessageIntermediateEvent;
import bpmn.ParallelGateway;
import bpmn.Pool;
import bpmn.SequenceFlow;
import bpmn.StartEvent;
import bpmn.Task;
import bpmn.TimerIntermediateEvent;
import etc.Constants;
import etc.TextToProcess;
import processing.WordNetWrapper;
import processing.FrameNetWrapper.PhraseType;
import tools.Configuration;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Flow;
import worldModel.Resource;
import worldModel.Specifier;
import worldModel.WorldModel;
import worldModel.Flow.FlowDirection;
import worldModel.Flow.FlowType;
import worldModel.Specifier.SpecifierType;


public class BPMNModelBuilder extends ProcessModelBuilder {
	
	private Configuration f_config = Configuration.getInstance();
	
	//Nodes
	private final boolean EVENTS_TO_LABELS = true;
	private final boolean REMOVE_LOW_ENTROPY_NODES = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_REMOVE_LOW_ENT_NODES));
	private final boolean HIGHLIGHT_LOW_ENTROPY = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_HIGHLIGHT_META_ACTIONS));
	//Labeling
	private final boolean ADD_UNKNOWN_PHRASETYPES = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_ADD_UNKNOWN_PT));
	private final int MAX_NAME_DEPTH = 3;
	//Model in General
	private final boolean BUILD_BLACK_BOX_POOL_COMMUNICATION = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_BB_POOLS));
	private final boolean BUILD_DATA_OBJECTS = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_DATA_OBJECTS));
	
	
	
	private TextToProcess f_parent;
	
	private BPMNModel f_model = new BPMNModel("generated Model");
	
	
	private HashMap<Actor, String> f_ActorToName = new HashMap<Actor, String>();
	private HashMap<String, Lane> f_NameToPool = new HashMap<String, Lane>();		
	private HashMap<Action, FlowObject> f_elementsMap = new HashMap<Action, FlowObject>();
	private HashMap<FlowObject, Action> f_elementsMap2 = new HashMap<FlowObject,Action>();
	
	private ArrayList<FlowObject> f_notAssigned = new ArrayList<FlowObject>();
	private Lane f_lastPool = null;
	private LaneableCluster f_mainPool;
	
	//for black box pools
	private HashMap<ProcessNode,String> f_CommLinks = new HashMap<ProcessNode, String>();
	private HashMap<String, Pool> f_bbPoolcache = new HashMap<String, Pool>();

	
	
	public BPMNModelBuilder(TextToProcess parent) {
		f_parent = parent;
	}
	
	@Override
	public ProcessModel createProcessModel(WorldModel world) {
		f_mainPool = new Pool();
		f_model.addNode(f_mainPool);
		
		createActions(world);		
		buildSequenceFlows(world);
		removeDummies(world);
		finishDanglingEnds();
		if(EVENTS_TO_LABELS) {
			eventsToLabels();
		}
		processMetaActivities(world);		
		buildBlackBoxPools(world);
		buildDataObjects(world);
		
		if(f_mainPool.getProcessNodes().size() == 0) {
			f_model.removeNode(f_mainPool);
		}		
		
		//ProcessUtils.sortClusters(f_model);
		layoutModel(f_model);
		layoutModel(f_model);
		f_parent.setElementMapping(f_elementsMap);
		return f_model;
		
	}

	
	/**
	 * @param world
	 */
	@Override
	public void buildDataObjects(WorldModel world) {
		//to avoid double adding
		HashMap<Action,List<String>> _handeledDOs = new HashMap<Action, List<String>>();
		
		if(BUILD_DATA_OBJECTS) {
			for(ProcessNode a:new ArrayList<ProcessNode>(f_model.getNodes())) {
				if(!(a instanceof Pool || a instanceof Lane)) {
					List<ProcessNode> _succs = f_model.getSuccessors(a);
					for(ProcessNode n:new ArrayList<ProcessNode>(_succs)) {
						if(n instanceof Gateway) {
							_succs.addAll(f_model.getSuccessors(n));
						}
					}
					Action _a = f_elementsMap2.get(a);
					Set<String> _candA = getDataObjectCandidates(_a);
					//now we got all pairs
					for(ProcessNode b:_succs) {
						Action _b = f_elementsMap2.get(b);
						Set<String> _candB = getDataObjectCandidates(_b);
						for(String s:_candB) {
							if(_candA.contains(s)) {
								//okay we need to generate this
								DataObject _do = createDataObject(_a, s,false);
								//connect to other Node
								ProcessNode _target = f_elementsMap.get(_b);
								Association _asc = new Association(_do,_target);
								f_model.addEdge(_asc);	
								put(_handeledDOs,_a,s);
								put(_handeledDOs,_b,s);
							}
						}
					}				
					//checked all pairs, now check singles only
					for(String s:_candA) {
						if(_handeledDOs.get(_a) == null || !_handeledDOs.get(_a).contains(s)) {
							createDataObject(_a, s,true);
						}
					}
				}			
			}
		}
	}


	public DataObject createDataObject(Action targetAc,String doName,boolean arriving) {
		ProcessNode _target = f_elementsMap.get(targetAc);
		Lane _lane = getLaneForNode(_target);
		if(_lane != null) {
			DataObject _do = new DataObject(0,0,doName);
			f_model.addNode(_do);
			_lane.addProcessNode(_do);			
			if(arriving) {
				Association _asc = new Association(_do,_target);
				f_model.addEdge(_asc);
			}else {
				Association _asc = new Association(_target,_do);
				f_model.addEdge(_asc);
			}
			return _do;
		}
		return null;
	}
	
	@Override
	public Map<ProcessNode, String> getCommLinks(){
		return f_CommLinks;
	}
	
	@Override
	protected void processMetaActivities(WorldModel world) {
		for(Action a:world.getActions()) {
			if(a.getActorFrom() != null && a.getActorFrom().isMetaActor()) {					
				if(WordNetWrapper.isVerbOfType(a.getName(),"end")){
					//found an end verb
					ProcessNode _pnode = f_elementsMap.get(a);
					List<ProcessNode> _succs = f_model.getSuccessors(_pnode);
//					boolean _allEnd = true;
//					for(ProcessNode n:_succs) {
//						if(!(n instanceof EndEvent)) {
//							_allEnd = false;
//							break;
//						}
//					}
//					if(_allEnd) {
						removeNode(a);
						if(a.getName().equals("terminate") && _succs.size()==1) {
							EndEvent _ee = (EndEvent) _succs.get(0);
//							try {
//								ProcessUtils.refactorNode(f_model, _ee, TerminateEndEvent.class);
//							}catch(Exception ex) {
//								ex.printStackTrace();
//							}
						}
//					}					
				}else if(WordNetWrapper.isVerbOfType(a.getName(),"start")) {
					ProcessNode _pnode = f_elementsMap.get(a);
					List<ProcessNode> _preds = f_model.getPredecessors(_pnode);
					if(_preds.size() == 1 && _preds.get(0) instanceof StartEvent) {
						//we do not need this node
						removeNode(a);
					}
				}
			}
		}
	}
	
	@Override
	protected void removeDummies(WorldModel world) {
		for(Action a:world.getActions()) {
			if(a instanceof DummyAction || a.getTransient()) {
				removeNode(a);
			}else {
				if(f_elementsMap.get(a).getText().equals("Dummy")) {
					removeNode(a);
				}
			}
		}
	}
	
	@Override
	protected void eventsToLabels() {
		for(ProcessNode node:new ArrayList<ProcessNode>(f_model.getNodes())) {
			if(node instanceof Gateway) {
				List<ProcessNode> _succs = f_model.getSuccessors(node);
				for(ProcessNode _succ : _succs) {
					if(_succ.getClass().getSimpleName().equals("IntermediateEvent")) { //only simple intermediate events
						List<ProcessNode> _succsIE = f_model.getSuccessors(_succ);
						if(_succsIE.size() == 1) {
							String _lbl = _succ.getName();
							SequenceFlow _newFlow = removeNode(_succ);
							_newFlow.setLabel(_lbl);
						}
					}else if(_succ instanceof Task) {
						Action _action = f_elementsMap2.get(_succ);
						List<Specifier> _specs = _action.getSpecifiers(SpecifierType.PP);
						if(_action.getActorFrom() != null) {
							_specs.addAll(_action.getActorFrom().getSpecifiers(SpecifierType.PP));
						}
						for(Specifier spec:_specs) {
							if(SearchUtils.startsWithAny(spec.getPhrase(),Constants.f_conditionIndicators)
									&& !Constants.f_conditionIndicators.contains(spec.getPhrase())) { //it should only be the start of a phrase, not the whole phrase!
								//found the phrase which can serve as a label
								SequenceFlow _sqf = (SequenceFlow) f_model.getConnectingEdge(node, _succ);
								_sqf.setLabel(spec.getPhrase());
								break;
							}
						}
						//}
						
					}
				}
			}
		}
	}
	
	protected SequenceFlow removeNode(Action a) {
		ProcessNode _node = toProcessNode(a);
//		if(f_model.getPredecessors(_node).size() == 0) {
//			//add a start node in front
//			StartEvent _start = new StartEvent();
//			f_model.addNode(_start);
//			Cluster _lane = f_model.getClusterForNode(_node);
//			if(_lane != null) {
//				_lane.addProcessNode(_start);
//			}
//			SequenceFlow _sqf = new SequenceFlow(_start,_node);
//			f_model.addFlow(_sqf);
	//	}
		return removeNode(_node);
	}

	/**
	 * removes a node from the model but keeps the predecessor and successor connected
	 * @param a
	 * 
	 */
	public SequenceFlow removeNode(ProcessNode node) {
		//remove this action and connect both nodes
		
		ProcessNode _pre = null;
		ProcessNode _succ = null;
		for(ProcessEdge edge:f_model.getEdges()) {
			if(edge.getTarget() != null && edge.getSource() != null){
				if(edge.getTarget().equals(node)) {
					_pre = edge.getSource();
				}
				if(edge.getSource().equals(node)) {
					_succ = edge.getTarget();
				}
			}
		}
		f_model.removeNode(node);
		if(_pre != null && _succ != null) {
			SequenceFlow _sqf = new SequenceFlow(_pre,_succ);
			f_model.addFlow(_sqf);
			return _sqf;
		}
		return null;
	}
	
	protected String createTaskText(Action a) {
		StringBuilder _b = new StringBuilder();		
		if(a.isNegated()) {
			if(a.getAux() != null) {
				_b.append(a.getAux());
				_b.append(' ');
			}
			_b.append("not");
			_b.append(' ');
		}
		if(WordNetWrapper.isWeakAction(a) && canBeTransformed(a)) {
			if(a.getActorFrom() != null && a.getActorFrom().isUnreal() && hasHiddenAction(a.getActorFrom())) {
				_b.append(transformToAction(a.getActorFrom()));					
			}else if(a.getObject() != null && ((a.getObject() instanceof Resource) ||  !((Actor)a.getObject()).isUnreal())) {
				_b.append(transformToAction(a.getObject()));					
			}			
		}else {
			boolean _weak = WordNetWrapper.isWeakVerb(a.getName());
			if(!_weak) {
				_b.append(WordNetWrapper.getBaseForm(a.getName()));
				if(a.getPrt()!= null) {
					_b.append(' ');
					_b.append(a.getPrt());
				}
				_b.append(' ');
			}else {
				//a weak verb which cannot be transformed hmmm.....
				if(REMOVE_LOW_ENTROPY_NODES && (a.getActorFrom() == null || a.getActorFrom().isMetaActor()) &&
						a.getXcomp() == null) {
					return "Dummy";					
				}else {
					if(a.getXcomp() == null) { //hm we should add something here or the label is empty
						_b.append(getEventText(a));
						return _b.toString().replaceAll("  ", " ");
					}
				}
			}
			boolean _xCompAdded = false;
			boolean _modAdded = false;
			if(a.getObject() != null) {
				if(a.getMod() != null && (a.getModPos() < a.getObject().getWordIndex())) {
					addMod(a,_b);	
					_b.append(' ');
					_modAdded = true;
				}
				if(a.getXcomp()!= null && (a.getXcomp().getWordIndex() < a.getObject().getWordIndex())) {
					addXComp(a, _b,!_weak);
					_b.append(' ');
					_xCompAdded = true;
				}			
				if(a.getSpecifiers(SpecifierType.IOBJ).size() > 0) {
					for(Specifier spec:a.getSpecifiers(SpecifierType.IOBJ)) {
						_b.append(spec.getPhrase());
						_b.append(' ');
					}
				}
				_b.append(getName(a.getObject(),true));
				//
				for(Specifier _dob : a.getSpecifiers(SpecifierType.DOBJ)) {
					_b.append(' ');
					_b.append(getName(_dob.getObject(),true));
				}
				
			}			
			if(!_modAdded) {
				addMod(a, _b);
			}			
			if(!_xCompAdded && a.getXcomp() != null) {
				addSpecifiers(a, _b,a.getXcomp().getWordIndex(),true);
				addXComp(a, _b,!_weak || a.getObject() != null);
			}
			addSpecifiers(a, _b,getXCompPos(a.getXcomp()),false);
			if(!(a.getObject() == null)) { //otherwise addSpecifiers already did the work!
				for(Specifier sp:a.getSpecifiers(SpecifierType.PP)) {
					if((sp.getName().startsWith("to") || sp.getName().startsWith("in") || sp.getName().startsWith("about"))
							&& !(SearchUtils.startsWithAny(sp.getPhrase(), Constants.f_conditionIndicators))) {
						_b.append(' ');
						if(sp.getObject() != null) {
							_b.append(sp.getHeadWord());
							_b.append(' ');
							_b.append(getName(sp.getObject(),true));
						}else {
							_b.append(sp.getName());
							break; // one is enough
						}						
					}
				}
			}
		}
		return _b.toString().replaceAll("  ", " ");
	}
	
	@Override
	protected boolean considerPhrase(Specifier spec) {
		if(spec.getPhraseType() == PhraseType.PERIPHERAL || spec.getPhraseType() == PhraseType.EXTRA_THEMATIC) {
			return false;
		}else {
			if(spec.getPhraseType() == PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
				return true;
			}
		}
		return true; //always accept core and genetive
	}
	
	@Override
	protected String getName(ExtractedObject a,boolean addDet) {
		return getName(a, addDet, 1);
	}
	
	private String getName(ExtractedObject a,boolean addDet,int level) {
		return getName(a, addDet, level, false);
	}

	/**
	 * 
	 */
	//BPMN exclusive
	private void buildBlackBoxPools(WorldModel world) {
		if(BUILD_BLACK_BOX_POOL_COMMUNICATION) {
			for(Action a:world.getActions()) {
				if(WordNetWrapper.isInteractionVerb(a)){
					checkForBBPools(a);	
				}
				if(a.getXcomp() != null && WordNetWrapper.isInteractionVerb(a.getXcomp())) {
					checkForBBPools(a.getXcomp());
				}
			}	
		}
	}
	//BPMN exclusive
	private void checkForBBPools(Action a) {
		//candidate
		Specifier _sender = containedSender(a.getSpecifiers(SpecifierType.PP));
		Specifier _receiver = containedReceiver(a.getSpecifiers(SpecifierType.PP));
		ExtractedObject _obj = a.getObject();
		if(_obj != null && _obj.needsResolve() && _obj.getReference() instanceof ExtractedObject) {
			_obj = (ExtractedObject) _obj.getReference();
		}
		if(_obj != null && !(_obj instanceof Actor)) {
			//checking genetive 
			for(Specifier spec:_obj.getSpecifiers(SpecifierType.PP)) {
				if(spec.getPhraseType().equals(PhraseType.GENITIVE)) {
					if(spec.getObject() instanceof Actor) {
						_obj = spec.getObject();
					}
				}
			}
		}
		if(_obj != null && _obj instanceof Actor && !((Actor)_obj).isMetaActor()) {
			String _name = getName(_obj, false,0,true);
			if(f_NameToPool.containsKey(_name)){
				
				f_CommLinks.put(f_elementsMap.get(a), _name);
			}else {
				Pool _bbP = getBBPool(_name);
				ProcessNode _t = ((Task)f_elementsMap.get(a));
				if(_t instanceof Task) {
				 _t.setStereotype(Task.TYPE_SEND);
				}
				MessageFlow _msf = new MessageFlow(_t,_bbP);
				f_model.addEdge(_msf);
			}
		}else {
			if(_sender != null && !f_NameToPool.containsKey(getName(_sender.getObject(), false))) {
				String _name = getName(_sender.getObject(), false);
				if(f_NameToPool.containsKey(_name)){
					f_CommLinks.put(f_elementsMap.get(a), _name);
				}else {
					Pool _bbP = getBBPool(_name);
					Task _t = ((Task)f_elementsMap.get(a));
					_t.setStereotype(Task.TYPE_SEND);
					MessageFlow _msf = new MessageFlow(_t,_bbP);
					f_model.addEdge(_msf);
				}	
			}else if(_receiver != null && !f_NameToPool.containsKey(getName(_receiver.getObject(), false))){
				String _name = getName(_receiver.getObject(), false);
				if(f_NameToPool.containsKey(_name)){
					f_CommLinks.put(f_elementsMap.get(a), _name);
				}else {
					Pool _bbP = getBBPool(_name);
					ProcessNode _rpn = f_elementsMap.get(a);
					if(_rpn instanceof Task) {
						Task _t = ((Task)_rpn);
						_t.setStereotype(Task.TYPE_RECEIVE);
					}
					MessageFlow _msf = new MessageFlow(_bbP,_rpn);
					f_model.addEdge(_msf);
				}
			}
		}
	}
	


	/**
	 * create or retrieves a cached blackbox pool
	 * @param a
	 * @return
	 */
	
	//BPMN exclusive
	private Pool getBBPool(String name) {
		if(f_bbPoolcache.containsKey(name)) {
			return f_bbPoolcache.get(name);
		}else {
			Pool _result = new Pool(0,-100,name);
			_result.setProperty(Pool.PROP_BLACKBOX_POOL, "TRUE");
			f_bbPoolcache.put(name, _result);
			f_model.addNode(_result);
			return _result;
		}		 
	}


	private void createActions(WorldModel world) {
		for(Action a:world.getActions()) {
			FlowObject _obj;
			if(!("if".equals(a.getMarker())) || a.isMarkerFromPP()) {
				_obj = createTask(a);	
			}else {				
				_obj = createEventNode(a);
				f_model.addFlowObject(_obj);
				_obj.setText(getEventText(a)+" "+_obj.getText());
			}
			if(HIGHLIGHT_LOW_ENTROPY) {
				if(a.getActorFrom() != null && a.getActorFrom().isMetaActor()); //{
				//	_obj.setBackground(Color.YELLOW);
			//	}
			}
			
			f_elementsMap.put(a,_obj);
			if(a.getXcomp() != null)
				f_elementsMap.put(a.getXcomp(), _obj);
			f_elementsMap2.put(_obj, a);
			Lane _p = null;
			if(!WordNetWrapper.isWeakAction(a)) {
				_p = getLane(a.getActorFrom());
			}
			if(_p == null) {
				if(f_lastPool == null) {
					f_notAssigned.add(_obj);
				}else {
					f_lastPool.addProcessNode(_obj);
				}
			}else {
				_p.addProcessNode(_obj);
				f_lastPool = _p;
				if(f_notAssigned.size() > 0) {
					for(FlowObject fo:f_notAssigned) {
						f_lastPool.addProcessNode(fo);
					}
					f_notAssigned.clear();
				}
			}
			if (_obj instanceof bpmn.Event){
				bpmn.Event e = (bpmn.Event) _obj;
				f_model.addEvent(e);
			}
		}
	}

	/**
	 * @param a
	 * @return
	 */
	//BPMN exclusive
	private IntermediateEvent createEventNode(Action a) {
		for(Specifier spec:a.getSpecifiers()) {
			for(String word:spec.getPhrase().split(" ")) {
				if(WordNetWrapper.isTimePeriod(word)) {
					IntermediateEvent _result = new TimerIntermediateEvent();
					_result.setText(spec.getPhrase());
					return _result;
				}
			}
		}
		
		if(WordNetWrapper.isVerbOfType(a.getName(), "send") || WordNetWrapper.isVerbOfType(a.getName(), "receive") /*isInteractionVerb(a)*/) {
			MessageIntermediateEvent _mie = new MessageIntermediateEvent();
			if(WordNetWrapper.isVerbOfType(a.getName(),"send")) {
				_mie.setProperty(MessageIntermediateEvent.PROP_EVENT_SUBTYPE, MessageIntermediateEvent.EVENT_SUBTYPE_THROWING);
			}
			return _mie;
		}
		return new IntermediateEvent();
	}

/**
 * 
 */
private void finishDanglingEnds() {
	for(ProcessNode node:new ArrayList<ProcessNode>(f_model.getNodes())) {
		if(node instanceof Task || node instanceof Gateway || node instanceof IntermediateEvent) {
			//has to be the source somewhere
			int _inC = 0;
			int _outC = 0;
			for(ProcessEdge e:f_model.getEdges()) {
				if (e.getSource() != null){
					if(e.getSource().equals(node)) {
						_outC++;
					}else if (e.getTarget().equals(node)) {
						_inC++;
					}	
				}
			}
			if((_outC == 0) || (node instanceof Gateway && _inC == 1 && _outC == 1)) {
				//need to finish this one
				EndEvent _end = new EndEvent();
				f_model.addNode(_end);
				addToSameLane(node, _end);
				SequenceFlow _sqf = new SequenceFlow(node,_end);
				f_model.addFlow(_sqf);	
			}				
			if(_inC == 0) {
				if(node instanceof IntermediateEvent) {
					IntermediateEvent _ime = (IntermediateEvent) node;
					if(_ime.getParentNodeId() != null) {
						//it s an attached intermediate event
						continue;
					}
				}
				StartEvent _start = new StartEvent();
				f_model.addNode(_start);
				addToSameLane(node, _start);
				SequenceFlow _sqf = new SequenceFlow(_start,node);
				f_model.addFlow(_sqf);	
			}
		}
	}
}



	
	private void buildSequenceFlows(WorldModel world) {
		for(Flow f:world.getFlows()) {
			if(f.getType() == FlowType.sequence && f.getMultipleObjects().size() == 1) {
				SequenceFlow _sqf = new SequenceFlow();
				_sqf.setSource(toProcessNode(f.getSingleObject()));
				_sqf.setTarget(toProcessNode(f.getMultipleObjects().get(0)));
				f_model.addEdge(_sqf);
			}else {
				if(f.getType() == FlowType.exception) {
					ErrorIntermediateEvent _exc = new ErrorIntermediateEvent();
					f_model.addFlowObject(_exc);
					ProcessNode _task = (ProcessNode) toProcessNode(f.getSingleObject()); //should be a task
					_exc.setParentNode(_task);
					addToSameLane(_task, _exc);		
					
					SequenceFlow _sqf = new SequenceFlow();
					_sqf.setSource(_exc);
					_sqf.setTarget(toProcessNode(f.getMultipleObjects().get(0)));
					f_model.addEdge(_sqf);
				}else {
					//is it a split?
					//create flow from single to gateway
					Gateway _gate = createGateway(f);
					SequenceFlow _sf1 = new SequenceFlow();
					if(f.getDirection() == FlowDirection.split) {
						_sf1.setSource(toProcessNode(f.getSingleObject()));
						_sf1.setTarget(_gate);
						addToPrevalentLane(f,_gate);
						for(Action action:f.getMultipleObjects()) {
							SequenceFlow _sf = new SequenceFlow();
							_sf.setSource(_gate);
							_sf.setTarget(toProcessNode(action));
							f_model.addEdge(_sf);
						}					
					}else if(f.getDirection() == FlowDirection.join) {
						_sf1.setSource(_gate);
						_sf1.setTarget(toProcessNode(f.getSingleObject()));
						addToPrevalentLane(f,_gate);
						for(Action action:f.getMultipleObjects()) {
							SequenceFlow _sf = new SequenceFlow();
							_sf.setSource(toProcessNode(action));
							_sf.setTarget(_gate);
							f_model.addEdge(_sf);
						}	
					}
					f_model.addEdge(_sf1);
				}
			}
		}
	}
	
	private FlowObject toProcessNode(Action a) {
		
		FlowObject _obj = f_elementsMap.get(a);
		if(_obj == null) {
			if(a instanceof DummyAction) {
				Task _t = new Task();
				_t.setText("Dummy Task");
				f_elementsMap.put(a, _t);
				f_elementsMap2.put(_t, a);
				return _t;
			}
			System.out.println("error no flowobject found!");
		}
		return _obj;
	}
	
	
	//bpmn exclusive
	/**
	 * @param source
	 * @param _gate
	 */
	private void addToSameLane(ProcessNode source, FlowObject _gate) {
		Lane _l = getLaneForNode(source);
		if(_l != null) {
			_l.addProcessNode(_gate);
		}
	}
	
	private Lane getLaneForNode(ProcessNode node) {
		for(Cluster c:node.getParentClusters()) {
			if(c instanceof Lane) {
				return (Lane)c;
			}
		}
		return null;
	}

	/**
	 * @param f
	 * @return
	 */
	private Gateway createGateway(Flow f) {
		Gateway _result;
		if(f.getType() == FlowType.concurrency) {
			_result = new ParallelGateway();
		}else if(f.getType() == FlowType.multiChoice) {
			_result = new InclusiveGateway();
		}else {
			for(Action a: f.getMultipleObjects()) {
				ProcessNode _node = f_elementsMap.get(a);
				if(_node instanceof IntermediateEvent && !(_node.getClass() == IntermediateEvent.class)) {
					
				}else {
					//not all special events
					_result = new ExclusiveGateway();
					f_model.addNode(_result);
					return _result;
				}
			}
			//all are special events (message, timer etc.)
			_result = new EventBasedGateway();
		}
		f_model.addNode(_result);
		return _result;
	}

	
	/**
	 * @param a
	 * @return
	 */
	private Task createTask(Action a) {
		Task _result = new Task();		
		String _name = createTaskText(a);
		_result.setText(_name);
		f_model.addFlowObject(_result);
		return _result;
	}



	/**
	 * @param actorFrom
	 * @return
	 */
	private Lane getLane(Actor a) {
		Actor original = a;
		if(a == null) {
			return null;
		}
		if(a.needsResolve()) {
			if(a.getReference() instanceof Actor) {
				a = (Actor) a.getReference();
			}else {
				return null;
			}
		}
		if(!a.isUnreal() && !a.isMetaActor() && original.isSubjectRole()) {			
			String _name = getName(a,false);
			f_ActorToName.put(a, _name);
			
			if(!f_NameToPool.containsKey(_name)) {
				Lane _lane = new Lane(_name,100,f_mainPool);
				f_mainPool.addLane(_lane);
				f_model.addNode(_lane);
				f_NameToPool.put(_name, _lane);
				return _lane;
			}
			return f_NameToPool.get(_name);
		}
		return null;
	}


	/**
	 * @param a
	 * @return
	 */
	@Override
	public String getName(ExtractedObject a,boolean addDet,int level,boolean compact) {
		if(a == null) {
			return "null";
		}
		if(a.needsResolve() && a.getReference() instanceof ExtractedObject) {
			return getName((ExtractedObject)a.getReference(),addDet);
		}
		StringBuilder _b = new StringBuilder();
		if(addDet && Constants.f_wantedDeterminers.contains(a.getDeterminer())) {
			_b.append(a.getDeterminer());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.AMOD)) {
			_b.append(s.getName());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.NUM)) {
			_b.append(s.getName());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.NN)) {
			_b.append(s.getName());
			_b.append(' ');
		}		
		_b.append(a.getName());
		for(Specifier s:a.getSpecifiers(SpecifierType.NNAFTER)) {
			_b.append(' ');
			_b.append(s.getName());
		}
		if(level <= MAX_NAME_DEPTH)
		for(Specifier s:a.getSpecifiers(SpecifierType.PP)) {
			if(s.getPhraseType() == PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
				if(s.getName().startsWith("of") || 
						(!compact && s.getName().startsWith("into")) || 
						(!compact && s.getName().startsWith("under")) ||
						(!compact && s.getName().startsWith("about"))) {
					addSpecifier(level, _b, s,compact);
				}	
			}else if(considerPhrase(s)) {
				addSpecifier(level, _b, s,compact);
			}
			
		}
		if(!compact) {
			for(Specifier s:a.getSpecifiers(SpecifierType.INFMOD)) {
				_b.append(' ');
				_b.append(s.getName());
			}for(Specifier s:a.getSpecifiers(SpecifierType.PARTMOD)) {
				_b.append(' ');
				_b.append(s.getName());
			}
		}
		return _b.toString();
	}


	@Override
	public void layoutModel(ProcessModel _result) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param f
	 * @param gate
	 */
	private void addToPrevalentLane(Flow f, Gateway gate) {
		HashMap<Lane, Integer> _countMap = new HashMap<Lane, Integer>();
		if(!(f.getSingleObject() instanceof DummyAction)) {
			Lane _lane1 = getLaneForNode(toProcessNode(f.getSingleObject()));
			_countMap.put(_lane1, 1);
		}
		for(Action a:f.getMultipleObjects()) {
			if(!(a instanceof DummyAction)) {
				Lane _lane = getLaneForNode(toProcessNode(a));
				if(_countMap.containsKey(_lane)) {
					_countMap.put(_lane,_countMap.get(_lane)+1);
				}else {
					_countMap.put(_lane, 1);
				}
			}
		}
		Lane _best = (Lane) SearchUtils.getMaxCountElement(_countMap);
		//if best is null, there simply is no lane in the process!
		if(_best != null) {
			_best.addProcessNode(gate);
		}
	}
	
	@Override
	protected void addXComp(Action a, StringBuilder _b,boolean needsAux) {
		if(a.getXcomp() != null) {
			if(needsAux) {
				if(a.getXcomp().getAux() != null) {
					_b.append(' ');
					_b.append(a.getXcomp().getAux());
					_b.append(' ');
				}else {
					_b.append(" to ");
				}
			}
			_b.append(createTaskText( a.getXcomp()));
		}		
	}

}
