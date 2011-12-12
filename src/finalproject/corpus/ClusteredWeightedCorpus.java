/**
 * 
 */
package finalproject.corpus;

import finalproject.GenericDocument;
import finalproject.index.spimi.DefaultInvertedIndex;
import finalproject.queryprocessor.clustering.KMeansClustering;

/**
 * @author matdumsa
 *
 */
public class ClusteredWeightedCorpus extends WeightedCorpus {

	private KMeansClustering km;
	/**
	 * @param documentTemplate
	 * @param index
	 */
	public ClusteredWeightedCorpus(
			Class<? extends GenericDocument> documentTemplate,
			DefaultInvertedIndex index) {
		super(documentTemplate, index);

		km = new KMeansClustering(this, index);
	}

	public KMeansClustering getClustering() {
		if (km.getLastClusteringMoment() == 0)
			km.performClustering();
		return km;
	}

}
