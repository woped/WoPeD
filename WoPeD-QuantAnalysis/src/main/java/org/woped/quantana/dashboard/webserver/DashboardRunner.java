package org.woped.quantana.dashboard.webserver;



import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.dashboard.storage.StorageEngine;
//import org.woped.quantana.gui.EmbeddedBrowserView;


public class DashboardRunner implements Runnable{

	StorageEngine storageengine = null;
	ClientStarter client = null;
	ThinServer server = ThinServer.createServer();
	

	
	public DashboardRunner(StorageEngine se,int port) {
		
		this.storageengine = se;
		server.setStorageEngine(storageengine);
		
		server.addAction(new TestpathAction());
		server.addAction(new GetRequestAction());
		server.addAction(new PostRequestAction());
		server.addAction(new DynamicPathAction());
		server.addAction(new ExampleAction());
	}
	
	public void RunServer() {	
        
        //Browser Starten
		//EmbeddedBrowserView browser = new EmbeddedBrowserView(2711);
		
		
		//storageengine.CreateTable(Table.SIM_VALUES);		
		server.start();

	}

	  protected void finalize() throws Throwable {
		  
		  LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "FINALIZE!!!");
		  
	  };
	
	@Override
	public void run() {
		this.RunServer();
	}
	static Boolean bAlreadyRunning = false;
	public StorageEngine startServer(){
		
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "DashBoardRunner is already running: " + bAlreadyRunning);
			bAlreadyRunning = true;

		Thread t = new Thread(this);
		t.start();
		return storageengine;
	}
}