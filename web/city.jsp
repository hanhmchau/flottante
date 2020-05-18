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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
  <head>
    <title>Flottante - Search</title>
      <link type="text/css" rel="stylesheet" href="<c:url value="/styles/common.css" />" />
      <link type="text/css" rel="stylesheet" href="<c:url value="/styles/one-city.css" />" />
  </head>
  <body>
    <div class="flex">
        <c:if test="${CITY != null}">
            <div class="title">${CITY.name}</div>
            <div class="country">${CITY.country.name}</div>
            <div class="images">
                <c:forEach items="${CITY.images}" var="image">
                    <img src="${image}" />
                </c:forEach>
            </div>
            <div class="info">
                <div class="col-title">Information</div>
                <c:if test="${CITY.population > 0}">
                    <div class="item">
                        <b>Population: </b> ${CITY.population}
                    </div>
                </c:if>
                <div class="item">
                    <b>Cost of living ranking:</b> ${CITY.costOfLivingRanking}
                </div>
                <div class="item">
                    <b>Safety ranking:</b> ${CITY.safetyRanking}
                </div>
                <c:if test="${not empty CITY.languages}">
                    <div class="item">
                        <b>Languages:</b>
                        <span>
                        <c:forEach varStatus="counter" var="lang" items="${CITY.languages}">
                            ${lang}<c:if test="${counter.count < fn:length(CITY.languages)}">,</c:if>
                        </c:forEach>
                    </span>
                    </div>
                </c:if>
            </div>
            <div class="col">
                <div class="col-title">Cost of Living</div>
                <table border="0" cellpadding="4" cellspacing="4">
                    <c:forEach var="cat" items="${CITY.costOfLiving.categories}">
                        <tr class="cat">
                            <td class="cat-title"><div class="inner">${cat.key}</div></td></tr>
                        <c:forEach var="item" items="${cat.value.items}">
                                <tr>
                                    <td class="cat-item">
                                            ${item.title}
                                    </td>
                                    <td class="cat-value">
                                            ${item.value}
                                    </td>
                                </tr>
                            </c:forEach>
                    </c:forEach>
                </table>
            </div>
        </c:if>
    </div>
  </body>
</html>
