<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 15.06.17.
  Time: 18:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Blog entry editor</title>
</head>
<body>

<jsp:include page="header.jsp"/>
<form action="${pageContext.request.contextPath}/servleti/author/${user.nick}/save?id=${blogEntry.id}"
      method="post">
    Title: <input type="text" name="title" value="${blogEntry.title}"><br/>
    Text: <textarea name="text" id="" cols="30" rows="10">${blogEntry.text}</textarea><br/>
    <input type="submit" value="Save">
</form>

</body>
</html>
