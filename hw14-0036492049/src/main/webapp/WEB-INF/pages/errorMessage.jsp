<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 29.05.17.
  Time: 17:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
${errorMessage}
</body>
</html>
