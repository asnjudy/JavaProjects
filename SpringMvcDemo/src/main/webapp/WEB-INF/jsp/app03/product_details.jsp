<%--
  Created by IntelliJ IDEA.
  User: asnju
  Date: 2016/9/16
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/main.css"/>
    <title>Save Product</title>
</head>
<body>

<div id="global">
    <h4>The product has been saved.</h4>
    <p>
        <h5>Details:</h5>
    Product Name: ${product.name}<br>
    Description: ${product.description}<br>
    Price: ${product.price}
    </p>

</div>
</body>
</html>
