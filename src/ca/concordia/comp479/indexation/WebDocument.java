package ca.concordia.comp479.indexation;

import info.mathieusavard.domain.GenericDocument;

public class WebDocument extends GenericDocument {

	private String url;
	
	public WebDocument (int id, String title, String text, String url) {
		super(id, title);
		super.setText(text);
		this.url = url;
	}
	
	@Override
	public String toString() {
		return super.getId() + "#" + super.getLengthInWords() + "#" + super.getTitle() + "#" + url;
	}
	
	public static WebDocument fromString(String input) {
		String[] parts = input.split("#");
		int id = Integer.parseInt(parts[0]);
		int length = Integer.parseInt(parts[1]);
		String title = "???";
		if (parts.length > 2)
			title = parts[2].trim();
		String url = parts[3];
		WebDocument wd =  new WebDocument(id, title, null, url);
		wd.setLengthInWords(length);
		return wd;

	}
}
