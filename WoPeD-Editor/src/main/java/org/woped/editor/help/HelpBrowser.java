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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 *     TODO: DOCUMENTATION (tfreytag)
 */
public class HelpBrowser {

  public static HelpBrowser c_instance = null;

  public static HelpBrowser getInstance() {
    if (c_instance == null) c_instance = new HelpBrowser();
    return c_instance;
  }

  private HelpBrowser() {}

  public void showURL(String currFileName) throws UnsupportedEncodingException {
    String helpDir = Messages.getString("Help.Dir");
    String contentFileName = Messages.getString("Help.File.Contents");
    String indexFileName = Messages.getString("Help.File.Index");
    String docPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

    if (currFileName == null) {
      currFileName = Messages.getString("Help.File.Index");
    }

    int pos = docPath.lastIndexOf("/");
    docPath = docPath.substring(0, pos) + "/doc";

    docPath = URLDecoder.decode(docPath, "utf-8");
    docPath = new File(docPath).getPath();

    if (!new File(docPath).exists()) {
      // locate HTML help files in local folder
      docPath = new File(".").getAbsolutePath();
      pos = docPath.lastIndexOf(".") - 1;
      docPath = docPath.substring(0, pos) + "/WoPeD-Starter/doc";
    }

    docPath = "file:" + docPath;
    currFileName = docPath + "/" + helpDir + currFileName;
    contentFileName = docPath + "/" + helpDir + contentFileName;
    indexFileName = docPath + "/" + helpDir + indexFileName;

    new LaunchDefaultBrowserAction(currFileName, null).displayURL();
  }
}
