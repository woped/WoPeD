package org.woped.quantana.dashboard.storage;

public class SimulationStorageDataRessource extends SimulationObject{

		private String RessourceName = "";
		private double IdleTime = 0;
		private double UtilizationRatio = 0;
		
		private double avgIdleTime = 0;
		private double avgUtilizationRatio = 0;
		
		
		public SimulationStorageDataRessource(String name, double idletime, double utilRatio){
			this.setRessourceName(name);
			this.setIdleTime(idletime);
			this.setUtilizationRatio(utilRatio);
			
			//beim ersten Duchlauf ist avg = aktueller Wert
			this.setAvgIdleTime(idletime);
			this.setAvgUtilizationRatio(utilRatio);
		}
		public SimulationStorageDataRessource(){
			
		}
		public String getRessourceName() {
			return RessourceName;
		}
		public void setRessourceName(String ressourceName) {
			RessourceName = ressourceName;
		}
		public double getIdleTime() {
			return IdleTime;
		}
		public void setIdleTime(double idleTime) {
			IdleTime = idleTime;
		}
		public double getUtilizationRatio() {
			return UtilizationRatio;
		}
		public void setUtilizationRatio(double utilizationRatio) {
			UtilizationRatio = utilizationRatio;
		}
		public double getAvgIdleTime() {
			return avgIdleTime;
		}
		public void setAvgIdleTime(double avgIdleTime) {
			this.avgIdleTime = avgIdleTime;
		}
		public double getAvgUtilizationRatio() {
			return avgUtilizationRatio;
		}
		public void setAvgUtilizationRatio(double avgUtilizationRatio) {
			this.avgUtilizationRatio = avgUtilizationRatio;
		}
}
