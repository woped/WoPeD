package dataModel.jsonStructure;

import java.util.ArrayList;

public class LaneLevel {
	
	String resourceId;
	LaneProperties properties;
	Stencil stencil;
	ArrayList<ElementLevel>childShapes;
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public LaneProperties getProps() {
		return properties;
	}
	public void setProps(LaneProperties props) {
		this.properties = props;
	}
	public Stencil getStencil() {
		return stencil;
	}
	public void setStencil(Stencil stencil) {
		this.stencil = stencil;
	}
	public ArrayList<ElementLevel> getChildShapes() {
		return childShapes;
	}
	public void setChildShapes(ArrayList<ElementLevel> childShapes) {
		this.childShapes = childShapes;
	}
	
	

}
