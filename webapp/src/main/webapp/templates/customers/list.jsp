<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout>
    <jsp:attribute name="title">Customers</jsp:attribute>
    <jsp:body>

        <div class="card">
            <div class="card-block">
                <h4 class="card-title">Register customer</h4>
                <div class="card-text">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            <c:out value="${error}"/>
                        </div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/customers/${formTarget}" method="post">
                        <div class="form-group">
                            <label for="nameInput">Name</label>
                            <input type="text" name="name" class="form-control" value="<c:out value='${name}'/>" id="nameInput">
                        </div>
                        <div class="form-group">
                            <label for="addressTextarea">Address</label>
                            <textarea name="address" class="form-control" id="addressTextarea"><c:out value='${address}'/></textarea>
                        </div>
                        <div class="form-group">
                            <label for="birthDate">Birth date</label>
                            <input type="date" class="form-control" id="birthDate" name="birthDate" value="<c:out value='${date}'/>">
                        </div>
                        <input type="Submit" class="btn btn-primary" value="Register">
                    </form>
                </div>
            </div>
        </div>
        <c:if test="${empty customers}">
            <hr>
            <div class="alert alert-info">There are no customers yet</div>
        </c:if>
        <c:if test="${not empty customers}">
            <table class="table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Address</th>
                    <th>Birth date</th>
                    <th>&nbsp;</th>
                </tr>
                </thead>
                <c:forEach items="${customers}" var="customer">
                    <tr>
                        <td><c:out value="${customer.name}"/></td>
                        <td><c:out value="${customer.address}"/></td>
                        <td><c:out value="${customer.birthDate}"/></td>
                        <td>
                            <c:if test="${reservations[customer.id] == 0}">
                            <form method="post" action="${pageContext.request.contextPath}/customers/delete?id=${customer.id}" style="display: inline-block">
                                <input type="submit" class="btn btn-danger" value="Delete">
                            </form>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/customers/update?id=${customer.id}" class="btn btn-primary">Update</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </jsp:body>
</t:layout>