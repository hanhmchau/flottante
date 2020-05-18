<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 05-Dec-19
  Time: 6:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<html>
  <head>
    <title>Flottante - Categories</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/categories.css" />" />
  </head>
  <body>
  <form action="<c:url value="/crawl"/>">
      <button class="button is-primary">Start crawling</button>
  </form>
  <table border="0">
      <tr>
          <th>Category name</th>
          <th>Weight</th>
      </tr>
      <c:forEach items="${CATEGORIES}" var="category">
          <tr>
              <td>
                      ${category.name}
              </td>
              <td>
                  <c:if var="hasValue" test="${category.weight >= 0}">
                      <input onblur="update(this)" id="${category.id}" value="${category.weight}" />
                  </c:if>
                  <c:if test="${not hasValue}">
                      <input onblur="update(this)" id="${category.id}" />
                  </c:if>
              </td>
          </tr>
      </c:forEach>
  </table>
  <script>
    function update(input) {
      const weight = input.value;
      if (!isNaN(parseFloat(weight))) {
          if (parseFloat(weight) >= 0) {
              let xhr = new XMLHttpRequest();
              xhr.open('GET', '<c:url value="/update-categories"/>' + '?categoryId=' + input.id + '&weight=' + weight);
              xhr.onload = function() {
                  if (xhr.status !== 200) { // analyze HTTP status of the response
                      <%--alert(`Error ${xhr.status}: ${xhr.statusText}`); // e.g. 404: Not Found--%>
                  } else { // show the result
                      console.log(xhr.responseText);
                  }
              };
              xhr.onerror = function() {
                  alert("Request failed");
              };
              xhr.send();
          }
      }
    }
    window.onload = function() {
    }
  </script>
  </body>
</html>
