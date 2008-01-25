package org.woped.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.woped.core.Constants;
import org.woped.core.utilities.LoggerManager;


/**
 * Singleton Class to handle the Client RMI
 * @author C. Krüger
 *
 */
public class ServerLoader {

	static public String url = "rmi://193.196.7.194:1099/WopedService"; 
	
	static public IServer instance = null;
	
	/**
	 * IP des Servers fehlt noch
	 * @return
	 */
	static public IServer getInstance() {
		if (null == instance) {
			try {
				instance = (IServer)Naming.lookup(url);
			} catch (MalformedURLException e) {
				LoggerManager.fatal(Constants.CORE_LOGGER, e.getMessage());
			} catch (RemoteException e) {
				LoggerManager.fatal(Constants.CORE_LOGGER, e.getMessage());
			} catch (NotBoundException e) {
				LoggerManager.fatal(Constants.CORE_LOGGER, e.getMessage());
			} 
		}
		return instance;
	}
	
}
