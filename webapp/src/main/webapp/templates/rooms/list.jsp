<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>Number</th>
        <th>Capacity</th>
        <th>Floor</th>
        <th>Price / day</th>
    </tr>
    </thead>
    <c:forEach items="${rooms}" var="room">
        <tr>
            <td><c:out value="${room.number}"/></td>
            <td><c:out value="${room.capacity}"/></td>
            <td><c:out value="${room.floor}"/></td>
            <td><c:out value="${room.pricePerDay}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/rooms/delete?id=${room.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Delete"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Add room</h2>
<c:if test="${not empty error}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${error}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/rooms/add" method="post">
    <table>
        <tr>
            <th>Room number:</th>
            <td><input type="number" name="number" value="<c:out value='${param.number}'/>"></td>
        </tr>
        <tr>
            <th>Capacity:</th>
            <td><input type="number" name="capacity" value="<c:out value='${param.capacity}'/>"></td>
        </tr>
        <tr>
            <th>Floor:</th>
            <td><input type="number" name="floor" value="<c:out value='${param.floor}'/>"></td>
        </tr>
        <tr>
            <th>Price/Day:</th>
            <td><input type="number" name="pricePerDay" value="<c:out value='${param.pricePerDay}'/>"></td>
        </tr>
    </table>
    <input type="Submit" value="Add">
</form>

</body>
</html>