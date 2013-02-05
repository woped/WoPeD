package org.woped.gui.images.svg;

import java.awt.*;
import java.awt.geom.*;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class editor_einblenden implements
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
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -4.049505710601807f, 962.1972045898438f));
// _0_0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.335793375968933f, 0.0f, 0.0f, 1.3894668817520142f, -11.807738304138184f, 42.364681243896484f));
// _0_0_0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0_0
paint = new LinearGradientPaint(new Point2D.Double(24.925514221191406, 40.00010681152344), new Point2D.Double(24.925514221191406, 19.53866958618164), new float[] {0.0f,1.0f}, new Color[] {new Color(211, 215, 207, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 5.0f, -6.0f));
shape = new RoundRectangle2D.Double(13.5, 3.0, 33.000038146972656, 30.499984741210938, 4.0, 4.0);
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(1.0000002f,0,0,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(13.5, 3.0, 33.000038146972656, 30.499984741210938, 4.0, 4.0);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0_1
paint = new LinearGradientPaint(new Point2D.Double(23.069358825683594, 6.799302101135254), new Point2D.Double(23.069358825683594, 12.403290748596191), new float[] {0.0f,1.0f}, new Color[] {new Color(52, 101, 164, 255),new Color(114, 159, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 5.0f, -6.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.499999, 8.030334);
((GeneralPath)shape).lineTo(13.499999, 4.5000057);
((GeneralPath)shape).curveTo(13.499999, 3.3920057, 14.391999, 2.5000057, 15.499999, 2.5000057);
((GeneralPath)shape).lineTo(44.5, 2.5000057);
((GeneralPath)shape).curveTo(45.608, 2.5000057, 46.5, 3.3920057, 46.5, 4.5000057);
((GeneralPath)shape).lineTo(46.5, 8.030334);
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.0000005f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.499999, 8.030334);
((GeneralPath)shape).lineTo(13.499999, 4.5000057);
((GeneralPath)shape).curveTo(13.499999, 3.3920057, 14.391999, 2.5000057, 15.499999, 2.5000057);
((GeneralPath)shape).lineTo(44.5, 2.5000057);
((GeneralPath)shape).curveTo(45.608, 2.5000057, 46.5, 3.3920057, 46.5, 4.5000057);
((GeneralPath)shape).lineTo(46.5, 8.030334);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 0.8f * origAlpha));
AffineTransform defaultTransform__0_0_0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0_2
paint = new LinearGradientPaint(new Point2D.Double(9.5, 16.264360427856445), new Point2D.Double(9.5, 13.408722877502441), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 120)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 5.0f, -6.0f));
stroke = new BasicStroke(1.0000008f,0,0,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(14.5, 3.5000076293945312, 31.000003814697266, 29.00000762939453, 1.9907817840576172, 1.9907817840576172);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_0_2);
g.setTransform(defaultTransform__0_0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 0.96f * origAlpha));
AffineTransform defaultTransform__0_0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1
paint = new Color(255, 255, 255, 255);
shape = new RoundRectangle2D.Double(33.98765563964844, 53.799896240234375, 13.738567352294922, 32.70222473144531, 1.208593487739563, 1.197706937789917);
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(0.53726053f,1,0,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(33.98765563964844, 53.799896240234375, 13.738567352294922, 32.70222473144531, 1.208593487739563, 1.197706937789917);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 4.049505710601807f, 42.16498947143555f));
// _0_0_0_2
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(0.5f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(29.7995, 20.725882);
((GeneralPath)shape).lineTo(43.6891, 20.725882);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 4.049505710601807f, 42.16498947143555f));
// _0_0_0_3
paint = new Color(128, 128, 128, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(31.693537, 23.377531);
((GeneralPath)shape).lineTo(41.03745, 23.5038);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 52);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(31.693537, 23.377531);
((GeneralPath)shape).lineTo(41.03745, 23.5038);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_4
paint = new Color(128, 128, 128, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.743042, 67.499695);
((GeneralPath)shape).lineTo(45.086952, 67.62596);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 52);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.743042, 67.499695);
((GeneralPath)shape).lineTo(45.086952, 67.62596);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_5
paint = new Color(128, 128, 128, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.743042, 69.26746);
((GeneralPath)shape).lineTo(45.086952, 69.39373);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 52);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.743042, 69.26746);
((GeneralPath)shape).lineTo(45.086952, 69.39373);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0_5);
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
        return 2;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 3;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 46;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 45;
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
	public editor_einblenden() {
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

