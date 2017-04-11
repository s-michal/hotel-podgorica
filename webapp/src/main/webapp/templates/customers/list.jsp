<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>Name</th>
        <th>Address</th>
        <th>Birth date</th>
    </tr>
    </thead>
    <c:forEach items="${customers}" var="customer">
        <tr>
            <td><c:out value="${customer.name}"/></td>
            <td><c:out value="${customer.address}"/></td>
            <td><c:out value="${customer.birthDate}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/customers/delete?id=${customer.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Delete"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Register customer</h2>
<c:if test="${not empty error}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${error}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/customers/add" method="post">
    <table>
        <tr>
            <th>Name:</th>
            <td><input type="text" name="name" value="<c:out value='${param.name}'/>"></td>
        </tr>
        <tr>
            <th>Address:</th>
            <td>
                <textarea name="address">
                    <c:out value='${param.address}'/>
                </textarea>
            </td>
        </tr>
        <tr>
            <th>Birth date:</th>
            <td><input type="date" name="birthDate" value="<c:out value='${param.date}'/>"></td>
        </tr>
    </table>
    <input type="Submit" value="Register">
</form>

</body>
</html>