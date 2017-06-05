<%--
  User: pavao
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Webapp2</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<a href="colors.jsp">Background color chooser</a><br>
<a href="stories/funny.jsp">A funny story</a><br>
<a href="trigonometric?a=0&b=90">Cosine and sine values for integer angles between 0 and 90</a><br>
<a href="report.jsp">Dinamicaly created report of OS usage </a><br>
<a href="appinfo.jsp">Current app uptime</a><br><br>
Trigonometric table generator:
<form action="trigonometric" method="GET">
    Starting angle: <input type="number" name="a" min="0" max="360" step="1" value="0"><br>
    Ending angle: <input type="number" name="b" min="0" max="360" step="1" value="360"><br>
    <input type="submit" value="Table"><input type="reset" value="Reset">
</form>
<br>
<a href="powers?a=1&b=100&n=3">Powers XLS table for range of [1,100] for powers [1,3]</a><br>
<a href="glasanje">Glasanje</a><br>
<a href="glasanje-rezultati">Glasanje - rezultati</a><br>
</body>
</html>
