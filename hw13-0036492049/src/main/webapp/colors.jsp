<%--
  User: pavao
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Background color chooser</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<a href="${pageContext.request.contextPath}/setcolor?color=WHITE">WHITE</a><br>
<a href="${pageContext.request.contextPath}/setcolor?color=RED">RED</a><br>
<a href="${pageContext.request.contextPath}/setcolor?color=GREEN">GREEN</a><br>
<a href="${pageContext.request.contextPath}/setcolor?color=CYAN">CYAN</a><br>
</body>
</html>
