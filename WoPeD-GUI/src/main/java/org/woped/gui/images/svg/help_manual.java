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
public class help_manual implements
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
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.1507899761199951f, 0.0f, 0.0f, 1.0f, -4.913690090179443f, 0.625f));
// _0_0_0
paint = new RadialGradientPaint(new Point2D.Double(25.125, 36.75), 15.75f, new Point2D.Double(25.125, 36.75), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5952379703521729f, -6.245009807472426E-16f, 14.875f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.875, 36.75);
((GeneralPath)shape).curveTo(40.875, 41.92767, 33.823486, 46.125, 25.125, 46.125);
((GeneralPath)shape).curveTo(16.426516, 46.125, 9.375, 41.92767, 9.375, 36.75);
((GeneralPath)shape).curveTo(9.375, 31.57233, 16.426516, 27.375, 25.125, 27.375);
((GeneralPath)shape).curveTo(33.823486, 27.375, 40.875, 31.57233, 40.875, 36.75);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 0.4853801f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0000007f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(37.209385, 21.763575);
((GeneralPath)shape).lineTo(44.444435, 21.763575);
((GeneralPath)shape).curveTo(44.79495, 21.763575, 44.98674, 21.801928, 45.038826, 22.172512);
((GeneralPath)shape).lineTo(45.93907, 30.290268);
((GeneralPath)shape).curveTo(46.018433, 30.838284, 46.14656, 31.149038, 45.779507, 31.167522);
((GeneralPath)shape).lineTo(37.05066, 31.167522);
((GeneralPath)shape).lineTo(37.209385, 21.763575);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2
paint = new Color(91, 107, 148, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.364322, 5.5185895);
((GeneralPath)shape).curveTo(6.455105, 3.6036003, 7.371976, 2.5542815, 9.078878, 2.549044);
((GeneralPath)shape).lineTo(38.405777, 2.4590578);
((GeneralPath)shape).curveTo(38.652363, 2.458301, 38.974316, 2.659207, 38.999012, 2.9089887);
((GeneralPath)shape).lineTo(42.257492, 35.86723);
((GeneralPath)shape).lineTo(40.94219, 35.923862);
((GeneralPath)shape).lineTo(41.57143, 42.369514);
((GeneralPath)shape).curveTo(41.632442, 42.9945, 41.39006, 43.52882, 40.5, 43.533035);
((GeneralPath)shape).lineTo(9.789305, 43.678474);
((GeneralPath)shape).curveTo(7.25676, 43.690468, 4.6538453, 41.59976, 4.7759337, 39.024403);
((GeneralPath)shape).lineTo(6.364322, 5.5185895);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(54, 72, 120, 255);
stroke = new BasicStroke(1.0000001f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.364322, 5.5185895);
((GeneralPath)shape).curveTo(6.455105, 3.6036003, 7.371976, 2.5542815, 9.078878, 2.549044);
((GeneralPath)shape).lineTo(38.405777, 2.4590578);
((GeneralPath)shape).curveTo(38.652363, 2.458301, 38.974316, 2.659207, 38.999012, 2.9089887);
((GeneralPath)shape).lineTo(42.257492, 35.86723);
((GeneralPath)shape).lineTo(40.94219, 35.923862);
((GeneralPath)shape).lineTo(41.57143, 42.369514);
((GeneralPath)shape).curveTo(41.632442, 42.9945, 41.39006, 43.52882, 40.5, 43.533035);
((GeneralPath)shape).lineTo(9.789305, 43.678474);
((GeneralPath)shape).curveTo(7.25676, 43.690468, 4.6538453, 41.59976, 4.7759337, 39.024403);
((GeneralPath)shape).lineTo(6.364322, 5.5185895);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
paint = new LinearGradientPaint(new Point2D.Double(10.496114730834961, 93.33804321289062), new Point2D.Double(10.219901084899902, 84.28707885742188), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(203, 203, 203, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.262739896774292f, 0.0f, 0.0f, 0.4419420063495636f, 1.0f, -0.875f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.125, 34.875);
((GeneralPath)shape).lineTo(10.9375, 35.0);
((GeneralPath)shape).curveTo(9.380982, 35.177868, 8.125, 36.39612, 8.125, 38.0);
((GeneralPath)shape).curveTo(8.125, 39.60388, 9.380982, 40.822132, 10.9375, 41.0);
((GeneralPath)shape).lineTo(40.125, 41.125);
((GeneralPath)shape).lineTo(40.125, 41.0625);
((GeneralPath)shape).curveTo(38.46938, 40.98435, 37.125, 39.67485, 37.125, 38.0);
((GeneralPath)shape).curveTo(37.125, 36.32515, 38.46938, 35.01565, 40.125, 34.9375);
((GeneralPath)shape).lineTo(40.125, 34.875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4
paint = new LinearGradientPaint(new Point2D.Double(6.587181091308594, 22.132999420166016), new Point2D.Double(14.511404037475586, 22.132999420166016), new float[] {0.0f,1.0f}, new Color[] {new Color(214, 227, 240, 255),new Color(149, 177, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9577500224113464f, 0.0f, 0.0f, 1.0279899835586548f, 1.0f, -0.5719109773635864f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(9.6875, 2.8125);
((GeneralPath)shape).curveTo(7.98059, 2.8125, 7.050103, 3.8215063, 6.96875, 5.673866);
((GeneralPath)shape).lineTo(5.3125, 37.82577);
((GeneralPath)shape).curveTo(5.22054, 40.904198, 7.1393733, 42.654484, 9.125, 43.15625);
((GeneralPath)shape).curveTo(4.875, 41.525578, 5.4375, 34.164455, 10.75, 34.19522);
((GeneralPath)shape).lineTo(41.648285, 34.19522);
((GeneralPath)shape).lineTo(38.335785, 3.2432432);
((GeneralPath)shape).curveTo(38.310024, 3.0025303, 37.987877, 2.8125, 37.742035, 2.8125);
((GeneralPath)shape).lineTo(9.6875, 2.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_4);
g.setComposite(AlphaComposite.getInstance(3, 0.4804469f * origAlpha));
AffineTransform defaultTransform__0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, -0.03582730144262314f, 0.9993579983711243f, 0.0f, 0.0f));
// _0_0_5
paint = new LinearGradientPaint(new Point2D.Double(35.43303680419922, 4.953004837036133), new Point2D.Double(41.2191276550293, 4.953004837036133), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 18),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.2540000081062317f, 0.0f, 1.8221499816276597E-16f, 3.759809970855713f, 0.7886289954185486f, 0.14856700599193573f));
shape = new Rectangle2D.Double(9.788626670837402, 3.968538999557495, 2.0, 29.60479164123535);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_5);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_6
paint = new LinearGradientPaint(new Point2D.Double(73.36198425292969, 26.652196884155273), new Point2D.Double(-2.7582900524139404, 21.270376205444336), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0254299640655518f, 0.0f, 0.0f, 0.957302987575531f, 0.0f, -0.8067579865455627f));
stroke = new BasicStroke(1.0000001f,1,0,20.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(9.875101, 3.333683);
((GeneralPath)shape).curveTo(8.191201, 3.333683, 7.5384235, 4.065846, 7.4581676, 5.887831);
((GeneralPath)shape).lineTo(6.159263, 35.7772);
((GeneralPath)shape).curveTo(7.092592, 34.170452, 8.598859, 33.594437, 11.011665, 33.594437);
((GeneralPath)shape).lineTo(40.96308, 33.594437);
((GeneralPath)shape).lineTo(38.13718, 3.757363);
((GeneralPath)shape).curveTo(38.114727, 3.5203092, 37.79396, 3.333683, 37.551434, 3.333683);
((GeneralPath)shape).lineTo(9.875101, 3.333683);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_6);
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
        return 5;
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
		return 43;
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
	public help_manual() {
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

