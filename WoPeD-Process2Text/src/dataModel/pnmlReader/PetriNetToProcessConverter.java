package dataModel.pnmlReader;

import java.util.HashMap;

import dataModel.pnmlReader.PetriNet.Element;
import dataModel.pnmlReader.PetriNet.PetriNet;
import dataModel.process.Activity;
import dataModel.process.ActivityType;
import dataModel.process.Arc;
import dataModel.process.Gateway;
import dataModel.process.GatewayType;
import dataModel.process.Lane;
import dataModel.process.Pool;
import dataModel.process.ProcessModel;

public class PetriNetToProcessConverter {
	
	public HashMap<String, Integer> transformedElems;
	public HashMap<Integer, String> transformedElemsRev;
	
	public ProcessModel convertToProcess(PetriNet petriNet) {
		
		ProcessModel model = new ProcessModel(1, "PetriNet");
		Pool pool = new Pool(model.getNewId(), "");
		Lane lane = new Lane(model.getId(), "", pool);
		model.addPool("");
		model.addLane("");
		
		transformedElems = new HashMap<String, Integer>();
		transformedElemsRev = new HashMap<Integer, String>();
		
		Element startElem = petriNet.getElements().get(petriNet.getStartPlace());
		transformElem(startElem, -1, petriNet, model, pool, lane);
		return model;
	}
	
	
	
	
	private void transformElem(Element elem, int precElem, PetriNet petriNet, ProcessModel model, Pool pool, Lane lane) {
		// Id of current petri net element
		String elemId = elem.getId();
		// If element not already ecists
		if (!transformedElems.keySet().contains(elemId)) {
			
			// Places ...
			if (elem.getClass().toString().endsWith("Place")) {

				// Simple place with 1 or more incoming and no outgoing arc
				if (petriNet.getSuccessor(elemId).size() == 0 && petriNet.getPredecessor(elemId).size() >= 1) {
					int newId = model.getNewId();
					model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.XOR));
					transformedElems.put(elemId, newId);
					transformedElemsRev.put(newId, elemId);
					if (precElem != -1){
						model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
					}
				}
				
				// Simple place with 1 incoming and one outgoing arc
				if (petriNet.getSuccessor(elemId).size() == 1 && petriNet.getPredecessor(elemId).size() <= 1) {
						String suc = petriNet.getSuccessor(elemId).get(0);
						transformElem(petriNet.getElements().get(suc), precElem, petriNet, model, pool, lane);
				}
				
				//  Place with multiple outgoing arcs (XOR-Join)
				if (petriNet.getSuccessor(elemId).size() == 1 && petriNet.getPredecessor(elemId).size() > 1) {
					
					// Create new element
					int newId = model.getNewId();
					model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.XOR));
					transformedElems.put(elemId, newId);
					transformedElemsRev.put(newId, elemId);
					if (precElem != -1){
						model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
					}
					
					// Recursively go through the model
					String suc = petriNet.getSuccessor(elemId).get(0);
					transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
				}
				
				// Place with multiple incoming arcs (XOR-Split)
				if (petriNet.getSuccessor(elemId).size() > 1 && petriNet.getPredecessor(elemId).size() == 1) {
					
					// Create new element
					int newId = model.getNewId();
					model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.XOR));
					transformedElems.put(elemId, newId);
					transformedElemsRev.put(newId, elemId);
					if (precElem != -1){
						model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));	
					}
					
					// Recursively go through the model
					for (String suc: petriNet.getSuccessor(elemId)) {
						transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
					}
				}
				
			// Transitions...	
			} else {

				// Simple place with 1 incoming and one outgoing arc
				if (petriNet.getSuccessor(elemId).size() == 1 && petriNet.getPredecessor(elemId).size() == 1) {
					int newId = model.getNewId();
					model.addActivity(new Activity(newId, elem.getLabel(), null, null, ActivityType.NONE));
					transformedElems.put(elemId, newId);
					transformedElemsRev.put(newId, elemId);
					if (precElem != -1){
						model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
					}
					// Recursively go through the model
					for (String suc: petriNet.getSuccessor(elemId)) {
						transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool,lane);
					}
				}
				
			//  Transition with multiple incoming arcs (AND-Join)
				if (petriNet.getSuccessor(elemId).size() == 1 && petriNet.getPredecessor(elemId).size() > 1) {
					
					// Create new element
					int newId = model.getNewId();
					model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.AND));
					transformedElems.put(elemId, newId);
					transformedElemsRev.put(newId, elemId);
					if (precElem != -1){
						model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
					}
					
					// Recursively go through the model
					String suc = petriNet.getSuccessor(elemId).get(0);
					transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool, lane);
				}
				
			//  Transition with multiple incoming arcs (AND-Join)
				if (petriNet.getSuccessor(elemId).size() > 1 && petriNet.getPredecessor(elemId).size() == 1) {
					
					// Create new element
					int newId = model.getNewId();
					model.addGateway(new Gateway(newId, "", lane, pool, GatewayType.AND));
					transformedElems.put(elemId, newId);
					transformedElemsRev.put(newId, elemId);
					if (precElem != -1){
						model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(newId)));
					}
					
					// Recursively go through the model
					for (String suc: petriNet.getSuccessor(elemId)) {
						transformElem(petriNet.getElements().get(suc), newId, petriNet, model, pool,lane);
					}
				}
			}
		} else {
			model.addArc(new Arc(model.getNewId(), "", model.getElem(precElem), model.getElem(transformedElems.get(elem.getId()))));
		}
	}

}
