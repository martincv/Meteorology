package core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.GeoPoint;
import model.GeoTimePoint;
import model.MeteoPoint;
import model.SearchQuery;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import dao.SearchQueryDAO;

public class MeteoService {
	
	private List<GeoPoint> areaPoints = new ArrayList<GeoPoint>();
	private long timeFrom;
	private long timeTo;

	
	public MeteoService(double[] areaPoints, String timeFrom, String timeTo) {
		this.areaPoints = extractAreaPoints(areaPoints);
		this.timeFrom = getSecondsPassedSince1970(timeFrom);
		this.timeTo = getSecondsPassedSince1970(timeTo);
	}
	
	public String findMostExtremePoints() throws SQLException{
		List<GeoTimePoint> geoTimePoints = getRandomPointsWithinArea();
		List<MeteoPoint> meteoPoints = ForecastServiceHelper.getForecastForGeoTimePoints(geoTimePoints);
		List<MeteoPoint> previousPoints = findPreviousPointsForSameRegionAndTime();
		saveQueryToDatabase(meteoPoints);
		meteoPoints.addAll(previousPoints);
		locateExtremePoints(meteoPoints);
		return "Not implemented yet";
	}
	
	/**
	 * At the moment just return some fixed points.
	 * This is where your code goes Bojana by using areaPoints, timeFrom and timeTo.
	 */
	private List<GeoTimePoint> getRandomPointsWithinArea() {
		List<GeoTimePoint> geoTimePoints = new ArrayList<GeoTimePoint>();
		geoTimePoints.add(new GeoTimePoint(48.887690, 2.378283, getSecondsPassedSince1970("2014-01-01T01:10:00+02")));
		geoTimePoints.add(new GeoTimePoint(47.273115, -1.554822, getSecondsPassedSince1970("2014-04-04T03:20:00+02")));
		geoTimePoints.add(new GeoTimePoint(44.833900, -0.544080, getSecondsPassedSince1970("2014-07-07T05:30:00+02")));
		geoTimePoints.add(new GeoTimePoint(45.776564, 4.795276, getSecondsPassedSince1970("2014-10-10T07:40:00+02")));
		geoTimePoints.add(new GeoTimePoint(48.685011, 6.179553, getSecondsPassedSince1970("2015-01-13T09:50:00+02")));
		geoTimePoints.add(new GeoTimePoint(49.118321, 6.179553, getSecondsPassedSince1970("2015-02-16T11:22:00+02")));
		geoTimePoints.add(new GeoTimePoint(48.130752, -1.664685, getSecondsPassedSince1970("2015-03-19T13:11:00+02")));
		return geoTimePoints;
	}
	
	private List<MeteoPoint> findPreviousPointsForSameRegionAndTime() {
		return new ArrayList<MeteoPoint>();
	}
	
	private void locateExtremePoints(List<MeteoPoint> meteoPoints) {
		// TODO Auto-generated method stub
		
	}

	private void saveQueryToDatabase(List<MeteoPoint> meteoPoints) throws SQLException {
		SearchQuery sq = new SearchQuery(areaPoints, meteoPoints, timeFrom, timeTo);
		SearchQueryDAO search = new SearchQueryDAO();
		search.insertNewQuery(sq);
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