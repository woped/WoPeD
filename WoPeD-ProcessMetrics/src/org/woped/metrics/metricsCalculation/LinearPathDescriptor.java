package org.woped.metrics.metricsCalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.woped.core.model.ArcModel;
import org.woped.metrics.exceptions.NaNException;

public class LinearPathDescriptor {

	private HashMap<String,ArrayList<String>> pathMap = new HashMap<String, ArrayList<String>>();
	private Set<String> nodeHighlight;
	private List<ArcModel> arcs = new ArrayList<ArcModel>();
	
	public LinearPathDescriptor(Map<String, Map<String,Object>> idMap, Map<String,ArcModel> arcMap){
		for(String key:idMap.keySet()){
			ArrayList<String> children = new ArrayList<String>();
			for(String subkey:idMap.get(key).keySet()){
				ArcModel arc = arcMap.get(subkey);
				if(arc == null) continue;
				arcs.add(arc);
				children.add(arc.getTargetId());
			}
			pathMap.put(key, children);
		}
	}
	
	public Set<String> getHighlightedNodes(){
		return nodeHighlight;
	}
	
	public Set<String> calculateHighlightedArcs(){
		Set<String> retarcs = new HashSet<String>();
		for(ArcModel arc:arcs)
			if(nodeHighlight.contains(arc.getSourceId()) && nodeHighlight.contains(arc.getTargetId()))
				retarcs.add(arc.getId());
		return retarcs;
	}
	
	public int getCyclicNodes(){
		nodeHighlight = new HashSet<String>();
		
		int cyclic = 0;
		for(String key:pathMap.keySet())
				if(findOrDie(key,key, new HashSet<String>())){
					cyclic++;
					
					// Highlighting
					nodeHighlight.add(key);
				}
						
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
	
	public double longestPath() throws NaNException{
		nodeHighlight = new HashSet<String>();
		HashSet<String> nodes = new HashSet<String>();
		for(String key:pathMap.keySet())
			nodes.add(key);
		
		for(String key:pathMap.keySet())
			for(String childKey:pathMap.get(key))
				nodes.remove(childKey);
		
		if(nodes.size()!=1)
			throw new NaNException();
		
		String startNode = null;;
		for(String key:nodes)
			startNode=key;
		
		return longestPath(1, startNode, new HashSet<String>(), nodeHighlight);
	}
	
	private double longestPath(double length, String myKey, HashSet<String> previousKeys, Set<String> maxset){
		if(pathMap.get(myKey).size()==0) {
			if(length > maxset.size()){
				maxset.clear();
				for(String key:previousKeys)
					maxset.add(key);
				maxset.add(myKey);
			}
			return length;
		}
		if(previousKeys.contains(myKey)) return -1;
		HashSet<String> newPrevKeys = new HashSet<String>();
		for(String key:previousKeys)
			newPrevKeys.add(key);
		newPrevKeys.add(myKey);
		double maxlength = -2;
		for(String child:pathMap.get(myKey))
			maxlength = Math.max(maxlength,longestPath(length+1,child,newPrevKeys, maxset));
		return maxlength;
	}
	
	public double cutVertices() throws NaNException{
		if(!isFinite()) throw new NaNException();
		
		nodeHighlight = new HashSet<String>();
		
		double vertices = 0;
		HashSet<String> nodes = new HashSet<String>();
		for(String key:pathMap.keySet())
			nodes.add(key);
		
		for(String key:pathMap.keySet())
			for(String childKey:pathMap.get(key))
				nodes.remove(childKey);
		
		if(nodes.size()<1)
			throw new NaNException();
		
		Set<String> keySet = pathMap.keySet();
		
		String startNode = null;;
		for(String key:nodes)
			startNode=key;
		for(String key:keySet){
			@SuppressWarnings("unchecked")
			HashMap<String,ArrayList<String>> newMap = (HashMap<String,ArrayList<String>>) pathMap.clone();
			newMap.put(key, new ArrayList<String>());
			if(!isConnected(startNode, newMap, new HashSet<String>(), key)){
				vertices++;
				
				//Highlight
				nodeHighlight.add(key);
			}
				
		}
		return vertices;
	}
	
	private boolean isFinite(){
		boolean hasStart = false;
		boolean hasEnd = false;
		HashSet<String> ids = new HashSet<String>();
		for(String key:pathMap.keySet())
			ids.add(key);
		for(String key:pathMap.keySet()){
			if(pathMap.get(key).size() == 0) hasEnd = true;
			for(String subKey:pathMap.get(key))
				ids.remove(subKey);
		}
		if (ids.size()>0) hasStart = true;
		return hasEnd && hasStart;
	}
	
	private boolean isConnected(String myKey, HashMap<String, ArrayList<String>> newMap, HashSet<String> previousKeys, String killedkey){
		if(myKey.equals(killedkey) && previousKeys.size() == 0) return true;
		if(newMap.get(myKey).size()==0 && (!myKey.equals(killedkey) || pathMap.get(myKey).size() == 0)) return true;
		if(previousKeys.contains(myKey)) return false;
		HashSet<String> newPrevKeys = new HashSet<String>();
		for(String key:previousKeys)
			newPrevKeys.add(key);
		newPrevKeys.add(myKey);
		boolean connected = false;
		for(String child:newMap.get(myKey))
			connected = connected || isConnected(child, newMap, newPrevKeys, killedkey);
		return connected;
	}
	
	
}
