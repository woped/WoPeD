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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Some static Stuff.
 *  
 */
public class Utils
{

    /**
     * This method returns the location that should place a component with
     * <code>componentDimension</code> in the center of the owner component (
     * <code>ownerBounds</code>).
     * 
     * @param ownerBounds
     * @param componentDimension
     * @return
     */
    public static Point getCenterPoint(Rectangle ownerBounds, Dimension componentDimension)
    {
        int middleX = (int) (ownerBounds.getX() + (ownerBounds.getWidth() * 0.5));
        int middleY = (int) (ownerBounds.getY() + (ownerBounds.getHeight() * 0.5));
        return new Point(middleX - (int) (componentDimension.getWidth() * 0.5), middleY - (int) (componentDimension.getHeight() * 0.5));
    }

    /**
     * Checks a filename for its extension and adds if necessary the standard
     * extension. <br>(= The first extension in the String Array)
     * 
     * @param fileName
     * @param extensions
     * @return
     */
    public static String getQualifiedFileName(String fileName, Vector<?> extensions)
    {

        boolean extensionFound = false;
        // must be at least 5 letters
        if (fileName.length() >= 5)
        {
            // for each extension
            for (int i = 0; i < extensions.size(); i++)
            {
                if (fileName.substring(fileName.length() - extensions.get(i).toString().length(), fileName.length()).equals(extensions.get(i).toString()))
                {
                    extensionFound = true;
                    // logger.debug("Extension found: " + extensions[i]);
                    break;
                }
            }
        }
        if (!extensionFound && extensions.get(0).toString() != null) fileName += "." + extensions.get(0).toString();
        return fileName;
    }

    /**
     * Sorts an Array so that Arcs are the first elements. TODO: move to Utils ?
     * 
     * @param toDelete
     * @return
     */
    public static Object[] sortArcsFirst(Object[] input)
    {
        if (input == null)
        {
            return null;
        }
        Object[] result = new Object[input.length];
        int pointer = 0;
        for (int i = 0; i < input.length; i++)
        {
            if (input[i] instanceof ArcModel)
            {
                result[pointer] = input[i];
                pointer++;
            }
        }
        for (int i = 0; i < input.length; i++)
        {
            if (!(input[i] instanceof ArcModel))
            {
                result[pointer] = input[i];
                pointer++;
            }
        }
        return result;
    }

    /**
     * Sorts an Array so that Arcs are the lest elements. TODO: move to Utils ?
     * 
     * @param toDelete
     * @return
     */
    public static Object[] sortArcsLast(Object[] input)
    {
        if (input == null)
        {
            return null;
        }
        Object[] result = new Object[input.length];
        int pointer = 0;
        for (int i = 0; i < input.length; i++)
        {
            if (!(input[i] instanceof ArcModel))
            {
                result[pointer] = input[i];
                pointer++;
            }
        }
        for (int i = 0; i < input.length; i++)
        {
            if (input[i] instanceof ArcModel)
            {
                result[pointer] = input[i];
                pointer++;
            }
        }
        return result;
    }

    /**
     * TODO: DOCUMENTATION (alexnagy)
     * 
     * @param editor
     */
    public static void print(IEditor editor)
    {
        AbstractGraph graph = editor.getGraph();
        if (graph != null)
        {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(graph);
            PageFormat oldPage = new PageFormat();
            PageFormat pageFormat = printJob.pageDialog(oldPage);
            if (oldPage != pageFormat)
            {
                if (printJob.printDialog())
                {
                    try
                    {
                        printJob.print();
                    } catch (PrinterException e)
                    {
                        //                        logger.warn("Could not Print");
                        //                        logger.debug("Exception", e);
                    }
                }
            }
        }
    }

}