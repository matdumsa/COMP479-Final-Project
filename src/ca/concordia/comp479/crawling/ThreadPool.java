package ca.concordia.comp479.crawling;

public class ThreadPool {

	private final static int  MAX_NUM_THREAD = 128;
	private static CrawlerThread[] threadArray = new CrawlerThread[MAX_NUM_THREAD];
	
	private static int nextThread = 0;
	public static synchronized CrawlerThread getThread(ConcreteCrawler c) {
		nextThread = nextThread%MAX_NUM_THREAD;
		if (threadArray[nextThread] == null) {
			threadArray[nextThread] = new CrawlerThread(c);
		}
		return threadArray[nextThread++];
	}
}
