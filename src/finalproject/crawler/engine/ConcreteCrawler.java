package finalproject.crawler.engine;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import websphinx.Crawler;
import websphinx.DownloadParameters;
import websphinx.Link;
import websphinx.Page;
import websphinx.Pattern;
import websphinx.Text;
import websphinx.Wildcard;
import finalproject.WebDocument;
import finalproject.corpus.CorpusFactory;
import finalproject.index.IndexerThread;
import finalproject.index.spimi.SPIMIReconciliation;
import finalproject.technicalservices.BenchmarkRow;
import finalproject.technicalservices.Property;

public class ConcreteCrawler extends Crawler {

	private static HashMap<String, Integer> domainCrawlMap = new HashMap<String, Integer>();
	private IndexerThread indexer;
	private static Set<BigInteger> alreadyVisitedDocumentSet = Collections.synchronizedSet(new HashSet<BigInteger>());

	/**
	 * 
	 */
	private static final long serialVersionUID = 7121483632480699156L;
	// This simple crawler visits a certain university department
	// and lists home-page URLs of the academic faculty.  All the
	// homepage URLs are assumed to obey the following pattern:
	static Pattern concordiaDomain = 
			new Wildcard("http://*cs.concordia.ca/*");

	public ConcreteCrawler(Link root) {
		super();


		CorpusFactory.getCorpus(true);
		indexer = new IndexerThread("Web indexer");
		indexer.start();
		DownloadParameters dp = super.getDownloadParameters();
		dp = dp.changeMaxThreads(40);
		super.setDownloadParameters(dp);
		super.addRoot(root);
	}

	public void concludeCrawl() {
		System.out.println("Done crawling, waiting for the worker to finish downloading.");
		while (this.getActiveThreads() > 0)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		System.out.println("Done downloading " + pageNumber + " pages, found " + duplicateCount + " duplicates. waiting for indexing to finish");
		IndexerThread.signalNoMoreDocumentsAreExpected();
		try {
			indexer.join();
			System.out.println("Done indexing");
			SPIMIReconciliation.reconciliate();
			System.out.println("Done reconciling");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Called by sphynx everytime a url is discovered
	 */
	@Override
	public boolean shouldVisit (Link link) {

		//Probably a cached page we already indexed anyway..
		if (link.getURL().toString().split("http://").length > 2)
			return false;

		if (concordiaDomain.found(link.getURL().toString()) == false)
			return false;

		String domainName = link.getHost();
		//If this the first time we see this domain?
		if (domainCrawlMap.containsKey(domainName)) {
			Integer i = domainCrawlMap.get(domainName);
			link.setPriority(i++);
			domainCrawlMap.put(domainName, i);
		} else {
			System.out.println(domainCrawlMap.size() + " " + domainName);
			domainCrawlMap.put(domainName, 1);
			link.setPriority(0);
		}

		link.discardContent();
		return true;
	}


	private static Integer pageNumber = 0;
	private static Integer duplicateCount =0;
	@Override
	/*
	 * (non-Javadoc)
	 * @see websphinx.Crawler#visit(websphinx.Page)
	 */
	public void visit (Page page) {
		if ((concordiaDomain.found(page.getURL().toString()))) {
			int currentPage;

			try {
				WebDocument doc = getDocumentFromPage(page);
				if (alreadyVisitedDocumentSet.contains(doc.getDigest())) {
					synchronized(duplicateCount) { duplicateCount++; }
				}
				else {
					alreadyVisitedDocumentSet.add(doc.getDigest());
					if (doc.getText().length() > 0) {
						synchronized(pageNumber) {
							currentPage = pageNumber++;
						}
						if (currentPage%1000 == 0)
							System.out.println("I am at page " + currentPage);
						doc.setId(currentPage);
						savePageToDisk(page, Property.get("basepath") + "/data/" + currentPage);
						IndexerThread.addDocument(doc);
					}

				}
			} catch (CannotGetParsedDocumentException e) {
				e.printStackTrace();
			}
		}
		page.getOrigin().setPage(null);
		page.discardContent();

	}

	private WebDocument getDocumentFromPage(Page page) throws CannotGetParsedDocumentException {
		String text = null;
		String title = null;

		if (page.isHTML()) {
			StringBuilder sb = new StringBuilder();
			Text[] words = page.getWords();
			if (words == null)
				throw new CannotGetParsedDocumentException();
			for (websphinx.Text t : words) {
				sb.append(t.toText() + " ");
			}
			text = sb.toString();
			title = page.getTitle();
			if (title == null || title.length() == 0 )
				title = "Unknown title";
			return new WebDocument(0, title, text, page.getURL().toString());
		}
		else {
			return WebDocumentFactory.getDocument(page);
		}


	}

	private void savePageToDisk(Page page, String saveTo) {
		try {
			OutputStream out = new FileOutputStream(saveTo);
			out.write( page.getContentBytes());
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Can't get a hold of " + saveTo + ". Abording");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BenchmarkRow bench = new BenchmarkRow("Crawling+Indexing+Computing TF-IDF");
		bench.start();
		ConcreteCrawler crawler;
		try {
			BenchmarkRow br = new BenchmarkRow("total crawl+index time");
			br.start();
			crawler = new ConcreteCrawler(new Link("http://users.encs.concordia.ca/~comp479_2"));
			crawler.run();
			crawler.concludeCrawl();
			br.stop();
			System.out.println(br.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bench.stop();
		System.out.println(bench.toString());
		/*System.out.println("Starting checking for double");
		BufferedReader index;
		try {
			index = new BufferedReader(new InputStreamReader(new FileInputStream(new File("../crawler_result/articles.txt"))));
		TreeSet<String> alreadyKnown = new TreeSet<String>();
		String currentLine;
		int i = 0;
			while ((currentLine = index.readLine()) != null){
				if (Integer.valueOf(currentLine.split("#")[0])%500==0){
					System.out.println(currentLine.split("#")[0]);
				}
				if (alreadyKnown.contains(currentLine.split("#")[4])){
					System.out.println("Found Doubles : "+currentLine.split("#")[4]);
				} else{
					alreadyKnown.add(currentLine.split("#")[4]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private class CannotGetParsedDocumentException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4119639557594499917L;
		public CannotGetParsedDocumentException() {
			super();
		}
	}
}
