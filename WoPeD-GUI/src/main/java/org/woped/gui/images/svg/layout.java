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
public class layout implements
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
g.transform(new AffineTransform(-0.9583333134651184f, 0.0f, 0.0f, 0.7142819762229919f, 47.0f, 15.00015640258789f));
// _0_0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0
paint = new RadialGradientPaint(new Point2D.Double(7.0, 39.464805603027344), 3.5f, new Point2D.Double(7.0, 39.464805603027344), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.0f, -1.0000009536743164f, 1.1428560018539429f, 0.0f, -41.10258865356445f, 45.5000114440918f));
shape = new Rectangle2D.Double(0.0, 35.0, 4.0, 7.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_1 = g.getTransform();
g.transform(new AffineTransform(-1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f));
// _0_0_0_1
paint = new RadialGradientPaint(new Point2D.Double(7.0, 39.464805603027344), 3.5f, new Point2D.Double(7.0, 39.464805603027344), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.0f, -1.0000009536743164f, 1.1428560018539429f, 0.0f, -89.10259246826172f, -31.499990463256836f));
shape = new Rectangle2D.Double(-48.0, -42.0, 4.0, 7.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_2
paint = new LinearGradientPaint(new Point2D.Double(18.142135620117188, 35.0), new Point2D.Double(18.142135620117188, 42.0406608581543), new float[] {0.0f,0.5f,1.0f}, new Color[] {new Color(0, 0, 0, 0),new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new Rectangle2D.Double(4.0, 35.0, 40.0, 7.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0_2);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
paint = new RadialGradientPaint(new Point2D.Double(8.337614059448242, 40.58210754394531), 20.500002f, new Point2D.Double(8.337614059448242, 40.58210754394531), new float[] {0.0f,1.0f}, new Color[] {new Color(211, 215, 207, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-0.9959856867790222f, 6.912070915632285E-8f, 1.1814220215455862E-6f, 0.8780487179756165f, 46.84596633911133f, 3.0487797260284424f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(43.215996, 7.5);
((GeneralPath)shape).lineTo(13.471509, 7.5);
((GeneralPath)shape).curveTo(13.314168, 7.5, 3.5, 17.314169, 3.5, 17.471508);
((GeneralPath)shape).lineTo(3.5, 42.215992);
((GeneralPath)shape).curveTo(3.5, 42.373333, 3.626668, 42.5, 3.784009, 42.5);
((GeneralPath)shape).lineTo(43.215996, 42.5);
((GeneralPath)shape).curveTo(43.373337, 42.5, 43.500004, 42.373333, 43.500004, 42.215992);
((GeneralPath)shape).lineTo(43.500004, 7.7840085);
((GeneralPath)shape).curveTo(43.500004, 7.626668, 43.373337, 7.5, 43.215996, 7.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(1.0f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(43.215996, 7.5);
((GeneralPath)shape).lineTo(13.471509, 7.5);
((GeneralPath)shape).curveTo(13.314168, 7.5, 3.5, 17.314169, 3.5, 17.471508);
((GeneralPath)shape).lineTo(3.5, 42.215992);
((GeneralPath)shape).curveTo(3.5, 42.373333, 3.626668, 42.5, 3.784009, 42.5);
((GeneralPath)shape).lineTo(43.215996, 42.5);
((GeneralPath)shape).curveTo(43.373337, 42.5, 43.500004, 42.373333, 43.500004, 42.215992);
((GeneralPath)shape).lineTo(43.500004, 7.7840085);
((GeneralPath)shape).curveTo(43.500004, 7.626668, 43.373337, 7.5, 43.215996, 7.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2
paint = new LinearGradientPaint(new Point2D.Double(35.47406005859375, 36.91294479370117), new Point2D.Double(35.47406005859375, 39.35141372680664), new float[] {0.0f,1.0f}, new Color[] {new Color(85, 87, 83, 255),new Color(85, 87, 83, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.00456964969635f, 0.0f, -0.18551939725875854f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.532116, 40.598797);
((GeneralPath)shape).lineTo(35.532116, 14.5);
((GeneralPath)shape).lineTo(7.51301, 14.5);
((GeneralPath)shape).lineTo(7.51301, 36.544823);
((GeneralPath)shape).lineTo(24.393398, 36.544823);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 0.6413044f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.5, 16.875002);
((GeneralPath)shape).lineTo(13.062499, 8.5);
((GeneralPath)shape).lineTo(42.5, 8.5);
((GeneralPath)shape).lineTo(42.5, 41.5);
((GeneralPath)shape).lineTo(4.5, 41.5);
((GeneralPath)shape).lineTo(4.5, 16.875002);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4
paint = new Color(85, 87, 83, 255);
stroke = new BasicStroke(0.99999994f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.0648146, 18.566679);
((GeneralPath)shape).lineTo(35.435184, 18.566679);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_4);
g.setComposite(AlphaComposite.getInstance(3, 0.35714284f * origAlpha));
AffineTransform defaultTransform__0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_5
paint = new RadialGradientPaint(new Point2D.Double(37.03035354614258, 12.989150047302246), 4.2929163f, new Point2D.Double(37.03035354614258, 12.989150047302246), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-2.1313509941101074f, 0.0f, 0.0f, 1.2838330268859863f, 86.54397583007812f, -0.127798393368721f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(4.0, 17.212006);
((GeneralPath)shape).curveTo(4.8898015, 15.865378, 11.455677, 13.481495, 14.873341, 12.681969);
((GeneralPath)shape).curveTo(14.666262, 14.254915, 15.023334, 19.0, 15.023334, 19.0);
((GeneralPath)shape).curveTo(12.503686, 17.625, 4.953745, 17.049892, 4.0, 17.212006);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_5);
g.setComposite(AlphaComposite.getInstance(3, 0.6413044f * origAlpha));
AffineTransform defaultTransform__0_0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_6
paint = new LinearGradientPaint(new Point2D.Double(20.875, 14.249998092651367), new Point2D.Double(20.875, 18.437498092651367), new float[] {0.0f,1.0f}, new Color[] {new Color(136, 138, 133, 255),new Color(136, 138, 133, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new Rectangle2D.Double(8.0, 15.0, 27.0, 3.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_6);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7
paint = new LinearGradientPaint(new Point2D.Double(38.94493103027344, 12.114839553833008), new Point2D.Double(37.30616760253906, 13.39281940460205), new float[] {0.0f,1.0f}, new Color[] {new Color(193, 199, 188, 255),new Color(232, 234, 230, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0f, 0.0f, 0.0f, 1.0f, 47.000003814697266f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(3.500024, 17.60468);
((GeneralPath)shape).curveTo(3.486859, 16.189537, 9.852559, 7.397427, 13.735143, 7.500906);
((GeneralPath)shape).curveTo(12.762105, 7.733901, 11.984788, 13.668936, 13.374508, 16.481436);
((GeneralPath)shape).curveTo(10.624508, 16.481436, 4.463889, 15.746506, 3.500024, 17.60468);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(1.0f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(3.500024, 17.60468);
((GeneralPath)shape).curveTo(3.486859, 16.189537, 9.852559, 7.397427, 13.735143, 7.500906);
((GeneralPath)shape).curveTo(12.762105, 7.733901, 11.984788, 13.668936, 13.374508, 16.481436);
((GeneralPath)shape).curveTo(10.624508, 16.481436, 4.463889, 15.746506, 3.500024, 17.60468);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_7);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_8
paint = new LinearGradientPaint(new Point2D.Double(32.86248779296875, 36.02836608886719), new Point2D.Double(34.170047760009766, 38.07038116455078), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.032779380679130554f, -1.0071301460266113f, -0.9994630217552185f, -0.03303084149956703f, 45.6202278137207f, 49.233341217041016f));
stroke = new BasicStroke(1.0000002f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(5.78902, 15.499999);
((GeneralPath)shape).curveTo(6.517382, 14.143041, 9.947557, 10.391559, 11.870143, 9.145044);
((GeneralPath)shape).curveTo(11.631203, 10.581831, 11.333088, 12.855721, 11.948728, 15.437757);
((GeneralPath)shape).curveTo(11.948728, 15.437757, 6.569724, 15.336642, 5.78902, 15.499999);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_8);
g.setComposite(AlphaComposite.getInstance(3, 0.7826087f * origAlpha));
AffineTransform defaultTransform__0_0_9 = g.getTransform();
g.transform(new AffineTransform(0.6285393238067627f, 0.0f, 0.0f, 0.606091320514679f, 3.999999761581421f, 7.693326950073242f));
// _0_0_9
paint = new Color(85, 87, 83, 255);
stroke = new BasicStroke(1.6201854f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.523418, 23.604815);
((GeneralPath)shape).curveTo(13.523418, 24.97165, 12.454952, 26.079689, 11.136932, 26.079689);
((GeneralPath)shape).curveTo(9.818913, 26.079689, 8.750447, 24.97165, 8.750447, 23.604815);
((GeneralPath)shape).curveTo(8.750447, 22.23798, 9.818913, 21.12994, 11.136932, 21.12994);
((GeneralPath)shape).curveTo(12.454952, 21.12994, 13.523418, 22.23798, 13.523418, 23.604815);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_9);
g.setComposite(AlphaComposite.getInstance(3, 0.7826087f * origAlpha));
AffineTransform defaultTransform__0_0_10 = g.getTransform();
g.transform(new AffineTransform(0.6285393238067627f, 0.0f, 0.0f, 0.6060914993286133f, 4.000000476837158f, 12.693323135375977f));
// _0_0_10
paint = new Color(85, 87, 83, 255);
stroke = new BasicStroke(1.6201854f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(13.523418, 23.604815);
((GeneralPath)shape).curveTo(13.523418, 24.97165, 12.454952, 26.079689, 11.136932, 26.079689);
((GeneralPath)shape).curveTo(9.818913, 26.079689, 8.750447, 24.97165, 8.750447, 23.604815);
((GeneralPath)shape).curveTo(8.750447, 22.23798, 9.818913, 21.12994, 11.136932, 21.12994);
((GeneralPath)shape).curveTo(12.454952, 21.12994, 13.523418, 22.23798, 13.523418, 23.604815);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_10);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_11 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_11
paint = new LinearGradientPaint(new Point2D.Double(18.384777069091797, 22.0), new Point2D.Double(23.292892456054688, 22.0), new float[] {0.0f,1.0f}, new Color[] {new Color(136, 138, 133, 255),new Color(136, 138, 133, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new Rectangle2D.Double(14.0, 21.0, 10.0, 2.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_11);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_12 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_12
paint = new LinearGradientPaint(new Point2D.Double(15.114407539367676, 27.0), new Point2D.Double(20.019962310791016, 27.0), new float[] {0.0f,1.0f}, new Color[] {new Color(136, 138, 133, 255),new Color(136, 138, 133, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new Rectangle2D.Double(14.0, 26.0, 7.0, 2.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_12);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_13 = g.getTransform();
g.transform(new AffineTransform(0.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f));
// _0_0_13
paint = new Color(85, 87, 83, 255);
shape = new Rectangle2D.Double(-14.659513473510742, -38.5, 0.1595131754875183, 3.0);
g.setPaint(paint);
g.fill(shape);
paint = new Color(85, 87, 83, 255);
stroke = new BasicStroke(1.0000001f,0,0,4.0f,null,0.0f);
shape = new Rectangle2D.Double(-14.659513473510742, -38.5, 0.1595131754875183, 3.0);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_13);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_14 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_14
paint = new Color(85, 87, 83, 255);
stroke = new BasicStroke(1.0f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(23.906406, 31.559769);
((GeneralPath)shape).lineTo(15.5, 31.559769);
((GeneralPath)shape).lineTo(15.5, 34.5);
((GeneralPath)shape).lineTo(25.525274, 34.5);
((GeneralPath)shape).lineTo(25.525274, 33.2951);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_14);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_15 = g.getTransform();
g.transform(new AffineTransform(-1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_15
paint = new LinearGradientPaint(new Point2D.Double(-8.28125, 12.475584030151367), new Point2D.Double(-8.28125, 6.2509765625), new float[] {0.0f,1.0f}, new Color[] {new Color(85, 87, 83, 255),new Color(85, 87, 83, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.2909091114997864f, 0.0f, 0.0f, 0.3497267961502075f, -33.09090805053711f, 9.251365661621094f));
shape = new Rectangle2D.Double(-36.0, 11.0, 1.0, 4.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_15);
g.setComposite(AlphaComposite.getInstance(3, 0.3f * origAlpha));
AffineTransform defaultTransform__0_0_16 = g.getTransform();
g.transform(new AffineTransform(-0.9166669845581055f, 0.0f, 0.0f, 0.7142819762229919f, 48.0f, 18.000120162963867f));
// _0_0_16
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_16_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_16_0
paint = new RadialGradientPaint(new Point2D.Double(7.0, 39.464805603027344), 3.5f, new Point2D.Double(7.0, 39.464805603027344), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.0f, -1.0000009536743164f, 1.1428560018539429f, 0.0f, -41.10258865356445f, 45.5000114440918f));
shape = new Rectangle2D.Double(0.0, 35.0, 4.0, 7.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_16_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_16_1 = g.getTransform();
g.transform(new AffineTransform(-1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f));
// _0_0_16_1
paint = new RadialGradientPaint(new Point2D.Double(7.0, 39.464805603027344), 3.5f, new Point2D.Double(7.0, 39.464805603027344), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.0f, -1.0000009536743164f, 1.1428560018539429f, 0.0f, -89.10259246826172f, -31.499990463256836f));
shape = new Rectangle2D.Double(-48.0, -42.0, 4.0, 7.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_16_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_16_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_16_2
paint = new LinearGradientPaint(new Point2D.Double(18.142135620117188, 35.0), new Point2D.Double(18.142135620117188, 42.0406608581543), new float[] {0.0f,0.5f,1.0f}, new Color[] {new Color(0, 0, 0, 0),new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new Rectangle2D.Double(4.0, 35.0, 40.0, 7.0);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_16_2);
g.setTransform(defaultTransform__0_0_16);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_17 = g.getTransform();
g.transform(new AffineTransform(-1.0f, 0.0f, 0.0f, 1.0f, 103.0f, 0.5714290738105774f));
// _0_0_17
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_17_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17_0
paint = new LinearGradientPaint(new Point2D.Double(13.63011360168457, 28.5), new Point2D.Double(25.20809555053711, 41.180992126464844), new float[] {0.0f,1.0f}, new Color[] {new Color(230, 206, 70, 255),new Color(214, 186, 28, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 53.0f, 1.4285709857940674f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(57.5, 14.928571);
((GeneralPath)shape).lineTo(57.5, 44.92857);
((GeneralPath)shape).lineTo(96.5, 44.92857);
((GeneralPath)shape).lineTo(57.5, 14.928571);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(63.5, 27.928572);
((GeneralPath)shape).lineTo(78.5, 38.92857);
((GeneralPath)shape).lineTo(63.5, 38.92857);
((GeneralPath)shape).lineTo(63.5, 27.928572);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(163, 133, 3, 255);
stroke = new BasicStroke(1.0000002f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(57.5, 14.928571);
((GeneralPath)shape).lineTo(57.5, 44.92857);
((GeneralPath)shape).lineTo(96.5, 44.92857);
((GeneralPath)shape).lineTo(57.5, 14.928571);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(63.5, 27.928572);
((GeneralPath)shape).lineTo(78.5, 38.92857);
((GeneralPath)shape).lineTo(63.5, 38.92857);
((GeneralPath)shape).lineTo(63.5, 27.928572);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_17_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_17_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17_1
paint = new Color(163, 133, 3, 255);
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(61.5, 44.92857);
((GeneralPath)shape).lineTo(61.5, 41.92857);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_17_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_17_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17_2
paint = new Color(163, 133, 3, 255);
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(67.5, 44.92857);
((GeneralPath)shape).lineTo(67.5, 41.92857);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_17_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_17_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17_3
paint = new Color(163, 133, 3, 255);
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(73.5, 44.92857);
((GeneralPath)shape).lineTo(73.5, 41.964287);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_17_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_17_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17_4
paint = new Color(163, 133, 3, 255);
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(79.5, 44.92857);
((GeneralPath)shape).lineTo(79.5, 41.92857);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_17_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_17_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17_5
paint = new Color(163, 133, 3, 255);
stroke = new BasicStroke(1.0f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(85.5, 44.92857);
((GeneralPath)shape).lineTo(85.5, 41.92857);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_17_5);
g.setComposite(AlphaComposite.getInstance(3, 0.4f * origAlpha));
AffineTransform defaultTransform__0_0_17_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17_6
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0000006f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(58.500004, 16.928576);
((GeneralPath)shape).lineTo(58.500004, 43.928585);
((GeneralPath)shape).lineTo(93.500015, 43.928585);
((GeneralPath)shape).lineTo(58.500004, 16.928576);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_17_6);
g.setTransform(defaultTransform__0_0_17);
g.setComposite(AlphaComposite.getInstance(3, 0.4f * origAlpha));
AffineTransform defaultTransform__0_0_18 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_18
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(40.498253, 40.542446);
((GeneralPath)shape).lineTo(40.542446, 26.532894);
((GeneralPath)shape).lineTo(21.458408, 40.563263);
((GeneralPath)shape).lineTo(40.498253, 40.542446);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_18);
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
        return 7;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 *
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static int getOrigWidth() {
		return 47;
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
	public layout() {
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

