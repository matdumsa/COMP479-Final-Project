package finalproject.queryprocessor.clustering;

import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

import finalproject.VectorTermSpace;
import finalproject.WeightedDocument;

public class Cluster {

	private LinkedList<WeightedDocument> members;
	private String name;
	private VectorTermSpace cachedCentroid = null;


	public int getSize() {
		return members.size();
	}
	
	public String getName() { return name; }

	/**
	 * Creates a new cluster and assign it the name given in parameter
	 * @param name
	 */
	public Cluster(String name) {
		this.name = name;
		members = new LinkedList<WeightedDocument>();
	}
	
	/**
	 * 
	 * @return Return the document that is the farther from the centroid and remove it from the collection
	 */
	public WeightedDocument poll() {
		return members.pollFirst();
	}

	/**
	 * Add the document given in parameter to this cluster
	 * @param d
	 */
	public synchronized void addDocument(WeightedDocument d) {
		members.add(d);
	}

	/**
	 * Compute the centroid in n-euclidian space for this cluster and return it
	 * @return
	 */
	private VectorTermSpace getCentroid() {
		if (cachedCentroid != null)
			return cachedCentroid;
		VectorTermSpace centroid = new VectorTermSpace();
		for (WeightedDocument d : members) {
			if (d != null)
				centroid = d.getVector().add(centroid);
		}
		centroid.divideBy(new Double(members.size()));
		cachedCentroid = centroid;
		return centroid;
	}
	
	/**
	 * Returns the centroid previously calculated and cached if the flag is set to false
	 * @param recompute, re-compute the centroid if set to true
	 * @return
	 */
	public VectorTermSpace getCentroid(boolean recompute) {
		if (recompute == true || cachedCentroid == null) {
			cachedCentroid=null;
			getCentroid();
		}
		return cachedCentroid;
	}

	/**
	 * Return numberOfDocument from this Cluster in descending order of distance from the centroid
	 * @param numberOfDocument
	 * @return
	 */
	public Collection<WeightedDocument> subList(int numberOfDocument) {
		TreeMap<Double, WeightedDocument> result = new TreeMap<Double, WeightedDocument>();
			
		for (WeightedDocument wd : members) {
			result.put(wd.getVector().getDistanceFromVector(cachedCentroid), wd);
		}

		LinkedList<WeightedDocument> finalResult = new LinkedList<WeightedDocument>();
		int x=1;
		while (x<numberOfDocument && result.size() > 0) {
			finalResult.add(result.pollFirstEntry().getValue());
			x++;
		}
		
		return finalResult;
	}
	
	public Collection<WeightedDocument> getClosestTen() {
		return subList(10);
	}
	
	/**
	 * Return the list of all member and empty this cluster.
	 * @return
	 */
	public LinkedList<WeightedDocument> getMembersAndRemove() {
		LinkedList<WeightedDocument> results = members;
		members = new LinkedList<WeightedDocument>();
		return results;
	}


}
