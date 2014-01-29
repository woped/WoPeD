/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
/*
 * Created on Jan 20, 2005 @author Thomas Freytag
 *  
 */

package org.woped.editor.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.EmptyStackException;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.gui.translations.Messages;

import com.sun.javafx.application.PlatformImpl;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 * 
 * TODO: DOCUMENTATION (tfreytag)
 */

@SuppressWarnings("serial")
public class HelpBrowser extends JFrame
{	     
	    
	    /** 
	     * createScene 
	     * 
	     * Note: Key is that Scene needs to be created and run on "FX user thread" 
	     *       NOT on the AWT-EventQueue Thread 
	     * 
	     */  
	    
	    
    public static HelpBrowser c_instance = null;

    private Stage stage;  
    private WebView browser;  
    private JFXPanel jfxPanel;  
    private WebEngine webEngine;  
    
    private JLabel            statusBarInfo;	
    private String            currURL;
    private String            homeURL;
    private String            contentsURL;
    private String			  defaultLangPat;
    
    private BrowserHistory    history = new BrowserHistory();

    public static HelpBrowser getInstance()
    {
        if (c_instance == null) c_instance = new HelpBrowser();
        return c_instance;
    }

    private HelpBrowser()
    {
        super("WoPeD Helpbrowser");
        
/*        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            LoggerManager.error(Constants.EDITOR_LOGGER, "Error setting Look and Feel: " + e);
        }*/

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });

        this.setIconImage(Messages.getImageIcon("Menu.Help.Index").getImage());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel jp1 = new JPanel(new BorderLayout());
        jp1.add(getMenubar(), BorderLayout.CENTER);
        topPanel.add(jp1, BorderLayout.NORTH);
        JPanel jp2 = new JPanel(new BorderLayout());
        jp2.add(getToolbar(), BorderLayout.CENTER);
        topPanel.add(jp2, BorderLayout.SOUTH);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width > 960 ? screenSize.width - 960 :  0, 40, 815 + topPanel.getWidth(), 702 + topPanel.getHeight());
        
        jfxPanel = new JFXPanel();  
        createScene();
        
        getContentPane().add(jfxPanel, BorderLayout.CENTER);  
        
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBarInfo = new JLabel();
        statusBar.add(statusBarInfo, BorderLayout.CENTER);
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }   
     
    
    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @param currURL
     * @param homeURL
     * @param contentsURL
     */
    public void init(String currFileName)
    {
    	String contentFileName = Messages.getString("Help.File.Contents");
    	String indexFileName = Messages.getString("Help.File.Index");
    	if (currFileName == null)
        	currFileName = Messages.getString("Help.File.Index");
   	
    	URL url = this.getClass().getResource("/doc");

		if (url != null)
		{
			// locate HTML files in jarfile
			currFileName = url.toExternalForm().concat(
					Messages.getString("Help.Dir")).concat(currFileName);
			contentFileName = url.toExternalForm().concat(
					Messages.getString("Help.Dir")).concat(contentFileName);
			indexFileName = url.toExternalForm().concat(
					Messages.getString("Help.Dir")).concat(indexFileName);
		} else
		{
			// locate HTML files in local folder
			File f = new File(".");
			String filePath = "file:" + f.getAbsolutePath();
			int dotAt = filePath.lastIndexOf(".");
			currFileName = filePath.substring(0, dotAt) + "doc/"
					+ Messages.getString("Help.Dir") + currFileName;
			contentFileName = filePath.substring(0, dotAt) + "doc/"
					+ Messages.getString("Help.Dir") + contentFileName;
			indexFileName = filePath.substring(0, dotAt) + "doc/"
					+ Messages.getString("Help.Dir") + indexFileName;
		}

        this.currURL = currFileName;
        this.homeURL = indexFileName;
        this.contentsURL = contentFileName;
        showPage(currURL, false, false);
        setVisible(true);
    }

    private HelpToolBar getToolbar()
    {
        return HelpToolBar.getInstance(this);
    }

    private HelpMenuBar getMenubar()
    {
        return HelpMenuBar.getInstance(this);
    }

    private void setStatusBarInfo(String url)
    {
        if (url.substring(0, 6).equals("file:/") || url.substring(0, 4).equals("jar:")) url = new File(url).getName();
        statusBarInfo.setText(url);
    }

    /**
     * Method showPage Displays a given HTML page in the HelpBrowser window
     * 
     * @param url
     *            URL of the page to be displayed
     * @param enterPage
     *            true if current URL should only be refreshed
     * @param fromHistory
     *            true if URL is retrieved from history buffer
     */
    private void showPage(final String url, boolean justRefresh, boolean fromHistory)
    {
        try
        {
        	//Thread for javafx web engine
        	PlatformImpl.runLater(new Runnable() {
                public void run() {
                    webEngine.load(url);
                }
           });
            
            setStatusBarInfo(url);

            if (!justRefresh)
            {
                if (!fromHistory)
                {
                    history.add(url);
                }

                getToolbar().getBackButton().setEnabled(history.canBack());
                getMenubar().getBackMenuItem().setEnabled(history.canBack());
                getToolbar().getForwardButton().setEnabled(history.canForward());
                getMenubar().getForwardMenuItem().setEnabled(history.canForward());
                currURL = url;
            }
        } catch (Exception ioe)
        {
            LoggerManager.error(Constants.EDITOR_LOGGER, "Can't open URL " + url + ": " + ioe);
        }
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public void refresh()
    {
        //TODO: adopt 
    	//Document doc = htmlPane.getDocument();
        //doc.putProperty(Document.StreamDescriptionProperty, null);
        showPage(currURL, true, true);
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public void home()
    {
        if (!currURL.equals(homeURL)) showPage(homeURL, false, false);
        else refresh();
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public void contents()
    {
        if (!currURL.equals(contentsURL)) showPage(contentsURL, false, false);
        else refresh();
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public void back()
    {
        try
        {
            String prevURL = history.back();
            showPage(prevURL, false, true);
        } catch (EmptyStackException e)
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "No backward operation allowed");
        }
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public void forward()
    {
        try
        {
            String nextURL = history.forward();
            showPage(nextURL, false, true);
        } catch (EmptyStackException e)
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "No forward operation allowed");
        }
    }

    
    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public void close()
    {
        setVisible(false);
    }
    
    public void setStartedAsApplet(boolean aStartedAsApplet) {
	}

	public void setCodeBase(URL aCodeBase) {
	}
	
	/**
     * Method transformHyperlinkURL 
     * 
     * @param prevUrl
     *            URL of the page (including path) that has been displayed before
     * @param nextDocument
     *            String value of the document that should be loaded next
     */
	private String transformHyperlinkURL(String prevUrl, String nextDocument){ 
		String linkedURL = prevUrl + nextDocument;
		
		String path = prevUrl;
		int index = path.lastIndexOf("/");
		path= path.substring(0, index+1);
		
        if ((path.substring(0, 5)).equals("file:") || (path.substring(0, 4)).equals("jar:"))
        { 
        	if (path.substring(0, 5).equals("file:"))
        	{   
        		if (defaultLangPat != null)
        		{
        			path = path.replace("/en/", "/"+defaultLangPat+"/");
        		}
        		
        		linkedURL = path + nextDocument;
        		
        		File f= new File(linkedURL.substring(5));
        		if (!f.exists())
        		{
        			Pattern p = Pattern.compile("/");
        			String[] dirs = p.split(linkedURL);
        			
        			// Vorletzter teil ist die Sprache
        			defaultLangPat = dirs[dirs.length - 2];
        			dirs[dirs.length - 2] = "en";
        			
        			linkedURL = dirs[0];
        			for (int i = 1; i < dirs.length; i++)
        				linkedURL = linkedURL + "/" + dirs[i];
        		}	
        	}
            // Local link, open in helpbrowser
            //showPage(linkedURL, false, false);
            
        }
        return linkedURL;
	}
	
	private void createScene() {  
		//Creation of browser to display help
        PlatformImpl.startup(new Runnable() {  
            public void run() {  
                stage = new Stage();  
                 
                stage.setTitle("WoPeD");  
                stage.setResizable(true);  
   
                Group root = new Group();  
                Scene scene = new Scene(root, 640, 480);  //1040, 830);//
                stage.setScene(scene);  
                 
                // Set up the embedded browser:
                browser = new WebView();
                webEngine = browser.getEngine();
                
                ObservableList<Node> children = root.getChildren();
                children.add(browser);                     
                 
                jfxPanel.setScene(scene);  
                
                //Listener when engine loads page
                webEngine.getLoadWorker().stateProperty().addListener(
                        new ChangeListener<State>() {
                            public void changed(ObservableValue ov, State oldState, State newState) {
                            	if(webEngine.getDocument() != null){
                            		//Listener for hyperlinks
                            		EventListener listener = new EventListener() {
                            			public void handleEvent(Event ev) {
                            				String nextURL = ((Element)ev.getTarget()).getAttribute("href");
                            				
                            				if(!nextURL.startsWith("http") && nextURL.endsWith(".htm")){
                            					//New htm document should be opened
                            					if(webEngine.getDocument() != null){
                            						String lastURL = webEngine.getDocument().getDocumentURI();
                            						//previous URL for path, next URL for document that needs to be loaded
                            						nextURL = transformHyperlinkURL(lastURL, nextURL);
                            					}
                            				}
                           					showPage(nextURL, false, false);
                           					  					
                            			}
                            		};
                            		
                            		//Set hyperlink listener to all link elements
                            		NodeList list = webEngine.getDocument().getElementsByTagName("a");
                            		for (int i=0; i<list.getLength(); i++){
                            			((EventTarget)list.item(i)).addEventListener("click", listener, false);
                            		}
                            		
                            		//New page loaded
                            		if(oldState == State.RUNNING && newState == State.SUCCEEDED){
                            			String doc = webEngine.getDocument().getDocumentURI();
                            			
                            			if(doc.contains("?") && !doc.startsWith("http")){
                            				//Add pages to history which were loaded through jquery and not through hyperlinks
                            				history.add(doc);
                            				getToolbar().getBackButton().setEnabled(history.canBack());
                                            getMenubar().getBackMenuItem().setEnabled(history.canBack());
                                            getToolbar().getForwardButton().setEnabled(history.canForward());
                                            getMenubar().getForwardMenuItem().setEnabled(history.canForward());
                                            currURL = doc;
                            			}

                            		}
                            	}
                            	
                            }
                        });
            }  
        });  
    } 
}

