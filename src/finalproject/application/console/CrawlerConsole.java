package finalproject.application.console;

import java.text.DecimalFormat;

import finalproject.WebDocument;
import finalproject.queryprocessor.RankedResult;
import finalproject.queryprocessor.Result;

public class CrawlerConsole {

	public static void main(String[] args) {

		InteractiveQuery i = new InteractiveQuery();
		i.setResultsPrinter(new ResultsPrinter() {

			@Override
			public void printResult(Result r) {
				WebDocument wd = (WebDocument) r.getDocument();
				if (r instanceof RankedResult){
					DecimalFormat df = new DecimalFormat("0.00000000");
					RankedResult rR = (RankedResult)r;
					System.out.print(rR.getDocument().getId() + " - " + df.format(rR.getRank()) + " - " + wd.getUrl());
					System.out.println("\t" + rR.getDocument().getTitle());
				} else {
					System.out.print(r.getDocument().getId() + " - " + wd.getUrl());
					System.out.println("\t" + r.getDocument().getTitle());
					}					
			}
			
		});
		
		i.run();
	}
}
