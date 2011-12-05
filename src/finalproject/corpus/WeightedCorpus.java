package finalproject.corpus;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import finalproject.GenericDocument;
import finalproject.Posting;
import finalproject.TaskComputeTFIDF;
import finalproject.WeightedDocument;
import finalproject.index.IndexSingleton;
import finalproject.index.spimi.DefaultInvertedIndex;
import finalproject.technicalservices.BenchmarkRow;



/**
 * @author jeremiemartinez
 *
 */
public class WeightedCorpus extends Corpus {
	
	private DefaultInvertedIndex index;  
	private int NUMBER_OF_THREAD = Runtime.getRuntime().availableProcessors();;
	//Default constructor allow only the factory in this package to create instances
	public WeightedCorpus() {
		super();
	}
	
	
	public void closeIndex(){
		computeTFIDFVector();
		super.closeIndex();
	}

	private void computeTFIDFVector() {
		BenchmarkRow bench = new BenchmarkRow("Generating TFIFD with " + NUMBER_OF_THREAD + " workers");
		bench.start();

		index = IndexSingleton.getInstance();
		if (index.validate() == false)
			throw new RuntimeException("Invalid index, cannot compute TFIDF on an invalid index");

		TreeMap<GenericDocument, LinkedList<Posting>> data = index.getDocumentBasedIndex();
		System.out.println("Starting TF-IDF computing");

		//Pre-process all postings to give them a unique id
		HashMap<String, Integer> termsToUniqueIds = new HashMap<String, Integer>();
		int c = 0;
		for (String s : index) {
			termsToUniqueIds.put(s, c++);
		}

		
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
		for (GenericDocument gd : data.keySet()) {
			WeightedDocument document = (WeightedDocument) gd;
			LinkedList<Posting> postingList = data.get(gd);
			executor.submit(new TaskComputeTFIDF(document, postingList, index, super.size(), termsToUniqueIds));
		}
		
		executor.shutdown();
		

		System.out.println("Computing TF-IDF finished");
		bench.stop();
		System.out.println(bench.toString());
	}


}
