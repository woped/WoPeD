package org.woped.quantana.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.vc.StructuralAnalysis;

public class WorkflowNetGraph {
	static String zyklen = "";

	Node[] nodeArray;
//	Node[] cycleArray;
	Node sourcePlace = null;
	Node sinkPlace = null;
	LinkedList<Node> path = new LinkedList<Node>();
	ModelElementContainer mec = null;
	
	public WorkflowNetGraph(StructuralAnalysis sa, ModelElementContainer mec){
		this.mec = mec;
		zyklen = "";
		
		int numNodes = mec.getRootElements().size(); //sa.getNumPlaces() + sa.getNumTransitions();
		//Iterator iter = mec.getRootElements().iterator();
//		while (iter.hasNext())
//			numNodes++;
		
		nodeArray = new Node[numNodes];
//		cycleArray = new Node[numNodes];
		int nextIdx = 0;
		
		AbstractPetriNetModelElement source = (AbstractPetriNetModelElement)sa.getSourcePlacesIterator().next();
		sourcePlace = new Node(source.getId(), source.getNameValue());
		sourcePlace.setType(Node.TYPE_PLACE);
		
		AbstractPetriNetModelElement sink = (AbstractPetriNetModelElement)sa.getSinkPlacesIterator().next();
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
//			cycleArray[i] = new Node(n.getId(), n.getName());
			//cycleArray[i].successor.clear();
			if (n.getType() == Node.TYPE_TRANS){
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
	
//	private boolean exists(String id){
//		for (int i = 0; i < nodeArray.length; i++){
//			if (nodeArray[i].id.equals(id))
//				return true;
//		}
//		
//		return false;
//	}
	
	public int getNodeIdx(String id){
		int f = nodeArray.length;
		for (int i = 0; i < f; i++){
			if (nodeArray[i].getId().equals(id))
				f = i;
		}
		
		return f;
	}
	
	/*public void findCycles(Node n) {
		if (n.markiert == Node.BUSY) {
			//System.out.print("\nZyklus " + i + ": ");
			zyklen += "\nZyklus : ";
			
			int idx = path.indexOf(n);
			if (idx >= 0){
				while (idx < path.size()){
					Node t = (Node)path.get(idx);
					zyklen += t;
					path.remove(idx);
				}
			}
			
		} else if (n.markiert == Node.NOT_STARTED) {
			n.markiert = Node.BUSY;
			path.add(n);
			for (Arc a : n.successor)
				findCycles(a.target);
			n.markiert = Node.FINISHED;
			if (!path.isEmpty())
				path.removeLast();
		}
	}*/
	
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

			//Vorgänger
			text += n + " << [ ";

			for (Arc a : n.getPredecessor()){
				text += a.target;
			}

			text += " ]\n";
		}
		
		return text;
	}
	
	/*public String cylcesToString(){
		String text = "Cycles in Graph\n\n";
		
		for (int i = 0; i < cycleArray.length; i++){
			Node n = cycleArray[i];
			text += n + " >> [ ";
			
			for (Arc a : n.getSuccessor()){
				text += a.target;
			}
			
			text += " ]\n";
		}
		
		return text;
	}*/
	
	public Node getStartPlace(){
		return sourcePlace;
	}
	
	private void buildGraph(Node n){
		Iterator postNodes = mec.getTargetElements(n.getId()).values().iterator();
		Iterator preNodes = mec.getSourceElements(n.getId()).values().iterator();

		if (mec.getTargetElements(n.getId()).size() > 1)
			n.setFork(true);
		
		while (postNodes.hasNext()){
			AbstractPetriNetModelElement currentPlace = (AbstractPetriNetModelElement) postNodes.next();

			int nodeIdx = getNodeIdx(currentPlace.getId());
			Node postNode = null;
			if (nodeIdx >= nodeArray.length){
				String id = currentPlace.getId();
				if (id.equals(sinkPlace.getId())){
					postNode = sinkPlace;
				} else {
					postNode = new Node(id, currentPlace.getNameValue());
					//double m = 1.0;
					switch (currentPlace.getType()){
					case AbstractPetriNetModelElement.PLACE_TYPE:
						postNode.setType(Node.TYPE_PLACE);
						break;
					case AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE:
						postNode.setType(Node.TYPE_TRANS);
						postNode.setTime(((TransitionModel)currentPlace).getToolSpecific().getTime());
						postNode.setTimeUnit(getTimeUnitConst(((TransitionModel)currentPlace).getToolSpecific().getTimeUnit()));
						int type = ((OperatorTransitionModel)currentPlace).getOperatorType();
						if ((type == OperatorTransitionModel.AND_JOIN_TYPE) || (type == OperatorTransitionModel.AND_SPLITJOIN_TYPE) || (type == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)){
							postNode.setAndJoin(true);
						}
						if ((type == OperatorTransitionModel.AND_SPLIT_TYPE) || (type == OperatorTransitionModel.AND_SPLITJOIN_TYPE) || (type == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)){
							postNode.setAndSplit(true);
						}
						break;
					case AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE:
						postNode.setType(Node.TYPE_TRANS);
						postNode.setTime(((TransitionModel)currentPlace).getToolSpecific().getTime());
						postNode.setTimeUnit(getTimeUnitConst(((TransitionModel)currentPlace).getToolSpecific().getTimeUnit()));
						break;
					case AbstractPetriNetModelElement.SUBP_TYPE:
						postNode.setType(Node.TYPE_SUBP);
						postNode.setTime(((TransitionModel)currentPlace).getToolSpecific().getTime());
						postNode.setTimeUnit(getTimeUnitConst(((TransitionModel)currentPlace).getToolSpecific().getTimeUnit()));
						break;
					default:
						postNode.setType(0);
					}
				}

				//postNode.setMultiply(m);

				nodeIdx = findNextFreeIndex();
				nodeArray[nodeIdx] = postNode;
			}

			try{
				double p = (mec.findArc(n.getId(), currentPlace.getId())).getProbability();
				
				// Kompatibilität zu alten Netzen
				if (p == 0.0) p = 1.0;
				
				Arc a = new Arc(nodeArray[nodeIdx], p);
				a.setSource(n);
				n.getSuccessor().add(a);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		while (preNodes.hasNext()){
			AbstractPetriNetModelElement currentNode = (AbstractPetriNetModelElement) preNodes.next();

			int nodeIdx = getNodeIdx(currentNode.getId());
			if (nodeIdx >= nodeArray.length){
				Node preNode = new Node(currentNode.getId(), currentNode.getNameValue());
				switch (currentNode.getType()){
				case AbstractPetriNetModelElement.PLACE_TYPE:
					preNode.setType(Node.TYPE_PLACE);
					break;
				case AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE:
					preNode.setType(Node.TYPE_TRANS);
					preNode.setTime(((TransitionModel)currentNode).getToolSpecific().getTime());
					break;
				case AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE:
					preNode.setType(Node.TYPE_TRANS);
					preNode.setTime(((TransitionModel)currentNode).getToolSpecific().getTime());
					break;
				case AbstractPetriNetModelElement.SUBP_TYPE:
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
				
				//n.predecessor.add(new Arc(nodeArray[nodeIdx]));
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
	
	/*private void tarjan(Node v, int max_dfs, LinkedList<Node> s){
		//String text = "";
		
		if (v.markiert == Node.NOT_STARTED){
			v.dfs = max_dfs;
			v.lowlink = max_dfs;
			max_dfs++;

			s.push(v);
			v.stacked = true;
			v.markiert = Node.FINISHED;
			
			for (Arc a : v.successor){
				Node t = a.target;
				if (t.markiert == Node.NOT_STARTED){
					//text += 
					tarjan(t, max_dfs, s);
					v.lowlink = StrictMath.min(v.lowlink,t.lowlink);
				} else if (t.stacked)
					v.lowlink = StrictMath.min(v.lowlink, t.dfs);
			}
			
			if (v.lowlink == v.dfs){
				//text += "\nSZK (" + v.dfs + "): ";
				int idx = getNodeIdx(v.id);
				//ArrayList<Arc> list = cycleArray[idx].successor;
				Node root = cycleArray[idx];
				
				Node u = s.pop();
				u.stacked = false;
				while (!u.equals(v)){
					//text += u;
					if (!root.foundCycle)
						root.successor.add(new Arc(cycleArray[getNodeIdx(u.id)]));
					
					u = s.pop();
					u.stacked = false;
				}
				//text += u;
				if (!root.foundCycle)
					root.successor.add(new Arc(cycleArray[getNodeIdx(u.id)]));
				
				root.foundCycle = true;
			}
		}
		
		//return text;
	}
	
	public void findCycles_T(){
		//String text = "";
		for (int i = 0; i < nodeArray.length; i++){
			int max_dfs = 0; 
			LinkedList<Node> stack = new LinkedList<Node>();
			resetMarking();
			//text = 
			tarjan(nodeArray[i], max_dfs, stack);
		}
	}*/
	
	/*public boolean isPartOfCycle(String id){
		return (cycleArray[getNodeIdx(id)].getSuccessor().size() > 1);
	}
	
	public ArrayList<Arc> getCycle(String id){
		return cycleArray[getNodeIdx(id)].getSuccessor();
	}*/
	
	public boolean isTransition(String id){
		int type = nodeArray[getNodeIdx(id)].getType();
		return ((type == Node.TYPE_TRANS) || (type == Node.TYPE_SUBP));
	}
	
	public boolean isTransitionGT0(String id){
		Node n = nodeArray[getNodeIdx(id)];
		int type = n.getType();
		return (((type == Node.TYPE_TRANS) || (type == Node.TYPE_SUBP)) && n.getTime() > 0);
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
	
	/*public String[] getGroupRoles(){
		String[] gr = new String[getNumTransitions()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransition(nodeArray[i].getId())){
				gr[idx] = nodeArray[i].getGroupRole();
				idx++;
			}
		}
		
		return gr;
	}
	
	public String[] getGroupRolesGT0(){
		String[] gr = new String[getNumTransitionsGT0()];
		int idx = 0;
		for (int i = 0; i < nodeArray.length; i++){
			if (isTransitionGT0(nodeArray[i].getId())){
				gr[idx] = nodeArray[i].getGroupRole();
				idx++;
			}
		}
		
		return gr;
	}*/
	
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
