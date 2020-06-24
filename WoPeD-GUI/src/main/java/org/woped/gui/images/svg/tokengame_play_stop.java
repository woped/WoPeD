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
public class tokengame_play_stop implements
        org.pushingpixels.flamingo.api.common.icon.ResizableIcon {

    private static Color red = new Color(128, 0, 0, 255);
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
    public tokengame_play_stop() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
    }

    /**
     * Paints the transcoded SVG image on the specified graphics context. You
     * can install a custom transformation on the graphics context to scale the
     * image.
     *
     * @param g Graphics context.
     */
    public static void paint(Graphics2D g) {
        Shape shape = null;
        Paint paint = null;
        Stroke stroke = null;

        float origAlpha = 1.0f;
        Composite origComposite = g.getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite =
                    (AlphaComposite) origComposite;
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
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -183.0f, -71.09429931640625f));
// _0_1_0
        g.setComposite(AlphaComposite.getInstance(3, 0.03999999f * origAlpha));
        AffineTransform defaultTransform__0_1_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.086609959602356f, 0.0f, 0.0f, 0.565684974193573f, 180.01100158691406f, 87.64710235595703f));
// _0_1_0_0
        paint = new RadialGradientPaint(new Point2D.Double(24.837125778198242, 36.42112731933594), 15.644737f, new Point2D.Double(24.837125778198242, 36.42112731933594), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5367230176925659f, -9.309409796862123E-13f, 16.87310028076172f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(40.48186, 36.421127);
        ((GeneralPath) shape).curveTo(40.483814, 39.421745, 37.50237, 42.19488, 32.66107, 43.69549);
        ((GeneralPath) shape).curveTo(27.81977, 45.196106, 21.854479, 45.196106, 17.01318, 43.69549);
        ((GeneralPath) shape).curveTo(12.17188, 42.19488, 9.190436, 39.421745, 9.192389, 36.421127);
        ((GeneralPath) shape).curveTo(9.190436, 33.42051, 12.17188, 30.647373, 17.01318, 29.14676);
        ((GeneralPath) shape).curveTo(21.854479, 27.646149, 27.81977, 27.646149, 32.66107, 29.14676);
        ((GeneralPath) shape).curveTo(37.50237, 30.647373, 40.483814, 33.42051, 40.48186, 36.421127);
        ((GeneralPath) shape).closePath();
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
        paint = new RadialGradientPaint(new Point2D.Double(172.1193389892578, 100.89449310302734), 11.0f, new Point2D.Double(172.1193389892578, 100.89449310302734), new float[]{0.0f, 1.0f},
                new Color[]{red, new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9391829967498779f, -1.1306999921798706f, 1.7874000072479248f, 1.4846299886703491f, -169.54600524902344f, 144.98500061035156f));
        shape = new Rectangle2D.Double(158.5, 84.5, 21.0, 21.0);
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_1_0_1_0);
        g.setComposite(AlphaComposite.getInstance(3, 0.2f * origAlpha));
        AffineTransform defaultTransform__0_1_0_1_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_1
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_1_0_1_1_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_1_0
        paint = new LinearGradientPaint(new Point2D.Double(45.365482330322266, 119.79000091552734), new Point2D.Double(47.039215087890625, 126.97866821289062), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 115.14399719238281f, -16.133800506591797f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(179.5, 101.8125);
        ((GeneralPath) shape).lineTo(158.5, 103.75);
        ((GeneralPath) shape).lineTo(158.5, 105.5);
        ((GeneralPath) shape).lineTo(179.5, 105.5);
        ((GeneralPath) shape).lineTo(179.5, 101.8125);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_1_0_1_1_0);
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_1_0_1_1_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_1_1
        paint = new LinearGradientPaint(new Point2D.Double(44.72807312011719, 106.5255126953125), new Point2D.Double(45.2320556640625, 109.82125091552734), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 115.14399719238281f, -16.133800506591797f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(179.5, 88.75);
        ((GeneralPath) shape).lineTo(158.5, 90.6875);
        ((GeneralPath) shape).lineTo(158.5, 93.6875);
        ((GeneralPath) shape).lineTo(179.5, 91.78125);
        ((GeneralPath) shape).lineTo(179.5, 88.75);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_1_0_1_1_1);
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_1_0_1_1_2 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_1_2
        paint = new LinearGradientPaint(new Point2D.Double(44.584930419921875, 111.75763702392578), new Point2D.Double(45.683101654052734, 117.88375091552734), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 115.14399719238281f, -16.133800506591797f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(179.5, 93.78125);
        ((GeneralPath) shape).lineTo(158.5, 95.71875);
        ((GeneralPath) shape).lineTo(158.5, 101.75);
        ((GeneralPath) shape).lineTo(179.5, 99.8125);
        ((GeneralPath) shape).lineTo(179.5, 93.78125);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_1_0_1_1_2);
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_1_0_1_1_3 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_1_3
        paint = new LinearGradientPaint(new Point2D.Double(44.64906692504883, 102.85250091552734), new Point2D.Double(44.901058197021484, 104.82125091552734), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 115.14399719238281f, -16.133800506591797f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(179.5, 84.75);
        ((GeneralPath) shape).lineTo(158.5, 86.6875);
        ((GeneralPath) shape).lineTo(158.5, 88.6875);
        ((GeneralPath) shape).lineTo(179.5, 86.75);
        ((GeneralPath) shape).lineTo(179.5, 84.75);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_1_0_1_1_3);
        g.setTransform(defaultTransform__0_1_0_1_1);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_1_0_1_2 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_2
        paint = new LinearGradientPaint(new Point2D.Double(14.617093086242676, 15.36176586151123), new Point2D.Double(14.577069282531738, 33.35448455810547), new float[]{0.0f, 1.0f},
                new Color[]{red, new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 145.0f, 71.0f));
        stroke = new BasicStroke(0.9999997f, 2, 0, 4.0f, null, 0.0f);
        shape = new Rectangle2D.Double(159.5, 85.5, 19.0, 19.0);
        g.setPaint(paint);
        g.setStroke(stroke);
        g.draw(shape);
        g.setTransform(defaultTransform__0_1_0_1_2);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_1_0_1_3 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0_1_3
        paint = new LinearGradientPaint(new Point2D.Double(158.6162567138672, 84.70542907714844), new Point2D.Double(158.6162567138672, 102.3972396850586), new float[]{0.0f, 1.0f},
                new Color[]{red, new Color(85, 87, 83, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
        stroke = new BasicStroke(1.0f, 2, 1, 4.0f, null, 0.0f);
        shape = new Rectangle2D.Double(158.5, 84.5, 21.0, 21.0);
        g.setPaint(paint);
        g.setStroke(stroke);
        g.draw(shape);
        g.setTransform(defaultTransform__0_1_0_1_3);
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
        return 7;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     *
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 13;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     *
     * @return The width of the bounding box of the original SVG image.
     */
    public static int getOrigWidth() {
        return 35;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     *
     * @return The height of the bounding box of the original SVG image.
     */
    public static int getOrigHeight() {
        return 30;
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

