package ca.concordia.comp479.crawling;

import info.mathieusavard.domain.WebDocument;
import info.mathieusavard.domain.WeightedDocument;
import info.mathieusavard.domain.corpus.WeightedCorpus;
import info.mathieusavard.domain.index.IndexerThread;
import info.mathieusavard.domain.index.spimi.SPIMIReconciliation;
import info.mathieusavard.technicalservices.BenchmarkRow;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import websphinx.Crawler;
import websphinx.DownloadParameters;
import websphinx.Link;
import websphinx.Page;
import websphinx.Pattern;
import websphinx.Text;
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
			new Wildcard ("http://encs.concordia.ca/*");

	public ConcreteCrawler(Link root) {
		super();
		//Telling the corpus class how-to create new documents
		WeightedCorpus.setNewDocumentFactory(WeightedDocument.class);

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

		System.out.println("Done downloading " + pageNumber + " pages, waiting for indexing to finish");
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
		String contentType = page.getContentType();
		if ((page.isHTML() || contentType.equalsIgnoreCase("application/pdf") || contentType.equalsIgnoreCase("application/x-pdf")) && concordiaDomain.found(page.getURL().toString())) {
			int currentPage;
			synchronized(pageNumber) {
				currentPage = pageNumber++;
			}
			if (currentPage%1000 == 0)
				System.out.println("I am at page " + currentPage);

			try {
				savePageToDisk(page, "../crawler_result/doc/" + currentPage);
				WebDocument doc = getDocumentFromPage(page, currentPage);
				IndexerThread.addDocument(doc);
			} catch (CannotGetParsedDocumentException e) {
				e.printStackTrace();
			}
		}
		page.getOrigin().setPage(null);
		page.discardContent();

	}

	private WebDocument getDocumentFromPage(Page page, int id) throws CannotGetParsedDocumentException {
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
		}
		else if (page.getContentType().equalsIgnoreCase("application/pdf") || page.getContentType().equalsIgnoreCase("application/x-pdf")) {

			PDFParser parser;
			COSDocument cosDoc = null;
			PDFTextStripper pdfStripper;
			PDDocument pdDoc = null;
			try {
				parser = new PDFParser( new ByteArrayInputStream(page.getContentBytes()));
				parser.parse();
				cosDoc = parser.getDocument();
				pdfStripper = new PDFTextStripper();
				pdDoc = new PDDocument(cosDoc);
				title = pdDoc.getDocumentInformation().getTitle();
				text = pdfStripper.getText(pdDoc);
			} catch (IOException e) {
				throw new CannotGetParsedDocumentException(e);
			} finally {
				try {
					if (pdDoc != null) pdDoc.close();
					if (cosDoc != null) cosDoc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		if (title == null || title.length() == 0 )
			title = "Unknown title";
		return new WebDocument(id, title, text, page.getURL().toString());


	}

	private void savePageToDisk(Page page, String saveTo) {
		try {
			OutputStream out = new FileOutputStream(saveTo);
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
		BenchmarkRow bench = new BenchmarkRow("Crawling+Indexing+Computing TF-IDF");
		bench.start();
		ConcreteCrawler crawler;
		try {
			crawler = new ConcreteCrawler(new Link("http://encs.concordia.ca/"));
			crawler.run();
			crawler.concludeCrawl();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bench.stop();
		System.out.println(bench.toString());
	}

	private class CannotGetParsedDocumentException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4119639557594499917L;
		public CannotGetParsedDocumentException(Exception e) {
			super(e);
		}
		public CannotGetParsedDocumentException() {
			super();
		}
	}
}
