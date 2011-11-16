package ca.concordia.comp479.application;

import ca.concordia.comp479.indexation.WebDocument;
import info.mathieusavard.application.console.InteractiveQuery;
import info.mathieusavard.domain.Corpus;

public class Console {

	public static void main(String[] args) {
		//Telling the corpus class how-to create new documents
		Corpus.setNewDocumentFactory(WebDocument.class);

		InteractiveQuery.main(args);
	}
}
