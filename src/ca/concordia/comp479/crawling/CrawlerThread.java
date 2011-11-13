package ca.concordia.comp479.crawling;

import java.util.Stack;

public class CrawlerThread extends Thread {

	private Stack<ConcreteCrawler> workToPerform = new Stack<ConcreteCrawler>();
	public CrawlerThread(ConcreteCrawler c) {
		workToPerform.push(c);		
	}
	
	@Override
	public void run() {
		while (true) {
			ConcreteCrawler nextTask = null;
			synchronized(workToPerform) {
				nextTask = workToPerform.pop();
			}
			if (nextTask==null)
				return;
			else
				nextTask.run();
		}
	}
	
	public static void addWork(ConcreteCrawler work) {

	}
}
