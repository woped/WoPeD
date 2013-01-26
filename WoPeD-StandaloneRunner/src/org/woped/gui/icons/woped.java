package org.woped.gui.icons;

import java.awt.*;
import java.awt.geom.*;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

/**
 * This class has been automatically generated using svg2java
 * 
 */
public class woped implements ResizableIcon {
	
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
		shape0.moveTo(28.383, 4.743);
		shape0.lineTo(182.747, 4.743);
		shape0.curveTo(194.875, 4.743, 204.706, 14.574, 204.706, 26.702);
		shape0.lineTo(204.706, 174.767);
		shape0.curveTo(204.706, 186.895, 194.875, 196.72699, 182.747, 196.72699);
		shape0.lineTo(28.383, 196.72699);
		shape0.curveTo(16.255, 196.72699, 6.4230003, 186.89499, 6.4230003, 174.767);
		shape0.lineTo(6.4230003, 26.702);
		shape0.curveTo(6.423, 14.574, 16.255, 4.743, 28.383, 4.743);
		shape0.closePath();
		g.setPaint(new Color(255, 127, 42, 255));
		g.fill(shape0);
	}

	private void paintTextNode_0_0_0_1(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		GeneralPath shape1 = new GeneralPath();
		shape1.moveTo(26.929688, 0.0);
		shape1.lineTo(0.140625, -104.0625);
		shape1.lineTo(14.34375, -104.0625);
		shape1.lineTo(35.648438, -21.867188);
		shape1.lineTo(55.265625, -104.0625);
		shape1.lineTo(69.46875, -104.0625);
		shape1.lineTo(88.03125, -22.992188);
		shape1.lineTo(110.953125, -104.0625);
		shape1.lineTo(122.97656, -104.0625);
		shape1.lineTo(93.796875, 0.0);
		shape1.lineTo(79.10156, 0.0);
		shape1.lineTo(60.75, -80.22656);
		shape1.lineTo(41.554688, 0.0);
		shape1.closePath();
		g.setPaint(new Color(255, 255, 255, 255));
		g.fill(shape1);
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
	}

	private void paintShapeNode_0_0_0_2(Graphics2D g) {
		GeneralPath shape2 = new GeneralPath();
		shape2.moveTo(24.684, 146.057);
		shape2.lineTo(186.016, 146.057);
		shape2.curveTo(196.29501, 146.057, 204.62701, 154.39001, 204.62701, 164.668);
		shape2.lineTo(204.62701, 182.736);
		shape2.curveTo(204.62701, 193.015, 196.29501, 201.34698, 186.01602, 201.34698);
		shape2.lineTo(24.684, 201.34698);
		shape2.curveTo(14.406, 201.34698, 6.073, 193.01498, 6.073, 182.736);
		shape2.lineTo(6.073, 164.668);
		shape2.curveTo(6.073, 154.39, 14.406, 146.057, 24.684, 146.057);
		shape2.closePath();
		g.setPaint(new Color(255, 173, 0, 241));
		g.fill(shape2);
	}

	private void paintShapeNode_0_0_0_3(Graphics2D g) {
		Rectangle2D.Double shape3 = new Rectangle2D.Double(5.561999797821045, 143.93299865722656, 200.25399780273438, 1.9199999570846558);
		g.setPaint(new Color(255, 255, 255, 241));
		g.fill(shape3);
	}

	private void paintShapeNode_0_0_0_4(Graphics2D g) {
		Rectangle2D.Double shape4 = new Rectangle2D.Double(5.946000099182129, 145.85299682617188, 198.89599609375, 33.404998779296875);
		g.setPaint(new Color(255, 173, 0, 241));
		g.fill(shape4);
	}

	private void paintTextNode_0_0_0_5(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
		GeneralPath shape5 = new GeneralPath();
		shape5.moveTo(7.4804688, 0.0);
		shape5.lineTo(0.0390625, -28.90625);
		shape5.lineTo(3.984375, -28.90625);
		shape5.lineTo(9.902344, -6.0742188);
		shape5.lineTo(15.3515625, -28.90625);
		shape5.lineTo(19.296875, -28.90625);
		shape5.lineTo(24.453125, -6.3867188);
		shape5.lineTo(30.820312, -28.90625);
		shape5.lineTo(34.160156, -28.90625);
		shape5.lineTo(26.054688, 0.0);
		shape5.lineTo(21.972656, 0.0);
		shape5.lineTo(16.875, -22.285156);
		shape5.lineTo(11.542969, 0.0);
		shape5.closePath();
		shape5.moveTo(46.367188, 0.48828125);
		shape5.quadTo(41.816406, 0.48828125, 39.101562, -2.5292969);
		shape5.quadTo(36.38672, -5.546875, 36.38672, -10.605469);
		shape5.quadTo(36.38672, -15.722656, 39.11133, -18.701172);
		shape5.quadTo(41.835938, -21.679688, 46.503906, -21.679688);
		shape5.quadTo(51.171875, -21.679688, 53.896484, -18.701172);
		shape5.quadTo(56.621094, -15.722656, 56.621094, -10.644531);
		shape5.quadTo(56.621094, -5.4492188, 53.88672, -2.4804688);
		shape5.quadTo(51.152344, 0.48828125, 46.367188, 0.48828125);
		shape5.closePath();
		shape5.moveTo(46.42578, -2.4023438);
		shape5.quadTo(52.539062, -2.4023438, 52.539062, -10.644531);
		shape5.quadTo(52.539062, -18.789062, 46.503906, -18.789062);
		shape5.quadTo(40.48828, -18.789062, 40.48828, -10.605469);
		shape5.quadTo(40.48828, -2.4023438, 46.42578, -2.4023438);
		shape5.closePath();
		shape5.moveTo(62.51953, 0.0);
		shape5.lineTo(62.51953, -28.90625);
		shape5.lineTo(70.390625, -28.90625);
		shape5.quadTo(75.625, -28.90625, 77.91016, -27.138672);
		shape5.quadTo(80.19531, -25.371094, 80.19531, -21.328125);
		shape5.quadTo(80.19531, -16.71875, 77.07031, -14.1015625);
		shape5.quadTo(73.94531, -11.484375, 68.39844, -11.484375);
		shape5.lineTo(66.58203, -11.484375);
		shape5.lineTo(66.58203, 0.0);
		shape5.closePath();
		shape5.moveTo(66.58203, -14.589844);
		shape5.lineTo(68.24219, -14.589844);
		shape5.quadTo(71.89453, -14.589844, 73.88672, -16.269531);
		shape5.quadTo(75.87891, -17.949219, 75.87891, -21.015625);
		shape5.quadTo(75.87891, -23.613281, 74.31641, -24.726562);
		shape5.quadTo(72.75391, -25.839844, 69.10156, -25.839844);
		shape5.lineTo(66.58203, -25.839844);
		shape5.closePath();
		shape5.moveTo(100.29297, -0.68359375);
		shape5.quadTo(96.42578, 0.48828125, 93.671875, 0.48828125);
		shape5.quadTo(88.984375, 0.48828125, 86.02539, -2.6269531);
		shape5.quadTo(83.06641, -5.7421875, 83.06641, -10.703125);
		shape5.quadTo(83.06641, -15.527344, 85.67383, -18.613281);
		shape5.quadTo(88.28125, -21.699219, 92.34375, -21.699219);
		shape5.quadTo(96.19141, -21.699219, 98.291016, -18.964844);
		shape5.quadTo(100.390625, -16.230469, 100.390625, -11.191406);
		shape5.lineTo(100.37109, -10.0);
		shape5.lineTo(86.99219, -10.0);
		shape5.quadTo(87.83203, -2.4414062, 94.39453, -2.4414062);
		shape5.quadTo(96.796875, -2.4414062, 100.29297, -3.7304688);
		shape5.closePath();
		shape5.moveTo(87.16797, -12.890625);
		shape5.lineTo(96.52344, -12.890625);
		shape5.quadTo(96.52344, -18.808594, 92.109375, -18.808594);
		shape5.quadTo(87.67578, -18.808594, 87.16797, -12.890625);
		shape5.closePath();
		shape5.moveTo(106.91406, 0.0);
		shape5.lineTo(106.91406, -28.90625);
		shape5.lineTo(116.52344, -28.90625);
		shape5.quadTo(120.82031, -28.90625, 123.4668, -27.949219);
		shape5.quadTo(126.11328, -26.992188, 128.04688, -24.707031);
		shape5.quadTo(131.11328, -21.074219, 131.11328, -15.136719);
		shape5.quadTo(131.11328, -7.9296875, 127.30469, -3.9648438);
		shape5.quadTo(123.49609, 0.0, 116.58203, 0.0);
		shape5.closePath();
		shape5.moveTo(111.015625, -3.0664062);
		shape5.lineTo(116.26953, -3.0664062);
		shape5.quadTo(121.89453, -3.0664062, 124.23828, -6.09375);
		shape5.quadTo(126.75781, -9.316406, 126.75781, -14.746094);
		shape5.quadTo(126.75781, -19.84375, 124.27734, -22.753906);
		shape5.quadTo(122.77344, -24.53125, 120.68359, -25.185547);
		shape5.quadTo(118.59375, -25.839844, 114.39453, -25.839844);
		shape5.lineTo(111.015625, -25.839844);
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
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 37.37590026855469f, 130.9810028076172f));
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
		g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 39.21580123901367f, 187.02490234375f));
		paintTextNode_0_0_0_5(g);
		g.setTransform(trans_0_0_0_5);
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
		g.transform(new AffineTransform(0.9987316131591797f, 0.0f, 0.0f, 0.9987316131591797f, -0.0f, 0.3221921350923367f));
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
        return 200;
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

