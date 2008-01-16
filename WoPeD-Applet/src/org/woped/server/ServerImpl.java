package org.woped.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Implementation of the IServer Interface
 * 
 * @author C. Krüger
 * @since 12.01.2008
 *
 */
public class ServerImpl extends UnicastRemoteObject implements IServer {

	/**
	 * Implementations must have an explicit
	 * constructor in order to declare the 
	 * RemoteException
	 * @throws RemoteException
	 */
	public ServerImpl() throws RemoteException {
		super();
	}

	@Override
	public ArrayList getList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loadModel(int modelid) throws RemoteException {
		
		// DB Abfrage ob ModelID vorhanden ist
		
		// wenn ja, dann Datei laden ModelID.pngl
		
		// und zurückgeben, entweder als String, oder als Objekt
		
		
		return null;
	}

	@Override
	public int saveModel(int userid, int modelid) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
}
