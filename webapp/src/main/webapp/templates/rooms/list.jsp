<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout>
    <jsp:attribute name="title">Rooms</jsp:attribute>
    <jsp:body>
        <div class="card">
            <div class="card-block">
                <h4 class="card-title">Add room</h4>
                <div class="card-text">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            <c:out value="${error}"/>
                        </div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/rooms/add" method="post">
                        <div class="form-group">
                            <label for="numberInput">Room number</label>
                            <input type="number" id="numberInput" class="form-control" name="number" value="<c:out value='${param.number}'/>">
                        </div>
                        <div class="form-group">
                            <label for="capacityInput">Capacity</label>
                            <input type="number" id="capacityInput" class="form-control" name="capacity" value="<c:out value='${param.capacity}'/>"></td>
                        </div>
                        <div class="form-group">
                            <label for="floorInput">Floor</label>
                            <input type="number" id="floorInput" class="form-control" name="floor" value="<c:out value='${param.floor}'/>"></td>
                        </div>
                        <div class="form-group">
                            <label for="pricePerDayInput">Price/Day:</label>
                            <input type="number" id="pricePerDayInput" class="form-control" name="pricePerDay" value="<c:out value='${param.pricePerDay}'/>"></td>
                        </div>
                        <input type="Submit" value="Add" class="btn btn-primary">
                    </form>
                </div>
            </div>
        </div>

        <hr>
        <c:if test="${empty rooms}">
            <div class="alert alert-info">There are no rooms yet</div>
        </c:if>
        <c:if test="${not empty rooms}">
            <table class="table">
                <thead>
                <tr>
                    <th>Number</th>
                    <th>Capacity</th>
                    <th>Floor</th>
                    <th>Price / day</th>
                    <th>&nbsp;</th>
                </tr>
                </thead>
                <c:forEach items="${rooms}" var="room">
                    <tr>
                        <td><c:out value="${room.number}"/></td>
                        <td><c:out value="${room.capacity}"/></td>
                        <td><c:out value="${room.floor}"/></td>
                        <td><c:out value="${room.pricePerDay}"/></td>
                        <td>
                            <c:if test="${reservations[room.id] == 0}">
                                <form method="post" action="${pageContext.request.contextPath}/rooms/delete?id=${room.id}" class="form-inline">
                                    <input class="btn btn-danger" type="submit" value="Delete">
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </jsp:body>
</t:layout>