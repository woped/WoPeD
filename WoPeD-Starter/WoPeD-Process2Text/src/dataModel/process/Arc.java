package dataModel.process;

public class Arc {
	
	private int id;
	private String label;
	private Element source;
	private Element target;
	private String type;

	public Arc(int id, String label, Element source, Element target) {
		this.id = id;
		this.label = label;
		this.source = source;
		this.target = target;
		this.type = "";
	}
	
	public Arc(int id, String label, Element source, Element target, String type) {
		this.id = id;
		this.label = label;
		this.source = source;
		this.target = target;
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public Element getSource() {
		return source;
	}

	public Element getTarget() {
		return target;
	}

	public void setSource(Element source) {
		this.source = source;
	}

	public void setTarget(Element target) {
		this.target = target;
	}
	

}
