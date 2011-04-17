package org.woped.metrics.metricsCalculation;

import java.util.HashSet;
import java.util.Set;

public class MetricHighlighting {

	private Set<String> nodeids = new HashSet<String>();
	private Set<String> arcids = new HashSet<String>();
	
	public Set<String> getNodeIDs(){
		return nodeids;
	}
	
	public Set<String> getArcIDs(){
		return arcids;
	}
	
	public void addNodeID(String nodeid){
		nodeids.add(nodeid);
	}
	
	public void addArcID(String arcid){
		arcids.add(arcid);
	}
	
	public void addArcIDs(Set<String> arcids){
		if(arcids != null)
			for(String arcid:arcids)
				addArcID(arcid);
	}
	
	public void addNodeIDs(Set<String> nodeids){
		if(nodeids != null)
			for(String nodeid:nodeids)
				addNodeID(nodeid);
	}
	
	public void removeNodeIDs(Set<String> nodeids){
		if(nodeids != null)
			for(String nodeid:nodeids)
				removeNodeID(nodeid);
	}
	
	public void removeArcIDs(Set<String> nodeids){
		if(nodeids != null)
			for(String nodeid:nodeids)
				removeNodeID(nodeid);
	}
	
	public void removeNodeID(String nodeid){
		nodeids.remove(nodeid);
	}
	
	public void removeArcID(String arcid){
		arcids.remove(arcid);
	}
	
	
}
