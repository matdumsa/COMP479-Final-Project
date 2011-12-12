package finalproject.application.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import finalproject.corpus.ClusteredWeightedCorpus;
import finalproject.corpus.Corpus;
import finalproject.corpus.CorpusFactory;
import finalproject.queryprocessor.QueryProcessor;
import finalproject.queryprocessor.Result;
import finalproject.queryprocessor.ResultSet;
import finalproject.queryprocessor.booleantree.InvalidQueryException;

public class CommandSearch extends Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String query = request.getParameter("q");
		request.setAttribute("query", query);
		
		System.out.println("searching for " + query);
		ResultSet resultset = null;
		try {
			resultset = QueryProcessor.performQuery(query);
			
			for(Result r : resultset){
				r.getDocument().setText(Snippet.findsnippet(r.getDocument().getId(), resultset.getUserInputQuery()));

			}
		} catch (InvalidQueryException e) {
			throw new ServletException(e);
		}
		request.setAttribute("timetomatch", QueryProcessor.getMatchingTime());
		request.setAttribute("resultcount", resultset.size());
		request.setAttribute("resultset", resultset);
		
		Corpus corpus = CorpusFactory.getCorpus();
		if (corpus instanceof ClusteredWeightedCorpus)
			request.setAttribute("cluster", ((ClusteredWeightedCorpus) corpus).getClustering());
		
		RequestDispatcher rd = request.getRequestDispatcher(super.getJSPPAth("search.jsp"));
		rd.forward(request, response);

	}

}
