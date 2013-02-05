package org.apromore.access;

import java.net.ProxySelector;
import java.util.List;

import javax.activation.DataHandler;
import javax.swing.JOptionPane;
import javax.xml.ws.WebServiceException;

import org.apromore.manager.model_portal.EditSessionType;
import org.apromore.manager.model_portal.ExportFormatInputMsgType;
import org.apromore.manager.model_portal.ExportFormatOutputMsgType;
import org.apromore.manager.model_portal.ImportProcessInputMsgType;
import org.apromore.manager.model_portal.ImportProcessOutputMsgType;
import org.apromore.manager.model_portal.ProcessSummariesType;
import org.apromore.manager.model_portal.ProcessSummaryType;
import org.apromore.manager.model_portal.ReadProcessSummariesInputMsgType;
import org.apromore.manager.model_portal.ReadProcessSummariesOutputMsgType;
import org.apromore.manager.service_portal.ManagerPortalPortType;
import org.apromore.manager.service_portal.ManagerPortalService;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

public class ApromoreAccessObject {

	ManagerPortalService service;
	ManagerPortalPortType serviceport;
	boolean isOnline = false;
	EditSessionType aproParams;
	public ApromoreAccessObject() {
		if (ConfigurationManager.getConfiguration().getApromoreUseProxy())
		ProxySelector.setDefault(new WoProxySelector(ConfigurationManager.getConfiguration().getApromoreProxyName(), ConfigurationManager.getConfiguration().getApromoreProxyPort()));
		setupService();
	}



	private void setupService() {
		Object[] options = { "OK" };
		
		try
		{
		service = new ManagerPortalService();
		serviceport = service.getManagerPortal();
		isOnline = true;
		} catch (WebServiceException e)
		{
			JOptionPane.showOptionDialog(null, Messages.getString("Apromore.Export.UI.Error.AproNoConn"), Messages.getString("Apromore.Export.UI.Error.AproNoConnTitle"),

			        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,

			        null, options, options[0]);
		}
		
	}

	public List<ProcessSummaryType> getList() {

		ReadProcessSummariesOutputMsgType sumout = serviceport
				.readProcessSummaries(new ReadProcessSummariesInputMsgType());
		ProcessSummariesType summaries = sumout.getProcessSummaries();
		return summaries.getProcessSummary();
	}

	public DataHandler getPNML(ExportFormatInputMsgType request) {
		request.setFormat("XPDL 2.1");
		ExportFormatOutputMsgType a = serviceport.exportFormat(request);
		return a.getNative();

	}
	
	public EditSessionType getParams() {
		return aproParams;

	}



	public ImportProcessOutputMsgType export(DataHandler a, EditSessionType e) {
		ImportProcessInputMsgType request = new ImportProcessInputMsgType();
		request.setProcessDescription(a);
		request.setAddFakeEvents(false);
		request.setEditSession(e);
		return serviceport.importProcess(request);
	}
	
	public boolean IsOnline()
	{
		return isOnline;
	}
}
