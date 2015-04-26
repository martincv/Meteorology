package weka;

import java.util.ArrayList;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class TestWeka {

	
	public static void main(String[] args) throws Exception {
		// create instances
	    Attribute attr1 = new Attribute("longitude");
	    Attribute attr2 = new Attribute("latitude");
	    Attribute attr3 = new Attribute("temperature");

	    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	    attributes.add(attr1);
	    attributes.add(attr2);
	    attributes.add(attr3);


	    // predict instance class values
	    Instances testing = new Instances("Test dataset", attributes, 0);
	    
	 // add data
	    double[] values = new double[testing.numAttributes()];
	    values[0] = 0.23;
	    values[1] = 3.45;
	    values[2] = 4.55;
	    

	    // add data to instance
	    testing.add(new DenseInstance(1.0, values));
	    
	 // create the model 
	    SimpleKMeans kMeans = new SimpleKMeans();
	    kMeans.setNumClusters(3);
	    kMeans.buildClusterer(testing); 

	    // print out the cluster centroids
	    Instances centroids = kMeans.getClusterCentroids(); 
	    for (int i = 0; i < centroids.numInstances(); i++) { 
	      System.out.println( "Centroid " + i+1 + ": " + centroids.instance(i)); 
	    } 

	    // get cluster membership for each instance 
	    for (int i = 0; i < testing.numInstances(); i++) { 
	      System.out.println( testing.instance(i) + " is in cluster " + kMeans.clusterInstance(testing.instance(i)) + 1); 

	    } 
	}

}
