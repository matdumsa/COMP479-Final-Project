<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="finalproject.WebDocument,finalproject.GenericDocument,java.util.List,finalproject.queryprocessor.QueryProcessor,finalproject.technicalservices.BenchmarkRow"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search results for ${query}></title>
</head>
<body>
	<jsp:include page="searchForm.jsp" />
	<c:choose>
		<c:when test="${resultcount == 0}">
			<h2>
				Sorry, no results have been found for ${query}
			</h2>
		</c:when>
		<c:otherwise>
	
	For Query : ${resultset.userInputQuery}
	</br>
	</br>
	<c:choose>
			<c:when test="${resultset.suggestedQuery != null}">
			<h2>
				<font size="3" color="red">Did you mean ?</font> <font size="3">${resultset.suggestedQuery }</font>
			</h2>
			</c:when>
	 </c:choose>
	I found ${resultcount} results in ${timetomatch} ms.
	<% int i = 1; 
	
	%>
	<c:forEach var="r" items="${resultset.results}">
				<h3>
					<%=i %> - <font size="3"> 
								<fmt:formatNumber type="number" maxIntegerDigits="3" maxFractionDigits="2" value="${r.rank}"/> - </font>
								<a href="${r.document.url}"> <font size="3">${r.document.title}</font> </a><br/> 
								<font size="2">(${r.document.url})</font><br/>
								<font size="2">(${r.document.text})</font>
				</h3>
				<%i++; %>
	</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>