<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 27.05.17.
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Trigonometric</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<table border="1">
    <c:forEach var="acs" items="${trigonometricList}">
        <tr>
            <td>${acs.angle}</td>
            <td>${acs.cos}</td>
            <td>${acs.sin}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
