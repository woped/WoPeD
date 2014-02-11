package org.woped.apromore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.ProxySelector;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import org.apromore.manager.client.ManagerService;
import org.apromore.manager.client.ManagerServiceClient;
import org.apromore.model.EditSessionType;
import org.apromore.model.ExportFormatResultType;
import org.apromore.model.ImportProcessResultType;
import org.apromore.model.ProcessSummariesType;
import org.apromore.model.ProcessSummaryType;
import org.apromore.plugin.property.RequestParameterType;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

public class ApromoreAccess {
	
	private static ApromoreAccess instance = null;
	
	public static ApromoreAccess getInstance() {
		return instance;
	}

	private HttpComponentsMessageSender httpCms;
	private Jaxb2Marshaller serviceMarshaller;
	private SaajSoapMessageFactory soapMsgFactory;
	private WebServiceTemplate wsTemp; 
	private ManagerService managerService;
	private EditSessionType aproParams;
	private boolean online = false;
	
	public ApromoreAccess() {
		if (ConfigurationManager.getConfiguration().getApromoreUseProxy())
			ProxySelector.setDefault(new WoProxySelector(ConfigurationManager.getConfiguration().getApromoreProxyName(), ConfigurationManager.getConfiguration().getApromoreProxyPort()));
		try {
			setupService();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setupService() throws SOAPException {
		String[] options = { "OK" };
		httpCms = new HttpComponentsMessageSender();
		httpCms.setReadTimeout(40000);
		
		serviceMarshaller = new Jaxb2Marshaller();
		serviceMarshaller.setContextPath("org.apromore.model");
		soapMsgFactory = new SaajSoapMessageFactory(MessageFactory.newInstance());
		soapMsgFactory.setSoapVersion(org.springframework.ws.soap.SoapVersion.SOAP_11);
		
		wsTemp = new WebServiceTemplate(soapMsgFactory);
		try
		{
			
			httpCms.setConnectionTimeout(10000);
			wsTemp.setMarshaller(serviceMarshaller);
		    wsTemp.setUnmarshaller(serviceMarshaller);
		    wsTemp.setMessageSender(httpCms);
		    wsTemp.setDefaultUri(ConfigurationManager.getConfiguration().getApromoreServerURL() + ":" +
		    				   ConfigurationManager.getConfiguration().getApromoreServerPort() + "/" +
		    				   ConfigurationManager.getConfiguration().getApromoreManagerPath());
		    managerService = new ManagerServiceClient(wsTemp);

		    online = true;
		} 
		catch (Exception e)
		{
			JOptionPane.showOptionDialog(null, Messages.getString("Apromore.Export.UI.Error.AproNoConn"), 
					Messages.getString("Apromore.Export.UI.Error.AproNoConnTitle"),
			        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
			        null, options, options[0]);
		}
		
	}

	public List<ProcessSummaryType> getList() {
		
		ProcessSummariesType processSummaries = managerService.readProcessSummaries(null);
		return processSummaries.getProcessSummary(); 
	}

	public ExportFormatResultType importProcess(int id) {
		
		ProcessSummariesType processSummaries = managerService.readProcessSummaries(null);
		ProcessSummaryType p = processSummaries.getProcessSummary().get(id);
		ExportFormatResultType exf = null;
		final Set<RequestParameterType<?>> noCanoniserParameters = Collections.emptySet();
		
		try {
			exf = managerService.exportFormat(p.getId(), p.getName(), "MAIN", p.getLastVersion(), "PNML 1.3.2", null, false, p.getOwner(), noCanoniserParameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return exf;
	}
	
	public EditSessionType getParams() {
		return aproParams;
	}
	

	
	public ImportProcessResultType export(String userName, String domain, String processName, 
			String version, boolean makePublic, FileInputStream fis) {
        final Set<RequestParameterType<?>> noCanoniserParameters = Collections.emptySet();
        try {
        	
        	SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("yyyy'/'MM'/'dd");
			
        	return managerService.importProcess(userName, 0, "PNML 1.3.2",
        		processName, version, fis, "", "", sdf.format(new Date()), "", makePublic,
        		noCanoniserParameters);
					 
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return null;
	}
	
	public boolean IsOnline()
	{
		return online;
	}
}

