package model;

import java.util.List;

public class SearchQuery {

	private List<GeoPoint> areaPoints;
	private double timeFrom;
	private double timeTo;
	public List<GeoPoint> getAreaPoints() {
		return areaPoints;
	}
	public void setAreaPoints(List<GeoPoint> areaPoints) {
		this.areaPoints = areaPoints;
	}
	public double getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(double timeFrom) {
		this.timeFrom = timeFrom;
	}
	public double getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(double timeTo) {
		this.timeTo = timeTo;
	}
	
	
}
