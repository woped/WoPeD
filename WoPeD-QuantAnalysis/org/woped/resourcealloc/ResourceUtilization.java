package org.woped.resourcealloc;

import java.util.ArrayList;

public class ResourceUtilization {
	
	private ArrayList<Resource> freeResources = new ArrayList<Resource>();
	private ArrayList<Resource> usedResources = new ArrayList<Resource>();
	private ResourceAllocation resAlloc;
	
	public ResourceUtilization(ResourceAllocation resAlloc){
		this.resAlloc = resAlloc;
		freeResources = this.resAlloc.getResources();
		for (Resource r: freeResources)
			r.setBusyTime(0.0);
	}
	
	public ArrayList<Resource> getFreeResources() {
		return freeResources;
	}
	
	public void setFreeResources(ArrayList<Resource> freeResources) {
		this.freeResources = freeResources;
	}
	
	public ArrayList<Resource> getUsedResources() {
		return usedResources;
	}
	
	public void setUsedResources(ArrayList<Resource> usedResources) {
		this.usedResources = usedResources;
	}
	
	public ArrayList<Resource> getFreeResPerRole(String role){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		for (Resource r : freeResources)
			if (r.getRoles().contains(role))
				list.add(r);
		
		return list;
	}
	
	public ArrayList<Resource> getFreeResPerGroup(String group){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		for (Resource r : freeResources)
			if (r.getGroups().contains(group))
				list.add(r);
		
		return list;
	}
	
	public ArrayList<Resource> getFreeResPerGroupRole(String group, String role){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		for (Resource r : freeResources)
			if (r.getGroups().contains(group) && r.getRoles().contains(role))
				list.add(r);
		
		return list;
	}
	
	public Resource chooseResourceFromFreeRoles(String role){
		Resource r = null;
		
		/** 
		 * @ToDo: I
		 */
		
		return r;
	}
	
	public Resource chooseResourceFromFreeGroups(String group){
		Resource r = null;
		
		/** 
		 * @ToDo: I
		 */
		
		return r;
	}
	
	public Resource chooseResourceFromFreeResources(String group, String role){
		Resource r = null;
		
		/** 
		 * @ToDo: I
		 */
		
		return r;
	}
	
	public void freeResource(Resource r){
		freeResources.add(r);
		usedResources.remove(r);
	}
	
	public void useResource(Resource r){
		freeResources.remove(r);
		usedResources.add(r);
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
}
