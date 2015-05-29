package distribution;

import java.util.ArrayList;
import java.util.List;


public class ProbabilityDistribution {

	public List<List<Double>> getAllPossiblePoints(Double minLat, Double maxLat, Double minLong, Double maxLong,
			Integer numberOfPossiblePoints, long timeFrom, long timeTo) {
		double numberOfTimePoints = 900.0;
		double a,b;
		double len1 = Math.abs(minLat - maxLat);
		double len2 = Math.abs(minLong - maxLong);

		b = Math.floor(Math.sqrt(numberOfPossiblePoints * len2 / len1));
		a = Math.floor(numberOfPossiblePoints / b);

		System.out.println("a="+ a+",b="+b);
		double dx1 = len1 / a;
		double dx2 = len2 / b;
		double dt = (timeTo - timeFrom) / numberOfTimePoints;
		
		List<List<Double>> possiblePoints = new ArrayList<List<Double>>(2);
		possiblePoints.add(new ArrayList<Double>());
		possiblePoints.add(new ArrayList<Double>());
		possiblePoints.add(new ArrayList<Double>());
		for (double lat = minLat; lat <= maxLat; lat += dx1){
			for (double lon = minLong; lon <= maxLong; lon += dx2){
				for (double time = timeFrom; time <= timeTo; time+=dt) {
					possiblePoints.get(0).add(lat);
					possiblePoints.get(1).add(lon);
					possiblePoints.get(2).add((double) Math.round(time));
//					System.out.println(lat + " " + lon + " " + Math.round(time));
				}
			}
		}

		return possiblePoints;
	}

}
