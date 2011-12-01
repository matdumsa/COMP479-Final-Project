package ca.concordia.comp479.application;

import info.mathieusavard.application.console.InteractiveQuery;
import info.mathieusavard.application.console.ResultsPrinter;
import info.mathieusavard.domain.WebDocument;
import info.mathieusavard.domain.WeightedDocument;
import info.mathieusavard.domain.corpus.Corpus;
import info.mathieusavard.domain.queryprocessor.RankedResult;
import info.mathieusavard.domain.queryprocessor.Result;

public class Console {

	public static void main(String[] args) {
		//Telling the corpus class how-to create new documents
		Corpus.setNewDocumentFactory(WebDocument.class);

		InteractiveQuery i = new InteractiveQuery();
		i.setResultsPrinter(new ResultsPrinter() {

			@Override
			public void printResult(Result r) {
				WebDocument wd = (WebDocument) r.getResult();
				if (r instanceof RankedResult){
					RankedResult rR = (RankedResult)r;
					System.out.print(rR.getResult().getId() + " - " + rR.getRank() + " - " + wd.getUrl());
					System.out.println("\t" + rR.getResult().getTitle());
				} else {
					System.out.print(r.getResult().getId() + " - " + wd.getUrl());
					System.out.println("\t" + r.getResult().getTitle());
					}					
			}
			
		});
		
		i.run();
	}
}
