<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 15.06.17.
  Time: 20:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:choose>
    <c:when test="${authors==null}">
        No authors!<br/>
    </c:when>
    <c:otherwise>
        <h1>Authors</h1>
        <ul>
            <c:forEach var="a" items="${authors}">
                <li>${a.nick}: <a href="${pageContext.request.contextPath}/servleti/author/${a.nick}">blogs</a> </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>
