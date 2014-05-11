package org.woped.apromore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

	public void connect(String uri) throws SOAPException {
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
		wsTemp.setDefaultUri(uri);
		managerService = new ManagerServiceClient(wsTemp);		
	}

	public void test(String server, String user) throws Exception {
		connect(server);
		managerService.readUserByUsername(user);
	}

	public String[][] getProcessList() throws Exception {
		processSummaries = managerService.readProcessSummaries(1, null);
		processList = processSummaries.getProcessSummary();
		String[][] s = new String[processList.size()][6];

		for (int i = 0; i < processList.size(); i++) {
			s[i][0] = "" + processList.get(i).getName();
			s[i][1] = "" + processList.get(i).getId();
			s[i][2] = "" + processList.get(i).getOwner();
			s[i][3] = "" + processList.get(i).getOriginalNativeType();
			s[i][4] = "" + processList.get(i).getDomain();
			List<VersionSummaryType> vst = processList.get(i)
					.getVersionSummaries();
			s[i][5] = "" + vst.get(vst.size() - 1).getVersionNumber();
		}
		return s;
	}

	public String[][] filterProcessList(String name, String id, String owner,
			String type, String domain) {

		int j = processList.size() - 1;

		if (!((name.equalsIgnoreCase("")) && (id == null)
				&& (owner.equalsIgnoreCase("")) && (type.equalsIgnoreCase("")) && domain
					.equalsIgnoreCase(""))) {
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
						&& (processList.get(i).getOwner().toLowerCase()
								.contains(domain.toLowerCase()) || domain
								.equalsIgnoreCase(""))
						&& ((id == null) || id.equals(""
								+ processList.get(i).getId())))
					j++;
			}
		}

		String[][] s = new String[j][6];

		int k = 0;

		for (int i = 0; i < processList.size() - 1; i++) {
			if (!((name.equalsIgnoreCase("")) && (id == null)
					&& (owner.equalsIgnoreCase(""))
					&& (domain.equalsIgnoreCase("")) && (type
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
						&& (processList.get(i).getDomain().toLowerCase()
								.contains(domain.toLowerCase()) || domain
								.equalsIgnoreCase(""))
						&& ((id == null) || id.equals(""
								+ processList.get(i).getId()))) {
					s[k][0] = "" + processList.get(i).getName();
					s[k][1] = "" + processList.get(i).getId();
					s[k][2] = "" + processList.get(i).getOwner();
					s[k][3] = "" + processList.get(i).getOriginalNativeType();
					s[k][4] = "" + processList.get(i).getDomain();
					List<VersionSummaryType> vst = processList.get(i)
							.getVersionSummaries();
					s[k][5] = "" + vst.get(vst.size() - 1).getVersionNumber();
					k++;
				}
			} else {
				s[i][0] = "" + processList.get(i).getName();
				s[i][1] = "" + processList.get(i).getId();
				s[i][2] = "" + processList.get(i).getOwner();
				s[i][3] = "" + processList.get(i).getOriginalNativeType();
				s[i][4] = "" + processList.get(i).getDomain();
				List<VersionSummaryType> vst = processList.get(i)
						.getVersionSummaries();
				s[i][5] = "" + vst.get(vst.size() - 1).getVersionNumber();
			}
		}

		return s;
	}

	public ByteArrayInputStream importProcess(int id) throws Exception {

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
		return new ByteArrayInputStream(inputString.getBytes());
	}

	public void exportProcess(String userName, String processName,
			ByteArrayOutputStream os, String domain, String version,
			boolean makePublic) throws Exception {

		Integer folderId = 1;
		String nativeType = "PNML 1.3.2";
		String documentation = "";
		final Set<RequestParameterType<?>> noCanoniserParameters = Collections
				.emptySet();
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

		managerService.importProcess(userName, folderId, nativeType,
				processName, version, is, domain, documentation,
				getCurrentDate(), getCurrentDate(), makePublic,
				noCanoniserParameters);
	}

	public void updateProcess(Integer id, String username, String nativeType,
			String processName, String newVersionNumber,
			ByteArrayOutputStream os) throws Exception {

		ProcessSummaryType p = null;
		for (ProcessSummaryType process : processList) {
			if (process.getId() == id) {
				p = process;
			}
		}

		String originalBranchName = "MAIN";
		String newBranchName = "MAIN";
		String originalVersionNumber = p.getLastVersion();
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		String domain = p.getDomain();

		managerService.updateProcess(Integer.MAX_VALUE, username, nativeType,
				id, domain, processName, originalBranchName, newBranchName,
				newVersionNumber, originalVersionNumber, "", is);
	}

	private String getCurrentDate() {
		Date currentDate = new Date(System.currentTimeMillis());
		return apromoreTimeFormat.format(currentDate).toString();
	}
}
