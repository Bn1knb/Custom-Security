<%--
  Created by IntelliJ IDEA.
  User: Anton_Kamisarau
  Date: 9/20/2019
  Time: 6:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<form method="POST" action="login">
    <label> Username <br>
        <input type="text" name="username">
    </label><br>
    <label> Password <br>
        <input type="password" name="password">
    </label> <br>
    <input type="submit" value="login">
</form>
<form  method="get" action="register">
    <input type="submit" value="register">
</form>
<p>${error}</p>
</body>
</html>
