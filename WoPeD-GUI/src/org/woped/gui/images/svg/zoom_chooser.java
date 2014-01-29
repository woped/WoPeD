package org.woped.gui.images.svg;

import java.awt.*;
import java.awt.geom.*;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class zoom_chooser implements
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
g.setTransform(defaultTransform__0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
g.setTransform(defaultTransform__0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -12.265130043029785f, 47.4999885559082f));
// _0_2_0
g.setTransform(defaultTransform__0_2_0);
g.setComposite(AlphaComposite.getInstance(3, 0.6f * origAlpha));
AffineTransform defaultTransform__0_2_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 0.8999999761581421f, 1.5f, 5.331352233886719f));
// _0_2_1
paint = new LinearGradientPaint(new Point2D.Double(3.5, 35.742942810058594), new Point2D.Double(46.77865982055664, 45.742942810058594), new float[] {0.0f,0.0807889f,0.6153967f,0.74850917f,1.0f}, new Color[] {new Color(0, 0, 0, 0),new Color(0, 0, 0, 0),new Color(0, 0, 0, 63),new Color(0, 0, 0, 127),new Color(0, 0, 0, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.0, 35.742943);
((GeneralPath)shape).curveTo(11.34, 35.742943, 3.5, 37.555954, 3.5, 39.789845);
((GeneralPath)shape).curveTo(3.5, 42.023735, 11.34, 43.836746, 21.0, 43.836746);
((GeneralPath)shape).curveTo(25.496435, 43.836746, 29.586826, 43.44134, 32.6875, 42.796116);
((GeneralPath)shape).lineTo(33.5, 43.7428);
((GeneralPath)shape).curveTo(33.525276, 43.764, 33.58684, 43.785507, 33.65625, 43.800613);
((GeneralPath)shape).lineTo(41.625, 45.657856);
((GeneralPath)shape).curveTo(41.98181, 45.740448, 42.507656, 45.759155, 43.15625, 45.73012);
((GeneralPath)shape).curveTo(43.804844, 45.701088, 44.567814, 45.60604, 45.375, 45.419376);
((GeneralPath)shape).curveTo(46.17552, 45.234257, 46.58081, 45.069504, 46.71875, 44.92074);
((GeneralPath)shape).curveTo(46.856686, 44.771976, 46.758026, 44.64076, 46.40625, 44.55941);
((GeneralPath)shape).lineTo(38.375, 42.71662);
((GeneralPath)shape).curveTo(38.29626, 42.69875, 38.203148, 42.684258, 38.09375, 42.68049);
((GeneralPath)shape).lineTo(34.0, 42.4926);
((GeneralPath)shape).curveTo(36.790146, 41.775562, 38.5, 40.829655, 38.5, 39.789845);
((GeneralPath)shape).curveTo(38.5, 37.555954, 30.66, 35.742943, 21.0, 35.742943);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_1);
g.setComposite(AlphaComposite.getInstance(3, 0.6f * origAlpha));
AffineTransform defaultTransform__0_2_2 = g.getTransform();
g.transform(new AffineTransform(1.1851849555969238f, 0.0f, 0.0f, 1.1636370420455933f, 0.25926101207733154f, -1.23636794090271f));
// _0_2_2
paint = new RadialGradientPaint(new Point2D.Double(17.062257766723633, 28.85142707824707), 13.5f, new Point2D.Double(17.062257766723633, 28.85142707824707), new float[] {0.0f,1.0f}, new Color[] {new Color(66, 158, 255, 255),new Color(0, 68, 167, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.4595450162887573f, -9.027299375141858E-15f, -5.118666091499795E-17f, 1.3453389406204224f, -7.403138160705566f, -10.821840286254883f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(31.0, 18.25);
((GeneralPath)shape).curveTo(31.0, 25.843916, 24.955845, 32.0, 17.5, 32.0);
((GeneralPath)shape).curveTo(10.044156, 32.0, 4.0, 25.843916, 4.0, 18.25);
((GeneralPath)shape).curveTo(4.0, 10.656085, 10.044156, 4.5, 17.5, 4.5);
((GeneralPath)shape).curveTo(24.955845, 4.5, 31.0, 10.656085, 31.0, 18.25);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_2);
g.setComposite(AlphaComposite.getInstance(3, 0.5f * origAlpha));
AffineTransform defaultTransform__0_2_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_3
paint = new RadialGradientPaint(new Point2D.Double(16.82952117919922, 24.743623733520508), 16.924616f, new Point2D.Double(16.82952117919922, 24.743623733520508), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.2312889099121094f, -0.5978720188140869f, 0.530252993106842f, 1.9790129661560059f, -30.74856948852539f, -16.49764060974121f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.430801, 3.5);
((GeneralPath)shape).curveTo(11.914639, 3.824042, 5.101928, 10.849356, 5.101928, 19.444372);
((GeneralPath)shape).curveTo(5.101928, 21.861292, 6.505045, 24.166162, 7.5932364, 26.15625);
((GeneralPath)shape).curveTo(7.1211653, 20.728065, 9.459854, 19.54307, 11.012791, 20.025177);
((GeneralPath)shape).curveTo(15.724296, 21.487862, 23.900133, 22.817383, 32.299923, 18.431707);
((GeneralPath)shape).curveTo(35.244587, 16.894245, 36.961784, 20.661175, 36.844128, 17.275469);
((GeneralPath)shape).curveTo(35.904686, 9.480881, 29.114153, 3.5, 21.0463, 3.5);
((GeneralPath)shape).curveTo(20.840021, 3.5, 20.63519, 3.492223, 20.430801, 3.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_4 = g.getTransform();
g.transform(new AffineTransform(1.600000023841858f, 0.0f, 0.0f, 1.600000023841858f, -63.0f, 12.800000190734863f));
// _0_2_4
paint = new RadialGradientPaint(new Point2D.Double(59.787471771240234, 10.901535034179688), 10.55559f, new Point2D.Double(59.787471771240234, 10.901535034179688), new float[] {0.0f,1.0f}, new Color[] {new Color(251, 251, 250, 255),new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(4.928248087399112E-11f, -1.6866090297698975f, 1.6633599996566772f, -1.7702019803681455E-15f, 41.6543083190918f, 111.7396011352539f));
stroke = new BasicStroke(1.8749999f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(62.5, 4.5);
((GeneralPath)shape).curveTo(62.5, 10.022847, 58.022846, 14.5, 52.5, 14.5);
((GeneralPath)shape).curveTo(46.977154, 14.5, 42.5, 10.022847, 42.5, 4.5);
((GeneralPath)shape).curveTo(42.5, -1.0228475, 46.977154, -5.5, 52.5, -5.5);
((GeneralPath)shape).curveTo(58.022846, -5.5, 62.5, -1.0228475, 62.5, 4.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_5 = g.getTransform();
g.transform(new AffineTransform(1.544052004814148f, 0.0f, 0.0f, 1.5360159873962402f, -85.57756042480469f, -44.44514846801758f));
// _0_2_5
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_5_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_5_0
paint = new LinearGradientPaint(new Point2D.Double(81.33245086669922, 55.10675811767578), new Point2D.Double(82.91964721679688, 53.511260986328125), new float[] {0.0f,0.70238096f,1.0f}, new Color[] {new Color(85, 87, 83, 255),new Color(163, 165, 162, 255),new Color(136, 138, 133, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(76.79635, 49.76846);
((GeneralPath)shape).lineTo(77.444, 53.023636);
((GeneralPath)shape).lineTo(82.625175, 58.23713);
((GeneralPath)shape).curveTo(82.81767, 58.430832, 83.59664, 58.55743, 84.56811, 57.58088);
((GeneralPath)shape).curveTo(85.53958, 56.604324, 85.463684, 55.877, 85.21576, 55.627773);
((GeneralPath)shape).lineTo(80.034584, 50.419495);
((GeneralPath)shape).lineTo(76.79635, 49.76846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(81.09630584716797, 57.148193359375), new Point2D.Double(83.6292953491211, 54.61520767211914), new float[] {0.0f,1.0f}, new Color[] {new Color(46, 52, 54, 255),new Color(85, 87, 83, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(0.6765347f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(76.79635, 49.76846);
((GeneralPath)shape).lineTo(77.444, 53.023636);
((GeneralPath)shape).lineTo(82.625175, 58.23713);
((GeneralPath)shape).curveTo(82.81767, 58.430832, 83.59664, 58.55743, 84.56811, 57.58088);
((GeneralPath)shape).curveTo(85.53958, 56.604324, 85.463684, 55.877, 85.21576, 55.627773);
((GeneralPath)shape).lineTo(80.034584, 50.419495);
((GeneralPath)shape).lineTo(76.79635, 49.76846);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_5_0);
g.setComposite(AlphaComposite.getInstance(3, 0.19215687f * origAlpha));
AffineTransform defaultTransform__0_2_5_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_5_1
paint = new Color(255, 255, 255, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(79.5673, 51.32004);
((GeneralPath)shape).curveTo(79.564316, 51.331245, 79.861404, 51.64436, 79.32201, 52.185024);
((GeneralPath)shape).curveTo(78.782616, 52.725685, 78.314865, 52.55901, 78.32852, 52.54528);
((GeneralPath)shape).lineTo(78.05938, 51.023705);
((GeneralPath)shape).lineTo(79.5673, 51.32004);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_5_1);
g.setTransform(defaultTransform__0_2_5);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_6
paint = new LinearGradientPaint(new Point2D.Double(40.25, 41.0), new Point2D.Double(43.0625, 38.43457794189453), new float[] {0.0f,1.0f}, new Color[] {new Color(136, 138, 133, 255),new Color(211, 215, 207, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f));
stroke = new BasicStroke(0.99999934f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(34.284645, 33.278595);
((GeneralPath)shape).lineTo(34.90721, 36.479813);
((GeneralPath)shape).lineTo(42.406807, 44.02859);
((GeneralPath)shape).curveTo(42.77564, 44.39984, 43.340656, 44.246494, 44.2745, 43.31265);
((GeneralPath)shape).curveTo(45.20835, 42.3788, 45.449127, 41.911354, 44.98546, 41.44495);
((GeneralPath)shape).lineTo(37.485863, 33.90116);
((GeneralPath)shape).lineTo(34.284645, 33.278595);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_6);
g.setComposite(AlphaComposite.getInstance(3, 0.15294118f * origAlpha));
AffineTransform defaultTransform__0_2_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_7
paint = new LinearGradientPaint(new Point2D.Double(33.98531723022461, 32.04590606689453), new Point2D.Double(37.21149444580078, 35.27207946777344), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(32.83193, 31.49164);
((GeneralPath)shape).curveTo(32.5721, 31.57915, 32.424583, 31.851686, 32.494167, 32.115646);
((GeneralPath)shape).lineTo(33.50745, 37.1077);
((GeneralPath)shape).curveTo(33.532726, 37.199364, 33.582794, 37.282375, 33.652206, 37.347702);
((GeneralPath)shape).lineTo(41.613724, 45.363785);
((GeneralPath)shape).curveTo(41.970535, 45.720947, 42.509182, 45.82534, 43.157776, 45.699787);
((GeneralPath)shape).curveTo(43.80637, 45.574238, 44.570164, 45.16296, 45.37735, 44.355774);
((GeneralPath)shape).curveTo(46.17787, 43.555256, 46.590458, 42.839054, 46.728397, 42.19575);
((GeneralPath)shape).curveTo(46.866333, 41.552452, 46.742413, 40.963512, 46.390633, 40.611736);
((GeneralPath)shape).lineTo(38.380863, 32.643654);
((GeneralPath)shape).curveTo(38.302124, 32.56637, 38.200752, 32.515945, 38.091354, 32.499653);
((GeneralPath)shape).lineTo(33.121437, 31.49164);
((GeneralPath)shape).curveTo(33.026863, 31.464506, 32.926502, 31.464506, 32.83193, 31.49164);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_7);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_8 = g.getTransform();
g.transform(new AffineTransform(1.649999976158142f, 0.0f, 0.0f, 1.649999976158142f, -65.625f, 12.574999809265137f));
// _0_2_8
paint = new RadialGradientPaint(new Point2D.Double(45.09462356567383, -2.693690776824951), 10.498367f, new Point2D.Double(45.09462356567383, -2.693690776824951), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-3.2942929792578377E-16f, 1.143442988395691f, -1.247217059135437f, -1.2485810430007405E-6f, 41.73500061035156f, -54.25682067871094f));
stroke = new BasicStroke(0.60606074f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(62.5, 4.5);
((GeneralPath)shape).curveTo(62.5, 10.022847, 58.022846, 14.5, 52.5, 14.5);
((GeneralPath)shape).curveTo(46.977154, 14.5, 42.5, 10.022847, 42.5, 4.5);
((GeneralPath)shape).curveTo(42.5, -1.0228475, 46.977154, -5.5, 52.5, -5.5);
((GeneralPath)shape).curveTo(58.022846, -5.5, 62.5, -1.0228475, 62.5, 4.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_8);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_9 = g.getTransform();
g.transform(new AffineTransform(1.75f, 0.0f, 0.0f, 1.75f, -70.875f, 12.125f));
// _0_2_9
paint = new LinearGradientPaint(new Point2D.Double(55.87828826904297, 12.472493171691895), new Point2D.Double(52.5, -4.62139892578125), new float[] {0.0f,1.0f}, new Color[] {new Color(136, 138, 133, 255),new Color(186, 189, 182, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(0.57142824f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(62.5, 4.5);
((GeneralPath)shape).curveTo(62.5, 10.022847, 58.022846, 14.5, 52.5, 14.5);
((GeneralPath)shape).curveTo(46.977154, 14.5, 42.5, 10.022847, 42.5, 4.5);
((GeneralPath)shape).curveTo(42.5, -1.0228475, 46.977154, -5.5, 52.5, -5.5);
((GeneralPath)shape).curveTo(58.022846, -5.5, 62.5, -1.0228475, 62.5, 4.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_9);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_10 = g.getTransform();
g.transform(new AffineTransform(1.453171968460083f, 0.0f, 0.0f, 1.4531749486923218f, -55.29153823852539f, 13.460709571838379f));
// _0_2_10
paint = new LinearGradientPaint(new Point2D.Double(54.11289978027344, 12.84677505493164), new Point2D.Double(50.07994842529297, -3.8813655376434326), new float[] {0.0f,1.0f}, new Color[] {new Color(186, 189, 182, 255),new Color(136, 138, 133, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(0.6881494f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(62.5, 4.5);
((GeneralPath)shape).curveTo(62.5, 10.022847, 58.022846, 14.5, 52.5, 14.5);
((GeneralPath)shape).curveTo(46.977154, 14.5, 42.5, 10.022847, 42.5, 4.5);
((GeneralPath)shape).curveTo(42.5, -1.0228475, 46.977154, -5.5, 52.5, -5.5);
((GeneralPath)shape).curveTo(58.022846, -5.5, 62.5, -1.0228475, 62.5, 4.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_10);
g.setComposite(AlphaComposite.getInstance(3, 0.1607843f * origAlpha));
AffineTransform defaultTransform__0_2_11 = g.getTransform();
g.transform(new AffineTransform(1.3248419761657715f, 0.0f, 0.0f, 1.3248419761657715f, 4.60508394241333f, 2.859860897064209f));
// _0_2_11
paint = new RadialGradientPaint(new Point2D.Double(8.040209770202637, 9.52802848815918), 9.8125f, new Point2D.Double(8.040209770202637, 9.52802848815918), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9468259811401367f, -1.8970430466371088E-16f, 1.8970430466371088E-16f, 0.9468259811401367f, 0.46935099363327026f, 0.4992609918117523f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.1875, 12.9375);
((GeneralPath)shape).curveTo(22.1875, 18.356794, 17.794294, 22.75, 12.375, 22.75);
((GeneralPath)shape).curveTo(6.955706, 22.75, 2.5625, 18.356794, 2.5625, 12.9375);
((GeneralPath)shape).curveTo(2.5625, 7.518206, 6.955706, 3.125, 12.375, 3.125);
((GeneralPath)shape).curveTo(17.794294, 3.125, 22.1875, 7.518206, 22.1875, 12.9375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2_11);
g.setComposite(AlphaComposite.getInstance(3, 0.7f * origAlpha));
AffineTransform defaultTransform__0_2_12 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_0
paint = new LinearGradientPaint(new Point2D.Double(17.25, 12.5625), new Point2D.Double(21.0, 28.4375), new float[] {0.0f,1.0f}, new Color[] {new Color(238, 238, 236, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.0, 10.5);
((GeneralPath)shape).curveTo(11.722565, 10.5, 11.5, 10.722565, 11.5, 11.0);
((GeneralPath)shape).lineTo(11.5, 17.5);
((GeneralPath)shape).lineTo(14.5, 17.5);
((GeneralPath)shape).lineTo(14.5, 13.5);
((GeneralPath)shape).lineTo(18.5, 13.5);
((GeneralPath)shape).lineTo(18.5, 10.5);
((GeneralPath)shape).lineTo(12.0, 10.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(23.5, 10.5);
((GeneralPath)shape).lineTo(23.5, 13.5);
((GeneralPath)shape).lineTo(27.5, 13.5);
((GeneralPath)shape).lineTo(27.5, 17.5);
((GeneralPath)shape).lineTo(30.5, 17.5);
((GeneralPath)shape).lineTo(30.5, 11.0);
((GeneralPath)shape).curveTo(30.5, 10.722565, 30.277435, 10.5, 30.0, 10.5);
((GeneralPath)shape).lineTo(23.5, 10.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(11.5, 22.5);
((GeneralPath)shape).lineTo(11.5, 29.0);
((GeneralPath)shape).curveTo(11.5, 29.277435, 11.722564, 29.5, 12.0, 29.5);
((GeneralPath)shape).lineTo(18.5, 29.5);
((GeneralPath)shape).lineTo(18.5, 26.5);
((GeneralPath)shape).lineTo(14.5, 26.5);
((GeneralPath)shape).lineTo(14.5, 22.5);
((GeneralPath)shape).lineTo(11.5, 22.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(27.5, 22.5);
((GeneralPath)shape).lineTo(27.5, 26.5);
((GeneralPath)shape).lineTo(23.5, 26.5);
((GeneralPath)shape).lineTo(23.5, 29.5);
((GeneralPath)shape).lineTo(30.0, 29.5);
((GeneralPath)shape).curveTo(30.277435, 29.5, 30.5, 29.277435, 30.5, 29.0);
((GeneralPath)shape).lineTo(30.5, 22.5);
((GeneralPath)shape).lineTo(27.5, 22.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(52, 101, 164, 255);
stroke = new BasicStroke(1.0000001f,1,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.0, 10.5);
((GeneralPath)shape).curveTo(11.722565, 10.5, 11.5, 10.722565, 11.5, 11.0);
((GeneralPath)shape).lineTo(11.5, 17.5);
((GeneralPath)shape).lineTo(14.5, 17.5);
((GeneralPath)shape).lineTo(14.5, 13.5);
((GeneralPath)shape).lineTo(18.5, 13.5);
((GeneralPath)shape).lineTo(18.5, 10.5);
((GeneralPath)shape).lineTo(12.0, 10.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(23.5, 10.5);
((GeneralPath)shape).lineTo(23.5, 13.5);
((GeneralPath)shape).lineTo(27.5, 13.5);
((GeneralPath)shape).lineTo(27.5, 17.5);
((GeneralPath)shape).lineTo(30.5, 17.5);
((GeneralPath)shape).lineTo(30.5, 11.0);
((GeneralPath)shape).curveTo(30.5, 10.722565, 30.277435, 10.5, 30.0, 10.5);
((GeneralPath)shape).lineTo(23.5, 10.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(11.5, 22.5);
((GeneralPath)shape).lineTo(11.5, 29.0);
((GeneralPath)shape).curveTo(11.5, 29.277435, 11.722564, 29.5, 12.0, 29.5);
((GeneralPath)shape).lineTo(18.5, 29.5);
((GeneralPath)shape).lineTo(18.5, 26.5);
((GeneralPath)shape).lineTo(14.5, 26.5);
((GeneralPath)shape).lineTo(14.5, 22.5);
((GeneralPath)shape).lineTo(11.5, 22.5);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(27.5, 22.5);
((GeneralPath)shape).lineTo(27.5, 26.5);
((GeneralPath)shape).lineTo(23.5, 26.5);
((GeneralPath)shape).lineTo(23.5, 29.5);
((GeneralPath)shape).lineTo(30.0, 29.5);
((GeneralPath)shape).curveTo(30.277435, 29.5, 30.5, 29.277435, 30.5, 29.0);
((GeneralPath)shape).lineTo(30.5, 22.5);
((GeneralPath)shape).lineTo(27.5, 22.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_12_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_1_0
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.5, 17.0);
((GeneralPath)shape).lineTo(12.5, 11.5);
((GeneralPath)shape).lineTo(18.0, 11.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_12_1_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_1_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_1_1
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.0, 11.5);
((GeneralPath)shape).lineTo(30.0, 11.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_12_1_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_1_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_1_2
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.5, 13.0);
((GeneralPath)shape).lineTo(28.5, 17.0);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_12_1_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_1_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_1_3
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(28.5, 23.0);
((GeneralPath)shape).lineTo(28.5, 27.5);
((GeneralPath)shape).lineTo(24.5, 27.5);
((GeneralPath)shape).lineTo(24.5, 28.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_12_1_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_1_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_1_4
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(18.0, 27.5);
((GeneralPath)shape).lineTo(14.0, 27.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_12_1_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2_12_1_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_12_1_5
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.5, 29.0);
((GeneralPath)shape).lineTo(12.5, 23.5);
((GeneralPath)shape).lineTo(14.0, 23.5);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_2_12_1_5);
g.setTransform(defaultTransform__0_2_12_1);
g.setTransform(defaultTransform__0_2_12);
g.setTransform(defaultTransform__0_2);
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
		return 47;
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
	public zoom_chooser() {
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

