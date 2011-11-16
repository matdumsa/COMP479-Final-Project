package ca.concordia.comp479.crawling;

import info.mathieusavard.domain.Corpus;
import info.mathieusavard.domain.index.IndexerThread;
import info.mathieusavard.domain.index.spimi.SPIMIReconciliation;
import ca.concordia.comp479.indexation.WebDocument;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import websphinx.Crawler;
import websphinx.DownloadParameters;
import websphinx.Link;
import websphinx.Page;
import websphinx.Pattern;
import websphinx.Wildcard;

public class ConcreteCrawler extends Crawler {

	private static HashMap<String, Integer> domainCrawlMap = new HashMap<String, Integer>();
	private IndexerThread indexer;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7121483632480699156L;
	// This simple crawler visits a certain university department
	// and lists home-page URLs of the academic faculty.  All the
	// homepage URLs are assumed to obey the following pattern:
	static Pattern concordiaDomain = 
			new Wildcard ("http://*cs.concordia.ca/*");

	public ConcreteCrawler(Link root) {
		super();
		//Telling the corpus class how-to create new documents
		Corpus.setNewDocumentFactory(WebDocument.class);

		indexer = new IndexerThread("Web indexer");
		indexer.start();
		DownloadParameters dp = super.getDownloadParameters();
		dp = dp.changeMaxThreads(40);
		super.setDownloadParameters(dp);
		super.addRoot(root);
	}

	public void concludeCrawl() {
		System.out.println("Donw crawling, waiting for the worker to finish downloading.");
		while (this.getActiveThreads() > 0)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		System.out.println("Done downloading, waiting for indexing to finish");
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
	@Override
	/*
	 * (non-Javadoc)
	 * @see websphinx.Crawler#visit(websphinx.Page)
	 */
	public void visit (Page page) {
		synchronized(pageNumber) {
			pageNumber++;
		}
		if (pageNumber%1000 == 0)
			System.out.println("I am at page " + pageNumber);

		savePageToDisk(page);

		WebDocument doc = new WebDocument(pageNumber, page.getTitle(), getWordsOnly(page), page.getURL().toString());
		IndexerThread.addDocument(doc);
		
		page.getOrigin().setPage(null);
		page.discardContent();

	}
	
	private String getWordsOnly(Page page) {
		StringBuilder sb = new StringBuilder();
		for (websphinx.Text t : page.getWords()) {
			sb.append(t.toText() + " ");
		}
		return sb.toString();
	}
	
	private void savePageToDisk(Page page) {
		try {
			int currentPage;
			synchronized(pageNumber) {
				currentPage = pageNumber;
			}
			OutputStream out = new FileOutputStream("../crawler_result/doc/" + currentPage);
			out.write( page.getContentBytes());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ConcreteCrawler crawler;
		try {
			crawler = new ConcreteCrawler(new Link("http://users.encs.concordia.ca/~c479_2/"));
			crawler.run();
			crawler.concludeCrawl();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}