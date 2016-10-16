<%--
  Created by IntelliJ IDEA.
  User: asnju
  Date: 2016/9/16
  Time: 21:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Add Book Form</title>
</head>
<body>

<div id="global">
    <form:form commandName="book" action="save" method="post">
        <fieldset>
            <legend>Add a book</legend>
            <p>
                <label for="category">Category: </label>
                <form:select id="category" path="category.id"
                             items="${categories}" itemLabel="name"
                             itemValue="id"/>
            </p>
            <p>
                <label for="title">Title: </label>
                <form:input id="title" path="title"/>
            </p>
            <p>
                <label for="author">Author: </label>
                <form:input id="author" path="author"/>
            </p>
            <p>
                <label for="isbn">ISBN: </label>
                <form:input id="isbn" path="isbn"/>
            </p>

            <p id="buttons">
                <input id="reset" type="reset" tabindex="4">
                <input id="submit" type="submit" tabindex="5"
                       value="Add Book">
            </p>
        </fieldset>
    </form:form>
</div>

</body>
</html>
