<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 06.06.17.
  Time: 23:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Glasanje</title>
</head>
<body>
<h1>Opcije</h1>
<ol>

    <c:forEach var="poll" items="${polls}">
        <li>
                ${poll.title} <a href="${pageContext.request.contextPath}/servleti/glasanje?pollID=${poll.id}">${poll.message}</a>
        </li>
    </c:forEach>
</ol>

</body>
</html>
