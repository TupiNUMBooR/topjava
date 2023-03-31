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
            <form id="filter">
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
                <button class="btn btn-outline-primary" onclick="filterTable()">
                    <span class="fa fa-filter"></span> <spring:message code="meal.filter"/></button>
                <button class="btn btn-outline-danger" onclick="unfilterTable()">
                    <spring:message code="common.cancel"/></button>
            </div>
        </div>

        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th><spring:message code="meal.dateTime"/></th>
                <th><spring:message code="meal.description"/></th>
                <th><spring:message code="meal.calories"/></th>
                <th>
                    <button class="btn btn-outline-success add" onclick="add()">
                        <span class="fa fa-plus"></span>
                    </button>
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

<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input type="datetime-local" class="form-control" id="dateTime" name="dateTime"
                               placeholder="<spring:message code="meal.dateTime"/>">
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label">
                            <spring:message code="meal.description"/></label>
                        <input type="text" class="form-control" id="description" name="description"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories"
                               placeholder="<spring:message code="meal.calories"/>">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>