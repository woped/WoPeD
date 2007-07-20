package org.woped.quantana.simulation;

public class WorkItem {
	
	private Case _case;
	private Server server;
	
	public WorkItem(Case c, Server s){
		_case = c;
		server = s;
	}
	
	/*public void enqueue(){
		server.enqueue(this);
	}*/

	public Case getCase() {
		return _case;
	}

	public void setCase(Case _case) {
		this._case = _case;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
}
