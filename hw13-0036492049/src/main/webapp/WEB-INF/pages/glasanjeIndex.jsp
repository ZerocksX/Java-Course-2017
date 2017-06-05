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
<h1>Glasanje za omiljeni bend:</h1>
<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste
    glasali!</p>
<ol>
    <c:forEach var="band" items="${bands}">
        <li><a href="glasanje-glasaj?id=${band.id}">${band.name}</a></li>
    </c:forEach>
</ol>
</body>
</html>
