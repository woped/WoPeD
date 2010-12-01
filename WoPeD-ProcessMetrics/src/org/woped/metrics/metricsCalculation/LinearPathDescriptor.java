package org.woped.metrics.metricsCalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.woped.core.model.ArcModel;

public class LinearPathDescriptor {

	private HashMap<String,ArrayList<String>> pathMap = new HashMap<String, ArrayList<String>>();
	
	public LinearPathDescriptor(Map<String, Map<String,Object>> idMap, Map<String,ArcModel> arcMap){
		for(String key:idMap.keySet()){
			ArrayList<String> children = new ArrayList<String>();
			for(String subkey:idMap.get(key).keySet()){
				ArcModel arc = arcMap.get(subkey);
				if(arc == null) continue;
				children.add(arc.getTargetId());
			}
			pathMap.put(key, children);
		}
	}
	
	public int getCyclicNodes(){
		int cyclic = 0;
		for(String key:pathMap.keySet())
				if(findOrDie(key,key, new HashSet<String>()))
						cyclic++;
		return cyclic;
	}
	
	private boolean findOrDie(String key, String findWhat, HashSet<String> visited){
		if(visited.contains(key))
			return false;
		visited.add(key);
		for(String s:pathMap.get(key))
			if(s == findWhat) return true;
			else if(findOrDie(s,findWhat,visited)) return true;
		return false;	
	}
	
	
}
