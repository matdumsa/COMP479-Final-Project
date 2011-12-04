package finalproject.index.spimi;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import finalproject.Posting;

public interface IInvertedIndex extends Iterable<String> {

	public abstract boolean add(String token, int id);

	public abstract Set<String> keySet();

	public abstract int size();

	public abstract AbstractSet<Posting> getSet(String token);

	public abstract Iterator<String> iterator();

	public abstract void clear();
	
	public abstract HashSet<Posting> getAll();

}