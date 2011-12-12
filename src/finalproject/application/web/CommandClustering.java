package finalproject.application.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import finalproject.corpus.ClusteredWeightedCorpus;
import finalproject.corpus.CorpusFactory;
import finalproject.queryprocessor.clustering.ResultCluster;

public class CommandClustering extends Command {

		@Override
		public void execute(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			
			System.out.println("Displaying Cluster");
			
			ClusteredWeightedCorpus corpus = (ClusteredWeightedCorpus) CorpusFactory.getCorpus();
			ResultCluster clusters = corpus.getClustering().createResultCluster();
			request.setAttribute("clusters", clusters);

			RequestDispatcher rd = request.getRequestDispatcher(super.getJSPPAth("cluster.jsp"));
			rd.forward(request, response);

		}


}
