<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout>
    <jsp:attribute name="title">Reservations</jsp:attribute>
    <jsp:body>
        <div class="card">
            <div class="card-block">
                <h4 class="card-title">Place a reservation</h4>
                <div class="card-text">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            <c:out value="${error}"/>
                        </div>
                    </c:if>
                    <c:if test="${not empty customers and not empty rooms}">
                        <form action="${pageContext.request.contextPath}/reservations/add" method="post">
                            <div class="form-group">
                                <label for="roomId">Room</label>
                                <select name="roomId" id="roomId" class="form-control">
                                    <c:forEach items="${rooms}" var="room">
                                        <option value="<c:out value="${room.id}"/>">no <c:out value="${room.number}"/> (floor <c:out value="${room.floor}"/>)</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="customerId">Customer</label>
                                <select name="customerId" id="customerId" class="form-control">
                                    <c:forEach items="${customers}" var="customer">
                                        <option value="<c:out value="${customer.id}"/>"><c:out value="${customer.name}"/></option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="since">Since</label>
                                <input type="date" name="since" id="since" class="form-control">
                            </div>
                            <div class="form-group">
                                <label for="until">Until</label>
                                <input type="date" name="until" id="until" class="form-control">
                            </div>
                            <input type="Submit" value="Add" class="btn btn-primary">
                        </form>
                    </c:if>
                    <c:if test="${empty customers or empty rooms}">
                        <div class="alert alert-info">You need at least one registered customer and one room to make an reservation</div>
                    </c:if>
                </div>
            </div>
        </div>

        <hr>
        <c:if test="${empty reservations}">
            <div class="alert alert-info">There are no reservations yet</div>
        </c:if>
        <c:if test="${not empty reservations}">
            <table class="table">
                <thead>
                <tr>
                    <th>Customer</th>
                    <th>Since</th>
                    <th>Until</th>
                    <th>Room</th>
                    <th>Price</th>
                </tr>
                </thead>
                <c:forEach items="${reservations}" var="reservation">
                    <tr>
                        <td><c:out value="${reservation.customer.name}"/></td>
                        <td><c:out value="${reservation.since}"/></td>
                        <td><c:out value="${reservation.until}"/></td>
                        <td><c:out value="${reservation.room.number}"/></td>
                        <td><c:out value="${reservation.totalPrice}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </jsp:body>
</t:layout>