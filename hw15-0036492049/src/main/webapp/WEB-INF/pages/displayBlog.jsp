<%@ page import="hr.fer.zemris.java.model.BlogEntry" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<html>
<body>

<jsp:include page="header.jsp"/>
<c:choose>
    <c:when test="${blogEntry==null}">
        Nema unosa!
    </c:when>
    <c:otherwise>
        <h1><c:out value="${blogEntry.title}"/></h1>
        <p>${fn:replace(blogEntry.text,newLineChar ,"<br/>" )}
        </p>
        <c:if test="${!blogEntry.comments.isEmpty()}">
            <ul>
                <c:forEach var="e" items="${blogEntry.comments}">
                    <li>
                        <div style="font-weight: bold">[User=<c:out value="${e.usersEMail}"/>] <c:out
                                value="${e.postedOn}"/></div>
                        <div style="padding-left: 10px;">${fn:replace(e.message, newLineChar,"<br/>" )}</div>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </c:otherwise>
</c:choose>

<c:if test="${editAllowed}"><a
        href="${pageContext.request.contextPath}/servleti/author/${blogEntry.creator.nick}/edit?id=${blogEntry.id}">Uredi</a></c:if>


<form action="${pageContext.request.contextPath}/servleti/addComment?id=${blogEntry.id}" method="post">
    Message: <textarea name="message" id="" cols="30" rows="10"></textarea>
    <input type="submit" value="post">
</form>
</body>
</html>