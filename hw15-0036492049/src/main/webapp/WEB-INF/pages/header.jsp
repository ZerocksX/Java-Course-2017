<%@ page import="hr.fer.zemris.java.model.BlogUser" %><%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 15.06.17.
  Time: 20:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<a href="${pageContext.request.contextPath}/">Home</a>
<%
    out.write(" ");
    BlogUser principal = (BlogUser) session.getAttribute("principal");
    if (principal == null) {
        out.write("Not logged in");
    } else {
        out.write("Hello, <a href=" + request.getContextPath() + "/servleti/author/" + principal.getNick() + ">" + principal.getNick() + "</a>");
        out.write(" <a href=" + request.getContextPath() + "/servleti/logout" + ">Logout</a>");
    }
    out.write("<br/>");

%>
