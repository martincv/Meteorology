package model;

public class GeoTimePoint extends GeoPoint {

	private long time;
	
	public long getTime() {
		return time;
	}

	public void setTime(long d) {
		this.time = d;
	}
	
	public GeoTimePoint(){}

	public GeoTimePoint(double latitude, double longitude, long time) {
		super(latitude, longitude);
		this.time = time;
	}

}
