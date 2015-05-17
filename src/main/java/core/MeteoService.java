package core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import model.GeoPoint;
import model.GeoTimePoint;
import model.MeteoPoint;
import model.SearchQuery;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.rosuda.JRI.Rengine;

import clustering.KMeansClustering;

import com.google.common.primitives.Doubles;

import comparator.MeteoPointTemperatureComparator;
import dao.SearchQueryDAO;
import distribution.ProbabilityDistribution;

public class MeteoService {
	
	private static final int NUMBER_OF_POSSIBLE_POINTS = 10000;
	private static final int NUMBER_OF_POINTS_TO_TEST = 100;
	private List<GeoPoint> areaPoints = new ArrayList<GeoPoint>();
	private long timeFrom;
	private long timeTo;
	private Rengine re;

	
	public MeteoService(double[] areaPoints, String timeFrom, String timeTo) {
		this.areaPoints = extractAreaPoints(areaPoints);
		this.timeFrom = getSecondsPassedSince1970(timeFrom);
		this.timeTo = getSecondsPassedSince1970(timeTo);
	}
	
	public List<List<MeteoPoint>> findMostExtremePoints() throws SQLException, Exception{
		//List<GeoTimePoint> geoTimePoints = getRandomPointsWithinArea();
		//List<MeteoPoint> meteoPoints = ForecastServiceHelper.getForecastForGeoTimePoints(geoTimePoints);
		List<MeteoPoint> meteoPoints = getPointsAccordingToProbabilityDistribution();
//		List<MeteoPoint> previousPoints = findPreviousPointsForSameRegionAndTime();
//		saveQueryToDatabase(meteoPoints);
//		meteoPoints = concatenate(meteoPoints, previousPoints);
		List<List<MeteoPoint>> clusters = locateExtremePoints(meteoPoints);
		List<List<MeteoPoint>> mostExtremeClusters = findExtremeClusters(clusters);
		List<ArrayList<GeoTimePoint>>hulls = runConvexHullAndCreateOpenGLGraphics(mostExtremeClusters);
		return mostExtremeClusters;
	}
	
	private List<ArrayList<GeoTimePoint>> runConvexHullAndCreateOpenGLGraphics(
			List<List<MeteoPoint>> mostExtremeClusters) throws Exception {
		System.out.println("ovdeka");
//		String[] Rargs = {"--vanilla"};
//		Rengine re = new Rengine(Rargs, false, null);	
//		if (!re.waitForR()) {
//			System.out.println("Cannot load R");
//			throw new Exception("Can not load R.");
//		}
		re.eval("library(grDevices)");
		re.eval("library(rgl)");
		re.eval("library(MASS)");
		
		List<MeteoPoint> minCluster = mostExtremeClusters.get(0);
		List<MeteoPoint> maxCluster = mostExtremeClusters.get(1);
		

		re.assign("iter", new int[] {minCluster.size()});
		re.eval("mat = matrix(NA, nrow=iter, ncol=3)");
		System.out.println("ovdeka");
		for (int i = 0; i < minCluster.size(); i++) {
		  re.assign("n", new int[] {i + 1});
		  re.assign("x1", new double[] {minCluster.get(i).getLongitude()});
		  re.assign("x2", new double[] {minCluster.get(i).getLatitude()});
		  re.assign("x3", new double[] {minCluster.get(i).getTime()});
		  re.eval("mat[n,] <- c(x1, x2, x3)");
//		  System.out.println(re.eval("mat[n,]"));
//		  System.out.println(minCluster.get(i).getLongitude() + " " + minCluster.get(i).getLatitude() + " " + minCluster.get(i).getTime());
		}
//		System.out.println(re.eval("mat"));
		
//		plot3d(x) # space and time
//		plot(x[,1], x[,2]) # just in spatial coordinates


		re.eval("plot(mat[,1], mat[,2])");
		re.eval("ch <- chull(mat[,c(1,2)])");
		re.eval("polygon(mat[ch,1], mat[ch,2])");
//		re.eval("dev.copy(jpeg,filename='plot.jpg');");
//		re.eval("dev.off();");

		re.eval("open3d()");
		System.out.println("trojka:" + re.eval("mat[,3]"));
		System.out.println("max=" + re.eval("max(mat[,3])"));
		System.out.println("min=" + re.eval("min(mat[,3])"));
		re.eval("diff = max(mat[,3]) - min(mat[,3])");
		System.out.println(re.eval("diff"));
//		re.eval("shade3d( extrude3d(mat[ch,1], mat[ch,2], thickness = max(mat[,3]) - min(mat[,3])), col = 'blue' ,alpha = 0.1)");
//		re.eval("shade3d( extrude3d(mat[ch,1], mat[ch,2], thickness = 3.0, col = 'blue' ,alpha = 0.1)");
		re.eval("time = ((mat[,3] - min(mat[,3])) * 3.0)/diff");
		re.eval("points3d(mat[,1], mat[,2], time");
		System.out.println("long=" + re.eval("mat[,1]"));
		System.out.println("lat=" + re.eval("mat[,2]"));
		System.out.println("time=" + re.eval("time"));
		re.eval("writeWebGL()");

		return null;
	}

	private List<List<MeteoPoint>> findExtremeClusters(List<List<MeteoPoint>> clusters) {
		List<List<MeteoPoint>> extremeClusters = new ArrayList<List<MeteoPoint>>(2);
		
		boolean init = false;
		double min = -1, max = -1;
		int minInd = -1, maxInd = -1;
		
		for (int i = 0; i < clusters.size(); i++) {
			List<MeteoPoint> cluster = clusters.get(i);
			double median;
			int len = cluster.size();
			Collections.sort(cluster, new MeteoPointTemperatureComparator());
			int mid = len / 2;
			if (len % 2 == 0) {
				median = (cluster.get(mid).getTemperature() + cluster.get(mid -1).getTemperature()) / 2.0;
			} else {
				median = cluster.get(mid).getTemperature();
			}
			if (init) {
				if (median < min) {
					min = median;
					minInd = i;
				} 
				if (median > max) {
					max = median;
					maxInd = i;
				}
			} else {
				min = median; minInd = i;
				max = median; maxInd = i;
				init = true;
			}
			System.out.println(i + " " + median);
		}
		
		System.out.println("Min= " + minInd + " " + min);
		System.out.println("Max= " + maxInd + " " + max);
		extremeClusters.add(clusters.get(minInd));
		extremeClusters.add(clusters.get(maxInd));
		System.out.println(extremeClusters.get(0).size());
		System.out.println(extremeClusters.get(1).size());
		System.out.println(extremeClusters.size());
		return extremeClusters;
		
	}

	private List<MeteoPoint> concatenate(List<MeteoPoint> meteoPoints,
			List<MeteoPoint> previousPoints) {
		Set<MeteoPoint> set = new HashSet<MeteoPoint>(meteoPoints);
		set.addAll(previousPoints);
		List<MeteoPoint> mergedList = new ArrayList<MeteoPoint>(set);
		return mergedList;
	}


	
	private List<GeoTimePoint> getRandomPointsWithinArea() {
		int numberOfPoints = 30;
		List<GeoTimePoint> geoTimePoints = new ArrayList<GeoTimePoint>();
		Double[] minMaxBorders = getMaxMinBorders();
//		System.out.println(minMaxBorders[0] + " "+ minMaxBorders[1] + " " + minMaxBorders[2] + " " + minMaxBorders[3]);
		Random rd = new Random();
		for (int i = 0; i < numberOfPoints;) {
			double randomLatitude = minMaxBorders[0] + (minMaxBorders[1] - minMaxBorders[0]) * rd.nextDouble();
			double randomLongitude = minMaxBorders[2] + (minMaxBorders[3] - minMaxBorders[2]) * rd.nextDouble();
			GeoPoint geoPoint = new GeoPoint(randomLatitude, randomLongitude);
			if(isLocationInsideArea(geoPoint)) {
				i++;
				long time = timeFrom + (long)(rd.nextDouble()*(timeTo-timeFrom));
//				System.out.println("rg:" + randomLatitude + " " + randomLongitude + " " +time);
				geoTimePoints.add(new GeoTimePoint(randomLatitude, randomLongitude, timeTo));
			} 
		}
		return geoTimePoints;
	}
	
	
	private List<MeteoPoint> getPointsAccordingToProbabilityDistribution() throws Exception {
		int numberOfPossiblePoints = NUMBER_OF_POSSIBLE_POINTS;
		Double[] minMaxBorders = getMaxMinBorders();
		ProbabilityDistribution pdis = new ProbabilityDistribution();
		List<List<Double>> possiblePoints = pdis.getAllPossiblePoints(minMaxBorders[0],
				minMaxBorders[1], minMaxBorders[2], minMaxBorders[3], numberOfPossiblePoints,
				timeFrom, timeTo);
		
		numberOfPossiblePoints = possiblePoints.get(0).size();
		double[] x1 = Doubles.toArray(possiblePoints.get(0));
		double[] x2 = Doubles.toArray(possiblePoints.get(1));
		double[] x3 = Doubles.toArray(possiblePoints.get(2));
		
		double[] xt1 = new double[NUMBER_OF_POINTS_TO_TEST];
		double[] xt2 = new double[NUMBER_OF_POINTS_TO_TEST];
		double[] xt3 = new double[NUMBER_OF_POINTS_TO_TEST];
		double[] y = new double[NUMBER_OF_POINTS_TO_TEST];
		
		
		int currentPoint = 0;
		int i = 0;
		
		//Add the four corner points
		currentPoint = addCornerPoint(minMaxBorders[0], minMaxBorders[2], timeFrom, xt1, xt2, xt3, y, currentPoint);
		currentPoint = addCornerPoint(minMaxBorders[0], minMaxBorders[3], timeFrom, xt1, xt2, xt3, y, currentPoint);
		currentPoint = addCornerPoint(minMaxBorders[1], minMaxBorders[2], timeFrom, xt1, xt2, xt3, y, currentPoint);
		currentPoint = addCornerPoint(minMaxBorders[1], minMaxBorders[3], timeFrom, xt1, xt2, xt3, y, currentPoint);
		currentPoint = addCornerPoint(minMaxBorders[0], minMaxBorders[2], timeTo, xt1, xt2, xt3, y, currentPoint);
		currentPoint = addCornerPoint(minMaxBorders[0], minMaxBorders[3], timeTo, xt1, xt2, xt3, y, currentPoint);
		currentPoint = addCornerPoint(minMaxBorders[1], minMaxBorders[2], timeTo, xt1, xt2, xt3, y, currentPoint);
		currentPoint = addCornerPoint(minMaxBorders[1], minMaxBorders[3], timeTo, xt1, xt2, xt3, y, currentPoint);
		i+=8;
		
		//Add 32 more random points
		Random rd = new Random();
		for (; i < 40; i++) {
			int nextPointIndex = rd.nextInt(numberOfPossiblePoints);
			currentPoint = addCornerPoint(x1[nextPointIndex], x2[nextPointIndex], (long) x3[nextPointIndex],
										  xt1, xt2, xt3, y, currentPoint);
		}
		
		//Pick other points using R' loess
		String[] Rargs = {"--vanilla"};
		re = new Rengine(Rargs, false, null);	
		if (!re.waitForR()) {
			System.out.println("Cannot load R");
			throw new Exception("Can not load R.");
		}
		
		re.assign("currentPoint", new int[] {currentPoint});
		re.assign("x1", x1);
		re.assign("x2", x2);
		re.assign("x3", x3);
		re.assign("xt1", xt1);
		re.eval("xt1 = xt1[1:currentPoint]");
		re.assign("xt2", xt2);
		re.eval("xt2 = xt2[1:currentPoint]");
		re.assign("xt3", xt3);
		re.eval("xt3 = xt3[1:currentPoint]");
		re.assign("y", y);
		re.eval("y = y[1:currentPoint]");
		re.eval("span = 0.5") ;
		
//		System.out.println(re.eval("x1"));
//		System.out.println(re.eval("x2"));
//		System.out.println(re.eval("x3"));
//		System.out.println(re.eval("xt1"));
//		System.out.println(re.eval("xt2"));
//		System.out.println(re.eval("xt3"));

		int initialValues = currentPoint;
		for (; i < NUMBER_OF_POINTS_TO_TEST;i++) {
			re.assign("i", new int[] {currentPoint - initialValues});
			System.out.println("i = " + re.eval("i").asInt());
			re.eval("fit <- (loess(y ~ xt1 + xt2 + xt3, span = span - 0.4* i/100))");
			re.eval("p <- predict(fit, data.frame(x1, x2, x3), se = T)");
			System.out.println("Probabilities = " + re.eval("p$se"));
			re.eval("x1.next <- x1[p$se == max(p$se)]");
			re.eval("rind = sample(1:length(x1.next), 1)");
			//System.out.println(re.eval("rind").asInt());
			re.eval("x1.next = x1.next[rind]");
			re.eval("x2.next <- x2[p$se == max(p$se)]");
			re.eval("x2.next = x2.next[rind]");
			re.eval("x3.next <- x3[p$se == max(p$se)]");
			re.eval("x3.next = x3.next[rind]");
			
			double x1next = re.eval("x1.next").asDouble();
			double x2next = re.eval("x2.next").asDouble();
			double x3next = re.eval("x3.next").asDouble();
			System.out.println("x1next = " + x1next);
			System.out.println("x2next = " + x2next);
			System.out.println("x3next = " + x3next);
			double tnext = getTemperatureForPoint(x1next, x2next, (long) x3next);
			if (tnext == -300) {
				continue;
			}
			currentPoint++;
			re.assign("tnext", new double[] {tnext});
			re.eval("y <- c(y, tnext)");
			re.eval("xt1 <- c(xt1, x1.next)");
			re.eval("xt2 <- c(xt2, x2.next)");
			re.eval("xt3 <- c(xt3, x3.next)");
			System.out.println(re.eval("xt1"));
			System.out.println(re.eval("xt2"));
			System.out.println(re.eval("xt3"));
			System.out.println(re.eval("y"));
		}
		
		
		xt1 = re.eval("xt1").asDoubleArray();
		xt2 = re.eval("xt2").asDoubleArray();
		xt3 = re.eval("xt3").asDoubleArray();
		y = re.eval("y").asDoubleArray();
		List<MeteoPoint> temperaturePoints = covertToTemperaturePoints(xt1, xt2, xt3, y, currentPoint);
		return temperaturePoints;
	}
	
	private List<MeteoPoint> covertToTemperaturePoints(double[] latitude, double[] longitude, double[] time, 
			double[] temperature, int len) {
		List<MeteoPoint> temperaturePoints = new ArrayList<MeteoPoint>();
		for(int i = 0; i < len; i++) {
			MeteoPoint meteo = new MeteoPoint();
			meteo.setLatitude(latitude[i]);
			meteo.setLongitude(longitude[i]);
			meteo.setTime((long) time[i]);
			meteo.setTemperature(temperature[i]);
			temperaturePoints.add(meteo);
		}
		return temperaturePoints;
	}

	private int addCornerPoint(double lat, double lon, long time, double[] xt1, double[] xt2, double[] xt3, double[] y, int index){
		double t1 = getTemperatureForPoint(lat, lon, time);
		if (t1!=-300) {
			xt1[index] = lat;
			xt2[index] = lon;
			xt3[index] = time;
			y[index] = t1;
			index++;
		} else {
			System.out.println("The temperature is invalid!.");
		}
		return index;
	}
	
	
	private double getTemperatureForPoint(Double lat, Double lon, Long time) {
		MeteoPoint point = ForecastServiceHelper.getDataForPoint(new GeoTimePoint(lat, lon, time));
		if (point != null && point.getTemperature() != -300.0) {
			return point.getTemperature();
		} else return -300;
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

//        boolean result = false;
//        GeoPoint p1 = areaPoints.get(0);
//        for (int i = 1; i < areaPoints.size(); i++) {
//            GeoPoint p2 = areaPoints.get(i);
//            if ( ((p2.getLatitude() <= point.getLatitude()
//                    && point.getLatitude() < p1.getLatitude()) ||
//                    (p1.getLatitude() <= point.getLatitude()
//                            && point.getLatitude() < p2.getLatitude()))
//                    && (point.getLongitude() < (p1.getLongitude() - p2.getLongitude())
//                    * (point.getLatitude() - p2.getLatitude())
//                    / (p1.getLatitude() - p2.getLatitude()) + p2.getLongitude()) )
//                result = !result;
//
//            p1 = p2;
//        }
//        return result;
    	return true;
    }
	
	private List<MeteoPoint> findPreviousPointsForSameRegionAndTime() throws SQLException {
		List<MeteoPoint> previousPoints = SearchQueryDAO.findRelatedPoints(timeFrom, timeTo);
		return previousPoints;
	}
	
	private List<List<MeteoPoint>> locateExtremePoints(List<MeteoPoint> meteoPoints) throws Exception {
		return KMeansClustering.clusterMeteoPoints(meteoPoints);
		
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