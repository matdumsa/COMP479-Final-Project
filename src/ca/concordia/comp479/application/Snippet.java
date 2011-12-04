package ca.concordia.comp479.application;


import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.WebDocument;
import info.mathieusavard.domain.corpus.Corpus;
import info.mathieusavard.domain.corpus.CorpusFactory;
import info.mathieusavard.domain.index.compression.StopwordRemover;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class Snippet {
	
	public static void main(String[] args) {
		Corpus txt = CorpusFactory.getCorpus();
		Snippet haha = new Snippet();
		System.out.print(haha.findsnippet(1443,"sabin gholi", txt));
	}
	
	public String findsnippet(int docid, String query, Corpus corpus){
		String txt = corpus.findArticle(docid).toString();
		System.out.print(txt);
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(txt.split(" ")));
		
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
