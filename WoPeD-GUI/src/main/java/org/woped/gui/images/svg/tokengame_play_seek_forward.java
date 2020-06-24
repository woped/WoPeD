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
public class tokengame_play_seek_forward implements
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
    public tokengame_play_seek_forward() {
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
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -183.0f, -121.0f));
// _0_0_0
        g.setComposite(AlphaComposite.getInstance(3, 0.03999999f * origAlpha));
        AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
        g.transform(new AffineTransform(-1.3423199653625488f, 0.0f, 0.0f, 0.565684974193573f, 240.33900451660156f, 137.64700317382812f));
// _0_0_0_0
        paint = new RadialGradientPaint(new Point2D.Double(24.837125778198242, 36.42112731933594), 15.644737f, new Point2D.Double(24.837125778198242, 36.42112731933594), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5367230176925659f, -1.0124599726310524E-13f, 16.87310028076172f));
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
        g.setTransform(defaultTransform__0_0_0_0);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_0
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 38.0f, 0.0f));
// _0_0_0_1_0_0
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_0_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_0_0_0
        paint = new RadialGradientPaint(new Point2D.Double(64.21421813964844, 146.18817138671875), 8.75f, new Point2D.Double(64.21421813964844, 146.18817138671875), new float[]{0.0f, 1.0f},
                new Color[]{green, new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.2642180025577545f, -0.9860659837722778f, -2.2388999462127686f, -0.5999100208282471f, 486.60198974609375f, 298.4880065917969f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(169.4996, 134.5);
        ((GeneralPath) shape).lineTo(186.9996, 145.0);
        ((GeneralPath) shape).lineTo(169.4996, 155.5);
        ((GeneralPath) shape).lineTo(169.4996, 134.5);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_0_0_1_0_0_0);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_0_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -18.0f, 0.0f));
// _0_0_0_1_0_0_1
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_0_1_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_0_0_1_0
        paint = new RadialGradientPaint(new Point2D.Double(64.21421813964844, 146.18817138671875), 8.75f, new Point2D
                .Double(64.21421813964844, 146.18817138671875), new float[]{0.0f, 1.0f},
                new Color[]{green, new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.2642180025577545f, -0.9860659837722778f, -2.2388999462127686f, -0.5999100208282471f, 486.60198974609375f, 298.4880065917969f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(169.4996, 134.5);
        ((GeneralPath) shape).lineTo(186.9996, 145.0);
        ((GeneralPath) shape).lineTo(169.4996, 155.5);
        ((GeneralPath) shape).lineTo(169.4996, 134.5);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_0_0_1_0_0_1_0);
        g.setTransform(defaultTransform__0_0_0_1_0_0_1);
        g.setTransform(defaultTransform__0_0_0_1_0_0);
        g.setComposite(AlphaComposite.getInstance(3, 0.2f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -9.0f, 0.0f));
// _0_0_0_1_0_1
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_1_0 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_0_1_0
        paint = new LinearGradientPaint(new Point2D.Double(53.931114196777344, 116.38297271728516), new Point2D.Double(54.39047622680664, 122.29480743408203), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 146.39999389648438f, 36.960601806640625f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(223.9375, 151.03125);
        ((GeneralPath) shape).lineTo(216.5, 151.71875);
        ((GeneralPath) shape).lineTo(216.5, 155.5);
        ((GeneralPath) shape).lineTo(223.9375, 151.03125);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(202.65625, 153.0);
        ((GeneralPath) shape).lineTo(198.5, 153.375);
        ((GeneralPath) shape).lineTo(198.5, 155.5);
        ((GeneralPath) shape).lineTo(202.65625, 153.0);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_0_0_1_0_1_0);
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_1_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_0_1_1
        paint = new LinearGradientPaint(new Point2D.Double(53.01784133911133, 99.44564056396484), new Point2D.Double(53.20103073120117, 101.35189056396484), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 146.39999389648438f, 36.960601806640625f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(216.71875, 134.625);
        ((GeneralPath) shape).lineTo(216.5, 134.65625);
        ((GeneralPath) shape).lineTo(216.5, 136.65625);
        ((GeneralPath) shape).lineTo(219.625, 136.375);
        ((GeneralPath) shape).lineTo(216.71875, 134.625);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(201.09375, 136.0625);
        ((GeneralPath) shape).lineTo(198.5, 136.3125);
        ((GeneralPath) shape).lineTo(198.5, 138.3125);
        ((GeneralPath) shape).lineTo(204.03125, 137.8125);
        ((GeneralPath) shape).lineTo(201.09375, 136.0625);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_0_0_1_0_1_1);
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_1_2 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_0_1_2
        paint = new LinearGradientPaint(new Point2D.Double(54.34602355957031, 102.97689056396484), new Point2D.Double(54.712406158447266, 106.38314056396484), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 146.39999389648438f, 36.960601806640625f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(222.5, 138.09375);
        ((GeneralPath) shape).lineTo(216.5, 138.65625);
        ((GeneralPath) shape).lineTo(216.5, 141.6875);
        ((GeneralPath) shape).lineTo(226.875, 140.71875);
        ((GeneralPath) shape).lineTo(222.5, 138.09375);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(206.90625, 139.5625);
        ((GeneralPath) shape).lineTo(198.5, 140.34375);
        ((GeneralPath) shape).lineTo(198.5, 143.34375);
        ((GeneralPath) shape).lineTo(211.25, 142.15625);
        ((GeneralPath) shape).lineTo(206.90625, 139.5625);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_0_0_1_0_1_2);
        g.setComposite(AlphaComposite.getInstance(3, 0.35135132f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_0_1_3 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_0_1_3
        paint = new LinearGradientPaint(new Point2D.Double(55.93444061279297, 107.94751739501953), new Point2D.Double(56.941993713378906, 113.80963134765625), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 0, 0, 255), new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 146.39999389648438f, 36.960601806640625f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(229.78125, 142.46875);
        ((GeneralPath) shape).lineTo(216.5, 143.6875);
        ((GeneralPath) shape).lineTo(216.5, 149.71875);
        ((GeneralPath) shape).lineTo(227.90625, 148.65625);
        ((GeneralPath) shape).lineTo(234.0, 145.0);
        ((GeneralPath) shape).lineTo(229.78125, 142.46875);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(214.1875, 143.90625);
        ((GeneralPath) shape).lineTo(198.5, 145.34375);
        ((GeneralPath) shape).lineTo(198.5, 151.375);
        ((GeneralPath) shape).lineTo(206.625, 150.625);
        ((GeneralPath) shape).lineTo(216.0, 145.0);
        ((GeneralPath) shape).lineTo(214.1875, 143.90625);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_0_0_1_0_1_3);
        g.setTransform(defaultTransform__0_0_0_1_0_1);
        g.setTransform(defaultTransform__0_0_0_1_0);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_1 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_1
        paint = new LinearGradientPaint(new Point2D.Double(90.0, 155.05650329589844), new Point2D.Double
                (89.00727844238281, 142.03872680664062), new float[]{0.0f, 1.0f},
                new Color[]{green, new Color(70, 71, 68, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0f, 3.7784399389005527E-17f, -3.7784399389005527E-17f, -1.0f, 279.0f, 290.0f));
        stroke = new BasicStroke(1.0000006f, 2, 1, 4.0f, null, 0.0f);
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(207.5, 155.5);
        ((GeneralPath) shape).lineTo(225.0, 145.0);
        ((GeneralPath) shape).lineTo(207.5, 134.5);
        ((GeneralPath) shape).lineTo(207.5, 155.5);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(189.5, 155.5);
        ((GeneralPath) shape).lineTo(207.0, 145.0);
        ((GeneralPath) shape).lineTo(189.5, 134.5);
        ((GeneralPath) shape).lineTo(189.5, 155.5);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.setStroke(stroke);
        g.draw(shape);
        g.setTransform(defaultTransform__0_0_0_1_1);
        g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
        AffineTransform defaultTransform__0_0_0_1_2 = g.getTransform();
        g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1_2
        paint = new LinearGradientPaint(new Point2D.Double(88.99994659423828, 149.4308319091797), new Point2D.Double(88.99994659423828, 140.0359344482422), new float[]{0.0f, 1.0f}, new Color[]{new Color(0, 255, 0, 255), new Color(238, 238, 236, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0f, -6.342579813908057E-18f, 6.342579813908057E-18f, -1.0f, 278.968994140625f, 290.0f));
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(207.96875, 154.625);
        ((GeneralPath) shape).lineTo(224.0, 145.0);
        ((GeneralPath) shape).lineTo(207.96875, 135.375);
        ((GeneralPath) shape).lineTo(207.96875, 154.625);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(189.96875, 154.625);
        ((GeneralPath) shape).lineTo(206.0, 145.0);
        ((GeneralPath) shape).lineTo(189.96875, 135.375);
        ((GeneralPath) shape).lineTo(189.96875, 154.625);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(208.96875, 152.84375);
        ((GeneralPath) shape).lineTo(208.96875, 137.15625);
        ((GeneralPath) shape).lineTo(222.0625, 145.0);
        ((GeneralPath) shape).lineTo(208.96875, 152.84375);
        ((GeneralPath) shape).closePath();
        ((GeneralPath) shape).moveTo(190.96875, 152.84375);
        ((GeneralPath) shape).lineTo(190.96875, 137.15625);
        ((GeneralPath) shape).lineTo(204.0625, 145.0);
        ((GeneralPath) shape).lineTo(190.96875, 152.84375);
        ((GeneralPath) shape).closePath();
        g.setPaint(paint);
        g.fill(shape);
        g.setTransform(defaultTransform__0_0_0_1_2);
        g.setTransform(defaultTransform__0_0_0_1);
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
        return 3;
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
        return 43;
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

