<%--
  Created by IntelliJ IDEA.
  User: Anton_Kamisarau
  Date: 10/7/2019
  Time: 5:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>LoginPage</title>
</head>
<body>
<form method="post" action="http://localhost:80/proxy/auth/login">
    <label> username:
        <input type="text" name="username">
    </label>
    <label> password:
        <input type="password" name="password">
    </label>
    <input type="submit" value="login">
</form>
</body>
</html>
