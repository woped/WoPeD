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
import java.awt.geom.RoundRectangle2D;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class analyze_semanticalanalysis implements
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
g.transform(new AffineTransform(1.126710057258606f, 0.0f, 0.0f, 1.0021100044250488f, -2.8918700218200684f, -0.8212440013885498f));
// _0_0_0
paint = new RadialGradientPaint(new Point2D.Double(24.041629791259766, 42.242130279541016), 17.576654f, new Point2D.Double(24.041629791259766, 42.242130279541016), new float[] {0.0f,1.0f}, new Color[] {new Color(50, 52, 47, 139),new Color(50, 52, 47, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.30459800362586975f, -5.757919805384696E-16f, 29.37529945373535f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(41.618282, 42.24213);
((GeneralPath)shape).curveTo(41.62035, 44.15528, 38.270714, 45.923378, 32.831627, 46.880135);
((GeneralPath)shape).curveTo(27.392542, 47.83689, 20.690716, 47.83689, 15.25163, 46.880135);
((GeneralPath)shape).curveTo(9.812545, 45.923378, 6.4629064, 44.15528, 6.4649754, 42.24213);
((GeneralPath)shape).curveTo(6.4629064, 40.32898, 9.812545, 38.560883, 15.25163, 37.604126);
((GeneralPath)shape).curveTo(20.690716, 36.64737, 27.392542, 36.64737, 32.831627, 37.604126);
((GeneralPath)shape).curveTo(38.270714, 38.560883, 41.62035, 40.32898, 41.618282, 42.24213);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
paint = new LinearGradientPaint(new Point2D.Double(25.86111831665039, 26.133586883544922), new Point2D.Double(18.300277709960938, 19.567596435546875), new float[] {0.0f,1.0f}, new Color[] {new Color(195, 198, 192, 255),new Color(232, 234, 230, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9523869752883911f, 0.0f, 0.0f, 1.018339991569519f, 1.1426000595092773f, -1.9416300058364868f));
shape = new RoundRectangle2D.Double(1.6199486255645752, 1.6600797176361084, 44.75983810424805, 41.70181655883789, 9.131982803344727, 9.131985664367676);
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(0.0012142062187194824, 24.012266159057617), new Point2D.Double(47.99876403808594, 24.012266159057617), new float[] {0.0f,1.0f}, new Color[] {new Color(169, 170, 167, 255),new Color(103, 105, 100, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9523869752883911f, 0.0f, 0.0f, 1.018339991569519f, 1.1426000595092773f, -1.9416300058364868f));
stroke = new BasicStroke(0.9999997f,1,1,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(1.6199486255645752, 1.6600797176361084, 44.75983810424805, 41.70181655883789, 9.131982803344727, 9.131985664367676);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2
paint = new RadialGradientPaint(new Point2D.Double(23.99413299560547, 32.266910552978516), 19.088932f, new Point2D.Double(23.99413299560547, 32.266910552978516), new float[] {0.0f,1.0f}, new Color[] {new Color(81, 135, 214, 255),new Color(30, 69, 128, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.768630027770996f, -1.9612599872176E-23f, 1.552060007423003E-23f, 1.3870999813079834f, -18.4424991607666f, -15.292699813842773f));
shape = new RoundRectangle2D.Double(5.4052019119262695, 5.4815545082092285, 37.17786407470703, 28.954593658447266, 3.389341354370117, 3.3893420696258545);
g.setPaint(paint);
g.fill(shape);
paint = new Color(23, 53, 98, 255);
stroke = new BasicStroke(0.9999998f,1,1,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(5.4052019119262695, 5.4815545082092285, 37.17786407470703, 28.954593658447266, 3.389341354370117, 3.3893420696258545);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
paint = new Color(236, 255, 217, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.390689, 20.935247);
((GeneralPath)shape).lineTo(6.368861, 20.935247);
((GeneralPath)shape).lineTo(6.368861, 22.152252);
((GeneralPath)shape).lineTo(14.087646, 22.152252);
((GeneralPath)shape).lineTo(15.493568, 16.239132);
((GeneralPath)shape).lineTo(18.956081, 29.936651);
((GeneralPath)shape).lineTo(22.164804, 19.116953);
((GeneralPath)shape).lineTo(25.059347, 25.028755);
((GeneralPath)shape).lineTo(28.946308, 21.516787);
((GeneralPath)shape).lineTo(41.654736, 21.516787);
((GeneralPath)shape).lineTo(41.654736, 19.457142);
((GeneralPath)shape).lineTo(28.505236, 19.457142);
((GeneralPath)shape).lineTo(25.335018, 22.59674);
((GeneralPath)shape).lineTo(22.059557, 15.937588);
((GeneralPath)shape).lineTo(19.049723, 24.112486);
((GeneralPath)shape).lineTo(15.78119, 11.248712);
((GeneralPath)shape).lineTo(12.390689, 20.935247);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(195, 234, 155, 106);
stroke = new BasicStroke(1.0f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.390689, 20.935247);
((GeneralPath)shape).lineTo(6.368861, 20.935247);
((GeneralPath)shape).lineTo(6.368861, 22.152252);
((GeneralPath)shape).lineTo(14.087646, 22.152252);
((GeneralPath)shape).lineTo(15.493568, 16.239132);
((GeneralPath)shape).lineTo(18.956081, 29.936651);
((GeneralPath)shape).lineTo(22.164804, 19.116953);
((GeneralPath)shape).lineTo(25.059347, 25.028755);
((GeneralPath)shape).lineTo(28.946308, 21.516787);
((GeneralPath)shape).lineTo(41.654736, 21.516787);
((GeneralPath)shape).lineTo(41.654736, 19.457142);
((GeneralPath)shape).lineTo(28.505236, 19.457142);
((GeneralPath)shape).lineTo(25.335018, 22.59674);
((GeneralPath)shape).lineTo(22.059557, 15.937588);
((GeneralPath)shape).lineTo(19.049723, 24.112486);
((GeneralPath)shape).lineTo(15.78119, 11.248712);
((GeneralPath)shape).lineTo(12.390689, 20.935247);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4
paint = new LinearGradientPaint(new Point2D.Double(20.33875846862793, 19.63689422607422), new Point2D.Double(46.092254638671875, 39.70832443237305), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9523869752883911f, 0.0f, 0.0f, 1.015660047531128f, 1.1426000595092773f, -0.8763250112533569f));
stroke = new BasicStroke(0.9999998f,1,1,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(2.553668975830078, 2.6544337272644043, 42.89474105834961, 39.646549224853516, 8.260666847229004, 8.260668754577637);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_4);
g.setComposite(AlphaComposite.getInstance(3, 0.38068184f * origAlpha));
AffineTransform defaultTransform__0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_5
paint = new Color(236, 255, 217, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(11.515689, 20.012278);
((GeneralPath)shape).lineTo(6.368861, 20.012278);
((GeneralPath)shape).lineTo(6.368861, 23.06103);
((GeneralPath)shape).lineTo(14.962646, 22.93603);
((GeneralPath)shape).lineTo(15.618568, 19.893387);
((GeneralPath)shape).lineTo(18.963228, 32.601727);
((GeneralPath)shape).lineTo(22.539804, 21.135092);
((GeneralPath)shape).lineTo(25.059347, 26.551191);
((GeneralPath)shape).lineTo(29.321308, 22.44261);
((GeneralPath)shape).lineTo(41.654736, 22.31761);
((GeneralPath)shape).lineTo(40.904736, 18.408072);
((GeneralPath)shape).lineTo(28.505236, 18.283072);
((GeneralPath)shape).lineTo(25.460018, 21.456026);
((GeneralPath)shape).lineTo(22.059557, 13.665616);
((GeneralPath)shape).lineTo(19.424723, 20.604265);
((GeneralPath)shape).lineTo(15.90619, 8.333659);
((GeneralPath)shape).lineTo(11.515689, 20.012278);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(195, 234, 155, 106);
stroke = new BasicStroke(1.0000004f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(11.515689, 20.012278);
((GeneralPath)shape).lineTo(6.368861, 20.012278);
((GeneralPath)shape).lineTo(6.368861, 23.06103);
((GeneralPath)shape).lineTo(14.962646, 22.93603);
((GeneralPath)shape).lineTo(15.618568, 19.893387);
((GeneralPath)shape).lineTo(18.963228, 32.601727);
((GeneralPath)shape).lineTo(22.539804, 21.135092);
((GeneralPath)shape).lineTo(25.059347, 26.551191);
((GeneralPath)shape).lineTo(29.321308, 22.44261);
((GeneralPath)shape).lineTo(41.654736, 22.31761);
((GeneralPath)shape).lineTo(40.904736, 18.408072);
((GeneralPath)shape).lineTo(28.505236, 18.283072);
((GeneralPath)shape).lineTo(25.460018, 21.456026);
((GeneralPath)shape).lineTo(22.059557, 13.665616);
((GeneralPath)shape).lineTo(19.424723, 20.604265);
((GeneralPath)shape).lineTo(15.90619, 8.333659);
((GeneralPath)shape).lineTo(11.515689, 20.012278);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_5);
g.setComposite(AlphaComposite.getInstance(3, 0.43181816f * origAlpha));
AffineTransform defaultTransform__0_0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_6
paint = new LinearGradientPaint(new Point2D.Double(8.820780754089355, 12.537569999694824), new Point2D.Double(12.499242782592773, 24.238262176513672), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(3.8095500469207764f, 0.0f, 0.0f, 1.7503199577331543f, -16.00040054321289f, -15.787199974060059f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.84375, 6.96875);
((GeneralPath)shape).lineTo(6.84375, 15.795073);
((GeneralPath)shape).curveTo(10.513653, 16.48318, 14.582567, 16.875, 18.875, 16.875);
((GeneralPath)shape).curveTo(27.810295, 16.875, 35.81226, 15.21019, 41.15625, 12.596829);
((GeneralPath)shape).lineTo(41.15625, 6.96875);
((GeneralPath)shape).lineTo(6.84375, 6.96875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_6);
g.setComposite(AlphaComposite.getInstance(3, 0.07954544f * origAlpha));
AffineTransform defaultTransform__0_0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.1875f, 0.6875f));
// _0_0_7
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7_0
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(39.125, 37.8125);
((GeneralPath)shape).lineTo(38.0625, 37.34375);
((GeneralPath)shape).curveTo(37.805687, 37.204185, 37.500156, 37.125, 37.1875, 37.125);
((GeneralPath)shape).curveTo(36.187, 37.125, 35.375, 37.937, 35.375, 38.9375);
((GeneralPath)shape).curveTo(35.375, 39.938, 36.187, 40.75, 37.1875, 40.75);
((GeneralPath)shape).curveTo(38.156734, 40.75, 38.951427, 39.98848, 39.0, 39.03125);
((GeneralPath)shape).curveTo(39.000393, 39.02353, 38.999706, 39.007744, 39.0, 39.0);
((GeneralPath)shape).lineTo(39.125, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(39.125, 37.8125);
((GeneralPath)shape).lineTo(38.0625, 37.34375);
((GeneralPath)shape).curveTo(37.805687, 37.204185, 37.500156, 37.125, 37.1875, 37.125);
((GeneralPath)shape).curveTo(36.187, 37.125, 35.375, 37.937, 35.375, 38.9375);
((GeneralPath)shape).curveTo(35.375, 39.938, 36.187, 40.75, 37.1875, 40.75);
((GeneralPath)shape).curveTo(38.156734, 40.75, 38.951427, 39.98848, 39.0, 39.03125);
((GeneralPath)shape).curveTo(39.000393, 39.02353, 38.999706, 39.007744, 39.0, 39.0);
((GeneralPath)shape).lineTo(39.125, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_7_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7_1
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.992525, 37.54466);
((GeneralPath)shape).lineTo(28.893139, 38.709835);
((GeneralPath)shape).curveTo(28.855812, 38.99973, 28.892397, 39.31323, 29.005384, 39.604755);
((GeneralPath)shape).curveTo(29.366953, 40.537636, 30.417519, 41.001312, 31.350403, 40.639748);
((GeneralPath)shape).curveTo(32.283283, 40.27818, 32.74696, 39.22761, 32.385395, 38.29473);
((GeneralPath)shape).curveTo(32.035126, 37.391003, 31.037884, 36.92522, 30.127792, 37.225857);
((GeneralPath)shape).curveTo(30.120453, 37.228283, 30.105982, 37.234627, 30.098654, 37.237152);
((GeneralPath)shape).lineTo(28.992525, 37.54466);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.992525, 37.54466);
((GeneralPath)shape).lineTo(28.893139, 38.709835);
((GeneralPath)shape).curveTo(28.855812, 38.99973, 28.892397, 39.31323, 29.005384, 39.604755);
((GeneralPath)shape).curveTo(29.366953, 40.537636, 30.417519, 41.001312, 31.350403, 40.639748);
((GeneralPath)shape).curveTo(32.283283, 40.27818, 32.74696, 39.22761, 32.385395, 38.29473);
((GeneralPath)shape).curveTo(32.035126, 37.391003, 31.037884, 36.92522, 30.127792, 37.225857);
((GeneralPath)shape).curveTo(30.120453, 37.228283, 30.105982, 37.234627, 30.098654, 37.237152);
((GeneralPath)shape).lineTo(28.992525, 37.54466);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_7_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7_2
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.5, 37.8125);
((GeneralPath)shape).lineTo(24.5625, 37.34375);
((GeneralPath)shape).curveTo(24.305689, 37.204185, 24.000156, 37.125, 23.6875, 37.125);
((GeneralPath)shape).curveTo(22.687, 37.125, 21.875, 37.937, 21.875, 38.9375);
((GeneralPath)shape).curveTo(21.875, 39.938, 22.687, 40.75, 23.6875, 40.75);
((GeneralPath)shape).curveTo(24.656734, 40.75, 25.451426, 39.98848, 25.5, 39.03125);
((GeneralPath)shape).curveTo(25.500393, 39.02353, 25.499704, 39.007744, 25.5, 39.0);
((GeneralPath)shape).lineTo(25.5, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.5, 37.8125);
((GeneralPath)shape).lineTo(24.5625, 37.34375);
((GeneralPath)shape).curveTo(24.305689, 37.204185, 24.000156, 37.125, 23.6875, 37.125);
((GeneralPath)shape).curveTo(22.687, 37.125, 21.875, 37.937, 21.875, 38.9375);
((GeneralPath)shape).curveTo(21.875, 39.938, 22.687, 40.75, 23.6875, 40.75);
((GeneralPath)shape).curveTo(24.656734, 40.75, 25.451426, 39.98848, 25.5, 39.03125);
((GeneralPath)shape).curveTo(25.500393, 39.02353, 25.499704, 39.007744, 25.5, 39.0);
((GeneralPath)shape).lineTo(25.5, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_7_2);
g.setTransform(defaultTransform__0_0_7);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_8
paint = new Color(224, 226, 223, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(39.125, 37.8125);
((GeneralPath)shape).lineTo(38.0625, 37.34375);
((GeneralPath)shape).curveTo(37.805687, 37.204185, 37.500156, 37.125, 37.1875, 37.125);
((GeneralPath)shape).curveTo(36.187, 37.125, 35.375, 37.937, 35.375, 38.9375);
((GeneralPath)shape).curveTo(35.375, 39.938, 36.187, 40.75, 37.1875, 40.75);
((GeneralPath)shape).curveTo(38.156734, 40.75, 38.951427, 39.98848, 39.0, 39.03125);
((GeneralPath)shape).curveTo(39.000393, 39.02353, 38.999706, 39.007744, 39.0, 39.0);
((GeneralPath)shape).lineTo(39.125, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(155, 157, 153, 255);
stroke = new BasicStroke(1.0f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(39.125, 37.8125);
((GeneralPath)shape).lineTo(38.0625, 37.34375);
((GeneralPath)shape).curveTo(37.805687, 37.204185, 37.500156, 37.125, 37.1875, 37.125);
((GeneralPath)shape).curveTo(36.187, 37.125, 35.375, 37.937, 35.375, 38.9375);
((GeneralPath)shape).curveTo(35.375, 39.938, 36.187, 40.75, 37.1875, 40.75);
((GeneralPath)shape).curveTo(38.156734, 40.75, 38.951427, 39.98848, 39.0, 39.03125);
((GeneralPath)shape).curveTo(39.000393, 39.02353, 38.999706, 39.007744, 39.0, 39.0);
((GeneralPath)shape).lineTo(39.125, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_8);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_9
paint = new Color(224, 226, 223, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.992525, 37.54466);
((GeneralPath)shape).lineTo(28.893139, 38.709835);
((GeneralPath)shape).curveTo(28.855812, 38.99973, 28.892397, 39.31323, 29.005384, 39.604755);
((GeneralPath)shape).curveTo(29.366953, 40.537636, 30.417519, 41.001312, 31.350403, 40.639748);
((GeneralPath)shape).curveTo(32.283283, 40.27818, 32.74696, 39.22761, 32.385395, 38.29473);
((GeneralPath)shape).curveTo(32.035126, 37.391003, 31.037884, 36.92522, 30.127792, 37.225857);
((GeneralPath)shape).curveTo(30.120453, 37.228283, 30.105982, 37.234627, 30.098654, 37.237152);
((GeneralPath)shape).lineTo(28.992525, 37.54466);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(155, 157, 153, 255);
stroke = new BasicStroke(1.0f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.992525, 37.54466);
((GeneralPath)shape).lineTo(28.893139, 38.709835);
((GeneralPath)shape).curveTo(28.855812, 38.99973, 28.892397, 39.31323, 29.005384, 39.604755);
((GeneralPath)shape).curveTo(29.366953, 40.537636, 30.417519, 41.001312, 31.350403, 40.639748);
((GeneralPath)shape).curveTo(32.283283, 40.27818, 32.74696, 39.22761, 32.385395, 38.29473);
((GeneralPath)shape).curveTo(32.035126, 37.391003, 31.037884, 36.92522, 30.127792, 37.225857);
((GeneralPath)shape).curveTo(30.120453, 37.228283, 30.105982, 37.234627, 30.098654, 37.237152);
((GeneralPath)shape).lineTo(28.992525, 37.54466);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_9);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_10
paint = new Color(224, 226, 223, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.5, 37.8125);
((GeneralPath)shape).lineTo(24.5625, 37.34375);
((GeneralPath)shape).curveTo(24.305689, 37.204185, 24.000156, 37.125, 23.6875, 37.125);
((GeneralPath)shape).curveTo(22.687, 37.125, 21.875, 37.937, 21.875, 38.9375);
((GeneralPath)shape).curveTo(21.875, 39.938, 22.687, 40.75, 23.6875, 40.75);
((GeneralPath)shape).curveTo(24.656734, 40.75, 25.451426, 39.98848, 25.5, 39.03125);
((GeneralPath)shape).curveTo(25.500393, 39.02353, 25.499704, 39.007744, 25.5, 39.0);
((GeneralPath)shape).lineTo(25.5, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(155, 157, 153, 255);
stroke = new BasicStroke(1.0f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.5, 37.8125);
((GeneralPath)shape).lineTo(24.5625, 37.34375);
((GeneralPath)shape).curveTo(24.305689, 37.204185, 24.000156, 37.125, 23.6875, 37.125);
((GeneralPath)shape).curveTo(22.687, 37.125, 21.875, 37.937, 21.875, 38.9375);
((GeneralPath)shape).curveTo(21.875, 39.938, 22.687, 40.75, 23.6875, 40.75);
((GeneralPath)shape).curveTo(24.656734, 40.75, 25.451426, 39.98848, 25.5, 39.03125);
((GeneralPath)shape).curveTo(25.500393, 39.02353, 25.499704, 39.007744, 25.5, 39.0);
((GeneralPath)shape).lineTo(25.5, 37.8125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_10);
g.setComposite(AlphaComposite.getInstance(3, 0.5738636f * origAlpha));
AffineTransform defaultTransform__0_0_11 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_11
paint = new LinearGradientPaint(new Point2D.Double(35.69420623779297, 37.333858489990234), new Point2D.Double(15.044075012207031, 5.958856582641602), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,1,1,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(4.3192057609558105, 4.635766983032227, 39.34986877441406, 30.64617919921875, 5.13934326171875, 5.13934326171875);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_11);
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
        return 2;
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
		return 46;
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
	public analyze_semanticalanalysis() {
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

