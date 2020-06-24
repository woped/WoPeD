package org.woped.quantana.dashboard.storage;

public class SimulationStorageData  {
	
	private SimulationStorageDataProcess process;
	
	private SimulationStorageDataRessource[] ressources;
	
	private SimulationStorageDataServer[] servers;
	
	
	/*
	 * getter and setter
	 */
	public SimulationStorageDataProcess getProcess() {
		return process;
	}
	public void setProcess(SimulationStorageDataProcess process) {
		this.process = process;
	}
	
	public SimulationStorageDataRessource[] getRessources() {
		return ressources;
	}
	public void setRessources(SimulationStorageDataRessource[] ressources) {
		this.ressources = ressources;
	}
	public SimulationStorageDataServer[] getServers() {
		return servers;
	}
	public void setServers(SimulationStorageDataServer[] servers) {
		this.servers = servers;
	}
	
}
