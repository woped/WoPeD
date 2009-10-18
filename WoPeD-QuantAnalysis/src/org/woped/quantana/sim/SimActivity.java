package org.woped.quantana.sim;

import org.woped.quantana.resourcealloc.Resource;

public class SimActivity {
	
	SimCase _case;
	SimServer server;
	Resource resource;
	
	public SimActivity(SimCase c, SimServer s, Resource r){
		_case = c;
		server = s;
		resource = r;
	}	
	public SimActivity(SimWorkItem wi, Resource r){
		_case = wi.getCase();
		server = wi.getServer();
		resource = r;
	}

	public SimCase getCase() {
		return _case;
	}

	public Resource getResource() {
		return resource;
	}	

	public SimServer getServer() {
		return server;
	}	
}
