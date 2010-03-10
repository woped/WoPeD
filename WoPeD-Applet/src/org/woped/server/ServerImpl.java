package org.woped.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.woped.applet.Constants;
import org.woped.core.utilities.LoggerManager;
import org.woped.server.configuration.PropertyLoader;
import org.woped.server.holder.ModellHolder;

/**
 * Implementation of the IServer Interface
 * 
 * @author C. Krüger
 * @since 12.01.2008
 * 
 * @author Dörner
 * @since 01.05.2008
 * 
 */
public class ServerImpl extends UnicastRemoteObject implements IServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path = File.separator + Constants.SERVER_MODELLS
			+ File.separator;


	/**
	 * Implementations must have an explicit constructor in order to declare the
	 * RemoteException
	 * 
	 * @param String
	 *            workingDirectory
	 * @throws RemoteException
	 *             TODO check if the given workingDirectory exists
	 */
	public ServerImpl(String workingDirectory) throws RemoteException {
		super();
		
		try {
			Class.forName(Constants.DB_DRIVER).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// set workingDirectory to .
		if (workingDirectory == null || workingDirectory.equals("")) {
			path = "." + path;
		} else {
			path = workingDirectory + path;
		}
	}

	/**
	 * getList()
	 * <p>
	 * Returns a list of all PetriNetModels which are shared and which owns to
	 * the users
	 * 
	 * @param userID -
	 *            ID of the User
	 * @param shared -
	 *            also shared Models
	 * @return - List of all loadable PetriNetModels.
	 * @throws RemoteException
	 * @see {@link IServer#getList(int)}
	 */
	public ArrayList<ModellHolder> getList(int userID, boolean shared)
			throws RemoteException {

		// resultType
		ArrayList<ModellHolder> resultValue = new ArrayList<ModellHolder>();
		// workItem
		Statement query = null;
		ResultSet result = null;
		Connection connection = null;

		try {
			connection = getConnection();
			query = connection.createStatement();
			if (shared) {
				result = query.executeQuery(String.format(
						Constants.SQL_SHARED_MODEL_LIST, userID));
			} else {
				result = query.executeQuery(String.format(
						Constants.SQL_MODEL_LIST, userID));
			}

			if (result != null) {
				while (result.next()) {
					resultValue.add(new ModellHolder(result.getInt("modellid"),
							result.getString("titel")));
				}
			}
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: "
					+ e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: "
					+ e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: "
					+ e.getErrorCode());
		} finally {
			try {
				if (query != null) {
					query.close();
				}
				
				if (result != null) {
					result.close();
				}
				
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		return resultValue;
	}

	/**
	 * loadModel()
	 * <p>
	 * loads above the Web, the specific PetriNetModel with the given modelID
	 * 
	 * @param modelid -
	 *            ID for the PetriNetModel which has to load
	 * @return Returns the content of the PetriNetModel File on the WebServer
	 * @throws RemoteException
	 * @see {@link IServer#loadModel(int)}
	 */
	public String loadModel(int modelid) throws RemoteException {

		StringBuffer buffer = new StringBuffer();

		// check if the PetriNet Model exists
		if (existsModel(modelid)) {
			// load the File with the Filename <modelid>.pnml
			if (new File(path + modelid + ".pnml").exists()) {
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(path
									+ modelid + ".pnml")));

					String content = null;
					// read the content
					while ((content = br.readLine()) != null) {
						buffer.append(content);
					}
					br.close();

				} catch (FileNotFoundException e) {
					// ignore
				} catch (IOException e) {
					// ignore
				}
			} else {
				// throw a RemoteException
				throw new RemoteException(Constants.ERROR_FILE_NOT_EXISTS);
			}
		} else {
			// it doesnt exists a entry in the DB
			throw new RemoteException(String.format(
					Constants.ERROR_NO_MATH_FOUND_FOR_MODELID, modelid));
		}

		// returns the content of StringBuffer
		return buffer.toString();
	}

	/**
	 * saveModel()
	 * <p>
	 * save the PetriNetModel over the web on the WebServer under the given
	 * modelId and userID
	 * 
	 * @param userid -
	 *            ID of the User
	 * @param modelid -
	 *            ID of the Model
	 * @param content -
	 *            content in XML
	 * @param title -
	 *            Title for a new PetriNetModel
	 * @return Returns the new ModelID, when the PetriNetModel for the first
	 *         time saved
	 * @throws RemoteException
	 * @see {@link IServer#saveModel(int, int, String, String)}
	 */
	public int saveModel(int userid, int modelid, String content, String title)
			throws RemoteException {

		// if user authorized to save the model
		if (!existsModelForUser(modelid, userid)) {
			// for a new model, check the available free memory
			if (freeMemoryForUser(userid) > 0) {
				modelid = getNewModelID(userid, title);
			} else {
				throw new RemoteException(Constants.ERROR_NOT_ENOUGH_MEMORY);
			}

		}

		// save content to File
		File file = new File(path + modelid + ".pnml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// ignore
			}
		}
		// writes the xml string to file
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			LoggerManager.fatal(Constants.APPLET_LOGGER,
					"Fehler beim Schreiben in der Datei");
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return modelid;
	}

	/**
	 * freeMemoryForUser()
	 * <p>
	 * checks if exists enough memory to save the new model
	 * 
	 * @param userid -
	 *            Id of User
	 * @return - free memory
	 */
	private long freeMemoryForUser(int userID) throws RemoteException {
		long maxMemory = 2048;
		if (PropertyLoader.getProperty(Constants.RMI_MEMORY) != null) {
			maxMemory = Long.parseLong(PropertyLoader
					.getProperty(Constants.RMI_MEMORY));
		}
		// memory in use
		long usageMemory = 0;

		// get all availably models for User
		ArrayList<ModellHolder> models = getList(userID, false);

		File file;
		// sums the size of all models
		for (Iterator<ModellHolder> iterator = models.iterator(); iterator.hasNext();) {
			ModellHolder name = (ModellHolder) iterator.next();
			file = new File(path + name.getModellID() + ".pnml");
			if (file.exists()) {
				usageMemory += (file.length() / 1024);
			}
		}
		// the difference of max and usage is the freeMemory
		return maxMemory - usageMemory;
	}

	/**
	 * getNewModelID()
	 * <p>
	 * 
	 * @param userid
	 * @param title
	 * @return
	 */
	private int getNewModelID(int userid, String title) {
		Statement stmt = null;
		ResultSet result = null;
		Connection connection = null;
		int resultValue = -1;

		try {
			connection = getConnection();
			stmt = connection.createStatement(Statement.CLOSE_ALL_RESULTS,
					Statement.RETURN_GENERATED_KEYS);
			Object[] args = { userid, title };
			stmt.executeUpdate(String.format(Constants.SQL_INSERT_MODEL, args),
					Statement.RETURN_GENERATED_KEYS);

			result = stmt.getGeneratedKeys();
			if (result.next()) {
				resultValue = result.getInt(1);
			}
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: "
					+ e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: "
					+ e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: "
					+ e.getErrorCode());
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				
				if (stmt != null) {
					stmt.close();	
				}
				
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultValue;
	}

	/**
	 * existsModelForUser()
	 * <p>
	 * checks if the userid owns of the modelid *
	 * 
	 * @param modelID -
	 *            ID of the Model
	 * @param userID -
	 *            ID of the User
	 * @return Returns true if the userID and modelID correct, otherwise false
	 */
	private boolean existsModelForUser(int modelID, int userID) {

		boolean returnValue = false;

		ResultSet result = null;
		Statement query = null;
		Connection connection = null;

		try {
			connection = getConnection();
			query = connection.createStatement();
			result = query.executeQuery(String.format(
					Constants.SQL_EXISTS_MODEL, modelID));
			if (result.next()) {
				if (userID == result.getInt("userid")) {
					returnValue = true;
				} else {
					returnValue = false;
				}
			}
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: "
					+ e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: "
					+ e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: "
					+ e.getErrorCode());
		} finally {
			try{
				if (result != null) {
					result.close();
				}
				
				if (query != null) {
					query.close();
				}
				
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return returnValue;
	}

	/**
	 * existsModel()
	 * <p>
	 * checks if the modelID exists *
	 * 
	 * @param modelid -
	 *            ID of the Model
	 * @return Returns true if the ModelID exists, otherwise false
	 */
	private boolean existsModel(int modelid) {

		boolean returnValue = false;

		Statement stmt = null;
		ResultSet result = null;
		Connection connection = null;
	
		try {
			// Query
			connection = getConnection();
		    stmt = connection.createStatement();
			result = stmt.executeQuery(String.format(
					Constants.SQL_EXISTS_MODEL, modelid));
			if (result.last()) {
				returnValue = true;
			}
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: "
					+ e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: "
					+ e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: "
					+ e.getErrorCode());
		} finally {
			try{
				if (result != null) {
					result.close();
				}
				
				if (stmt != null) {
					stmt.close();
				}
			
				if (connection != null){
					connection.close();
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return returnValue;
	}

	/**
	 * getUserID()
	 * <p>
	 * 
	 * @param token
	 * @see {@link IServer#getUserID(String)}
	 */
	public int getUserID(String token) throws RemoteException {

		int resultValue = -1;
		Statement stmt = null;
		ResultSet result = null;
		Connection connection = null;

		try {
			connection = getConnection();
			stmt = connection.createStatement();
			result = stmt
					.executeQuery("SELECT * FROM benutzer WHERE sessionid='"
							+ token +"'");
			if (result.next()) {
				resultValue = result.getInt("userid");
			}
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: "
					+ e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: "
					+ e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: "
					+ e.getErrorCode());
		} finally {
			try{
				if (result != null) {
					result.close();
				}
				
				if (stmt != null) {
					stmt.close();
				}
				
				if (connection != null){
					connection.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			connection = getConnection();
			stmt = connection.createStatement();
			// erase session token
			stmt.executeUpdate("UPDATE benutzer SET sessionid=" + null
					+ " WHERE sessionid='" + token + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				if (result != null) {
					result.close();
				}
				
				if (stmt != null) {
					stmt.close();
				}
				
				if (connection != null){
					connection.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return resultValue;
	}

	/**
	 * createConnection
	 * <p>
	 * create Connection to the Database
	 */
	private Connection getConnection() {
		Connection connection = null;
		try {
			connection  = DriverManager.getConnection(createConnectionUrl());
		} catch (SQLException e) {
			LoggerManager
					.fatal(Constants.APPLET_LOGGER,
							"Es konnte keine Verbindung zur mySql DB hergestellt werden!");
		}
		
		return connection;
	}

	/**
	 * 
	 * @return ConnectionURL
	 */
	public String createConnectionUrl() {
		// "jdbc:mysql://host/db?"
		// "user=xxxx&password=xxxx";
		String url = "jdbc:mysql://";
		url += PropertyLoader.getProperty("dbHost");
		url += "/";
		url += PropertyLoader.getProperty("dbDB");
		url += "?user=";
		url += PropertyLoader.getProperty("dbUser");
		url += "&password=";
		url += PropertyLoader.getProperty("dbPw");

		return url;
	}

}
