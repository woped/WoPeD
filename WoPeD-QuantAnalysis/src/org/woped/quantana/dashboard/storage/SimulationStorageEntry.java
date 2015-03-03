package org.woped.quantana.dashboard.storage;

public class SimulationStorageEntry{
	
	private Integer ID = 0;
	private long Tick = 0;
	private SimulationStorageData Data = null;

	
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public long getTick() {
		return Tick;
	}
	public void setTick(long tick) {
		Tick = tick;
	}
	public SimulationStorageData getData() {
		return Data;
	}
	public void setData(SimulationStorageData data) {
		Data = data;
	}
	public SimulationStorageEntry(Integer Id, long Tick, SimulationStorageData rd) {
		this.ID = Id;
		this.Tick = Tick;
		this.Data = rd;
		
	}
	public SimulationStorageEntry() {
		
	}
	
}
