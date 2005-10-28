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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
/*
 * Created on Jan 20, 2005 @author Thomas Freytag
 *  
 */

package org.woped.gui.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.EmptyStackException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

import org.woped.core.utilities.LoggerManager;
import org.woped.gui.Constants;
import org.woped.gui.action.help.LaunchDefaultBrowserAction;

/**
 * @author <a href="mailto:freytag@ba-karlsruhe.de">Thomas Freytag </a> <br>
 * 
 * TODO: DOCUMENTATION (tfreytag)
 */
public class HelpBrowser extends JFrame implements HyperlinkListener
{
    public static HelpBrowser c_instance = null;

    private JTextField        urlField;
    private JEditorPane       htmlPane;
    private JLabel            statusBarInfo;
    private String            currURL;
    private String            homeURL;
    private String            contentsURL;
    private BrowserHistory    history    = new BrowserHistory();

    public static HelpBrowser getInstance()
    {
        if (c_instance == null) c_instance = new HelpBrowser();
        return c_instance;
    }

    private HelpBrowser()
    {
        super("WoPeD Helpbrowser");

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            LoggerManager.error(Constants.GUI_LOGGER, "Error setting Look and Feel: " + e);
        }

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel jp1 = new JPanel(new BorderLayout());
        jp1.add(getMenubar(), BorderLayout.CENTER);
        topPanel.add(jp1, BorderLayout.NORTH);
        JPanel jp2 = new JPanel(new BorderLayout());
        jp2.add(getToolbar(), BorderLayout.CENTER);
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
    public void init(String currURL, String homeURL, String contentsURL)
    {
        this.homeURL = homeURL;
        this.currURL = currURL;
        this.contentsURL = contentsURL;
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
        if (url.substring(0, 6).equals("file:/") | url.substring(0, 4).equals("jar:")) url = new File(url).getName();
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

                getToolbar().getBackButton().setEnabled(history.canBack());
                getMenubar().getBackMenuItem().setEnabled(history.canBack());
                getToolbar().getForwardButton().setEnabled(history.canForward());
                getMenubar().getForwardMenuItem().setEnabled(history.canForward());
                currURL = url;
            }
        } catch (IOException ioe)
        {
            LoggerManager.error(Constants.GUI_LOGGER, "Can't open URL " + url + ": " + ioe);
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
            LoggerManager.warn(Constants.GUI_LOGGER, "No backward operation allowed");
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
            LoggerManager.warn(Constants.GUI_LOGGER, "No forward operation allowed");
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

            if ((linkedURL.substring(0, 5)).equals("file:") | (linkedURL.substring(0, 4)).equals("jar:"))
            {
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
}