<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 28.05.17.
  Time: 00:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Title</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<%
    long startTime = (Long) request.getServletContext().getAttribute("startTime");
    long currentTime = System.currentTimeMillis();
    out.write(
            String.format(
                    "%d days, %d hours, %d minutes, %d seconds and %d milliseconds%n",
                    (currentTime - startTime) / (24 * 60 * 60 * 1000),
                    (currentTime - startTime) % (24 * 60 * 60 * 1000) / (60 * 60 * 1000),
                    (currentTime - startTime) % (60 * 60 * 1000) / (60 * 1000),
                    (currentTime - startTime) % (60 * 1000) / 1000,
                    (currentTime - startTime) % 1000
            )
    );%>
</body>
</html>
