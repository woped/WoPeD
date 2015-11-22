package org.woped.file;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;

public class ImageExport {

    public static RenderedImage getRenderedImage(EditorVC editor) {
	JGraph graph = editor.getGraph();
        graph.clearSelection();
        Object[] cells = graph.getRoots();
        BufferedImage image = null;
        
        if (cells.length > 0) {
            Rectangle2D rectangle = graph.getCellBounds(cells);

            graph.setGridVisible(false);
            graph.toScreen(rectangle);

            // Create a Buffered Image
            Dimension dimension = rectangle.getBounds().getSize();
            image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.translate(-rectangle.getX(), -rectangle.getY());
            graph.paint(graphics);

            graph.setGridVisible(ConfigurationManager.getConfiguration().isShowGrid() 
        	    && !editor.isTokenGameEnabled());            
        }
        
        return image;
    }
    
    public static boolean saveJPG(RenderedImage image, File file) {
	try {
	    ImageIO.write(image, "jpg", file);
	    LoggerManager.info(Constants.FILE_LOGGER, "File saved to: " + file.getAbsolutePath());
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    LoggerManager.error(Constants.FILE_LOGGER, "Could not export as JPG");
	    return false;
	}
    }
    
    public static boolean savePNG(RenderedImage image, File file) {
	try {
	    ImageIO.write(image, "png", file);
	    LoggerManager.info(Constants.FILE_LOGGER, "File saved to: " + file.getAbsolutePath());
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    LoggerManager.error(Constants.FILE_LOGGER, "Could not export as PNG");
	    return false;
	}
    }
    
    public static boolean saveBMP(RenderedImage image, File file) {
	try {
	    ImageIO.write(image, "bmp", file);
	    LoggerManager.info(Constants.FILE_LOGGER, "File saved to: " + file.getAbsolutePath());
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    LoggerManager.error(Constants.FILE_LOGGER, "Could not export as BMP");
	    return false;
	}
    }
}
