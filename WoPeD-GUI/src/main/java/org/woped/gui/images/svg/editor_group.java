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
public class editor_group implements
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1004.3621826171875f));
// _0_0
g.setComposite(AlphaComposite.getInstance(3, 0.35f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0909091234207153f, 0.0f, 0.0f, 1.3333332538604736f, -1.1401646137237549f, 999.2911376953125f));
// _0_0_0
paint = new Color(0, 0, 0, 160);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.0, 34.5);
((GeneralPath)shape).curveTo(40.0, 35.328426, 32.612698, 36.0, 23.5, 36.0);
((GeneralPath)shape).curveTo(14.387301, 36.0, 7.0, 35.328426, 7.0, 34.5);
((GeneralPath)shape).curveTo(7.0, 33.671574, 14.387301, 33.0, 23.5, 33.0);
((GeneralPath)shape).curveTo(32.612698, 33.0, 40.0, 33.671574, 40.0, 34.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
paint = new LinearGradientPaint(new Point2D.Double(10.454312324523926, 1022.3546752929688), new Point2D.Double(30.08074378967285, 1022.3546752929688), new float[] {0.0f,1.0f}, new Color[] {new Color(236, 236, 236, 255),new Color(236, 236, 236, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8927791118621826f, 0.0f, 0.0f, 0.9022687077522278f, 2.312424659729004f, 101.26888275146484f));
shape = new Rectangle2D.Double(11.54828929901123, 1014.1339721679688, 17.3599853515625, 18.077281951904297);
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 8, 36, 255);
stroke = new BasicStroke(0.5219833f,0,2,4.0f,null,0.0f);
shape = new Rectangle2D.Double(11.54828929901123, 1014.1339721679688, 17.3599853515625, 18.077281951904297);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(0.0826527401804924f, 0.0f, 0.0f, 0.09157008677721024f, -3.4443540573120117f, 992.8811645507812f));
// _0_0_2
paint = new Color(249, 249, 249, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(521.46387, 429.50504);
((GeneralPath)shape).curveTo(521.46387, 483.15555, 476.03696, 526.6479, 420.0, 526.6479);
((GeneralPath)shape).curveTo(363.96304, 526.6479, 318.53613, 483.15555, 318.53613, 429.50504);
((GeneralPath)shape).curveTo(318.53613, 375.85452, 363.96304, 332.36218, 420.0, 332.36218);
((GeneralPath)shape).curveTo(476.03696, 332.36218, 521.46387, 375.85452, 521.46387, 429.50507);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(6.0f,0,2,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(521.46387, 429.50504);
((GeneralPath)shape).curveTo(521.46387, 483.15555, 476.03696, 526.6479, 420.0, 526.6479);
((GeneralPath)shape).curveTo(363.96304, 526.6479, 318.53613, 483.15555, 318.53613, 429.50504);
((GeneralPath)shape).curveTo(318.53613, 375.85452, 363.96304, 332.36218, 420.0, 332.36218);
((GeneralPath)shape).curveTo(476.03696, 332.36218, 521.46387, 375.85452, 521.46387, 429.50507);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
paint = new Color(0, 107, 70, 255);
stroke = new BasicStroke(0.5219833f,0,2,4.0f,new float[] {1.5659502f,1.5659502f},0.0f);
shape = new Rectangle2D.Double(6.946263313293457, 1011.2816162109375, 34.4780158996582, 34.53501510620117);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_3);
g.setTransform(defaultTransform__0_0);
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
        return 7;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 45;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 42;
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
	public editor_group() {
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

