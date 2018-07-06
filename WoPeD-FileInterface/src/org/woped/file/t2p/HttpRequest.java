package org.woped.file.t2p;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {	
	private DataOutputStream out;
	private HttpURLConnection con;
	private BufferedReader in;
	
	private HttpResponse response;
	
	public HttpRequest(String url, String body) {
		int code = -1;
		String responseBody = "";
		
		try {
			URL urlObj = new URL(url);
			con = (HttpURLConnection) urlObj.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setDoOutput(true);
			
			out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(body);
			out.flush();
			out.close();
			
			code = con.getResponseCode();
			
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();
			responseBody = response.toString();
			con.disconnect();
		} catch(Exception e) {
			killConnections();
		} finally {
			killConnections();
		}
		this.response = new HttpResponse(code, responseBody);
	}
	
	public HttpResponse getResponse() {
		return response;
	}
	
	private void killConnections() {
		if (in != null) try {in.close();} catch (Exception e) {}
		if (out != null) try {out.close();} catch (Exception e) {}
		if (con != null) con.disconnect();
		
		con = null;
		in = null;
		out = null;
	}
	
	public void cancel() {
		killConnections();
	}
}

class HttpResponse {
	int responseCode;
	String body;
	
	public HttpResponse(int code, String body) {
		this.responseCode = code;
		this.body = body;
	}
}
