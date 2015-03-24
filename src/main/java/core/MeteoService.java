package core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	 */
	private List<GeoTimePoint> getRandomPointsWithinArea() {
		List<GeoTimePoint> geoTimePoints = new ArrayList<GeoTimePoint>();
		Double[] minMaxBorders = getMaxMinBorders();
		System.out.println(minMaxBorders[0] + " "+ minMaxBorders[1] + " " + minMaxBorders[2] + " " + minMaxBorders[3]);
		Random rd = new Random();
		for (int i = 0; i < 10;) {
			double randomLatitude = minMaxBorders[0] + (minMaxBorders[1] - minMaxBorders[0]) * rd.nextDouble();
			double randomLongitude = minMaxBorders[2] + (minMaxBorders[3] - minMaxBorders[2]) * rd.nextDouble();
			GeoPoint geoPoint = new GeoPoint(randomLatitude, randomLongitude);
			if(isLocationInsideArea(geoPoint)) {
				i++;
				long time = timeFrom + (long)(rd.nextDouble()*(timeTo-timeFrom));
				System.out.println("rg:" + randomLatitude + " " + randomLongitude + " " +time);
				geoTimePoints.add(new GeoTimePoint(randomLatitude, randomLongitude, time));
			} 
		}
		return geoTimePoints;
	}
	
	private Double[] getMaxMinBorders() {
		Double[] minMaxBorders = new Double[4];
		minMaxBorders[0] = areaPoints.get(0).getLatitude();
		minMaxBorders[1] = areaPoints.get(0).getLatitude();
		minMaxBorders[2] = areaPoints.get(0).getLongitude();
		minMaxBorders[3] = areaPoints.get(0).getLongitude();
		for (GeoPoint geoPoint : areaPoints) {
			if (geoPoint.getLatitude() < minMaxBorders[0]) {
				minMaxBorders[0] = geoPoint.getLatitude();
			}
			if (geoPoint.getLatitude() > minMaxBorders[1]) {
				minMaxBorders[1] = geoPoint.getLatitude();
			}
			if (geoPoint.getLongitude() < minMaxBorders[2]) {
				minMaxBorders[2] = geoPoint.getLongitude();
			}
			if (geoPoint.getLongitude() > minMaxBorders[3]) {
				minMaxBorders[3] = geoPoint.getLongitude();
			}
		}
		return minMaxBorders;
	}
	
	
    /**
     * Returns true if a given location (LatLon) is located inside a given polygon
     *
     * @param point the location
     * @param positions the list of positions describing the polygon. Last one should be the same as the first one.
     * @return true if the location is inside the polygon.
     */
    private boolean isLocationInsideArea(GeoPoint point)
    {

        boolean result = false;
        GeoPoint p1 = areaPoints.get(0);
        for (int i = 1; i < areaPoints.size(); i++) {
            GeoPoint p2 = areaPoints.get(i);
            if ( ((p2.getLatitude() <= point.getLatitude()
                    && point.getLatitude() < p1.getLatitude()) ||
                    (p1.getLatitude() <= point.getLatitude()
                            && point.getLatitude() < p2.getLatitude()))
                    && (point.getLongitude() < (p1.getLongitude() - p2.getLongitude())
                    * (point.getLatitude() - p2.getLatitude())
                    / (p1.getLatitude() - p2.getLatitude()) + p2.getLongitude()) )
                result = !result;

            p1 = p2;
        }
        return result;
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