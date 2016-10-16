<%--
  Created by IntelliJ IDEA.
  User: asnju
  Date: 2016/9/16
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/main.css"/>
    <title>Add Product Form</title>
</head>
<body>

<div id="global">
    <p>
        <%
            response.getWriter().println(request.getContextPath());
            response.getWriter().println(request.getPathInfo());
            response.getWriter().println(request.getPathTranslated());
            response.getWriter().println(request.getServletPath());
        %>
    </p>
    <form action="save" method="post">
        <fieldset>
            <legend>Add a product</legend>
            <p>
                <label for="name">Product Name:</label>
                <input type="text" id="name" name="name" tabindex="1">
            </p>
            <p>
                <label for="description">Description: </label>
                <input type="text" id="description" name="description" tabindex="2">
            </p>
            <p>
                <label for="price">Price: </label>
                <input type="text" id="price" name="price" tabindex="3">
            </p>
            <p id="buttons">
                <input id="reset" type="reset" tabindex="4">
                <input id="submit" type="submit" tabindex="5" value="Add Product">
            </p>
        </fieldset>
    </form>
</div>

</body>
</html>
