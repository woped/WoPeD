package org.woped.qualanalysis.coverabilitygraph.gui;

import org.jgraph.JGraph;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.LoggerManager;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

class ImageExport {
	
	private final static int 	MAX_WIDTH			= 10000;
	private final static int	MAX_HEIGHT			= 10000;

    static RenderedImage getRenderedImage(CoverabilityGraphVC editor) {
	JGraph graph = editor.getGraph();
        Object[] cells = graph.getRoots();
        BufferedImage image = null;
        
        if (cells.length > 0) {
        	graph.validate();
            Rectangle2D rectangle = graph.getCellBounds(cells);
			graph.setGridVisible(false);
            graph.toScreen(rectangle);

            Dimension dimension = rectangle.getBounds().getSize();
            // Check Dimension, if max size reached, let user know about this and resize graph
            if(dimension.width < MAX_WIDTH && dimension.height < MAX_HEIGHT)
            {
            	image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
            	Graphics2D graphics = image.createGraphics();
                graphics.translate(-rectangle.getX(), -rectangle.getY());
                graph.paint(graphics);

                graph.setGridVisible(ConfigurationManager.getConfiguration().isShowGrid());   
            }
            else
            {
//            	ReferenceProvider mediator = new ReferenceProvider();
            	JOptionPane.showMessageDialog(null,
    					Messages.getString("QuanlAna.ReachabilityGraph.ExportFailed"), Messages
    							.getString("QuanlAna.ReachabilityGraph.ExportFailed.Title"),
    					JOptionPane.ERROR_MESSAGE);
                double g2dWidth = dimension.width;
                double g2dHeight = dimension.height;
				double xScaleFactor = (double) MAX_WIDTH / g2dWidth;
                double yScaleFactor = (double) MAX_HEIGHT / g2dHeight;
            	image = new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                graphics.translate(-rectangle.getX(), -rectangle.getY());
                graphics.scale(xScaleFactor, yScaleFactor);
                graph.paint(graphics);

                graph.setGridVisible(ConfigurationManager.getConfiguration().isShowGrid()); 
            }
                     
        }
        
        return image;
    }
    
    static boolean saveJPG(RenderedImage image, File file) {
    	if(image != null)
    	{
    		try {
    			ImageIO.write(image, "jpg", file);
    			LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "File saved to: " + file.getAbsolutePath());
    			return true;
    		} catch (IOException e) {
    			e.printStackTrace();
    			LoggerManager.error(Constants.QUALANALYSIS_LOGGER, "Could not export as JPG");
    			return false;
    		}
    	}
    	else
    	{
    		return false;
    	}
    }
    
    static boolean savePNG(RenderedImage image, File file) {
	if(image != null){
		try {
		    ImageIO.write(image, "png", file);
		    LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "File saved to: " + file.getAbsolutePath());
		    return true;
		} catch (IOException e) {
		    e.printStackTrace();
		    LoggerManager.error(Constants.QUALANALYSIS_LOGGER, "Could not export as PNG");
		    return false;
		}
	}
	else{
		return false;
	}
    }
    
    static boolean saveBMP(RenderedImage image, File file) {
	if(image != null){
		try {
		    ImageIO.write(image, "bmp", file);
		    LoggerManager.info(Constants.QUALANALYSIS_LOGGER, "File saved to: " + file.getAbsolutePath());
		    return true;
		} catch (IOException e) {
		    e.printStackTrace();
		    LoggerManager.error(Constants.QUALANALYSIS_LOGGER, "Could not export as BMP");
		    return false;
		}
	}
	else{
		return false;
	}
    }
}
