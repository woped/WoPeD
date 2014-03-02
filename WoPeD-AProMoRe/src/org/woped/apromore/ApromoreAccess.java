package org.woped.apromore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.ProxySelector;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import org.apromore.manager.client.ManagerService;
import org.apromore.manager.client.ManagerServiceClient;
import org.apromore.model.ExportFormatResultType;
import org.apromore.model.ProcessSummariesType;
import org.apromore.model.ProcessSummaryType;
import org.apromore.model.VersionSummaryType;
import org.apromore.plugin.property.RequestParameterType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.woped.core.config.ConfigurationManager;

public class ApromoreAccess {

	private static ApromoreAccess instance = null;
	private static final SimpleDateFormat apromoreTimeFormat = new SimpleDateFormat(
			"dd-MM-yyyy hh-mm-ss");

	public static ApromoreAccess getInstance() {
		return instance;
	}

	private HttpComponentsMessageSender httpCms;
	private Jaxb2Marshaller serviceMarshaller;
	private SaajSoapMessageFactory soapMsgFactory;
	private WebServiceTemplate wsTemp;
	private ManagerService managerService;
	private ProcessSummariesType processSummaries;
	private List<ProcessSummaryType> processList;

	public ApromoreAccess() {
		if (ConfigurationManager.getConfiguration().getApromoreUseProxy())
			ProxySelector.setDefault(new WoProxySelector(ConfigurationManager
					.getConfiguration().getApromoreProxyName(),
					ConfigurationManager.getConfiguration()
							.getApromoreProxyPort()));
	}

	public void connect() throws SOAPException {
		httpCms = new HttpComponentsMessageSender();
		httpCms.setReadTimeout(120000);
		httpCms.setConnectionTimeout(120000);

		serviceMarshaller = new Jaxb2Marshaller();
		serviceMarshaller.setContextPath("org.apromore.model");
		soapMsgFactory = new SaajSoapMessageFactory(
				MessageFactory.newInstance());
		soapMsgFactory
				.setSoapVersion(org.springframework.ws.soap.SoapVersion.SOAP_11);

		wsTemp = new WebServiceTemplate(soapMsgFactory);
		wsTemp.setMarshaller(serviceMarshaller);
		wsTemp.setUnmarshaller(serviceMarshaller);
		wsTemp.setMessageSender(httpCms);
		wsTemp.setDefaultUri(ConfigurationManager.getConfiguration()
				.getApromoreServerURL()
				+ ":"
				+ ConfigurationManager.getConfiguration()
						.getApromoreServerPort()
				+ "/"
				+ ConfigurationManager.getConfiguration()
						.getApromoreManagerPath());
		managerService = new ManagerServiceClient(wsTemp);
	}

	public String[][] getProcessList() throws Exception {
		processSummaries = managerService.readProcessSummaries(1, null);
		processList = processSummaries.getProcessSummary();
		String[][] s = new String[processList.size()][5];

		for (int i = 0; i < processList.size(); i++) {
			s[i][0] = "" + processList.get(i).getName();
			s[i][1] = "" + processList.get(i).getId();
			s[i][2] = "" + processList.get(i).getOwner();
			s[i][3] = "" + processList.get(i).getOriginalNativeType();
			List<VersionSummaryType> vst = processList.get(i)
					.getVersionSummaries();
			s[i][4] = "" + vst.get(vst.size() - 1).getVersionNumber();
		}
		return s;
	}

	public String[][] filterProcessList(String name, int id, String owner,
			String type) {

		int j = processList.size() - 1;

		if (!((name.equalsIgnoreCase("")) && (id == 0)
				&& (owner.equalsIgnoreCase("")) && (type.equalsIgnoreCase("")))) {
			j = 0;
			for (int i = 0; i < processList.size() - 1; i++) {
				if ((processList.get(i).getName().toLowerCase()
						.contains(name.toLowerCase()) || name
						.equalsIgnoreCase(""))
						&& (processList.get(i).getOriginalNativeType()
								.toLowerCase().contains(type.toLowerCase()) || type
								.equalsIgnoreCase(""))
						&& (processList.get(i).getOwner().toLowerCase()
								.contains(owner.toLowerCase()) || owner
								.equalsIgnoreCase(""))
						&& ((processList.get(i).getId() == id) || (id == 0)))
					j++;
			}
		}

		String[][] s = new String[j][5];

		int k = 0;

		for (int i = 0; i < processList.size() - 1; i++) {
			if (!((name.equalsIgnoreCase("")) && (id == 0)
					&& (owner.equalsIgnoreCase("")) && (type
						.equalsIgnoreCase("")))) {

				if ((processList.get(i).getName().toLowerCase()
						.contains(name.toLowerCase()) || name
						.equalsIgnoreCase(""))
						&& (processList.get(i).getOriginalNativeType()
								.toLowerCase().contains(type.toLowerCase()) || type
								.equalsIgnoreCase(""))
						&& (processList.get(i).getOwner().toLowerCase()
								.contains(owner.toLowerCase()) || owner
								.equalsIgnoreCase(""))
						&& ((processList.get(i).getId() == id) || (id == 0))) {
					s[k][0] = "" + processList.get(i).getName();
					s[k][1] = "" + processList.get(i).getId();
					s[k][2] = "" + processList.get(i).getOwner();
					s[k][3] = "" + processList.get(i).getOriginalNativeType();
					List<VersionSummaryType> vst = processList.get(i)
							.getVersionSummaries();
					s[k][4] = "" + vst.get(vst.size() - 1).getVersionNumber();
					k++;
				}
			} else {
				s[i][0] = "" + processList.get(i).getName();
				s[i][1] = "" + processList.get(i).getId();
				s[i][2] = "" + processList.get(i).getOwner();
				s[i][3] = "" + processList.get(i).getOriginalNativeType();
				List<VersionSummaryType> vst = processList.get(i)
						.getVersionSummaries();
				s[i][4] = "" + vst.get(vst.size() - 1).getVersionNumber();
			}
		}

		return s;
	}

	public InputStream importProcess(int id) throws Exception {

		ProcessSummaryType p = processList.get(id);
		ExportFormatResultType exf = null;
		final Set<RequestParameterType<?>> noCanoniserParameters = Collections
				.emptySet();
		String inputString;

		exf = managerService.exportFormat(p.getId(), p.getName(), "MAIN",
				p.getLastVersion(), "PNML 1.3.2", p.getLastVersion(), true,
				p.getOwner(), noCanoniserParameters);

		Scanner scanner = new Scanner(exf.getNative().getInputStream());
		Scanner s = scanner.useDelimiter("\\A");
		inputString = s.hasNext() ? s.next() : "";
		inputString = inputString.replaceAll("pnml.apromore.org",
				"pnml.woped.org");
		scanner.close();
		;
		return new ByteArrayInputStream(inputString.getBytes());
	}

	public void exportProcess(String userName, String processName,
			InputStream is, String domain, boolean makePublic) {

		Integer folderId = 1;
		String nativeType = "PNML 1.3.2";
		String version = "1.0";
		String documentation = "";
		final Set<RequestParameterType<?>> noCanoniserParameters = Collections
				.emptySet();

		try {
			managerService.importProcess(userName, folderId, nativeType,
					processName, version, is, domain, documentation,
					getCurrentDate(), getCurrentDate(), makePublic,
					noCanoniserParameters);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void updateProcess(Integer id, InputStream is) {

		ProcessSummaryType p = processList.get(id);
		String username = p.getOwner();
		String nativeType = p.getOriginalNativeType();
		String domain = p.getDomain();
		String processName = p.getName();
		String originalBranchName = "MAIN";
		String newBranchName = "MAIN";
		String versionNumber = String
				.valueOf(Double.valueOf(p.getLastVersion()) + 0.1);
		String originalVersionNumber = p.getLastVersion();		

		try {
			managerService.updateProcess(0, username, nativeType, id, domain,
					processName, originalBranchName, newBranchName,
					versionNumber, originalVersionNumber, "", is);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private String getCurrentDate() {
		Date currentDate = new Date(System.currentTimeMillis());
		return apromoreTimeFormat.format(currentDate).toString();
	}
}
