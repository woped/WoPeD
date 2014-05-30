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

import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;

import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 * 
 * TODO: DOCUMENTATION (tfreytag)
 */

@SuppressWarnings("serial")
public class HelpBrowser
{	      
	    
	    
    public static HelpBrowser c_instance = null;

    private String            currURL;
    private String            homeURL;
    private String            contentsURL;
    private String			  defaultLangPat;

    public static HelpBrowser getInstance()
    {
        if (c_instance == null) c_instance = new HelpBrowser();
        return c_instance;
    }

    private HelpBrowser()
    {
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
    	
    	if (currFileName == null){
        	currFileName = Messages.getString("Help.File.Index");
    	}
   	
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
			//locate HTML files in local folder
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
        try {
        	new LaunchDefaultBrowserAction(url, null).displayURL();
        } catch (Exception ioe) {
            LoggerManager.error(Constants.EDITOR_LOGGER, "Can't open URL " + url + ": " + ioe);
        }
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
}

