package org.woped.quantana.dashboard.webserver;

import com.google.gson.Gson;

import org.woped.quantana.dashboard.webserver.ThinServerAction;
import org.woped.quantana.dashboard.webserver.Request;
import org.woped.quantana.dashboard.webserver.Response;
import org.woped.quantana.dashboard.storage.AjaxAction;
import org.woped.quantana.dashboard.storage.AjaxActionDeleteTables;
import org.woped.quantana.dashboard.storage.TableInfo;

public class PostRequestAction extends ThinServerAction {

	@Override
	public void doAction(Request request, Response response) {
		
		//List<String> str  = request.getAllParameter();
		
		String strAction = request.getParameter("json");
		
		Gson gson = new Gson();
		AjaxAction aa = gson.fromJson(strAction,  AjaxAction.class);
		
		if(aa.getAction().equals("deleteArchives")){
			AjaxActionDeleteTables ad = gson.fromJson(strAction,  AjaxActionDeleteTables.class);
			for(AjaxActionDeleteTables.Parameter p : ad.getParameter()){
				String strID = p.ID; 
				ThinServerAction.storageengine.DeleteTable(strID);
				
			}
			
			response.setContentType("application/json");
			
			TableInfo[] ti = ThinServerAction.storageengine.GetTables();
			
			String strRet = gson.toJson(ti);
			
			response.addContent(strRet);
		}
	}
	@Override
	public String getActionpathListenPattern() {
		
		return "^/postrequest/.*";
	}

}
