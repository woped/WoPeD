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
package org.woped.editor.utilities;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import org.apache.log4j.Logger;

/* Comment:
 * Originally placed in class Editor, this is now its own class.
 * Alexis Nagy, 12.12.2004
 */
/**
 * Encapsulates an awt Image for Data Transfer to/from the clipboard. <br>
 * TODO: Documentation (xraven)
 * 
 * @author Thomas Pohl
 */
public class ImageSelection implements Transferable
{

    public static DataFlavor imageFlavor      = new DataFlavor("image/x-java-image; class=java.awt.Image", "Image");
    private DataFlavor[]     supportedFlavors = { imageFlavor };
    // the diagram image data
    private Image            diagramImage;

    /**
     * Constructor for Image Selection
     * 
     * @param newDiagramImage
     *            the Image to contain.
     * 
     * @param newDiagramImage
     */
    public ImageSelection(Image newDiagramImage)
    {
        diagramImage = newDiagramImage;
    }

    /**
     * Gets the DataFlavors used for transfer.
     * 
     * @return image/x-java-image; class=java.awt.Image
     */
    public synchronized DataFlavor[] getTransferDataFlavors()
    {
        return (supportedFlavors);
    }

    /**
     * Checks wether the passed Flavor is supported.
     */
    public boolean isDataFlavorSupported(DataFlavor parFlavor)
    {
        return (parFlavor.getMimeType().equals(imageFlavor.getMimeType()) && parFlavor.getHumanPresentableName().equals(imageFlavor.getHumanPresentableName()));
    }

    /**
     * Returns the Transfered data. Thats the image.
     */
    public synchronized Object getTransferData(DataFlavor parFlavor) throws UnsupportedFlavorException
    {
        if (isDataFlavorSupported(parFlavor))
        {
            return (diagramImage);
        } else
        {
            Logger.getInstance("PWTLogger").debug("Unsoported Flavor");
            throw new UnsupportedFlavorException(imageFlavor);
        }
    }
}