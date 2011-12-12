package finalproject.crawler;

import finalproject.WebDocument;
import finalproject.application.console.InteractiveQuery;
import finalproject.application.console.ResultsPrinter;
import finalproject.queryprocessor.RankedResult;
import finalproject.queryprocessor.Result;

public class Console {

	public static void main(String[] args) {

		InteractiveQuery i = new InteractiveQuery();
		i.setResultsPrinter(new ResultsPrinter() {

			@Override
			public void printResult(Result r) {
				WebDocument wd = (WebDocument) r.getDocument();
				if (r instanceof RankedResult){
					RankedResult rR = (RankedResult)r;
					System.out.print(rR.getDocument().getId() + " - " + rR.getRank() + " - " + wd.getUrl());
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
