package model;

import java.util.List;

public class ClustersAndHulls {
	
	private List<ConvexHullAndTime> hulls;
	private List<List<MeteoPoint>> clusters;
	public ClustersAndHulls(List<ConvexHullAndTime> hulls,
			List<List<MeteoPoint>> clusters) {
		super();
		this.hulls = hulls;
		this.clusters = clusters;
	}
	public List<ConvexHullAndTime> getHulls() {
		return hulls;
	}
	public List<List<MeteoPoint>> getClusters() {
		return clusters;
	}
	
	
}
