package org.woped.core.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class IntPair implements Serializable {

	private int x1, x2;

	public IntPair(Dimension d) {
		this.x1 = d.width;
		this.x2 = d.height;
	}

	public IntPair(Point p) {
		this.x1 = p.x;
		this.x2 = p.y;
	}

	public IntPair(int x1, int x2) {
		this.x1 = x1;
		this.x2 = x2;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

}
