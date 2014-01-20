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
import java.io.IOException;
import java.net.URL;
import java.util.EmptyStackException;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityRibbonVC;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 *
 * TODO: DOCUMENTATION (tfreytag)
 */

@SuppressWarnings("serial")
public class HelpBrowser extends JFrame implements HyperlinkListener
{
    public static HelpBrowser c_instance = null;

    private JEditorPane       htmlPane;
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
        JRibbon ribbon = HelpRibbonMenu.getInstance(this);
        //hide taskbar
        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(BorderFactory.createEmptyBorder(-25, 0, 0, 0));
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(ribbon,BorderLayout.CENTER);
        JPanel jp2 = new JPanel(new BorderLayout());
        jp2.add(containerPanel, BorderLayout.CENTER);
        topPanel.add(jp2, BorderLayout.SOUTH);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screenSize.width > 960 ? screenSize.width - 960 : 0, 40, screenSize.width > 720 ? 720 : screenSize.width, screenSize.height > 760 ? 720 : screenSize.height);

        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.addHyperlinkListener(this);
        htmlPane.setEditable(false);
        getContentPane().add(new JScrollPane(htmlPane), BorderLayout.CENTER);

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
    private HelpRibbonMenu getRibbon()
    {
        return HelpRibbonMenu.getInstance(this);
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
    private void showPage(String url, boolean justRefresh, boolean fromHistory)
    {
        try
        {
            htmlPane.setPage(url);
            setStatusBarInfo(url);

            if (!justRefresh)
            {
                if (!fromHistory)
                {
                    history.add(url);
                }

                getRibbon().getBackButton().setEnabled(history.canBack());
                getRibbon().getForwardButton().setEnabled(history.canForward());

                currURL = url;
            }
        } catch (IOException ioe)
        {
            LoggerManager.error(Constants.EDITOR_LOGGER, "Can't open URL " + url + ": " + ioe);
        }
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public void refresh()
    {
        Document doc = htmlPane.getDocument();
        doc.putProperty(Document.StreamDescriptionProperty, null);
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
     *
     * @param event
     */
    public void hyperlinkUpdate(HyperlinkEvent event)
    {
        if (event.getEventType() == HyperlinkEvent.EventType.ENTERED)
        {
            setStatusBarInfo(event.getURL().toExternalForm());
        }

        if (event.getEventType() == HyperlinkEvent.EventType.EXITED)
        {
            setStatusBarInfo(currURL);
        }

        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
            String linkedURL = event.getURL().toExternalForm();

            if ((linkedURL.substring(0, 5)).equals("file:") || (linkedURL.substring(0, 4)).equals("jar:"))
            {
            	if (linkedURL.substring(0, 5).equals("file:"))
            	{
            		if (defaultLangPat != null)
            		{
            			linkedURL=linkedURL.replace("/en/", "/"+defaultLangPat+"/");
            		}
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
                showPage(linkedURL, false, false);
            } else
            {
                // External WWW link, open in default Webbrowser
                LaunchDefaultBrowserAction lda = new LaunchDefaultBrowserAction(linkedURL, null);
                lda.displayURL();
            }
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
}