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
package org.woped.core.utilities;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;


/**
 * "Generic" file filter. Accepts any file extension.
 * 
 * @author <a href="mailto:ricardo_padilha@users.sourceforge.net">Ricardo Sangoi
 *         Padilha </a>
 */
public class FileFilterImpl extends javax.swing.filechooser.FileFilter 
{

    //private String ext;
    private String          desc;
    private Hashtable<String, FileFilterImpl>       exts;
    //private String fullDescription = null;
    private int             type;
    public final static int JPGFilter     = 1;
    public final static int PNMLFilter    = 2;
    public final static int TPNFilter     = 3;
    public final static int SAMPLEFilter  = 4;
    public final static int FOLDERFilter  = 5;
    public final static int XMLFilter     = 6;
    public final static int OLDPNMLFilter = 7;
    public final static int PNGFilter 	  = 8;
    public final static int BMPFilter     = 9;
    public final static int BPELFilter    = 10;
    public final static int JPGBWFilter	  = 11;
    public final static int PNGBWFilter   = 12;
    public final static int BMPBWFilter   = 13;
    
    public final static int YAWLFilter    = 14;
    

    public FileFilterImpl(int type, String descritption)
    {
        this.type = type;
        desc = descritption;
    }

    /**
     *  
     */
    public FileFilterImpl(int type, String description, String extension)
    {

        this.type = type;
        desc = description;
        //this.ext = extension;
        addExtension(extension);
    }

    /**
     *  
     */
    public FileFilterImpl(int type, String description, Vector<?> extensions)
    {

        this.type = type;
        desc = description;
        for (int i = 0; i < extensions.size(); i++)
        {
            // add filters one by one
            addExtension(extensions.get(i).toString());
        }
    }

    /**
     *  
     */
    public boolean accept(File f)
    {
        if (f != null)
        {
            if (f.isDirectory())
            {
                return true;
            }
            String extension = getFileExtension(f);
            if (type == FOLDERFilter && f.isDirectory())
            {
                return true;
            } else if (exts != null && extension != null && exts.get(extension) != null)
            {
                return true;
            }
            ;
        }
        return false;
    }

    /**
     * Return the extension portion of the file's name .
     */
    public String getFileExtension(File f)
    {
        if (f != null)
        {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1)
            {
                return filename.substring(i + 1).toLowerCase();
            }
            ;
        }
        return null;
    }

    /**
     *  
     */
    public java.lang.String getDescription()
    {
        return desc;
    }

    /**
     * Adds a filetype "dot" extension to filter against.
     * 
     * For example: the following code will create a filter that filters out all
     * files except those that end in ".jpg" and ".tif":
     * 
     * ExampleFileFilter filter = new ExampleFileFilter();
     * filter.addExtension("jpg"); filter.addExtension("tif");
     * 
     * Note that the "." before the extension is not needed and will be ignored.
     */
    public void addExtension(String extension)
    {
        if (exts == null)
        {
            exts = new Hashtable<>(5);
        }
        exts.put(extension.toLowerCase(), this);
        //fullDescription = null;
    }

    /**
     * 
     *  
     */
    public int getFilterType()
    {

        return type;

    }

    /**
     * Returns the default file extensions.
     *
     * @return the default file extension
     */
    public String getDefaultExtension(){
        if(exts.isEmpty()) throw new IllegalStateException("There is no file extension registered.");

        // return any of the extensions
        return exts.keySet().iterator().next();
    }

}