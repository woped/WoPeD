package org.woped.gui.images.svg;

import java.awt.*;
import java.awt.geom.*;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class apromore_export implements
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -0.0f, -0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 0.4091f * origAlpha));
AffineTransform defaultTransform__0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
paint = new RadialGradientPaint(new Point2D.Double(-280.9945983886719, 1104.888671875), 9.687f, new Point2D.Double(-280.9945983886719, 1104.888671875), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.063499927520752f, 0.0f, 0.0f, 0.734499990940094f, 606.1962280273438f, -772.3687744140625f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(46.355, 39.172);
((GeneralPath)shape).curveTo(46.355, 43.102, 37.406, 46.287003, 26.365, 46.287003);
((GeneralPath)shape).curveTo(15.324001, 46.287003, 6.375, 43.101, 6.375, 39.172005);
((GeneralPath)shape).curveTo(6.375, 35.243004, 15.325, 32.057007, 26.365, 32.057007);
((GeneralPath)shape).curveTo(37.406, 32.057, 46.355, 35.242, 46.355, 39.172);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
paint = new Color(212, 87, 45, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.269, 35.503);
((GeneralPath)shape).curveTo(24.324, 35.42, 25.210999, 36.565998, 25.210999, 36.565998);
((GeneralPath)shape).curveTo(29.103998, 32.279, 39.038998, 24.266998, 41.75, 17.584997);
((GeneralPath)shape).curveTo(36.27, 23.74, 27.313, 33.641, 24.269, 35.503);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
paint = new LinearGradientPaint(new Point2D.Double(70.44339752197266, 1728.4365234375), new Point2D.Double(60.47420120239258, 1711.8472900390625), new float[] {0.0f,1.0f}, new Color[] {new Color(245, 161, 8, 0),new Color(254, 253, 195, 189)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.8952000141143799f, 0.0f, 0.0f, 0.46639999747276306f, -35.451900482177734f, -788.1837768554688f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.561, 7.918);
((GeneralPath)shape).curveTo(13.025002, 7.6920004, 10.912001, 10.642, 9.769001, 13.448);
((GeneralPath)shape).curveTo(8.635001, 17.143, 10.238001, 24.123001, 18.096, 23.592);
((GeneralPath)shape).curveTo(22.578001, 23.592, 24.359001, 18.939, 26.718, 17.730999);
((GeneralPath)shape).curveTo(28.864, 16.911, 29.031, 16.837, 31.296001, 16.837);
((GeneralPath)shape).curveTo(34.443, 16.837, 40.137, 14.531, 40.137, 12.243);
((GeneralPath)shape).curveTo(39.168, 10.407, 36.600002, 7.138, 31.880001, 7.695);
((GeneralPath)shape).curveTo(28.875, 8.049, 26.032001, 10.223001, 25.29, 11.305);
((GeneralPath)shape).curveTo(23.471, 9.68, 21.148, 7.918, 17.561, 7.918);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3
paint = new Color(199, 75, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.841, 9.372);
((GeneralPath)shape).curveTo(33.69, 1.4259996, 44.476997, 8.75, 44.404, 16.286);
((GeneralPath)shape).curveTo(44.34, 23.175, 39.154, 26.473999, 26.125, 38.725998);
((GeneralPath)shape).curveTo(12.487, 27.328, 7.682, 24.699, 7.752, 16.525);
((GeneralPath)shape).curveTo(7.824, 8.064, 17.058, 1.715, 25.841, 9.372);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(174, 51, 0, 255);
stroke = new BasicStroke(0.9511f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.841, 9.372);
((GeneralPath)shape).curveTo(33.69, 1.4259996, 44.476997, 8.75, 44.404, 16.286);
((GeneralPath)shape).curveTo(44.34, 23.175, 39.154, 26.473999, 26.125, 38.725998);
((GeneralPath)shape).curveTo(12.487, 27.328, 7.682, 24.699, 7.752, 16.525);
((GeneralPath)shape).curveTo(7.824, 8.064, 17.058, 1.715, 25.841, 9.372);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_3);
g.setComposite(AlphaComposite.getInstance(3, 0.625f * origAlpha));
AffineTransform defaultTransform__0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4
paint = new LinearGradientPaint(new Point2D.Double(19.854000091552734, 844.087890625), new Point2D.Double(57.09669876098633, 844.087890625), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 102, 0, 255),new Color(255, 102, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.9452999830245972f, 0.0f, 0.0f, 0.9570000171661377f, -10.770700454711914f, -792.047119140625f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(8.025, 16.248);
((GeneralPath)shape).curveTo(7.819, 23.119, 8.776, 22.030998, 10.504999, 24.206999);
((GeneralPath)shape).curveTo(12.204999, 26.348, 19.619999, 24.998999, 26.276, 18.647);
((GeneralPath)shape).curveTo(33.009, 12.217999, 39.876, 15.856999, 43.204, 12.278);
((GeneralPath)shape).curveTo(40.725, 7.974, 32.987, 2.7600002, 25.863998, 9.986);
((GeneralPath)shape).curveTo(16.734, 1.635, 8.231, 9.3, 8.025, 16.248);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_4);
g.setComposite(AlphaComposite.getInstance(3, 0.392f * origAlpha));
AffineTransform defaultTransform__0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_5
paint = new LinearGradientPaint(new Point2D.Double(-528.2020874023438, 29.173799514770508), new Point2D.Double(-545.55517578125, -6.888599872589111), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 118)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(0.9511f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.934, 10.564);
((GeneralPath)shape).curveTo(33.397, 2.3970003, 43.594, 9.22, 43.53, 16.24);
((GeneralPath)shape).curveTo(43.468, 22.657, 38.26, 26.189999, 26.123, 37.606);
((GeneralPath)shape).curveTo(13.417, 26.986, 8.563, 24.155, 8.629, 16.541);
((GeneralPath)shape).curveTo(8.695, 8.659, 17.75, 2.438, 25.934, 10.564);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_5);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_6
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_6_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_6_0
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(18.304, 24.699);
((GeneralPath)shape).lineTo(18.304, 16.478);
((GeneralPath)shape).lineTo(21.367, 16.478);
((GeneralPath)shape).curveTo(21.907001, 16.478, 22.318, 16.504, 22.602001, 16.556);
((GeneralPath)shape).curveTo(23.001001, 16.623, 23.335001, 16.751999, 23.604002, 16.94);
((GeneralPath)shape).curveTo(23.874002, 17.129, 24.091002, 17.393, 24.255001, 17.733);
((GeneralPath)shape).curveTo(24.420002, 18.073, 24.502, 18.447, 24.502, 18.855);
((GeneralPath)shape).curveTo(24.502, 19.553999, 24.282001, 20.146, 23.843, 20.629);
((GeneralPath)shape).curveTo(23.404001, 21.112999, 22.61, 21.355999, 21.461, 21.355999);
((GeneralPath)shape).lineTo(19.378, 21.355999);
((GeneralPath)shape).lineTo(19.378, 24.697998);
((GeneralPath)shape).lineTo(18.304, 24.697998);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(19.378, 20.387);
((GeneralPath)shape).lineTo(21.477001, 20.387);
((GeneralPath)shape).curveTo(22.171001, 20.387, 22.665, 20.255999, 22.956001, 19.994);
((GeneralPath)shape).curveTo(23.247002, 19.732, 23.394001, 19.364, 23.394001, 18.89);
((GeneralPath)shape).curveTo(23.394001, 18.546, 23.308, 18.251, 23.137001, 18.007);
((GeneralPath)shape).curveTo(22.965, 17.762, 22.739, 17.6, 22.458, 17.522);
((GeneralPath)shape).curveTo(22.277, 17.473, 21.943, 17.449, 21.456, 17.449);
((GeneralPath)shape).lineTo(19.379, 17.449);
((GeneralPath)shape).lineTo(19.379, 20.387);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_6_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_6_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_6_1
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(25.732, 24.699);
((GeneralPath)shape).lineTo(25.732, 18.743);
((GeneralPath)shape).lineTo(26.628, 18.743);
((GeneralPath)shape).lineTo(26.628, 19.646);
((GeneralPath)shape).curveTo(26.857, 19.223999, 27.068, 18.945, 27.263, 18.811);
((GeneralPath)shape).curveTo(27.456001, 18.676, 27.669, 18.609001, 27.902, 18.609001);
((GeneralPath)shape).curveTo(28.239, 18.609001, 28.58, 18.718, 28.927, 18.935001);
((GeneralPath)shape).lineTo(28.583, 19.871002);
((GeneralPath)shape).curveTo(28.34, 19.725002, 28.097, 19.652002, 27.853, 19.652002);
((GeneralPath)shape).curveTo(27.634, 19.652002, 27.439001, 19.719002, 27.265001, 19.851002);
((GeneralPath)shape).curveTo(27.091002, 19.984001, 26.968, 20.168001, 26.894001, 20.403002);
((GeneralPath)shape).curveTo(26.784, 20.762001, 26.728, 21.154001, 26.728, 21.581001);
((GeneralPath)shape).lineTo(26.728, 24.699001);
((GeneralPath)shape).lineTo(25.732, 24.699001);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_6_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_6_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_6_2
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(29.148, 21.721);
((GeneralPath)shape).curveTo(29.148, 20.618, 29.451, 19.802, 30.057001, 19.271);
((GeneralPath)shape).curveTo(30.563002, 18.83, 31.179, 18.609, 31.908, 18.609);
((GeneralPath)shape).curveTo(32.717003, 18.609, 33.377003, 18.877998, 33.89, 19.414);
((GeneralPath)shape).curveTo(34.404, 19.951, 34.66, 20.692, 34.66, 21.638);
((GeneralPath)shape).curveTo(34.66, 22.404001, 34.547, 23.007, 34.32, 23.446001);
((GeneralPath)shape).curveTo(34.092, 23.885, 33.761, 24.227001, 33.328, 24.469002);
((GeneralPath)shape).curveTo(32.894, 24.712002, 32.420998, 24.834002, 31.907999, 24.834002);
((GeneralPath)shape).curveTo(31.084, 24.834002, 30.418, 24.566002, 29.91, 24.031002);
((GeneralPath)shape).curveTo(29.402, 23.497, 29.148, 22.727, 29.148, 21.721);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(30.174, 21.721);
((GeneralPath)shape).curveTo(30.174, 22.484001, 30.338, 23.054, 30.667, 23.434);
((GeneralPath)shape).curveTo(30.996, 23.814, 31.409, 24.003, 31.907999, 24.003);
((GeneralPath)shape).curveTo(32.402, 24.003, 32.814, 23.813, 33.142, 23.431);
((GeneralPath)shape).curveTo(33.469997, 23.049, 33.635, 22.468, 33.635, 21.687);
((GeneralPath)shape).curveTo(33.635, 20.951, 33.469997, 20.393, 33.14, 20.013);
((GeneralPath)shape).curveTo(32.809, 19.634, 32.398, 19.444, 31.907999, 19.444);
((GeneralPath)shape).curveTo(31.408998, 19.444, 30.995998, 19.632, 30.667, 20.01);
((GeneralPath)shape).curveTo(30.338, 20.389, 30.174, 20.958, 30.174, 21.721);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_6_2);
g.setTransform(defaultTransform__0_6);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_7
g.setTransform(defaultTransform__0_7);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_8
paint = new RadialGradientPaint(new Point2D.Double(-2140.16455078125, 905.6063842773438), 16.961f, new Point2D.Double(-2140.16455078125, 905.6063842773438), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 255, 255),new Color(0, 0, 89, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-0.002199999988079071f, -0.5005999803543091f, 0.6144999861717224f, -0.0027000000700354576f, -550.1688232421875f, -1051.78369140625f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(5.614, 20.387);
((GeneralPath)shape).lineTo(16.443, 20.338999);
((GeneralPath)shape).lineTo(16.409, 12.647999);
((GeneralPath)shape).lineTo(21.246, 12.626999);
((GeneralPath)shape).lineTo(10.879, 0.768);
((GeneralPath)shape).lineTo(0.783, 12.682);
((GeneralPath)shape).lineTo(5.583, 12.701);
((GeneralPath)shape).lineTo(5.614, 20.387);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 82, 255);
stroke = new BasicStroke(0.5981f,1,1,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(5.614, 20.387);
((GeneralPath)shape).lineTo(16.443, 20.338999);
((GeneralPath)shape).lineTo(16.409, 12.647999);
((GeneralPath)shape).lineTo(21.246, 12.626999);
((GeneralPath)shape).lineTo(10.879, 0.768);
((GeneralPath)shape).lineTo(0.783, 12.682);
((GeneralPath)shape).lineTo(5.583, 12.701);
((GeneralPath)shape).lineTo(5.614, 20.387);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_8);
g.setComposite(AlphaComposite.getInstance(3, 0.508f * origAlpha));
AffineTransform defaultTransform__0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_9
paint = new RadialGradientPaint(new Point2D.Double(-1192.7548828125, 578.8319702148438), 17.1728f, new Point2D.Double(-1192.7548828125, 578.8319702148438), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-0.0052999998442828655f, -1.2152999639511108f, 0.9383000135421753f, -0.004100000020116568f, -550.5833129882812f, -1439.736328125f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(1.418, 12.408);
((GeneralPath)shape).lineTo(5.882, 12.388);
((GeneralPath)shape).lineTo(5.939, 16.284);
((GeneralPath)shape).curveTo(9.317, 9.737, 15.55, 10.99, 15.894, 6.9610004);
((GeneralPath)shape).lineTo(10.875999, 1.3150005);
((GeneralPath)shape).lineTo(1.418, 12.408);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_9);
g.setComposite(AlphaComposite.getInstance(3, 0.4813f * origAlpha));
AffineTransform defaultTransform__0_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_10
paint = new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.5981f,0,0,10.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(6.28, 19.79);
((GeneralPath)shape).lineTo(15.858, 19.748001);
((GeneralPath)shape).lineTo(15.823999, 12.033001);
((GeneralPath)shape).lineTo(19.925999, 12.015001);
((GeneralPath)shape).lineTo(10.875, 1.7);
((GeneralPath)shape).lineTo(2.062, 12.119);
((GeneralPath)shape).lineTo(6.17, 12.088);
((GeneralPath)shape).lineTo(6.28, 19.79);
((GeneralPath)shape).lineTo(6.28, 19.79);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_10);
g.setTransform(defaultTransform__0);
g.setTransform(defaultTransform_);

	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 1;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 1;
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
	public apromore_export() {
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

