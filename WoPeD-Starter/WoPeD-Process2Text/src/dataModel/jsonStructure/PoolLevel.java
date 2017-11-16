package dataModel.jsonStructure;

import java.util.ArrayList;

public class PoolLevel {
	
	String resourceId;
	PoolProperties properties;
	Stencil stencil;
	ArrayList<LaneLevel>childShapes;
	Target target;
	ArrayList<Outgoing> outgoing;
	
	
	
	public ArrayList<Outgoing> getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(ArrayList<Outgoing> outgoing) {
		this.outgoing = outgoing;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public PoolProperties getProps() {
		return properties;
	}
	public void setProps(PoolProperties props) {
		this.properties = props;
	}
	public Stencil getStencil() {
		return stencil;
	}
	public void setStencil(Stencil stencil) {
		this.stencil = stencil;
	}
	public ArrayList<LaneLevel> getChildShapes() {
		return childShapes;
	}
	public void setChildShapes(ArrayList<LaneLevel> childShapes) {
		this.childShapes = childShapes;
	}
	
	
	
}
