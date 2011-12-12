package finalproject;

import java.util.HashMap;
import java.util.LinkedList;

import finalproject.index.spimi.DefaultInvertedIndex;

/**
 * This class represents a Task that compute TF-IDF vector for a given document. 
 * @author jeremiemartinez
 *
 */
public class TaskComputeTFIDF implements Runnable {

	private DefaultInvertedIndex index;
	private double corpusSize = 0;
	private LinkedList<Posting> postingList;
	private WeightedDocument document;
	private HashMap<String, Integer> mapTermToId;

	public TaskComputeTFIDF (WeightedDocument document, LinkedList<Posting> postingList, DefaultInvertedIndex d, int corpusSize, HashMap<String, Integer> mapTermToId){
		this.document = document;
		this.postingList = postingList;
		this.index = d;
		this.corpusSize = (double) corpusSize;
		this.mapTermToId = mapTermToId;
	}

	@Override
	public void run(){
		document.setVector(getTFIDFVector(postingList));
	}

	private VectorTermSpace getTFIDFVector(LinkedList<Posting> linkedList) {
		VectorTermSpace vector = new VectorTermSpace();
		if (linkedList != null){
			for (Posting p : linkedList){
				vector.getVector().put(mapTermToId.get(p.getTerm()), computeTFIDFScore(p));
			}
		} 
		return vector;
	}

	private Double computeTFIDFScore(Posting p) {
		double tf = (double)p.getOccurence();//(1.0+Math.log(p.getOccurence()));
		double idf = Math.log(corpusSize/(double)index.getIDFScore(p.getTerm()));
		return tf*idf;
	}

}
