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
public class help_reportbug implements
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
g.transform(new AffineTransform(1.5969699621200562f, 0.0f, 0.0f, 1.5260599851608276f, 3.680229902267456f, -19.70949935913086f));
// _0_0_0
paint = new RadialGradientPaint(new Point2D.Double(6.7027130126953125, 73.61571502685547), 7.228416f, new Point2D.Double(6.7027130126953125, 73.61571502685547), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.9022200107574463f, 0.0f, 0.0f, 0.5257030129432678f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.5, 38.7);
((GeneralPath)shape).curveTo(26.5, 40.798683, 20.343916, 42.5, 12.75, 42.5);
((GeneralPath)shape).curveTo(5.1560845, 42.5, -1.0, 40.798683, -1.0, 38.7);
((GeneralPath)shape).curveTo(-1.0, 36.60132, 5.1560845, 34.9, 12.75, 34.9);
((GeneralPath)shape).curveTo(20.343916, 34.9, 26.5, 36.60132, 26.5, 38.7);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.004729986190796f, 0.0f, 0.0f, 1.00600004196167f, 0.054565198719501495f, -9.119159698486328f));
// _0_0_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_0
paint = new LinearGradientPaint(new Point2D.Double(11.57284164428711, 4.746162414550781), new Point2D.Double(18.47528648376465, 26.02290916442871), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(226, 226, 226, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.3434699773788452f, 0.0f, 0.0f, 1.5058499574661255f, 2.879509925842285f, -2.2660200595855713f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.34375, 15.454879);
((GeneralPath)shape).lineTo(6.34375, 41.44216);
((GeneralPath)shape).lineTo(43.3125, 41.44216);
((GeneralPath)shape).lineTo(43.25, 15.554447);
((GeneralPath)shape).curveTo(43.24999, 15.548732, 43.250374, 15.527358, 43.25, 15.521258);
((GeneralPath)shape).curveTo(43.249268, 15.514776, 43.251087, 15.494928, 43.25, 15.488068);
((GeneralPath)shape).curveTo(43.24856, 15.480833, 43.22054, 15.462487, 43.21875, 15.454879);
((GeneralPath)shape).lineTo(6.34375, 15.454879);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(136, 138, 133, 255);
stroke = new BasicStroke(0.99466485f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.34375, 15.454879);
((GeneralPath)shape).lineTo(6.34375, 41.44216);
((GeneralPath)shape).lineTo(43.3125, 41.44216);
((GeneralPath)shape).lineTo(43.25, 15.554447);
((GeneralPath)shape).curveTo(43.24999, 15.548732, 43.250374, 15.527358, 43.25, 15.521258);
((GeneralPath)shape).curveTo(43.249268, 15.514776, 43.251087, 15.494928, 43.25, 15.488068);
((GeneralPath)shape).curveTo(43.24856, 15.480833, 43.22054, 15.462487, 43.21875, 15.454879);
((GeneralPath)shape).lineTo(6.34375, 15.454879);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_1
paint = new LinearGradientPaint(new Point2D.Double(9.164306640625, 38.070892333984375), new Point2D.Double(9.885503768920898, 52.09067916870117), new float[] {0.0f,0.23809524f,1.0f}, new Color[] {new Color(223, 224, 223, 255),new Color(166, 176, 166, 255),new Color(181, 190, 181, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.454780101776123f, 0.0f, 0.0f, 0.762004017829895f, 2.8817501068115234f, 0.33738601207733154f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.490673, 29.058712);
((GeneralPath)shape).lineTo(7.09471, 40.0307);
((GeneralPath)shape).lineTo(21.003551, 30.426394);
((GeneralPath)shape).lineTo(30.02171, 30.426394);
((GeneralPath)shape).lineTo(42.440758, 39.90859);
((GeneralPath)shape).lineTo(30.577332, 29.058712);
((GeneralPath)shape).lineTo(20.490673, 29.058712);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_1_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_2
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.9946648f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(7.34375, 16.733862);
((GeneralPath)shape).curveTo(7.3370547, 16.747198, 7.349144, 16.753506, 7.34375, 16.765453);
((GeneralPath)shape).curveTo(7.3413825, 16.771078, 7.3145366, 16.791779, 7.3125, 16.797049);
((GeneralPath)shape).curveTo(7.3107977, 16.801958, 7.313866, 16.824087, 7.3125, 16.82864);
((GeneralPath)shape).curveTo(7.311473, 16.83283, 7.3131857, 16.85641, 7.3125, 16.860233);
((GeneralPath)shape).lineTo(7.34375, 40.333652);
((GeneralPath)shape).lineTo(42.28125, 40.333652);
((GeneralPath)shape).lineTo(42.21875, 16.986605);
((GeneralPath)shape).curveTo(42.218063, 16.982891, 42.219772, 16.959093, 42.21875, 16.955011);
((GeneralPath)shape).curveTo(42.20409, 16.906933, 42.17692, 16.822855, 42.125, 16.733862);
((GeneralPath)shape).lineTo(7.34375, 16.733862);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_3
paint = new RadialGradientPaint(new Point2D.Double(27.741130828857422, 38.71150588989258), 17.977943f, new Point2D.Double(27.741130828857422, 38.71150588989258), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 33),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.6299290060997009f, 0.45937299728393555f, -0.14767499268054962f, 0.24851199984550476f, 16.517200469970703f, 9.053739547729492f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(23.329298, 32.99672);
((GeneralPath)shape).curveTo(20.93719, 32.550377, 7.9003873, 18.771126, 6.596606, 16.372023);
((GeneralPath)shape).curveTo(6.5816493, 16.343449, 6.5559707, 16.288609, 6.5446897, 16.2636);
((GeneralPath)shape).lineTo(41.057804, 16.2636);
((GeneralPath)shape).curveTo(40.780724, 18.766403, 33.533577, 32.769344, 31.496525, 32.99672);
((GeneralPath)shape).curveTo(31.488352, 32.99719, 31.475246, 32.99672, 31.46725, 32.99672);
((GeneralPath)shape).lineTo(23.446392, 32.99672);
((GeneralPath)shape).curveTo(23.412766, 32.99672, 23.368837, 33.0041, 23.329298, 32.99672);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_1_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_4
paint = new LinearGradientPaint(new Point2D.Double(11.742170333862305, 11.484869956970215), new Point2D.Double(13.846982955932617, 11.98198127746582), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(226, 226, 226, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.2960200309753418f, 0.0f, 0.0f, -1.4369200468063354f, 3.746579885482788f, 33.2052001953125f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.77475, 31.085394);
((GeneralPath)shape).curveTo(18.407309, 30.694258, 7.945269, 18.619434, 7.118584, 16.51709);
((GeneralPath)shape).curveTo(7.109327, 16.49205, 7.094677, 16.443993, 7.088438, 16.42208);
((GeneralPath)shape).lineTo(42.630646, 16.42208);
((GeneralPath)shape).curveTo(41.80703, 18.6153, 31.332195, 30.886145, 29.185501, 31.085394);
((GeneralPath)shape).curveTo(29.176985, 31.085802, 29.16359, 31.085394, 29.155355, 31.085394);
((GeneralPath)shape).lineTo(20.895334, 31.085394);
((GeneralPath)shape).curveTo(20.860706, 31.085394, 20.81388, 31.091858, 20.77475, 31.085394);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(152, 152, 152, 255);
stroke = new BasicStroke(0.8520339f,0,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.77475, 31.085394);
((GeneralPath)shape).curveTo(18.407309, 30.694258, 7.945269, 18.619434, 7.118584, 16.51709);
((GeneralPath)shape).curveTo(7.109327, 16.49205, 7.094677, 16.443993, 7.088438, 16.42208);
((GeneralPath)shape).lineTo(42.630646, 16.42208);
((GeneralPath)shape).curveTo(41.80703, 18.6153, 31.332195, 30.886145, 29.185501, 31.085394);
((GeneralPath)shape).curveTo(29.176985, 31.085802, 29.16359, 31.085394, 29.155355, 31.085394);
((GeneralPath)shape).lineTo(20.895334, 31.085394);
((GeneralPath)shape).curveTo(20.860706, 31.085394, 20.81388, 31.091858, 20.77475, 31.085394);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_5
paint = new LinearGradientPaint(new Point2D.Double(10.027000427246094, 20.21976089477539), new Point2D.Double(17.178024291992188, -7.527464389801025), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(226, 226, 226, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.5706100463867188f, 0.0f, 0.0f, -1.231510043144226f, 2.973439931869507f, 33.33489990234375f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.625174, 30.490479);
((GeneralPath)shape).curveTo(18.51921, 29.999928, 7.7224803, 17.98771, 7.0314245, 16.466377);
((GeneralPath)shape).curveTo(7.028888, 16.460379, 7.033602, 16.43969, 7.0314245, 16.434063);
((GeneralPath)shape).curveTo(7.0259733, 16.418306, 7.002328, 16.381763, 7.0001745, 16.369436);
((GeneralPath)shape).curveTo(7.000203, 16.366104, 6.9997683, 16.34006, 7.0001745, 16.337122);
((GeneralPath)shape).curveTo(7.0013437, 16.334982, 7.0298696, 16.33886, 7.0314245, 16.337122);
((GeneralPath)shape).lineTo(7.1251745, 16.240181);
((GeneralPath)shape).lineTo(42.593925, 16.240181);
((GeneralPath)shape).curveTo(42.59121, 16.264507, 42.57124, 16.307055, 42.562675, 16.337122);
((GeneralPath)shape).curveTo(42.555172, 16.360727, 42.542103, 16.407354, 42.531425, 16.434063);
((GeneralPath)shape).curveTo(41.609325, 18.615, 31.023436, 30.200512, 29.187674, 30.490479);
((GeneralPath)shape).curveTo(29.172747, 30.492123, 29.138826, 30.490479, 29.125174, 30.490479);
((GeneralPath)shape).lineTo(20.750174, 30.490479);
((GeneralPath)shape).curveTo(20.719887, 30.488811, 20.66042, 30.49869, 20.625174, 30.490479);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_1_5);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_6
paint = new LinearGradientPaint(new Point2D.Double(11.841544151306152, 4.250730514526367), new Point2D.Double(40.0240592956543, 7.412107467651367), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(237, 237, 237, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.3709299564361572f, 0.0f, 0.0f, -1.4645600318908691f, 2.525059938430786f, 33.71269989013672f));
stroke = new BasicStroke(0.85203373f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.875174, 30.051142);
((GeneralPath)shape).curveTo(18.427216, 29.50167, 8.704, 18.433899, 7.5314245, 16.451725);
((GeneralPath)shape).lineTo(42.125175, 16.451725);
((GeneralPath)shape).curveTo(40.634987, 18.784897, 31.078503, 29.863516, 28.968924, 30.051142);
((GeneralPath)shape).curveTo(28.96018, 30.051542, 28.946142, 30.051142, 28.937674, 30.051142);
((GeneralPath)shape).lineTo(21.031424, 30.051142);
((GeneralPath)shape).curveTo(21.00503, 30.051142, 20.966541, 30.054691, 20.937674, 30.051142);
((GeneralPath)shape).curveTo(20.917889, 30.047995, 20.896025, 30.05582, 20.875174, 30.051142);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_1_6);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1_7
paint = new LinearGradientPaint(new Point2D.Double(17.39720344543457, 33.35737609863281), new Point2D.Double(22.177709579467773, 31.02674102783203), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.95951, 30.447113);
((GeneralPath)shape).lineTo(9.018012, 38.717968);
((GeneralPath)shape).lineTo(11.237445, 38.724075);
((GeneralPath)shape).lineTo(21.23557, 31.855137);
((GeneralPath)shape).lineTo(30.057478, 30.432299);
((GeneralPath)shape).lineTo(20.95951, 30.447113);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_1_7);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(0.2831900119781494f, 0.0f, 0.0f, 0.27343299984931946f, 18.54509925842285f, 14.819700241088867f));
// _0_0_2
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_0
paint = new RadialGradientPaint(new Point2D.Double(28.789100646972656, 20.971200942993164), 13.2807f, new Point2D.Double(28.789100646972656, 20.971200942993164), new float[] {0.0f,1.0f}, new Color[] {new Color(228, 30, 8, 255),new Color(144, 21, 5, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.3103899955749512f, 0.0f, 0.0f, 1.3103899955749512f, -7.051690101623535f, 2.8718700408935547f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.526474, 16.494654);
((GeneralPath)shape).curveTo(37.792797, 19.424309, 40.017414, 19.27525, 40.072433, 23.185474);
((GeneralPath)shape).curveTo(40.10381, 25.366167, 41.82834, 26.511229, 41.82834, 28.738884);
((GeneralPath)shape).curveTo(41.82834, 30.96654, 37.981052, 32.067253, 36.670666, 33.377636);
((GeneralPath)shape).curveTo(35.36028, 34.688026, 36.774044, 38.048435, 33.21911, 39.035915);
((GeneralPath)shape).curveTo(29.697956, 40.014015, 27.977564, 38.642803, 25.749907, 38.642803);
((GeneralPath)shape).curveTo(23.522247, 38.642803, 18.1916, 42.938225, 16.22602, 40.972645);
((GeneralPath)shape).curveTo(14.26044, 39.007065, 14.218504, 34.711643, 12.515002, 33.00814);
((GeneralPath)shape).curveTo(10.68046, 31.173597, 7.781911, 28.610462, 7.781911, 25.334496);
((GeneralPath)shape).curveTo(7.781911, 22.05853, 9.673644, 20.329124, 12.383964, 17.283503);
((GeneralPath)shape).curveTo(15.180328, 14.141192, 15.266814, 9.421184, 18.54278, 9.421184);
((GeneralPath)shape).curveTo(21.818747, 9.421184, 23.074402, 10.319816, 25.862627, 10.24935);
((GeneralPath)shape).curveTo(30.307198, 10.137024, 30.851856, 8.023086, 34.120678, 9.680644);
((GeneralPath)shape).curveTo(37.002735, 11.14208, 35.53517, 14.201256, 36.526474, 16.494654);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(76, 9, 1, 255);
stroke = new BasicStroke(1.0f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.526474, 16.494654);
((GeneralPath)shape).curveTo(37.792797, 19.424309, 40.017414, 19.27525, 40.072433, 23.185474);
((GeneralPath)shape).curveTo(40.10381, 25.366167, 41.82834, 26.511229, 41.82834, 28.738884);
((GeneralPath)shape).curveTo(41.82834, 30.96654, 37.981052, 32.067253, 36.670666, 33.377636);
((GeneralPath)shape).curveTo(35.36028, 34.688026, 36.774044, 38.048435, 33.21911, 39.035915);
((GeneralPath)shape).curveTo(29.697956, 40.014015, 27.977564, 38.642803, 25.749907, 38.642803);
((GeneralPath)shape).curveTo(23.522247, 38.642803, 18.1916, 42.938225, 16.22602, 40.972645);
((GeneralPath)shape).curveTo(14.26044, 39.007065, 14.218504, 34.711643, 12.515002, 33.00814);
((GeneralPath)shape).curveTo(10.68046, 31.173597, 7.781911, 28.610462, 7.781911, 25.334496);
((GeneralPath)shape).curveTo(7.781911, 22.05853, 9.673644, 20.329124, 12.383964, 17.283503);
((GeneralPath)shape).curveTo(15.180328, 14.141192, 15.266814, 9.421184, 18.54278, 9.421184);
((GeneralPath)shape).curveTo(21.818747, 9.421184, 23.074402, 10.319816, 25.862627, 10.24935);
((GeneralPath)shape).curveTo(30.307198, 10.137024, 30.851856, 8.023086, 34.120678, 9.680644);
((GeneralPath)shape).curveTo(37.002735, 11.14208, 35.53517, 14.201256, 36.526474, 16.494654);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_1
paint = new RadialGradientPaint(new Point2D.Double(26.887432098388672, 20.479358673095703), 14.6944f, new Point2D.Double(26.887432098388672, 20.479358673095703), new float[] {0.0f,1.0f}, new Color[] {new Color(228, 30, 8, 255),new Color(144, 21, 5, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.1355500221252441f, 0.0f, 0.0f, 1.1355500221252441f, -2.9493300914764404f, 5.17041015625f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(36.56784, 24.815445);
((GeneralPath)shape).curveTo(36.56784, 31.060974, 31.45786, 36.170956, 25.21233, 36.170956);
((GeneralPath)shape).curveTo(18.966799, 36.170956, 13.85682, 31.060974, 13.85682, 24.815445);
((GeneralPath)shape).curveTo(13.85682, 18.569914, 18.966799, 13.459934, 25.21233, 13.459934);
((GeneralPath)shape).curveTo(31.45786, 13.459934, 36.56784, 18.569914, 36.56784, 24.815445);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_2_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_2
paint = new LinearGradientPaint(new Point2D.Double(15.338735580444336, 5.924462795257568), new Point2D.Double(43.1409797668457, 49.89507293701172), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 161, 150, 255),new Color(255, 31, 6, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9757279753684998f, 0.0f, 0.0f, 0.9310330152511597f, 1.4589899778366089f, 9.226240158081055f));
stroke = new BasicStroke(1.0f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(35.47996, 17.27797);
((GeneralPath)shape).curveTo(36.63322, 19.946056, 38.65922, 19.810305, 38.709328, 23.371416);
((GeneralPath)shape).curveTo(38.737904, 25.35741, 40.30846, 26.400236, 40.30846, 28.429003);
((GeneralPath)shape).curveTo(40.30846, 30.457767, 36.80467, 31.460203, 35.61128, 32.653595);
((GeneralPath)shape).curveTo(34.417885, 33.84699, 35.705425, 36.90737, 32.467888, 37.806686);
((GeneralPath)shape).curveTo(29.26111, 38.697456, 27.694319, 37.448673, 25.665552, 37.448673);
((GeneralPath)shape).curveTo(23.636787, 37.448673, 18.782074, 41.360588, 16.991985, 39.5705);
((GeneralPath)shape).curveTo(15.201898, 37.780415, 15.163706, 33.868496, 13.612297, 32.317085);
((GeneralPath)shape).curveTo(11.941548, 30.646338, 9.301789, 28.312046, 9.301789, 25.328568);
((GeneralPath)shape).curveTo(9.301789, 22.345087, 11.024624, 20.770088, 13.492958, 17.996387);
((GeneralPath)shape).curveTo(16.039656, 15.134631, 16.11842, 10.836037, 19.101898, 10.836037);
((GeneralPath)shape).curveTo(22.085379, 10.836037, 23.228926, 11.654438, 25.768211, 11.590263);
((GeneralPath)shape).curveTo(29.815958, 11.487966, 30.311989, 9.562765, 33.28896, 11.072332);
((GeneralPath)shape).curveTo(35.9137, 12.403288, 34.577164, 15.189332, 35.47996, 17.27797);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_3
paint = new LinearGradientPaint(new Point2D.Double(18.570419311523438, 9.443662643432617), new Point2D.Double(29.957399368286133, 22.78322410583496), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 63),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0286699533462524f, 0.0f, 0.0f, 0.9721289873123169f, 0.3106600046157837f, 8.437179565429688f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.25424, 14.300304);
((GeneralPath)shape).curveTo(19.386423, 14.300304, 14.607978, 19.042538, 14.607978, 24.910355);
((GeneralPath)shape).curveTo(14.607978, 28.581348, 16.591682, 31.690983, 19.424145, 33.60118);
((GeneralPath)shape).curveTo(26.664616, 29.70422, 25.377949, 22.795994, 35.03142, 20.854633);
((GeneralPath)shape).curveTo(33.432415, 17.005531, 29.679832, 14.300304, 25.25424, 14.300304);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_2_3);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(0.5615630149841309f, 0.0f, 0.0f, 0.6691679954528809f, 19.922000885009766f, 14.109600067138672f));
// _0_0_3
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3_0 = g.getTransform();
g.transform(new AffineTransform(0.6181550025939941f, 0.7518050074577332f, -0.7518050074577332f, 0.6181550025939941f, 27.75349998474121f, -0.38506901264190674f));
// _0_0_3_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_0_0
paint = new LinearGradientPaint(new Point2D.Double(21.587093353271484, 23.499000549316406), new Point2D.Double(21.587093353271484, 2.8163671493530273), new float[] {0.0f,1.0f}, new Color[] {new Color(114, 159, 207, 255),new Color(175, 201, 228, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.106909990310669f, -0.2584039866924286f, 0.25974801182746887f, 1.1016700267791748f, -19.66699981689453f, 12.19789981842041f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(1.349542, 35.242012);
((GeneralPath)shape).lineTo(7.245074, 40.036877);
((GeneralPath)shape).lineTo(20.092052, 24.392048);
((GeneralPath)shape).lineTo(24.022408, 27.588623);
((GeneralPath)shape).lineTo(32.220425, 1.6394368);
((GeneralPath)shape).lineTo(10.266168, 16.400606);
((GeneralPath)shape).lineTo(14.19652, 19.597183);
((GeneralPath)shape).lineTo(1.349542, 35.242012);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(52, 101, 164, 255);
stroke = new BasicStroke(0.9999991f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(1.349542, 35.242012);
((GeneralPath)shape).lineTo(7.245074, 40.036877);
((GeneralPath)shape).lineTo(20.092052, 24.392048);
((GeneralPath)shape).lineTo(24.022408, 27.588623);
((GeneralPath)shape).lineTo(32.220425, 1.6394368);
((GeneralPath)shape).lineTo(10.266168, 16.400606);
((GeneralPath)shape).lineTo(14.19652, 19.597183);
((GeneralPath)shape).lineTo(1.349542, 35.242012);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_3_0_0);
g.setComposite(AlphaComposite.getInstance(3, 0.5714286f * origAlpha));
AffineTransform defaultTransform__0_0_3_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_0_1
paint = new LinearGradientPaint(new Point2D.Double(25.95013427734375, 2.8703360557556152), new Point2D.Double(29.477813720703125, 46.06208038330078), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.106909990310669f, -0.2584039866924286f, 0.25974801182746887f, 1.1016700267791748f, -19.66699981689453f, 12.19789981842041f));
stroke = new BasicStroke(0.99999946f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(2.982173, 35.037884);
((GeneralPath)shape).lineTo(7.054945, 38.437515);
((GeneralPath)shape).lineTo(19.91051, 22.73824);
((GeneralPath)shape).lineTo(23.42135, 25.60654);
((GeneralPath)shape).lineTo(30.103682, 4.4964604);
((GeneralPath)shape).lineTo(12.144203, 16.461586);
((GeneralPath)shape).lineTo(15.824755, 19.393003);
((GeneralPath)shape).lineTo(2.982173, 35.037884);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_3_0_1);
g.setTransform(defaultTransform__0_0_3_0);
g.setTransform(defaultTransform__0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 0.3f * origAlpha));
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.109089970588684f, 0.0f, 0.0f, 0.8620499968528748f, 18.785900115966797f, 7.606790065765381f));
// _0_0_4
paint = new RadialGradientPaint(new Point2D.Double(7.2054595947265625, 75.56610870361328), 7.228416f, new Point2D.Double(7.2054595947265625, 75.56610870361328), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.9022200107574463f, 0.0f, 0.0f, 0.35548698902130127f, 0.0f, 12.862600326538086f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.5, 38.7);
((GeneralPath)shape).curveTo(26.5, 40.798683, 20.343916, 42.5, 12.75, 42.5);
((GeneralPath)shape).curveTo(5.1560845, 42.5, -1.0, 40.798683, -1.0, 38.7);
((GeneralPath)shape).curveTo(-1.0, 36.60132, 5.1560845, 34.9, 12.75, 34.9);
((GeneralPath)shape).curveTo(20.343916, 34.9, 26.5, 36.60132, 26.5, 38.7);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_4);
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
        return 6;
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
	public help_reportbug() {
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

