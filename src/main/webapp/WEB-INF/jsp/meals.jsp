<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<script src="resources/js/topjava.common.js" defer></script>
<script src="resources/js/topjava.meals.js" defer></script>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>
        <div class="card border-dark p-3">
            <form id="filter" action="meals/filter">
                <div class="d-flex justify-content-between">
                    <div>
                        <label><spring:message code="meal.startDate"/>
                            <input type="date" name="startDate" autocomplete="off">
                        </label>
                    </div>
                    <div>
                        <label><spring:message code="meal.endDate"/>
                            <input type="date" name="endDate" autocomplete="off">
                        </label>
                    </div>
                </div>
                <div class="d-flex justify-content-between">
                    <div>
                        <label><spring:message code="meal.startTime"/>
                            <input type="time" name="startTime" autocomplete="off">
                        </label>
                    </div>
                    <div>
                        <label><spring:message code="meal.endTime"/>
                            <input type="time" name="endTime" autocomplete="off">
                        </label>
                    </div>
                </div>
            </form>
            <div>
                <button class="btn btn-outline-primary" type="submit" form="filter">
                    <span class="fa fa-filter"></span> <spring:message code="meal.filter"/>
                </button>
                <a class="btn btn-outline-danger" href="meals"><spring:message code="common.cancel"/></a>
            </div>
        </div>

        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th><spring:message code="meal.dateTime"/></th>
                <th><spring:message code="meal.description"/></th>
                <th><spring:message code="meal.calories"/></th>
                <th>
                    <button class="btn btn-outline-success add"><span class="fa fa-plus"></span></button>
                </th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${requestScope.meals}" var="meal">
                <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                <tr id="${meal.id}" data-meal-excess="${meal.excess}">
                    <td>${fn:formatDateTime(meal.dateTime)}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td>
                        <button class="btn btn-outline-info edit"><span class="fa fa-pencil"></span></button>
                    </td>
                    <td>
                        <button class="btn btn-outline-danger delete"><span class="fa fa-remove"></span></button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>