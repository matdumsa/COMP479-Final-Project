package ca.concordia.comp479.application;

import info.mathieusavard.application.console.InteractiveQuery;
import info.mathieusavard.domain.WebDocument;
import info.mathieusavard.domain.corpus.Corpus;

public class Console {

	public static void main(String[] args) {
		//Telling the corpus class how-to create new documents
		Corpus.setNewDocumentFactory(WebDocument.class);

		InteractiveQuery.main(args);
	}
}
