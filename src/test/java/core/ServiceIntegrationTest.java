package core;

import java.sql.SQLException;

public class ServiceIntegrationTest {

	public static void main(String[] args) throws Exception {
		double[] areaPoints = getAreaPoints();
		String timeFrom = "2014-01-01T00:00:00+02";
		String timeTo = "2015-12-31T23:59:59+02";
		
		MeteoService service = new MeteoService(areaPoints, timeFrom, timeTo);
		System.out.println(service.findMostExtremePoints());

	}

	private static double[] getAreaPoints() {
//		double[] areaPoints = {48.379457, -4.521131, 51.063354, 2.466174, 48.959877, 8.135119, 46.552664, 6.047717
//								,43.891399, 7.453967,42.481656, 3.059436, 43.430422, -1.774549};
		
		double[] areaPoints = {46.565148, 13.501787,46.693404, 16.369219,45.520454, 16.446123,45.497356, 13.567705};

		return areaPoints;
	}

}
