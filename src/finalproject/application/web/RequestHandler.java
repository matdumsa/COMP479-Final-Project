package finalproject.application.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import finalproject.WebDocument;
import finalproject.corpus.Corpus;

/**
 * Servlet implementation class RequestHandler
 */
public class RequestHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	static {
		Corpus.setNewDocumentFactory(WebDocument.class);
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestHandler() {
        super();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		executeRequest(request, response);
	}

	private void executeRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //Read the url requested and remove the servlet part of it (I am the servlet).
		String requestedURI = request.getServletPath();
        
		try {
			Command c = CommandFactory.createCommand(requestedURI);
			c.execute(request, response);
		} catch (finalproject.application.web.CommandFactory.IllegalURIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		executeRequest(request, response);
	}


}
