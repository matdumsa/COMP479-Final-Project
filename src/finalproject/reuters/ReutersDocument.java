package finalproject.reuters;

import finalproject.GenericDocument;
import finalproject.index.DiskReaderThread;

public class ReutersDocument extends GenericDocument {

	
	public ReutersDocument(int id, String title, String text) {
		super(id, title);
		super.setText(text);
	}
	

	@Override
	public String getText() {
		if (text != null) return text;
		//Make article act as a proxy here
		return DiskReaderThread.getArticleById(getId()).getText();
	}

}
