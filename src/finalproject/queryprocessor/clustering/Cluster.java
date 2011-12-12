package finalproject.queryprocessor.clustering;

import java.util.Collection;
import java.util.LinkedList;

import finalproject.VectorTermSpace;
import finalproject.WeightedDocument;

public class Cluster {

	private LinkedList<WeightedDocument> members;
	private String name;
	private VectorTermSpace cachedCentroid = null;


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
	 * Return numberOfDocument from this Cluster
	 * @param numberOfDocument
	 * @return
	 */
	public Collection<WeightedDocument> subList(int numberOfDocument) {
		LinkedList<WeightedDocument> list = new LinkedList<WeightedDocument>();
		int currentSize = 0;
		int numberPass = 0;
		while (currentSize < numberOfDocument && currentSize < members.size()){
			for(WeightedDocument w : members.subList(numberPass*numberOfDocument, Math.min(numberOfDocument, members.size()))){
				if (!list.contains(w)){
					list.add(w);
				}
				currentSize++;
			}
			numberPass++;
		}
		if (members.size() < numberOfDocument)
			return members;
		return list;
		
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
