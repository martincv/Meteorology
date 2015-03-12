package core;

import com.github.dvdme.ForecastIOLib.ForecastIO;

public class ServiceCaller {

	public static void main(String args[ ]) throws Exception {
		
		MeteoService meteo = new MeteoService(new double[] {1.0,2.0}, "2015-09-03T11:44:00+02", 
				"2014-09-03T20:18:00+02");
		long t1 = meteo.getTimeFrom();
		long t2 = meteo.getTimeTo();
		
		ForecastIO fio = new ForecastIO("3b7da2be19497ca170e70829ffa73d1f");
	    fio.setUnits(ForecastIO.UNITS_SI);
	    fio.setLang(ForecastIO.LANG_ENGLISH);
	    fio.setTime(Long.toString(t2));
	    fio.getForecast("46.0514263", "14.505965500000002");
	    System.out.println("Latitude: "+fio.getLatitude());
	    System.out.println("Longitude: "+fio.getLongitude());
	    System.out.println("Timezone: "+fio.getTimezone());
	    System.out.println("Offset: "+fio.offset());
	    //just a test
//	    FIODaily daily = new FIODaily(fio);
//	    String [] f  = daily.days().getFieldsArray();
//	    FIOCurrently currently = new FIOCurrently(fio);
//	    for(int i = 0; i<f.length;i++)
//	        System.out.println(f[i]+": "+currently.get().getByKey(f[i]));
//	    
//	    //Print currently data
//	    System.out.println("\nCurrently\n");
//	    String [] f  = currently.get().getFieldsArray();
//	    FIODataPoint dataPoint = currently.get();
//	    System.out.println("tmax:" + dataPoint.temperatureMax());
//	    System.out.println("precipAcc:" + dataPoint.precipAccumulation());
//	    System.out.println(dataPoint.precipType());
//	    System.out.println(dataPoint.precipIntensity());
//	    System.out.println(dataPoint.precipProbability());
//	    System.out.println(dataPoint.temperature());
//	    
//	    for(int i = 0; i<f.length;i++)
//	        System.out.println(f[i]+": "+currently.get().getByKey(f[i]));
   }

}
