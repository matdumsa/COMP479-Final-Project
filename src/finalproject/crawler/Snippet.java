package finalproject.crawler;


import java.util.ArrayList;
import java.util.Arrays;

import finalproject.GenericDocument;
import finalproject.corpus.Corpus;
import finalproject.corpus.CorpusFactory;
import finalproject.index.compression.StopwordRemover;

public class Snippet {
	
	public static void main(String[] args) {
		Corpus txt = CorpusFactory.getCorpus();
		Snippet haha = new Snippet();
		System.out.print(haha.findsnippet(1443,"sabin gholi", txt));
	}
	
	public String findsnippet(int docid, String query, Corpus corpus){
		GenericDocument txt = corpus.findArticle(docid);
		
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(txt.getText()));
		
		
		
		String[] terms = query.split(" ");
		String output = "";
		for(String q : terms){
			if(!StopwordRemover.stopwords.contains(q) && words.contains(q))
			{
				int termposition = words.indexOf(q);
				if(termposition>2){
					output = words.get(termposition-2)+" "+
							 words.get(termposition-1)+" "+
							 words.get(termposition)+" "+
							 words.get(termposition+1)+" "+
							 words.get(termposition+2)+"....";
				}
			}
		}
		return output;
	}
	

}
