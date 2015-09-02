package dataModel.process;

import java.util.ArrayList;
import java.util.HashMap;

import contentDetermination.labelAnalysis.EnglishLabelCategorizer;
import contentDetermination.labelAnalysis.EnglishLabelDeriver;
import contentDetermination.labelAnalysis.EnglishLabelHelper;
import contentDetermination.labelAnalysis.EnglishLabelProperties;


public class ProcessModel {
	
	private int id;
	private String name;
	private HashMap <Integer,Arc> arcs;
	private HashMap <Integer, Activity> activities;
	private HashMap <Integer,Event> events;
	private HashMap <Integer,Gateway> gateways;
	private ArrayList<String> lanes;
	private ArrayList<String> pools;
	private HashMap<Integer,ProcessModel> alternativePaths;
	
	public static final int VOS = 0;
	public static final int AN = 1;
	public static final int INVESTIGATE = 2;
	
	public int getNewId() {
		int base = 0;
		
		for (int i: arcs.keySet()) {
			if (i > base) {
				base = i;
			}
		}
		for (int i: activities.keySet()) {
			if (i > base) {
				base = i;
			}
		}
		for (int i: gateways.keySet()) {
			if (i > base) {
				base = i;
			}
		}
		for (int i: events.keySet()) {
			if (i > base) {
				base = i;
			}
		}
		base++;
		return base;
	}
	
	public HashMap<Integer, ProcessModel> getAlternativePaths() {
		return alternativePaths;
	}

	public void addElem(Element elem) {
		if (elem.getClass().toString().endsWith("Gateway")) {
			gateways.put(elem.getId(), (Gateway) elem);
		}
		if (elem.getClass().toString().endsWith("Activity")) {
			activities.put(elem.getId(), (Activity) elem);
		}
		if (elem.getClass().toString().endsWith("Event")) {
			events.put(elem.getId(), (Event) elem);
		}
	}
	
	public Element getElem(int id) {
		if (events.containsKey(id)) {
			return events.get(id);
		}
		if (gateways.containsKey(id)) {
			return gateways.get(id);
		}
		if (activities.containsKey(id)) {
			return activities.get(id);
		}
		return null;
	}
	
	public Arc getArc(int id) {
		return arcs.get(id);
	}
	
	public void removeArc(int id) {
		if (arcs.containsKey(id) == false ) {
			System.out.println("NO ARC: " + id);
		}
		this.arcs.remove(id);
	}
	
	public void removeElem(int id) {
		if (events.containsKey(id)) {
			events.remove(id);
		}
		if (gateways.containsKey(id)) {
			gateways.remove(id);
		}
		if (activities.containsKey(id)) {
			activities.remove(id);
		}
	}
	
	public void normalize() {
		
		// Clean arcs
		ArrayList<Integer> toBeDeleted = new ArrayList<Integer>();
		for (int key: arcs.keySet()) {
			if (arcs.get(key).getTarget() == null) {
				toBeDeleted.add(key);
			}
		}
		for (int key: toBeDeleted) {
			arcs.remove(key);
		}
		
		
		for (int activityKey: activities.keySet()) {
			int count = 0;
			
			// Count arcs (incoming)
			for (Arc arc: arcs.values()) {
				if (arc.getTarget().getId() == activityKey) {
					count++;
				}
			}
			if (count > 1) {
				Activity a = activities.get(activityKey);
				int gwId = getNewId();
				Gateway xorGateway =  new Gateway(gwId , "", a.getLane(), a.getPool(), GatewayType.XOR);
				gateways.put(gwId, xorGateway);
				// Modify target of incoming arcs to new gateway
				for (Arc arc: arcs.values()) {
					if (arc.getTarget().getId() == activityKey) {
						arc.setTarget(xorGateway);
					}
				}
				
				// Create new arc from gateway to activity
				int arcId = getNewId();
				Arc arc = new Arc(arcId, "", xorGateway, a);
				arcs.put(arcId, arc);
				System.out.println("Gateway for incoming arcs inserted (" + activityKey + ") :" + gwId);
			}
			
			
			count = 0;
			// Count arcs (outgoing)
			for (Arc arc: arcs.values()) {
				if (arc.getSource().getId() == activityKey) {
					count++;
				}
			}
			if (count > 1) {
				Activity a = activities.get(activityKey);
				int gwId = getNewId();
				boolean isAND = true;
				for (Arc arc: arcs.values()) {
					if (arc.getType().equals("VirtualFlow")) {
						isAND = false;
					}
				}
				
				Gateway gateway = null;
				if (isAND == true) {
					gateway =  new Gateway(gwId , "", a.getLane(), a.getPool(), GatewayType.AND);
				} else {
					gateway =  new Gateway(gwId , "", a.getLane(), a.getPool(), GatewayType.XOR);
				}
				gateways.put(gwId, gateway);
				System.out.println("Gateway for outgoing arcs inserted: " + gwId);
				
				// Modify target of incoming arcs to new gateway
				for (Arc arc: arcs.values()) {
					if (arc.getSource().getId() == activityKey) {
						arc.setSource(gateway);
					}
				}
				
				// Create new arc from gateway to activity
				int arcId = getNewId();
				Arc arc = new Arc(arcId, "", a, gateway);
				arcs.put(arcId, arc);
			}
			
			
		}
	}
	
	public void normalizeEndEvents() {
		int count = 0;
		ArrayList<Integer> endEvents = new ArrayList<Integer>();
		
		for (Event e: events.values()) {
			if (EventType.isEndEvent(e.getType())) {
				count++;
				endEvents.add(e.getId());
			}
		}
		if (count > 1) {
			System.out.println("Multiple End Events detected");
			int endEventId = getNewId();
			
			Event endEvent = new Event(endEventId, "", events.get(endEvents.get(0)).getLane(), events.get(endEvents.get(0)).getPool(), EventType.START_EVENT);
			events.put(endEventId, endEvent);
			
			// For each end event, create an arc to the new end event
			for (int mEndEventId: endEvents) {
				int arcId = getNewId();
				Arc arc = new Arc(arcId, "", events.get(mEndEventId), endEvent);
				arcs.put(arcId, arc);
			}
		}
	}
	
	public void annotateModel(int option, EnglishLabelDeriver lDeriver, EnglishLabelHelper lHelper) {
		
//		System.out.println(activities.size() + "\t" + events.size() + "\t" + gateways.size());
		
		EnglishLabelCategorizer lC = new EnglishLabelCategorizer(lHelper.getDictionary(), lHelper, lDeriver);
		ArrayList<contentDetermination.labelAnalysis.structure.Activity> modela = new ArrayList<contentDetermination.labelAnalysis.structure.Activity>();
		
		for (Activity a: activities.values()) {
			EnglishLabelProperties props = new EnglishLabelProperties();
				try {
					String label = a.getLabel().toLowerCase().replaceAll("\n", " ");
					label = label.replaceAll("  ", " ");
					
					if (label.contains("glossary://")) {
						label = label.replace("glossary://", "");
						label = label.substring(label.indexOf("/")+1,label.length());
						label = label.replace(";;", "");
					}
					
					String[] labelSplit = label.split(" ");
					
					contentDetermination.labelAnalysis.structure.Activity act = new contentDetermination.labelAnalysis.structure.Activity(label, label, "",modela);
//					if (lC.getLabelStyle(act).equals("VO")) {
						lDeriver.deriveFromVOS(a.getLabel(), labelSplit, props);
//					} else {
//						lDeriver.deriveFromActionNounLabels(props, label, labelSplit);
//					}
				
					Annotation anno = new Annotation();
					
					// No Conjunction label
					if (props.hasConjunction() == false) {
						
						// If no verb-object label
						if (lHelper.isVerb(labelSplit[0]) == false) {
							anno.addAction("conduct");
							anno.addBusinessObjects(a.getLabel().toLowerCase());
							a.addAnnotation(anno);
						
						// If verb-object label
						} else {
							anno.addAction(props.getAction());
							String bo = props.getBusinessObject();
							if (bo.startsWith("the ")) {
								bo = bo.replace("the ", "");
							}
							if (bo.startsWith("an ")) {
								bo = bo.replace("an ", "");
							}
							anno.addBusinessObjects((bo));
							String add = props.getAdditionalInfo();
							String[] splitAdd = add.split(" "); 
							if (splitAdd.length > 2 && splitAdd[1].equals("the")) {
								add = add.replace("the ", "");
							}
							anno.setAddition(add);
							a.addAnnotation(anno);
						}
					// Conjunction label	
					} else {
						for (String action: props.getMultipleActions()) {
							anno.addAction(action);
						}
						for (String bo: props.getMultipleBOs()) {
							String temp = bo;
							if (temp.startsWith("the ")) {
								temp = temp.replace("the ", "");
							}
							if (temp.startsWith("an ")) {
								temp = temp.replace("an ", "");
							}
							anno.addBusinessObjects(temp);
						}
						anno.setAddition("");
						a.addAnnotation(anno);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public ProcessModel(int id, String name) {
		this.id = id;
		this.name = name;
		arcs = new HashMap<Integer, Arc>();
		activities = new HashMap<Integer, Activity>();
		events = new HashMap<Integer, Event>();
		gateways = new HashMap<Integer, Gateway>();
		lanes = new ArrayList<String>();
		pools = new ArrayList<String>();
		alternativePaths = new HashMap<Integer, ProcessModel>();
	}
	
	public void addAlternativePath(ProcessModel path, int id) {
		alternativePaths.put(id, path);
	}
	
	public int getElemAmount() {
		return activities.size() + gateways.size() + events.size();
	}
	
	
	public ArrayList<String> getPools() {
		return pools;
	}

	public void addPool(String pool) {
		this.pools.add(pool);
	}

	public void addArc(Arc arc) {
		arcs.put(arc.getId(), arc);
	}
	
	public void addActivity(Activity activity) {
		activities.put(activity.getId(), activity);
	}
	
	public void addEvent(Event event) {
		events.put(event.getId(), event);
	}
	
	public void addGateway(Gateway gateway) {
		gateways.put(gateway.getId(), gateway);
	}

	public HashMap<Integer, Arc> getArcs() {
		return arcs;
	}

	public HashMap<Integer, Activity> getActivites() {
		return activities;
	}

	public HashMap<Integer, Event> getEvents() {
		return events;
	}

	public HashMap<Integer, Gateway> getGateways() {
		return gateways;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Activity getActivity(int id) {
		return activities.get(id);
	}
	
	public void addLane(String lane) {
		String temp = lane;
		if (temp.contains("glossary://")) {
			temp = lane.replace("glossary://", "");
			temp = temp.substring(temp.indexOf("/")+1,temp.length());
			temp = temp.replace(";;", "");
		}
		lanes.add(temp);
	}
	
	public ArrayList<String> getLanes() {
		return lanes;
	}
	
	public void print() {
		System.out.println("Process Model: "  + this.name + " (" + this.getId() + ")");
		for (Activity a: activities.values()) {
			System.out.println("Activity (" + a.getId() + ") " + a.getLabel());
		}
		for (Event e: events.values()) {
			System.out.println("Event (" + e.getId() + ") " + e.getLabel() + " - Type: "  + e.getType());
		}
		for (Gateway g: gateways.values()) {
			System.out.println("Gatewyay (" + g.getId() + ")" + " " + g.getType());
		}
		for (Arc arc: arcs.values()) {
			System.out.println("Arc: (s: " + arc.getSource().getId() + " t: " + arc.getTarget().getId() + ")" + "- " + arc.getId());
		}
	}

}
