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
package org.woped.file;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 13.05.2003
 */
public class JPGExport
{

    // Create a buffered image of the specified graph.
    public static boolean save(String absFileName, EditorVC editor)
    {
        JGraph graph = editor.getGraph();
        graph.clearSelection();
        Object[] cells = graph.getRoots();
        if (cells.length > 0)
        {
            Rectangle2D bounds = graph.getCellBounds(cells);

            graph.setGridVisible(false);
            graph.toScreen(bounds);

            // Create a Buffered Image
            Dimension d = bounds.getBounds().getSize();
            BufferedImage img = new BufferedImage(d.width + 10, d.height + 10, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            graphics.translate(-bounds.getX() + 10, -bounds.getY() + 10);
            graph.paint(graphics);

            graph.setGridVisible(ConfigurationManager.getConfiguration().isShowGrid() & !editor.isTokenGameMode());

            try
            {
                FileOutputStream fos = new FileOutputStream(new File(absFileName));
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
                encoder.encode(img);
                fos.flush();
                fos.close();
                LoggerManager.info(Constants.FILE_LOGGER, "File saved to: " + absFileName);
                return true;
            } catch (Exception e)
            {
                e.printStackTrace();
                LoggerManager.error(Constants.FILE_LOGGER, "Could not export as JPG");
                return false;
            }
        }
        return false;
    }
}