package org.woped.quantana.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.woped.quantana.gui.ActivityPanel;
import org.woped.quantana.resourcealloc.Resource;

public class ServerStatisticsModel {

	private TasksResourcesAllocation tra;
	private ArrayList<ActivityPanel> actList;

	private HashMap<String, HashMap<String, ArrayList<ActivityPanel>>> sraList = new HashMap<String, HashMap<String, ArrayList<ActivityPanel>>>();
	private int rowNum = 0;
	private int numTasks = 0;
	private String[] sortedTasks;
	private ArrayList<ArrayList<ArrayList<ActivityPanel>>> sortedMatrix;

	public ServerStatisticsModel(TasksResourcesAllocation tra,
			ArrayList<ActivityPanel> ap) {
		this.tra = tra;
		this.actList = ap;

		makeSRAList();
	}

	private void makeSRAList() {
		for (String s : tra.getTasks()) {
			ArrayList<Resource> list = tra.getResources(s);
			HashMap<String, ArrayList<ActivityPanel>> hl = new HashMap<String, ArrayList<ActivityPanel>>();

			int l = list.size();
			if (l == 0)
				l = 1;
			rowNum += l;
			numTasks++;

			for (Resource r : list) {
				ArrayList<ActivityPanel> apList = new ArrayList<ActivityPanel>();
				hl.put(r.getName(), apList);
			}

			sraList.put(s, hl);
		}

		for (ActivityPanel ap : actList) {
			String server = ap.getServer();
			String resource = ap.getResource();
			HashMap<String, ArrayList<ActivityPanel>> hl = sraList.get(server);
			if (hl!=null){
				ArrayList<ActivityPanel> apList = hl.get(resource);
				apList.add(ap);
			}
		}
	}

	public int getRowNum() {
		return rowNum;
	}

	public ArrayList<ArrayList<ArrayList<ActivityPanel>>> getSortedMatrix() {
		if (sortedMatrix == null) {
			sortedMatrix = new ArrayList<ArrayList<ArrayList<ActivityPanel>>>();
			if (sortedTasks == null)
				makeSortedTasks();
			for (int i = 0; i < numTasks; i++) {
				String s = sortedTasks[i];
				ArrayList<ArrayList<ActivityPanel>> list = new ArrayList<ArrayList<ActivityPanel>>();
				String[] resources = getSortedResources(s);
				for (int j = 0; j < resources.length; j++) {
					list.add(getActivityPanelsList(s, resources[j]));
				}
				sortedMatrix.add(list);
			}
		}

		return sortedMatrix;
	}

	private void makeSortedTasks() {
		Object[] o = tra.getTasks().toArray();
		sortedTasks = new String[o.length];
		for (int i = 0; i < o.length; i++)
			sortedTasks[i] = (String) o[i];
		Arrays.sort(sortedTasks);
	}

	public String[] getSortedResources(String task) {
		ArrayList<Resource> list = tra.getResources(task);
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i).getName();
		}
		Arrays.sort(array);

		return array;
	}

	public ArrayList<ActivityPanel> getActivityPanelsList(String server,
			String resource) {
		return (sraList.get(server)).get(resource);
	}

	public String getServer(int row) {
		if (sortedMatrix == null)
			getSortedMatrix();

		return sortedMatrix.get(row).get(0).get(0).getServer();
	}

	public String getResource(int row, int col) {
		if (sortedMatrix == null)
			getSortedMatrix();

		return sortedMatrix.get(row).get(col).get(0).getResource();
	}

	public int getNumTasks() {
		return numTasks;
	}

	public String[] getSortedTasks() {
		if (sortedTasks == null)
			makeSortedTasks();
		return sortedTasks;
	}
}
