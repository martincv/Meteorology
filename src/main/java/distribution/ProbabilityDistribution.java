package distribution;

import java.util.ArrayList;
import java.util.List;


public class ProbabilityDistribution {

	public List<List<Double>> getAllPossiblePoints(Double minLat, Double maxLat, Double minLong, Double maxLong, Integer numberOfPossiblePoints){
		double a,b;
		double len1 = Math.abs(minLat - maxLat);
		double len2 = Math.abs(minLong - maxLong);
		
		b = Math.sqrt(numberOfPossiblePoints * len2 / len1);
		a = numberOfPossiblePoints / b;
		
		System.out.println("a="+ a+",b="+b);
		double dx1 = len1 / a;
		double dx2 = len2 / b;
		
		List<List<Double>> possiblePoints = new ArrayList<List<Double>>(2);
		possiblePoints.add(new ArrayList<Double>());
		possiblePoints.add(new ArrayList<Double>());
		for (double lat = minLat; lat <= maxLat; lat += dx1){
			for (double lon = minLong; lon <= maxLong; lon += dx2){
				possiblePoints.get(0).add(lat);
				possiblePoints.get(1).add(lon);
			}
		}
		
		return possiblePoints;
	}
	
}
