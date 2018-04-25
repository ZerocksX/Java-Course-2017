<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 30.05.17.
  Time: 23:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Glasanje - Index</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<h1>${poll.title}</h1>
<p>${poll.message}</p>
<ol>
    <c:forEach var="pollOption" items="${pollOptions}">
        <li><a href="glasanje-glasaj?id=${pollOption.id}">${pollOption.optionTitle}</a></li>
    </c:forEach>
</ol>
<a href="${pageContext.request.contextPath}/servleti/glasanje-rezultati?pollID=${poll.id}">Rezultati</a>
</body>
</html>
