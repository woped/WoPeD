package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Point;
import java.util.LinkedList;

public class CircleCoordinates {
	public static LinkedList<Point> getCircleCoordinates(int width, int height, int objectcount) {
		LinkedList<Point> points = new LinkedList<Point>();
		// get the Middle of the graph and do some correction to fit correctly
		int mwidth = (width / 2) - 60;
		int mheight = (height / 2) - 45;

		// get the steps in degrees
		int degreestep = 360 / objectcount;
		
		//Fill the linked list with coordinates for every object
		for(int i = 0; i < objectcount; i++) {
			int x = (int) (mwidth + (mwidth * (Math.sin(Math.toRadians(i * degreestep)))) + 5);
			int y = (int) (mheight - (mheight * (Math.cos(Math.toRadians(i * degreestep)))) + 5);
			points.add(new Point(x, y));
		}
		return points;
	}
}
