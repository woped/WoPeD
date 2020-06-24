package org.woped.gui.images.svg;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class woped_community implements org.pushingpixels.flamingo.api.common.icon.ResizableIcon {
	
	private float origAlpha = 1.0f;
	
	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * 
	 * @param g
	 *            Graphics context.
	 */
	public void paint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        origAlpha = 1.0f;
        Composite origComposite = g.getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = 
			(AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }
        
		// _0
		AffineTransform trans_0 = g.getTransform();
		paintRootGraphicsNode_0(g);
		g.setTransform(trans_0);
		
	}
	
	private void paintShapeNode_0_0_0_0(Graphics2D g) {
		GeneralPath shape0 = new GeneralPath();
		shape0.moveTo(26.104, 4.742);
		shape0.lineTo(165.052, 4.742);
		shape0.curveTo(175.968, 4.742, 184.81801, 13.592001, 184.81801, 24.508001);
		shape0.lineTo(184.81801, 157.78499);
		shape0.curveTo(184.81801, 168.702, 175.968, 177.551, 165.052, 177.551);
		shape0.lineTo(26.104, 177.551);
		shape0.curveTo(15.187, 177.551, 6.337, 168.702, 6.337, 157.78499);
		shape0.lineTo(6.337, 24.509);
		shape0.curveTo(6.337, 13.592, 15.187, 4.742, 26.104, 4.742);
		shape0.closePath();
		g.setPaint(new Color(255, 127, 42, 255));
		g.fill(shape0);
	}
	
	private void paintTextNode_0_0_0_1(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo(24.24014, 0.0);
		shape1.lineTo(0.12658037, -93.66948);
		shape1.lineTo(12.911198, -93.66948);
		shape1.lineTo(32.088123, -19.683249);
		shape1.lineTo(49.746086, -93.66948);
		shape1.lineTo(62.530704, -93.66948);
		shape1.lineTo(79.23931, -20.69589);
		shape1.lineTo(99.87191, -93.66948);
		shape1.lineTo(110.694534, -93.66948);
		shape1.lineTo(84.42911, 0.0);
		shape1.lineTo(71.20146, 0.0);
		shape1.lineTo(54.68272, -72.2141);
		shape1.lineTo(37.4045, 0.0);
		shape1.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape1);
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
	}
	
	private void paintShapeNode_0_0_0_2(Graphics2D g) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo(22.774, 131.943);
		shape2.lineTo(167.993, 131.943);
		shape2.curveTo(177.246, 131.943, 184.746, 139.444, 184.746, 148.696);
		shape2.lineTo(184.746, 164.95999);
		shape2.curveTo(184.746, 174.211, 177.246, 181.713, 167.993, 181.713);
		shape2.lineTo(22.774, 181.713);
		shape2.curveTo(13.523, 181.713, 6.0219994, 174.211, 6.0219994, 164.95999);
		shape2.lineTo(6.0219994, 148.69598);
		shape2.curveTo(6.022, 139.444, 13.523, 131.943, 22.774, 131.943);
		shape2.closePath();
		g.setPaint(new Color(255, 173, 0, 241));
		g.fill(shape2);
	}
	
	private void paintShapeNode_0_0_0_3(Graphics2D g) {
		Rectangle2D.Double shape3 = new Rectangle2D.Double(5.561999797821045, 130.03199768066406, 180.25399780273438, 1.7289999723434448);
		g.setPaint(new Color(255, 255, 255, 241));
		g.fill(shape3);
	}
	
	private void paintShapeNode_0_0_0_4(Graphics2D g) {
		Rectangle2D.Double shape4 = new Rectangle2D.Double(5.9070000648498535, 131.75999450683594, 179.03199768066406, 30.066999435424805);
		g.setPaint(new Color(255, 173, 0, 241));
		g.fill(shape4);
	}
	
	private void paintTextNode_0_0_0_5(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo(6.733376, 0.0);
		shape5.lineTo(0.03516123, -26.019312);
		shape5.lineTo(3.5864456, -26.019312);
		shape5.lineTo(8.913372, -5.4675717);
		shape5.lineTo(13.818364, -26.019312);
		shape5.lineTo(17.369648, -26.019312);
		shape5.lineTo(22.010931, -5.7488613);
		shape5.lineTo(27.742212, -26.019312);
		shape5.lineTo(30.748497, -26.019312);
		shape5.lineTo(23.452541, 0.0);
		shape5.lineTo(19.778193, 0.0);
		shape5.lineTo(15.189652, -20.059483);
		shape5.lineTo(10.390144, 0.0);
		shape5.closePath();
		shape5.moveTo(41.73638, 0.43951538);
		shape5.quadTo(37.6401, 0.43951538, 35.196392, -2.2766898);
		shape5.quadTo(32.752686, -4.992895, 32.752686, -9.546274);
		shape5.quadTo(32.752686, -14.152396, 35.205185, -16.833439);
		shape5.quadTo(37.65768, -19.514484, 41.859447, -19.514484);
		shape5.quadTo(46.061214, -19.514484, 48.51371, -16.833439);
		shape5.quadTo(50.966206, -14.152396, 50.966206, -9.581435);
		shape5.quadTo(50.966206, -4.9049916, 48.50492, -2.2327383);
		shape5.quadTo(46.043633, 0.43951538, 41.73638, 0.43951538);
		shape5.closePath();
		shape5.moveTo(41.789124, -2.1624157);
		shape5.quadTo(47.291855, -2.1624157, 47.291855, -9.581435);
		shape5.quadTo(47.291855, -16.912552, 41.859447, -16.912552);
		shape5.quadTo(36.44462, -16.912552, 36.44462, -9.546274);
		shape5.quadTo(36.44462, -2.1624157, 41.789124, -2.1624157);
		shape5.closePath();
		shape5.moveTo(56.275555, 0.0);
		shape5.lineTo(56.275555, -26.019312);
		shape5.lineTo(63.360542, -26.019312);
		shape5.quadTo(68.07214, -26.019312, 70.12908, -24.428267);
		shape5.quadTo(72.18601, -22.83722, 72.18601, -19.198032);
		shape5.quadTo(72.18601, -15.049007, 69.373116, -12.693205);
		shape5.quadTo(66.56021, -10.337402, 61.567318, -10.337402);
		shape5.lineTo(59.932323, -10.337402);
		shape5.lineTo(59.932323, 0.0);
		shape5.closePath();
		shape5.moveTo(59.932323, -13.13272);
		shape5.lineTo(61.426674, -13.13272);
		shape5.quadTo(64.71425, -13.13272, 66.50747, -14.644653);
		shape5.quadTo(68.3007, -16.156586, 68.3007, -18.916742);
		shape5.quadTo(68.3007, -21.254965, 66.89425, -22.25706);
		shape5.quadTo(65.48779, -23.259155, 62.200222, -23.259155);
		shape5.lineTo(59.932323, -23.259155);
		shape5.closePath();
		shape5.moveTo(90.27646, -0.6153216);
		shape5.quadTo(86.7955, 0.43951538, 84.316635, 0.43951538);
		shape5.quadTo(80.09728, 0.43951538, 77.43382, -2.3645928);
		shape5.quadTo(74.770355, -5.168701, 74.770355, -9.634177);
		shape5.quadTo(74.770355, -13.976589, 77.11737, -16.754328);
		shape5.quadTo(79.46438, -19.532064, 83.12115, -19.532064);
		shape5.quadTo(86.58453, -19.532064, 88.47445, -17.070778);
		shape5.quadTo(90.364365, -14.609491, 90.364365, -10.073693);
		shape5.lineTo(90.34678, -9.001275);
		shape5.lineTo(78.30406, -9.001275);
		shape5.quadTo(79.06003, -2.197577, 84.96712, -2.197577);
		shape5.quadTo(87.12953, -2.197577, 90.27646, -3.3578975);
		shape5.closePath();
		shape5.moveTo(78.46229, -11.603207);
		shape5.lineTo(86.8834, -11.603207);
		shape5.quadTo(86.8834, -16.930134, 82.91018, -16.930134);
		shape5.quadTo(78.91938, -16.930134, 78.46229, -11.603207);
		shape5.closePath();
		shape5.moveTo(96.23628, 0.0);
		shape5.lineTo(96.23628, -26.019312);
		shape5.lineTo(104.88595, -26.019312);
		shape5.quadTo(108.753685, -26.019312, 111.13586, -25.157862);
		shape5.quadTo(113.51803, -24.296412, 115.258514, -22.23948);
		shape5.quadTo(118.01867, -18.969484, 118.01867, -13.624977);
		shape5.quadTo(118.01867, -7.13773, 114.59045, -3.568865);
		shape5.quadTo(111.16223, 0.0, 104.93869, 0.0);
		shape5.closePath();
		shape5.moveTo(99.928215, -2.7601566);
		shape5.lineTo(104.6574, -2.7601566);
		shape5.quadTo(109.72062, -2.7601566, 111.83029, -5.4851522);
		shape5.quadTo(114.09819, -8.385954, 114.09819, -13.273365);
		shape5.quadTo(114.09819, -17.861906, 111.865456, -20.481417);
		shape5.quadTo(110.51174, -22.081253, 108.63062, -22.670204);
		shape5.quadTo(106.7495, -23.259155, 102.96966, -23.259155);
		shape5.lineTo(99.928215, -23.259155);
		shape5.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape5);
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
	}
	
	private void paintShapeNode_0_0_0_6(Graphics2D g) {
		GeneralPath shape6 = new GeneralPath();
		shape6.moveTo(199.0, 181.574);
		shape6.lineTo(57.811996, 200.414);
		shape6.curveTo(57.561996, 200.447, 57.331997, 200.271, 57.297997, 200.019);
		shape6.lineTo(53.253, 169.707);
		shape6.curveTo(53.219997, 169.457, 53.396, 169.228, 53.647, 169.193);
		shape6.lineTo(194.836, 150.35399);
		shape6.curveTo(195.086, 150.32098, 195.315, 150.49799, 195.35, 150.74799);
		shape6.lineTo(199.393, 181.05998);
		shape6.curveTo(199.427, 181.31, 199.252, 181.541, 199.0, 181.574);
		shape6.closePath();
		g.setPaint(new Color(0, 0, 0, 241));
		g.fill(shape6);
	}
	
	private void paintTextNode_0_0_0_7(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		GeneralPath shape7 = new GeneralPath();
		shape7.moveTo(8.056641, 0.36132812);
		shape7.quadTo(4.6875, 0.36132812, 2.8515625, -1.6162109);
		shape7.quadTo(1.015625, -3.59375, 1.015625, -7.216797);
		shape7.quadTo(1.015625, -10.830078, 2.8857422, -12.822266);
		shape7.quadTo(4.7558594, -14.814453, 8.154297, -14.814453);
		shape7.quadTo(10.097656, -14.814453, 12.705078, -14.1796875);
		shape7.lineTo(12.705078, -12.255859);
		shape7.quadTo(9.736328, -13.28125, 8.125, -13.28125);
		shape7.quadTo(5.7714844, -13.28125, 4.482422, -11.689453);
		shape7.quadTo(3.1933594, -10.097656, 3.1933594, -7.1972656);
		shape7.quadTo(3.1933594, -4.4335938, 4.5703125, -2.836914);
		shape7.quadTo(5.9472656, -1.2402344, 8.330078, -1.2402344);
		shape7.quadTo(10.380859, -1.2402344, 12.724609, -2.5);
		shape7.lineTo(12.724609, -0.7421875);
		shape7.quadTo(10.5859375, 0.36132812, 8.056641, 0.36132812);
		shape7.closePath();
		shape7.moveTo(21.523438, 0.36132812);
		shape7.quadTo(18.515625, 0.36132812, 16.68457, -1.7236328);
		shape7.quadTo(14.853516, -3.8085938, 14.853516, -7.236328);
		shape7.quadTo(14.853516, -10.683594, 16.694336, -12.749023);
		shape7.quadTo(18.535156, -14.814453, 21.611328, -14.814453);
		shape7.quadTo(24.677734, -14.814453, 26.523438, -12.753906);
		shape7.quadTo(28.36914, -10.693359, 28.36914, -7.2558594);
		shape7.quadTo(28.36914, -3.75, 26.523438, -1.6943359);
		shape7.quadTo(24.677734, 0.36132812, 21.523438, 0.36132812);
		shape7.closePath();
		shape7.moveTo(21.552734, -1.171875);
		shape7.quadTo(23.769531, -1.171875, 24.980469, -2.7685547);
		shape7.quadTo(26.191406, -4.3652344, 26.191406, -7.2753906);
		shape7.quadTo(26.191406, -10.097656, 24.975586, -11.689453);
		shape7.quadTo(23.759766, -13.28125, 21.611328, -13.28125);
		shape7.quadTo(19.453125, -13.28125, 18.242188, -11.68457);
		shape7.quadTo(17.03125, -10.087891, 17.03125, -7.2460938);
		shape7.quadTo(17.03125, -4.4140625, 18.232422, -2.7929688);
		shape7.quadTo(19.433594, -1.171875, 21.552734, -1.171875);
		shape7.closePath();
		shape7.moveTo(31.240234, 0.0);
		shape7.lineTo(31.240234, -14.453125);
		shape7.lineTo(34.08203, -14.453125);
		shape7.lineTo(38.085938, -3.2714844);
		shape7.lineTo(42.20703, -14.453125);
		shape7.lineTo(44.746094, -14.453125);
		shape7.lineTo(44.746094, 0.0);
		shape7.lineTo(42.83203, 0.0);
		shape7.lineTo(42.83203, -11.7578125);
		shape7.lineTo(38.847656, -0.9667969);
		shape7.lineTo(36.865234, -0.9667969);
		shape7.lineTo(32.998047, -11.787109);
		shape7.lineTo(32.998047, 0.0);
		shape7.closePath();
		shape7.moveTo(48.466797, 0.0);
		shape7.lineTo(48.466797, -14.453125);
		shape7.lineTo(51.308594, -14.453125);
		shape7.lineTo(55.3125, -3.2714844);
		shape7.lineTo(59.433594, -14.453125);
		shape7.lineTo(61.972656, -14.453125);
		shape7.lineTo(61.972656, 0.0);
		shape7.lineTo(60.058594, 0.0);
		shape7.lineTo(60.058594, -11.7578125);
		shape7.lineTo(56.07422, -0.9667969);
		shape7.lineTo(54.091797, -0.9667969);
		shape7.lineTo(50.22461, -11.787109);
		shape7.lineTo(50.22461, 0.0);
		shape7.closePath();
		shape7.moveTo(65.57617, -14.453125);
		shape7.lineTo(67.62695, -14.453125);
		shape7.lineTo(67.62695, -5.3320312);
		shape7.quadTo(67.62695, -3.1542969, 68.43262, -2.163086);
		shape7.quadTo(69.23828, -1.171875, 70.99609, -1.171875);
		shape7.quadTo(72.71484, -1.171875, 73.43262, -2.1044922);
		shape7.quadTo(74.15039, -3.0371094, 74.15039, -5.263672);
		shape7.lineTo(74.15039, -14.453125);
		shape7.lineTo(75.947266, -14.453125);
		shape7.lineTo(75.947266, -5.2929688);
		shape7.quadTo(75.947266, -2.3339844, 74.731445, -0.9863281);
		shape7.quadTo(73.515625, 0.36132812, 70.859375, 0.36132812);
		shape7.quadTo(68.1543, 0.36132812, 66.865234, -1.0351562);
		shape7.quadTo(65.57617, -2.4316406, 65.57617, -5.3515625);
		shape7.closePath();
		shape7.moveTo(79.55078, 0.0);
		shape7.lineTo(79.55078, -14.453125);
		shape7.lineTo(81.5625, -14.453125);
		shape7.lineTo(88.83789, -3.2910156);
		shape7.lineTo(88.83789, -14.453125);
		shape7.lineTo(90.5957, -14.453125);
		shape7.lineTo(90.5957, 0.0);
		shape7.lineTo(88.59375, 0.0);
		shape7.lineTo(81.30859, -11.162109);
		shape7.lineTo(81.30859, 0.0);
		shape7.closePath();
		shape7.moveTo(94.31641, 0.0);
		shape7.lineTo(94.31641, -14.453125);
		shape7.lineTo(96.36719, -14.453125);
		shape7.lineTo(96.36719, 0.0);
		shape7.closePath();
		shape7.moveTo(103.52539, 0.0);
		shape7.lineTo(103.52539, -12.919922);
		shape7.lineTo(98.39844, -12.919922);
		shape7.lineTo(98.39844, -14.453125);
		shape7.lineTo(110.703125, -14.453125);
		shape7.lineTo(110.703125, -12.919922);
		shape7.lineTo(105.57617, -12.919922);
		shape7.lineTo(105.57617, 0.0);
		shape7.closePath();
		shape7.moveTo(115.859375, 0.0);
		shape7.lineTo(115.859375, -6.0351562);
		shape7.lineTo(111.03516, -14.453125);
		shape7.lineTo(113.37891, -14.453125);
		shape7.lineTo(117.12891, -7.9296875);
		shape7.lineTo(121.171875, -14.453125);
		shape7.lineTo(123.07617, -14.453125);
		shape7.lineTo(117.91016, -6.0742188);
		shape7.lineTo(117.91016, 0.0);
		shape7.closePath();
		g.setPaint(new Color(249, 249, 249, 255));
		g.fill(shape7);
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
	}
	
	private void paintCompositeGraphicsNode_0_0_0(Graphics2D g) {
		// _0_0_0_0
		g.setComposite(AlphaComposite.getInstance(3, 0.89f * origAlpha));
		AffineTransform trans_0_0_0_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_0(g);
		g.setTransform(trans_0_0_0_0);
		// _0_0_0_1
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_0_1 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 34.29710006713867f, 118.3718032836914f));
		paintTextNode_0_0_0_1(g);
		g.setTransform(trans_0_0_0_1);
		// _0_0_0_2
		AffineTransform trans_0_0_0_2 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_2(g);
		g.setTransform(trans_0_0_0_2);
		// _0_0_0_3
		AffineTransform trans_0_0_0_3 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_3(g);
		g.setTransform(trans_0_0_0_3);
		// _0_0_0_4
		AffineTransform trans_0_0_0_4 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_4(g);
		g.setTransform(trans_0_0_0_4);
		// _0_0_0_5
		AffineTransform trans_0_0_0_5 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 34.15299987792969f, 168.81809997558594f));
		paintTextNode_0_0_0_5(g);
		g.setTransform(trans_0_0_0_5);
		// _0_0_0_6
		AffineTransform trans_0_0_0_6 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_6(g);
		g.setTransform(trans_0_0_0_6);
		// _0_0_0_7
		AffineTransform trans_0_0_0_7 = g.getTransform();
		g.transform(new AffineTransform(0.9911999702453613f, -0.13230000436306f, 0.13230000436306f, 0.9911999702453613f, 67.03880310058594f, 191.497802734375f));
		paintTextNode_0_0_0_7(g);
		g.setTransform(trans_0_0_0_7);
	}
	
	private void paintCanvasGraphicsNode_0_0(Graphics2D g) {
		// _0_0_0
		AffineTransform trans_0_0_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -5.56222677230835f, -4.742427349090576f));
		paintCompositeGraphicsNode_0_0_0(g);
		g.setTransform(trans_0_0_0);
	}
	
	private void paintRootGraphicsNode_0(Graphics2D g) {
		// _0_0
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0 = g.getTransform();
		g.transform(new AffineTransform(0.9987316131591797f, 0.0f, 0.0f, 0.9987316131591797f, -0.0f, 0.2922999833099311f));
		paintCanvasGraphicsNode_0_0(g);
		g.setTransform(trans_0_0);
	}
	
	
	
    /**
     * Returns the X of the bounding box of the original SVG image.
     * @return The X of the bounding box of the original SVG image.
     */
    public int getOrigX() {
        return 0;
    }
	
    /**
     * Returns the Y of the bounding box of the original SVG image.
     * @return The Y of the bounding box of the original SVG image.
     */
    public int getOrigY() {
        return 1;
    }
	
    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 194;
    }
	
    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 196;
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
	public woped_community() {
        this.width = getOrigWidth();
        this.height = getOrigHeight();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
    @Override
	public int getIconHeight() {
		return height;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
    @Override
	public int getIconWidth() {
		return width;
	}
	
	/*
	 * Set the dimension of the icon.
	 */
	
	public void setDimension(Dimension newDimension) {
		this.width = newDimension.width;
		this.height = newDimension.height;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
    @Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(x, y);
		
		double coef1 = (double) this.width / (double) getOrigWidth();
		double coef2 = (double) this.height / (double) getOrigHeight();
		double coef = Math.min(coef1, coef2);
		g2d.scale(coef, coef);
		paint(g2d);
		g2d.dispose();
	}
}

