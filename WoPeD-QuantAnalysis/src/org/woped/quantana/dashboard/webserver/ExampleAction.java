package org.woped.quantana.dashboard.webserver;

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
			
			
		}else
		if(response.getContent().equals("")){
				
				TemplateEngine te = new TemplateEngine();
				
				response.addContent(te.getTemplateContent("header.html"));
				
				response.addContent(te.getTemplateContent("footer.html"));
				
				
		}

	}

}
