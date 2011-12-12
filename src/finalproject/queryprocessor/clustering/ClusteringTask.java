package finalproject.queryprocessor.clustering;

import java.util.Collection;
import java.util.concurrent.Callable;

import finalproject.WeightedDocument;

public class ClusteringTask implements Callable<Object>{

	private WeightedDocument document;
	private Collection<Cluster> clusterList;
	private Cluster currentCluster;
	
	public ClusteringTask(WeightedDocument document, Collection<Cluster> clusterList) {
		this.document = document;
		this.clusterList = clusterList;
	}
	
	@Override
	public Object call() {
		currentCluster = findClosestCluster(document);
		currentCluster.addDocument(document);
		document.setCluster(currentCluster);
		return null;
	}

	public Cluster getCurrentCluster() {
		return currentCluster;
	}
	
	private Cluster findClosestCluster(WeightedDocument d) {
		Cluster closest = null;
		Double closestDistance = Double.MAX_VALUE;
		for (Cluster cluster : clusterList) {
			Double distance = cluster.getCentroid(false).getDistanceFromVector(d.getVector());
			if (distance < closestDistance) {
				closestDistance = distance;
				closest = cluster;
			}
		}
		return closest;
	}
	
	public WeightedDocument getDocument() {
		return document;
	}
}
