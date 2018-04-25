<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 15.06.17.
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome to the BLOG</title>
</head>
<body>

<jsp:include page="header.jsp"/>
<c:if test="${login!=null}}">${login.equals("failed")? "Invalid nick or password" :""}</c:if>
<form action="${pageContext.request.contextPath}/servleti/login" method="post">
    Nick: <input type="text" name="nick" value=""><br>
    Password: <input type="password" name="password" value=""><br>
    <input type="submit" value="Login"><br>
</form>
Or <a href="${pageContext.request.contextPath}/servleti/register">register</a><br/>
<jsp:include page="displayAuthors.jsp"/>
</body>
</html>
