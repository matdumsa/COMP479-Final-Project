package finalproject.queryprocessor;

import finalproject.GenericDocument;
import finalproject.Posting;

public class Result implements Comparable<Result> {

	private GenericDocument document;
	
	public Result(GenericDocument document) {
		super();
		this.document = document;
	}

	public Result(GenericDocument result, Posting p) {
		super();
		this.document = result;
	}

	public GenericDocument getDocument() {
		return document;
	}
	
	
	@Override
	/**
	 * USed for insertion in a sorted array.. the better the document the first you are
	 */
	public int compareTo(Result other) {
			return  this.document.compareTo(other.getDocument());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Result))
			return false;
		return (((Result) o).getDocument().equals(getDocument()));
	}
	
	
	
}
