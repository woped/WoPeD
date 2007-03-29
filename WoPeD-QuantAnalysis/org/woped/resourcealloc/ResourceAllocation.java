package org.woped.resourcealloc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;

public class ResourceAllocation {
	private ArrayList<String> roles = new ArrayList<String>();
	private ArrayList<String> groups = new ArrayList<String>();
	private ArrayList<Resource> resources = new ArrayList<Resource>();
	private AllocationTable taskAlloc2;
	private PetriNetModelProcessor proc;
	private ResourceClassTaskAllocationTable resClsTskAlloc;
	
	public ResourceAllocation(ArrayList<String> roles, ArrayList<String> groups, Iterator tasks, PetriNetModelProcessor pmp){
		this.roles = (ArrayList<String>)roles;
		this.groups = (ArrayList<String>)groups;
		this.proc = pmp;
		
		taskAlloc2 = new AllocationTable();
		
		while (tasks.hasNext()){
			TransitionModel transMod = (TransitionModel)tasks.next();
			TransitionResourceModel resMod = transMod.getToolSpecific().getTransResource();
			
			ArrayList<String> rNames = new ArrayList<String>();
			ArrayList<String> gNames = new ArrayList<String>();
			if (resMod != null){
				rNames.add(resMod.getTransRoleName());
				gNames.add(resMod.getTransOrgUnitName());
			}
			
			String tsk = transMod.getNameValue() + " (" + transMod.getId() + ")";
			
			taskAlloc2.getTable().add(new AllocationTableItem(tsk, rNames, gNames));
			
			Vector res = proc.getResources();
			
			HashMap<String, String> resClsType = new HashMap<String, String>();
			for (int i = 0; i < roles.size(); i++){
				resClsType.put(roles.get(i), "role");
			}
			for (int i = 0; i < groups.size(); i++){
				resClsType.put(groups.get(i), "group");
			}
			
			for (int i = 0; i < res.size(); i++){
				String name = ((ResourceModel)res.get(i)).getName();
				Resource resObj = new Resource(name);
				resources.add(resObj);
				
				Vector classes = proc.getResourceClassesResourceIsAssignedTo(name);
				for (int j = 0; j < classes.size(); j++){
					String cls = (String)classes.get(j);
					if (resClsType.get(cls).equals("role"))
						resObj.getRoles().add(cls);
					else
						resObj.getGroups().add(cls);
				}
			}
			
//			HashMap rm = proc.getResourceMapping();
			resClsTskAlloc = new ResourceClassTaskAllocationTable();
			Set<String> classes = resClsType.keySet();
			Iterator iter = classes.iterator();
			while (iter.hasNext()){
				ResourceClassTaskAllocation rcta = new ResourceClassTaskAllocation((String)iter.next());
				for (int i = 0; i < taskAlloc2.getTable().size(); i++){
					AllocationTableItem t = taskAlloc2.getTable().get(i);
					if (t.getRoles().contains(rcta.getResClass()) || t.getGroups().contains(rcta.getResClass()))
						rcta.getTasks().add(t.getTask());
				}
				resClsTskAlloc.getTable().add(rcta);
			}
		}
	}
	
	public ArrayList<String> getGroups() {
		return groups;
	}
	
	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}
	
	public ArrayList<Resource> getResources() {
		return resources;
	}
	
	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}
	
	public ArrayList<String> getRoles() {
		return roles;
	}
	
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	
	public void addResource(Resource res){
		resources.add(res);
	}
	
	public int getNumPerRole(String role){
		return getListPerRole(role).size();
	}
	
	public int getNumPerGroup(String group){
		return getListPerRole(group).size();
	}
	
	public ArrayList<Resource> getListPerRole(String role){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		if (roles.contains(role)){
			for (Resource r : resources){
				if (r.getRoles().contains(role))
					list.add(r);
			}
		}
		
		return list;
	}
	
	public ArrayList<Resource> getListPerGroup(String group){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		if (roles.contains(group)){
			for (Resource r : resources){
				if (r.getGroups().contains(group))
					list.add(r);
			}
		}
		
		return list;
	}

	public ArrayList<String> getTasksForRole(String role){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<AllocationTableItem> table = taskAlloc2.getTable();
		
		for (int i = 0; i < table.size(); i++){
			if (table.get(i).getRoles().contains(role))
				list.add(table.get(i).getTask());
		}
		
		return list;
	}
	
	public ArrayList<String> getTasksForGroup(String group){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<AllocationTableItem> table = taskAlloc2.getTable();
		
		for (int i = 0; i < table.size(); i++){
			if (table.get(i).getGroups().contains(group))
				list.add(table.get(i).getTask());
		}
		
		return list;
	}
	
	public ArrayList<String> getTasksForGroupRole(String group, String role){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<AllocationTableItem> table = taskAlloc2.getTable();

		for (int i = 0; i < table.size(); i++){
			if (table.get(i).getGroups().contains(group) && table.get(i).getRoles().contains(role))
				list.add(table.get(i).getTask());
		}

		return list;
	}

	public AllocationTable getTaskAlloc2() {
		return taskAlloc2;
	}

	public void setTaskAlloc2(AllocationTable taskAlloc2) {
		this.taskAlloc2 = taskAlloc2;
	}
	
	public String toString(){
		String text = "";
		for (int i = 0; i < taskAlloc2.getTable().size(); i++){
			text += "\n" + taskAlloc2.getTable().get(i);
		}
		
		return text;
	}

	public ResourceClassTaskAllocationTable getResClsTskAlloc() {
		return resClsTskAlloc;
	}

	public void setResClsTskAlloc(ResourceClassTaskAllocationTable resClsTskAlloc) {
		this.resClsTskAlloc = resClsTskAlloc;
	}
	
	public int getNumOfResClasses(){
		return resClsTskAlloc.getTable().size();
	}
	
	
}
