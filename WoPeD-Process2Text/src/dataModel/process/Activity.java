package dataModel.process;

import java.util.ArrayList;


public class Activity extends Element {

	private int type;
	private ArrayList <Annotation> annotations;
	private ArrayList <Integer> attachedEvents;
	
	public Activity(int id, String label, Lane lane, Pool pool, int type) {
		super(id, label, lane, pool);
		this.type = type;
		annotations = new ArrayList<Annotation>();
		attachedEvents = new ArrayList<Integer>();
	}
	
	public boolean hasAttachedEvents() {
		return attachedEvents.size() > 0;
	}
	
	public ArrayList<Integer> getAttachedEvents() {
		return attachedEvents;
	}
	
	public void addAttachedEvent(Integer id) {
		attachedEvents.add(id);
	}
	
	public ArrayList<Annotation> getAnnotations() {
		return annotations;
	}

	public int getType() {
		return type;
	}
	
	public void addAnnotation(Annotation a) {
		annotations.add(a);
	}

}
