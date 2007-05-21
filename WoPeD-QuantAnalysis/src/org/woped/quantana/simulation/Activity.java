package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;

public class Activity {
	
	private Case _case;
	private Server server;
	private Resource resource;
	
	public Activity(Case c, Server s, Resource r){
		_case = c;
		server = s;
		resource = r;
	}
	
	public Activity(WorkItem wi, Resource r){
		_case = wi.get_case();
		server = wi.getServer();
		resource = r;
	}

	public Case get_case() {
		return _case;
	}

	public void set_case(Case _case) {
		this._case = _case;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
	public void startService(){
		
	}
}
