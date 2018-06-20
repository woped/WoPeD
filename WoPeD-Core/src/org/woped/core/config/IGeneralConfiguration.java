package org.woped.core.config;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Locale;
import java.util.Vector;

import org.woped.config.ApromoreServer;

/**
 * Interface defining the public capabilities of the general WoPeD configuration
 * 
 * @author Philip Allgaier
 *
 */
public interface IGeneralConfiguration extends IConfiguration {
	//
	public void setLocale();

	//
	public Locale getLocale();

	//
	public boolean isEditingOnCreation();

	//
	public void setEditingOnCreation(boolean editingOnCreation);

	//
	public boolean isInsertCOPYwhenCopied();

	//
	public void setInsertCOPYwhenCopied(boolean insertCOPYwhenCopied);

	//
	public boolean isExportToolspecific();

	//
	public boolean isImportToolspecific();

	//
	public void setExportToolspecific(boolean exportToolspecific);

	//
	public void setImportToolspecific(boolean importToolspecific);

	//
	public Vector<?> getRecentFiles();

	//
	public void addRecentFile(String name, String path);

	//
	public void removeRecentFile(String name, String path);

	//
	public void removeAllRecentFiles();

	//
	public Dimension getWindowSize();

	//
	public boolean isMaximizeWindow();

	//
	public int getWindowX();

	//
	public int getWindowY();

	//
	public void setWindowSize(Dimension windowSize);

	//
	public void setMaximizeWindow(boolean maximize);

	//
	public void setWindowX(int windowX);

	//
	public void setWindowY(int windowY);

	//
	public String getLogdir();

	//
	public void setLogdir(String logdir);

	//
	public boolean isDefaultHomedirSet();

	//
	public String getDefaultHomedir();

	//
	public void setDefaultHomedir(String dhdir);

	//
	public boolean isCurrentWorkingdirSet();

	//
	public String getCurrentWorkingdir();

	//
	public void setCurrentWorkingdir(String cwdir);

	//
	public boolean isHomedirSet();

	//
	public String getHomedir();

	//
	public void setHomedir(String dir);

	//
	public String getUserdir();

	//
	public boolean isUseMetrics();

	//
	public void setUseMetrics(boolean useMetrics);

	//
	public boolean isUseWoflan();

	//
	public void setUseWoflan(boolean useWoflan);

	//
	public String getWoflanPath();

	//
	public void setWoflanPath(String woflanPath);

	//
	public boolean getColorOn();

	//
	public void setColorOn(boolean b);

	//
	public boolean isSmartEditing();

	//
	public void setSmartEditing(boolean smartEditing);

	//
	public boolean isTpnSaveElementAsName();

	//
	public void setTpnSaveElementAsName(boolean b);

	//
	public void setLookAndFeel(String className);

	//
	public String getLookAndFeel();

	//
	public void setShowGrid(boolean showGrid);

	//
	public boolean isShowGrid();

	//
	public void setArrowWidth(int width);

	//
	public int getArrowWidth();

	//
	public void setArrowheadSize(int headSize);

	//
	public int getArrowheadSize();

	//
	public boolean isFillArrowHead();

	//
	public void setFillArrowHead(boolean fill);

	//
	public boolean isRoundRouting();

	//
	public void setRoundRouting(boolean round);

	//
	public Color getSelectionColor();

	//
	public void setSelectionColor(Color color);

	//
	public Color getPortColor();

	//
	public void setPortColor(Color color);

	//
	public void setLocaleLanguage(String language);

	//
	public String getLocaleLanguage();

	//
	public void setLocaleCountry(String country);

	//
	public String getLocaleCountry();

	//
	public void setLocaleVariant(String variant);

	//
	public String getLocaleVariant();

	//
	public void setColor1(int rgb);

	//
	public void setColor2(int rgb);

	//
	public void setColor3(int rgb);

	//
	public void setColor4(int rgb);

	//
	public void setColor5(int rgb);

	//
	public void setColor6(int rgb);

	//
	public void setColor7(int rgb);

	//
	public void setColor8(int rgb);

	//
	public void setColor9(int rgb);

	//
	public void setColor10(int rgb);

	//
	public void setColor11(int rgb);

	//
	public void setColor12(int rgb);

	//
	public void setColor13(int rgb);

	//
	public void setColor14(int rgb);

	//
	public void setColor15(int rgb);

	//
	public void setColor16(int rgb);

	//
	public int getColor1();

	//
	public int getColor2();

	//
	public int getColor3();

	//
	public int getColor4();

	//
	public int getColor5();

	//
	public int getColor6();

	//
	public int getColor7();

	//
	public int getColor8();

	//
	public int getColor9();

	//
	public int getColor10();

	//
	public int getColor11();

	//
	public int getColor12();

	//
	public int getColor13();

	//
	public int getColor14();

	//
	public int getColor15();

	//
	public int getColor16();

	//
	public Color[] getUnderstandColors();

	//
	public int getDefaultColor1();

	//
	public int getDefaultColor2();

	//
	public int getDefaultColor3();

	//
	public int getDefaultColor4();

	//
	public int getDefaultColor5();

	//
	public int getDefaultColor6();

	//
	public int getDefaultColor7();

	//
	public int getDefaultColor8();

	//
	public int getDefaultColor9();

	//
	public int getDefaultColor10();

	//
	public int getDefaultColor11();

	//
	public int getDefaultColor12();

	//
	public int getDefaultColor13();

	//
	public int getDefaultColor14();

	//
	public int getDefaultColor15();

	//
	public int getDefaultColor16();

	//
	public Color[] getDefaultUnderstandColors();

	//
	public int getAlgorithmMode();

	//
	public void setAlgorithmMode(int n);

	//
	public int getAlgorithmDecimalPlaces();

	//
	public void setAlgorithmDecimalPlaces(int n);

	//
	public int getVariableDecimalPlaces();

	//
	public void setVariableDecimalPlaces(int n);

	//
	public boolean isUseAlgorithmHighlighting();

	//
	public void setUseAlgorithmHighlighting(boolean useHighlighting);

	//
	public boolean isShowNamesInBuilder();

	//
	public void setShowNamesInBuilder(boolean showNames);

	//
	public boolean isShowAdvancedErrorMessages();

	//
	public void setShowAdvancedErrorMessages(boolean showAdvanced);

	// Apromore
	public boolean getApromoreUse();

	public void setApromoreUse(boolean selected);

	public int getCurrentApromoreIndex();

	public void setCurrentApromoreIndex(int index);

	public String getApromoreServerName();

	public void setApromoreServerName(String name);

	public String getApromoreServerURL();

	public void setApromoreServerURL(String server);

	public int getApromoreServerPort();

	public void setApromoreServerPort(int port);

	public String getApromoreManagerPath();

	public void setApromoreManagerPath(String managerUrl);

	public String getApromoreUsername();

	public void setApromoreUsername(String user);

	public String getApromorePassword();

	public void setApromorePassword(String user);

	public boolean getApromoreUseProxy();

	public void setApromoreUseProxy(boolean set);

	public String getApromoreProxyName();

	public void setApromoreProxyName(String user);

	public int getApromoreProxyPort();

	public void setApromoreProxyPort(int port);
	
	public boolean isSetApromorePassword();

	//

	public boolean isRegistered();

	public void setRegistered(boolean selected);

	public boolean isShowOnStartup();

	public void setShowOnStartup(boolean selected);

	public String getRegistrationEmail();

	public void setRegistrationEmail(String address);

	public int getLaunchCounter();

	public void setLaunchCounter(int value);

	// Apromore server-list administration

	public boolean isSetApromoreServers();

	public String[] getApromoreServerNames();

	public int getApromoreServerListLength();

	public ApromoreServer[] getApromoreServers();

	public void addApromoreServer(int ID, String name, String url, int port,
			String path, String user, String pwd, boolean useProxy,
			String proxyUrl, int proxyPort);

	public void changeApromoreServerSettings(int ID, String name, String url,
			int port, String path, String user, String pwd, boolean useProxy,
			String proxyUrl, int proxyPort);

	public void removeApromoreServer(int index);

	public String getProcess2TextServerHost();

	public void setProcess2TextServerHost(String host);

	public int getProcess2TextServerPort();

	public void setProcess2TextServerPort(int port);

	public String getProcess2TextServerURI();

	public void setProcess2TextServerURI(String uri);

	public boolean getProcess2TextUse();

	public void setProcess2TextUse(boolean selected);

	public String getText2ProcessServerHost();

	public void setText2ProcessServerHost(String host);

	public int getText2ProcessServerPort();

	public void setText2ProcessServerPort(int port);

	public String getText2ProcessServerURI();

	public void setText2ProcessServerURI(String uri);

	public boolean getText2ProcessUse();

	public void setText2ProcessUse(boolean selected);
	
	//dashboard
	public int getBusinessDashboardServerPort();

	public void setBusinessDashboardServerPort(int port);
	
	public boolean getBusinessDashboardUseByDefault();

	public void setBusinessDashboardUseByDefault(boolean bAutoStart);
	
	public int getBusinessDashboardMaxValues();

	public void setBusinessDashboardMaxValues(int maxvalues);
}