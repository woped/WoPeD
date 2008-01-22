package org.woped.qualanalysis;

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
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;

public class ImageExport {

    public static RenderedImage getRenderedImage(ReachabilityGraphVC editor) {
	JGraph graph = editor.getActualJGraph();
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

            graph.setGridVisible(ConfigurationManager.getConfiguration().isShowGrid());            
        }
        
        return image;
    }
    
    public static boolean saveJPG(RenderedImage image, File file) {
	try {
	    ImageIO.write((RenderedImage) image, "jpg", file);
	    LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "File saved to: " + file.getAbsolutePath());
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    LoggerManager.error(Constants.QUALANALYSIS_LOGGER, "Could not export as JPG");
	    return false;
	}
    }
    
    public static boolean savePNG(RenderedImage image, File file) {
	try {
	    ImageIO.write((RenderedImage) image, "png", file);
	    LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "File saved to: " + file.getAbsolutePath());
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    LoggerManager.error(Constants.QUALANALYSIS_LOGGER, "Could not export as PNG");
	    return false;
	}
    }
    
    public static boolean saveBMP(RenderedImage image, File file) {
	try {
	    ImageIO.write((RenderedImage) image, "bmp", file);
	    LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "File saved to: " + file.getAbsolutePath());
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    LoggerManager.error(Constants.QUALANALYSIS_LOGGER, "Could not export as BMP");
	    return false;
	}
    }
}