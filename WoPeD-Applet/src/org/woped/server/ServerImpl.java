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

import org.woped.applet.Constants;
import org.woped.core.utilities.LoggerManager;
import org.woped.server.holder.ModellHolder;

/**
 * Implementation of the IServer Interface
 * 
 * @author C. Krüger
 * @since 12.01.2008
 *
 */
public class ServerImpl extends UnicastRemoteObject implements IServer {

	
	private String path = "."+File.separator+"modells"+File.separator;
	
	private Connection connection = null;
	
	private String connectionUrl = "jdbc:mysql://localhost/wopedweb?" + 
                                   "user=wopedweb&password=geheim";
	
	
	
	/**
	 * Implementations must have an explicit
	 * constructor in order to declare the 
	 * RemoteException
	 * @throws RemoteException
	 */
	public ServerImpl() throws RemoteException {
		super();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();			
			createConnection();
		} catch (Exception e) {
			LoggerManager.fatal(Constants.APPLET_LOGGER, "Connection Error for Database!");
			e.printStackTrace();
		}
		
	}

	/**
	 * getList()
	 * <p>
	 * Returns a list of all PetriNetModels which are shared and which owns to the users
	 * @param userID - ID of the User
	 * @param shared - also shared Models
	 * @return - List of all loadable PetriNetModels.
	 * @throws RemoteException
	 * @see {@link IServer#getList(int)}
	 */
	@Override
	public ArrayList<ModellHolder> getList(int userID, boolean shared) throws RemoteException {
		
		// resultType 
		ArrayList<ModellHolder> resultValue = new ArrayList<ModellHolder>();
		// workItem		
		Statement query = null; 
		ResultSet result = null;
			
		String sql = "SELECT * FROM `modelle` WHERE `userid`="+userID;
		if (shared) {
			sql += " OR `shared`=1";
		}
		System.out.println(sql);
		
		try {
			query = connection.createStatement();
			
			result = query.executeQuery(sql);
			if (result != null) {
				while (result.next()) {
					resultValue.add(new ModellHolder(result.getInt("modellid"),result.getString("titel")));
				}
			}
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: " + e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: " + e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: " + e.getErrorCode());
		} finally {
			if (query != null) {
				try {
					query.close();
				} catch (SQLException e) {
					// ignore
				}				
			}
			if (result != null) {
				try {
					result.close();					
				} catch (SQLException e) {
					// ignore
				}
			}
		}
		return resultValue;
	}

	/**
	 * loadModel()
	 * <p>
	 * loads above the Web, the specific PetriNetModel with the given modelID
	 * @param modelid - ID for the PetriNetModel which has to load
	 * @return Returns the content of the PetriNetModel File on the WebServer
	 * @throws RemoteException
	 * @see {@link IServer#loadModel(int)} 
	 */
	@Override
	public String loadModel(int modelid) throws RemoteException {
		
		StringBuffer buffer = new StringBuffer();
		
		// check if the PetriNet Model exists
		if (existsModel(modelid)) {
			// load the File with the Filename <modelid>.pnml
			if (new File(path + modelid+ ".pnml").exists()) {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + modelid + ".pnml")));
					
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
				throw new RemoteException("Datei existiert nicht");
			}
		} else {
			// it doesnt exists a entry in the DB
			throw new RemoteException("Model Existiert nicht in der DB");
		}
				
		// returns the content of StringBuffer
		return buffer.toString();
	}

	/**
	 * saveModel()
	 * <p>
	 * save the PetriNetModel over the web on the WebServer under the given modelId and userID
	 * @param userid - ID of the User 
	 * @param modelid - ID of the Model
	 * @param content - content in XML
	 * @param title - Title for a new PetriNetModel
	 * @return Returns the new ModelID, when the PetriNetModel for the first time saved
	 * @throws RemoteException
	 * @see {@link IServer#saveModel(int, int, String, String)} 
	 */
	@Override
	public int saveModel(int userid, int modelid, String content, String title) throws RemoteException {
		
		// if user authorized to save the model
		if (!existsModelForUser(modelid, userid)) {
			modelid = getNewModelID(userid,title);
		} else {
			
			// save content to File
			File file = new File(path+modelid+".pnml");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// ignore
				}
			}
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file));
				bw.write(content);
				bw.flush();
				bw.close();
			} catch (IOException e) {
				LoggerManager.fatal(Constants.APPLET_LOGGER, "Fehler beim Schreiben in der Datei");
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						// ignore						
					}
				}
			}
			
		} 		
		return modelid;
	}
	
	/**
	 * getNewModelID()
	 * <p> 
	 * @param userid
	 * @param title
	 * @return
	 */
	private int getNewModelID(int userid, String title) {
		
		Statement stmt = null;
		ResultSet result = null;
		int resultValue = -1;
		
		try {
			stmt = connection.createStatement(Statement.CLOSE_ALL_RESULTS,Statement.RETURN_GENERATED_KEYS);
			stmt.executeQuery("INSERT INTO `modellid` (userid,titel,lastedit) VALUES ("+userid+",'"+title+"',NOW())");
			result = stmt.getGeneratedKeys();
			if (result.next()) {
				resultValue = result.getInt(1);
			}
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: " + e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: " + e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: " + e.getErrorCode());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {
					// ignore
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
		
		return 0;
	}

	/**
	 * existsModelForUser()
	 * <p>
	 * checks if the userid owns of the modelid	 * 
	 * @param modelID - ID of the Model
	 * @param userID - ID of the User
	 * @return Returns true if the userID and modelID correct, otherwise false
	 */
	private boolean existsModelForUser(int modelID, int userID) {
		
		boolean returnValue = false;
		
		ResultSet result = null;
		Statement query = null;
		
		try {
			query = connection.createStatement();
			result = query.executeQuery("SELECT * FROM `modelle` WHERE `modellid`="+modelID);
			if (result.next()) {
				if (userID == result.getInt("userid")) {
					returnValue = true;
				} else {
					returnValue = false;
				}
			}			
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: " + e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: " + e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: " + e.getErrorCode());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {
					// ignore
				}
			}
			if (query != null) {
				try {
					query.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
		
		
		return returnValue;
	}

	/**
	 * existsModel()
	 * <p>
	 * checks if the modelID exists	 * 
	 * @param modelid - ID of the Model
	 * @return Returns true if the ModelID exists, otherwise false
	 */
	private boolean existsModel(int modelid) {
		
		boolean returnValue = false;
		
		ResultSet result = null;
		
		try {
			// Query
			Statement query = connection.createStatement();
			result = query.executeQuery("SELECT * FROM `modelle` WHERE `modellid`="+modelid);
			if (result.last()) {
				returnValue = true;
			}		
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: " + e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: " + e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: " + e.getErrorCode());
		} finally {
			if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				//ignore
			}
			}
		}
			
		return returnValue;
	}
	
	/**
	 * getUserID()
	 * <p>
	 * @param token
	 * @see {@link IServer#getUserID(String)}
	 */
	public int getUserID(String token) throws RemoteException {
		
		int resultValue = -1;
		Statement stmt = null;
		ResultSet result = null;
		
		try {
			stmt = connection.createStatement();
			result = stmt.executeQuery("SELECT * FROM `benutzer` WHERE `sessionid`='"+token+"'");
			if (result.next()) {
				resultValue = result.getInt("userid");
			}
			// erase session token
			stmt.executeQuery("UPDATE `benutzer` SET `sessionid`=" + null + " WHERE `sessionid`='"+token+"'");
		} catch (SQLException e) {
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLException: " + e.getMessage());
			LoggerManager.error(Constants.APPLET_LOGGER, "SQLState: " + e.getSQLState());
			LoggerManager.error(Constants.APPLET_LOGGER, "VendorCode: " + e.getErrorCode());
		} finally {
			if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				//ignore
			}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
		return resultValue;
	}
	
	
	/**
	 * createConnection
	 * <p>
	 * create Connection to the Database
	 */
	private void createConnection() {
		try {
			connection = DriverManager.getConnection(connectionUrl);			
		} catch (SQLException e) {
			LoggerManager.fatal(Constants.APPLET_LOGGER, "Es konnte keine Verbindung zur mySql DB hergestellt werden!");
		}
	}

	/**
	 * finalize()
	 * <p>
	 * shutdown the connection
	 */
	@Override
	protected void finalize() throws Throwable {
		if (connection != null) {
			connection.close();
		}
		
		super.finalize();
	}
	
	
	
	
}
