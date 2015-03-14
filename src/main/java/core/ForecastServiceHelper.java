package core;

import java.util.ArrayList;
import java.util.List;

import model.GeoTimePoint;
import model.MeteoPoint;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODataPoint;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class ForecastServiceHelper {
	
	final private static String API_ID = "3b7da2be19497ca170e70829ffa73d1f";
	private ForecastIO fio;
	
	public ForecastServiceHelper() {
		fio = new ForecastIO(API_ID);
	    fio.setUnits(ForecastIO.UNITS_SI);
	    fio.setLang(ForecastIO.LANG_ENGLISH);
	}
	
	public List<MeteoPoint> getForecastForGeoTimePoints(List<GeoTimePoint> geoPoints){
		List<MeteoPoint> meteoPoints = new ArrayList<MeteoPoint>();
		for (GeoTimePoint geoTimePoint : geoPoints) {
			meteoPoints.add(getDataForPoint(geoTimePoint));
		}
		return meteoPoints;
	}

	private MeteoPoint getDataForPoint(GeoTimePoint geoTimePoint) {
		MeteoPoint meteoPoint = new MeteoPoint();
		
		String time = Long.toString(geoTimePoint.getTime());
		String latitude = Double.toString(geoTimePoint.getLatitude());
		String longitude = Double.toString(geoTimePoint.getLongitude());
		
	    fio.setTime(time);
        System.out.println(time + " " + latitude + " " + longitude);
	    fio.getForecast(latitude, longitude);

	    FIOCurrently currently = new FIOCurrently(fio);
	    meteoPoint = getDataForPoint(currently);

	    return meteoPoint;
	}

	private MeteoPoint getDataForPoint(FIOCurrently currently) {
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
		point.setTemperature(dataPoint.temperature());
		
		return point;
	}
	
}
