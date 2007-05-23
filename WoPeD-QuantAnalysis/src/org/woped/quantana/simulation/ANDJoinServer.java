package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.HashMap;

public class ANDJoinServer extends Server {
	
	private HashMap<Case, ArrayList<CaseCopy>> copyList = new HashMap<Case, ArrayList<CaseCopy>>();
	
	public ANDJoinServer(Simulator sim, String id, String name, ProbabilityDistribution dist){
		super(sim, id, name, dist);
	}
	
	public void doService(){
		super.doService();
		
	}

	public HashMap<Case, ArrayList<CaseCopy>> getCopyList() {
		return copyList;
	}

	public void setCopyList(HashMap<Case, ArrayList<CaseCopy>> copyList) {
		this.copyList = copyList;
	}
}
