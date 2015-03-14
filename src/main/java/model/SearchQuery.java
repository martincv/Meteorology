package model;

import java.util.List;

public class SearchQuery {

	private List<GeoPoint> areaPoints;
	private List<MeteoPoint> queriedPoints;
	private Long timeFrom;
	private Long timeTo;
	
	public List<MeteoPoint> getQueriedPoints() {
		return queriedPoints;
	}
	public void setQueriedPoints(List<MeteoPoint> queriedPoints) {
		this.queriedPoints = queriedPoints;
	}

	public List<GeoPoint> getAreaPoints() {
		return areaPoints;
	}
	public void setAreaPoints(List<GeoPoint> areaPoints) {
		this.areaPoints = areaPoints;
	}
	public Long getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(Long timeFrom) {
		this.timeFrom = timeFrom;
	}
	public Long getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(Long timeTo) {
		this.timeTo = timeTo;
	}
	
	
}
