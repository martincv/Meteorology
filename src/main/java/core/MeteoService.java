package core;

import java.util.ArrayList;
import java.util.List;

import model.GeoPoint;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class MeteoService {
	
	private List<GeoPoint> areaPoints = new ArrayList<GeoPoint>();
	private long timeFrom;
	private long timeTo;

	
	public MeteoService(double[] areaPoints, String timeFrom, String timeTo) {
		this.areaPoints = extractAreaPoints(areaPoints);
		this.timeFrom = getSecondsPassedSince1970(timeFrom);
		this.timeTo = getSecondsPassedSince1970(timeTo);
	}
	
	public String findMostExtremePoints(){
		return "Not implemented yet";
	}

	private long getSecondsPassedSince1970(String time) {
		DateTime date = ISODateTimeFormat.dateTimeParser().parseDateTime(time);
		return date.getMillis() / 1000;
	}

	private List<GeoPoint> extractAreaPoints(double[] points) {
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		for (int i = 0; i < points.length; i += 2) {
			geoPoints.add(new GeoPoint(points[i], points[i+1]));
		}
		return geoPoints;
	}

	public List<GeoPoint> getAreaPoints() {
		return areaPoints;
	}

	public long getTimeFrom() {
		return timeFrom;
	}

	public long getTimeTo() {
		return timeTo;
	}
	
}