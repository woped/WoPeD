package org.woped.quantana.resourcealloc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.quantana.utilities.ColorFactory;

public class ResourceAllocation {
	private ArrayList<String> roles = new ArrayList<String>();
	private ArrayList<String> groups = new ArrayList<String>();
	private HashMap<String, Resource> resources = new HashMap<String, Resource>();
	private AllocationTable taskAlloc2;
	private PetriNetModelProcessor proc;
	private ResourceClassTaskAllocationTable resClsTskAlloc;
	private ColorFactory colorFactory = new ColorFactory();
	
	public ResourceAllocation(ArrayList<String> roles, ArrayList<String> groups, Iterator<TransitionModel> tasks, PetriNetModelProcessor pmp){
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
			
			taskAlloc2.getTable().put(tsk, new AllocationTableItem(tsk, rNames, gNames));
			
			Vector<ResourceModel> res = proc.getResources();
			
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
				resources.put(name, resObj);
				
				resObj.setColor(colorFactory.nextColor());
				
				Vector<String> classes = proc.getResourceClassesResourceIsAssignedTo(name);
				for (int j = 0; j < classes.size(); j++){
					String cls = (String)classes.get(j);
					if (resClsType.get(cls).equals("role"))
						resObj.getRoles().add(cls);
					else
						resObj.getGroups().add(cls);
				}
			}
			
			resClsTskAlloc = new ResourceClassTaskAllocationTable();
			Set<String> classes = resClsType.keySet();
			Iterator<String> iter = classes.iterator();
			while (iter.hasNext()){
				ResourceClassTaskAllocation rcta = new ResourceClassTaskAllocation((String)iter.next());
				for (AllocationTableItem t : taskAlloc2.getTable().values()){
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
	
	public HashMap<String, Resource> getResources() {
		return resources;
	}
	
	public void setResources(HashMap<String, Resource> resources) {
		this.resources = resources;
	}
	
	public ArrayList<String> getRoles() {
		return roles;
	}
	
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	
	public void addResource(Resource res){
		resources.put(res.getName(), res);
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
			for (Resource r : resources.values()){
				if (r.getRoles().contains(role))
					list.add(r);
			}
		}
		
		return list;
	}
	
	public ArrayList<Resource> getListPerGroup(String group){
		ArrayList<Resource> list = new ArrayList<Resource>();
		
		if (roles.contains(group)){
			for (Resource r : resources.values()){
				if (r.getGroups().contains(group))
					list.add(r);
			}
		}
		
		return list;
	}

	public ArrayList<String> getTasksForRole(String role){
		ArrayList<String> list = new ArrayList<String>();
		HashMap<String, AllocationTableItem> table = taskAlloc2.getTable();
		
		for (int i = 0; i < table.size(); i++){
			if (table.get(i).getRoles().contains(role))
				list.add(table.get(i).getTask());
		}
		
		return list;
	}
	
	public ArrayList<String> getTasksForGroup(String group){
		ArrayList<String> list = new ArrayList<String>();
		HashMap<String, AllocationTableItem> table = taskAlloc2.getTable();
		
		for (int i = 0; i < table.size(); i++){
			if (table.get(i).getGroups().contains(group))
				list.add(table.get(i).getTask());
		}
		
		return list;
	}
	
	public ArrayList<String> getTasksForGroupRole(String group, String role){
		ArrayList<String> list = new ArrayList<String>();
		HashMap<String, AllocationTableItem> table = taskAlloc2.getTable();

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
		for (AllocationTableItem t : taskAlloc2.getTable().values()){
			text += "\n" + t;
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
	
	public String getRole(String id){
		ArrayList<String> roles = taskAlloc2.getTable().get(id).getRoles();
		
		if (roles.size() <= 0) return "";
		else return roles.get(0);
	}
	
	public String getGroup(String id){
		ArrayList<String> groups = taskAlloc2.getTable().get(id).getGroups();
		
		if (groups.size() <= 0) return "";
		else return groups.get(0);
	}
	
	public ArrayList<Resource> getResourcesPerTask(String id){
		ArrayList<Resource> list = new ArrayList<Resource>();
		ArrayList<String> roles = taskAlloc2.getTable().get(id).getRoles();
		ArrayList<String> groups = taskAlloc2.getTable().get(id).getGroups();
		
		if (roles != null && groups != null){
			for (String r : roles){
				for (String g : groups){
					for (Resource res : resources.values()){
						if (res.getRoles().contains(r) && res.getGroups().contains(g))
							list.add(res);
					}
				}
			}
		}
		
		return list;
	}
	
	public String printResourcesPerTasks(String[] tasks){
		String text = "";
		for (String s : tasks){
			ArrayList<Resource> list = getResourcesPerTask(s);
			text += "\n" + s + ": ";
			for (Resource r : list){
				text += r.getName() + ",";
			}
		}
		
		return text;
	}
}
