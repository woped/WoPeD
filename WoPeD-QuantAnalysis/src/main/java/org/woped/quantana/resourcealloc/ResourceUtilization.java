package org.woped.quantana.resourcealloc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ResourceUtilization {
	
	private HashMap<String, Resource> freeResources = new HashMap<String, Resource>();
	private HashMap<String, Resource> usedResources = new HashMap<String, Resource>();
	private HashMap<String, Resource> reservedResources = new HashMap<String, Resource>();
	
	private ResourceAllocation resAlloc;
	private Random choice;
	
	public ResourceUtilization(ResourceAllocation resAlloc){
		this.resAlloc = resAlloc;
		HashMap<String, Resource> resources = this.resAlloc.getResources();
		
		for (Resource r : resources.values()) freeResources.put(r.getName(), r);
		
		for (Resource r: freeResources.values())
			r.setBusyTime(0.0);
		
		choice = new Random(new Date().getTime());
	}
	
	public HashMap<String, Resource> getFreeResources() {
		return freeResources;
	}
	
	public void setFreeResources(HashMap<String, Resource> freeResources) {
		this.freeResources = freeResources;
	}
	
	public HashMap<String, Resource> getUsedResources() {
		return usedResources;
	}
	
	public void setUsedResources(HashMap<String, Resource> usedResources) {
		this.usedResources = usedResources;
	}
	
	public ArrayList<Resource> getFreeResPerRole(String role){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		for (Resource r : freeResources.values())
			if (r.getRoles().contains(role))
				list.add(r);
		
		return list;
	}
	
	public ArrayList<Resource> getFreeResPerGroup(String group){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		for (Resource r : freeResources.values())
			if (r.getGroups().contains(group))
				list.add(r);
		
		return list;
	}
	
	public ArrayList<Resource> getFreeResPerGroupRole(String group, String role){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		for (Resource r : freeResources.values())
			if (r.getGroups().contains(group) && r.getRoles().contains(role))
				list.add(r);
		
		return list;
	}
	
	public Resource chooseResourceFromFreeRoles(String role){
		Resource r = null;
		ArrayList<Resource> list = getFreeResPerRole(role);
		int resNum = list.size();
		
		if (resNum > 0){
			int rnd = choice.nextInt(resNum);
			r = list.get(rnd);
		}
		
		return r;
	}
	
	public Resource chooseResourceFromFreeGroups(String group){
		Resource r = null;
		ArrayList<Resource> list = getFreeResPerGroup(group);
		int resNum = list.size();
		
		if (resNum > 0){
			int rnd = choice.nextInt(resNum);
			r = list.get(rnd);
		}
		
		return r;
	}
	
	public Resource chooseResourceFromFreeResources(String group, String role){
		Resource r = null;
		ArrayList<Resource> list = getFreeResPerGroupRole(group, role);
		int resNum = list.size();
		
		if (resNum > 0){
			int rnd = choice.nextInt(resNum);
			r = list.get(rnd);
		}

		return r;
	}
	
	public void freeResource(Resource r){
		if (r != null){
			freeResources.put(r.getName(), r);
			usedResources.remove(r.getName());
		}
	}

	public void useResource(Resource r){
		if (r != null){
			reservedResources.remove(r.getName());
			usedResources.put(r.getName(), r);
		}
	}
	
	public void reserveResource(Resource r){
		if (r != null){
			freeResources.remove(r.getName());
			reservedResources.put(r.getName(), r);
		}
	}
	
	public double getUtilization(Resource r, double period){
		return r.getBusyTime() / period;
	}

	public ResourceAllocation getResAlloc() {
		return resAlloc;
	}

	public void setResAlloc(ResourceAllocation resAlloc) {
		this.resAlloc = resAlloc;
	}
	
	public String printFreeResources(){
		String s = "[";
		int l = freeResources.size();
		Object[] fr = freeResources.values().toArray();
		
		if (l > 1){
			for (int i = 0; i < l - 1; i++){
				s += ((Resource)fr[i]).getName() + ",";
			}
		}
		
		if (l > 0) s += ((Resource)fr[l - 1]).getName();
		
		return s + "]";
	}
	
	public String printUsedResources(){
		String s = "[";
		int l = usedResources.size();
		Object[] fr = usedResources.values().toArray();
		
		if (l > 1){
			for (int i = 0; i < l - 1; i++){
				s += ((Resource)fr[i]).getName() + ",";
			}
		}
		
		if (l > 0) s += ((Resource)fr[l - 1]).getName();
		
		return s + "]";
	}

	public HashMap<String, Resource> getReservedResources() {
		return reservedResources;
	}

	public void setReservedResources(HashMap<String, Resource> reservedResources) {
		this.reservedResources = reservedResources;
	}
}
