package org.woped.quantana.dashboard.storage;

public class SimulationStorageDataServer {
		private String ServerName = "";
		
		
		private double Accesses;
		private double AvgAccesses;
		
		
		private double AvgQLength;
		
		private double AvgResNumber;
		
		private double ServTime;
		private double AvgServTime;
		
		private double WaitTime;
		private double AvgWaitTime;
		
		
		private int Calls;
		private double AvgCalls;
		
		private int Departures;	
		private int MaxQLength;
		private int MaxResNumber;	
		private double MaxWaitTime;
		private int NumServedWhenStopped;
		private int QLengthWhenStopped;	
		private int ZeroDelays;
		
		
		public SimulationStorageDataServer(){
			
			
		}
		
		public SimulationStorageDataServer(String ServerName,
											double Accesses,
											double AvgQLength,
											double AvgResNumber,
											double AvgServTime,
											double AvgWaitTime,
											double Calls,
											int Departures,	
											int MaxQLength,
											int MaxResNumber,	
											double MaxWaitTime,
											int NumServedWhenStopped,
											int QLengthWhenStopped,
											int ZeroDelays){
			this.AvgAccesses = Accesses;
			
			this.AvgResNumber = AvgResNumber;
			this.AvgServTime =  AvgServTime;
			this.AvgWaitTime =  AvgWaitTime;
			this.AvgCalls  = Calls;
			this.Departures  = Departures;
			this.MaxQLength  = MaxQLength;
			this.MaxResNumber =  MaxResNumber;
			this.MaxWaitTime =  MaxWaitTime;
			this.NumServedWhenStopped  = NumServedWhenStopped;
			this.QLengthWhenStopped =  QLengthWhenStopped;
			this.ZeroDelays  = ZeroDelays;
		}
		
		public String getServerName() {
			return ServerName;
		}

		public void setServerName(String serverName) {
			ServerName = serverName;
		}

		public double getAccesses() {
			return Accesses;
		}

		public void setAccesses(double accesses) {
			this.Accesses = accesses;
		}

		public double getAvgAccesses() {
			return AvgAccesses;
		}

		public void setAvgAccesses(double avgAccesses) {
			this.AvgAccesses = avgAccesses;
		}
		
		public double getAvgQLength() {
			return AvgQLength;
		}

		public void setAvgQLength(double avgQLength) {
			this.AvgQLength = avgQLength;
		}

		public double getAvgResNumber() {
			return AvgResNumber;
		}

		public void setAvgResNumber(double avgResNumber) {
			this.AvgResNumber = avgResNumber;
		}

		public double getAvgServTime() {
			return AvgServTime;
		}

		public void setAvgServTime(double avgServTime) {
			this.AvgServTime = avgServTime;
		}

		/*
		public void setWaitTime(double WaitTime) {
			this.WaitTime = WaitTime;
		}
		
		public double getWaitTime() {
			return WaitTime;
		}
		*/
		
		public double getAvgWaitTime() {
			return AvgWaitTime;
		}

		public void setAvgWaitTime(double avgWaitTime) {
			this.AvgWaitTime = avgWaitTime;
		}

		public int getCalls() {
			return Calls;
		}

		public void setCalls(int calls) {
			this.Calls = calls;
		}
		
		public double getAvgCalls() {
			return AvgCalls;
		}

		public void setAvgCalls(double calls) {
			this.AvgCalls = calls;
		}


		public int getDepartures() {
			return Departures;
		}

		public void setDepartures(int departures) {
			this.Departures = departures;
		}

		public int getMaxQLength() {
			return MaxQLength;
		}

		public void setMaxQLength(int maxQLength) {
			this.MaxQLength = maxQLength;
		}

		public int getMaxResNumber() {
			return MaxResNumber;
		}

		public void setMaxResNumber(int maxResNumber) {
			this.MaxResNumber = maxResNumber;
		}

		public double getMaxWaitTime() {
			return MaxWaitTime;
		}

		public void setMaxWaitTime(double maxWaitTime) {
			this.MaxWaitTime = maxWaitTime;
		}

		public int getNumServedWhenStopped() {
			return NumServedWhenStopped;
		}

		public void setNumServedWhenStopped(int numServedWhenStopped) {
			this.NumServedWhenStopped = numServedWhenStopped;
		}

		public int getqLengthWhenStopped() {
			return QLengthWhenStopped;
		}

		public void setqLengthWhenStopped(int qLengthWhenStopped) {
			this.QLengthWhenStopped = qLengthWhenStopped;
		}

		public int getZeroDelays() {
			return ZeroDelays;
		}

		public void setZeroDelays(int zeroDelays) {
			this.ZeroDelays = zeroDelays;
		}

		public double getServTime() {
			return ServTime;
		}

		public void setServTime(double servTime) {
			ServTime = servTime;
		}

		public double getWaitTime() {
			return WaitTime;
		}

		public void setWaitTime(double waitTime) {
			WaitTime = waitTime;
		}
		
		
}
