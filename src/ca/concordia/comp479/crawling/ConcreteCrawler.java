package ca.concordia.comp479.crawling;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 7121483632480699156L;
	// This simple crawler visits a certain university department
	// and lists home-page URLs of the academic faculty.  All the
	// homepage URLs are assumed to obey the following pattern:
	static Pattern concordiaDomain = 
			new Wildcard ("http://*.concordia.ca/*");

	public ConcreteCrawler(Link root) {
		super();

		DownloadParameters dp = super.getDownloadParameters();
		dp = dp.changeMaxThreads(50);
		super.setDownloadParameters(dp);
		super.addRoot(root);
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
			domainCrawlMap.put(domainName, 1);
			link.setPriority(0);
			System.out.println(domainCrawlMap.size() + "-" + domainName);
		}
		
		return true;
	}


	private static Integer pageNumber = 0;

	@Override
	/*
	 * (non-Javadoc)
	 * @see websphinx.Crawler#visit(websphinx.Page)
	 */
	public void visit (Page page) {
//		try {
//			int currentPage;
//			synchronized(pageNumber) {
//				currentPage = ++pageNumber;
//			}
//			OutputStream out = new FileOutputStream("data/" + currentPage);
//			out.write( page.getContentBytes());
//			out.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println ( Thread.currentThread().getName() + ":"+ page.getURL () ); 
//		page.discardContent();

	}

	public static void main(String[] args) {
		ConcreteCrawler crawler;
		try {
			crawler = new ConcreteCrawler(new Link("http://www.concordia.ca"));
			crawler.run();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}