package org.woped.quantana.sim;


public class SimWorkItem {
	SimCase _case;
	SimServer server;
	
	public SimWorkItem(SimCase c, SimServer s){
		_case = c;
		server = s;
	}

	public SimCase getCase() {
		return _case;
	}

	public void setCase(SimCase _case) {
		this._case = _case;
	}

	public SimServer getServer() {
		return server;
	}

	public void setServer(SimServer server) {
		this.server = server;
	}

}
