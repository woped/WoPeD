package org.woped.quantana.dashboard.webserver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.dashboard.storage.StorageEngine;

public abstract class ThinServerAction {
	
	protected static StorageEngine storageengine = null;
	public static void setStorageEngine(StorageEngine se){
		if (storageengine == null){
			storageengine = se;
		}
	}
	
	public void doAction(Request request, Response response){};
	public String getActionpathListenPattern(){
		return "^/.*";
                
	}
        public boolean mapActionPath(Request request){
            String actionPath = getActionpathListenPattern();
            String regExpActionPath = actionPath;
            String requestPath = request.getPath();
            List<String> varNames = new ArrayList<String>();
            //List<String> varValue = new ArrayList<String>();
            
           
            // 1. Path:    /test/[var1]/test2/[var2]/
            // 2. Request: /test/value1/test2/value2/
            // 3. Aus dem Path var1 und var2 extrahieren
            // 4. Aus dem Request value1 und value2 extrahieren und den vars zuweisen
            
            Matcher matcher;
            matcher = Pattern.compile("(/\\[\\w+\\]/)").matcher(actionPath);
            
            // Parse Names of Variables from the ActionPath
            while(matcher.find()){
                //System.out.printf( "%s an Postion [%d,%d]%n",  
                //      matcher.group(), matcher.start(),  
                //      matcher.end() );
                varNames.add(actionPath.substring((matcher.start()+2), (matcher.end()-2)));
                
                String regExpGroup = matcher.group();
                regExpGroup = regExpGroup.replaceAll("\\[", "\\\\[");
                regExpGroup = regExpGroup.replaceAll("\\]", "\\\\]");
                //System.out.println(regExpGroup);
                regExpActionPath = regExpActionPath.replaceAll(regExpGroup, "/(\\\\w+)/");
                //System.out.println(regExpActionPath);
            }
            if(requestPath.matches(regExpActionPath)){
                // Parse Variable Values
                matcher = Pattern.compile(regExpActionPath).matcher(requestPath);
                if(matcher.matches()){
                    if((matcher.groupCount()) != varNames.size()){
                        return false;
                    }
                    for(int i=0; i<matcher.groupCount();i++){
                        request.setParameter(varNames.get(i), matcher.group(i+1));
                        LoggerManager.info(Constants.DASHBOARDWEBSRV_LOGGER, "Find vars:"+varNames.get(i)+"="+matcher.group(i+1));
                    }
                }
                //LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, regExpActionPath);
                LoggerManager.debug(Constants.DASHBOARDWEBSRV_LOGGER, requestPath);
                return true;
            }
            return false;
        }
        
}
