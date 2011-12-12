<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="finalproject.WebDocument,finalproject.GenericDocument,java.util.List,finalproject.queryprocessor.QueryProcessor,finalproject.technicalservices.BenchmarkRow"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	
	<title>Search results for ${query}></title>
	<meta name="description" content="">
	<meta name="author" content="">
	
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<link rel="shortcut icon" href="/COMp479-Final-Project/static/favicon.ico">
	<link rel="apple-touch-icon" href="/COMp479-Final-Project/static/apple-touch-icon.png">
	<link rel="stylesheet" href="/COMp479-Final-Project/static/css/style.css?v=2">
</head>
<body>
	<div id="container">
        <div id="contact-form" class="clearfix">
            <h1>Amazing Search Engines</h1>
            <jsp:include page="searchForm.jsp" />
			<c:choose>
				<c:when test="${resultcount == 0}">
					<h2>
						Sorry, no results have been found for ${query}
					</h2>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			For Query : ${resultset.userInputQuery}
			<br/>
			<br/>
			<c:choose>
					<c:when test="${resultset.suggestedQuery != null}">
					<h2>
						<font size="3" color="red">Did you mean ?</font> <font size="3">${resultset.suggestedQuery }</font>
					</h2>
					</c:when>
			 </c:choose>
			I found ${resultcount} results in ${timetomatch} ms.
		</div>

		<center>
		<div>
			<c:if test="${cluster != null && cluster.lastClusteringMoment > 0}">
				<c:forEach var="cluster" items="${cluster.clusterList }">
					<label class="inline"><input type="checkbox"
						id="${cluster.name }" class="clusterCheck" checked style="" />
						${cluster.name }</label>
				</c:forEach>
				<button id="toggleCheck">toggle</button>
			</c:if>
		</div>
		</center>

		<%
			int i = 1;
		%>
	<c:forEach var="r" items="${resultset.results}">
				<div id="box" class="box ${r.document.cluster.name }">
					<h1> 
						<%=i%> - <fmt:formatNumber type="number" maxIntegerDigits="3" maxFractionDigits="2" value="${r.rank}"/> - <a href="${r.document.url}"> ${r.document.title} </a>
						<c:if test="${ r.document.cluster != null}">
							<small>found in ${r.document.cluster.name }</small>
						</c:if>
					</h1><br/> 
					${r.document.url}<hr>
					${r.document.text}
				</div>
				<%
					i++;
				%>
	</c:forEach>
	</div>
	
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.min.js"></script>
	<script src="/COMp479-Final-Project/static/js/plugins.js"></script>
	<script src="/COMp479-Final-Project/static/js/script.js"></script>
	<!--[if lt IE 7 ]>
	<script src="js/libs/dd_belatedpng.js"></script>
	<script> DD_belatedPNG.fix('img, .png_bg');</script>
	<![endif]-->
</body>
</html>