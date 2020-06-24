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

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class tokengame_play_start implements
		org.pushingpixels.flamingo.api.common.icon.ResizableIcon {

    private static Color green = new Color(0, 128, 0, 255);
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
    public tokengame_play_start() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
    }

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
        Composite origComposite = g.getComposite();
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
g.setTransform(defaultTransform__0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -83.00039672851562f, -71.09429931640625f));
// _0_1_0
g.setComposite(AlphaComposite.getInstance(3, 0.03999999f * origAlpha));
AffineTransform defaultTransform__0_1_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0546799898147583f, 0.0f, 0.0f, 0.565684974193573f, 80.80460357666016f, 87.64710235595703f));
// _0_1_0_0
paint = new RadialGradientPaint(new Point2D.Double(24.837125778198242, 36.42112731933594), 15.644737f, new Point2D.Double(24.837125778198242, 36.42112731933594), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5367230176925659f, -4.0098398776736155E-13f, 16.87310028076172f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.48186, 36.421127);
((GeneralPath)shape).curveTo(40.483814, 39.421745, 37.50237, 42.19488, 32.66107, 43.69549);
((GeneralPath)shape).curveTo(27.81977, 45.196106, 21.854479, 45.196106, 17.01318, 43.69549);
((GeneralPath)shape).curveTo(12.17188, 42.19488, 9.190436, 39.421745, 9.192389, 36.421127);
((GeneralPath)shape).curveTo(9.190436, 33.42051, 12.17188, 30.647373, 17.01318, 29.14676);
((GeneralPath)shape).curveTo(21.854479, 27.646149, 27.81977, 27.646149, 32.66107, 29.14676);
((GeneralPath)shape).curveTo(37.50237, 30.647373, 40.483814, 33.42051, 40.48186, 36.421127);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 38.0f, 0.0f));
// _0_1_0_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_0
        paint = new RadialGradientPaint(new Point2D.Double(110.35346221923828, 84.47499084472656), 12.551644f, new Point2D.Double(110.35346221923828, 84.47499084472656), new float[]{0.0f, 1.0f},
                new Color[]{green, new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.053243398666381836f, -0.8362380266189575f, 2.019469976425171f, 0.12856799364089966f, -106.91899871826172f, 179.17100524902344f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(57.49988, 108.90574);
((GeneralPath)shape).lineTo(57.49988, 81.09425);
((GeneralPath)shape).lineTo(81.603165, 95.0);
((GeneralPath)shape).lineTo(57.49988, 108.90574);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1_0_1_0_0);
g.setComposite(AlphaComposite.getInstance(3, 0.2f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_1
g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_1_0
paint = new LinearGradientPaint(new Point2D.Double(56.81288528442383, 112.14862823486328), new Point2D.Double(57.254825592041016, 114.10179901123047), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.62568998336792f, -26.6018009185791f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(64.0625, 84.875);
((GeneralPath)shape).lineTo(57.5, 85.46875);
((GeneralPath)shape).lineTo(57.5, 87.5);
((GeneralPath)shape).lineTo(67.0625, 86.625);
((GeneralPath)shape).lineTo(64.0625, 84.875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1_0_1_0_1_0);
g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_1_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_1_1
paint = new LinearGradientPaint(new Point2D.Double(58.55234909057617, 115.93924713134766), new Point2D.Double(59.17106628417969, 119.10179901123047), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.62568998336792f, -26.6018009185791f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(70.0625, 88.34375);
((GeneralPath)shape).lineTo(57.5, 89.5);
((GeneralPath)shape).lineTo(57.5, 92.5);
((GeneralPath)shape).lineTo(74.5625, 90.9375);
((GeneralPath)shape).lineTo(70.0625, 88.34375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1_0_1_0_1_1);
g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_1_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_1_2
paint = new LinearGradientPaint(new Point2D.Double(59.178348541259766, 120.81781768798828), new Point2D.Double(60.50599670410156, 126.43153381347656), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.62568998336792f, -26.6018009185791f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(77.5625, 92.6875);
((GeneralPath)shape).lineTo(57.5, 94.53125);
((GeneralPath)shape).lineTo(57.5, 100.53125);
((GeneralPath)shape).lineTo(74.75, 98.9375);
((GeneralPath)shape).lineTo(81.59375, 95.0);
((GeneralPath)shape).lineTo(77.5625, 92.6875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1_0_1_0_1_2);
g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_1_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_1_3
paint = new LinearGradientPaint(new Point2D.Double(58.08574295043945, 129.01956176757812), new Point2D.Double(61.177520751953125, 134.5398406982422), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.62568998336792f, -26.6018009185791f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(70.59375, 101.34375);
((GeneralPath)shape).lineTo(57.5, 102.5625);
((GeneralPath)shape).lineTo(57.5, 108.90625);
((GeneralPath)shape).lineTo(70.59375, 101.34375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1_0_1_0_1_3);
g.setTransform(defaultTransform__0_1_0_1_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_2
paint = new LinearGradientPaint(new Point2D.Double(57.9375, 86.0), new Point2D.Double(57.94369125366211, 103.04012298583984), new float[] {0.0f,1.0f}, new Color[] {new Color(173, 176, 168, 255),new Color(70, 71, 68, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0000004f,2,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(57.49988, 108.90574);
((GeneralPath)shape).lineTo(57.49988, 81.09425);
((GeneralPath)shape).lineTo(81.603165, 95.0);
((GeneralPath)shape).lineTo(57.49988, 108.90574);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_1_0_1_0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1_0_1_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_0_3
        paint = new LinearGradientPaint(new Point2D.Double(13.375120162963867, 17.812509536743164), new Point2D.Double(13.500120162963867, 32.9375114440918), new float[]{0.0f, 1.0f},
                new Color[]{green, new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 44.999900817871094f, 71.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(57.99988, 81.96874);
((GeneralPath)shape).lineTo(57.99988, 108.03124);
((GeneralPath)shape).lineTo(80.59363, 94.99999);
((GeneralPath)shape).lineTo(57.99988, 81.96874);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(58.99988, 83.71874);
((GeneralPath)shape).lineTo(78.56238, 94.99999);
((GeneralPath)shape).lineTo(58.99988, 106.28124);
((GeneralPath)shape).lineTo(58.99988, 83.71874);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1_0_1_0_3);
g.setTransform(defaultTransform__0_1_0_1_0);
g.setTransform(defaultTransform__0_1_0_1);
g.setTransform(defaultTransform__0_1_0);
g.setTransform(defaultTransform__0_1);
g.setTransform(defaultTransform__0);
g.setTransform(defaultTransform_);

	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     *
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 8;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     *
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 10;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
     *
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 34;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
     *
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 33;
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

