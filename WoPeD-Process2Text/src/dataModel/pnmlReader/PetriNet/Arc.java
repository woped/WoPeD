package dataModel.pnmlReader.PetriNet;

public class Arc {
	
	public Arc (String id, String source, String target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	private String id;
	private String source;
	private String target;

}
