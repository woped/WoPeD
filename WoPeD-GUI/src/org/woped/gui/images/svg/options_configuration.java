package org.woped.gui.images.svg;



import java.awt.*;
import java.awt.geom.*;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class options_configuration implements
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
g.setComposite(AlphaComposite.getInstance(3, 0.19886367f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(0.751118004322052f, 0.0f, 0.0f, 0.578702986240387f, 17.0408992767334f, 19.363399505615234f));
// _0_0_0
paint = new RadialGradientPaint(new Point2D.Double(24.8125, 39.125), 17.6875f, new Point2D.Double(24.8125, 39.125), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.3745580017566681f, 7.272829867098658E-15f, 24.470399856567383f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(42.5, 39.125);
((GeneralPath)shape).curveTo(42.5, 42.783886, 34.581036, 45.75, 24.8125, 45.75);
((GeneralPath)shape).curveTo(15.043963, 45.75, 7.125, 42.783886, 7.125, 39.125);
((GeneralPath)shape).curveTo(7.125, 35.466114, 15.043963, 32.5, 24.8125, 32.5);
((GeneralPath)shape).curveTo(34.581036, 32.5, 42.5, 35.466114, 42.5, 39.125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 0.3125f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(0.8360710144042969f, 0.0f, 0.0f, 0.6854360103607178f, -7.9596099853515625f, 15.71780014038086f));
// _0_0_1
paint = new RadialGradientPaint(new Point2D.Double(24.8125, 39.125), 17.6875f, new Point2D.Double(24.8125, 39.125), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.3745580017566681f, 7.194330241678919E-15f, 24.470399856567383f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(42.5, 39.125);
((GeneralPath)shape).curveTo(42.5, 42.783886, 34.581036, 45.75, 24.8125, 45.75);
((GeneralPath)shape).curveTo(15.043963, 45.75, 7.125, 42.783886, 7.125, 39.125);
((GeneralPath)shape).curveTo(7.125, 35.466114, 15.043963, 32.5, 24.8125, 32.5);
((GeneralPath)shape).curveTo(34.581036, 32.5, 42.5, 35.466114, 42.5, 39.125);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2
paint = new LinearGradientPaint(new Point2D.Double(19.64834213256836, 42.25360107421875), new Point2D.Double(20.631223678588867, 6.775803089141846), new float[] {0.0f,0.5f,0.6761296f,0.8405172f,0.875f,1.0f}, new Color[] {new Color(182, 182, 182, 255),new Color(242, 242, 242, 255),new Color(250, 250, 250, 255),new Color(216, 216, 216, 255),new Color(242, 242, 242, 255),new Color(219, 219, 219, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8782699704170227f, 0.0f, 0.0f, 0.8782699704170227f, 2.53698992729187f, 4.967679977416992f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.906713, 21.215675);
((GeneralPath)shape).lineTo(36.899303, 40.6474);
((GeneralPath)shape).curveTo(37.66779, 41.52567, 40.102814, 42.20446, 41.729786, 40.6474);
((GeneralPath)shape).curveTo(43.300915, 39.143787, 42.93741, 37.024536, 41.400436, 35.487564);
((GeneralPath)shape).lineTo(23.176332, 15.946056);
((GeneralPath)shape).curveTo(25.426332, 9.696056, 20.872444, 4.446488, 14.997444, 5.571488);
((GeneralPath)shape).lineTo(13.73493, 6.7242174);
((GeneralPath)shape).lineTo(17.687145, 10.456865);
((GeneralPath)shape).lineTo(17.906713, 13.750381);
((GeneralPath)shape).lineTo(14.955871, 16.443983);
((GeneralPath)shape).lineTo(11.429472, 16.05584);
((GeneralPath)shape).lineTo(7.8066087, 12.652544);
((GeneralPath)shape).curveTo(7.8066087, 12.652544, 6.536487, 13.907448, 6.536487, 13.907448);
((GeneralPath)shape).curveTo(5.945724, 19.548765, 11.844213, 24.590675, 17.906713, 21.215675);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(0.9999997f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.906713, 21.215675);
((GeneralPath)shape).lineTo(36.899303, 40.6474);
((GeneralPath)shape).curveTo(37.66779, 41.52567, 40.102814, 42.20446, 41.729786, 40.6474);
((GeneralPath)shape).curveTo(43.300915, 39.143787, 42.93741, 37.024536, 41.400436, 35.487564);
((GeneralPath)shape).lineTo(23.176332, 15.946056);
((GeneralPath)shape).curveTo(25.426332, 9.696056, 20.872444, 4.446488, 14.997444, 5.571488);
((GeneralPath)shape).lineTo(13.73493, 6.7242174);
((GeneralPath)shape).lineTo(17.687145, 10.456865);
((GeneralPath)shape).lineTo(17.906713, 13.750381);
((GeneralPath)shape).lineTo(14.955871, 16.443983);
((GeneralPath)shape).lineTo(11.429472, 16.05584);
((GeneralPath)shape).lineTo(7.8066087, 12.652544);
((GeneralPath)shape).curveTo(7.8066087, 12.652544, 6.536487, 13.907448, 6.536487, 13.907448);
((GeneralPath)shape).curveTo(5.945724, 19.548765, 11.844213, 24.590675, 17.906713, 21.215675);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 0.4261364f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.99999917f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(18.117386, 19.9401);
((GeneralPath)shape).lineTo(37.320267, 39.967712);
((GeneralPath)shape).curveTo(37.915173, 40.647606, 39.800194, 41.173077, 41.05968, 39.967712);
((GeneralPath)shape).curveTo(42.275932, 38.803722, 41.994534, 37.16315, 40.80472, 35.97334);
((GeneralPath)shape).lineTo(22.313189, 16.352182);
((GeneralPath)shape).curveTo(23.813189, 9.852183, 20.454401, 6.3475456, 15.454401, 6.4725456);
((GeneralPath)shape).lineTo(15.18427, 6.7459226);
((GeneralPath)shape).lineTo(18.787193, 9.982189);
((GeneralPath)shape).lineTo(18.917358, 14.163983);
((GeneralPath)shape).lineTo(15.303442, 17.462465);
((GeneralPath)shape).lineTo(11.061136, 17.004257);
((GeneralPath)shape).lineTo(7.884554, 14.012776);
((GeneralPath)shape).lineTo(7.5319166, 14.442835);
((GeneralPath)shape).curveTo(7.2194166, 20.411585, 14.023635, 23.1276, 18.117386, 19.9401);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 0.17045456f * origAlpha));
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(0.6979380249977112f, 0.7161579728126526f, -0.7161579728126526f, 0.6979380249977112f, 0.0f, 0.0f));
// _0_0_4
paint = new LinearGradientPaint(new Point2D.Double(50.152931213378906, -3.6324477195739746), new Point2D.Double(25.291086196899414, -4.300265312194824), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(0, 0, 0, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8782699704170227f, -1.3759400130359091E-15f, 1.3759400130359091E-15f, 0.8782699704170227f, 5.3282999992370605f, 1.6502399444580078f));
stroke = new BasicStroke(0.9999972f,0,0,4.0f,null,0.0f);
shape = new RoundRectangle2D.Double(28.185335159301758, -2.6184492111206055, 23.26827621459961, 2.0554912090301514, 1.767761468887329, 1.767761468887329);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_5
paint = new LinearGradientPaint(new Point2D.Double(38.22765350341797, 13.602526664733887), new Point2D.Double(37.535369873046875, 6.628589630126953), new float[] {0.0f,1.0f}, new Color[] {new Color(152, 160, 169, 255),new Color(195, 208, 221, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8782699704170227f, 0.0f, 0.0f, 0.8782699704170227f, 2.8475000858306885f, 5.588709831237793f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.498795, 30.12538);
((GeneralPath)shape).curveTo(23.332335, 29.410917, 35.782627, 16.67687, 35.782627, 16.67687);
((GeneralPath)shape).lineTo(38.85657, 16.457302);
((GeneralPath)shape).lineTo(43.687057, 9.76049);
((GeneralPath)shape).lineTo(39.66273, 6.1752987);
((GeneralPath)shape).lineTo(33.405056, 11.554705);
((GeneralPath)shape).lineTo(33.405056, 14.628651);
((GeneralPath)shape).lineTo(20.670141, 27.857594);
((GeneralPath)shape).curveTo(20.066332, 28.461403, 21.730309, 30.784082, 22.498795, 30.12538);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(135, 143, 157, 255);
stroke = new BasicStroke(0.9999997f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.498795, 30.12538);
((GeneralPath)shape).curveTo(23.332335, 29.410917, 35.782627, 16.67687, 35.782627, 16.67687);
((GeneralPath)shape).lineTo(38.85657, 16.457302);
((GeneralPath)shape).lineTo(43.687057, 9.76049);
((GeneralPath)shape).lineTo(39.66273, 6.1752987);
((GeneralPath)shape).lineTo(33.405056, 11.554705);
((GeneralPath)shape).lineTo(33.405056, 14.628651);
((GeneralPath)shape).lineTo(20.670141, 27.857594);
((GeneralPath)shape).curveTo(20.066332, 28.461403, 21.730309, 30.784082, 22.498795, 30.12538);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_5);
g.setComposite(AlphaComposite.getInstance(3, 0.53977275f * origAlpha));
AffineTransform defaultTransform__0_0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_6
paint = new LinearGradientPaint(new Point2D.Double(31.177404403686523, 19.821514129638672), new Point2D.Double(40.85917663574219, 9.656853675842285), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0000002f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(22.401987, 29.085455);
((GeneralPath)shape).curveTo(23.04876, 28.531078, 35.426388, 15.855648, 35.426388, 15.855648);
((GeneralPath)shape).lineTo(38.354973, 15.607649);
((GeneralPath)shape).lineTo(42.568886, 9.945584);
((GeneralPath)shape).lineTo(39.679157, 7.3965945);
((GeneralPath)shape).lineTo(34.20258, 12.114067);
((GeneralPath)shape).lineTo(34.357838, 14.965022);
((GeneralPath)shape).lineTo(21.68173, 28.257345);
((GeneralPath)shape).curveTo(21.213213, 28.725863, 21.805693, 29.596565, 22.401987, 29.085455);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_6);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7
paint = new LinearGradientPaint(new Point2D.Double(9.750324249267578, 32.28376007080078), new Point2D.Double(16.91529655456543, 39.44321823120117), new float[] {0.0f,1.0E-9f,0.0f,0.75f,1.0f}, new Color[] {new Color(52, 101, 164, 255),new Color(159, 188, 225, 255),new Color(107, 149, 202, 255),new Color(61, 106, 165, 255),new Color(56, 110, 180, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8782699704170227f, 0.0f, 0.0f, 0.8782699704170227f, 2.53698992729187f, 4.967679977416992f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(8.465311, 43.61156);
((GeneralPath)shape).curveTo(9.7818985, 45.07679, 13.438996, 45.739727, 15.060755, 42.901646);
((GeneralPath)shape).curveTo(15.767862, 41.66421, 17.154697, 38.198845, 23.341883, 32.63038);
((GeneralPath)shape).curveTo(24.38103, 31.696207, 25.481792, 29.55924, 24.54863, 28.406511);
((GeneralPath)shape).lineTo(22.133387, 25.991268);
((GeneralPath)shape).curveTo(21.145334, 24.893433, 18.398973, 25.40552, 17.272211, 26.942144);
((GeneralPath)shape).curveTo(13.913455, 31.53834, 8.42614, 35.197025, 7.1887026, 35.638966);
((GeneralPath)shape).curveTo(4.8207827, 36.484653, 5.0872917, 39.975117, 6.653879, 41.635452);
((GeneralPath)shape).lineTo(8.465311, 43.61156);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(32, 74, 135, 255);
stroke = new BasicStroke(0.9999997f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(8.465311, 43.61156);
((GeneralPath)shape).curveTo(9.7818985, 45.07679, 13.438996, 45.739727, 15.060755, 42.901646);
((GeneralPath)shape).curveTo(15.767862, 41.66421, 17.154697, 38.198845, 23.341883, 32.63038);
((GeneralPath)shape).curveTo(24.38103, 31.696207, 25.481792, 29.55924, 24.54863, 28.406511);
((GeneralPath)shape).lineTo(22.133387, 25.991268);
((GeneralPath)shape).curveTo(21.145334, 24.893433, 18.398973, 25.40552, 17.272211, 26.942144);
((GeneralPath)shape).curveTo(13.913455, 31.53834, 8.42614, 35.197025, 7.1887026, 35.638966);
((GeneralPath)shape).curveTo(4.8207827, 36.484653, 5.0872917, 39.975117, 6.653879, 41.635452);
((GeneralPath)shape).lineTo(8.465311, 43.61156);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_7);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_8 = g.getTransform();
g.transform(new AffineTransform(0.8782699704170227f, 0.0f, 0.0f, 0.8782699704170227f, 2.4272000789642334f, 5.077459812164307f));
// _0_0_8
paint = new Color(255, 255, 255, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(43.25, 37.5);
((GeneralPath)shape).curveTo(43.25, 38.25939, 42.63439, 38.875, 41.875, 38.875);
((GeneralPath)shape).curveTo(41.11561, 38.875, 40.5, 38.25939, 40.5, 37.5);
((GeneralPath)shape).curveTo(40.5, 36.74061, 41.11561, 36.125, 41.875, 36.125);
((GeneralPath)shape).curveTo(42.63439, 36.125, 43.25, 36.74061, 43.25, 37.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(161, 161, 161, 255);
stroke = new BasicStroke(1.1386017f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(43.25, 37.5);
((GeneralPath)shape).curveTo(43.25, 38.25939, 42.63439, 38.875, 41.875, 38.875);
((GeneralPath)shape).curveTo(41.11561, 38.875, 40.5, 38.25939, 40.5, 37.5);
((GeneralPath)shape).curveTo(40.5, 36.74061, 41.11561, 36.125, 41.875, 36.125);
((GeneralPath)shape).curveTo(42.63439, 36.125, 43.25, 36.74061, 43.25, 37.5);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_8);
g.setComposite(AlphaComposite.getInstance(3, 0.60227275f * origAlpha));
AffineTransform defaultTransform__0_0_9 = g.getTransform();
g.transform(new AffineTransform(0.5708760023117065f, 0.0f, 0.0f, 0.5708760023117065f, 9.154850006103516f, 11.251099586486816f));
// _0_0_9
paint = new Color(255, 255, 255, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.771261, 28.20101);
((GeneralPath)shape).curveTo(20.771261, 29.17732, 19.979805, 29.968777, 19.003494, 29.968777);
((GeneralPath)shape).curveTo(18.027184, 29.968777, 17.235727, 29.17732, 17.235727, 28.20101);
((GeneralPath)shape).curveTo(17.235727, 27.224699, 18.027184, 26.433243, 19.003494, 26.433243);
((GeneralPath)shape).curveTo(19.979805, 26.433243, 20.771261, 27.224699, 20.771261, 28.20101);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_9);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_10
paint = new LinearGradientPaint(new Point2D.Double(12.0046968460083, 35.68846130371094), new Point2D.Double(10.650805473327637, 33.19496536254883), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0072499513626099f, -0.02636530064046383f, 0.02636530064046383f, 1.0072499513626099f, 1.5934100151062012f, 0.07919099926948547f));
stroke = new BasicStroke(2.2945092f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(18.678905, 29.624807);
((GeneralPath)shape).curveTo(18.678905, 29.624807, 11.509014, 36.92442, 8.150257, 38.161858);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_10);
g.setComposite(AlphaComposite.getInstance(3, 0.19886364f * origAlpha));
AffineTransform defaultTransform__0_0_11 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_11
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.99999946f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(8.806002, 42.48669);
((GeneralPath)shape).curveTo(10.247267, 44.232307, 13.405535, 44.64792, 14.397161, 42.1161);
((GeneralPath)shape).curveTo(15.078468, 40.37659, 17.730783, 36.450314, 22.594746, 32.072746);
((GeneralPath)shape).curveTo(23.411654, 31.338364, 24.277002, 29.658419, 23.543411, 28.752218);
((GeneralPath)shape).lineTo(21.644705, 26.853512);
((GeneralPath)shape).curveTo(20.867962, 25.990463, 18.708952, 26.393032, 17.823164, 27.601028);
((GeneralPath)shape).curveTo(15.182728, 31.214256, 9.33982, 35.940582, 7.9274144, 36.406654);
((GeneralPath)shape).curveTo(5.7406197, 37.128265, 6.150422, 39.627953, 7.3819714, 40.933205);
((GeneralPath)shape).lineTo(8.806002, 42.48669);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_11);
g.setComposite(AlphaComposite.getInstance(3, 0.27840912f * origAlpha));
AffineTransform defaultTransform__0_0_12 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_12
paint = new LinearGradientPaint(new Point2D.Double(14.017541885375977, 36.942543029785156), new Point2D.Double(15.415793418884277, 38.268367767333984), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8780990242958069f, -0.017323700711131096f, 0.017323700711131096f, 0.8780990242958069f, 2.1636900901794434f, 4.06790018081665f));
stroke = new BasicStroke(2.2945092f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.824602, 31.261024);
((GeneralPath)shape).curveTo(20.824602, 31.261024, 13.501839, 37.87843, 11.910849, 42.12107);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_12);
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
        return 5;
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
		return 43;
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
	public options_configuration() {
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

