<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Update meal</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<div class="container-fluid">
    <form method="POST" action="meals" name="updateMeal">
        <%--    <jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>--%>
        <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
        <p><label>ID: <input type="number" name="id" value="${meal.id}" readonly="readonly"></label></p>
        <p><label>Time: <input type="datetime-local" name="dateTime" value="${meal.dateTime}" step="any"></label></p>
        <p><label>Description: <input type="text" name="description" value="${meal.description}"></label></p>
        <p><label>Calories: <input type="number" name="calories" value="${meal.calories}"></label></p>
        <jsp:useBean id="action" scope="request" type="java.lang.String"/>
        <p><input type="hidden" name="action" value="${action}"></p>
        <p><input type="submit" value="Submit" class="btn btn-outline-primary"/></p>
    </form>
</div>
</body>
</html>