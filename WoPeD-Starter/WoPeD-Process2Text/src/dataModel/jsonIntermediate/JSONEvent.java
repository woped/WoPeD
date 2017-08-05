package dataModel.jsonIntermediate;

import java.util.ArrayList;

public class JSONEvent extends JSONElem{
	

	public JSONEvent(int id, String label, String type, int laneId, int poolId, ArrayList<Integer> arcs) {
		super(id, label, arcs, laneId, poolId, type);
	}
	
	public String toString() {
		String a = "";
		for (int i: arcs) {
			a = a + " " + i;
		}
		return "Event (" + id + ") - " + "Lane: " + laneId + " " + label + " - " + type;
	}
}
