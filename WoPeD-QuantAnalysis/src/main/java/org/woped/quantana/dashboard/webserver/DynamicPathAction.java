package org.woped.quantana.dashboard.webserver;

/**
 *
 */
public class DynamicPathAction extends ThinServerAction{

    @Override
    public void doAction(Request request, Response response) {
        response.addContent("<b>Content of DynamicPathAction</b><br/>");
	response.addContent("Path: /dynpath/[var1]/user/[var2]/ <br/>");
	response.addContent("Value of var1: "+ request.getParameter("var1")+"<br/>");
	response.addContent("Value of var2: "+ request.getParameter("var2")+"<br/>");
    }
    @Override
    public String getActionpathListenPattern() {
        return "/dynpath/[var1]/user/[var2]/.*";
    }
    
}