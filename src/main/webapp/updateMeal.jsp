<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fs" uri="http://example.com" %>
<c:set var="title" scope="request" value='${param.action.equals("add") ? "Add meal" : "Edit meal"}'/>
<html lang="ru">
<head>
    <title>${title}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${title}</h2>
<form class="container-fluid" method="POST" action="meals" name="updateMeal">
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <p><label>Time: <input type="datetime-local" name="dateTime" value="${fs:formatHtmlInput(meal.dateTime)}" step="any"></label></p>
    <p><label>Description: <input type="text" name="description" value="${meal.description}"></label></p>
    <p><label>Calories: <input type="number" name="calories" value="${meal.calories}"></label></p>
    <c:if test="${meal.id != null}">
        <input type="hidden" name="id" value="${meal.id}">
    </c:if>
    <p><input type="submit" value="Submit" class="btn btn-outline-primary"/></p>
    <button class="btn btn-outline-danger" onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>