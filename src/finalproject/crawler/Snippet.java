package finalproject.crawler;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.compress.utils.IOUtils;
import org.jsoup.Jsoup;

import finalproject.GenericDocument;
import finalproject.corpus.Corpus;
import finalproject.corpus.CorpusFactory;
import finalproject.index.compression.StopwordRemover;
import finalproject.technicalservices.BenchmarkRow;
import finalproject.technicalservices.Constants;

public class Snippet {
	
	public static void main(String[] args) {

		System.out.print(Snippet.findsnippet(5684,"schedules dynamic gholi registrar"));

	}
	
	public static String findsnippet(int docid, String query){

		String[] contentTerms = null;
		try {
			contentTerms = getPageContentById(docid).toLowerCase().split(" ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(contentTerms));
		
		String[] terms = query.toLowerCase().split(" ");
		String output = "";
		for(String q : terms){
			if(!StopwordRemover.stopwords.contains(q) && words.contains(q))
			{
				int termposition = words.indexOf(q);
				if(termposition>2)
				{
					if(termposition+2<words.size())
					{
						output += words.get(termposition-2)+" "+
								 words.get(termposition-1)+" "+
								 words.get(termposition)+" "+
								 words.get(termposition+1)+" "+
								 words.get(termposition+2)+"....";
					}
				}
				else if(termposition>1)
				{
					if(termposition+3<words.size())
					{
						output += words.get(termposition-1)+" "+
								 words.get(termposition)+" "+
								 words.get(termposition+1)+" "+
								 words.get(termposition+2)+" "+
								 words.get(termposition+3)+" "+"....";						
					}
				}
				else if(termposition>0)
				{
					if(termposition+4<words.size())
					{
						output += words.get(termposition)+" "+
								 words.get(termposition+1)+" "+
								 words.get(termposition+2)+" "+
								 words.get(termposition+3)+" "+
								 words.get(termposition+4)+" "+"....";						
					}
				}
			}
		}
		return output;
	}
	

	public static String getPageContentById(int id) throws IOException {
		String path = Constants.basepath + "/data/" + id;
		String output = readFile(path);
		return output.replaceAll("\\<[^>]*>","").replaceAll("\\&.*?\\;", "").replaceAll("\\s+", " ");
	}
	
//	public static String getPageContentById(int id) {
//		String path = Constants.basepath + "/data/" + id;
//		File inputFile = new File(path);
//		String output;
//		try {
//			output = Jsoup.parse(inputFile, "UTF-8", "").text();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			output = null;
//		}
//		return output;
//	}
	
	private static String readFile(String path) throws IOException {
		  FileInputStream stream = new FileInputStream(new File(path));
		  try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    /* Instead of using default, pass in a decoder. */
		    return Charset.defaultCharset().decode(bb).toString();
		  }
		  finally {
		    stream.close();
		  }
		}
}
