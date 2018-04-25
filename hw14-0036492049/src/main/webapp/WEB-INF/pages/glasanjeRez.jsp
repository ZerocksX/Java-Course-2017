<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 30.05.17.
  Time: 23:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Glasanje - rezultati</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<h1>Rezultati glasanja</h1>
<p>Ovo su rezultati glasanja.</p>
<table border="1" cellspacing="0" class="rez">
    <thead>
    <tr>
        <th>Opcija</th>
        <th>Broj glasova</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="pollOption" items="${pollOptions}">
        <tr>
            <td>${pollOption.optionTitle}</td>
            <td>${pollOption.votesCount}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h2>Grafički prikaz rezultata</h2>
<img alt="Pie-chart" src="${pageContext.request.contextPath}/servleti/glasanje-grafika?pollID=${poll.id}" width="400" height="400"/>

<h2>Rezultati u XLS formatu</h2>
<p>Rezultati u XLS formatu dostupni su <a href="${pageContext.request.contextPath}/servleti/glasanje-xls?pollID=${poll.id}">ovdje</a></p>

<h2>Razno</h2>
<p>Primjeri pobjedničkih opcija:</p>
<ul>
    <c:forEach var="winningOption" items="${winningOptions}">
        <li><a href="${winningOption.optionLink}" target="_blank">${winningOption.optionTitle}</a></li>
    </c:forEach>
</ul>
</body>
</html>
