package org.woped.quantana.graph;


public class Arc {
	public static final int COLOR_BLACK	= 1;
	public static final int COLOR_RED	= 2;
	
	Node target;
	Node source;
	double probability = 1.0;
	double multiply = 1.0;
	int color = COLOR_BLACK;
	
	public Arc(Node n){
		target = n;
	}
	
	public Arc(Node n, double p){
		target = n;
		probability = p;
		multiply = p;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double prob) {
		this.probability = prob;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public double getMultiply() {
		return multiply;
	}

	public void setMultiply(double multiply) {
		this.multiply = multiply;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}
}
