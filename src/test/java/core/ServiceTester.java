package core;

import java.util.ArrayList;
import java.util.List;

import model.GeoTimePoint;
import model.MeteoPoint;

public class ServiceTester {

	public static void main(String[] args) {
		MeteoService meteo = new MeteoService(new double[] {1.0,2.0}, "2015-09-03T11:44:00+02", 
				"2014-09-03T20:18:00+02");
		long t1 = meteo.getTimeFrom();
		long t2 = meteo.getTimeTo();
				
		List<GeoTimePoint> list = new ArrayList<GeoTimePoint>();
		list.add(new GeoTimePoint(46.0514263, 14.505965500000002, t2));
		
		ForecastServiceHelper fsh = new ForecastServiceHelper();
		List<MeteoPoint> meteoPoints = fsh.getForecastForGeoTimePoints(list);
		for (MeteoPoint meteoPoint : meteoPoints) {
			System.out.println(meteoPoint.getLatitude());
			System.out.println(meteoPoint.getLongitude());
			System.out.println(meteoPoint.getTime());
			System.out.println(meteoPoint.getApparentTemperature());
			System.out.println(meteoPoint.getApparentTemperatureMax());
			System.out.println(meteoPoint.getApparentTemperatureMin());
			System.out.println(meteoPoint.getTemperature());
			System.out.println(meteoPoint.getTemperatureMin());
			System.out.println(meteoPoint.getTemperatureMax());
			System.out.println(meteoPoint.getPrecipIntensity());
			System.out.println(meteoPoint.getPrecipType());
			System.out.println(meteoPoint.getPrecipProbability());
			System.out.println(meteoPoint.getPrecipIntensity());
			System.out.println(meteoPoint.getPrecipIntensityMax());
			System.out.println(meteoPoint.getHumidity());
			System.out.println(meteoPoint.getPressure());
			System.out.println(meteoPoint.getNearestStormDistance());
			System.out.println(meteoPoint.getPrecipAccumulation());
		}

	}

}
