package org.woped.quantana.sim;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;

public class SimGraph {
	Map<String, SimNode> Nodes = new HashMap<String, SimNode>();
	ModelElementContainer mec = null;
	SimNode sink = null;
	SimNode source = null;	
	Map<String, AbstractPetriNetElementModel> allTrans = null;

	/**
	 * 
	 * @param mec
	 * @param sa
	 * @param res
	 */
	public SimGraph(IEditor editor) {
		this.mec = editor.getModelProcessor().getElementContainer();
		IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);		
		buildGraph();		
		AbstractPetriNetElementModel el = (AbstractPetriNetElementModel) qualanService.getSinkPlaces().iterator().next();
		sink = Nodes.get(el.getId());
		el = (AbstractPetriNetElementModel) qualanService.getSourcePlaces().iterator().next();
		source = Nodes.get(el.getId());				
	}

	public String[] getTransitions(){
		String[] transitions = new String[allTrans.size()];
		int ind = 0;
		for(SimNode n :Nodes.values()){
			if (n.isTransition()){
				transitions[ind]=n.getname()+ " (" + n.getid() + ")";
				ind++;
			}
		}		
		return transitions;
	}
	
	public int getNumTransitionsGT0(){
        int num = 0;
        for(SimNode n :Nodes.values()){
        	if(isTransitionGT0(n.getid())) num++;
        }        
        return num;
	}


public boolean isTransitionGT0(String id){
        SimNode n = Nodes.get(id);        	
        return (n.isTransition() && n.gettime() > 0);
  }

	
	public String[] getTransitionsGT0(){
        String[] trans = new String[getNumTransitionsGT0()];
        int idx = 0;
        for(SimNode n :Nodes.values()){
              if (isTransitionGT0(n.getid())){
                    trans[idx] = n.getname() + " (" + n.getid() + ")";
                    idx++;
              }
        }        
        return trans;
  }

	
	SimNode createSimNode(AbstractPetriNetElementModel el) {
		SimNode n = new SimNode(el.getId(), el.getNameValue());
		switch (el.getType()) {
		case AbstractPetriNetElementModel.PLACE_TYPE:
			n.settype(SimNode.NT_PLACE);
			break;
		case AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE:
			n.settype(SimNode.NT_TRANSITION);
			n.settime(((TransitionModel) el).getToolSpecific().getTime());
			n.settimeunit(((TransitionModel) el).getToolSpecific()
					.getTimeUnit());
			int type = ((OperatorTransitionModel) el).getOperatorType();
			if ((type == OperatorTransitionModel.AND_JOIN_TYPE)
					|| (type == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
					|| (type == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)) {
				n.setandjoin(true);
				n.settype(SimNode.NT_AND_JOIN);
			}
			if ((type == OperatorTransitionModel.AND_SPLIT_TYPE)
					|| (type == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
					|| (type == OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)) {
				n.setandsplit(true);
				n.settype(SimNode.NT_AND_SPLIT);
			}
			break;
		case AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE:
			n.settype(SimNode.NT_TRANSITION);
			n.settime(((TransitionModel) el).getToolSpecific().getTime());
			n.settimeunit(((TransitionModel) el).getToolSpecific()
					.getTimeUnit());
			// check implicit and join/split
			if (mec.getSourceElements(el.getId()).size() > 1) {
				n.setandjoin(true);
				n.settype(SimNode.NT_AND_JOIN);
			}
			if (mec.getTargetElements(el.getId()).size() > 1) {
				n.setandsplit(true);
				n.settype(SimNode.NT_AND_SPLIT);
			}
			break;
		case AbstractPetriNetElementModel.SUBP_TYPE:
			n.settype(SimNode.NT_TRANSITION);
			n.settime(((TransitionModel) el).getToolSpecific().getTime());
			n.settimeunit((byte) ((TransitionModel) el).getToolSpecific()
					.getTimeUnit());
			break;
		default:
			n.settype(SimNode.NT_UNKNOWN);
		}
		return n;
	}

	void buildGraph() {
		// first run through all elements creates the map with all SimNodes
		AbstractPetriNetElementModel el = null;
		SimNode node = null;
		for (Iterator<?> i = mec
				.getElementsByType(AbstractPetriNetElementModel.PLACE_TYPE).values()
				.iterator(); i.hasNext();) {
			el = (AbstractPetriNetElementModel) i.next();
			Nodes.put(el.getId(), createSimNode(el));
		}
		allTrans = mec.getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
		allTrans.putAll(mec.getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
		allTrans.putAll(mec.getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE));		
		for (Iterator<AbstractPetriNetElementModel> i = allTrans.values().iterator(); i.hasNext();) {
			el = (AbstractPetriNetElementModel) i.next();
			node = createSimNode(el);
			Nodes.put(el.getId(), node);			
			addResource(el, node);			
		}
		
		// second run through all elements creates the arc with probability and
		// pre and post SimNodes
		createArcPrePostNode();		
	}

	public void createArcPrePostNode() {
		for (SimNode n : Nodes.values()) {
			for (Iterator<?> target = mec.getTargetElements(n.getid()).values()
					.iterator(); target.hasNext();) {
				AbstractPetriNetElementModel postNode = (AbstractPetriNetElementModel) target
						.next();
				double p = (mec.findArc(n.getid(), postNode.getId()))
						.getProbability();
				if (p == 0.0)
					p = 1;
				SimArc arc = new SimArc(n, Nodes.get(postNode.getId()), p);
				n.getarcOut().add(arc);
			}
			for (Iterator<?> source = mec.getSourceElements(n.getid()).values()
					.iterator(); source.hasNext();) {
				AbstractPetriNetElementModel preNode = (AbstractPetriNetElementModel) source
						.next();
				SimArc arc = new SimArc(n, Nodes.get(preNode.getId()));
				n.getarcIn().add(arc);
			}
		}
	}

	void addResource(AbstractPetriNetElementModel el, SimNode n) {
		TransitionModel tm = (TransitionModel) el;
		TransitionResourceModel resMod = tm.getToolSpecific().getTransResource();
		if (resMod != null) {
			n.setrole(resMod.getTransRoleName());
			n.setgroup(resMod.getTransOrgUnitName());
			n.setHasResource(true);
		}else{ 
			TriggerModel t = tm.getToolSpecific().getTrigger();
			if ((t!=null)&&(t.getTriggertype()!=TriggerModel.TRIGGER_RESOURCE))						
				n.settime(0);
		}
	}

	public String toString() {
		String text = "Graph starts with " + source + "\n\n";

		for (SimNode n : Nodes.values()) {
			text += n + " >> [ ";
			for (Iterator<SimArc> a = n.getarcOut().iterator(); a.hasNext();) {
				SimArc ar = a.next();
				text += "(" + ar.getTarget() + "(" + ar.getProbability() + "))";
			}
			text += " ]\n";

			text += n + " << [ ";
			for (Iterator<SimArc> i = n.getarcIn().iterator(); i.hasNext();) {
				SimArc a = (SimArc) i.next();
				text += a.getTarget();
			}
			text += " ]\n";

		}
		return text;
	}

	public Map<String, SimNode> getNodes() {
		return Nodes;
	}
	
	public SimNode getSource(){
		return source;
	}
	
	public SimNode getSink(){
		return sink;
	}
	
}