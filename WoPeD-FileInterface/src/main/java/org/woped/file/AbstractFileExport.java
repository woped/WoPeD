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
package org.woped.file;

import org.woped.core.utilities.FileFilterImpl;

/**
 * @author lai
 * 
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu
 * Ändern: Fenster > Benutzervorgaben > Java > Codegenerierung > Code und
 * Kommentare
 */
public abstract class AbstractFileExport
{

    String absolutePath = null;
    String fileName     = null;
    int    type         = -1;

    public AbstractFileExport(Object Data, String absoluteFilePath, String fileName, int FileFilterType)
    {
        this.absolutePath = absoluteFilePath;
        this.fileName = fileName;
        this.type = FileFilterType;
    }

    public String getPathWithExtension()
    {
        String savePath = absolutePath.substring(0, absolutePath.length() - fileName.length());
        return savePath + getQualifiedFileName(fileName, type);

    }

    public String getQualifiedFileName(String fileName, int type)
    {

        if (type == FileFilterImpl.JPGFilter)
        {

            if (fileName.length() >= 5)
            {
                if (!(fileName.substring(fileName.length() - 4, fileName.length()).equals(".jpg")) && !(fileName.substring(fileName.length() - 5, fileName.length()).equals(".jpeg")))
                {
                    fileName = fileName + ".jpg";
                }
            } else fileName = fileName + ".jpg";
        } else if (type == FileFilterImpl.PNGFilter)
        {

            if (fileName.length() >= 5)
            {
                if (!(fileName.substring(fileName.length() - 4, fileName.length()).equals(".png")))
                {
                    fileName = fileName + ".png";
                }
            } else fileName = fileName + ".png";
        } else if (type == FileFilterImpl.BMPFilter)
        {

            if (fileName.length() >= 5)
            {
                if (!(fileName.substring(fileName.length() - 4, fileName.length()).equals(".bmp")))
                {
                    fileName = fileName + ".bmp";
                }
            } else fileName = fileName + ".bmp";
        } else if (type == FileFilterImpl.PNMLFilter)
        {

            if (fileName.length() >= 5)
            {
                if (!(fileName.substring(fileName.length() - 4, fileName.length()).equals(".xml")) && !(fileName.substring(fileName.length() - 5, fileName.length()).equals(".pnml")))
                {
                    fileName = fileName + ".pnml";
                }
            } else
            {
                fileName = fileName + ".pnml";
            }
        }
        return fileName;

    }

}