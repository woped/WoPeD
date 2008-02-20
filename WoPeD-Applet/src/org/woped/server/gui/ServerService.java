package org.woped.server.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;

import org.woped.server.ServerImpl;
import org.woped.server.tunnel.FixedPortRMISocketFactory;

public class ServerService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String host = "";
		String service = "";
		String workingDir = "";
				
		
		if (args.length != 3) {
			System.out.println("Not Enough Params");
			System.out.println("Server host servicename workingDirectory");
			System.exit(-1);
		} else {
			host = args[0];
			service = args[1];
			workingDir = args[2];
		
			try {
				System.out.println("Server starts up");
				RMISocketFactory.setSocketFactory(new FixedPortRMISocketFactory());
				LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
				Naming.bind("rmi://"+host+"/"+service, new ServerImpl(workingDir));
				System.out.println("Server ready");
			} catch (RemoteException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (AlreadyBoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}

		
		}
		
		
		
	}

}
