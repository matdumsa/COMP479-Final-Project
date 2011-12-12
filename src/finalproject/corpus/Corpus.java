package finalproject.corpus;


import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.TreeMap;

import finalproject.GenericDocument;
import finalproject.index.spimi.DefaultInvertedIndex;
import finalproject.technicalservices.Constants;

public class Corpus implements Iterable<GenericDocument>{

	protected TreeMap<Integer, GenericDocument> documentMap;
	private Class<? extends GenericDocument> factory = GenericDocument.class;
	private boolean readOnly=false; //when a Corpus is created, it is read-write and when finalized, it becomes read-only
	private boolean isDirty = true;
	private DefaultInvertedIndex index;
	
	//Default constructor allow only the factory in this package to create instances
	/**
	 * When using a document type that extends from GenericDocument, the factory (class that contains fromString) that can
	 * create this type of document should be passed here.
	 * @param factory
	 */

	
	Corpus(Class<? extends GenericDocument> factory, DefaultInvertedIndex index) {
		super();
		System.out.println("Creating a corpus");
		this.factory = factory;
		this.index = index;
		if (CorpusFactory.corpus != null)
			throw new RuntimeException("Oups.. you can't create a new corpus if there is already one in CorpusFactory");
	}

	public synchronized void addArticle(GenericDocument d) {
		if (documentMap == null)
			documentMap = new TreeMap<Integer, GenericDocument>();
		if (readOnly==true)
			throw new RuntimeException("Oups.. looks like you tried to add documents to a corpus that was finalized. It's now read-only.");
		// Check if this document already exists
		if (!documentMap.values().contains(d)){
			documentMap.put(d.getId(), d);
		}
		isDirty = true;
	}
	
	public void closeIndex(){
		if (isDirty)
			writeToDisk();
		readOnly=true;
	}
	
	protected void writeToDisk() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.basepath + "/articles.txt"));
			for (Integer i : documentMap.keySet()) {
				GenericDocument a = documentMap.get(i);
				out.write(a.toString() + "\n");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isDirty = false;
	}
	
	public void clear() {
		documentMap.clear();
	}
	
	public void readFromDisk() {
		documentMap = new TreeMap<Integer, GenericDocument>();
		try {
			LineNumberReader in = new LineNumberReader(new FileReader(Constants.basepath + "/articles.txt"));
			String line;
			line = in.readLine();
			while (line != null && line.length()>0) {
				try {
					Method factoryMethod = getFactory().getDeclaredMethod("fromString", String.class);
					GenericDocument d = (GenericDocument) factoryMethod.invoke(null, line);
					documentMap.put(d.getId(), d);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isDirty = false;
		closeIndex();
	}
	
	public int getTotalLength() {
		if (documentMap == null) readFromDisk();
		int ans=0;
		for (GenericDocument a : documentMap.values()) {
			ans+=a.getLengthInWords();
		}
		return ans;
	}

	public GenericDocument findArticle(int documentId) {
		if (documentMap == null) readFromDisk();
		return documentMap.get(documentId);
	}

	public int size() {
		if (documentMap == null) readFromDisk();
		return documentMap.size();
	}

	@Override
	public Iterator<GenericDocument> iterator() {
		return documentMap.values().iterator();
	}
	
	public DefaultInvertedIndex getIndex() {
		return index;
	}
	
	private Class<? extends GenericDocument> getFactory() {
		return this.factory;
	}
	
		
}
