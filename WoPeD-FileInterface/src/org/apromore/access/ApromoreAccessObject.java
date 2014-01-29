package org.apromore.access;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.ProxySelector;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apromore.manager.client.ManagerService;
import org.apromore.manager.client.ManagerServiceClient;
import org.apromore.model.EditSessionType;
import org.apromore.model.ExportFormatResultType;
import org.apromore.model.ImportProcessResultType;
import org.apromore.model.ProcessSummariesType;
import org.apromore.model.ProcessSummaryType;
import org.apromore.plugin.property.RequestParameterType;
// import org.apromore.plugin.property.RequestParameterType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;
import org.woped.file.gui.*;
public class ApromoreAccessObject {

	private ApplicationContext appContext = new AnnotationConfigApplicationContext(ApromoreConfig.class);   
	private HttpComponentsMessageSender httpCms = (HttpComponentsMessageSender) appContext.getBean(HttpComponentsMessageSender.class);
	private Jaxb2Marshaller serviceMarshaller = (Jaxb2Marshaller) appContext.getBean(Jaxb2Marshaller.class);
	private SaajSoapMessageFactory ssmf = (SaajSoapMessageFactory) appContext.getBean(SaajSoapMessageFactory.class);
	private WebServiceTemplate temp = new WebServiceTemplate(ssmf); 
	private ManagerService managerService;
	private boolean isOnline = false;
	private EditSessionType aproParams;
	
	public ApromoreAccessObject() {
		if (ConfigurationManager.getConfiguration().getApromoreUseProxy())
			ProxySelector.setDefault(new WoProxySelector(ConfigurationManager.getConfiguration().getApromoreProxyName(), ConfigurationManager.getConfiguration().getApromoreProxyPort()));
		setupService();
	}
	
	private void setupService() {
		String[] options = { "OK" };
		
		try
		{
			httpCms.setConnectionTimeout(10000);
			temp.setMarshaller(serviceMarshaller);
		    temp.setUnmarshaller(serviceMarshaller);
		    temp.setMessageSender(httpCms);
		    temp.setDefaultUri(ConfigurationManager.getConfiguration().getApromoreServerURL() + ":" +
		    				   ConfigurationManager.getConfiguration().getApromoreServerPort() + "/" +
		    				   ConfigurationManager.getConfiguration().getApromoreManagerPath());
		    managerService = new ManagerServiceClient(temp);

		    isOnline = true;
		} 
		catch (Exception e)
		{
			JOptionPane.showOptionDialog(null, Messages.getString("Apromore.Export.UI.Error.AproNoConn"), Messages.getString("Apromore.Export.UI.Error.AproNoConnTitle"),

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
	

	
	public ImportProcessResultType export(FileInputStream fis) {
        final Set<RequestParameterType<?>> noCanoniserParameters = Collections.emptySet();
        try {
        	
        	String userName = ExportFrame.getUserName();
        	String domain = ExportFrame.getDomain();
        	String process = ExportFrame.getProcess();
        	String version = ExportFrame.getVersion();
        	//+public
        	
        	SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("yyyy'/'MM'/'dd");
			
        	return managerService.importProcess(userName, 0, "PNML 1.3.2",
        		process, version, fis, "", "", sdf.format(new Date()), "", true,
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
		return isOnline;
	}
}
