<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="finalproject.WebDocument,finalproject.GenericDocument,java.util.List,finalproject.queryprocessor.QueryProcessor,finalproject.technicalservices.BenchmarkRow"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search results for ${query}></title>
</head>
<body>
	<jsp:include page="searchForm.jsp" />
	<% int i = 1; %>
	<c:forEach var="cluster" items="${clusters.clusterList}">
		<h2> ${cluster.name} (${cluster.size} documents)</h2>
		<c:forEach var="document" items="${cluster.closestTen }">
				<h3>
					<a href="${document.url}"> <font size="3">${document.title}</font> </a><br/> <font size="2">(${document.url})</font>
				</h3>
		</c:forEach>
		<%i++; %>
	</c:forEach>
</body>
</html>