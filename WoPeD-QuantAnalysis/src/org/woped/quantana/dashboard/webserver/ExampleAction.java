package org.woped.quantana.dashboard.webserver;

import java.io.IOException;

import org.woped.quantana.dashboard.webserver.ThinServerAction;
import org.woped.quantana.dashboard.webserver.TemplateEngine;
import org.woped.quantana.dashboard.webserver.Request;
import org.woped.quantana.dashboard.webserver.Response;



public class ExampleAction extends ThinServerAction {

	@Override
	public void doAction(Request request, Response response) {
				
		String strPath = request.getPath();
		
		if(strPath.endsWith(".css")){
			TemplateEngine te = new TemplateEngine();
			response.setContentType("text/css");
			response.addContent(te.getTemplateContent(strPath));
			
			
		}else 	if(strPath.endsWith(".js")){
				TemplateEngine te = new TemplateEngine();
				response.setContentType("application/javascript");
				response.addContent(te.getTemplateContent(strPath));
				
				
		}else 	if(strPath.endsWith(".html")){
			TemplateEngine te = new TemplateEngine();
			response.setContentType("text/html");
			response.addContent(te.getTemplateContent(strPath));
			
			
		}else if(strPath.endsWith(".png") || strPath.endsWith(".gif")){
			
				String strFile = "";
			
				TemplateEngine te = new TemplateEngine();
				try {
					String current = new java.io.File( "." ).getCanonicalPath();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				strFile = strPath.replace("/GUI/","");
				
				if (strPath.endsWith(".png")){
					response.setContentType("image/png");
				}
				else if (strPath.endsWith(".gif")){
					response.setContentType("image/gif");
				}
				
				byte[] _byte = te.getImageContent(strFile);
				
				response.setBinContent(_byte);
				
				
		 }
		else if(strPath.endsWith(".svg")){
		
		String strFile = "";
	
		TemplateEngine te = new TemplateEngine();
		try {
			String current = new java.io.File( "." ).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		strFile = strPath.replace("/SVG/","");
		

		response.setContentType("image/svg+xml");
		byte[] _byte = te.getImageSVGContent(strFile);
		
		response.setBinContent(_byte);
		
		
 	}

		else if(response.getContent().equals("")){
				
				TemplateEngine te = new TemplateEngine();
				
				response.addContent(te.getTemplateContent("header.html"));
				
				response.addContent(te.getTemplateContent("footer.html"));
				
				
		}

	}

}
