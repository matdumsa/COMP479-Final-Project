package finalproject.queryprocessor.spelling;
import finalproject.index.spimi.DefaultInvertedIndex;
import finalproject.index.spimi.IInvertedIndex;

public class fromIndex {

	public static void main(String[] args) {
		IInvertedIndex i = DefaultInvertedIndex.readFromFile("index.txt");
		for (String s : i) {
			System.out.println(i + " " + Soundex.soundex(s));
		}
	}
}
