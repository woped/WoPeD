package org.woped.quantana.dashboard.storage;


public class ArchiveProvider implements Runnable{

	MessageQueue msgQueue = null;
	
	StorageEngine dbt = null;
	
	public ArchiveProvider(MessageQueue msgQueue)  {
		
		try {
		//	this.dbt = new StorageEngine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.msgQueue = msgQueue;
		
		
	}
/*	

	public void writeData(StorageData stoda){
		
		this.msgQueue.send(stoda);
	}
	
	public StorageData readData(){
		//return this.msgQueue.receive();
		return null;
	}
	
	public void close(){
		dbt.CloseConnection();
	}
*/
	
	public void run() {
		
		System.out.println("start thread");
		
		boolean bReady = false;
		while(bReady == false)
		{
			//wait for new data
			//StorageData stoda = this.msgQueue.receive();
			//String strBar = stoda.getBar();	
			//ArrayList<SimulationObject> stodaArray = new ArrayList<SimulationObject>();
			//stodaArray.add(stoda);
			//this.dbt.InsertGeneric(stodaArray);		
			
			/*if(strBar != null)
			{
				System.out.println("	wrote entry");
			
			}else{
				
				System.out.println("	No more entries");
				return;
			}
			*/
			
		}
		System.out.println("exit thread");
		
	}
/*	
	public static void main(String[] args){
		MessageQueue msgq = new MessageQueue(100);
		
		ArchiveProvider ap = new ArchiveProvider(msgq);
		
		Thread storagethread = new Thread(ap);
		
		storagethread.start();
		
		StorageData stoda = null;
		for(int i = 0; i< 1000; i++){
			stoda = new StorageData(i,"val_"+i);
			
			System.out.println("send data");
			msgq.send(stoda);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stoda = new StorageData(0,null);
		System.out.println("send last data");
		msgq.send(stoda);
		
	}
*/
}
