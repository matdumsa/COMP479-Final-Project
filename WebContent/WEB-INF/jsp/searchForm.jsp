<form method="get" action="search">
	<input id="q" name="q" type="text" value="<%= (request.getParameter("q") != null) ? request.getParameter("q") : ""%>" autofocus />         
                <span id="loading"></span>
                <input type="submit" value="Search" id="submit-button" /><br/>
                <br/>
				<a href="http://localhost:8080/COMp479-Final-Project/cluster"> Clustering </a>	
            </form>

<hr/>
<br/>