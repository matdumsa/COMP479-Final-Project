/**
 * 
 */
package finalproject.corpus;

import finalproject.GenericDocument;
import finalproject.index.IndexSingleton;
import finalproject.index.spimi.DefaultInvertedIndex;
import finalproject.technicalservices.Property;

/**
 * This class is responsible for creating the proper Corpus singleton.
 * The singleton effect is garanteed by the visibility of the corpus and weightedcorpus constructor
 * @author jeremiemartinez
 *
 */
public class CorpusFactory {

	//Default visibility makes it visible to corpus class so they can throw a runtime :P
	static Corpus corpus;

	public synchronized static Corpus getCorpus(){
		return getCorpus(false);
	}

	@SuppressWarnings("unchecked")
	public synchronized static Corpus getCorpus(boolean emptyCorpus){
		try {
			if (corpus == null) {
				//Find a document factory from file.
				Class<? extends GenericDocument> documentTemplate = (Class<? extends GenericDocument>) Class.forName(Property.get("documentTemplate"));
				
				//Load an index
				
				DefaultInvertedIndex index;
				index = IndexSingleton.getInstance(emptyCorpus);
				
				String corpusType = Property.get("corpusType");
				if (corpusType.equalsIgnoreCase("finalproject.corpus.ClusteredWeightedCorpus"))
					corpus = new ClusteredWeightedCorpus(documentTemplate, index);
				if (corpusType.equalsIgnoreCase("finalproject.corpus.Corpus"))
					corpus = new Corpus(documentTemplate, index);
				if (corpusType.equalsIgnoreCase("finalproject.corpus.WeightedCorpus"))
					corpus = new WeightedCorpus(documentTemplate, index);
					
				if (emptyCorpus == false)
					corpus.readFromDisk();
			}
			return corpus;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
