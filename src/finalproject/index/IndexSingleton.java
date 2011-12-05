package finalproject.index;

import finalproject.index.spimi.DefaultInvertedIndex;

public class IndexSingleton {

	private static DefaultInvertedIndex index = null;
	
	public static synchronized DefaultInvertedIndex getInstance() {
		if (index==null)
			index = DefaultInvertedIndex.readFromFile("index.txt");
		return index;
	}
}
