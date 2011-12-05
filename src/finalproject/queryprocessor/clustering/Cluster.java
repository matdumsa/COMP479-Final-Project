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

	public synchronized void addDocument(WeightedDocument d) {
		members.add(d);
	}

	public VectorTermSpace getCentroid() {
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
	
	public VectorTermSpace getCentroid(boolean force) {
		cachedCentroid = null;
		return getCentroid();
	}

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
	
	public LinkedList<WeightedDocument> getMembersAndRemove() {
		LinkedList<WeightedDocument> results = members;
		members = new LinkedList<WeightedDocument>();
		return results;
	}


}
