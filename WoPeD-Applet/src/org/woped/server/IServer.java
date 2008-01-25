package org.woped.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.woped.server.holder.ModellHolder;


/**
 * Interface for the WoPeD Service
 *  
 * @author C. Krueger
 * @since 12.01.2008
 * @version 1.0
 */
public interface IServer extends Remote {
	
	
	/**
	 * getList()
	 * <p>
	 * @param userID
	 * @param shared
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<ModellHolder> getList(int userID, boolean shared) throws RemoteException;
	
	
	/**
	 * saveModel()
	 * <p>
	 * saves the given Model on the WebServer
	 * @param UserID
	 * @param ModelID
	 * @param content - XML Content of the PetriNetModel
	 * @param title - title for a new PetriNetModel
	 * @return Returns the ModelID for the PetriNetModel which is stored in the DB
	 * @throws RemoteException
	 */
	public int saveModel(int UserID, int ModelID, String content, String title) throws RemoteException;
	
	/**
	 * loadModel()
	 * <p>
	 * load the PetriNetModel with the given ModelID from the WebServer
	 * @param ModelID
	 * @return Returns the content of the PetriNetModel File
	 * @throws RemoteException
	 */
	public String loadModel(int ModelID) throws RemoteException;
	
	/**
	 * getUserID()
	 * <p>
	 * retrieves the UserID in case of the given session token
	 * after that the session token will be reseted
	 * @param token - SessionID
	 * @return Returns the UserId
	 * @throws RemoteException
	 */
	public int getUserID(String token) throws RemoteException; 

}
