package org.woped.quantana.sim;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;

public class SimGraph {
	Map<String, SimNode> Nodes = new HashMap<String, SimNode>();
	ModelElementContainer supermec = null;
	SimNode sink = null;
	SimNode source = null;

	/**
	 * 
	 * @param mec
	 * @param sa
	 * @param res
	 */
	public SimGraph(IEditor editor) {
		this.supermec = editor.getModelProcessor().getElementContainer();
		IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);		
		buildGraph(this.supermec, "");
		AbstractPetriNetElementModel el = qualanService.getSinkPlaces().iterator().next();
		sink = Nodes.get(el.getId());
		el = qualanService.getSourcePlaces().iterator().next();
		source = Nodes.get(el.getId());
		resourceCheck(editor);
	}

	private void resourceCheck(IEditor editor) {
		Vector<String> problemList = new Vector<String>();
		for (SimNode n : Nodes.values()) {
			if (n.hasResource()) {
				if (numResources(editor, n.getrole(), n.getgroup()) < 1) {
					problemList.add(n.getname());
				}
			}
		}
		if (problemList.size() > 0) {
			String MsgText = Messages.getString("QuantAna.Simulation.resourceProblem");
			for (String s : problemList) {
				MsgText += "\n"+s;
			}
			JOptionPane.showMessageDialog(null, MsgText, Messages.getString("QuantAna.Simulation.resourceProblemTitle"), JOptionPane.WARNING_MESSAGE);
		}
	}

	private int numResources(IEditor editor, String role, String group) {
	    Vector<?> res = editor.getModelProcessor().getResources();
		int count = 0;
		Vector<?> rlist;

		for (int i = 0; i < res.size(); i++) {
			String name = res.get(i).toString();
	        rlist = editor.getModelProcessor().getResourceClassesResourceIsAssignedTo(name);

			if (rlist.contains(role) && rlist.contains(group)) {
				count++;
			}
		}
		return count;
	}

	public String[] getTransitions(){
		Vector<String> transitions = new Vector<String>();
		for(SimNode n :Nodes.values()){
			if (n.isTransition()){
				transitions.addElement(n.getname()+ " (" + n.getid() + ")");
			}
		}		
		return transitions.toArray(new String[transitions.size()]);
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

	
	SimNode createSimNode(AbstractPetriNetElementModel el, ModelElementContainer mec, String namePrefix) {
		SimNode n = new SimNode(el.getId(), el.getNameValue());
		switch (el.getType()) {
		case AbstractPetriNetElementModel.PLACE_TYPE:
			n.settype(SimNode.NT_PLACE);
			n.setid(namePrefix+n.getid());
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
		default:
			n.settype(SimNode.NT_UNKNOWN);
		}
		return n;
	}

	Map<String, SimNode> buildGraph(ModelElementContainer mec, String namePrefix) {
		Map<String, SimNode> myNodes = new HashMap<String, SimNode>();
		// first run through all elements creates the map with all SimNodes
		SimNode node = null;
		for (AbstractPetriNetElementModel el : mec.getElementsByType(AbstractPetriNetElementModel.PLACE_TYPE).values()) {
			myNodes.put(namePrefix+el.getId(), createSimNode(el, mec, namePrefix));
		}
		Map<String, AbstractPetriNetElementModel> allTrans = mec.getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
		allTrans.putAll(mec.getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE));
		allTrans.putAll(mec.getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE));		
		for (AbstractPetriNetElementModel el : allTrans.values()) {
			if (el.getType() != AbstractPetriNetElementModel.SUBP_TYPE) {
				node = createSimNode(el, mec, namePrefix);
				myNodes.put(el.getId(), node);
				addResource(el, node);
			} else {
				SimNode entryNode = new SimNode(el.getId()+"_SubProcessEntry", el.getNameValue()+"_SubProcessEntry");
				entryNode.settype(SimNode.NT_SUBPROCESS);
				entryNode.settime(((TransitionModel) el).getToolSpecific().getTime());
				entryNode.settimeunit((byte) ((TransitionModel) el).getToolSpecific().getTimeUnit());
				myNodes.put(entryNode.getId(), entryNode);

				SimNode exitNode = new SimNode(el.getId()+"_SubProcessExit", el.getNameValue()+"_SubProcessExit");
				exitNode.settype(SimNode.NT_SUBPROCESS);
				exitNode.settime(((TransitionModel) el).getToolSpecific().getTime());
				exitNode.settimeunit((byte) ((TransitionModel) el).getToolSpecific().getTimeUnit());
				myNodes.put(exitNode.getId(), exitNode);

				ModelElementContainer subModelContainer = ((SubProcessModel)el).getElementContainer();
				Map<String, SimNode> subNodes = buildGraph(subModelContainer, el.getNameValue()+"_" + namePrefix);

				SimNode subSink = null;
				SimNode subSource = null;
				for (SimNode n : subNodes.values()) {
					if (n.gettype() == SimNode.NT_PLACE && n.getarcIn().size() == 0) {
						subSource = n;
					}
					if (n.gettype() == SimNode.NT_PLACE && n.getarcOut().size() == 0) {
						subSink = n;
					}
				}
				entryNode.getarcOut().add(new SimArc(entryNode, subSource));
				exitNode.getarcIn().add(new SimArc(exitNode, subSink));
				subSource.getarcIn().add(new SimArc(subSource, entryNode));
				subSink.getarcOut().add(new SimArc(subSink, exitNode));
			}
		}
		// second run through all elements creates the arc with probability and
		// pre and post SimNodes
		createArcPrePostNode(mec, myNodes, namePrefix);
		Nodes.putAll(myNodes);
		return myNodes;
	}

	public void createArcPrePostNode(ModelElementContainer mec, Map<String, SimNode> myNodes, String namePrefix) {
		for (SimNode n : myNodes.values()) {
			String nid;
			if (n.gettype() == SimNode.NT_SUBPROCESS) {
				nid = n.getid().substring(0, n.getid().lastIndexOf('_'));
				if (n.getid().endsWith("_SubProcessExit")) {
					for (AbstractPetriNetElementModel postNode : mec.getTargetElements(nid).values()) {
						double p = (mec.findArc(nid, postNode.getId())).getProbability();
						if (p == 0.0)
							p = 1;
						SimArc arc;
						if (postNode.getType() == AbstractPetriNetElementModel.SUBP_TYPE)
							arc = new SimArc(n, myNodes.get(postNode.getId()+"_SubProcessEntry"), p);
						else
							if (postNode.getType() == AbstractPetriNetElementModel.PLACE_TYPE)
								arc = new SimArc(n, myNodes.get(namePrefix+postNode.getId()), p);
							else
								arc = new SimArc(n, myNodes.get(postNode.getId()), p);
						n.getarcOut().add(arc);
					}
				}
				if (n.getid().endsWith("SubProcessEntry")) {
					for (AbstractPetriNetElementModel preNode : mec.getSourceElements(nid).values()) {
						SimArc arc;
						if (preNode.getType() == AbstractPetriNetElementModel.SUBP_TYPE)
							arc = new SimArc(n, myNodes.get(preNode.getId()+"_SubProcessExit"));
						else
							if (preNode.getType() == AbstractPetriNetElementModel.PLACE_TYPE)
								arc = new SimArc(n, myNodes.get(namePrefix+preNode.getId()));
							else
								arc = new SimArc(n, myNodes.get(preNode.getId()));
						n.getarcIn().add(arc);
					}
				}
			} else {
				nid = n.getid();
				if (n.gettype() == SimNode.NT_PLACE) {
					nid = nid.substring(namePrefix.length());
				}
				for (AbstractPetriNetElementModel postNode : mec.getTargetElements(nid).values()) {
					double p = (mec.findArc(nid, postNode.getId())).getProbability();
					if (p == 0.0)
						p = 1;
					SimArc arc;
					if (postNode.getType() == AbstractPetriNetElementModel.SUBP_TYPE)
						arc = new SimArc(n, myNodes.get(postNode.getId()+"_SubProcessEntry"), p);
					else
						if (postNode.getType() == AbstractPetriNetElementModel.PLACE_TYPE)
							arc = new SimArc(n, myNodes.get(namePrefix+postNode.getId()), p);
						else
							arc = new SimArc(n, myNodes.get(postNode.getId()), p);
					n.getarcOut().add(arc);
				}
				for (AbstractPetriNetElementModel preNode : mec.getSourceElements(nid).values()) {
					SimArc arc;
					if (preNode.getType() == AbstractPetriNetElementModel.SUBP_TYPE)
						arc = new SimArc(n, myNodes.get(preNode.getId()+"_SubProcessExit"));
					else
						if (preNode.getType() == AbstractPetriNetElementModel.PLACE_TYPE)
							arc = new SimArc(n, myNodes.get(namePrefix+preNode.getId()));
						else
							arc = new SimArc(n, myNodes.get(preNode.getId()));
					n.getarcIn().add(arc);
				}
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
				SimArc a = i.next();
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