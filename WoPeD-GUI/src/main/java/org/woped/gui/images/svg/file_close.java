package org.woped.gui.images.svg;



import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class file_close implements
		org.pushingpixels.flamingo.api.common.icon.ResizableIcon {
	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * 
	 * @param g
	 *            Graphics context.
	 */
	public static void paint(Graphics2D g) {
        Shape shape = null;
        Paint paint = null;
        Stroke stroke = null;
         
        float origAlpha = 1.0f;
        Composite origComposite = ((Graphics2D)g).getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = 
                (AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }
        
	    AffineTransform defaultTransform_ = g.getTransform();
// 
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
g.setComposite(AlphaComposite.getInstance(3, 0.6f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0705499649047852f, 0.0f, 0.0f, 0.5249999761581421f, -0.8927549719810486f, 22.5f));
// _0_0_0
paint = new RadialGradientPaint(new Point2D.Double(23.85714340209961, 40.0), 17.142857f, new Point2D.Double(23.85714340209961, 40.0), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 20.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(41.0, 40.0);
((GeneralPath)shape).curveTo(41.0, 44.733868, 33.324883, 48.571426, 23.857143, 48.571426);
((GeneralPath)shape).curveTo(14.389405, 48.571426, 6.714287, 44.733868, 6.714287, 40.0);
((GeneralPath)shape).curveTo(6.714287, 35.266132, 14.389405, 31.428572, 23.857143, 31.428572);
((GeneralPath)shape).curveTo(33.324883, 31.428572, 41.0, 35.266132, 41.0, 40.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0);
g.setTransform(defaultTransform__0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0_0 = g.getTransform();
g.transform(new AffineTransform(0.9204879999160767f, 0.0f, 0.0f, 0.9204879999160767f, 2.368530035018921f, 0.9740800261497498f));
// _0_1_0_0
paint = new LinearGradientPaint(new Point2D.Double(36.91797637939453, 66.2880630493164), new Point2D.Double(19.071495056152344, 5.541010856628418), new float[] {0.0f,1.0f}, new Color[] {new Color(164, 0, 0, 255),new Color(255, 23, 23, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(46.857143, 23.928572);
((GeneralPath)shape).curveTo(46.857143, 36.828365, 36.399796, 47.285713, 23.5, 47.285713);
((GeneralPath)shape).curveTo(10.600206, 47.285713, 0.1428566, 36.828365, 0.1428566, 23.928572);
((GeneralPath)shape).curveTo(0.1428566, 11.028778, 10.600206, 0.5714283, 23.5, 0.5714283);
((GeneralPath)shape).curveTo(36.399796, 0.5714283, 46.857143, 11.028778, 46.857143, 23.928572);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(178, 0, 0, 255);
stroke = new BasicStroke(1.08638f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(46.857143, 23.928572);
((GeneralPath)shape).curveTo(46.857143, 36.828365, 36.399796, 47.285713, 23.5, 47.285713);
((GeneralPath)shape).curveTo(10.600206, 47.285713, 0.1428566, 36.828365, 0.1428566, 23.928572);
((GeneralPath)shape).curveTo(0.1428566, 11.028778, 10.600206, 0.5714283, 23.5, 0.5714283);
((GeneralPath)shape).curveTo(36.399796, 0.5714283, 46.857143, 11.028778, 46.857143, 23.928572);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_1_0_0);
g.setComposite(AlphaComposite.getInstance(3, 0.34659088f * origAlpha));
AffineTransform defaultTransform__0_1_0_1 = g.getTransform();
g.transform(new AffineTransform(0.8560929894447327f, 0.0f, 0.0f, 0.8560929894447327f, 1.818269968032837f, 0.19776900112628937f));
// _0_1_0_1
paint = new Color(204, 0, 0, 0);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(49.901535, 26.635273);
((GeneralPath)shape).curveTo(49.901535, 39.885204, 39.160343, 50.626396, 25.910412, 50.626396);
((GeneralPath)shape).curveTo(12.6604805, 50.626396, 1.9192886, 39.885204, 1.9192886, 26.635273);
((GeneralPath)shape).curveTo(1.9192886, 13.385342, 12.6604805, 2.6441498, 25.910412, 2.6441498);
((GeneralPath)shape).curveTo(39.160343, 2.6441498, 49.901535, 13.385342, 49.901535, 26.635273);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(43.93581008911133, 53.83598327636719), new Point2D.Double(20.064685821533203, -8.562670707702637), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 230, 155, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.1680961f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(49.901535, 26.635273);
((GeneralPath)shape).curveTo(49.901535, 39.885204, 39.160343, 50.626396, 25.910412, 50.626396);
((GeneralPath)shape).curveTo(12.6604805, 50.626396, 1.9192886, 39.885204, 1.9192886, 26.635273);
((GeneralPath)shape).curveTo(1.9192886, 13.385342, 12.6604805, 2.6441498, 25.910412, 2.6441498);
((GeneralPath)shape).curveTo(39.160343, 2.6441498, 49.901535, 13.385342, 49.901535, 26.635273);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_1_0_1);
g.setTransform(defaultTransform__0_1_0);
g.setTransform(defaultTransform__0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_0 = g.getTransform();
g.transform(new AffineTransform(0.7071070075035095f, -0.7071070075035095f, 0.7071070075035095f, 0.7071070075035095f, 0.0f, 0.0f));
// _0_2_0
paint = new Color(239, 239, 239, 255);
shape = new Rectangle2D.Double(-13.292923927307129, 30.234006881713867, 28.000001907348633, 6.000087261199951);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_1 = g.getTransform();
g.transform(new AffineTransform(0.7071070075035095f, 0.7071070075035095f, -0.7071070075035095f, 0.7071070075035095f, 0.0f, 0.0f));
// _0_2_1
paint = new Color(239, 239, 239, 255);
shape = new Rectangle2D.Double(19.23404884338379, -3.707120180130005, 28.000001907348633, 6.000087261199951);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_1);
g.setTransform(defaultTransform__0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_3_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_0
paint = new LinearGradientPaint(new Point2D.Double(21.993772506713867, 33.955299377441406), new Point2D.Double(20.917078018188477, 15.81460189819336), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 254, 255, 85),new Color(255, 254, 255, 55)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0029900074005127f, 0.0f, 0.0f, 1.0029900074005127f, -0.07185900211334229f, 0.0196835994720459f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(43.42868, 21.800186);
((GeneralPath)shape).curveTo(43.42868, 32.66323, 33.04335, 15.515116, 24.698029, 22.18773);
((GeneralPath)shape).curveTo(16.547377, 28.704695, 4.039397, 34.414776, 4.039397, 23.551735);
((GeneralPath)shape).curveTo(4.039397, 12.434496, 12.760828, 2.1207585, 23.623875, 2.1207585);
((GeneralPath)shape).curveTo(34.48692, 2.1207585, 43.42868, 10.937141, 43.42868, 21.800186);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_3_0);
g.setTransform(defaultTransform__0_3);
g.setTransform(defaultTransform__0);
g.setTransform(defaultTransform_);

	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 3;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 2;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 44;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 47;
	}

	/**
	 * The current width of this resizable icon.
	 */
	int width;

	/**
	 * The current height of this resizable icon.
	 */
	int height;

	/**
	 * Creates a new transcoded SVG image.
	 */
	public file_close() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
    @Override
	public int getIconHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
    @Override
	public int getIconWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.common.icon.ResizableIcon#setDimension(java.awt.Dimension
	 * )
	 */
	@Override
	public void setDimension(Dimension newDimension) {
		this.width = newDimension.width;
		this.height = newDimension.height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */
    @Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.translate(x, y);

		double coef1 = (double) this.width / (double) getOrigWidth();
		double coef2 = (double) this.height / (double) getOrigHeight();
		double coef = Math.min(coef1, coef2);
		g2d.scale(coef, coef);
		paint(g2d);
		g2d.dispose();
	}
}

