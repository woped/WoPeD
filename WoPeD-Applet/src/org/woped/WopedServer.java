package org.woped;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.woped.core.Constants;
import org.woped.core.utilities.LoggerManager;
import org.woped.server.ServerImpl;

/**
 * Class to run the RMI Server
 * @author C. Krüger
 *
 */
public class WopedServer {

	/**
	 * Constructor WopedServer
	 * <p>
	 * Starts the Registry Service
	 * and binds the ServerImpl to <b>WopedServer</b>
	 */
	public WopedServer() {
		try {
			//System.setSecurityManager(new RMISecurityManager());
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			Naming.bind("rmi://localhost:1099/WopedService", new ServerImpl());
		} catch (AlreadyBoundException e) {
			LoggerManager.fatal(Constants.CORE_LOGGER,e.getMessage());
		} catch (MalformedURLException e) {
			System.out.println(e);
		} catch (RemoteException e) {
			System.out.println(e);
		}
		
	}
	
	
	/**
	 * Run Method Server 
	 * @param args
	 */
	public static void main(String[] args) {
		new WopedServer();

	}

}
