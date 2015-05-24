package model;

public class ConvexHullAndTime {

	private double minTime;
	private double maxTime;
	private double[][] convexHull;
	
	public ConvexHullAndTime(double[][] convexHull,double minTime, double maxTime) {
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.convexHull = convexHull;
	}

	public double getMinTime() {
		return minTime;
	}

	public double getMaxTime() {
		return maxTime;
	}

	public double[][] getConvexHull() {
		return convexHull;
	}
	
}
