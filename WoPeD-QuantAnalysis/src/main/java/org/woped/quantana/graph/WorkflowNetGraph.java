package org.woped.quantana.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;

public class WorkflowNetGraph {
	static String zyklen = "";

	Node[] nodeArray;
	Node sourcePlace = null;
	Node sinkPlace = null;
	LinkedList<Node> path = new LinkedList<Node>();
	ModelElementContainer mec = null;
	
	public WorkflowNetGraph(IEditor editor){
		IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);
		this.mec = editor.getModelProcessor().getElementContainer();
		zyklen = "";
		
		int numNodes = mec.getRootElements().size();
		
		nodeArray = new Node[numNodes];
		int nextIdx = 0;

        AbstractPetriNetElementModel source = qualanService.getSourcePlaces().iterator().next();
        sourcePlace = new Node(source.getId(), source.getNameValue());
		sourcePlace.setType(Node.TYPE_PLACE);

        AbstractPetriNetElementModel sink = qualanService.getSinkPlaces().iterator().next();
        sinkPlace = new Node(sink.getId(), sink.getNameValue());
		sinkPlace.setType(Node.TYPE_PLACE);
		
		initNodeArray();
		
		nodeArray[nextIdx] = sourcePlace;
		nextIdx++;
		
		buildGraph(sourcePlace);
		while(nextIdx < nodeArray.length){
			buildGraph(nodeArray[nextIdx]);
			nextIdx++;
		}
		
		resetMarking();
		
		for (int i = 0; i < nodeArray.length; i++){
			Node n = nodeArray[i];
			if (n.getType() == Node.TYPE_TRANS_SIMPLE){
				int s = n.getSuccessor().size();
				int p = n.getPredecessor().size();
				if (s > 1) n.setAndSplit(true);
				if (p > 1) n.setAndJoin(true);
			}
		}
	}
	
	private void initNodeArray(){
		Node dummy = new Node("", "");
		for (int i = 0; i < nodeArray.length; i++)
			nodeArray[i] = dummy;
	}
	
	public int getNodeIdx(String id){
		int f = nodeArray.length;
		for (int i = 0; i < f; i++){
			if (nodeArray[i].getId().equals(id))
				f = i;
		}
		
		return f;
	}
	
	public void resetMarking(){
		for (Node n : nodeArray){
			n.setMarkiert(Node.NOT_STARTED);
		}
	}
	
	public String toString(){
		String text = "Graph starts with " + getStartPlace() + "\n\n";
		
		for (int i = 0; i < nodeArray.length; i++){
			Node n = nodeArray[i];
			text += n + " >> [ ";
			
			for (Arc a : n.getSuccessor()){
				text += "(" +a.target + "(" + a.getProbability() + "))";
			}

			text += " ]\n";

			//Vorgaenger
			text += n + " << [ ";

			for (Arc a : n.getPredecessor()){
				text += a.target;
			}

			text += " ]\n";
		}
		
		return text;
	}
	
	public Node getStartPlace(){
		return sourcePlace;
	}
	
	private void buildGraph(Node n){
		Iterator<AbstractPetriNetElementModel> postNodes = mec.getTargetElements(n.getId()).values().iterator();
		Iterator<AbstractPetriNetElementModel> preNodes = mec.getSourceElements(n.getId()).values().iterator();

		if (mec.getTargetElements(n.getId()).size() > 1)
			n.setFork(true);
		
		while (postNodes.hasNext()){
            AbstractPetriNetElementModel currentPlace = postNodes.next();

			int nodeIdx = getNodeIdx(currentPlace.getId());
			Node postNode = null;
			if (nodeIdx >= nodeArray.length){
				String id = currentPlace.getId();
				if (id.equals(sinkPlace.getId())){
					postNode = sinkPlace;
				} else {
					postNode = new Node(id, currentPlace.getNameValue());
					switch (currentPlace.getType()){
					case AbstractPetriNetElementModel.PLACE_TYPE:
						postNode.setType(Node.TYPE_PLACE);
						break;
					case AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE:
						postNode.setType(Node.TYPE_TRANS_SIMPLE);
						postNode.setTime(((TransitionModel)currentPlace).getToolSpecific().getTime());
						postNode.setTimeUnit(getTimeUnitConst(((TransitionModel)currentPlace).getToolSpecific().getTimeUnit()));
						int type = ((OperatorTransitionModel)currentPlace).getOperatorType();
						if ((type == OperatorTransitionModel.AND_JOIN_TYPE) || (type == OperatorTransitionModel.AND_SPLITJOIN_TYPE) || (type == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)){
							postNode.setAndJoin(true);
							postNode.setType(Node.TYPE_AND_JOIN);							
						}
						if ((type == OperatorTransitionModel.AND_SPLIT_TYPE) || (type == OperatorTransitionModel.AND_SPLITJOIN_TYPE) || (type == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)){
							postNode.setAndSplit(true);
							postNode.setType(Node.TYPE_AND_SPLIT);							
						}
                        if ((type == OperatorTransitionModel.XOR_JOIN_TYPE) || (type == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE) || (type == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)) {
                            postNode.setType(Node.TYPE_XOR_JOIN);
						}
                        if ((type == OperatorTransitionModel.XOR_SPLIT_TYPE) || (type == OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE) || (type == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)) {
                            postNode.setType(Node.TYPE_XOR_SPLIT);
						}
						break;
					case AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE:
						postNode.setType(Node.TYPE_TRANS_SIMPLE);
						postNode.setTime(((TransitionModel)currentPlace).getToolSpecific().getTime());
						postNode.setTimeUnit(getTimeUnitConst(((TransitionModel)currentPlace).getToolSpecific().getTimeUnit()));
						break;
					case AbstractPetriNetElementModel.SUBP_TYPE:
						postNode.setType(Node.TYPE_SUBP);
						postNode.setTime(((TransitionModel)currentPlace).getToolSpecific().getTime());
						postNode.setTimeUnit(getTimeUnitConst(((TransitionModel)currentPlace).getToolSpecific().getTimeUnit()));
						break;
					default:
						postNode.setType(0);
					}
				}

				nodeIdx = findNextFreeIndex();
				nodeArray[nodeIdx] = postNode;
			}

			try{
				double p = (mec.findArc(n.getId(), currentPlace.getId())).getProbability();
				
				Arc a = new Arc(nodeArray[nodeIdx], p);
				a.setSource(n);
				n.getSuccessor().add(a);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		while (preNodes.hasNext()){
            AbstractPetriNetElementModel currentNode = preNodes.next();

			int nodeIdx = getNodeIdx(currentNode.getId());
			if (nodeIdx >= nodeArray.length){
				Node preNode = new Node(currentNode.getId(), currentNode.getNameValue());
				switch (currentNode.getType()){
				case AbstractPetriNetElementModel.PLACE_TYPE:
					preNode.setType(Node.TYPE_PLACE);
					break;
				case AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE:
					preNode.setType(Node.TYPE_TRANS_SIMPLE);
					preNode.setTime(((TransitionModel)currentNode).getToolSpecific().getTime());
					break;
				case AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE:
					preNode.setType(Node.TYPE_TRANS_SIMPLE);
					preNode.setTime(((TransitionModel)currentNode).getToolSpecific().getTime());
					break;
				case AbstractPetriNetElementModel.SUBP_TYPE:
					preNode.setType(Node.TYPE_SUBP);
					preNode.setTime(((TransitionModel)currentNode).getToolSpecific().getTime());
					break;
				default:
					preNode.setType(0);
				}
				nodeIdx = findNextFreeIndex();
				nodeArray[nodeIdx] = preNode;
			}
			
			try{
				double p = (mec.findArc(currentNode.getId(), n.getId())).getProbability();
				Arc a = new Arc(nodeArray[nodeIdx], p);
				a.setSource(a.getTarget());
				a.setTarget(n);
				n.getPredecessor().add(a);
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private int findNextFreeIndex(){
		int i = 0;
		boolean found = false;
		
		for (int j = 0; j < nodeArray.length; j++){
			if (nodeArray[j].getId().equals("") && !found){
				i = j;
				found = true;
			}
		}
		
		return i;
	}
	
	public String getZyklen(){
		return zyklen;
	}
	
	public boolean isTransition(String id){
		Node n = nodeArray[getNodeIdx(id)];
		return n.isTransition();
	}
	
	public boolean isTransitionGT0(String id){
		Node n = nodeArray[getNodeIdx(id)];
		return (n.isTransition() && n.getTime() > 0);
	}
	
	public int getNumTransitions(){
		int num = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransition(nodeArray[i].getId()))
				num++;
		}
		
		return num;
	}
	
	public int getNumTransitionsGT0(){
		int num = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransitionGT0(nodeArray[i].getId()))
				num++;
		}
		
		return num;
	}
	
	public String[] getTransitions(){
		String[] trans = new String[getNumTransitions()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransition(nodeArray[i].getId())){
				trans[idx] = nodeArray[i].getName() + " (" + nodeArray[i].getId() + ")";
				idx++;
			}
		}
		
		return trans;
	}
	
	public String[] getTransitionsGT0(){
		String[] trans = new String[getNumTransitionsGT0()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransitionGT0(nodeArray[i].getId())){
				trans[idx] = nodeArray[i].getName() + " (" + nodeArray[i].getId() + ")";
				idx++;
			}
		}
		
		return trans;
	}
	
	public double[] getTimes(){
		double[] times = new double[getNumTransitions()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransition(nodeArray[i].getId())){
				times[idx] = nodeArray[i].getTime();
				idx++;
			}
		}
		
		return times;
	}
	
	public double[] getTimesGT0(){
		double[] times = new double[getNumTransitionsGT0()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransitionGT0(nodeArray[i].getId())){
				times[idx] = nodeArray[i].getTime();
				idx++;
			}
		}
		
		return times;
	}
	
	public double[] getRuns(){
		double[] runs = new double[getNumTransitions()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransition(nodeArray[i].getId())){
				runs[idx] = nodeArray[i].getNumOfRuns();
				idx++;
			}
		}
		
		return runs;
	}
	
	public double[] getRunsGT0(){
		double[] runs = new double[getNumTransitionsGT0()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransitionGT0(nodeArray[i].getId())){
				runs[idx] = nodeArray[i].getNumOfRuns();
				idx++;
			}
		}
		
		return runs;
	}
	
	public Node[] getNodeArray(){
		return nodeArray.clone();
	}
	
	public ArrayList<Node> getInputNodes(Node n){
		ArrayList<Node> list = new ArrayList<Node>();
		for (int i = 0; i < nodeArray.length; i++){
			for (Arc a: nodeArray[i].getSuccessor()){
				if (n.equals(a.target))
					list.add(nodeArray[i]);
			}
		}
		
		return list;
	}

	public Node getSinkPlace() {
		return sinkPlace;
	}

	public void setSinkPlace(Node sinkPlace) {
		this.sinkPlace = sinkPlace;
	}
	
	private int getTimeUnitConst(int tu){
		return tu;
	}
}
