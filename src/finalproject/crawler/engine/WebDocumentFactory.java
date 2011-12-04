package finalproject.crawler.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

import websphinx.Page;
import finalproject.WebDocument;

public class WebDocumentFactory {

	private static Tika tika;
	
	private synchronized static Tika getTika() {
		if (tika == null)
			tika = new Tika();
		return tika;
	}
	
	public static WebDocument getDocument(Page page) {
		InputStream stream = new ByteArrayInputStream(page.getContentBytes());
		try {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.CONTENT_TYPE, page.getContentType());
			metadata.set(Metadata.CONTENT_LOCATION, page.getURL().toString());
			metadata.set(Metadata.LOCATION, page.getURL().toString());
			metadata.set(Metadata.MIME_TYPE_MAGIC, page.getContentType());
			metadata.set(Metadata.CONTENT_ENCODING, page.getContentEncoding());
			metadata.set(Metadata.TIKA_MIME_FILE, page.getContentType());
			
			String text = getTika().parseToString(stream, metadata);

		    WebDocument wd = new WebDocument(0, metadata.get(Metadata.TITLE), text, page.getURL().toString());
		    stream.close();
		    return wd;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            // close the stream
		}
		
		return null;

	}
}
