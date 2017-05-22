package bpmn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nodes.Cluster;
import nodes.ProcessNode;

public abstract class LaneableCluster extends Cluster{

	 public List<Lane> f_lanes = new ArrayList<Lane>();
	    public static final String PROP_VERTICAL = "vertical_Pool";

	    
	    protected void init() {
	    	setProperty(PROP_VERTICAL, "FALSE");
	    	//setPropertyEditor(PROP_VERTICAL, new BooleanPropertyEditor());
	    }
	    
	    public void addLane(Lane lane) {
	        if (!f_lanes.contains(lane)) {
	            f_lanes.add(lane);
	            addProcessNode(lane);
	        }
	    }
	    
	    public boolean isVertical() {
	    	return "TRUE".equals(getProperty(PROP_VERTICAL));
	    }

	    public void removeLane(Lane lane) {
	        f_lanes.remove(lane);
	        removeProcessNode(lane);
	    }

	    public List<Lane> getLanes() {
	        return f_lanes;
	    }

	    public Set<Lane> getLanesRecursively() {
	        Set<Lane> lanes = new HashSet<Lane>( this.f_lanes );

	        for ( Lane lane : this.f_lanes )
	            lanes.addAll( lane.getLanesRecursively() );

	        return lanes;
	    }
	    
	    @Override
	    public void setProperty(String key, String value) {
	        super.setProperty(key, value);
	        // Check if key=PROP_CONTAINED_NODES
	        if (key.equals(PROP_CONTAINED_NODES)) {
	            // reestablishing connection with lanes
	            ArrayList<ProcessNode> _nodes = new ArrayList<ProcessNode>(getProcessNodes());
	            if (_nodes != null) {
	                if (f_lanes != null) {
	                    f_lanes.clear();
	                }
	                for (ProcessNode pn : _nodes) {
	                    if (pn instanceof Lane) {
	                        if (!f_lanes.contains(pn)) {
	                            ((Lane) pn).setParent(this);
	                            addLane((Lane) pn);
	                            //Collections.sort(f_lanes, new LaneSorter());
	                        }
	                    }else if(pn instanceof Pool) {//this should not happen...
	                    	this.removeProcessNode(pn);
	                    }
	                }
	            }
	        }
	    }
	
}
