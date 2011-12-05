package finalproject.queryprocessor;

import finalproject.GenericDocument;
import finalproject.Posting;

public class RankedResult extends Result{

	private double rank;

	public RankedResult(GenericDocument document, double rank) {
		super(document);
		this.rank = rank;
	}

	public RankedResult(GenericDocument document, double rank, Posting p) {
		super(document, p);
		this.rank = rank;
	}

	public double getRank() {
		return rank;
	}


	@Override
	/**
	 * USed for insertion in a sorted array.. the better the result the first you are
	 */
	public int compareTo(Result other) {
		if (other instanceof RankedResult){
			RankedResult rankOther = (RankedResult)other;
			if (rankOther.getRank() == this.rank)
				return  new Integer(this.getDocument().getId()).compareTo(other.getDocument().getId());
			else
				return Double.compare(rankOther.getRank(),this.rank);
		} else{
			return -1;
		}

	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RankedResult))
			return false;
		return (((RankedResult) o).getDocument().equals(getDocument()));

	}

}