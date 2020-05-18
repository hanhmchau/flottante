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
    <title>Flottante - Search</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/common.css" />" />
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/home.css" />" />
  </head>
  <body>
    <div class="flex">
      <div class="container">
        <div class="title">Find your City</div>
        <div class="subtitle">
          Compare cities on quality of life, cost of living, salaries and more.
          <br/>
          Explore where to move based on your personal preferences.
        </div>
        <div class="links">
          <button class="button is-primary">
            <a href="${pageContext.request.contextPath}/find-a-city">Start exploring</a>
          </button>
          <button class="button is-light">
            <a href="${pageContext.request.contextPath}/cities">Try searching</a>
          </button>
        </div>
      </div>
      <div class="logo">
        <img src="<c:url value="/images/logo.png"/>"  alt="Logo"/>
      </div>
    </div>

  </body>
</html>
