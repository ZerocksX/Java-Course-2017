<%--
  Created by IntelliJ IDEA.
  User: pavao
  Date: 27.05.17.
  Time: 19:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<jsp:useBean id="random" class="hr.fer.zemris.java.web.random.RandomColor" scope="application"/>
<html>
<head>
    <title>A funny story</title>
</head>
<body bgcolor=${sessionScope.getOrDefault("pickedBgCol", "WHITE")}>
<h1>Title of the story</h1>
<p style="color:<%= random.randomColorHex() %>">
    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc rhoncus ultricies efficitur.
    Curabitur blandit vestibulum semper. Mauris fermentum, ligula sed sollicitudin gravida, ipsum velit
    vestibulum velit, id consequat libero risus sit amet diam.
    Proin semper tincidunt quam dignissim commodo. Quisque metus lectus, finibus sit amet augue vitae, sodales egestas
    quam.
    Aliquam sagittis euismod elit sit amet pulvinar. Suspendisse volutpat, nunc vitae egestas dictum,
    odio leo hendrerit lacus, eget tincidunt dolor dolor ut eros. Nulla sagittis nec mauris nec malesuada.
    Integer molestie eros dolor, a iaculis odio semper in. Quisque feugiat erat at enim posuere finibus.
    Donec elementum, tellus a eleifend bibendum, tortor felis accumsan felis, et eleifend diam nisl a velit.
    Morbi ultricies erat vel ipsum bibendum euismod. Vestibulum ac mi sed purus volutpat rhoncus. Quisque eget nisl at
    felis rutrum feugiat.</p>
</body>
</html>
