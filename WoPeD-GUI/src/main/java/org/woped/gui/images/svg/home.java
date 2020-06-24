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
public class home implements
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
g.setComposite(AlphaComposite.getInstance(3, 0.3f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0374000072479248f, 0.0f, -1.6485999822616577f));
// _0_0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(-1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f));
// _0_0_0_0
paint = new RadialGradientPaint(new Point2D.Double(1.0, 44.0), 5.0f, new Point2D.Double(1.0, 44.0), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.0f, 0.0f, 0.0f, 0.800000011920929f, -13.0f, -79.19999694824219f));
shape = new Rectangle2D.Double(-11.0, -48.0, 10.0, 8.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1
paint = new RadialGradientPaint(new Point2D.Double(1.0, 44.0), 5.0f, new Point2D.Double(1.0, 44.0), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.0f, 0.0f, 0.0f, 0.800000011920929f, 36.0f, 8.800000190734863f));
shape = new Rectangle2D.Double(38.0, 40.0, 10.0, 8.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_2
paint = new LinearGradientPaint(new Point2D.Double(21.875, 48.000999450683594), new Point2D.Double(21.875, 40.0), new float[] {0.0f,0.5f,1.0f}, new Color[] {new Color(0, 0, 0, 0),new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new Rectangle2D.Double(11.0, 40.0, 27.0, 8.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0_2);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
paint = new LinearGradientPaint(new Point2D.Double(8.177200317382812, 5.857699871063232), new Point2D.Double(8.177200317382812, 15.677000045776367), new float[] {0.0f,1.0f}, new Color[] {new Color(114, 159, 207, 255),new Color(56, 110, 166, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(3.137200117111206f, 0.0f, 0.0f, 3.1840999126434326f, -1.6066999435424805f, -4.861100196838379f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.046, 1.5072);
((GeneralPath)shape).lineTo(1.2180004, 25.5762);
((GeneralPath)shape).lineTo(6.4862003, 25.5762);
((GeneralPath)shape).lineTo(6.5487003, 44.5552);
((GeneralPath)shape).lineTo(42.5467, 44.4932);
((GeneralPath)shape).lineTo(42.3987, 25.469198);
((GeneralPath)shape).lineTo(46.874702, 25.513199);
((GeneralPath)shape).lineTo(37.540703, 15.684198);
((GeneralPath)shape).lineTo(37.5497, 3.5331984);
((GeneralPath)shape).lineTo(30.505701, 3.5236983);
((GeneralPath)shape).lineTo(30.536701, 8.187199);
((GeneralPath)shape).lineTo(24.0457, 1.5073986);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.046, 1.5072);
((GeneralPath)shape).lineTo(1.2180004, 25.5762);
((GeneralPath)shape).lineTo(6.4862003, 25.5762);
((GeneralPath)shape).lineTo(6.5487003, 44.5552);
((GeneralPath)shape).lineTo(42.5467, 44.4932);
((GeneralPath)shape).lineTo(42.3987, 25.469198);
((GeneralPath)shape).lineTo(46.874702, 25.513199);
((GeneralPath)shape).lineTo(37.540703, 15.684198);
((GeneralPath)shape).lineTo(37.5497, 3.5331984);
((GeneralPath)shape).lineTo(30.505701, 3.5236983);
((GeneralPath)shape).lineTo(30.536701, 8.187199);
((GeneralPath)shape).lineTo(24.0457, 1.5073986);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 0.41237f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2
paint = new LinearGradientPaint(new Point2D.Double(26.062000274658203, 23.266000747680664), new Point2D.Double(29.0310001373291, 55.26599884033203), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.031, 3.0312);
((GeneralPath)shape).lineTo(3.6870003, 24.5002);
((GeneralPath)shape).lineTo(6.5, 24.5002);
((GeneralPath)shape).curveTo(6.782, 24.4992, 7.0527, 24.6112, 7.2522, 24.8102);
((GeneralPath)shape).curveTo(7.4516, 25.0102, 7.5633, 25.2802, 7.5625, 25.5622);
((GeneralPath)shape).lineTo(7.625, 43.5002);
((GeneralPath)shape).lineTo(41.469, 43.438198);
((GeneralPath)shape).lineTo(41.344, 25.469198);
((GeneralPath)shape).curveTo(41.343002, 25.187199, 41.455, 24.916199, 41.654003, 24.717197);
((GeneralPath)shape).curveTo(41.854004, 24.517197, 42.124004, 24.405197, 42.406002, 24.406197);
((GeneralPath)shape).lineTo(44.375004, 24.406197);
((GeneralPath)shape).lineTo(36.750004, 16.406197);
((GeneralPath)shape).curveTo(36.569004, 16.211197, 36.469006, 15.954197, 36.469006, 15.688196);
((GeneralPath)shape).lineTo(36.500004, 4.5941963);
((GeneralPath)shape).lineTo(31.562004, 4.5939965);
((GeneralPath)shape).lineTo(31.594004, 8.187696);
((GeneralPath)shape).curveTo(31.596004, 8.617996, 31.337004, 9.006797, 30.939003, 9.171296);
((GeneralPath)shape).curveTo(30.542004, 9.335796, 30.084003, 9.243496, 29.781002, 8.937696);
((GeneralPath)shape).lineTo(24.031002, 3.0313964);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2);
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
        return 1;
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
		return 48;
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
	public home() {
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

