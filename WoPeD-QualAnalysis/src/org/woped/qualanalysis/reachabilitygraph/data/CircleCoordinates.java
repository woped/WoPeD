package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Point;
import java.util.LinkedList;

public class CircleCoordinates {
	public static LinkedList<Point> getCircleCoordinates(int width, int height, int objectcount){
		LinkedList<Point> points = new LinkedList<Point>();
		for(int i = 0; i < objectcount; i++){
			points.add(new Point(5*i,5*i));
		}
		return points;
	}
}
