package dataModel.jsonIntermediate;

public class JSONArc {
	
	private int id;
	private int target;
	private int source;
	private int laneId;
	private String label;
	private String type;
	
	public JSONArc(int id, int target, int laneId, String label, String type) {
		super();
		this.id = id;
		this.target = target;
		this.laneId = laneId;
		this.label = label;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
	public String toString() {
		return "Arc (" + id + ") - Lane: " + laneId + " - s:" + source + " t:" + target + " name: " + label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getLaneId() {
		return laneId;
	}

	public void setLaneId(int laneId) {
		this.laneId = laneId;
	}
	
	

}
