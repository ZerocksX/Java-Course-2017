<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 27.05.17.
  Time: 22:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Title</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<h1>OS usage</h1>
<p>Here are the results of OS usage in survey that we completed</p>
<img src="${pageContext.request.contextPath}/reportImage">

</body>
</html>
