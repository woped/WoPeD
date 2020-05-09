/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The super-structure which holds all elements
 * which were extracted during the transformation phase
 *
 */
public class WorldModel {
	
	private ArrayList<Action> f_actions = new ArrayList<Action>();
	private ArrayList<Actor> f_actors = new ArrayList<Actor>();
	private ArrayList<Resource> f_resources = new ArrayList<Resource>();
	private ArrayList<Flow> f_flows = new ArrayList<Flow>();
	private Flow f_lastFlowAdded = null;

	
	public void addAction(Action a) {
		f_actions.add(a);
		addAllElementsInSpecifiers(a);
		if(a.getXcomp() != null)
			addAllElementsInSpecifiers(a.getXcomp());
	}
	
	public void addActor(Actor a) {
		f_actors.add(a);
		addAllElementsInSpecifiers(a);
	}

	private void addAllElementsInSpecifiers(SpecifiedElement a) {
		for(Specifier spec:a.getSpecifiers()) {
			if(spec.getObject() != null) {
				if(spec.getObject() instanceof Actor) {
					addActor((Actor) spec.getObject());
				}else {
					addResource((Resource) spec.getObject());
				}
			}
		}
	}
	
	public void addResource(Resource r) {
		f_resources.add(r);
		addAllElementsInSpecifiers(r);
	}
	
	public void addFlow(Flow f) {
		if(f.getSingleObject() != null) {
			f_flows.add(f);
			f_lastFlowAdded = f;
		}else {
			System.err.println("Tried to add null flow!");
		}
	}
	
	public Flow getLastFlowAdded() {
		return f_lastFlowAdded;
	}
	
	
	/**
	 * @return the f_actions
	 */
	public List<Action> getActions() {
		return f_actions;
	}
	
	/**
	 * @param sentence
	 * @return
	 */
	public List<Action> getActions(T2PSentence sentence) {
		ArrayList<Action> _result = new ArrayList<Action>();
		for(Action a:getActions()) {
			if(a.getOrigin().equals(sentence)) {
				_result.add(a);
			}
		}
		return _result;
	}
	
	/**
	 * @return the f_actors
	 */
	public List<Actor> getActors() {
		return f_actors;
	}
	
	/**
	 * @param sentence
	 * @return
	 */
	public List<Actor> getActors(T2PSentence sentence) {
		ArrayList<Actor> _result = new ArrayList<Actor>();
		for(Actor a:getActors()) {
			if(a.getOrigin().equals(sentence)) {
				_result.add(a);
			}
		}
		return _result;
	}
	
	
	/**
	 * @return the f_resources
	 */
	public List<Resource> getResources() {
		return f_resources;
	}
	
	/**get all Resources, that the sentence contains
	 *
	 * @param sentence
	 * @return List of resources that the sentence contains
	 */
	public List<Resource> getResources(T2PSentence sentence) {
		ArrayList<Resource> _result = new ArrayList<Resource>();
		for(Resource r:getResources()) {
			if(r.getOrigin().equals(sentence)) {
				_result.add(r);
			}
		}
		return _result;
	}
	
	/**
	 * @return the f_flows
	 */
	public ArrayList<Flow> getFlows() {
		return f_flows;
	}

	/**
	 * 
	 */
	public void clear() {
		f_flows.clear();
		f_actions.clear();
		f_actors.clear();
		f_resources.clear();
	}

	/**
	 * @return
	 */
	public ArrayList<ExtractedObject> getElements() {
		ArrayList<ExtractedObject> _list = new ArrayList<ExtractedObject>();
		_list.addAll(f_actors);
		_list.addAll(f_resources);
		return _list;
	}

	/**
	 * an action which e.g. was merged into another one
	 * can be removed from the system
	 * @param me
	 */
	public void removeAction(Action me) {
		f_actions.remove(me);
	}

	/** Switches position of Action a with action b
	 * @param a
	 * @param b
	 */
	public void switchPositions(Action a, Action b) {
		int _idA = f_actions.indexOf(a);
		int _idB = f_actions.indexOf(b);
		f_actions.set(_idA, b);
		f_actions.set(_idB, a);
	}

	
	

}
