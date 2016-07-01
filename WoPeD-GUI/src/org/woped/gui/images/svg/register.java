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
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D; 

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class register implements org.pushingpixels.flamingo.api.common.icon.ResizableIcon {
	
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
		shape0.moveTo(49.0, 7.5);
		shape0.lineTo(145.5, 7.5);
		shape0.curveTo(149.0, 7.5, 152.5, 7.5, 157.0, 12.0);
		shape0.lineTo(208.25, 63.25);
		shape0.curveTo(211.375, 66.375, 216.0, 73.0, 216.0, 80.5);
		shape0.lineTo(216.0, 231.5);
		shape0.lineTo(48.0, 231.5);
		shape0.lineTo(49.0, 7.5);
		shape0.closePath();
		g.setPaint(new Color(0, 0, 0, 255));
		g.fill(shape0);
	}

	private void paintShapeNode_0_0_0_1(Graphics2D g) {
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo(14.038953, 238.05026);
		shape1.lineTo(111.39555, 238.05026);
		shape1.curveTo(114.92661, 238.05026, 118.45768, 238.05026, 122.99763, 233.51031);
		shape1.lineTo(174.70255, 181.80539);
		shape1.curveTo(177.85529, 178.65265, 182.52135, 171.96884, 182.52135, 164.40227);
		shape1.lineTo(182.52135, 12.0619);
		shape1.lineTo(13.030077, 12.0619);
		shape1.lineTo(14.038953, 238.05026);
		shape1.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(97.5, 330.32525634765625), new Point2D.Double(157.78732299804688, 34.61892318725586), new float[] {0.0f,1.0f}, new Color[] {new Color(212, 212, 212, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0088765621185303f, 0.0f, 0.0f, -1.0088765621185303f, -35.395999908447266f, 250.66122436523438f)));
		g.fill(shape1);
	}

	private void paintShapeNode_0_0_0_2(Graphics2D g) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo(14.038953, 238.05026);
		shape2.lineTo(111.39555, 238.05026);
		shape2.curveTo(114.92661, 238.05026, 118.45768, 238.05026, 122.99763, 233.51031);
		shape2.lineTo(174.70255, 181.80539);
		shape2.curveTo(177.85529, 178.65265, 182.52135, 171.96884, 182.52135, 164.40227);
		shape2.lineTo(182.52135, 12.0619);
		shape2.lineTo(13.030077, 12.0619);
		shape2.lineTo(14.038953, 238.05026);
		shape2.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(172.4296112060547, 38.00879669189453), new Point2D.Double(137.5, 78.0), new float[] {0.0f,1.0f}, new Color[] {new Color(183, 183, 183, 255),new Color(211, 211, 211, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0088765621185303f, 0.0f, 0.0f, -1.0088765621185303f, -35.395999908447266f, 250.66122436523438f)));
		g.fill(shape2);
	}

	private void paintShapeNode_0_0_0_3(Graphics2D g) {
		RoundRectangle2D.Double shape3 = new RoundRectangle2D.Double(32.82521438598633, 199.82521057128906, 106.34957122802734, 16.349571228027344, 2.1676383018493652, 1.781571865081787);
		g.setPaint(new Color(218, 218, 218, 255));
		g.fill(shape3);
		g.setPaint(new Color(148, 148, 148, 255));
		g.setStroke(new BasicStroke(0.6504291f,0,0,4.0f,null,0.0f));
		g.draw(shape3);
	}

	private void paintShapeNode_0_0_0_4(Graphics2D g) {
		GeneralPath shape4 = new GeneralPath();
		shape4.moveTo(152.0, 10.75);
		shape4.curveTo(155.58017, 13.082371, 159.03844, 23.454962, 160.7698, 29.10975);
		shape4.curveTo(160.7698, 29.10975, 164.75, 67.25, 164.75, 67.25);
		shape4.curveTo(195.23135, 65.36129, 203.49007, 68.6258, 215.5, 83.0);
		shape4.curveTo(210.0, 64.5, 152.0, 10.75, 152.0, 10.75);
		shape4.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(154.0, 48.375), new Point2D.Double(234.5, 52.875), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)));
		g.fill(shape4);
	}

	private void paintShapeNode_0_0_0_5(Graphics2D g) {
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo(114.92661, 237.94579);
		shape5.curveTo(124.07301, 234.33821, 134.51895, 228.21628, 134.85193, 190.88528);
		shape5.curveTo(167.62161, 193.2952, 177.09863, 182.05762, 182.52135, 165.91557);
		shape5.curveTo(182.30968, 175.12128, 177.6466, 179.70386, 172.6848, 186.09311);
		shape5.lineTo(130.31198, 230.48367);
		shape5.curveTo(123.72054, 236.50359, 119.37156, 237.9853, 114.92661, 237.94579);
		shape5.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(187.5, 41.124691009521484), new Point2D.Double(175.0, 54.124691009521484), new float[] {0.0f,1.0f}, new Color[] {new Color(169, 169, 169, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0088765621185303f, 0.0f, 0.0f, -1.0088765621185303f, -30.856054306030273f, 252.67897033691406f)));
		g.fill(shape5);
	}

	private void paintShapeNode_0_0_0_6(Graphics2D g) {
		GeneralPath shape6 = new GeneralPath();
		shape6.moveTo(36.13854, 84.15929);
		shape6.lineTo(160.86147, 84.15929);
		shape6.curveTo(162.51196, 84.15929, 163.84071, 85.48803, 163.84071, 87.138535);
		shape6.lineTo(163.84071, 112.86146);
		shape6.curveTo(163.84071, 114.51197, 162.51196, 115.84071, 160.86147, 115.84071);
		shape6.lineTo(116.0, 115.84071);
		shape6.lineTo(116.0, 120.59071);
		shape6.curveTo(115.95362, 121.42389, 115.64311, 121.84071, 114.875, 121.84071);
		shape6.lineTo(36.13854, 121.84071);
		shape6.curveTo(34.488033, 121.84071, 33.15929, 120.51197, 33.15929, 118.86146);
		shape6.lineTo(33.15929, 87.138535);
		shape6.curveTo(33.15929, 85.48803, 34.488033, 84.15929, 36.13854, 84.15929);
		shape6.closePath();
		g.setPaint(new Color(143, 143, 143, 255));
		g.fill(shape6);
	}

	private void paintShapeNode_0_0_0_7(Graphics2D g) {
		RoundRectangle2D.Double shape7 = new RoundRectangle2D.Double(33.1592903137207, 138.15928649902344, 130.68142700195312, 13.68142318725586, 6.6894731521606445, 6.6894731521606445);
		g.fill(shape7);
	}

	private void paintShapeNode_0_0_0_8_0_0(Graphics2D g) {
		GeneralPath shape8 = new GeneralPath();
		shape8.moveTo(32.15625, 9.15625);
		shape8.curveTo(19.647873, 9.15625, 9.5, 19.302969, 9.5, 31.8125);
		shape8.curveTo(9.5, 44.32203, 19.647873, 54.46875, 32.15625, 54.46875);
		shape8.curveTo(44.664627, 54.46875, 54.8125, 44.32203, 54.8125, 31.8125);
		shape8.curveTo(54.8125, 19.302969, 44.664627, 9.15625, 32.15625, 9.15625);
		shape8.closePath();
		shape8.moveTo(32.235886, 12.301845);
		shape8.lineTo(32.275703, 12.301845);
		shape8.curveTo(43.035103, 12.323153, 51.786358, 21.049267, 51.786358, 31.8125);
		shape8.curveTo(51.786358, 42.59005, 43.01228, 51.32315, 32.235886, 51.323154);
		shape8.curveTo(26.416424, 51.323154, 21.197615, 48.793377, 17.622803, 44.75324);
		shape8.curveTo(19.092678, 43.612442, 21.562235, 43.89037, 24.590895, 44.23561);
		shape8.curveTo(26.042295, 44.400726, 27.701097, 44.59568, 29.40883, 44.633785);
		shape8.curveTo(34.179867, 44.6615, 39.30065, 43.788425, 42.469025, 42.404);
		shape8.curveTo(44.517384, 41.505676, 45.830074, 40.8803, 46.64988, 40.13439);
		shape8.curveTo(46.94547, 39.891914, 47.088955, 39.519424, 47.247143, 39.099133);
		shape8.lineTo(47.366596, 38.820408);
		shape8.curveTo(47.499382, 38.48094, 47.719147, 37.74551, 47.804592, 37.347157);
		shape8.curveTo(47.842693, 37.172802, 47.848503, 36.96633, 47.724957, 36.869343);
		shape8.lineTo(47.28696, 36.94898);
		shape8.curveTo(45.89676, 37.78264, 42.417976, 39.39283, 39.164158, 39.457493);
		shape8.curveTo(35.132103, 39.541782, 26.991299, 35.39571, 26.143784, 34.958096);
		shape8.lineTo(26.064148, 34.838642);
		shape8.curveTo(25.860928, 34.353687, 24.649294, 31.464226, 24.391806, 30.856876);
		shape8.curveTo(30.241287, 34.708805, 35.088963, 36.8565, 38.8058, 37.187885);
		shape8.curveTo(42.93831, 37.555065, 46.147205, 35.285152, 47.525867, 34.321014);
		shape8.curveTo(47.794903, 34.137424, 47.999027, 33.993828, 48.083317, 33.962654);
		shape8.lineTo(48.202766, 33.8432);
		shape8.curveTo(47.974148, 32.390644, 45.81387, 25.337984, 44.181183, 23.64988);
		shape8.curveTo(43.728558, 23.186863, 43.35919, 22.72561, 42.628296, 22.29608);
		shape8.curveTo(36.718773, 18.851744, 22.699562, 16.777624, 22.042564, 16.681787);
		shape8.lineTo(21.923111, 16.721605);
		shape8.lineTo(21.883293, 16.841059);
		shape8.curveTo(21.883293, 16.841059, 21.809431, 19.742989, 21.803658, 20.06629);
		shape8.curveTo(21.322166, 19.906948, 19.840858, 19.418507, 17.702438, 18.831942);
		shape8.curveTo(21.267847, 14.840296, 26.465986, 12.313447, 32.235886, 12.301845);
		shape8.closePath();
		shape8.moveTo(37.690907, 22.654438);
		shape8.curveTo(41.0013, 22.773367, 43.61744, 25.557907, 43.504284, 28.865993);
		shape8.curveTo(43.445396, 30.466345, 42.76155, 31.914728, 41.593037, 33.00703);
		shape8.curveTo(40.421062, 34.105106, 38.902313, 34.691513, 37.29273, 34.639553);
		shape8.curveTo(33.988106, 34.516006, 31.411785, 31.731466, 31.519167, 28.428);
		shape8.curveTo(31.575745, 26.825336, 32.21516, 25.335983, 33.3906, 24.247145);
		shape8.curveTo(34.561417, 23.149067, 36.08709, 22.601324, 37.690907, 22.654438);
		shape8.closePath();
		shape8.moveTo(37.65109, 24.406416);
		shape8.curveTo(36.533382, 24.364847, 35.47879, 24.758081, 34.664764, 25.521309);
		shape8.curveTo(33.853043, 26.28338, 33.386574, 27.310291, 33.35078, 28.428);
		shape8.curveTo(33.27457, 30.737309, 35.065365, 32.682682, 37.372364, 32.768124);
		shape8.curveTo(38.493534, 32.802765, 39.579857, 32.41299, 40.398506, 31.653229);
		shape8.curveTo(41.214848, 30.887693, 41.67785, 29.859629, 41.71249, 28.74654);
		shape8.curveTo(41.789852, 26.433765, 39.959244, 24.487242, 37.65109, 24.406416);
		shape8.closePath();
		shape8.moveTo(38.327988, 26.51675);
		shape8.curveTo(39.35332, 26.51675, 40.199417, 27.065233, 40.199417, 27.751099);
		shape8.curveTo(40.199417, 28.4335, 39.35332, 28.985447, 38.327988, 28.985447);
		shape8.curveTo(37.29919, 28.985447, 36.496376, 28.432344, 36.496376, 27.751099);
		shape8.curveTo(36.496376, 27.065233, 37.29919, 26.51675, 38.327988, 26.51675);
		shape8.closePath();
		g.setPaint(new Color(46, 52, 54, 134));
		g.fill(shape8);
	}

	private void paintCompositeGraphicsNode_0_0_0_8_0(Graphics2D g) {
		// _0_0_0_8_0_0
		AffineTransform trans_0_0_0_8_0_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_0_0(g);
		g.setTransform(trans_0_0_0_8_0_0);
	}

	private void paintShapeNode_0_0_0_8_1_0(Graphics2D g) {
		GeneralPath shape9 = new GeneralPath();
		shape9.moveTo(16.21524, 8.981412);
		shape9.lineTo(17.232908, 10.442215);
		shape9.curveTo(17.660637, 10.144073, 18.096521, 9.856807, 18.540562, 9.582227);
		shape9.lineTo(17.613514, 8.064333);
		shape9.curveTo(17.13685, 8.355225, 16.671967, 8.662428, 16.216146, 8.982319);
		shape9.lineTo(16.21524, 8.981412);
		shape9.closePath();
		g.fill(shape9);
	}

	private void paintShapeNode_0_0_0_8_1_1(Graphics2D g) {
		GeneralPath shape10 = new GeneralPath();
		shape10.moveTo(37.84001, 6.363386);
		shape10.curveTo(38.31033, 6.4703183, 38.77612, 6.5881248, 39.235565, 6.716806);
		shape10.lineTo(39.720387, 5.0004535);
		shape10.curveTo(39.22922, 4.8618045, 38.731716, 4.736748, 38.22877, 4.6243787);
		shape10.lineTo(37.84001, 6.3624797);
		shape10.lineTo(37.84001, 6.363386);
		shape10.closePath();
		g.fill(shape10);
	}

	private void paintShapeNode_0_0_0_8_1_2(Graphics2D g) {
		GeneralPath shape11 = new GeneralPath();
		shape11.moveTo(9.580007, 18.770239);
		shape11.lineTo(8.039458, 17.878532);
		shape11.curveTo(7.759441, 18.360634, 7.493923, 18.849985, 7.2438107, 19.349304);
		shape11.lineTo(8.836919, 20.148577);
		shape11.curveTo(9.071626, 19.680975, 9.319926, 19.222435, 9.580007, 18.770239);
		shape11.closePath();
		g.fill(shape11);
	}

	private void paintShapeNode_0_0_0_8_1_3(Graphics2D g) {
		GeneralPath shape12 = new GeneralPath();
		shape12.moveTo(24.515171, 5.02764);
		shape12.lineTo(25.00271, 6.7394614);
		shape12.curveTo(25.501123, 6.598093, 26.008596, 6.473037, 26.519697, 6.357949);
		shape12.lineTo(26.135466, 4.619848);
		shape12.curveTo(25.590837, 4.740373, 25.048925, 4.873585, 24.515171, 5.02764);
		shape12.closePath();
		g.fill(shape12);
	}

	private void paintShapeNode_0_0_0_8_1_4(Graphics2D g) {
		GeneralPath shape13 = new GeneralPath();
		shape13.moveTo(12.667447, 11.928391);
		shape13.lineTo(13.914385, 13.201609);
		shape13.curveTo(14.285023, 12.835502, 14.669254, 12.48027, 15.059829, 12.139537);
		shape13.lineTo(13.894449, 10.793822);
		shape13.curveTo(13.473064, 11.159023, 13.063459, 11.538723, 12.667447, 11.928391);
		shape13.lineTo(12.667447, 11.928391);
		shape13.closePath();
		g.fill(shape13);
	}

	private void paintShapeNode_0_0_0_8_1_5(Graphics2D g) {
		GeneralPath shape14 = new GeneralPath();
		shape14.moveTo(33.556377, 5.788852);
		shape14.curveTo(34.079258, 5.815132, 34.601234, 5.855005, 35.11777, 5.915721);
		shape14.lineTo(35.32348, 4.1422777);
		shape14.curveTo(34.76707, 4.0806556, 34.21338, 4.030814, 33.655155, 4.0036283);
		shape14.lineTo(33.556377, 5.788852);
		shape14.lineTo(33.556377, 5.788852);
		shape14.closePath();
		g.fill(shape14);
	}

	private void paintShapeNode_0_0_0_8_1_6(Graphics2D g) {
		GeneralPath shape15 = new GeneralPath();
		shape15.moveTo(20.20073, 6.6588087);
		shape15.lineTo(20.963755, 8.266417);
		shape15.curveTo(21.433168, 8.044396, 21.909832, 7.8332505, 22.394651, 7.6375103);
		shape15.lineTo(21.730404, 5.9854984);
		shape15.curveTo(21.210241, 6.1957383, 20.700954, 6.420477, 20.20073, 6.6588087);
		shape15.closePath();
		g.fill(shape15);
	}

	private void paintShapeNode_0_0_0_8_1_7(Graphics2D g) {
		GeneralPath shape16 = new GeneralPath();
		shape16.moveTo(29.041664, 4.138653);
		shape16.lineTo(29.240124, 5.907565);
		shape16.curveTo(29.755754, 5.8513803, 30.27501, 5.8069763, 30.80061, 5.77979);
		shape16.lineTo(30.709084, 4.0000033);
		shape16.curveTo(30.147236, 4.029002, 29.592638, 4.0770307, 29.041664, 4.137747);
		shape16.lineTo(29.041664, 4.138653);
		shape16.closePath();
		g.fill(shape16);
	}

	private void paintShapeNode_0_0_0_8_1_8(Graphics2D g) {
		GeneralPath shape17 = new GeneralPath();
		shape17.moveTo(37.69411, 57.293015);
		shape17.lineTo(38.072903, 59.03293);
		shape17.curveTo(38.610283, 58.91965, 39.147663, 58.785534, 39.674168, 58.639633);
		shape17.lineTo(39.194786, 56.91966);
		shape17.curveTo(38.699093, 57.05921, 38.197056, 57.185177, 37.69411, 57.293015);
		shape17.lineTo(37.69411, 57.293015);
		shape17.closePath();
		g.fill(shape17);
	}

	private void paintShapeNode_0_0_0_8_1_9(Graphics2D g) {
		GeneralPath shape18 = new GeneralPath();
		shape18.moveTo(12.047603, 15.22879);
		shape18.lineTo(10.674703, 14.096033);
		shape18.curveTo(10.31947, 14.525574, 9.978737, 14.965084, 9.649785, 15.412749);
		shape18.lineTo(11.087932, 16.464853);
		shape18.curveTo(11.396042, 16.043468, 11.715026, 15.632051, 12.047603, 15.22879);
		shape18.closePath();
		g.fill(shape18);
	}

	private void paintShapeNode_0_0_0_8_1_10(Graphics2D g) {
		GeneralPath shape19 = new GeneralPath();
		shape19.moveTo(28.933826, 59.48603);
		shape19.curveTo(29.47755, 59.549465, 30.024899, 59.597492, 30.576777, 59.62921);
		shape19.lineTo(30.678272, 57.84852);
		shape19.curveTo(30.16083, 57.820423, 29.648823, 57.774208, 29.139536, 57.717117);
		shape19.lineTo(28.933826, 59.485123);
		shape19.lineTo(28.933826, 59.48603);
		shape19.closePath();
		g.fill(shape19);
	}

	private void paintShapeNode_0_0_0_8_1_11(Graphics2D g) {
		GeneralPath shape20 = new GeneralPath();
		shape20.moveTo(57.981323, 28.31892);
		shape20.lineTo(59.74933, 28.08059);
		shape20.curveTo(59.674114, 27.504242, 59.57896, 26.930614, 59.46659, 26.364237);
		shape20.lineTo(57.714897, 26.714031);
		shape20.curveTo(57.818207, 27.245068, 57.908825, 27.778824, 57.981323, 28.31892);
		shape20.lineTo(57.981323, 28.31892);
		shape20.closePath();
		g.fill(shape20);
	}

	private void paintShapeNode_0_0_0_8_1_12(Graphics2D g) {
		GeneralPath shape21 = new GeneralPath();
		shape21.moveTo(7.733161, 22.671452);
		shape21.lineTo(6.062119, 22.047075);
		shape21.curveTo(5.867285, 22.567238, 5.6851377, 23.093742, 5.521115, 23.627497);
		shape21.lineTo(7.223874, 24.15219);
		shape21.curveTo(7.379741, 23.654684, 7.5482955, 23.159897, 7.733161, 22.671452);
		shape21.closePath();
		g.fill(shape21);
	}

	private void paintShapeNode_0_0_0_8_1_13(Graphics2D g) {
		GeneralPath shape22 = new GeneralPath();
		shape22.moveTo(6.0820556, 32.484745);
		shape22.curveTo(6.069369, 31.979988, 6.0720873, 31.477951, 6.087493, 30.980446);
		shape22.lineTo(4.3068, 30.922447);
		shape22.curveTo(4.2895823, 31.453484, 4.2868633, 31.989958, 4.2995505, 32.530964);
		shape22.curveTo(4.3004565, 32.557243, 4.302269, 32.58624, 4.302269, 32.61252);
		shape22.lineTo(6.083868, 32.56449);
		shape22.curveTo(6.083868, 32.537304, 6.0820556, 32.51012, 6.0820556, 32.484745);
		shape22.lineTo(6.0820556, 32.484745);
		shape22.closePath();
		g.fill(shape22);
	}

	private void paintShapeNode_0_0_0_8_1_14(Graphics2D g) {
		GeneralPath shape23 = new GeneralPath();
		shape23.moveTo(50.36467, 10.747606);
		shape23.lineTo(49.199287, 12.096945);
		shape23.curveTo(49.50287, 12.363369, 49.808258, 12.633418, 50.103683, 12.915248);
		shape23.curveTo(50.187958, 12.994088, 50.265892, 13.080177, 50.35017, 13.162642);
		shape23.lineTo(51.597107, 11.883081);
		shape23.curveTo(51.507393, 11.797897, 51.41949, 11.710902, 51.329777, 11.623906);
		shape23.curveTo(51.01351, 11.324858, 50.690903, 11.031248, 50.36467, 10.747606);
		shape23.lineTo(50.36467, 10.747606);
		shape23.closePath();
		g.fill(shape23);
	}

	private void paintShapeNode_0_0_0_8_1_15(Graphics2D g) {
		GeneralPath shape24 = new GeneralPath();
		shape24.moveTo(59.991283, 31.105501);
		shape24.curveTo(59.991283, 31.076504, 59.986755, 31.046598, 59.986755, 31.018505);
		shape24.lineTo(58.205154, 31.07016);
		shape24.curveTo(58.205154, 31.09644, 58.20878, 31.12453, 58.20878, 31.149904);
		shape24.curveTo(58.223278, 31.690002, 58.218746, 32.22829, 58.20153, 32.76023);
		shape24.lineTo(59.982224, 32.825478);
		shape24.curveTo(60.000347, 32.256382, 60.00669, 31.681849, 59.991283, 31.106407);
		shape24.lineTo(59.991283, 31.105501);
		shape24.closePath();
		g.fill(shape24);
	}

	private void paintShapeNode_0_0_0_8_1_16(Graphics2D g) {
		GeneralPath shape25 = new GeneralPath();
		shape25.moveTo(57.025276, 19.262308);
		shape25.curveTo(56.762478, 18.735804, 56.47702, 18.219267, 56.177975, 17.712696);
		shape25.lineTo(54.63471, 18.618902);
		shape25.curveTo(54.917442, 19.093754, 55.18568, 19.574043, 55.434887, 20.064299);
		shape25.lineTo(57.02437, 19.262308);
		shape25.lineTo(57.025276, 19.262308);
		shape25.closePath();
		g.fill(shape25);
	}

	private void paintShapeNode_0_0_0_8_1_17(Graphics2D g) {
		GeneralPath shape26 = new GeneralPath();
		shape26.moveTo(41.847248, 7.608512);
		shape26.curveTo(42.339317, 7.807877, 42.828667, 8.022648, 43.307144, 8.249199);
		shape26.lineTo(44.071075, 6.6307163);
		shape26.curveTo(43.55907, 6.3887596, 43.038906, 6.161302, 42.5124, 5.9510627);
		shape26.lineTo(41.847248, 7.608512);
		shape26.lineTo(41.847248, 7.608512);
		shape26.closePath();
		g.fill(shape26);
	}

	private void paintShapeNode_0_0_0_8_1_18(Graphics2D g) {
		GeneralPath shape27 = new GeneralPath();
		shape27.moveTo(45.733055, 9.56229);
		shape27.curveTo(46.16894, 9.828714, 46.59939, 10.106013, 47.022587, 10.399623);
		shape27.lineTo(48.037537, 8.937008);
		shape27.curveTo(47.58806, 8.620743, 47.130424, 8.321695, 46.661915, 8.037147);
		shape27.lineTo(45.733055, 9.56229);
		shape27.closePath();
		g.fill(shape27);
	}

	private void paintShapeNode_0_0_0_8_1_19(Graphics2D g) {
		GeneralPath shape28 = new GeneralPath();
		shape28.moveTo(52.22239, 15.188917);
		shape28.curveTo(52.5305, 15.557742, 52.823204, 15.935629, 53.108658, 16.318048);
		shape28.lineTo(54.549522, 15.256882);
		shape28.curveTo(54.24232, 14.842746, 53.926056, 14.442204, 53.598915, 14.045286);
		shape28.lineTo(52.22239, 15.188917);
		shape28.closePath();
		g.fill(shape28);
	}

	private void paintShapeNode_0_0_0_8_1_20(Graphics2D g) {
		GeneralPath shape29 = new GeneralPath();
		shape29.moveTo(57.03615, 24.041634);
		shape29.lineTo(58.744347, 23.506971);
		shape29.curveTo(58.580322, 22.98681, 58.40452, 22.469366, 58.20878, 21.961893);
		shape29.lineTo(56.54227, 22.592611);
		shape29.curveTo(56.7217, 23.068369, 56.88572, 23.553188, 57.037056, 24.041634);
		shape29.lineTo(57.03615, 24.041634);
		shape29.closePath();
		g.fill(shape29);
	}

	private void paintShapeNode_0_0_0_8_1_21(Graphics2D g) {
		GeneralPath shape30 = new GeneralPath();
		shape30.moveTo(49.121357, 51.59208);
		shape30.lineTo(50.28311, 52.94051);
		shape30.curveTo(50.69634, 52.581654, 51.104134, 52.2092, 51.499237, 51.826786);
		shape30.lineTo(50.260456, 50.547222);
		shape30.curveTo(49.890724, 50.905174, 49.511024, 51.25497, 49.121357, 51.59117);
		shape30.lineTo(49.121357, 51.59208);
		shape30.closePath();
		g.fill(shape30);
	}

	private void paintShapeNode_0_0_0_8_1_22(Graphics2D g) {
		GeneralPath shape31 = new GeneralPath();
		shape31.moveTo(41.80375, 56.036106);
		shape31.lineTo(42.463467, 57.69265);
		shape31.curveTo(42.974567, 57.484222, 43.478416, 57.265827, 43.975925, 57.032024);
		shape31.lineTo(43.218334, 55.419888);
		shape31.curveTo(42.755264, 55.63738, 42.282227, 55.8449, 41.804657, 56.036106);
		shape31.lineTo(41.80375, 56.036106);
		shape31.closePath();
		g.fill(shape31);
	}

	private void paintShapeNode_0_0_0_8_1_23(Graphics2D g) {
		GeneralPath shape32 = new GeneralPath();
		shape32.moveTo(45.64606, 54.114044);
		shape32.lineTo(46.569485, 55.641907);
		shape32.curveTo(47.039803, 55.353737, 47.50197, 55.051064, 47.95235, 54.74114);
		shape32.lineTo(46.94103, 53.272182);
		shape32.curveTo(46.51964, 53.56398, 46.088287, 53.851246, 45.64606, 54.114952);
		shape32.lineTo(45.64606, 54.114044);
		shape32.closePath();
		g.fill(shape32);
	}

	private void paintShapeNode_0_0_0_8_1_24(Graphics2D g) {
		GeneralPath shape33 = new GeneralPath();
		shape33.moveTo(57.69496, 37.03027);
		shape33.lineTo(59.44031, 37.38822);
		shape33.curveTo(59.55087, 36.84812, 59.644207, 36.305305, 59.72033, 35.75977);
		shape33.lineTo(57.95776, 35.507847);
		shape33.curveTo(57.883453, 36.01804, 57.798267, 36.530045, 57.69496, 37.031178);
		shape33.lineTo(57.69496, 37.03027);
		shape33.closePath();
		g.fill(shape33);
	}

	private void paintShapeNode_0_0_0_8_1_25(Graphics2D g) {
		GeneralPath shape34 = new GeneralPath();
		shape34.moveTo(56.487896, 41.154408);
		shape34.lineTo(58.150784, 41.79147);
		shape34.curveTo(58.348335, 41.278557, 58.529575, 40.759304, 58.697224, 40.235516);
		shape34.lineTo(56.998997, 39.698135);
		shape34.curveTo(56.84494, 40.188393, 56.672764, 40.67412, 56.487896, 41.153503);
		shape34.lineTo(56.487896, 41.154408);
		shape34.closePath();
		g.fill(shape34);
	}

	private void paintShapeNode_0_0_0_8_1_26(Graphics2D g) {
		GeneralPath shape35 = new GeneralPath();
		shape35.moveTo(52.14083, 48.533634);
		shape35.lineTo(53.502857, 49.67364);
		shape35.curveTo(53.85718, 49.252254, 54.19973, 48.820904, 54.52687, 48.37958);
		shape35.lineTo(53.09325, 47.322945);
		shape35.curveTo(52.78786, 47.733456, 52.469784, 48.13672, 52.139923, 48.533634);
		shape35.lineTo(52.14083, 48.533634);
		shape35.closePath();
		g.fill(shape35);
	}

	private void paintShapeNode_0_0_0_8_1_27(Graphics2D g) {
		GeneralPath shape36 = new GeneralPath();
		shape36.moveTo(33.434948, 57.85758);
		shape36.lineTo(33.523754, 59.63646);
		shape36.curveTo(34.075634, 59.61018, 34.622074, 59.565777, 35.166706, 59.50687);
		shape36.lineTo(34.97278, 57.73524);
		shape36.curveTo(34.464397, 57.79324, 33.95239, 57.830395, 33.434948, 57.85758);
		shape36.lineTo(33.434948, 57.85758);
		shape36.closePath();
		g.fill(shape36);
	}

	private void paintShapeNode_0_0_0_8_1_28(Graphics2D g) {
		GeneralPath shape37 = new GeneralPath();
		shape37.moveTo(54.616585, 45.02209);
		shape37.lineTo(56.153507, 45.92467);
		shape37.curveTo(56.430805, 45.452538, 56.69723, 44.97225, 56.95006, 44.480183);
		shape37.lineTo(55.36239, 43.66822);
		shape37.curveTo(55.12859, 44.1322, 54.878475, 44.581676, 54.616585, 45.02209);
		shape37.lineTo(54.616585, 45.02209);
		shape37.closePath();
		g.fill(shape37);
	}

	private void paintShapeNode_0_0_0_8_1_29(Graphics2D g) {
		GeneralPath shape38 = new GeneralPath();
		shape38.moveTo(9.713219, 48.314335);
		shape38.curveTo(10.028578, 48.737534, 10.352094, 49.155293, 10.686483, 49.558556);
		shape38.lineTo(12.06029, 48.425797);
		shape38.curveTo(11.746743, 48.047005, 11.441352, 47.65371, 11.148648, 47.257698);
		shape38.lineTo(9.714126, 48.314335);
		shape38.lineTo(9.713219, 48.314335);
		shape38.closePath();
		g.fill(shape38);
	}

	private void paintShapeNode_0_0_0_8_1_30(Graphics2D g) {
		GeneralPath shape39 = new GeneralPath();
		shape39.moveTo(7.2836833, 44.368717);
		shape39.curveTo(7.538327, 44.873474, 7.8092823, 45.367355, 8.094737, 45.852177);
		shape39.lineTo(9.630754, 44.956844);
		shape39.curveTo(9.366142, 44.498306, 9.111499, 44.03614, 8.873167, 43.56763);
		shape39.lineTo(7.2836833, 44.369625);
		shape39.lineTo(7.2836833, 44.368717);
		shape39.closePath();
		g.fill(shape39);
	}

	private void paintShapeNode_0_0_0_8_1_31(Graphics2D g) {
		GeneralPath shape40 = new GeneralPath();
		shape40.moveTo(7.241998, 39.544987);
		shape40.lineTo(5.5410514, 40.07421);
		shape40.curveTo(5.709605, 40.613403, 5.8962836, 41.14897, 6.0938363, 41.67457);
		shape40.lineTo(7.7603474, 41.042946);
		shape40.curveTo(7.573669, 40.5545, 7.40149, 40.053368, 7.241998, 39.545895);
		shape40.lineTo(7.241998, 39.544987);
		shape40.closePath();
		g.fill(shape40);
	}

	private void paintShapeNode_0_0_0_8_1_32(Graphics2D g) {
		GeneralPath shape41 = new GeneralPath();
		shape41.moveTo(6.3077006, 35.312103);
		shape41.lineTo(4.542413, 35.551342);
		shape41.curveTo(4.6185346, 36.11047, 4.7118735, 36.6696, 4.8188057, 37.220573);
		shape41.lineTo(6.5677814, 36.871685);
		shape41.curveTo(6.4662867, 36.358772, 6.3792906, 35.83861, 6.3077006, 35.312103);
		shape41.lineTo(6.3077006, 35.312103);
		shape41.closePath();
		g.fill(shape41);
	}

	private void paintShapeNode_0_0_0_8_1_33(Graphics2D g) {
		GeneralPath shape42 = new GeneralPath();
		shape42.moveTo(6.5541883, 26.8264);
		shape42.lineTo(4.8043065, 26.486574);
		shape42.curveTo(4.709155, 26.982267, 4.6266904, 27.482494, 4.5551, 27.98725);
		shape42.lineTo(6.321294, 28.231924);
		shape42.curveTo(6.387447, 27.759792, 6.46538, 27.29219, 6.5541883, 26.827307);
		shape42.lineTo(6.5541883, 26.8264);
		shape42.closePath();
		g.fill(shape42);
	}

	private void paintShapeNode_0_0_0_8_1_34(Graphics2D g) {
		GeneralPath shape43 = new GeneralPath();
		shape43.moveTo(24.430895, 58.585262);
		shape43.curveTo(24.959211, 58.73569, 25.491154, 58.873436, 26.02944, 58.99396);
		shape43.lineTo(26.420921, 57.254047);
		shape43.curveTo(25.91707, 57.142582, 25.42047, 57.01481, 24.924776, 56.869816);
		shape43.lineTo(24.430895, 58.585262);
		shape43.lineTo(24.430895, 58.585262);
		shape43.closePath();
		g.fill(shape43);
	}

	private void paintShapeNode_0_0_0_8_1_35(Graphics2D g) {
		GeneralPath shape44 = new GeneralPath();
		shape44.moveTo(20.092892, 56.925095);
		shape44.curveTo(20.60399, 57.17249, 21.124151, 57.402664, 21.650658, 57.617435);
		shape44.lineTo(22.322155, 55.968143);
		shape44.curveTo(21.82918, 55.764244, 21.343454, 55.55038, 20.864979, 55.320206);
		shape44.lineTo(20.092892, 56.925095);
		shape44.lineTo(20.092892, 56.925095);
		shape44.closePath();
		g.fill(shape44);
	}

	private void paintShapeNode_0_0_0_8_1_36(Graphics2D g) {
		GeneralPath shape45 = new GeneralPath();
		shape45.moveTo(16.148182, 54.6043);
		shape45.curveTo(16.593126, 54.919662, 17.049854, 55.220524, 17.513832, 55.508698);
		shape45.lineTo(18.449036, 53.991707);
		shape45.curveTo(18.014963, 53.720753, 17.58814, 53.441643, 17.171286, 53.148937);
		shape45.lineTo(16.148182, 54.603397);
		shape45.lineTo(16.148182, 54.6043);
		shape45.closePath();
		g.fill(shape45);
	}

	private void paintShapeNode_0_0_0_8_1_37(Graphics2D g) {
		GeneralPath shape46 = new GeneralPath();
		shape46.moveTo(12.684665, 51.724384);
		shape46.curveTo(13.057115, 52.088676, 13.441346, 52.44663, 13.833733, 52.78827);
		shape46.lineTo(15.00455, 51.44527);
		shape46.curveTo(14.638443, 51.124477, 14.279586, 50.7919, 13.930697, 50.452072);
		shape46.lineTo(12.685571, 51.724384);
		shape46.lineTo(12.684665, 51.724384);
		shape46.closePath();
		g.fill(shape46);
	}

	private void paintCompositeGraphicsNode_0_0_0_8_1(Graphics2D g) {
		// _0_0_0_8_1_0
		AffineTransform trans_0_0_0_8_1_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_0(g);
		g.setTransform(trans_0_0_0_8_1_0);
		// _0_0_0_8_1_1
		AffineTransform trans_0_0_0_8_1_1 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_1(g);
		g.setTransform(trans_0_0_0_8_1_1);
		// _0_0_0_8_1_2
		AffineTransform trans_0_0_0_8_1_2 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_2(g);
		g.setTransform(trans_0_0_0_8_1_2);
		// _0_0_0_8_1_3
		AffineTransform trans_0_0_0_8_1_3 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_3(g);
		g.setTransform(trans_0_0_0_8_1_3);
		// _0_0_0_8_1_4
		AffineTransform trans_0_0_0_8_1_4 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_4(g);
		g.setTransform(trans_0_0_0_8_1_4);
		// _0_0_0_8_1_5
		AffineTransform trans_0_0_0_8_1_5 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_5(g);
		g.setTransform(trans_0_0_0_8_1_5);
		// _0_0_0_8_1_6
		AffineTransform trans_0_0_0_8_1_6 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_6(g);
		g.setTransform(trans_0_0_0_8_1_6);
		// _0_0_0_8_1_7
		AffineTransform trans_0_0_0_8_1_7 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_7(g);
		g.setTransform(trans_0_0_0_8_1_7);
		// _0_0_0_8_1_8
		AffineTransform trans_0_0_0_8_1_8 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_8(g);
		g.setTransform(trans_0_0_0_8_1_8);
		// _0_0_0_8_1_9
		AffineTransform trans_0_0_0_8_1_9 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_9(g);
		g.setTransform(trans_0_0_0_8_1_9);
		// _0_0_0_8_1_10
		AffineTransform trans_0_0_0_8_1_10 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_10(g);
		g.setTransform(trans_0_0_0_8_1_10);
		// _0_0_0_8_1_11
		AffineTransform trans_0_0_0_8_1_11 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_11(g);
		g.setTransform(trans_0_0_0_8_1_11);
		// _0_0_0_8_1_12
		AffineTransform trans_0_0_0_8_1_12 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_12(g);
		g.setTransform(trans_0_0_0_8_1_12);
		// _0_0_0_8_1_13
		AffineTransform trans_0_0_0_8_1_13 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_13(g);
		g.setTransform(trans_0_0_0_8_1_13);
		// _0_0_0_8_1_14
		AffineTransform trans_0_0_0_8_1_14 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_14(g);
		g.setTransform(trans_0_0_0_8_1_14);
		// _0_0_0_8_1_15
		AffineTransform trans_0_0_0_8_1_15 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_15(g);
		g.setTransform(trans_0_0_0_8_1_15);
		// _0_0_0_8_1_16
		AffineTransform trans_0_0_0_8_1_16 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_16(g);
		g.setTransform(trans_0_0_0_8_1_16);
		// _0_0_0_8_1_17
		AffineTransform trans_0_0_0_8_1_17 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_17(g);
		g.setTransform(trans_0_0_0_8_1_17);
		// _0_0_0_8_1_18
		AffineTransform trans_0_0_0_8_1_18 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_18(g);
		g.setTransform(trans_0_0_0_8_1_18);
		// _0_0_0_8_1_19
		AffineTransform trans_0_0_0_8_1_19 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_19(g);
		g.setTransform(trans_0_0_0_8_1_19);
		// _0_0_0_8_1_20
		AffineTransform trans_0_0_0_8_1_20 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_20(g);
		g.setTransform(trans_0_0_0_8_1_20);
		// _0_0_0_8_1_21
		AffineTransform trans_0_0_0_8_1_21 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_21(g);
		g.setTransform(trans_0_0_0_8_1_21);
		// _0_0_0_8_1_22
		AffineTransform trans_0_0_0_8_1_22 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_22(g);
		g.setTransform(trans_0_0_0_8_1_22);
		// _0_0_0_8_1_23
		AffineTransform trans_0_0_0_8_1_23 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_23(g);
		g.setTransform(trans_0_0_0_8_1_23);
		// _0_0_0_8_1_24
		AffineTransform trans_0_0_0_8_1_24 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_24(g);
		g.setTransform(trans_0_0_0_8_1_24);
		// _0_0_0_8_1_25
		AffineTransform trans_0_0_0_8_1_25 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_25(g);
		g.setTransform(trans_0_0_0_8_1_25);
		// _0_0_0_8_1_26
		AffineTransform trans_0_0_0_8_1_26 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_26(g);
		g.setTransform(trans_0_0_0_8_1_26);
		// _0_0_0_8_1_27
		AffineTransform trans_0_0_0_8_1_27 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_27(g);
		g.setTransform(trans_0_0_0_8_1_27);
		// _0_0_0_8_1_28
		AffineTransform trans_0_0_0_8_1_28 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_28(g);
		g.setTransform(trans_0_0_0_8_1_28);
		// _0_0_0_8_1_29
		AffineTransform trans_0_0_0_8_1_29 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_29(g);
		g.setTransform(trans_0_0_0_8_1_29);
		// _0_0_0_8_1_30
		AffineTransform trans_0_0_0_8_1_30 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_30(g);
		g.setTransform(trans_0_0_0_8_1_30);
		// _0_0_0_8_1_31
		AffineTransform trans_0_0_0_8_1_31 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_31(g);
		g.setTransform(trans_0_0_0_8_1_31);
		// _0_0_0_8_1_32
		AffineTransform trans_0_0_0_8_1_32 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_32(g);
		g.setTransform(trans_0_0_0_8_1_32);
		// _0_0_0_8_1_33
		AffineTransform trans_0_0_0_8_1_33 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_33(g);
		g.setTransform(trans_0_0_0_8_1_33);
		// _0_0_0_8_1_34
		AffineTransform trans_0_0_0_8_1_34 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_34(g);
		g.setTransform(trans_0_0_0_8_1_34);
		// _0_0_0_8_1_35
		AffineTransform trans_0_0_0_8_1_35 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_35(g);
		g.setTransform(trans_0_0_0_8_1_35);
		// _0_0_0_8_1_36
		AffineTransform trans_0_0_0_8_1_36 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_36(g);
		g.setTransform(trans_0_0_0_8_1_36);
		// _0_0_0_8_1_37
		AffineTransform trans_0_0_0_8_1_37 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_8_1_37(g);
		g.setTransform(trans_0_0_0_8_1_37);
	}

	private void paintCompositeGraphicsNode_0_0_0_8(Graphics2D g) {
		// _0_0_0_8_0
		AffineTransform trans_0_0_0_8_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintCompositeGraphicsNode_0_0_0_8_0(g);
		g.setTransform(trans_0_0_0_8_0);
		// _0_0_0_8_1
		AffineTransform trans_0_0_0_8_1 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintCompositeGraphicsNode_0_0_0_8_1(g);
		g.setTransform(trans_0_0_0_8_1);
	}

	private void paintShapeNode_0_0_0_9(Graphics2D g) {
		RoundRectangle2D.Double shape47 = new RoundRectangle2D.Double(93.15928649902344, 31.159286499023438, 70.68142700195312, 36.68142318725586, 6.6894731521606445, 6.689473628997803);
		g.setPaint(new Color(143, 143, 143, 255));
		g.fill(shape47);
	}

	private void paintCompositeGraphicsNode_0_0_0(Graphics2D g) {
		// _0_0_0_0
		g.setComposite(AlphaComposite.getInstance(3, 0.7816092f * origAlpha));
		AffineTransform trans_0_0_0_0 = g.getTransform();
		g.transform(new AffineTransform(1.0088765621185303f, 0.0f, 0.0f, -1.0088765621185303f, -35.395999908447266f, 246.62571716308594f));
		paintShapeNode_0_0_0_0(g);
		g.setTransform(trans_0_0_0_0);
		// _0_0_0_1
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_0_1 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_1(g);
		g.setTransform(trans_0_0_0_1);
		// _0_0_0_2
		AffineTransform trans_0_0_0_2 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_2(g);
		g.setTransform(trans_0_0_0_2);
		// _0_0_0_3
		g.setComposite(AlphaComposite.getInstance(3, 0.7816092f * origAlpha));
		AffineTransform trans_0_0_0_3 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_3(g);
		g.setTransform(trans_0_0_0_3);
		// _0_0_0_4
		g.setComposite(AlphaComposite.getInstance(3, 0.44f * origAlpha));
		AffineTransform trans_0_0_0_4 = g.getTransform();
		g.transform(new AffineTransform(1.0088765621185303f, 0.0f, 0.0f, -1.0088765621185303f, -35.395999908447266f, 246.62571716308594f));
		paintShapeNode_0_0_0_4(g);
		g.setTransform(trans_0_0_0_4);
		// _0_0_0_5
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_0_5 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_5(g);
		g.setTransform(trans_0_0_0_5);
		// _0_0_0_6
		g.setComposite(AlphaComposite.getInstance(3, 0.22988509f * origAlpha));
		AffineTransform trans_0_0_0_6 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_6(g);
		g.setTransform(trans_0_0_0_6);
		// _0_0_0_7
		AffineTransform trans_0_0_0_7 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_7(g);
		g.setTransform(trans_0_0_0_7);
		// _0_0_0_8
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_0_8 = g.getTransform();
		g.transform(new AffineTransform(0.7843140959739685f, 0.0f, 0.0f, 0.7843140959739685f, 31.787588119506836f, 27.044513702392578f));
		paintCompositeGraphicsNode_0_0_0_8(g);
		g.setTransform(trans_0_0_0_8);
		// _0_0_0_9
		g.setComposite(AlphaComposite.getInstance(3, 0.22988509f * origAlpha));
		AffineTransform trans_0_0_0_9 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_9(g);
		g.setTransform(trans_0_0_0_9);
	}

	private void paintShapeNode_0_0_1_0(Graphics2D g) {
		GeneralPath shape48 = new GeneralPath();
		shape48.moveTo(22.0, 202.0);
		shape48.curveTo(22.0, 202.0, 120.0, 204.0, 137.0, 198.0);
		shape48.curveTo(154.0, 192.0, 139.0, 188.0, 129.0, 188.0);
		shape48.curveTo(119.0, 188.0, 23.0, 202.0, 22.0, 202.0);
		shape48.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(33.0, 193.16000366210938), new Point2D.Double(155.70558166503906, 193.16000366210938), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -11.0f, 2.0f)));
		g.fill(shape48);
	}

	private void paintShapeNode_0_0_1_1(Graphics2D g) {
		GeneralPath shape49 = new GeneralPath();
		shape49.moveTo(33.0, 200.0);
		shape49.curveTo(33.0, 200.0, 131.0, 202.0, 148.0, 196.0);
		shape49.curveTo(165.0, 190.0, 150.0, 186.0, 140.0, 186.0);
		shape49.curveTo(130.0, 186.0, 34.0, 200.0, 33.0, 200.0);
		shape49.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(33.0, 193.16000366210938), new Point2D.Double(155.70558166503906, 193.16000366210938), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)));
		g.fill(shape49);
	}

	private void paintShapeNode_0_0_1_2_0(Graphics2D g) {
		GeneralPath shape50 = new GeneralPath();
		shape50.moveTo(41.80768, 189.10728);
		shape50.curveTo(43.987514, 191.86575, 37.344845, 200.51834, 27.116617, 208.59361);
		shape50.curveTo(16.8884, 216.66887, 5.229167, 218.97324, 4.7489047, 218.36546);
		shape50.curveTo(4.213636, 217.68813, 9.211727, 206.9544, 19.439955, 198.87914);
		shape50.curveTo(29.668173, 190.80386, 39.627842, 186.34879, 41.80768, 189.10728);
		shape50.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(37.8302116394043, 210.74517822265625), new Point2D.Double(42.072853088378906, 215.3413543701172), new float[] {0.0f,0.31578946f,0.61764693f,1.0f}, new Color[] {new Color(46, 52, 54, 255),new Color(238, 238, 236, 255),new Color(127, 131, 131, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -33.498897552490234f, -31.936145782470703f)));
		g.fill(shape50);
	}

	private void paintShapeNode_0_0_1_2_1(Graphics2D g) {
		GeneralPath shape51 = new GeneralPath();
		shape51.moveTo(9.043325, 209.60898);
		shape51.curveTo(12.055741, 211.78539, 13.329736, 213.5537, 14.149827, 215.94675);
		shape51.curveTo(14.149827, 215.94675, 15.04809, 216.63148, 15.04809, 216.63148);
		shape51.lineTo(28.3807, 210.3214);
		shape51.lineTo(17.796032, 197.70122);
		shape51.lineTo(8.818822, 208.04941);
		shape51.lineTo(9.043325, 209.60898);
		shape51.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(39.24442672729492, 203.67410278320312), new Point2D.Double(48.43681335449219, 214.98780822753906), new float[] {0.0f,0.31578946f,0.61764693f,1.0f}, new Color[] {new Color(46, 52, 54, 255),new Color(238, 238, 236, 255),new Color(127, 131, 131, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -33.498897552490234f, -31.936145782470703f)));
		g.fill(shape51);
	}

	private void paintShapeNode_0_0_1_2_2(Graphics2D g) {
		GeneralPath shape52 = new GeneralPath();
		shape52.moveTo(106.34431, 116.5197);
		shape52.curveTo(85.77179, 133.21696, 41.82492, 171.3043, 16.77834, 198.76624);
		shape52.curveTo(22.107601, 201.31596, 24.515232, 204.3447, 26.251717, 211.30138);
		shape52.curveTo(60.416763, 192.8701, 111.3661, 155.73473, 135.32526, 136.81012);
		shape52.curveTo(178.57327, 102.64985, 211.04982, 69.46014, 214.33936, 56.96062);
		shape52.curveTo(214.43362, 56.57746, 214.5571, 56.06383, 214.5942, 55.72131);
		shape52.curveTo(214.6148, 55.48087, 214.57928, 55.12899, 214.57016, 54.90989);
		shape52.curveTo(214.54594, 54.566242, 214.44598, 54.13016, 214.34415, 53.842777);
		shape52.curveTo(214.31622, 53.77272, 214.30167, 53.553543, 214.26881, 53.487076);
		shape52.curveTo(214.23349, 53.422436, 214.10715, 53.2924, 214.06685, 53.2314);
		shape52.curveTo(214.04546, 53.20181, 213.98853, 53.13222, 213.96587, 53.103558);
		shape52.curveTo(213.94322, 53.074883, 213.88872, 53.00337, 213.8649, 52.975716);
		shape52.curveTo(213.81488, 52.922382, 213.71764, 52.76936, 213.66293, 52.720024);
		shape52.curveTo(213.6059, 52.67267, 213.39604, 52.60774, 213.33435, 52.564358);
		shape52.curveTo(213.07838, 52.398716, 212.67732, 52.200466, 212.34862, 52.097343);
		shape52.curveTo(212.13757, 52.037716, 211.8035, 51.921665, 211.56482, 51.88601);
		shape52.curveTo(211.22302, 51.84277, 210.69476, 51.843933, 210.30019, 51.846886);
		shape52.curveTo(197.37866, 52.151997, 157.56956, 76.058464, 114.32154, 110.21874);
		shape52.curveTo(111.63054, 112.34427, 108.94726, 114.40706, 106.34431, 116.5197);
		shape52.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(28.55991554260254, 60.046409606933594), new Point2D.Double(28.697391510009766, 67.58842468261719), new float[] {0.0f,0.31578946f,0.9152094f,1.0f}, new Color[] {new Color(114, 159, 207, 255),new Color(165, 191, 218, 255),new Color(30, 92, 157, 255),new Color(92, 164, 242, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(3.5534191131591797f, -2.8067355155944824f, 2.8067355155944824f, 3.5534191131591797f, -175.07374572753906f, -8.813166618347168f)));
		g.fill(shape52);
	}

	private void paintShapeNode_0_0_1_2_3(Graphics2D g) {
		GeneralPath shape53 = new GeneralPath();
		shape53.moveTo(24.03472, 195.0658);
		shape53.curveTo(26.128515, 196.77269, 28.003893, 198.69801, 29.161243, 201.34135);
		shape53.curveTo(29.07182, 197.89075, 26.565456, 195.83823, 24.03472, 195.0658);
		shape53.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape53);
	}

	private void paintCompositeGraphicsNode_0_0_1_2(Graphics2D g) {
		// _0_0_1_2_0
		AffineTransform trans_0_0_1_2_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_1_2_0(g);
		g.setTransform(trans_0_0_1_2_0);
		// _0_0_1_2_1
		AffineTransform trans_0_0_1_2_1 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_1_2_1(g);
		g.setTransform(trans_0_0_1_2_1);
		// _0_0_1_2_2
		AffineTransform trans_0_0_1_2_2 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_1_2_2(g);
		g.setTransform(trans_0_0_1_2_2);
		// _0_0_1_2_3
		AffineTransform trans_0_0_1_2_3 = g.getTransform();
		g.transform(new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -18.529918670654297f, -15.815705299377441f));
		paintShapeNode_0_0_1_2_3(g);
		g.setTransform(trans_0_0_1_2_3);
	}

	private void paintShapeNode_0_0_1_3_0_0(Graphics2D g) {
		GeneralPath shape54 = new GeneralPath();
		shape54.moveTo(31.964436, 57.726574);
		shape54.curveTo(35.48695, 56.52491, 40.23736, 56.178204, 44.51491, 56.81058);
		shape54.curveTo(48.792465, 57.442963, 51.985386, 58.96398, 52.95055, 60.82909);
		g.setPaint(new Color(94, 94, 94, 255));
		g.setStroke(new BasicStroke(0.384238f,0,0,4.0f,null,0.0f));
		g.draw(shape54);
	}

	private void paintShapeNode_0_0_1_3_0_1(Graphics2D g) {
		GeneralPath shape55 = new GeneralPath();
		shape55.moveTo(31.964436, 57.726574);
		shape55.curveTo(35.48695, 56.52491, 40.23736, 56.178204, 44.51491, 56.81058);
		shape55.curveTo(48.792465, 57.442963, 51.985386, 58.96398, 52.95055, 60.82909);
		g.setPaint(new Color(255, 255, 255, 255));
		g.setStroke(new BasicStroke(0.192119f,0,0,4.0f,null,0.0f));
		g.draw(shape55);
	}

	private void paintCompositeGraphicsNode_0_0_1_3_0(Graphics2D g) {
		// _0_0_1_3_0_0
		AffineTransform trans_0_0_1_3_0_0 = g.getTransform();
		g.transform(new AffineTransform(4.079741477966309f, -2.5703048706054688f, 2.612147331237793f, 4.092135906219482f, -163.8147430419922f, -60.937889099121094f));
		paintShapeNode_0_0_1_3_0_0(g);
		g.setTransform(trans_0_0_1_3_0_0);
		// _0_0_1_3_0_1
		AffineTransform trans_0_0_1_3_0_1 = g.getTransform();
		g.transform(new AffineTransform(4.079741477966309f, -2.5703048706054688f, 2.612147331237793f, 4.092135906219482f, -163.8147430419922f, -61.187889099121094f));
		paintShapeNode_0_0_1_3_0_1(g);
		g.setTransform(trans_0_0_1_3_0_1);
	}

	private void paintShapeNode_0_0_1_3_1(Graphics2D g) {
		GeneralPath shape56 = new GeneralPath();
		shape56.moveTo(122.75, 103.875);
		shape56.curveTo(131.16888, 108.85987, 137.3704, 116.20516, 140.625, 126.6875);
		shape56.lineTo(136.375, 110.125);
		shape56.lineTo(122.75, 103.875);
		shape56.closePath();
		g.setPaint(new Color(0, 0, 0, 255));
		g.fill(shape56);
	}

	private void paintShapeNode_0_0_1_3_2(Graphics2D g) {
		GeneralPath shape57 = new GeneralPath();
		shape57.moveTo(136.93892, 90.693146);
		shape57.curveTo(131.75359, 94.93072, 126.79048, 99.29376, 122.12245, 103.43422);
		shape57.curveTo(135.10538, 109.75465, 140.01057, 119.66516, 143.32812, 130.2813);
		shape57.curveTo(150.5953, 125.18602, 158.21977, 119.55687, 165.8654, 113.51783);
		shape57.curveTo(202.35593, 84.6951, 228.95415, 55.681686, 230.69936, 43.830765);
		shape57.curveTo(230.7129, 43.72678, 230.74104, 43.47631, 230.75064, 43.375046);
		shape57.curveTo(230.75826, 43.275154, 230.7983, 43.01647, 230.80193, 42.91934);
		shape57.curveTo(230.80602, 42.679947, 230.79897, 42.329727, 230.77788, 42.107914);
		shape57.curveTo(230.76741, 42.020603, 230.71709, 41.83665, 230.70255, 41.752216);
		shape57.curveTo(230.65279, 41.503223, 230.5378, 41.135616, 230.4509, 40.912964);
		shape57.curveTo(230.41983, 40.84023, 230.4108, 40.62701, 230.37556, 40.557262);
		shape57.curveTo(230.31956, 40.4549, 230.1381, 40.269287, 230.07262, 40.173733);
		shape57.curveTo(230.04973, 40.14264, 229.99559, 40.076214, 229.97163, 40.045895);
		shape57.curveTo(229.9477, 40.01557, 229.8956, 39.947514, 229.87065, 39.91804);
		shape57.curveTo(229.79288, 39.832203, 229.65434, 39.612705, 229.56772, 39.53451);
		shape57.curveTo(229.50803, 39.484085, 229.3027, 39.425907, 229.23914, 39.378853);
		shape57.curveTo(229.04266, 39.24273, 228.71169, 39.04573, 228.48099, 38.93966);
		shape57.curveTo(228.40222, 38.905964, 228.23492, 38.814392, 228.1524, 38.783985);
		shape57.curveTo(227.94151, 38.712097, 227.60245, 38.624123, 227.36862, 38.572666);
		shape57.curveTo(227.27328, 38.5537, 227.01236, 38.53273, 226.91342, 38.517002);
		shape57.curveTo(226.81268, 38.50288, 226.56252, 38.47225, 226.45822, 38.461353);
		shape57.curveTo(214.52533, 37.41433, 180.14046, 56.569584, 143.64993, 85.39232);
		shape57.curveTo(141.37943, 87.18573, 139.12929, 88.90313, 136.93892, 90.693146);
		shape57.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(192.3330535888672, 111.39665985107422), new Point2D.Double(173.5340576171875, 86.14791870117188), new float[] {0.0f,0.14285721f,0.4656152f,0.7761835f,0.89721245f,1.0f}, new Color[] {new Color(223, 226, 227, 255),new Color(40, 42, 42, 255),new Color(202, 202, 202, 255),new Color(255, 255, 255, 255),new Color(160, 161, 160, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -33.498897552490234f, -31.936145782470703f)));
		g.fill(shape57);
	}

	private void paintShapeNode_0_0_1_3_3(Graphics2D g) {
		GeneralPath shape58 = new GeneralPath();
		shape58.moveTo(201.02455, 47.132412);
		shape58.curveTo(209.51341, 50.433155, 215.07076, 56.24664, 215.9695, 66.0532);
		shape58.curveTo(227.03989, 54.763573, 233.68031, 44.793762, 233.05072, 39.689842);
		shape58.curveTo(233.04607, 39.657322, 233.0323, 39.53307, 233.02704, 39.500954);
		shape58.curveTo(233.02116, 39.46925, 233.00983, 39.34336, 233.00336, 39.312065);
		shape58.curveTo(232.99626, 39.281166, 232.98737, 39.15364, 232.97968, 39.123165);
		shape58.curveTo(232.92981, 38.942802, 232.80238, 38.65486, 232.73033, 38.489693);
		shape58.curveTo(232.7177, 38.462585, 232.64261, 38.388515, 232.62936, 38.361855);
		shape58.curveTo(232.61548, 38.335613, 232.62016, 38.198757, 232.60567, 38.172962);
		shape58.curveTo(232.59053, 38.14759, 232.52045, 38.07004, 232.50468, 38.045113);
		shape58.curveTo(232.4883, 38.02063, 232.42072, 37.94132, 232.40372, 37.91727);
		shape58.curveTo(232.38605, 37.89367, 232.32101, 37.812576, 232.30272, 37.789433);
		shape58.curveTo(232.28444, 37.766273, 232.22061, 37.684227, 232.20174, 37.661594);
		shape58.curveTo(232.1823, 37.639458, 232.12079, 37.55535, 232.10077, 37.53374);
		shape58.curveTo(232.08017, 37.51264, 232.02095, 37.42649, 231.99979, 37.405903);
		shape58.curveTo(231.97804, 37.38581, 231.84383, 37.35868, 231.82152, 37.3391);
		shape58.curveTo(231.79865, 37.32003, 231.74397, 37.22982, 231.72054, 37.211258);
		shape58.curveTo(231.57654, 37.102913, 231.32594, 36.91226, 231.16203, 36.821987);
		shape58.curveTo(231.13419, 36.807446, 231.01218, 36.769253, 230.98376, 36.7552);
		shape58.curveTo(230.9548, 36.74164, 230.83498, 36.70147, 230.8055, 36.688408);
		shape58.curveTo(230.77547, 36.675842, 230.65778, 36.633686, 230.62721, 36.621605);
		shape58.curveTo(225.8079, 34.827034, 214.5712, 38.977806, 201.02455, 47.132412);
		shape58.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(225.5830535888672, 78.95732116699219), new Point2D.Double(212.5340576171875, 61.35008239746094), new float[] {0.0f,0.14285721f,0.4656152f,0.7761835f,0.89721245f,1.0f}, new Color[] {new Color(223, 226, 227, 255),new Color(40, 42, 42, 255),new Color(199, 199, 199, 255),new Color(255, 255, 255, 255),new Color(160, 161, 160, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -33.498897552490234f, -31.936145782470703f)));
		g.fill(shape58);
	}

	private void paintShapeNode_0_0_1_3_4(Graphics2D g) {
		GeneralPath shape59 = new GeneralPath();
		shape59.moveTo(36.364517, 54.473244);
		shape59.curveTo(36.846443, 55.27986, 36.010174, 56.13992, 34.47655, 56.414906);
		shape59.curveTo(32.94293, 56.68989, 31.267382, 56.28022, 30.69383, 55.49003);
		shape59.lineTo(33.5, 54.9375);
		shape59.closePath();
		g.setPaint(new RadialGradientPaint(new Point2D.Double(35.910640716552734, 54.91318893432617), 2.9034028f, new Point2D.Double(35.910640716552734, 54.91318893432617), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(112, 112, 112, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.6470903158187866f, 0.026652546599507332f, -0.01650943048298359f, 0.3932119905948639f, 12.621391296386719f, 32.624874114990234f)));
		g.fill(shape59);
	}

	private void paintShapeNode_0_0_1_3_5(Graphics2D g) {
		GeneralPath shape60 = new GeneralPath();
		shape60.moveTo(189.81528, 54.401344);
		shape60.curveTo(197.93973, 57.200233, 202.19548, 63.094093, 203.2503, 71.54868);
		shape60.curveTo(201.1795, 63.754436, 197.52351, 58.06174, 189.81528, 54.401344);
		shape60.closePath();
		g.setPaint(new RadialGradientPaint(new Point2D.Double(199.58090209960938, 61.701454162597656), 6.717515f, new Point2D.Double(199.58090209960938, 61.701454162597656), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 0),new Color(0, 0, 0, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0744322538375854f, 0.0f, 0.0f, 1.3713146448135376f, -15.605262756347656f, -23.581642150878906f)));
		g.fill(shape60);
	}

	private void paintShapeNode_0_0_1_3_6(Graphics2D g) {
		GeneralPath shape61 = new GeneralPath();
		shape61.moveTo(200.8984, 46.909588);
		shape61.curveTo(210.25339, 50.132397, 215.15372, 56.91894, 216.3683, 66.65406);
		shape61.curveTo(213.98384, 57.6793, 209.77412, 51.124386, 200.8984, 46.909588);
		shape61.closePath();
		g.setPaint(new RadialGradientPaint(new Point2D.Double(199.58090209960938, 61.24766540527344), 6.717515f, new Point2D.Double(199.58090209960938, 61.24766540527344), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(2.3652706146240234f, 0.0f, 0.0f, 3.018831968307495f, -261.1904602050781f, -130.76075744628906f)));
		g.fill(shape61);
	}

	private void paintShapeNode_0_0_1_3_7(Graphics2D g) {
		GeneralPath shape62 = new GeneralPath();
		shape62.moveTo(122.1527, 94.779655);
		shape62.curveTo(127.10058, 96.20394, 131.54312, 92.43158, 133.5548, 90.44862);
		shape62.curveTo(133.5548, 90.44862, 126.83861, 94.978806, 124.27402, 93.01189);
		shape62.lineTo(122.1527, 94.779655);
		shape62.closePath();
		g.setPaint(new LinearGradientPaint(new Point2D.Double(122.15270233154297, 92.77104949951172), new Point2D.Double(133.55479431152344, 92.77104949951172), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)));
		g.fill(shape62);
	}

	private void paintCompositeGraphicsNode_0_0_1_3(Graphics2D g) {
		// _0_0_1_3_0
		AffineTransform trans_0_0_1_3_0 = g.getTransform();
		g.transform(new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -14.865994453430176f, -12.965986251831055f));
		paintCompositeGraphicsNode_0_0_1_3_0(g);
		g.setTransform(trans_0_0_1_3_0);
		// _0_0_1_3_1
		g.setComposite(AlphaComposite.getInstance(3, 0.6206894f * origAlpha));
		AffineTransform trans_0_0_1_3_1 = g.getTransform();
		g.transform(new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -18.529918670654297f, -15.815705299377441f));
		paintShapeNode_0_0_1_3_1(g);
		g.setTransform(trans_0_0_1_3_1);
		// _0_0_1_3_2
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_1_3_2 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_1_3_2(g);
		g.setTransform(trans_0_0_1_3_2);
		// _0_0_1_3_3
		AffineTransform trans_0_0_1_3_3 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_1_3_3(g);
		g.setTransform(trans_0_0_1_3_3);
		// _0_0_1_3_4
		AffineTransform trans_0_0_1_3_4 = g.getTransform();
		g.transform(new AffineTransform(3.4896926879882812f, -2.2130277156829834f, 2.2343552112579346f, 3.5233216285705566f, -110.94986724853516f, -33.31517028808594f));
		paintShapeNode_0_0_1_3_4(g);
		g.setTransform(trans_0_0_1_3_4);
		// _0_0_1_3_5
		g.setComposite(AlphaComposite.getInstance(3, 0.33333334f * origAlpha));
		AffineTransform trans_0_0_1_3_5 = g.getTransform();
		g.transform(new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -18.529918670654297f, -15.815705299377441f));
		paintShapeNode_0_0_1_3_5(g);
		g.setTransform(trans_0_0_1_3_5);
		// _0_0_1_3_6
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_1_3_6 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_1_3_6(g);
		g.setTransform(trans_0_0_1_3_6);
		// _0_0_1_3_7
		g.setComposite(AlphaComposite.getInstance(3, 0.6206894f * origAlpha));
		AffineTransform trans_0_0_1_3_7 = g.getTransform();
		g.transform(new AffineTransform(1.15146005153656f, 0.0f, 0.0f, 1.15146005153656f, -18.529918670654297f, -15.815705299377441f));
		paintShapeNode_0_0_1_3_7(g);
		g.setTransform(trans_0_0_1_3_7);
	}

	private void paintShapeNode_0_0_1_4(Graphics2D g) {
		GeneralPath shape63 = new GeneralPath();
		shape63.moveTo(192.95164, 55.48556);
		shape63.lineTo(124.93299, 105.23515);
		shape63.curveTo(91.96187, 131.13522, 44.821396, 177.97517, 44.821396, 177.97517);
		shape63.curveTo(44.821396, 177.97517, 90.94434, 133.83934, 127.44567, 106.7104);
		shape63.lineTo(195.07396, 56.6228);
		shape63.curveTo(202.21284, 50.433647, 213.83136, 46.13927, 213.83136, 46.13927);
		shape63.curveTo(213.83136, 46.13927, 200.16273, 50.67321, 192.95164, 55.48556);
		shape63.closePath();
		g.setPaint(new RadialGradientPaint(new Point2D.Double(126.25, 139.8346405029297), 82.75f, new Point2D.Double(126.25, 139.8346405029297), new float[] {0.0f,1.0f}, new Color[] {new Color(255, 255, 255, 255),new Color(255, 255, 255, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.6706949472427368f, 0.0f, 0.0f, 1.2820438146591187f, -97.67522430419922f, -59.77413558959961f)));
		g.fill(shape63);
	}

	private void paintShapeNode_0_0_1_5(Graphics2D g) {
		GeneralPath shape64 = new GeneralPath();
		shape64.moveTo(213.07231, 47.70424);
		shape64.curveTo(213.07231, 47.70424, 206.46033, 57.000546, 200.9806, 60.947353);
		shape64.curveTo(200.9806, 60.947353, 191.55939, 72.161736, 136.0, 116.0);
		shape64.lineTo(98.43314, 145.95203);
		shape64.lineTo(137.5, 117.5);
		shape64.curveTo(162.58234, 99.49744, 201.9806, 62.447353, 201.9806, 62.447353);
		shape64.curveTo(208.66647, 56.744534, 213.07231, 47.70424, 213.07231, 47.70424);
		shape64.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape64);
	}

	private void paintShapeNode_0_0_1_6(Graphics2D g) {
		GeneralPath shape65 = new GeneralPath();
		shape65.moveTo(16.793785, 238.32233);
		shape65.curveTo(16.794249, 238.9542, 16.457417, 239.53827, 15.910276, 239.85434);
		shape65.curveTo(15.363137, 240.17041, 14.6889, 240.17041, 14.141761, 239.85434);
		shape65.curveTo(13.594621, 239.53827, 13.257788, 238.9542, 13.258252, 238.32233);
		shape65.curveTo(13.257788, 237.69046, 13.594621, 237.10638, 14.141761, 236.79031);
		shape65.curveTo(14.6889, 236.47424, 15.363137, 236.47424, 15.910276, 236.79031);
		shape65.curveTo(16.457417, 237.10638, 16.794249, 237.69046, 16.793785, 238.32233);
		shape65.closePath();
		g.setPaint(new RadialGradientPaint(new Point2D.Double(15.026019096374512, 238.32232666015625), 1.767767f, new Point2D.Double(15.026019096374512, 238.32232666015625), new float[] {0.0f,1.0f}, new Color[] {new Color(3, 15, 115, 255),new Color(3, 15, 115, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)));
		g.fill(shape65);
	}

	private void paintShapeNode_0_0_1_7(Graphics2D g) {
		GeneralPath shape66 = new GeneralPath();
		shape66.moveTo(16.793785, 238.32233);
		shape66.curveTo(16.794249, 238.9542, 16.457417, 239.53827, 15.910276, 239.85434);
		shape66.curveTo(15.363137, 240.17041, 14.6889, 240.17041, 14.141761, 239.85434);
		shape66.curveTo(13.594621, 239.53827, 13.257788, 238.9542, 13.258252, 238.32233);
		shape66.curveTo(13.257788, 237.69046, 13.594621, 237.10638, 14.141761, 236.79031);
		shape66.curveTo(14.6889, 236.47424, 15.363137, 236.47424, 15.910276, 236.79031);
		shape66.curveTo(16.457417, 237.10638, 16.794249, 237.69046, 16.793785, 238.32233);
		shape66.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape66);
	}

	private void paintCompositeGraphicsNode_0_0_1(Graphics2D g) {
		// _0_0_1_0
		g.setComposite(AlphaComposite.getInstance(3, 0.62643677f * origAlpha));
		AffineTransform trans_0_0_1_0 = g.getTransform();
		g.transform(new AffineTransform(1.015053629875183f, -0.4941403865814209f, 0.5084705948829651f, 1.0444906949996948f, -68.7270278930664f, 10.888948440551758f));
		paintShapeNode_0_0_1_0(g);
		g.setTransform(trans_0_0_1_0);
		// _0_0_1_1
		g.setComposite(AlphaComposite.getInstance(3, 0.47126436f * origAlpha));
		AffineTransform trans_0_0_1_1 = g.getTransform();
		g.transform(new AffineTransform(1.4721611738204956f, -0.7328161001205444f, 0.7374494075775146f, 1.5489921569824219f, -125.63784790039062f, -79.13948822021484f));
		paintShapeNode_0_0_1_1(g);
		g.setTransform(trans_0_0_1_1);
		// _0_0_1_2
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_1_2 = g.getTransform();
		g.transform(new AffineTransform(0.9324427843093872f, -0.13802079856395721f, 0.13802079856395721f, 0.9324427843093872f, 27.211082458496094f, 2.910223960876465f));
		paintCompositeGraphicsNode_0_0_1_2(g);
		g.setTransform(trans_0_0_1_2);
		// _0_0_1_3
		AffineTransform trans_0_0_1_3 = g.getTransform();
		g.transform(new AffineTransform(0.9324427843093872f, -0.13802079856395721f, 0.13802079856395721f, 0.9324427843093872f, 27.211082458496094f, 2.910223960876465f));
		paintCompositeGraphicsNode_0_0_1_3(g);
		g.setTransform(trans_0_0_1_3);
		// _0_0_1_4
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_1_4 = g.getTransform();
		g.transform(new AffineTransform(1.073670744895935f, -0.158925399184227f, 0.158925399184227f, 1.073670744895935f, 8.457204818725586f, -7.865288734436035f));
		paintShapeNode_0_0_1_4(g);
		g.setTransform(trans_0_0_1_4);
		// _0_0_1_5
		g.setComposite(AlphaComposite.getInstance(3, 0.46551722f * origAlpha));
		AffineTransform trans_0_0_1_5 = g.getTransform();
		g.transform(new AffineTransform(1.073670744895935f, -0.158925399184227f, 0.158925399184227f, 1.073670744895935f, 9.75009536743164f, -3.77950119972229f));
		paintShapeNode_0_0_1_5(g);
		g.setTransform(trans_0_0_1_5);
		// _0_0_1_6
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_1_6 = g.getTransform();
		g.transform(new AffineTransform(0.807101309299469f, 0.0f, 0.0f, 0.807101309299469f, -1.0889465808868408f, 42.99856948852539f));
		paintShapeNode_0_0_1_6(g);
		g.setTransform(trans_0_0_1_6);
		// _0_0_1_7
		AffineTransform trans_0_0_1_7 = g.getTransform();
		g.transform(new AffineTransform(0.5801039934158325f, 0.0f, 0.0f, 0.5801039934158325f, 2.1435720920562744f, 96.7403793334961f));
		paintShapeNode_0_0_1_7(g);
		g.setTransform(trans_0_0_1_7);
	}

	private void paintCanvasGraphicsNode_0_0(Graphics2D g) {
		// _0_0_0
		AffineTransform trans_0_0_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintCompositeGraphicsNode_0_0_0(g);
		g.setTransform(trans_0_0_0);
		// _0_0_1
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0_1 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintCompositeGraphicsNode_0_0_1(g);
		g.setTransform(trans_0_0_1);
	}

	private void paintRootGraphicsNode_0(Graphics2D g) {
		// _0_0
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		AffineTransform trans_0_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
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
        return 0;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 256;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 256;
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
	public register() {
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

