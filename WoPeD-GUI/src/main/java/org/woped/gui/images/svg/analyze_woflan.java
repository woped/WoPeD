package org.woped.gui.images.svg;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class analyze_woflan implements
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
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(5.644, 50.0);
((GeneralPath)shape).lineTo(5.644, 40.307);
((GeneralPath)shape).lineTo(4.47, 40.307);
((GeneralPath)shape).lineTo(4.47, 36.064);
((GeneralPath)shape).lineTo(5.057, 36.064);
((GeneralPath)shape).lineTo(5.057, 32.183);
((GeneralPath)shape).lineTo(6.636, 32.183);
((GeneralPath)shape).lineTo(6.636, 23.595);
((GeneralPath)shape).lineTo(7.54, 23.595);
((GeneralPath)shape).lineTo(7.54, 20.357);
((GeneralPath)shape).lineTo(8.442, 20.357);
((GeneralPath)shape).lineTo(8.442, 16.295);
((GeneralPath)shape).lineTo(9.435, 16.295);
((GeneralPath)shape).lineTo(9.435, 15.122);
((GeneralPath)shape).lineTo(8.351, 15.122);
((GeneralPath)shape).lineTo(8.262, 6.456);
((GeneralPath)shape).lineTo(9.435, 6.456);
((GeneralPath)shape).lineTo(9.435, 1.219);
((GeneralPath)shape).lineTo(12.955, 1.219);
((GeneralPath)shape).lineTo(17.108, 6.456);
((GeneralPath)shape).lineTo(28.301, 6.545);
((GeneralPath)shape).lineTo(33.897, 0.317);
((GeneralPath)shape).lineTo(36.336, 0.317);
((GeneralPath)shape).lineTo(36.336, 1.219);
((GeneralPath)shape).lineTo(37.148, 1.31);
((GeneralPath)shape).lineTo(37.148, 18.19);
((GeneralPath)shape).lineTo(38.321, 18.19);
((GeneralPath)shape).lineTo(38.321, 22.705);
((GeneralPath)shape).lineTo(39.405, 22.614);
((GeneralPath)shape).lineTo(39.405, 31.911);
((GeneralPath)shape).lineTo(41.21, 32.002);
((GeneralPath)shape).lineTo(41.21, 38.411);
((GeneralPath)shape).lineTo(42.111, 38.32);
((GeneralPath)shape).lineTo(42.111, 39.584);
((GeneralPath)shape).lineTo(43.016, 39.584);
((GeneralPath)shape).lineTo(43.016, 41.751);
((GeneralPath)shape).lineTo(44.278, 41.841);
((GeneralPath)shape).lineTo(44.278, 43.827);
((GeneralPath)shape).lineTo(45.362, 43.827);
((GeneralPath)shape).lineTo(45.362, 45.903);
((GeneralPath)shape).lineTo(46.627, 45.813);
((GeneralPath)shape).lineTo(46.627, 48.973);
((GeneralPath)shape).lineTo(47.529, 49.063);
((GeneralPath)shape).lineTo(47.529, 50.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
paint = new Color(255, 255, 255, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(9.073, 50.0);
((GeneralPath)shape).lineTo(6.365, 46.626);
((GeneralPath)shape).lineTo(6.365, 43.827);
((GeneralPath)shape).lineTo(8.622, 42.112);
((GeneralPath)shape).lineTo(9.886, 42.112);
((GeneralPath)shape).lineTo(14.399, 47.889);
((GeneralPath)shape).lineTo(16.386, 48.702);
((GeneralPath)shape).lineTo(24.329, 48.702);
((GeneralPath)shape).lineTo(30.739, 46.057);
((GeneralPath)shape).lineTo(34.169, 42.112);
((GeneralPath)shape).lineTo(36.064, 42.112);
((GeneralPath)shape).lineTo(37.238, 43.467);
((GeneralPath)shape).lineTo(37.238, 45.543);
((GeneralPath)shape).lineTo(33.537, 50.0);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_2 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
paint = new Color(255, 255, 255, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(16.747, 20.177);
((GeneralPath)shape).lineTo(10.518, 20.177);
((GeneralPath)shape).curveTo(10.518, 20.177, 8.297, 24.028, 10.518, 29.925999);
((GeneralPath)shape).curveTo(10.518, 29.925999, 14.49, 44.368, 21.800999, 44.821);
((GeneralPath)shape).curveTo(29.112999, 45.272, 34.981, 32.5, 34.981, 32.5);
((GeneralPath)shape).curveTo(37.96, 25.684, 34.981, 20.178001, 34.981, 20.178001);
((GeneralPath)shape).lineTo(30.828, 20.178001);
((GeneralPath)shape).lineTo(30.828, 18.102001);
((GeneralPath)shape).lineTo(28.752998, 18.102001);
((GeneralPath)shape).lineTo(28.661, 14.942001);
((GeneralPath)shape).lineTo(25.682999, 14.942001);
((GeneralPath)shape).lineTo(19.183998, 14.942001);
((GeneralPath)shape).curveTo(18.641998, 17.651001, 16.927998, 18.011002, 16.927998, 18.011002);
((GeneralPath)shape).lineTo(16.747, 20.177);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3
paint = new Color(201, 201, 200, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.031, 37.947);
((GeneralPath)shape).curveTo(25.265, 38.633, 24.388, 38.977997, 23.427, 38.919);
((GeneralPath)shape).curveTo(23.421, 38.807, 23.371, 38.689, 23.259, 38.579);
((GeneralPath)shape).curveTo(22.293001, 37.642998, 21.104, 37.115997, 19.866001, 36.639);
((GeneralPath)shape).curveTo(19.650002, 36.555, 19.476002, 36.618, 19.365002, 36.746);
((GeneralPath)shape).curveTo(18.875002, 36.583, 18.375002, 37.218, 18.810001, 37.609997);
((GeneralPath)shape).curveTo(19.906002, 38.6, 21.141, 39.494995, 22.591002, 39.862995);
((GeneralPath)shape).curveTo(24.139002, 40.257996, 25.615002, 39.733994, 26.779001, 38.693996);
((GeneralPath)shape).curveTo(27.288, 38.239, 26.538, 37.495, 26.031, 37.947);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.017, 20.177);
((GeneralPath)shape).curveTo(17.017, 20.177, 18.191, 22.794, 21.26, 22.614);
((GeneralPath)shape).curveTo(24.329, 22.434, 16.476, 18.01, 17.017, 20.177);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_5
paint = new Color(0, 0, 0, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(30.5, 20.177);
((GeneralPath)shape).curveTo(30.5, 20.177, 29.326, 22.794, 26.257, 22.614);
((GeneralPath)shape).curveTo(23.188, 22.434, 31.041, 18.01, 30.5, 20.177);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_5);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_6 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_6
paint = new Color(1, 1, 1, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(21.974, 14.94);
((GeneralPath)shape).lineTo(21.974, 17.828999);
((GeneralPath)shape).lineTo(23.156, 17.828999);
((GeneralPath)shape).lineTo(23.156, 22.621998);
((GeneralPath)shape).curveTo(23.156, 22.621998, 22.968, 25.681997, 22.565, 26.854998);
((GeneralPath)shape).curveTo(22.164001, 28.028997, 21.08, 32.812996, 21.441, 33.354996);
((GeneralPath)shape).curveTo(21.801, 33.896996, 25.593, 35.520996, 26.316, 33.354996);
((GeneralPath)shape).curveTo(27.039, 31.187996, 26.316, 30.285995, 26.316, 30.285995);
((GeneralPath)shape).curveTo(26.316, 30.285995, 25.312, 24.255995, 25.543, 22.621996);
((GeneralPath)shape).curveTo(25.774, 20.986996, 23.958, 18.550995, 25.543, 16.835997);
((GeneralPath)shape).lineTo(25.543, 14.94);
((GeneralPath)shape).lineTo(21.974, 14.94);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_6);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_7
paint = new Color(1, 1, 1, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.405, 37.599);
((GeneralPath)shape).lineTo(21.53, 37.599);
((GeneralPath)shape).curveTo(21.53, 37.599, 23.335001, 40.939, 26.405, 37.96);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_7);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_8
paint = new Color(201, 201, 200, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.414, 5.643);
((GeneralPath)shape).lineTo(15.482, 8.531);
((GeneralPath)shape).lineTo(15.482, 6.185);
((GeneralPath)shape).lineTo(13.046, 3.837);
((GeneralPath)shape).lineTo(11.24, 3.837);
((GeneralPath)shape).lineTo(10.518, 5.552);
((GeneralPath)shape).lineTo(9.977, 6.636);
((GeneralPath)shape).lineTo(9.254, 11.962);
((GeneralPath)shape).lineTo(10.067, 13.587);
((GeneralPath)shape).lineTo(11.601, 13.587);
((GeneralPath)shape).lineTo(11.872, 9.886);
((GeneralPath)shape).lineTo(10.698, 9.705);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_8);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_9
paint = new Color(201, 201, 200, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(34.114, 4.379);
((GeneralPath)shape).lineTo(31.045, 7.268);
((GeneralPath)shape).lineTo(31.045, 4.921);
((GeneralPath)shape).lineTo(33.483, 2.574);
((GeneralPath)shape).lineTo(35.288, 2.574);
((GeneralPath)shape).lineTo(36.154, 3.837);
((GeneralPath)shape).lineTo(37.273, 5.914);
((GeneralPath)shape).lineTo(37.273, 13.587);
((GeneralPath)shape).lineTo(36.426, 13.587);
((GeneralPath)shape).lineTo(34.892, 13.587);
((GeneralPath)shape).lineTo(34.656, 8.622);
((GeneralPath)shape).lineTo(35.829, 8.441);
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_9);
g.setComposite(AlphaComposite.getInstance(3, 0.26f * origAlpha));
AffineTransform defaultTransform__0_10 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_10
paint = new Color(201, 201, 200, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.963, 20.638);
((GeneralPath)shape).curveTo(19.932999, 19.953001, 18.752998, 19.492, 17.609, 19.035);
((GeneralPath)shape).curveTo(17.234, 18.885, 16.984999, 19.183, 16.974998, 19.503);
((GeneralPath)shape).curveTo(16.713, 19.566, 16.495998, 19.802, 16.598999, 20.136);
((GeneralPath)shape).curveTo(16.915998, 21.17, 17.940998, 22.088999, 18.765999, 22.727);
((GeneralPath)shape).curveTo(19.622, 23.39, 20.918, 24.119999, 21.887, 23.251999);
((GeneralPath)shape).curveTo(22.948, 22.302, 21.784, 21.185, 20.963, 20.638);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(21.193, 22.449);
((GeneralPath)shape).curveTo(20.743, 23.005, 19.602001, 22.060999, 19.251001, 21.772999);
((GeneralPath)shape).curveTo(18.783, 21.387999, 18.134, 20.807999, 17.796001, 20.242998);
((GeneralPath)shape).curveTo(18.459002, 20.515999, 19.113, 20.806997, 19.746002, 21.145998);
((GeneralPath)shape).curveTo(20.173002, 21.374998, 20.600002, 21.626999, 20.959002, 21.954998);
((GeneralPath)shape).curveTo(21.053001, 22.040998, 21.184002, 22.162998, 21.230001, 22.276999);
((GeneralPath)shape).curveTo(21.265, 22.364, 21.276, 22.348, 21.193, 22.449);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_10);
g.setComposite(AlphaComposite.getInstance(3, 0.26f * origAlpha));
AffineTransform defaultTransform__0_11 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_11
paint = new Color(201, 201, 200, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(31.155, 19.682);
((GeneralPath)shape).curveTo(31.131, 19.546, 31.058, 19.422998, 30.914001, 19.359999);
((GeneralPath)shape).curveTo(29.891, 18.911999, 28.497002, 19.586998, 27.582, 20.032);
((GeneralPath)shape).curveTo(26.933, 20.345, 25.908, 20.907, 25.552, 21.666);
((GeneralPath)shape).curveTo(25.471, 21.774, 25.427, 21.911001, 25.436, 22.044);
((GeneralPath)shape).curveTo(25.415, 22.193, 25.42, 22.35, 25.460001, 22.511002);
((GeneralPath)shape).curveTo(25.596, 23.056002, 26.182001, 23.466002, 26.711, 23.544003);
((GeneralPath)shape).curveTo(27.373001, 23.643003, 28.01, 23.406002, 28.593, 23.110003);
((GeneralPath)shape).curveTo(29.65, 22.571003, 30.967001, 21.561003, 31.466, 20.443003);
((GeneralPath)shape).curveTo(31.615, 20.108, 31.415, 19.797, 31.155, 19.682);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(28.116, 20.943);
((GeneralPath)shape).curveTo(28.574, 20.714, 29.057, 20.535, 29.550999, 20.399);
((GeneralPath)shape).curveTo(29.584, 20.389, 29.617998, 20.381, 29.651999, 20.373001);
((GeneralPath)shape).curveTo(28.953999, 21.172, 27.824, 21.86, 26.849998, 21.740002);
((GeneralPath)shape).curveTo(26.854998, 21.736002, 26.859999, 21.731, 26.863998, 21.728003);
((GeneralPath)shape).curveTo(27.227, 21.398, 27.681, 21.16, 28.116, 20.943);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_11);
g.setComposite(AlphaComposite.getInstance(3, 0.26f * origAlpha));
AffineTransform defaultTransform__0_12 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_12
paint = new Color(201, 201, 200, 255);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.554, 28.16);
((GeneralPath)shape).curveTo(26.345001, 27.514, 25.326, 27.791, 25.535, 28.441);
((GeneralPath)shape).curveTo(25.802, 29.265, 25.937, 30.129, 25.987, 30.994);
((GeneralPath)shape).curveTo(26.029, 31.708, 26.078, 32.737, 25.605, 33.338);
((GeneralPath)shape).curveTo(25.137, 33.931, 24.175, 33.93, 23.499, 33.837);
((GeneralPath)shape).curveTo(22.759, 33.735, 21.935001, 33.483, 21.363, 32.983);
((GeneralPath)shape).curveTo(20.854, 32.536003, 20.104, 33.280003, 20.617, 33.729);
((GeneralPath)shape).curveTo(22.001001, 34.945, 24.697, 35.507, 26.205, 34.236);
((GeneralPath)shape).curveTo(26.977, 33.585, 27.056, 32.351, 27.056, 31.423);
((GeneralPath)shape).curveTo(27.057, 30.334, 26.889, 29.196, 26.554, 28.16);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_12);
g.setTransform(defaultTransform__0);
g.setTransform(defaultTransform_);

	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 5;
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
		return 44;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static int getOrigHeight() {
		return 50;
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
	public analyze_woflan() {
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

