package finalproject.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import finalproject.index.compression.StopwordRemover;
import finalproject.technicalservices.Constants;

/**
 * This class provides static methods to compute snippet for a document. It helps to have a better web interface.
 * @author jeremiemartinez
 *
 */
public class Snippet {
	
	public static void main(String[] args) {

		System.out.print(Snippet.findsnippet(5684,"registrar schedule"));

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
		ArrayList<String> phrases = new ArrayList<String>();
		String output = "";
		for(String word : words)
		{
			output += word+" "; 
			if(words.indexOf(word)%5==0)
			{
				phrases.add(output);
				output = "";
			}
		}
		output = "";
		String[] terms = query.toLowerCase().split(" ");
		for(String sterm : terms){
			if(!StopwordRemover.stopwords.contains(sterm))
			{
				int count = 0;
				for(String phrase : phrases)
				{
					if(phrase.contains(sterm)&count<2)
					{
						output += phrase+"...";
						count++;
					}
					else if(count>2)
					{
						break;
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
