package core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import model.GeoTimePoint;
import model.MeteoPoint;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODataPoint;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class ForecastServiceHelper {
	
	final private static String API_ID = "3b7da2be19497ca170e70829ffa73d1f";

	
	public static List<MeteoPoint> getForecastForGeoTimePoints(List<GeoTimePoint> geoPoints){

		List<MeteoPoint> meteoPoints = new ArrayList<MeteoPoint>();
		for (GeoTimePoint geoTimePoint : geoPoints) {
			MeteoPoint point = getDataForPoint(geoTimePoint);
			if (point != null && point.getTemperature() != -300.0) {
				meteoPoints.add(point);
			}
		}
		return meteoPoints;
	}

	private static MeteoPoint getDataForPoint(GeoTimePoint geoTimePoint) {
		ForecastIO fio;
		MeteoPoint meteoPoint = new MeteoPoint();
		final String time = Long.toString(geoTimePoint.getTime());
		final String latitude = Double.toString(geoTimePoint.getLatitude());
		final String longitude = Double.toString(geoTimePoint.getLongitude());
		
//		System.out.println(time + " " + latitude + " " + longitude);
	    
	    ExecutorService service = Executors.newSingleThreadExecutor();
	    Future<ForecastIO> future = service.submit(new Callable<ForecastIO>() {
	        public ForecastIO call() throws Exception {
	        	ForecastIO fio = new ForecastIO(API_ID);
	    		fio.setUnits(ForecastIO.UNITS_SI);
	    	    fio.setLang(ForecastIO.LANG_ENGLISH);
	    	    fio.setTime(time);
	            boolean success = fio.getForecast(latitude, longitude);
	            if (success) {
	            	return fio;
	            } else {
	            	return null;
	            }
	            
	        }
	    });

	    try {
	        fio = future.get(1, TimeUnit.SECONDS);
	    }
	    catch(Exception e) {
	    	System.out.println("nesto");
	    	e.printStackTrace();
	        return null;
	    }
        
	    FIOCurrently currently = new FIOCurrently(fio);
	    meteoPoint = getDataForPoint(currently, fio);
	    return meteoPoint;
	}

	private static MeteoPoint getDataForPoint(FIOCurrently currently, ForecastIO fio) {
		MeteoPoint point = new MeteoPoint();
		FIODataPoint dataPoint = currently.get();
		
		point.setLatitude(fio.getLatitude());
		point.setLongitude(fio.getLongitude());
		point.setTime(Long.parseLong(fio.getTime()));
		point.setHumidity(dataPoint.humidity());
		point.setPressure(dataPoint.pressure());
		point.setWindSpeed(dataPoint.windSpeed());
		point.setPrecipAccumulation(dataPoint.precipAccumulation());
		point.setPrecipType(dataPoint.precipType());
		point.setPrecipIntensity(dataPoint.precipIntensity());
		point.setNearestStormDistance(dataPoint.nearestStormDistance());
		point.setPrecipProbability(dataPoint.precipProbability());
		if(dataPoint.temperature() == null) {
			point.setTemperature(-300.0);
		} else {
			point.setTemperature(dataPoint.temperature());
		}
//		System.out.println(dataPoint.temperature());
		return point;
	}
	
}
