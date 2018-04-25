<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 15.06.17.
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>

<jsp:include page="header.jsp"/>
<form action="${pageContext.request.contextPath}/servleti/register" method="post">
    First Name: <input type="text" name="firstName" value="${invalidForm.firstName}"><c:if test="${flags[0]}">size(<20) or characters(only letters) are invalid</c:if><br/>
    Last Name:  <input type="text" name="lastName" value="${invalidForm.lastName}"><c:if test="${flags[1]}">size(<20) or characters(only letters) are invalid</c:if><br/>
    Email:      <input type="text" name="email" value="${invalidForm.email}"><c:if test="${flags[2]}">size(<50) is invalid</c:if><br/>
    Nick:       <input type="text" name="nick" value="${invalidForm.nick}"><c:if test="${flags[3]}">size(<20) or characters(only letters) are invalid, or nick is in use</c:if><br/>
    Password:   <input type="password" name="password"><c:if test="${flags[4]}">size(<10) is invalid</c:if><br/>
    <input type="submit" value="Register">
</form>
</body>
</html>
