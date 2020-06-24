package org.woped.gui.images.svg;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class Twitter_icon implements
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
g.transform(new AffineTransform(0.03979337587952614f, 0.0f, 0.0f, 0.03979337587952614f, 0.005731955998271587f, -0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0
paint = new Color(65, 171, 225, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6701.0, 645.0);
((GeneralPath)shape).curveTo(6454.0, 754.0, 6189.0, 828.0, 5911.0, 861.0);
((GeneralPath)shape).curveTo(6195.0, 691.0, 6413.0, 421.0, 6515.0, 100.0);
((GeneralPath)shape).curveTo(6249.0, 258.0, 5955.0, 372.0, 5642.0, 434.0);
((GeneralPath)shape).curveTo(5391.0, 167.0, 5034.0, 0.0, 4638.0, 0.0);
((GeneralPath)shape).curveTo(3879.0, 0.0, 3263.0, 616.0, 3263.0, 1375.0);
((GeneralPath)shape).curveTo(3263.0, 1483.0, 3275.0, 1588.0, 3299.0, 1688.0);
((GeneralPath)shape).curveTo(2156.0, 1631.0, 1143.0, 1083.0, 465.0, 251.0);
((GeneralPath)shape).curveTo(347.0, 454.0, 279.0, 690.0, 279.0, 942.0);
((GeneralPath)shape).curveTo(279.0, 1419.0, 522.0, 1840.0, 891.0, 2086.0);
((GeneralPath)shape).curveTo(666.0, 2079.0, 454.0, 2017.0, 268.0, 1914.0);
((GeneralPath)shape).curveTo(268.0, 1920.0, 268.0, 1925.0, 268.0, 1931.0);
((GeneralPath)shape).curveTo(268.0, 2597.0, 742.0, 3153.0, 1371.0, 3279.0);
((GeneralPath)shape).curveTo(1256.0, 3310.0, 1134.0, 3327.0, 1009.0, 3327.0);
((GeneralPath)shape).curveTo(920.0, 3327.0, 834.0, 3318.0, 750.0, 3302.0);
((GeneralPath)shape).curveTo(925.0, 3848.0, 1433.0, 4246.0, 2034.0, 4257.0);
((GeneralPath)shape).curveTo(1563.0, 4626.0, 971.0, 4846.0, 326.0, 4846.0);
((GeneralPath)shape).curveTo(215.0, 4846.0, 106.0, 4839.0, -2.0, 4827.0);
((GeneralPath)shape).curveTo(606.0, 5217.0, 1329.0, 5445.0, 2106.0, 5445.0);
((GeneralPath)shape).curveTo(4635.0, 5445.0, 6018.0, 3350.0, 6018.0, 1533.0);
((GeneralPath)shape).curveTo(6018.0, 1473.0, 6017.0, 1414.0, 6014.0, 1355.0);
((GeneralPath)shape).curveTo(6283.0, 1161.0, 6516.0, 919.0, 6700.0, 643.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0);
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
        return 0;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 0;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 267;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 217;
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
	public Twitter_icon() {
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

