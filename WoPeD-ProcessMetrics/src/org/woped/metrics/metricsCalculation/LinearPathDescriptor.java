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
	
	public double longestPath(){
		HashSet<String> nodes = new HashSet<String>();
		for(String key:pathMap.keySet())
			nodes.add(key);
		
		for(String key:pathMap.keySet())
			for(String childKey:pathMap.get(key))
				nodes.remove(childKey);
		
		if(nodes.size()!=1)
			return -1;
		
		String startNode = null;;
		for(String key:nodes)
			startNode=key;
		return longestPath(1, startNode, new HashSet<String>());
	}
	
	private double longestPath(double length, String myKey, HashSet<String> previousKeys){
		if(pathMap.get(myKey).size()==0) return length;
		if(previousKeys.contains(myKey)) return -1;
		HashSet<String> newPrevKeys = new HashSet<String>();
		for(String key:previousKeys)
			newPrevKeys.add(key);
		newPrevKeys.add(myKey);
		double maxlength = -2;
		for(String child:pathMap.get(myKey))
			maxlength = Math.max(maxlength,longestPath(length+1,child,newPrevKeys));
		return maxlength;
	}
	
	
}
