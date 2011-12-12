package finalproject.index;

import finalproject.index.spimi.DefaultInvertedIndex;

public class IndexSingleton {

	private static DefaultInvertedIndex index = null;

	public static synchronized DefaultInvertedIndex getInstance() {
		return getInstance(false);
	}
		
	public static synchronized DefaultInvertedIndex getInstance(boolean empty) {
		if (index==null) {
			if (empty==true)
				index = new DefaultInvertedIndex();
			else
				index = DefaultInvertedIndex.readFromFile("index.txt");	
		}
		return index;
	}
}
