package org.woped.quantana.dashboard.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.ResourceBundle;

import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.dashboard.storage.StorageEngine;



/**
 * 
 * @author chrisn
 *
 */
public class ThinServer {
	
	//Archive
	StorageEngine storageengine = null;
	
	//Thinserver 
	private int port;
	private boolean shutdown = false;
	private List<ThinServerAction> actions = new ArrayList<ThinServerAction>();
	private ServerSocket serverSocket = null;
	//private String publicFolderPath = ".";
	private ThinServerFileReader fileReader = null;
	protected static ResourceBundle rb = PropertyResourceBundle.getBundle("org.woped.gui.translations.Messages");
	
	
	public static ThinServer createServer(){
		return new ThinServer(2711);
	}
	public static ThinServer createServer(int port){
		return new ThinServer(port);
	}
	private ThinServer(int port){
        
		this.port = port;
		
	}
	
	/**
	 * Returns an Image object that can then be painted on the screen. 
	 *
	 * @param  url  an absolute URL giving the base location of the image
	 * @return      the image at the specified URL
	 * @see         Image
	 * @exception 	(@throws is a synonym added in Javadoc 1.2)
	 */
	public void start(){
		try {
			LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "Webserver is getting started");
			//ServerSocket serverSocket = new ServerSocket(port);
			while(true){
				if(shutdown){
					LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "Webserver is shutting down");
					break;
				}
				//LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "bind socket");
				
				Socket socket = getServerSocket().accept();
				Request request = new Request(socket);
				Response response = new Response(socket);
                                
                                // >> SessionCookie
                                determineSession(request, response);
                                // << SessionCookie
                               
                
                if(request.getPath().startsWith("/shutdownserver")){
                	this.shutDown();
                }
                                
				// >> File Handling
				if(request.getPath().startsWith("/public/")){
					LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "request for a file");
					
					request.getPath().replace("../", "");
					request.getPath().replace("/..", "");
					
					byte[] content = getFileReader().getFileContent(request.getPath());
					if(content.length == 0){
						LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER, "responing STATUS_NOT_FOUND");
						response.setStatus(Response.STATUS_NOT_FOUND);
					}else{
						response.setBinContent(content);
						response.setContentType(getFileReader().getContentType(request.getPath()));	
					}
				}else{
				// << File Handling
					for(ThinServerAction action:actions){
                                            if(request.getRequestProcessState().stopProcessing()){
                                                break;
                                            }
                                            if(action.mapActionPath(request)){
                                                action.doAction(request, response);
                                            }
					}
				}
				if(response.getBinContent().length > 0){
					LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "request for binary content");
					
					OutputStream outputstream = socket.getOutputStream();
					outputstream.write(response.getBinResponse());
					outputstream.flush();
					outputstream.close();
				}else{
					//LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "any other request");
					
					PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					writer.println(response.getResponse());
					writer.println("\r\n");
					writer.flush();
					writer.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER, "Exception in Thinserver.start()" +e.toString());
			e.printStackTrace();
			sendShutdownRequest();
		}
	}
	static Boolean bAlreadyRunning = false;
	private ServerSocket getServerSocket(){
		
		
		//LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "webserver is already running: " + bAlreadyRunning);
		bAlreadyRunning = true;
		if(serverSocket == null){
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER, "Exception in Thinserver.getServerSocket()" +e.toString());
			}
		}
		return serverSocket;
	}
	public void shutDown(){
		
		LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "webserver is shuting down ");
		
		shutdown = true;
		try {
			getServerSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER, "shut down failed: " +e.toString());
		}
		
	}
	public void addAction(ThinServerAction action){
		
		ThinServerAction.setStorageEngine(this.storageengine);
		actions.add(action);
		
	}
	
	
	public void open(){
		URI uri;
		try {
			
			LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "getting URL of local service and open the System-Default-Browser ");
			
			uri = new URI("localhost:"+port);
			
			java.awt.Desktop.getDesktop().browse(uri);
			
		} catch (URISyntaxException e) {
			
			LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER, "URISyntaxException in .open(): " + e.toString() );
			
		} catch (IOException e) {
			
			LoggerManager.error(Constants.DASHBOARDWEBSRV_LOGGER, "IOException in .open(): " + e.toString() );
			
		}
		
	}
	/*
	public void setPublicFolderPath(String publicFolderPath){
		this.publicFolderPath = publicFolderPath;
	}
	*/
	public ThinServerFileReader getFileReader(){
		LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "get file reader");
		
		if(fileReader == null){
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "new filereader will be created");
			fileReader = new DefaultFileReader();
		}
		return fileReader;
	}
	public void setFileReader(ThinServerFileReader fileReader){
		LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "set file reader");
		
		this.fileReader = fileReader;
	}
        
        private HashMap<String,SessionData> sessions = new HashMap<String,SessionData>();
        public void determineSession(Request request, Response response){
            HttpCookie cookie = request.getCookie("pts_sessionid");
            SessionData session = null;
            if(cookie != null){
                session = sessions.get(cookie.getValue());
            }
            if(cookie == null || session == null){
                // >> Create new Session
                String sessionId = createRandomString();
                while(sessions.get(sessionId) != null){
                    sessionId = createRandomString();
                }
                session = new SessionData();
                sessions.put(sessionId, session);
                cookie = new HttpCookie("pts_sessionid",sessionId);
                response.setCookie(cookie);
            }
            request.setSessionData(session);
        }
        
        private String createRandomString(){
            char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            return sb.toString();
        }
        
        public void setStorageEngine(StorageEngine se){
        	LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, "Storage Engine will be set");
        	this.storageengine = se;
        }
        
        private void sendShutdownRequest(){
        	
        	Socket socket;
			try {
				socket = new Socket("http://127.0.0.1", this.port);
				
				String request = "get ZZGNH http/1.0\n\n";
	        	OutputStream os = socket.getOutputStream();
	        	os.write(request.getBytes());
	        	os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
}
