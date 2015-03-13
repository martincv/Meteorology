package core;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import model.GeoTimePoint;
import model.MeteoPoint;

import com.github.dvdme.ForecastIOLib.FIODaily;
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
			meteoPoints.addAll(getDataForPoint(geoTimePoint));
		}
		return meteoPoints;
	}

	private List<MeteoPoint> getDataForPoint(GeoTimePoint geoTimePoint) {
		List<MeteoPoint> meteoPoints = new ArrayList<MeteoPoint>();
		
		String time = Long.toString(geoTimePoint.getTime());
		String latitude = Double.toString(geoTimePoint.getLatitude());
		String longitude = Double.toString(geoTimePoint.getLongitude());
		
	    fio.setTime(time);
        System.out.println(time + " " + latitude + " " + longitude);
	    fio.getForecast(latitude, longitude);

	    FIODaily dailyPredictions = new FIODaily(fio);
	    if(dailyPredictions.days() == -1) {
	    	meteoPoints = null;
	    } else {
	    	meteoPoints = getPointForEachDay(dailyPredictions);
	    }
	    return meteoPoints;
	}

	private List<MeteoPoint> getPointForEachDay(FIODaily dailyPredictions) {
		List<MeteoPoint> meteoPoints = new ArrayList<MeteoPoint>();
		int numOfDays = dailyPredictions.days();
		
		for (int i = 0; i < numOfDays; i++) {
			MeteoPoint point = new MeteoPoint();
			FIODataPoint dataPoint = dailyPredictions.getDay(i);
			point.setLatitude(fio.getLatitude());
			point.setLongitude(fio.getLongitude());
			point.setTime(getSecondsPassedSince1970(fio.getTime()));
			point.setHumidity(dataPoint.humidity());
			point.setPressure(dataPoint.pressure());
			point.setWindSpeed(dataPoint.windSpeed());
			point.setApparentTemperature(dataPoint.apparentTemperature());
			point.setApparentTemperatureMin(dataPoint.apparentTemperatureMin());
			point.setApparentTemperatureMax(dataPoint.apparentTemperatureMax());
			point.setPrecipAccumulation(dataPoint.precipAccumulation());
			point.setPrecipType(dataPoint.precipType());
			point.setPrecipIntensity(dataPoint.precipIntensity());
			point.setPrecipIntensityMax(dataPoint.precipIntensityMax());
			point.setNearestStormDistance(dataPoint.nearestStormDistance());
			point.setPrecipProbability(dataPoint.precipProbability());
			point.setTemperature(dataPoint.temperature());
			point.setTemperatureMax(dataPoint.temperatureMax());
			point.setTemperatureMin(dataPoint.temperatureMin());
			meteoPoints.add(point);
		}
		
		return meteoPoints;
	}
	
	private long getSecondsPassedSince1970(String time) {
		DateTime date = ISODateTimeFormat.dateTimeParser().parseDateTime(time);
		return date.getMillis() / 1000;
	}
}
