package clustering;

import java.util.ArrayList;
import java.util.List;

import plotting.ClusterPlot;
import model.MeteoPoint;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class KMeansClustering {

	
	public static List<List<MeteoPoint>> clusterMeteoPoints(List<MeteoPoint> meteoPoints) throws Exception {
		// TODO Auto-generated method stub
		// create instances
	    Attribute attr1 = new Attribute("latitude");
	    Attribute attr2 = new Attribute("longitude");
	    Attribute attr3 = new Attribute("temperature");

	    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	    attributes.add(attr1);
	    attributes.add(attr2);
	    attributes.add(attr3);


	    // predict instance class values
	    Instances testing = new Instances("Test dataset", attributes, 0);
	    
	 // add data
	    double minLat = meteoPoints.get(0).getLatitude();
	    double maxLat = meteoPoints.get(0).getLatitude();
	    double minLon = meteoPoints.get(0).getLongitude();
	    double maxLon = meteoPoints.get(0).getLongitude();
	    double minTemp = meteoPoints.get(0).getTemperature();
	    double maxTemp = meteoPoints.get(0).getTemperature();
	    for (MeteoPoint meteoPoint : meteoPoints) {
		    if (meteoPoint.getLatitude() < minLat) {
		    	minLat = meteoPoint.getLatitude();
		    }
		    if (meteoPoint.getLatitude() > maxLat) {
		    	maxLat = meteoPoint.getLatitude();
		    }
		    if (meteoPoint.getLongitude() < minLon) {
		    	minLon = meteoPoint.getLongitude();
		    }
		    if (meteoPoint.getLongitude() > maxLon) {
		    	maxLon = meteoPoint.getLongitude();
		    }
		    if (meteoPoint.getTemperature() < minTemp) {
		    	minTemp = meteoPoint.getTemperature();
		    }
		    if (meteoPoint.getTemperature() > maxTemp) {
		    	maxTemp = meteoPoint.getTemperature();
		    }
	    }
	    
	    // add data
	    int N = meteoPoints.size();
	    System.out.println("Number of points=" + N);
	    for (MeteoPoint meteoPoint : meteoPoints) {
		    double[] values = new double[testing.numAttributes()];
		    values[0] = (meteoPoint.getLatitude() - minLat) / (maxLat - minLat);
		    values[1] = (meteoPoint.getLongitude() - minLon) / (maxLon - minLon);
		    values[2] = (meteoPoint.getTemperature() - minTemp) / (maxTemp - minTemp);
//		    System.out.println(values);
		    testing.add(new DenseInstance(1.0, values));
	    }
	    
	    double minValidity = Double.MAX_VALUE;
	    int bestNumberOfClusters = 0;
//	    for (int k=2; k < Math.ceil(Math.sqrt(N)); k++) {
	    for (int k=2; k <= N / 2; k++) {
	    	double validity;
	    	
		    // create the model 
		    SimpleKMeans kMeans = new SimpleKMeans();
		    kMeans.setNumClusters(k);
		    kMeans.buildClusterer(testing); 
	
		    // print out the cluster centroids
		    Instances centroids = kMeans.getClusterCentroids(); 
		    double inter = Math.sqrt(Math.pow(centroids.instance(0).value(0) - centroids.instance(1).value(0), 2)
		    		  + Math.pow(centroids.instance(0).value(1) - centroids.instance(1).value(1), 2)
		    		  + Math.pow(centroids.instance(0).value(2) - centroids.instance(1).value(2), 2));
		    int numCentroids = centroids.numInstances();
		    for (int i = 0; i < numCentroids - 1; i++) { 
		    	for (int j = i + 1; j < numCentroids; j++){
		    		double newInter = Math.sqrt(Math.pow(centroids.instance(i).value(0) - centroids.instance(j).value(0), 2)
				    		  + Math.pow(centroids.instance(i).value(1) - centroids.instance(j).value(1), 2)
				    		  + Math.pow(centroids.instance(i).value(2) - centroids.instance(j).value(2), 2));
		    		if (newInter < inter) {
		    			inter = newInter;
		    		}
		    	}
		    } 
		    
		    double intra = 0.0;
		    // get cluster membership for each instance 
		    for (int i = 0; i < testing.numInstances(); i++) { 
//		      System.out.println( testing.instance(i) + " is in cluster " + kMeans.clusterInstance(testing.instance(i)) + 1); 
		      Instance point = testing.instance(i);
		      int clusterNumber = kMeans.clusterInstance(point);
		      Instance cluster = centroids.instance(clusterNumber);
		      intra += Math.sqrt(Math.pow(cluster.value(0) - point.value(0), 2)
		    		  + Math.pow(cluster.value(1) - point.value(1), 2)
		    		  + Math.pow(cluster.value(2) - point.value(2), 2));
		    }
		    intra /= N;
		    
		    validity = intra/inter;
		    if(validity < minValidity){
		    	minValidity = validity;
		    	bestNumberOfClusters = k;
		    }
		    System.out.println(k + " validity=" + validity);
	    }
	    
	    System.out.println("minValidity=" + minValidity + " num clusters = "+ bestNumberOfClusters);
	    SimpleKMeans kMeans = new SimpleKMeans();
	    kMeans.setNumClusters(bestNumberOfClusters);
	    kMeans.buildClusterer(testing); 
	    List<List<MeteoPoint>> clusters = new ArrayList<List<MeteoPoint>>(bestNumberOfClusters);
	    for (int j = 0; j < bestNumberOfClusters; j++) {
	    	clusters.add(new ArrayList<MeteoPoint>());
	    }
	    for (int i = 0; i < testing.numInstances(); i++) { 
//		      System.out.println( testing.instance(i) + " is in cluster " + kMeans.clusterInstance(testing.instance(i)) + 1); 
		      Instance point = testing.instance(i);
		      int clusterNumber = kMeans.clusterInstance(point);
		      clusters.get(clusterNumber).add(meteoPoints.get(i));
	    }
//	    ClusterPlot plot = new ClusterPlot(clusters);
//        plot.setVisible(true);
		return clusters;
	}

}
