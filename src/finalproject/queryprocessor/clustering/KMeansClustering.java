package finalproject.queryprocessor.clustering;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import finalproject.GenericDocument;
import finalproject.WeightedDocument;
import finalproject.corpus.Corpus;
import finalproject.index.spimi.DefaultInvertedIndex;
import finalproject.technicalservices.BenchmarkRow;

public class KMeansClustering {

	private static final int NUMBER_OF_PASS = 5;
	private LinkedList<WeightedDocument> docList = new LinkedList<WeightedDocument>();
	private List<Cluster> clusterList = new ArrayList<Cluster>();
	private int k = 8;
	private DefaultInvertedIndex index;
	private int NUMBER_OF_THREAD = Runtime.getRuntime().availableProcessors();;
	private long lastClustering = 0;
	private Corpus corpus;
	private LinkedList<ClusteringTask> clusteringTask;
	
	/**
	 * Create a clustering engine for the given corpus and index
	 * @param corpus
	 * @param index
	 */
	public KMeansClustering(Corpus corpus, DefaultInvertedIndex index) {
		k=15;
		this.corpus = corpus;
		this.index = index;
		for (int x=0; x< k; x++)
			clusterList.add(new Cluster("Cluster" + x));
	}
	
	/**
	 * Peek at a given cluster number and retrieve numberOfDocument from it
	 * @param clusterNo
	 * @param numberOfDocument
	 * @return
	 */
	public Collection<WeightedDocument> peekAtClusters(int clusterNo, int numberOfDocument) {
		return clusterList.get(clusterNo).subList(numberOfDocument);
	}

	/**
	 * Perform the actual clustering of the document, comparing each document in the set
	 * with the centroid of each cluster, adding each document to the closest centroid's cluster.
	 */
	public void performClustering() {
		System.out.println("Clustering started for " + k + " clusters");

		for (GenericDocument d : corpus) {
			docList.add((WeightedDocument) d);
		}

		BenchmarkRow clusteringBenchmark = new BenchmarkRow("Clustering");
		clusteringBenchmark.start();
		System.out.println("Clustering: pre-processing the data");
		
		//Pre-process all postings to give them a unique id
		HashMap<String, Integer> termsToUniqueIds = new HashMap<String, Integer>();
		int c = 0;
		for (String s : index) {
			termsToUniqueIds.put(s, c++);
		}

		System.out.println("Initializing each document in a random cluster");
		clusteringTask = new LinkedList<ClusteringTask>();
		while (docList.isEmpty() == false) {
			WeightedDocument document = docList.poll();
			if (document.getVector() != null) {
	 			clusterList.get((int) (Math.random()*k)).addDocument(document);
	 			//This task list will be re-used at every step of the K-Mean algorithm as a handle
	 			ClusteringTask task = new ClusteringTask(document, clusterList);
	 			clusteringTask.add(task);
			}
	 		
		}
		docList = null;
		//Create the thread pool that will serve all the clustering tasks
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);

		
		for (int passNo =1; passNo<= NUMBER_OF_PASS; passNo++) {
			for (Cluster cluster : clusterList) {
				//force centroid calculation and caching.
				cluster.getCentroid(true);
				//remove all members (they will be added shortly
				cluster.getMembersAndRemove();
			}
			System.out.println("Clustering: pass " + passNo + "/" + NUMBER_OF_PASS);
			try {
				//Start the task and return only when completed.
				executor.invokeAll(clusteringTask);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//One final centroid recompute
		for (Cluster cluster : clusterList) {
			//force centroid calculation and caching.
			cluster.getCentroid(true);
		}

		clusteringBenchmark.stop();
		System.out.println(clusteringBenchmark.toString());
		lastClustering = System.currentTimeMillis();
	}

	public long getLastClusteringMoment() {
		return lastClustering;
	}
	

	public int getClusterCount() {
		return k;
	}
	
	public List<Cluster> getClusterList() {
		return clusterList;
	}

}
