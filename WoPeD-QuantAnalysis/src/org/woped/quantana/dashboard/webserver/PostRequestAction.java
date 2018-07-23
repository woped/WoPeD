package org.woped.quantana.dashboard.webserver;

import com.google.gson.Gson;

import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.dashboard.webserver.ThinServerAction;
import org.woped.quantana.dashboard.webserver.Request;
import org.woped.quantana.dashboard.webserver.Response;
import org.woped.quantana.dashboard.storage.AjaxAction;
import org.woped.quantana.dashboard.storage.AjaxActionDeleteTables;
import org.woped.quantana.dashboard.storage.AjaxActionDeleteTables.Parameter;
import org.woped.quantana.dashboard.storage.Constants;
import org.woped.quantana.dashboard.storage.SaveConfig;
import org.woped.quantana.dashboard.storage.TableInfo;
import org.woped.quantana.dashboard.storage.StorageEngine;
//import org.woped.quantana.gui.EmbeddedBrowserView;
import org.woped.quantana.gui.JFXUsageNotSupported;

import java.io.*;

public class PostRequestAction extends ThinServerAction {

	@Override
	public void doAction(Request request, Response response) {
		
		//List<String> str  = request.getAllParameter();
		
		String strAction = request.getParameter("json");
		
		Gson gson = new Gson();
		AjaxAction aa = gson.fromJson(strAction,  AjaxAction.class);
		
		// is it a AJAX-Post with an JSON-Information?
		
		if(aa != null){
			
			
			
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
			else if(aa.getAction().equals("createEmbeddedBrowserView")) {
				//try{
				//	EmbeddedBrowserView browser = new EmbeddedBrowserView(2711,false);
				//	browser.setVisible(true);
				//}
				//catch(JFXUsageNotSupported e){
					ClientStarter cs = new ClientStarter();
					cs.startClient(false);
				//}

			}
			else if(aa.getAction().equals("saveConfig")) {
				strAction = strAction;
				
				
				SaveConfig sc = gson.fromJson(strAction,  SaveConfig.class);
				
				for(SaveConfig.Parameter p : sc.getParameter()){
					int row = p.row; 
					int col = p.col; 
					int size_x = p.size_x; 
					int size_y = p.size_y; 
					
				}
				storageengine.InsertUIconfig("SIM_ATTRIBUTES", sc);
				
//				AjaxActionDeleteTables ad = gson.fromJson(strAction,  AjaxActionDeleteTables.class);
//				for(AjaxActionDeleteTables.Parameter p : ad.getParameter()){
//					String strID = p.ID; 
//					
//					
//				}
////
//				String[] par = null;
				
//				String[] par = null;
//				Parameter[] p = ad.getParameter();
//				for (int i = 1; i <=p.length; i++) {
//					par[i] = p[i].ID;
//				}
//				for(AjaxActionDeleteTables.Parameter p : ad.getParameter()){
//					String strID = p.ID; 

//				}
//				try {
//					 
//					String content = strAction;
//		 
//					File file = new File("saveConfig.txt");
//		 
//					// if file doesnt exists, then create it
//					if (!file.exists()) {
//						file.createNewFile();
//					}
//		 
//					FileWriter fw = new FileWriter(file.getAbsoluteFile());
//					BufferedWriter bw = new BufferedWriter(fw);
//					bw.write(content);
//					bw.close();
//		 
//					System.out.println("Done");
//		 
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		
//					
				
				
			
			}else{
				
				LoggerManager.info(Constants.DASHBOARDSTORE_LOGGER, "PostRequestAction: unknown JSON-POST");
			}
		}else{
			
			if(request.getPath().contains("editdescription")){
				
			//zuerst Anzahl Elemente analysieren, danach mithilfe von Schleife dynamisch zuordnen
				String numberOfSimulationsString  = request.getParameter("savedSimulation");

				int numberOfSimulations =  Integer.parseInt(numberOfSimulationsString);		
				for(int i=0;i<numberOfSimulations;i++){

				
				String strTable = request.getParameter("tablename_"+i);
				
				String strDescription = request.getParameter("description_"+i);
				
				
				LoggerManager.info(Constants.DASHBOARDSTORE_LOGGER, "PostRequestAction: new description arrived");
				
				ThinServerAction.storageengine.InsertDescription(strTable, strDescription);
				}
				
				
				TemplateEngine te = new TemplateEngine();
				
				response.addContent(te.getTemplateContent("header.html"));
				
				response.addContent(te.getTemplateContent("listarchives.html"));
				
				response.addContent(te.getTemplateContent("footer.html"));
				// save into table
			}
		}
	}
	@Override
	public String getActionpathListenPattern() {
		
		return "^/postrequest/.*";
	}

}
