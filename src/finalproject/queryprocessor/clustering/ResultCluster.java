package finalproject.queryprocessor.clustering;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import finalproject.WeightedDocument;

public class ResultCluster {
	private List<Collection<WeightedDocument>> results;
	
	
	
	public List<Collection<WeightedDocument>> getResults() {
		return results;
	}

	public ResultCluster(){
		results = new LinkedList<Collection<WeightedDocument>>();
	}

	public void add(Collection<WeightedDocument> peekAtClusters) {
		results.add(peekAtClusters);
	}
}
