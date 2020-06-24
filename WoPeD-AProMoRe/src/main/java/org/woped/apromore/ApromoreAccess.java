package org.woped.apromore;

import java.awt.Container;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.JOptionPane;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apromore.manager.client.ManagerService;
import org.apromore.manager.client.ManagerServiceClient;
import org.apromore.model.ExportFormatResultType;
import org.apromore.model.FolderType;
import org.apromore.model.ProcessSummaryType;
import org.apromore.model.SummariesType;
import org.apromore.model.SummaryType;
import org.apromore.model.UserType;
import org.apromore.model.VersionSummaryType;
import org.apromore.plugin.property.RequestParameterType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.WebServiceTransportException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessageCreationException;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.woped.config.ApromoreServer;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

public class ApromoreAccess {

	public final int READ_TIMEOUT = 10000;
	public final int CONNECTION_TIMEOUT = 10000;

	private static ApromoreAccess instance = null;
	private static final SimpleDateFormat apromoreTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");

	public static ApromoreAccess getInstance() {
		return instance;
	}

	private Container parent;
	private HttpComponentsMessageSender httpCms;
	private Jaxb2Marshaller serviceMarshaller;
	private SaajSoapMessageFactory soapMsgFactory;
	private WebServiceTemplate wsTemp;
	private ManagerService managerService;
	private SummariesType processSummaries;
	private List<ProcessSummaryType> processList;

	private ApromoreServer[] servers = null;

	public ApromoreAccess(Container parent) {
		this.parent = parent;
		this.servers = ConfigurationManager.getConfiguration().getApromoreServers();
	}

	public boolean checkIfServerAvailable() {
		try {
			managerService.readAllUsers();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void connect(String uri) throws SOAPException {
		httpCms = new HttpComponentsMessageSender();
		httpCms.setReadTimeout(READ_TIMEOUT);
		httpCms.setConnectionTimeout(CONNECTION_TIMEOUT);

		serviceMarshaller = new Jaxb2Marshaller();
		serviceMarshaller.setContextPath("org.apromore.model");
		soapMsgFactory = new SaajSoapMessageFactory(MessageFactory.newInstance());
		soapMsgFactory.setSoapVersion(org.springframework.ws.soap.SoapVersion.SOAP_11);

		wsTemp = new WebServiceTemplate(soapMsgFactory);
		wsTemp.setMarshaller(serviceMarshaller);
		wsTemp.setUnmarshaller(serviceMarshaller);
		wsTemp.setMessageSender(httpCms);

		// If URI is empty, take the one from the server configuration.
		if (uri == null) {
			wsTemp.setDefaultUri(getURI());
		} else {
			wsTemp.setDefaultUri(uri);
		}
		managerService = new ManagerServiceClient(wsTemp);
	}

	public void test(String server, String port, String managerpath, String username) {

		try {
			Integer.valueOf(port);
		} catch (NumberFormatException e) {
			showDialog(Messages.getString("Apromore.UI.Validation.Error.Port"),
					Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!managerpath.equals("manager/services/manager")) {
			showDialog(Messages.getString("Apromore.UI.Validation.Warning.WebservicePath"),
					Messages.getString("Apromore.UI.Warning.Title"), JOptionPane.WARNING_MESSAGE);
		}

		String serverUrl = server + ":" + port + "/" + managerpath;

		try {
			new URL(serverUrl);
			connect(serverUrl);

			org.apromore.model.UsernamesType test = managerService.readAllUsers();

			if (test == null) {
				showDialog(Messages.getString("Apromore.UI.Validation.Error.Connection"),
						Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
				return;
			}

		} catch (MalformedURLException e) {
			showDialog(Messages.getString("Apromore.UI.Validation.Error.Url"),
					Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			return;
		} catch (SOAPException e) {
			showDialog(Messages.getString("Apromore.UI.Validation.Error.Connection"),
					Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			return;
		} catch (WebServiceTransportException e) {
			showDialog(Messages.getString("Apromore.UI.Validation.Error.Connection"),
					Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			return;
		} catch (WebServiceIOException e) {
			if (e.getCause() instanceof ConnectTimeoutException) {
				showDialog(Messages.getString("Apromore.UI.Validation.Error.Timeout"),
						Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			} else if (e.getCause() instanceof UnknownHostException) {
				showDialog(Messages.getString("Apromore.UI.Validation.Error.UnknownHost"),
						Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			} else {
				showDialog(Messages.getString("Apromore.UI.Validation.Error.Connection"),
						Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			}
			return;
		} catch (SoapMessageCreationException e) {
			showDialog(Messages.getString("Apromore.UI.Validation.Error.WebservicePath"),
					Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		UserType user = managerService.readUserByUsername(username);
		if (user == null) {
			showDialog(Messages.getString("Apromore.UI.Validation.Error.User"),
					Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		showDialog(Messages.getString("Apromore.UI.Validation.ConnectionOk"),
				Messages.getString("Apromore.UI.TextBand.Title"), JOptionPane.INFORMATION_MESSAGE);
	}

	public String[][] getProcessList() throws Exception {
		UserType user = managerService
				.readUserByUsername(ConfigurationManager.getConfiguration().getApromoreUsername());

		List<FolderType> folderForUser = managerService.getWorkspaceFolderTree(user.getId());

		processList = new ArrayList<ProcessSummaryType>();
		for (FolderType folder : folderForUser) {
			processSummaries = managerService.readProcessSummaries(folder.getId(), null);
			for (SummaryType summary : processSummaries.getSummary()) {
				ProcessSummaryType process = (ProcessSummaryType)summary;
				process.setFolder(folder);
				processList.add(process);
			}
		}
		String[][] s = new String[processList.size()][7];

		for (int i = 0; i < processList.size(); i++) {
			s[i][0] = "" + processList.get(i).getName();
			s[i][1] = "" + processList.get(i).getId();
			s[i][2] = "" + processList.get(i).getOwner();
			s[i][3] = "" + processList.get(i).getOriginalNativeType();
			s[i][4] = "" + processList.get(i).getDomain();

			List<VersionSummaryType> vst = processList.get(i).getVersionSummaries();
			s[i][5] = "" + vst.get(vst.size() - 1).getVersionNumber();
			s[i][6] = "" + processList.get(i).getFolder().getFolderName();
		}
		return s;
	}

	public String[][] filterProcessList(String name, String id, String owner, String type, String domain) {

		int j = processList.size() - 1;

		if (!((name.equalsIgnoreCase("")) && (id == null) && (owner.equalsIgnoreCase("")) && (type.equalsIgnoreCase(""))
				&& domain.equalsIgnoreCase(""))) {
			j = 0;
			for (int i = 0; i < processList.size() - 1; i++) {
				if ((processList.get(i).getName().toLowerCase().contains(name.toLowerCase())
						|| name.equalsIgnoreCase(""))
						&& (processList.get(i).getOriginalNativeType().toLowerCase().contains(type.toLowerCase())
						|| type.equalsIgnoreCase(""))
						&& (processList.get(i).getOwner().toLowerCase().contains(owner.toLowerCase())
						|| owner.equalsIgnoreCase(""))
						&& (processList.get(i).getDomain().toLowerCase().contains(domain.toLowerCase())
						|| domain.equalsIgnoreCase(""))
						&& ((id == null) || id.equals("" + processList.get(i).getId())))
					j++;
			}
		}

		String[][] s = new String[j][7];

		int k = 0;

		for (int i = 0; i < processList.size() - 1; i++) {
			if (!((name.equalsIgnoreCase("")) && (id == null) && (owner.equalsIgnoreCase(""))
					&& (domain.equalsIgnoreCase("")) && (type.equalsIgnoreCase("")))) {

				if ((processList.get(i).getName().toLowerCase().contains(name.toLowerCase())
						|| name.equalsIgnoreCase(""))
						&& (processList.get(i).getOriginalNativeType().toLowerCase().contains(type.toLowerCase())
						|| type.equalsIgnoreCase(""))
						&& (processList.get(i).getOwner().toLowerCase().contains(owner.toLowerCase())
						|| owner.equalsIgnoreCase(""))
						&& (processList.get(i).getDomain().toLowerCase().contains(domain.toLowerCase())
						|| domain.equalsIgnoreCase(""))
						&& ((id == null) || id.equals("" + processList.get(i).getId()))) {
					s[k][0] = "" + processList.get(i).getName();
					s[k][1] = "" + processList.get(i).getId();
					s[k][2] = "" + processList.get(i).getOwner();
					s[k][3] = "" + processList.get(i).getOriginalNativeType();
					s[k][4] = "" + processList.get(i).getDomain();
					List<VersionSummaryType> vst = processList.get(i).getVersionSummaries();
					s[k][5] = "" + vst.get(vst.size() - 1).getVersionNumber();
					s[k][6] = "" + processList.get(i).getFolder().getFolderName();
					k++;
				}
			} else {
				s[i][0] = "" + processList.get(i).getName();
				s[i][1] = "" + processList.get(i).getId();
				s[i][2] = "" + processList.get(i).getOwner();
				s[i][3] = "" + processList.get(i).getOriginalNativeType();
				s[i][4] = "" + processList.get(i).getDomain();
				List<VersionSummaryType> vst = processList.get(i).getVersionSummaries();
				s[i][5] = "" + vst.get(vst.size() - 1).getVersionNumber();
				s[i][6] = "" + processList.get(i).getFolder().getFolderName();
			}
		}

		return s;
	}

	public ByteArrayInputStream importProcess(int id, boolean edgesToPlaces, boolean tasksToTransitions) throws Exception {

		ProcessSummaryType p = processList.get(id);
		ExportFormatResultType exf = null;
		final Set<RequestParameterType<?>> noCanoniserParameters = new CopyOnWriteArraySet<>();
		RequestParameterType<?> rptTasks = new RequestParameterType<>("isCpfTaskPnmlTransition", tasksToTransitions);
		//RequestParameterType<?> rptEdges = new RequestParameterType<>("isCpfTaskPnmlTransition", edgesToPlaces);
		noCanoniserParameters.add(rptTasks);
		//noCanoniserParameters.add(rptEdges); --> Probleme beim Hinzufügen des zweiten Parameters
		String inputString;

		exf = managerService.exportFormat(p.getId(), p.getName(), "MAIN", p.getLastVersion(), "PNML 1.3.2",
				p.getLastVersion(), true, p.getOwner(), noCanoniserParameters);

		Scanner scanner = new Scanner(exf.getNative().getInputStream());
		Scanner s = scanner.useDelimiter("\\A");
		inputString = s.hasNext() ? s.next() : "";
		inputString = inputString.replaceAll("pnml.apromore.org", "pnml.woped.org");
		scanner.close();
		return new ByteArrayInputStream(inputString.getBytes());
	}

	public void exportProcess(String userName, String folder, String processName, ByteArrayOutputStream os,
							  String domain, String version, boolean makePublic) throws Exception {

		UserType user = managerService.readUserByUsername(userName);
		List<FolderType> folders = managerService.getWorkspaceFolderTree(user.getId());
		int folderId = Integer.MIN_VALUE;
		for (FolderType ft : folders) {
			if (ft.getFolderName().equals(folder)) {
				folderId = ft.getId();
			}
		}
		if (folderId != Integer.MIN_VALUE) {
			String nativeType = "PNML 1.3.2";
			String documentation = "";
			final Set<RequestParameterType<?>> noCanoniserParameters = Collections.emptySet();
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

			managerService.importProcess(userName, folderId, nativeType, processName, version, is, domain,
					documentation, getCurrentDate(), getCurrentDate(), makePublic, noCanoniserParameters);
		}
	}

	public void updateProcess(Integer id, String username, String nativeType, String processName,
							  String newVersionNumber, ByteArrayOutputStream os) throws Exception {

		ProcessSummaryType p = null;
		for (ProcessSummaryType process : processList) {
			if (process.getId().equals(id)) {
				p = process;
			}
		}

		String originalBranchName = "MAIN";
		String newBranchName = "MAIN";
		String originalVersionNumber = p.getLastVersion();
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		String domain = p.getDomain();

		managerService.updateProcess(Integer.MAX_VALUE, username, nativeType, id, domain, processName,
				originalBranchName, newBranchName, newVersionNumber, originalVersionNumber, "", is);
	}

	private String getCurrentDate() {
		Date currentDate = new Date(System.currentTimeMillis());
		return apromoreTimeFormat.format(currentDate).toString();
	}

	public List<FolderType> getFoldersForCurrentUser() {
		List<FolderType> list = new ArrayList<FolderType>();
		try {

			UserType user = managerService
					.readUserByUsername(ConfigurationManager.getConfiguration().getApromoreUsername());
			return managerService.getWorkspaceFolderTree(user.getId());

		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	private String getURI() {
		return ConfigurationManager.getConfiguration().getApromoreServerURL() + ":"
				+ ConfigurationManager.getConfiguration().getApromoreServerPort() + "/"
				+ ConfigurationManager.getConfiguration().getApromoreManagerPath();
	}

	private String getURI(String url, int port, String managerPath) {
		return url + ":" + port + "/" + managerPath;
	}

	private void showDialog(String message, String titel, Integer type) {
		JOptionPane.showMessageDialog(parent, message, titel, type);
	}

	public ApromoreServer[] getServers() {
		return servers;
	}

	public int getCurrentServerIndex() {
		return ConfigurationManager.getConfiguration().getCurrentApromoreIndex();
	}

	public void setCurrentServer(int selectedIndex) {

		ConfigurationManager.getConfiguration().setCurrentApromoreIndex(selectedIndex);

		ConfigurationManager.getConfiguration().setApromoreManagerPath(servers[selectedIndex].getApromoreManagerPath());
		ConfigurationManager.getConfiguration().setApromoreServerName(servers[selectedIndex].getApromoreServerName());
		ConfigurationManager.getConfiguration().setApromoreServerPort(servers[selectedIndex].getApromoreServerPort());

		ConfigurationManager.getConfiguration().setApromoreServerURL(servers[selectedIndex].getApromoreServerURL());

		String url = servers[selectedIndex].getApromoreServerURL();
		int port = servers[selectedIndex].getApromoreServerPort();
		String managerPath = servers[selectedIndex].getApromoreManagerPath();

		try {
			connect(getURI(url, port, managerPath));
		} catch (SOAPException e) {
			showDialog(Messages.getString("Apromore.UI.Validation.Error.connection"),
					Messages.getString("Apromore.UI.Error.Title"), JOptionPane.ERROR_MESSAGE);

		}
	}
}