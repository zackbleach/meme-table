package com.zackbleach.memetable.clustering;

import org.springframework.util.StringUtils;

public class ClusterPair implements Comparable<ClusterPair> {
	
	private Cluster[] clusters = new Cluster[2];
	private float distance;
	
	public ClusterPair(Cluster left, Cluster right) {
		clusters[0] = left;
		clusters[1] = right;
		distance = left.getFeature().getDistance(right.getFeature());
	}
	
	public float getDistance() {
		return distance;
	}
	
	public Cluster[] getClusters() {
		return clusters;
	}
	
	public Cluster agglomerate() {

		Cluster left = clusters[0];
		Cluster right = clusters[1];
		Cluster cluster = new Cluster();
		cluster = setClusterName(left, right, cluster);
		cluster = setClusterChildren(left, right, cluster);
		cluster = setClusterMemeFeature(left, right, cluster);
		cluster = setClusterHeight(left, right, cluster);
		cluster.setDifference(getDistance());
		return cluster;
 	}

	private Cluster setClusterMemeFeature(Cluster left, Cluster right, Cluster cluster) {
		MemeFeature m = left.getFeature().averageFeature(right.getFeature());
		cluster.setFeature(m);
		return cluster;
	}

	private Cluster setClusterChildren(Cluster left, Cluster right, Cluster cluster) {
		Cluster[] children = new Cluster[2];
		children[0] = left;
		children[1] = right;
		cluster.setChildren(children);
		return cluster;
	}

	private Cluster setClusterName(Cluster left, Cluster right, Cluster cluster) {
		if (!StringUtils.isEmpty(left.getName()) && !StringUtils.isEmpty(right.getName())) {
			if (left.getName().equals(right.getName())) {
				cluster.setName(left.getName());
			} else {
			cluster.setName(left.getName() + "+" + right.getName());
			}
		} else if (left.getName() != null) {
			cluster.setName(left.getName());
		} else if (right.getName() != null) {
			cluster.setName(right.getName());
		}
		return cluster;
	}
	
	private Cluster setClusterHeight(Cluster left, Cluster right, Cluster cluster) {
		int height = Math.max(left.getHeight(), right.getHeight()) + 1;
		cluster.setHeight(height);
		return cluster;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clusters == null) ? 0 : clusters.hashCode());
		result = prime * result + Float.floatToIntBits(distance);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClusterPair other = (ClusterPair) obj;
		if (clusters == null) {
			if (other.clusters != null)
				return false;
		} else if (!clusters.equals(other.clusters))
			return false;
		if (Float.floatToIntBits(distance) != Float
				.floatToIntBits(other.distance))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(ClusterPair otherClusterPair) {
		if (otherClusterPair.getDistance() == this.getDistance()) {
			return 0;
		}
		return (this.getDistance() > otherClusterPair.getDistance()) ? 1 : -1;
	}
}