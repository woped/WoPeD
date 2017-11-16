package dataModel.jsonStructure;

import java.util.ArrayList;

public class Doc {
	
	String resourceId;
	DocProperties properties;
	Stencil stencil;
	ArrayList<PoolLevel>childShapes;

	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public DocProperties getProps() {
		return properties;
	}
	public void setProps(DocProperties props) {
		this.properties = props;
	}
	public Stencil getStencil() {
		return stencil;
	}
	public void setStencil(Stencil stencil) {
		this.stencil = stencil;
	}
	public ArrayList<PoolLevel> getChildShapes() {
		return childShapes;
	}
	public void setChildShapes(ArrayList<PoolLevel> childShapes) {
		this.childShapes = childShapes;
	}
	
	public void print() {
		 System.out.println("Document: " + this.getProps().getName());
		    ArrayList <PoolLevel> pools = this.getChildShapes();
		    for (PoolLevel pool : pools) {
		    	System.out.print("\t"+pool.getResourceId() + " (");
		    	System.out.print(pool.getProps().getName() + ", ");
		    	System.out.println(pool.getStencil() + ")");

		    	ArrayList<LaneLevel> lanes = pool.getChildShapes();
		    	for (LaneLevel lane:lanes) {
		    		System.out.print("\t\t"+lane.getResourceId() + " (");
			    	System.out.print(lane.getProps().getName() + ", ");
			    	System.out.println(lane.getStencil() + ")");
			    	
		    		ArrayList<ElementLevel> elems = lane.getChildShapes();
		    		for (ElementLevel elem: elems) {
		    			System.out.print("\t\t\t"+elem.getResourceId() + " (");
		    	    	System.out.print(elem.getProps().getName() + ", ");
		    	    	System.out.println(elem.getStencil() + ")");
		    		}
		    	}
		    	
		    }
	}
	
	public ArrayList<String> getTasks() {
		ArrayList<String> tasks = new ArrayList<String>();

		// Pool level
		for (PoolLevel pool:this.getChildShapes()) {
			  String elemName = pool.getStencil().toString();
			  if (elemName.contains(" ")) {
				  elemName = elemName.replace(" ", "");
			  }
			  if (elemName.toLowerCase().equals("task")) {
				  tasks.add(pool.getProps().getName());
			  }
			  
			  // Lane level
			  for (LaneLevel lane: pool.getChildShapes()) {
				  if (lane.getStencil().toString().toLowerCase().equals("task")) {
					  tasks.add(lane.getProps().getName());
				  }
				  
				  // Element level
				  for (ElementLevel elem: lane.getChildShapes()) {
					  if (elem.getStencil().toString().toLowerCase().equals("task")) {
						  tasks.add(elem.getProps().getName());
					  }
				  }
			  }
		  }
		return tasks;
	}
	
	public void clean() {
		 for (PoolLevel poolLevelElems : this.getChildShapes()) {
			 
		 }
		
	}
	
	
	
	

}
