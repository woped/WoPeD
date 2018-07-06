package dataModel.pnmlReader.PetriNet;

public abstract class Element {
	
	private String label;
	private String id;
	private String role;
	
	public Element (String id, String label, String role) {
		this.id = id;
		this.label = label;
		this.role = role;
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
	public void setRole(String role) {
		this.role = role;
	}
	public String getRole() {
		return role;
	}
}
