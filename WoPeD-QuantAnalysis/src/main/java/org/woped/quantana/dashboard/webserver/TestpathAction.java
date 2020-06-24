package org.woped.quantana.dashboard.webserver;

public class TestpathAction extends ThinServerAction {

    @Override
	public void doAction(Request request, Response response) {
		response.addContent("<b>Content of TestpathAction</b><br/>");
		response.addContent("<a href='/'>Go to Root path</a>");

	}

	@Override
	public String getActionpathListenPattern() {
		
		return "^/test/.*";
	}
	

}
