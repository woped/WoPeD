package org.woped.quantana.dashboard.webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Response {
	public static final int STATUS_OK = 200;
	public static final int STATUS_NOT_FOUND = 404;
	
        private String content = "";
	private byte[] binContent = new byte[0];
	private String contentType = "text/html";
	private int status = STATUS_OK;
        private HashMap<String, HttpCookie> cookies = new HashMap<String, HttpCookie>();
        
        // >> Clone
        public Response clone(){
            Response response = new Response(null);
            response.addContent(this.getContent());
            response.setBinContent(this.getBinContent());
            response.setContentType(this.getContentType());
            response.setStatus(this.getStatus());
            for(String cookieName:getAllCookieNames()){
                response.setCookie(getCookie(cookieName));
            }
            return response;
        }
        // << Clone
        
	public Response(Socket socket){
		
	}
	public void addContent(String cont){
		content += cont;
	}
	public String getContent(){
		return content;
	}
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public byte[] getBinContent() {
		return binContent;
	}
	public void setBinContent(byte[] binContent) {
		this.binContent = binContent;
	}
	public String getResponse(){
		// Header Data
		String response = getHeader();
		// Content Data
		if(getStatus() == STATUS_OK){
			if(binContent.length > 0){
				
			}else{
				response += getContent();
			}
		}
		
		return response;
	}
	
	private String getHeader(){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		Calendar cal = Calendar.getInstance();
		
		String response = "HTTP/1.1 "+getStatus()+" OK";
		response += "\r\n";
		response += "Date: " + sdf.format(cal.getTime()); //"Fri, 11 Mar 2013 17:00:00 GMT";
		response += "\r\n";
		response += "Last-Modified: "+ sdf.format(cal.getTime());//"Fri, 11 Mar 2013 17:00:00 GMT";
		response += "\r\n";
		response += "Content-Type: "+ contentType +"; charset=utf-8";
		response += "\r\n";
		for(String cookieName:getAllCookieNames()){
                    response += createSetCookieString(getCookie(cookieName));
                    response += "\r\n";
                }
		response += "\r\n";
		return response;
	}
	public byte[] getBinResponse(){
		byte[] header = getHeader().getBytes();
		byte[] content = getBinContent();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write( header );
			outputStream.write( content );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray( );
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
        
        private static String createSetCookieString(HttpCookie cookie){
            //Set-Cookie: varTestHero=7472417; Expires=Fri, 06-Dec-2013 19:44:26 GMT; Path=/
            //Set-Cookie: ns_sample=3; Domain=.domain.de; Expires=Sun, 07-Jun-2015 19:44:26 GMT; Path=/
            String cookieString = "Set-Cookie: "+cookie.getName()+"="+cookie.getValue()+"; ";
            return cookieString;
        }
	
	
}
