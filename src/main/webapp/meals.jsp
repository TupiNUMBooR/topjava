<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Users</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table class="table table-bordered">
    <thead class="thead-light">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2"></th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
    <c:forEach var="meal" items="${meals}">
        <tr class="table-${meal.excess ? "danger" : "success"}">
            <td>${meal.dateTime.format(dateTimeFormatter)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a class="btn btn-sm btn-outline-primary" href="meals?action=update&id=<c:out value="${meal.id}"/>">Update</a>
            </td>
            <td><a class="btn btn-sm btn-outline-danger" href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a>
            </td>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="3"></td>
        <td><a class="btn btn-sm btn-outline-success" href="meals?action=add">Add Meal</a></td>
    </tr>
    </tbody>
</table>
</body>
</html>