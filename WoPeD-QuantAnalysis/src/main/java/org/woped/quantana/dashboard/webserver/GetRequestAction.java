package org.woped.quantana.dashboard.webserver;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.dashboard.storage.SaveConfig;
import org.woped.quantana.dashboard.storage.SimulationRessourceAllocData;
import org.woped.quantana.dashboard.storage.SimulationStorageEntry;
import org.woped.quantana.dashboard.storage.TableInfo;
import org.woped.quantana.sim.SimParameters;

import com.google.gson.Gson;

/**
 * 
 * @author chrisn
 *
 */
public class GetRequestAction extends ThinServerAction {

	
	TemplateEngine te = new TemplateEngine();
	
	/**
	 * Performs the activities that are requested by the client 
	 * the following actions are possible:
	 *
	 *	listarchivesFull	-	responses the complete html-site with list of archives (type:html)
	 *  showdashboard		-	responses the working-area of the simulation values (type: html)
	 *  showdashboardFull	- 	responses the complete html-site with the simulation-values (type: html)
	 *  getDashboardValues	-	responses the simulation data (type:json)
	 *  						the following specification can be made:
	 *  								tick	: get the value or all values less or equal to tick 
	 *  								single	: get a period (from start to the value of 'tick') or a specific set of values (that is according to tick)
	 *  								table	: request data of a special table; when empty: the current (last) table of simulation will be selected automatically
	 * 	getArchiveList		- 	responses a objects that contains the information of stored data-tables (type:json)
	 *	getSimulationStats	-	responses the statistic-data of a simulation (type:json)
	 *									table	:  request data of a special table; when empty: the current (last) table of simulation will be selected automatically (type:json)
	 *
	 *	getSimulationResAlloc	-responeses the allocation-table of a specific ressource, or server (necessary for gantt-diagramm (type:json)
	 *
	 *					
	 * @param  request  request from the client
	 * @param  response  response that will be delivered to the client
	 * 
	 * @return     
	 * @see         Image
	 */
	@Override
	public void doAction(Request request, Response response) {

		
		//LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"process request");
		
		String strAction = request.getParameter("action");
		
		if (strAction == null){
			return;
		}
			
		
		if(strAction.equals("listarchivesFull")){
			
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"listarchivesFull");
	
			response.addContent(te.getTemplateContent("header.html"));
			
			listArchives(response);
			
			response.addContent(te.getTemplateContent("footer.html"));
		
		}
		else  if(strAction.equals("showdashboard")){
		
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"showdashboard");
			
			showdashboard(response);
		
		}
		else  if(strAction.equals("showdashboardFull")){
			
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"showdashboardFull");
			
			//content-language setzen?
			//http://nimbupani.com/declaring-languages-in-html-5.html
			
			String bAutoUpdate = request.getParameter("AutoUpdate"); 
			
			response.addContent(te.getTemplateContent("header.html"));
			
			if(bAutoUpdate!=null && bAutoUpdate.equals("true")){
				showdashboardWithAutoUpdateOption(response, true);
			}else{
				showdashboard(response);
			}
			
			
			response.addContent(te.getTemplateContent("footer.html"));
			
		
		}
		else if(strAction.equals("getDashboardValues")){
		
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"getDashboardValues");
			
			response.setContentType("application/json");
			
			String tick = request.getParameter("tick");
			String bSingleValue = request.getParameter("single");
			String tablename = request.getParameter("table");
			String strBounding = request.getParameter("bounding");
			
			long Tick= 0;
			Boolean single = false;
			Boolean bBounding = false;
			
			if( tick!= null && !tick.equals("undefined")){
				Tick = Long.parseLong(tick);
			}
			else{
				Tick = -1;				
			}
			
			if( strBounding!= null && !strBounding.equals("undefined")){
				bBounding = Boolean.parseBoolean(strBounding);
			}
			else{
				bBounding = false;				
			}
			
			if(bSingleValue !=null ){
				single = Boolean.parseBoolean(bSingleValue);
			}
			
			SimulationStorageEntry[] sd =  ThinServerAction.storageengine.GetSimulationData(tablename, Tick, single, bBounding);
			
			Gson gson = new Gson();
			String ret = gson.toJson(sd);
			response.addContent(ret);
			
		}
		else if(strAction.equals("getArchiveList")){
			
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"getArchiveList");
			
			
			response.setContentType("application/json");
			
			TableInfo[] ti = ThinServerAction.storageengine.GetTables();
			Gson gson = new Gson();
			
			String strRet = gson.toJson(ti);
			
			response.addContent(strRet);
			
		} 
		else if(strAction.equals("getSimulationStats")){
			
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"getSimulationStats");
			
			response.setContentType("application/json");
			
			String tablename = request.getParameter("table");
			
			SimParameters simparams =  ThinServerAction.storageengine.GetSimulationStatisticData(tablename);
			
			Gson gson = new Gson();
			String ret = gson.toJson(simparams);
			response.addContent(ret);
			
		}
		else if(strAction.equals("getSimulationResAlloc")){
			
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"getSimulationResAlloc");

			response.setContentType("application/json");

			String tablename = request.getParameter("table");
			String ressource = request.getParameter("ressource");
			String server = request.getParameter("server");
						
			SimulationRessourceAllocData[] simparams =  ThinServerAction.storageengine.GetSimResAlloc(tablename, ressource, server);
			
			Gson gson = new Gson();
			String ret = gson.toJson(simparams);
			response.addContent(ret);
			
		}
		else if(strAction.equals("getImage")){
			
			LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER,"getSimulationResAlloc");

			//response.setContentType("application/json");

			String tablename = request.getParameter("table");
			
			byte[] imageInByte = ThinServerAction.storageengine.GetImage(tablename);
			
			response.setContentType("image/png");
			
			
			response.setBinContent(imageInByte);
		}
		else if(strAction.equals("getUIConfig")){
			//has to be changed to SaveConfig Object
			SaveConfig c = storageengine.GetUIConfig("SIM_ATTRIBUTES");
		}
	}
	
	
	/**
	 * delivers the search pattern that is necessary to identify a getrequest
	 *
	 * @param  request  content to the client
	 * @return String constant with search-pattern of the getrequest
	 */
	@Override
	public String getActionpathListenPattern() {
		
		return "^/getrequest/.*";
	}
	
	/**
	 * returns the HTML-content of the dashboard with the simulated data
	 * @param  request  content to the client
	 * 
	 * @return void
	 */
	private void showdashboard(Response response) {

		//String strret = te.getTemplateContent("dashboard.html");
		//response.addContent(strret);
		showdashboardWithAutoUpdateOption(response,false);
	}
	
	private void showdashboardWithAutoUpdateOption(Response response, Boolean bAutoUpdate) {

		String strret = te.getTemplateContent("dashboard.html");
		String patternString = "%%.*%%";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(strret);
		
		while(matcher.find()) {
			
			String strFound = matcher.group();
			if(strFound.equals("%%AUTOUPDATE%%")){	
				if (true == bAutoUpdate){
					strret = strret.replace("%%AUTOUPDATE%%", "checked=\"checked\"");
				}else{
					strret = strret.replace("%%AUTOUPDATE%%", "");
				}
			}
			 		    	    
		}
		response.addContent(strret);
		
	}
		
	
	/**
	 * Returns the HTML-content that shows all available simulation-runs
	 *
	 * @param  request  content to the client
	 * @return void
	 */
	private void listArchives(Response response) {
		
		response.addContent(te.getTemplateContent("listarchives.html"));
		
	}
	
	
	
	

}
