package finalproject.queryprocessor.spelling;
import finalproject.index.IndexSingleton;
import finalproject.index.spimi.IInvertedIndex;

public class fromIndex {

	public static void main(String[] args) {
		IInvertedIndex i = IndexSingleton.getInstance();
		for (String s : i) {
			System.out.println(i + " " + Soundex.soundex(s));
		}
	}
}
