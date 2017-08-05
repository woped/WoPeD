package dataModel.jsonIntermediate;

import java.util.ArrayList;

public class JSONElem {
	
	protected int id;
	protected String label;
	protected String type;
	protected int laneId;
	protected int poolId;
	protected ArrayList<Integer> arcs;
	
	public JSONElem(int id, String label, ArrayList<Integer> arcs, int laneId, int poolId, String type) {
		this.id = id;
		this.label = label;
		this.arcs = arcs;
		this.laneId = laneId;
		this.type = type;
		this.poolId = poolId;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public int getLaneId() {
		return laneId;
	}

	public int getPoolId() {
		return poolId;
	}

	public ArrayList<Integer> getArcs() {
		return arcs;
	}
	
}
