package org.woped.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Schnittstellenvertrag für das Laden und Speichern
 * von Petrimodels vom Server 
 * @author C. Krüger
 * @since 12.01.2008
 */
public interface IServer extends Remote {

	/**
	 * loadModel
	 * <p>
	 * load a petrinetmodel from the webserver with the given modelid
	 * <p>
	 * @param modelid - ID of the PetrinetModel 
	 * @return XML Structur des PetriModels
	 * @throws RemoteException
	 */
	public String loadModel(int modelid) throws RemoteException;
	
	/**
	 * saveModel
	 * <p>
	 * save a Petrinetmodel for the given UserID at the WebServer
	 * in the case of ModelID equals -1, the call returns the new ModelID 
	 * <p>
	 * @param userid - UserID for witch the Model will be saved
	 * @param modellid - ModelID to save, or -1 to saveas
	 * @return - new ModelID
	 * @throws RemoteException
	 */
	public int saveModel(int userid, int modelid) throws RemoteException;
	
	/**
	 * getList
	 * <p>
	 * returns a list of shared petrinetmodels
	 * @return - List of PetrinetModels
	 * @throws RemoteException
	 */
	public ArrayList getList() throws RemoteException;
	
}
