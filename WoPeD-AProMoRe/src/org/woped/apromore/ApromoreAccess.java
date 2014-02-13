package org.woped.apromore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import org.apromore.model.VersionSummaryType;
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
	private Jaxb2Marshaller 			serviceMarshaller;
	private SaajSoapMessageFactory 		soapMsgFactory;
	private WebServiceTemplate 			wsTemp; 
	private ManagerService 				managerService;
	private EditSessionType 			aproParams;
		
	public ApromoreAccess() {
		if (ConfigurationManager.getConfiguration().getApromoreUseProxy())
			ProxySelector.setDefault(new WoProxySelector(ConfigurationManager.getConfiguration().getApromoreProxyName(), ConfigurationManager.getConfiguration().getApromoreProxyPort()));
	}
	
	public void connect() throws SOAPException {
		httpCms = new HttpComponentsMessageSender();
		httpCms.setReadTimeout(40000);
		httpCms.setConnectionTimeout(40000);
		
		serviceMarshaller = new Jaxb2Marshaller();
		serviceMarshaller.setContextPath("org.apromore.model");
		soapMsgFactory = new SaajSoapMessageFactory(MessageFactory.newInstance());
		soapMsgFactory.setSoapVersion(org.springframework.ws.soap.SoapVersion.SOAP_11);
		
		wsTemp = new WebServiceTemplate(soapMsgFactory);
		wsTemp.setMarshaller(serviceMarshaller);
		wsTemp.setUnmarshaller(serviceMarshaller);
		wsTemp.setMessageSender(httpCms);
		wsTemp.setDefaultUri(ConfigurationManager.getConfiguration().getApromoreServerURL() + ":" +
		    				   ConfigurationManager.getConfiguration().getApromoreServerPort() + "/" +
		    				   ConfigurationManager.getConfiguration().getApromoreManagerPath());
		managerService = new ManagerServiceClient(wsTemp);
	}

	public String[][] getProcessList() throws Exception {
		
		ProcessSummariesType processSummaries = managerService.readProcessSummaries(null);
		List<ProcessSummaryType> list = processSummaries.getProcessSummary(); 
		String[][] s = new String[list.size()][5];

		for (int i = 0; i < list.size(); i++) {
			s[i][0] = "" + list.get(i).getName();
			s[i][1] = "" + list.get(i).getId();
			s[i][2] = "" + list.get(i).getOwner();
			s[i][3] = "" + list.get(i).getOriginalNativeType();
			List<VersionSummaryType> b = list.get(i).getVersionSummaries();
			s[i][4] = "";
			for (int z = 0; z < b.size() - 1; z++) {
				s[i][4] = s[i][4] + b.get(z).getName() + "; ";
			}
			s[i][4] = s[i][4] + b.get(b.size() - 1).getName();
		}
		return s;
	}
	
	public String[][] filterProcessList(String name, int id, String owner, String type) {

		ProcessSummariesType processSummaries = managerService.readProcessSummaries(null);
		List<ProcessSummaryType> list = processSummaries.getProcessSummary(); 

		int j = list.size() - 1;

		if (!((name.equalsIgnoreCase("")) && (id == 0)
				&& (owner.equalsIgnoreCase("")) && (type.equalsIgnoreCase("")))) {
			j = 0;
			for (int i = 0; i < list.size() - 1; i++) {
				if ((list.get(i).getName().toLowerCase()
						.contains(name.toLowerCase()) || name
						.equalsIgnoreCase(""))
						&& (list.get(i).getOriginalNativeType().toLowerCase()
								.contains(type.toLowerCase()) || type
								.equalsIgnoreCase(""))
						&& (list.get(i).getOwner().toLowerCase()
								.contains(owner.toLowerCase()) || owner
								.equalsIgnoreCase(""))
						&& ((list.get(i).getId() == id) || (id == 0)))
					j++;
			}
		}

		String[][] s = new String[j][5];

		int k = 0;

		for (int i = 0; i < list.size() - 1; i++) {
			if (!((name.equalsIgnoreCase("")) && (id == 0)
					&& (owner.equalsIgnoreCase("")) && (type
						.equalsIgnoreCase("")))) {

				if ((list.get(i).getName().toLowerCase()
						.contains(name.toLowerCase()) || name
						.equalsIgnoreCase(""))
						&& (list.get(i).getOriginalNativeType().toLowerCase()
								.contains(type.toLowerCase()) || type
								.equalsIgnoreCase(""))
						&& (list.get(i).getOwner().toLowerCase()
								.contains(owner.toLowerCase()) || owner
								.equalsIgnoreCase(""))
						&& ((list.get(i).getId() == id) || (id == 0))) {
					s[k][0] = "" + list.get(i).getName();
					s[k][1] = "" + list.get(i).getId();
					s[k][2] = "" + list.get(i).getOwner();
					s[k][3] = "" + list.get(i).getOriginalNativeType();
					List<VersionSummaryType> b = list.get(i)
							.getVersionSummaries();
					s[k][4] = "";
					for (int z = 0; z < b.size() - 1; z++) {
						s[k][4] = s[k][4] + b.get(z).getName() + "; ";
					}
					s[k][4] = s[k][4] + b.get(b.size() - 1).getName();

					k++;
				}
			} else {
				s[i][0] = "" + list.get(i).getName();
				s[i][1] = "" + list.get(i).getId();
				s[i][2] = "" + list.get(i).getOwner();
				s[i][3] = "" + list.get(i).getOriginalNativeType();
				List<VersionSummaryType> b = list.get(i).getVersionSummaries();
				s[i][4] = "";
				for (int z = 0; z < b.size() - 1; z++) {
					s[i][4] = s[i][4] + b.get(z).getName() + "; ";
				}
				s[i][4] = s[i][4] + b.get(b.size() - 1).getName();
			}
		}
		
		return s;
	}

	public InputStream importProcess(int id) throws Exception {
		
		ProcessSummariesType processSummaries = managerService.readProcessSummaries(null);
		ProcessSummaryType p = processSummaries.getProcessSummary().get(id);
		ExportFormatResultType exf = null;
		final Set<RequestParameterType<?>> noCanoniserParameters = Collections.emptySet();
		exf = managerService.exportFormat(p.getId(), p.getName(), "MAIN", p.getLastVersion(), "PNML 1.3.2", null, false, p.getOwner(), noCanoniserParameters);		
		InputStream is = exf.getNative().getInputStream();
		return is;
	}
	
	public EditSessionType getParams() {
		return aproParams;
	}
	
	public ImportProcessResultType export(String userName, String domain, String processName, 
			String version, boolean makePublic, FileInputStream fis) throws Exception {
        
		final Set<RequestParameterType<?>> noCanoniserParameters = Collections.emptySet();
         	
        SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy'/'MM'/'dd");
			
        return managerService.importProcess(userName, 0, "PNML 1.3.2",
        		processName, version, fis, "", "", sdf.format(new Date()), "", makePublic,
        		noCanoniserParameters);
	}
}

