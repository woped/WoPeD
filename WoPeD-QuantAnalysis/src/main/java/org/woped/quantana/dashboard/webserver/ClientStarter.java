package org.woped.quantana.dashboard.webserver;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.woped.core.utilities.LoggerManager;

public class ClientStarter {

		static Boolean bAutoUpdate = false;

        Thread runBrowser = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String url = "http://localhost:" + 2711 + "/getrequest/?action=showdashboardFull&AutoUpdate=" + Boolean.toString(ClientStarter.bAutoUpdate);
				
		        if(Desktop.isDesktopSupported()){
		            Desktop desktop = Desktop.getDesktop();
		            try {
		                desktop.browse(new URI(url));
		                LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "ClientStarter : client gestartet");
		            } catch (IOException | URISyntaxException e) {
		                // TODO Auto-generated catch block
		            	LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER,"Exception in runBrowser.run" + e.toString());
		                //e.printStackTrace();
		            }
		        }else{
		        	LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "Starting Client-Runtime");
		        	
		            Runtime runtime = Runtime.getRuntime();
		            try {
		                runtime.exec("xdg-open " + url);
		            } catch (IOException e) {
		                
		            	LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER,"IOException instarting runtime" + e.toString());
		                //e.printStackTrace();
		            }
		        }
				
			}
		});
        
        public void startClient(){
        	LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"ClientStarter::startClient");
        	runBrowser.start();
        };
        public void startClient(boolean bAutoUpdate){
        	ClientStarter.bAutoUpdate = bAutoUpdate;
        	LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"ClientStarter::startClient");
        	runBrowser.start();
        };
		
}
