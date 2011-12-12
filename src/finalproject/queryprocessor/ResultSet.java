package finalproject.queryprocessor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import finalproject.Posting;
import finalproject.corpus.CorpusFactory;
import finalproject.index.spimi.DefaultInvertedIndex;

/**
 * A result set represents a set of result. It also contains the input query, the suggested query and the compressed query.
 * This object is used to encapsulate all information required by user interfaces to display the information found to the user.
 * @author jeremiemartinez
 *
 */
public class ResultSet implements Iterable<Result>{

	Collection<Result> results = null;
	private String userInputQuery = null;
	private String suggestedQuery = null;
	private String compressedQuery = null;
	private DefaultInvertedIndex index;
	// clusters /Êclasses blabla for future use

	public String getCompressedQuery() {
		return compressedQuery;
	}

	public String getUserInputQuery() {
		return userInputQuery;
	}

	protected DefaultInvertedIndex getIndex() {
		return index;
	}

	public ResultSet(DefaultInvertedIndex index, String userInputQuery, String compressedInputQuery ,Collection<Posting> results) {
		super();
		this.index = index;
		this.userInputQuery = userInputQuery;
		this.compressedQuery = compressedInputQuery;
		this.results = new LinkedList<Result>();
		this.results.addAll(generateResult(results));
	}


	public ResultSet() {

	}

	private static Collection<Result> generateResult(Collection<Posting> matchingDocument) {
		HashSet<Result> resultSet = new HashSet<Result>();
		for (Posting p : matchingDocument) 
			resultSet.add(new Result(CorpusFactory.getCorpus().findArticle(p.getDocumentId())));
		return resultSet;
	}

	public String getSuggestedQuery() {
		return suggestedQuery;
	}

	public Collection<Result> getResults() {
		return this.results;
	}
	
	public void addResult(Result r) {
		this.results.add(r);
	}

	@Override
	public Iterator<Result> iterator() {
		return results.iterator();
	}

	public void setSuggestedQuery(String suggestedQuery) {
		this.suggestedQuery = suggestedQuery;
	}
	
	public int size() {
		if (results == null){
			return 0;
		} else {
			return results.size();	
		}
	}
}
