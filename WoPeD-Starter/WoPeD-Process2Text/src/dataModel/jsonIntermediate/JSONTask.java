package dataModel.jsonIntermediate;

import java.util.ArrayList;

public class JSONTask extends JSONElem { 
	
	public JSONTask(int id, String label, ArrayList<Integer> arcs, int laneId, int poolId, String type) {
		super(id, label, arcs, laneId, poolId, type);
	}

	public String toString() {
		String a = "";
		for (int i: arcs) {
			a = a + " " + i;
		}
		a = a.trim();
		return "Task (" + id + ") - " + "Lane: " + laneId + " " + label + " - " + a + " - type: " + type;
	}
}
