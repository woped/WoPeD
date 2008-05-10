package org.woped.server;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.woped.core.Constants;
import org.woped.core.utilities.LoggerManager;
import org.woped.server.configuration.PropertyLoader;


/**
 * Singleton Class to handle the Client RMI
 * @author C. Krüger
 * @author Sascha
 *
 */
public class ServerLoader {

	/**
	 * returns the Instance to the remote Service
	 * @return
	 */
	static public IServer getInstance() {
		IServer instance = null;
		
		try {
			instance = (IServer)Naming.lookup(PropertyLoader.getProperty("rmiURL"));
		} catch (MalformedURLException e) {
			LoggerManager.fatal(Constants.CORE_LOGGER, e.getMessage());
		} catch (RemoteException e) {
			LoggerManager.fatal(Constants.CORE_LOGGER, e.getMessage());
		} catch (NotBoundException e) {
			LoggerManager.fatal(Constants.CORE_LOGGER, e.getMessage());
		} 
		return instance;
	}
	
	
	
}
