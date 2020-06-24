package org.woped.quantana.dashboard.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Request implements Cloneable{
	public static String REQUEST_TYPE_GET     = "GET";
	public static String REQUEST_TYPE_POST    = "POST";
    public static String REQUEST_TYPE_PUT     = "PUT";
    public static String REQUEST_TYPE_DELETE  = "DELETE";

	private String requestType = "";
	private String path = "";
	private String[] pathElements = new String[0];
	private int contentLength = 0;
	private HashMap<String, String> requestParameter = new HashMap<String, String>();
        private HashMap<String, HttpCookie> cookies = new HashMap<String, HttpCookie>();
        private SessionData sessionData = null;
        private RequestProcessState requestProcessState = new RequestProcessState();
        
        // >> Clone
        public Request clone(){
            Request request = new Request(null);
            request.setRequestType(getRequestType());
            request.setPath(this.getPath());
            request.setPathElements(this.getPathElements());
            for(String paramname: getAllParameter()){
                request.setParameter(paramname, getParameter(paramname));
            }
            for(String cookieName:getAllCookieNames()){
                request.setCookie(cookieName, getCookie(cookieName));
            }
            request.setSessionData(this.getSessionData());
            request.setRequestProcessState(this.getRequestProcessState());
            return request;
        }
        // << Clone
        
	public Request(Socket socket) {
            if(socket == null){
                return;
            }
		try {
			InputStreamReader isr = new InputStreamReader(
					socket.getInputStream());
			BufferedReader brInput = new BufferedReader(isr);
			String line = brInput.readLine();
			while (line != null && !line.equals("")) {
				parseLine(line); 
				line = brInput.readLine();
			}
			// Parse Content
			StringBuilder requestContent = new StringBuilder();
			for (int i = 0; i < contentLength; i++) {
				requestContent.append((char) brInput.read());
			}
			parseRequestParameterFromString(requestContent.toString());

			// line=brInput.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseLine(String line) {
		String lineLower = line.toLowerCase();
		if (lineLower.startsWith("get ")) {
			// GET /action/2 HTTP/1.1
			requestType = REQUEST_TYPE_GET;
			parseURLPath(line);
		} else if (lineLower.startsWith("post ")) {
			// GET /action/2 HTTP/1.1
			requestType = REQUEST_TYPE_POST;
			parseURLPath(line);
		} else if (lineLower.startsWith("put ")) {
			// PUT /action/2 HTTP/1.1
			requestType = REQUEST_TYPE_PUT;
			parseURLPath(line);
		} else if (lineLower.startsWith("delete ")) {
			// DELETE /action/2 HTTP/1.1
			requestType = REQUEST_TYPE_DELETE;
			parseURLPath(line);
		} else if (lineLower.startsWith("content-length: ")) {
			contentLength = Integer.parseInt((line
					.substring(("content-length:").length())).trim());

		} else if (lineLower.startsWith("cookie: ")){
                    String cookieline = line;
                    cookieline = line.substring(8);
                    // >> Parse cookies
                    try{
                        List<HttpCookie> parsedCookies = HttpCookie.parse(cookieline);
                        for(HttpCookie cookie:parsedCookies){
                            cookies.put(cookie.getName(), cookie);
                        }
                    }catch(IllegalArgumentException e){
                    
                    }
                    // << Parse cookies
                }
	}

	private void parseURLPath(String line) {
		String[] lineElements = line.split(" ");
		if (lineElements.length < 2) {
			path = "/";
		} else {
			path = lineElements[1];
			parseRequestParameterFromPath(path);
		}
		parsePathElements(path);
	}

	private void parseRequestParameterFromPath(String path) {
		// /this/is/the/path?name1=value1&name2=value2....
		String tmpPath = path;
		int position = tmpPath.indexOf("?");
		if (position > 0) {
			tmpPath = tmpPath.substring(position + 1);
			parseRequestParameterFromString(tmpPath);
		}
	}

	private void parseRequestParameterFromString(String string) {
		// /this/is/the/path?name1=value1&name2=value2....
		while (string.length() > 0) {
			int indexEqual = string.indexOf("=");
			int indexNext = string.indexOf("&");
			if (indexEqual == -1) {
				break;
			}
			String paramName = string.substring(0, indexEqual);
			String paramValue = "";
			if (indexNext == -1) {
				paramValue = string.substring(indexEqual + 1);
				string = "";
			} else {
				paramValue = string.substring(indexEqual + 1, indexNext);
				string = string.substring(indexNext + 1);
			}
			try {
				paramName = URLDecoder.decode(paramName, "UTF-8");
				paramValue = URLDecoder.decode(paramValue, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			this.setParameter(paramName, paramValue);
		}
	}
	private void parsePathElements(String string){
		// /this/is/the/path?name1=value1&name2=value2....
		string = string.toLowerCase();
		List<String> pathList = new ArrayList<String>();
		int indexQuestMark = string.indexOf("?");
		if(indexQuestMark != -1){
			string = string.substring(0, indexQuestMark);
		}
		while (string.startsWith("/")){
			string = string.substring(1);
		}
		while (string.length() > 0) {
			int nextSep = string.indexOf("/");
			if(nextSep == -1){
				pathList.add(string);
				break;
			}
			pathList.add(string.substring(0, nextSep));
			string = string.substring(nextSep+1);
		}
		pathElements = new String[pathList.size()];
		for(int i=0; i<pathList.size();i++){
			pathElements[i] = pathList.get(i);
		}
	}

	// >> Request Parameter >>
	public String getParameter(String name) {
		return requestParameter.get(name);
	}

	public void setParameter(String name, String value) {
		requestParameter.put(name, value);
	}
        public List<String> getAllParameter(){
            return new ArrayList<String>(requestParameter.keySet());
        }
	public void removeParameter(String name) {
		requestParameter.remove(name);
	}

	// << Request Parameter >>

	// >> Getter & Setter >>
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getPath() {
		return path;
	}
	public String[] getPathElements() {
		return pathElements;
	}
        public void setPathElements(String[] pathElements){
            this.pathElements = pathElements;
        }

	public void setPath(String path) {
		this.path = path;
		parsePathElements(this.path);
	}
        public HttpCookie getCookie(String name){
            return cookies.get(name);
        }
        public List<String> getAllCookieNames(){
            return new ArrayList<String>(cookies.keySet());
        }
        public void setCookie(HttpCookie cookie){
            setCookie(cookie.getName(),cookie);
        }
        public void setCookie(String name, HttpCookie cookie){
            cookies.put(name, cookie);
        }
	// << Getter & Setter <<
        
        // >> SessionData 
        public void setSessionData(SessionData sessionData){
            if(sessionData != null)
                this.sessionData = sessionData;
        }
        public SessionData getSessionData(){
            return this.sessionData;
        }
        // << SessionData
        // >> RequestProcessState
        public RequestProcessState getRequestProcessState() {
            return requestProcessState;
        }

        public void setRequestProcessState(RequestProcessState requestProcessState) {
            this.requestProcessState = requestProcessState;
        }
        
        // << RequestProcessState
}
