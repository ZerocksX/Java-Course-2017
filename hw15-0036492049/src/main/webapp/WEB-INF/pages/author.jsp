<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 15.06.17.
  Time: 15:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Author's page</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<c:choose>
    <c:when test="${user==null}">
        Nema unosa!
    </c:when>
    <c:otherwise>
        <h1><c:out value="${user.nick}"/></h1>
        <c:if test="${!user.blogEntries.isEmpty()}">
            <ul>
                <c:forEach var="e" items="${user.blogEntries}">
                    <li><a href="${pageContext.request.contextPath}/servleti/author/${user.nick}/${e.id}"> ${e.title}</a> ( ${e.lastModifiedAt} )</li>
                </c:forEach>
            </ul>
        </c:if>
    </c:otherwise>
</c:choose>

<c:if test="${editAllowed}"><a href="${pageContext.request.contextPath}/servleti/author/${user.nick}/new">New</a></c:if>

</body>
</html>
