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
    <title>Flottante</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/common.css" />" />
  </head>
  <body>
    Search!
    <c:set var="search" value="${not empty txtSearch ? txtSearch : (not empty param.txtSearch ? param.txtSearch : '')}" />
    <form action="search.jsp">
      <label>
        Search:
        <input name="txtSearch" value="${search}" />
      </label>
    </form>
    <c:import url="/stylesheets/search.xsl" var="xslSearch" />
    <c:import url="/stylesheets/cities.xsl" var="xslCities" />

    <x:transform xslt="${xslCities}">
      <x:transform xml="${CITIES}" xslt="${xslSearch}">
        <x:param name="search" value="${search}" />
      </x:transform>
    </x:transform>
  </body>
</html>
