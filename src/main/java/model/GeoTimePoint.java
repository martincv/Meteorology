package model;

public class GeoTimePoint extends GeoPoint {

	private Long time;
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long d) {
		this.time = d;
	}
	
	public GeoTimePoint(){}

	public GeoTimePoint(Double latitude, Double longitude, Long time) {
		super(latitude, longitude);
		this.time = time;
	}

}
