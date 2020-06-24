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
import java.awt.geom.RoundRectangle2D;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class woped implements org.pushingpixels.flamingo.api.common.icon.ResizableIcon {
	
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
		RoundRectangle2D.Double shape0 = new RoundRectangle2D.Double(6.423509120941162, 4.742427349090576, 198.28317260742188, 191.98423767089844, 43.91897201538086, 43.91897201538086);
		g.setPaint(new Color(255, 127, 42, 255));
		g.fill(shape0);
	}

	private void paintTextNode_0_0_0_1(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo(59.632633, 130.98059);
		shape1.lineTo(33.65607, 22.175901);
		shape1.lineTo(56.144352, 22.175901);
		shape1.lineTo(72.5467, 96.914185);
		shape1.lineTo(92.437325, 22.175901);
		shape1.lineTo(118.562325, 22.175901);
		shape1.lineTo(137.63654, 98.1759);
		shape1.lineTo(154.33575, 22.175901);
		shape1.lineTo(176.45294, 22.175901);
		shape1.lineTo(150.03107, 130.98059);
		shape1.lineTo(126.72639, 130.98059);
		shape1.lineTo(105.05451, 49.636837);
		shape1.lineTo(83.456856, 130.98059);
		shape1.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape1);
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
	}

	private void paintShapeNode_0_0_0_2(Graphics2D g) {
		RoundRectangle2D.Double shape2 = new RoundRectangle2D.Double(6.073121547698975, 146.05706787109375, 198.55467224121094, 55.29145812988281, 37.22266387939453, 37.22266387939453);
		g.setPaint(new Color(255, 173, 0, 241));
		g.fill(shape2);
	}

	private void paintShapeNode_0_0_0_3(Graphics2D g) {
		Rectangle2D.Double shape3 = new Rectangle2D.Double(5.56222677230835, 143.93276977539062, 200.2532196044922, 1.9198436737060547);
		g.setPaint(new Color(255, 255, 255, 241));
		g.fill(shape3);
	}

	private void paintShapeNode_0_0_0_4(Graphics2D g) {
		Rectangle2D.Double shape4 = new Rectangle2D.Double(5.946183681488037, -179.25787353515625, 198.8957061767578, 33.40525817871094);
		g.setPaint(new Color(255, 173, 0, 241));
		g.fill(shape4);
	}

	private void paintTextNode_0_0_0_5(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo(26.787035, 158.39166);
		shape5.lineTo(35.18547, 158.39166);
		shape5.lineTo(38.212814, 174.38776);
		shape5.lineTo(42.626877, 158.39166);
		shape5.lineTo(51.005783, 158.39166);
		shape5.lineTo(55.439377, 174.38776);
		shape5.lineTo(58.46672, 158.39166);
		shape5.lineTo(66.826096, 158.39166);
		shape5.lineTo(60.5175, 187.02448);
		shape5.lineTo(51.845627, 187.02448);
		shape5.lineTo(46.826096, 168.99713);
		shape5.lineTo(41.826096, 187.02448);
		shape5.lineTo(33.15422, 187.02448);
		shape5.closePath();
		shape5.moveTo(68.232346, 176.71198);
		shape5.quadTo(68.232346, 171.96588, 71.43547, 168.88971);
		shape5.quadTo(74.638596, 165.81354, 80.087814, 165.81354);
		shape5.quadTo(86.31828, 165.81354, 89.50188, 169.42682);
		shape5.quadTo(92.06047, 172.33698, 92.06047, 176.59479);
		shape5.quadTo(92.06047, 181.37994, 88.88664, 184.43658);
		shape5.quadTo(85.712814, 187.49323, 80.107346, 187.49323);
		shape5.quadTo(75.107346, 187.49323, 72.02141, 184.95416);
		shape5.quadTo(68.232346, 181.80963, 68.232346, 176.71198);
		shape5.closePath();
		shape5.moveTo(76.201096, 176.69244);
		shape5.quadTo(76.201096, 179.46588, 77.32414, 180.794);
		shape5.quadTo(78.44719, 182.12213, 80.14641, 182.12213);
		shape5.quadTo(81.86516, 182.12213, 82.96867, 180.81354);
		shape5.quadTo(84.07219, 179.50494, 84.07219, 176.61432);
		shape5.quadTo(84.07219, 173.919, 82.95891, 172.60065);
		shape5.quadTo(81.84563, 171.28229, 80.205, 171.28229);
		shape5.quadTo(78.46672, 171.28229, 77.33391, 172.62018);
		shape5.quadTo(76.201096, 173.95807, 76.201096, 176.69244);
		shape5.closePath();
		shape5.moveTo(96.37688, 158.39166);
		shape5.lineTo(111.08391, 158.39166);
		shape5.quadTo(115.888596, 158.39166, 118.28117, 160.67682);
		shape5.quadTo(120.67375, 162.96198, 120.67375, 167.18073);
		shape5.quadTo(120.67375, 171.51666, 118.06633, 173.95807);
		shape5.quadTo(115.45891, 176.39948, 110.107346, 176.39948);
		shape5.lineTo(105.263596, 176.39948);
		shape5.lineTo(105.263596, 187.02448);
		shape5.lineTo(96.37688, 187.02448);
		shape5.closePath();
		shape5.moveTo(105.263596, 170.5987);
		shape5.lineTo(107.431564, 170.5987);
		shape5.quadTo(109.99016, 170.5987, 111.025314, 169.71002);
		shape5.quadTo(112.06047, 168.82135, 112.06047, 167.43463);
		shape5.quadTo(112.06047, 166.08698, 111.16203, 165.14948);
		shape5.quadTo(110.263596, 164.21198, 107.78313, 164.21198);
		shape5.lineTo(105.263596, 164.21198);
		shape5.closePath();
		shape5.moveTo(147.80266, 178.62604);
		shape5.lineTo(131.86516, 178.62604);
		shape5.quadTo(132.08, 180.5401, 132.90031, 181.4776);
		shape5.quadTo(134.05266, 182.82526, 135.90813, 182.82526);
		shape5.quadTo(137.08, 182.82526, 138.13469, 182.23932);
		shape5.quadTo(138.77922, 181.86823, 139.52141, 180.93073);
		shape5.lineTo(147.35344, 181.65338);
		shape5.quadTo(145.55656, 184.77838, 143.0175, 186.1358);
		shape5.quadTo(140.47844, 187.49323, 135.73235, 187.49323);
		shape5.quadTo(131.61125, 187.49323, 129.24797, 186.33112);
		shape5.quadTo(126.88469, 185.169, 125.331955, 182.63971);
		shape5.quadTo(123.77922, 180.11041, 123.77922, 176.69244);
		shape5.quadTo(123.77922, 171.82916, 126.894455, 168.82135);
		shape5.quadTo(130.00969, 165.81354, 135.49797, 165.81354);
		shape5.quadTo(139.9511, 165.81354, 142.52922, 167.1612);
		shape5.quadTo(145.10735, 168.50885, 146.455, 171.06744);
		shape5.quadTo(147.80266, 173.62604, 147.80266, 177.7276);
		shape5.closePath();
		shape5.moveTo(139.71672, 174.81744);
		shape5.quadTo(139.48235, 172.51276, 138.47649, 171.51666);
		shape5.quadTo(137.47063, 170.52057, 135.83, 170.52057);
		shape5.quadTo(133.93547, 170.52057, 132.80266, 172.02448);
		shape5.quadTo(132.08, 172.96198, 131.88469, 174.81744);
		shape5.closePath();
		shape5.moveTo(152.09953, 158.39166);
		shape5.lineTo(165.24406, 158.39166);
		shape5.quadTo(169.13078, 158.39166, 171.52336, 159.44635);
		shape5.quadTo(173.91594, 160.50104, 175.47844, 162.4737);
		shape5.quadTo(177.04094, 164.44635, 177.74406, 167.06354);
		shape5.quadTo(178.44719, 169.68073, 178.44719, 172.61041);
		shape5.quadTo(178.44719, 177.20026, 177.40227, 179.72955);
		shape5.quadTo(176.35735, 182.25885, 174.50188, 183.96783);
		shape5.quadTo(172.64641, 185.67682, 170.5175, 186.24323);
		shape5.quadTo(167.60735, 187.02448, 165.24406, 187.02448);
		shape5.lineTo(152.09953, 187.02448);
		shape5.closePath();
		shape5.moveTo(160.94719, 164.87604);
		shape5.lineTo(160.94719, 180.52057);
		shape5.lineTo(163.11516, 180.52057);
		shape5.quadTo(165.8886, 180.52057, 167.06047, 179.90533);
		shape5.quadTo(168.23235, 179.2901, 168.89641, 177.7569);
		shape5.quadTo(169.56047, 176.2237, 169.56047, 172.7862);
		shape5.quadTo(169.56047, 168.23541, 168.0761, 166.55573);
		shape5.quadTo(166.59172, 164.87604, 163.15422, 164.87604);
		shape5.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape5);
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
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
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
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f));
		paintShapeNode_0_0_0_4(g);
		g.setTransform(trans_0_0_0_4);
		// _0_0_0_5
		AffineTransform trans_0_0_0_5 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintTextNode_0_0_0_5(g);
		g.setTransform(trans_0_0_0_5);
	}

	private void paintCanvasGraphicsNode_0_0(Graphics2D g) {
		// _0_0_0
		AffineTransform trans_0_0_0 = g.getTransform();
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
		paintCompositeGraphicsNode_0_0_0(g);
		g.setTransform(trans_0_0_0);
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
        return 6;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * @return The Y of the bounding box of the original SVG image.
     */
    public int getOrigY() {
        return 5;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * @return The width of the bounding box of the original SVG image.
     */
    public int getOrigWidth() {
        return 201;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * @return The height of the bounding box of the original SVG image.
     */
    public int getOrigHeight() {
        return 197;
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
	public woped() {
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

