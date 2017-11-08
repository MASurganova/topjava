<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Show All Meals</title>
</head>
<body>
<table border=1>
    <thead>
    <tr>
        <%--<th>Id</th>--%>
        <th>Date</th>
        <th>Time</th>
        <th>Description</th>
        <th>Calories</th>
        <%--<th colspan=2>Action</th>--%>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <c:if test="${meal.exceed}">
            <tr bgcolor="red">
                <%--<td><c:out value="${meal.id}" /></td>--%>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <td><fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDateTime}" /></td>
                <td><fmt:formatDate pattern="HH:mm" value="${parsedDateTime}" /></td>
                <td><c:out value="${meal.description}" /></td>
                <td><c:out value="${meal.calories}" /></td>
                <%--<td><a href="MealServlet?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>--%>
                <%--<td><a href="MealServlet?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>--%>
            </tr>
        </c:if>
        <c:if test="${!meal.exceed}">
            <tr bgcolor="green">
                <%--<td><c:out value="${meal.id}" /></td>--%>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <td><fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDateTime}" /></td>
                <td><fmt:formatDate pattern="HH:mm" value="${parsedDateTime}" /></td>
                <td><c:out value="${meal.description}" /></td>
                <td><c:out value="${meal.calories}" /></td>
                <%--<td><a href="MealServlet?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>--%>
                <%--<td><a href="MealServlet?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>--%>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>
<%--<p><a href="MealServlet?action=insert">Add User</a></p>--%>
</body>
</html>