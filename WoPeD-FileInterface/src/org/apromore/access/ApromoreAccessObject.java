package org.apromore.access;

import java.net.ProxySelector;
import java.util.List;

import javax.activation.DataHandler;
import javax.swing.JOptionPane;
import javax.xml.ws.WebServiceException;

import org.apromore.manager.client.ManagerService;
import org.apromore.manager.client.ManagerServiceClient;
import org.apromore.model.EditSessionType;
import org.apromore.model.ProcessSummariesType;
import org.apromore.model.ProcessSummaryType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

public class ApromoreAccessObject {

	//managerService = ()...

	ApplicationContext appContext = new ClassPathXmlApplicationContext("managerClientContext.xml");
    HttpComponentsMessageSender httpCms = (HttpComponentsMessageSender) appContext.getBean("httpSender");
    
    Jaxb2Marshaller serviceMarshaller = (Jaxb2Marshaller) appContext.getBean("serviceMarshaller");
    
    SaajSoapMessageFactory ssmf = (SaajSoapMessageFactory) appContext.getBean("messageFactory");
    
    WebServiceTemplate temp = new WebServiceTemplate(ssmf);
    ManagerService managerService;
//	ManagerPortalService service;
//	ManagerPortalPortType serviceport;
	boolean isOnline = false;
	EditSessionType aproParams;
	public ApromoreAccessObject() {
//		if (ConfigurationManager.getConfiguration().getApromoreUseProxy())
//		ProxySelector.setDefault(new WoProxySelector(ConfigurationManager.getConfiguration().getApromoreProxyName(), ConfigurationManager.getConfiguration().getApromoreProxyPort()));
		setupService();
	}
	
	private void setupService() {
		Object[] options = { "OK" };
		
		try
		{
			temp.setMarshaller(serviceMarshaller);
		    temp.setUnmarshaller(serviceMarshaller);
		    temp.setMessageSender(httpCms);
		    temp.setDefaultUri(ConfigurationManager.getConfiguration().getApromoreServerURL() + ":" +
		    				   ConfigurationManager.getConfiguration().getApromoreServerPort() + "/" +
		    				   ConfigurationManager.getConfiguration().getApromoreManagerPath());
		    managerService = new ManagerServiceClient(temp);
//		service = new ManagerPortalService();
//		serviceport = service.getManagerPortal();
		isOnline = true;
		} catch (WebServiceException e)
		{
			JOptionPane.showOptionDialog(null, Messages.getString("Apromore.Export.UI.Error.AproNoConn"), Messages.getString("Apromore.Export.UI.Error.AproNoConnTitle"),

			        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,

			        null, options, options[0]);
		}
		
	}

	public List<ProcessSummaryType> getList() {

		/*komplette klasse auskommentieren, getList() Methode nur anpassen und versuchen ob das schon
		 * funktioniert bzw. reicht (2 Zeile sind einzuf�gen und oben eine Sache zu implementieren)
		 * 
		 * 
		 * ProcessSummaries
		 * 
		 * 
		 */
		
		ProcessSummariesType processSummaries = managerService
                .readProcessSummaries(null);
		return processSummaries.getProcessSummary(); //processes
	}

////	public DataHandler getPNML(ExportFormatInputMsgType request) {
////		request.setFormat("XPDL 2.1");
////		ExportFormatOutputMsgType a =  managerService.exportFormat(request);
////		return a.getNative();
//
//	}
//	
	public EditSessionType getParams() {
		return aproParams;

	}



//	public ImportProcessOutputMsgType export(DataHandler a, EditSessionType e) {
//		ImportProcessInputMsgType request = new ImportProcessInputMsgType();
//		request.setProcessDescription(a);
//		request.setAddFakeEvents(false);
//		request.setEditSession(e);
//		return serviceport.importProcess(request);
//	}
//	
	public boolean IsOnline()
	{
		return isOnline;
	}
}
