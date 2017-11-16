package dataModel.pnmlReader.PetriNet;

public abstract class Element {
	
	private String label;
	private String id;
	
	public Element (String id, String label) {
		this.id = id;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
