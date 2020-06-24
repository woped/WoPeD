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
public class refresh implements
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
g.setComposite(AlphaComposite.getInstance(3, 0.3611111f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(-1.5146484375f, 0.0f, 0.0f, -0.7917057871818542f, 60.92323684692383f, 69.52841186523438f));
// _0_0_0
paint = new RadialGradientPaint(new Point2D.Double(24.837125778198242, 36.42112731933594), 15.644737f, new Point2D.Double(24.837125778198242, 36.42112731933594), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5367230176925659f, 0.0f, 16.87306022644043f));
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
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 0.5180723f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
paint = new LinearGradientPaint(new Point2D.Double(13.478553771972656, 10.61220645904541), new Point2D.Double(15.419417381286621, 19.115121841430664), new float[] {0.0f,0.33333334f,1.0f}, new Color[] {new Color(52, 101, 164, 255),new Color(91, 134, 190, 255),new Color(131, 168, 216, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0818661451339722f, 0.0f, 0.0f, 1.116685152053833f, -0.8207482099533081f, -1.862243413925171f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.478497, 9.771147);
((GeneralPath)shape).curveTo(20.478497, 9.771147, 12.632988, 7.9438004, 14.368023, 21.024298);
((GeneralPath)shape).lineTo(5.1028657, 21.024298);
((GeneralPath)shape).curveTo(5.1028657, 21.024298, 6.008533, 7.5377774, 20.478497, 9.771147);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(37.1280517578125, 29.729604721069336), new Point2D.Double(37.402549743652344, 26.800912857055664), new float[] {0.0f,1.0f}, new Color[] {new Color(52, 101, 164, 255),new Color(52, 101, 164, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0818661451339722f, 0.0f, 0.0f, -1.116685152053833f, 50.09458923339844f, 49.64485549926758f));
stroke = new BasicStroke(1.0430058f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.478497, 9.771147);
((GeneralPath)shape).curveTo(20.478497, 9.771147, 12.632988, 7.9438004, 14.368023, 21.024298);
((GeneralPath)shape).lineTo(5.1028657, 21.024298);
((GeneralPath)shape).curveTo(5.1028657, 21.024298, 6.008533, 7.5377774, 20.478497, 9.771147);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(-0.6129282116889954f, -0.5154380798339844f, -0.5178496241569519f, 0.610073983669281f, 58.68616485595703f, 13.911360740661621f));
// _0_0_2
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_0
paint = new LinearGradientPaint(new Point2D.Double(61.572532653808594, 28.049652099609375), new Point2D.Double(10.969182014465332, 20.333938598632812), new float[] {0.0f,0.23705086f,0.54706067f,0.7455769f,0.87321436f,1.0f}, new Color[] {new Color(153, 184, 223, 255),new Color(57, 105, 168, 255),new Color(79, 126, 186, 255),new Color(150, 182, 215, 255),new Color(160, 189, 220, 255),new Color(114, 159, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(45.862103, 50.27352);
((GeneralPath)shape).curveTo(62.92443, 34.96305, 47.15024, 15.929711, 22.760624, 12.513943);
((GeneralPath)shape).lineTo(22.113577, 3.1522143);
((GeneralPath)shape).lineTo(7.613534, 20.510136);
((GeneralPath)shape).lineTo(22.703188, 33.23244);
((GeneralPath)shape).curveTo(22.703188, 33.23244, 22.454828, 23.347105, 22.454828, 23.347105);
((GeneralPath)shape).curveTo(41.289894, 24.339584, 54.775795, 35.67504, 45.862103, 50.27352);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.2497795f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(45.862103, 50.27352);
((GeneralPath)shape).curveTo(62.92443, 34.96305, 47.15024, 15.929711, 22.760624, 12.513943);
((GeneralPath)shape).lineTo(22.113577, 3.1522143);
((GeneralPath)shape).lineTo(7.613534, 20.510136);
((GeneralPath)shape).lineTo(22.703188, 33.23244);
((GeneralPath)shape).curveTo(22.703188, 33.23244, 22.454828, 23.347105, 22.454828, 23.347105);
((GeneralPath)shape).curveTo(41.289894, 24.339584, 54.775795, 35.67504, 45.862103, 50.27352);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2_0);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 0.5481928f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(-0.6128110289573669f, -0.5154405832290649f, -0.51775062084198f, 0.610076904296875f, 58.67563247680664f, 13.911364555358887f));
// _0_0_3
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 1.0080026413561427E-6f, 8.522378607267456E-7f));
// _0_0_3_0
paint = new LinearGradientPaint(new Point2D.Double(8.687857627868652, 25.265626907348633), new Point2D.Double(52.12267303466797, 25.265626907348633), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.2492865f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.0625, 6.3125);
((GeneralPath)shape).lineTo(9.3125, 20.34375);
((GeneralPath)shape).lineTo(21.46875, 30.59375);
((GeneralPath)shape).curveTo(21.39688, 27.754892, 21.28125, 23.375, 21.28125, 23.375);
((GeneralPath)shape).curveTo(21.27502, 23.044409, 21.405773, 22.725958, 21.642519, 22.495132);
((GeneralPath)shape).curveTo(21.879265, 22.264305, 22.200924, 22.141653, 22.53125, 22.15625);
((GeneralPath)shape).curveTo(32.170254, 22.66763, 40.44103, 25.773014, 45.28125, 30.875);
((GeneralPath)shape).curveTo(48.676468, 34.453835, 50.10205, 39.13153, 49.125, 44.21875);
((GeneralPath)shape).curveTo(50.36834, 42.205643, 51.107895, 40.194595, 51.375, 38.21875);
((GeneralPath)shape).curveTo(51.870422, 34.55401, 50.856987, 30.946651, 48.5625, 27.59375);
((GeneralPath)shape).curveTo(43.973526, 20.887947, 34.236977, 15.361613, 22.3125, 13.6875);
((GeneralPath)shape).curveTo(21.732115, 13.606381, 21.295727, 13.117098, 21.28125, 12.53125);
((GeneralPath)shape).lineTo(21.0625, 6.3125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_3_0);
g.setTransform(defaultTransform__0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 0.5f * origAlpha));
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(0.19086800515651703f, 0.1612599939107895f, 0.1612599939107895f, -0.19086800515651703f, -0.7190830111503601f, 15.306130409240723f));
// _0_0_4
g.setTransform(defaultTransform__0_0_4);
g.setComposite(AlphaComposite.getInstance(3, 0.5180723f * origAlpha));
AffineTransform defaultTransform__0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_5
paint = new LinearGradientPaint(new Point2D.Double(13.478553771972656, 10.61220645904541), new Point2D.Double(15.419417381286621, 19.115121841430664), new float[] {0.0f,0.33333334f,1.0f}, new Color[] {new Color(52, 101, 164, 255),new Color(91, 134, 190, 255),new Color(131, 168, 216, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-1.0818661451339722f, 0.0f, 0.0f, -1.116685152053833f, 48.639854431152344f, 47.86224365234375f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(27.340609, 36.22885);
((GeneralPath)shape).curveTo(27.340609, 36.22885, 35.186115, 38.0562, 33.45108, 24.975702);
((GeneralPath)shape).lineTo(42.71624, 24.975702);
((GeneralPath)shape).curveTo(42.71624, 24.975702, 41.810574, 38.462223, 27.340609, 36.22885);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(37.1280517578125, 29.729604721069336), new Point2D.Double(37.402549743652344, 26.800912857055664), new float[] {0.0f,1.0f}, new Color[] {new Color(52, 101, 164, 255),new Color(52, 101, 164, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0818661451339722f, 0.0f, 0.0f, 1.116685152053833f, -2.275484800338745f, -3.6448540687561035f));
stroke = new BasicStroke(1.0430058f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(27.340609, 36.22885);
((GeneralPath)shape).curveTo(27.340609, 36.22885, 35.186115, 38.0562, 33.45108, 24.975702);
((GeneralPath)shape).lineTo(42.71624, 24.975702);
((GeneralPath)shape).curveTo(42.71624, 24.975702, 41.810574, 38.462223, 27.340609, 36.22885);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_5);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_6 = g.getTransform();
g.transform(new AffineTransform(0.610133171081543f, 0.5154998898506165f, 0.5154880881309509f, -0.6101471185684204f, -10.618023872375488f, 32.08855438232422f));
// _0_0_6
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_6_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_6_0
paint = new LinearGradientPaint(new Point2D.Double(61.572532653808594, 28.049652099609375), new Point2D.Double(10.969182014465332, 20.333938598632812), new float[] {0.0f,0.23705086f,0.54706067f,0.7455769f,0.87321436f,1.0f}, new Color[] {new Color(153, 184, 223, 255),new Color(57, 105, 168, 255),new Color(79, 126, 186, 255),new Color(150, 182, 215, 255),new Color(160, 189, 220, 255),new Color(114, 159, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(45.862103, 50.27352);
((GeneralPath)shape).curveTo(62.92443, 34.96305, 47.15024, 15.929711, 22.760624, 12.513943);
((GeneralPath)shape).lineTo(22.113577, 3.152214);
((GeneralPath)shape).lineTo(7.613534, 20.510136);
((GeneralPath)shape).lineTo(22.703188, 33.23244);
((GeneralPath)shape).curveTo(22.703188, 33.23244, 22.454828, 23.347105, 22.454828, 23.347105);
((GeneralPath)shape).curveTo(41.289894, 24.339584, 54.775795, 35.67504, 45.862103, 50.27352);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.2525637f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(45.862103, 50.27352);
((GeneralPath)shape).curveTo(62.92443, 34.96305, 47.15024, 15.929711, 22.760624, 12.513943);
((GeneralPath)shape).lineTo(22.113577, 3.152214);
((GeneralPath)shape).lineTo(7.613534, 20.510136);
((GeneralPath)shape).lineTo(22.703188, 33.23244);
((GeneralPath)shape).curveTo(22.703188, 33.23244, 22.454828, 23.347105, 22.454828, 23.347105);
((GeneralPath)shape).curveTo(41.289894, 24.339584, 54.775795, 35.67504, 45.862103, 50.27352);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_6_0);
g.setTransform(defaultTransform__0_0_6);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7 = g.getTransform();
g.transform(new AffineTransform(0.6128106713294983f, 0.5154405832290649f, 0.5177503824234009f, -0.610076904296875f, -10.856505393981934f, 32.0886344909668f));
// _0_0_7
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7_0 = g.getTransform();
g.transform(new AffineTransform(0.9972307085990906f, -0.0024605588987469673f, -0.0024605593644082546f, 0.9980642199516296f, 0.24570290744304657f, 0.2077351063489914f));
// _0_0_7_0
paint = new LinearGradientPaint(new Point2D.Double(5.892597675323486, 20.54067611694336), new Point2D.Double(45.19892120361328, 27.72103500366211), new float[] {0.0f,1.0f}, new Color[] {new Color(238, 238, 236, 85),new Color(238, 238, 236, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.2497319f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.0625, 6.3125);
((GeneralPath)shape).lineTo(9.3125, 20.34375);
((GeneralPath)shape).lineTo(21.46875, 30.59375);
((GeneralPath)shape).curveTo(21.39688, 27.754892, 21.28125, 23.375, 21.28125, 23.375);
((GeneralPath)shape).curveTo(21.27502, 23.044409, 21.405773, 22.725958, 21.642519, 22.495132);
((GeneralPath)shape).curveTo(21.879265, 22.264305, 22.200924, 22.141653, 22.53125, 22.15625);
((GeneralPath)shape).curveTo(32.170254, 22.66763, 40.44103, 25.773014, 45.28125, 30.875);
((GeneralPath)shape).curveTo(48.676468, 34.453835, 50.10205, 39.13153, 49.125, 44.21875);
((GeneralPath)shape).curveTo(50.36834, 42.205643, 51.107895, 40.194595, 51.375, 38.21875);
((GeneralPath)shape).curveTo(51.870422, 34.55401, 50.856987, 30.946651, 48.5625, 27.59375);
((GeneralPath)shape).curveTo(43.973526, 20.887947, 34.236977, 15.361613, 22.3125, 13.6875);
((GeneralPath)shape).curveTo(21.732115, 13.606381, 21.295727, 13.117098, 21.28125, 12.53125);
((GeneralPath)shape).lineTo(21.0625, 6.3125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_7_0);
g.setTransform(defaultTransform__0_0_7);
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
		return 48;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 *
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 48;
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
	public refresh() {
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

