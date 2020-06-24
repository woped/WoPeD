package org.woped.qualanalysis.coverabilitygraph.gui.layout.circular;

import java.awt.Point;
import java.util.LinkedList;
/**
 * Class for getting the Coordinates for a number of objects on a circle
 *
 */
class CircleCoordinates {
	/**
	 * Method to get the Coordinates on a circle with a certain width and height for a number of objects
	 * @param width of the graph window
	 * @param height of the graph windows
	 * @param count of the objects to be displayed in the window
	 * @return LinkedList containing a corner point for each object
	 */
	@SuppressWarnings("SpellCheckingInspection")
	static LinkedList<Point> getCircleCoordinates(int width, int height, int count) {
		LinkedList<Point> points = new LinkedList<>();
		// get the Middle of the graph and do some correction to fit correctly
		int mwidth = (width / 2) - 60;
		int mheight = (height / 2) - 45;

		// get the steps in degrees
		double degreestep = 0;
		if(count > 0){
			degreestep = 360 / count;
		}
		
		//Fill the linked list with coordinates for every object
		for(int i = 0; i < count; i++) {
			int x = (int) (mwidth + (mwidth * (Math.sin(Math.toRadians(i * degreestep)))) + 5);
			int y = (int) (mheight - (mheight * (Math.cos(Math.toRadians(i * degreestep)))) + 5);
			points.add(new Point(x, y));
		}
		return points;
	}
}
