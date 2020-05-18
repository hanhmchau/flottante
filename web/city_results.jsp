<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 08-Dec-19
  Time: 1:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Flottante - Find a City</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/common.css" />" />
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/city-results.css" />" />
</head>
<body>
    <div class="flex">
        <div class="title">Your result</div>
        <div class="ranking">
            <c:if var="hasCities" test="${not empty CITIES}">
                <c:set var="maxScore" value="${CITIES[0].rankingScore}" />
                <div id="ranking-table">
                    <c:forEach var="city" varStatus="counter" items="${CITIES}">
                        <div class="item">
                            <a class="name" href="<c:url value="/city?id=${city.id}"/>">
                                    ${city.name}
                            </a>
                            <div class="full-bar">
                                <div class="bar" style="width: ${city.rankingScore / maxScore * 100}%;">
                                    ${city.rankingScore}
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <div id="pagination" class="pagination">
                <c:if test="${PAGE > 1}">
                    <button class="button is-light" onclick="goToPage(${PAGE - 1})">Prev</button>
                </c:if>
                <button class="button is-primary" onclick="goToPage(${PAGE + 1})">Next</button>
            </div>
            <c:if test="${not hasCities}">
                We can't find a city that matches your criteria.
            </c:if>
        </div>
    </div>
    <script>
        let scoreMax = 0;
        <c:if test="${not empty CITIES}">
            scoreMax = ${CITIES[0].rankingScore};
        </c:if>
        let page = ${PAGE};
        const populationMin = ${POPULATION_MIN};
        const populationMax = ${POPULATION_MAX};
        const col = ${COL};
        const safety = ${SAFETY};
        const languages = [
          <c:forEach varStatus="count" var="language" items="${LANGUAGES}">
                '${language}'
                <c:if test="${count.count < fn:length(LANGUAGES)}">,</c:if>
            </c:forEach>
        ];
        const languageQuery = '' <c:forEach var="language" items="${LANGUAGES}">+ '&languages=${language}' </c:forEach>;

        const goToPage = page => {
            let xhr = new XMLHttpRequest();
            const query = '<c:url value="/filter-cities?page=XXX&col=${COL}&safety=${SAFETY}&populationMin=${POPULATION_MIN}&populationMax=${POPULATION_MAX}"/>' + languageQuery;
            const formattedQuery = query.replace('XXX', page.toString());
            xhr.open('GET', formattedQuery);
            xhr.onload = function() {
                if (xhr.status !== 200) { // analyze HTTP status of the response
                    <%--alert(`Error ${xhr.status}: ${xhr.statusText}`); // e.g. 404: Not Found--%>
                } else { // show the result
                    const rankingTable = document.getElementById("ranking-table");
                    const parser = new DOMParser();
                    const xmlDoc = parser.parseFromString(xhr.responseText, "text/xml");
                    const cityNodes = xmlDoc.getElementsByTagName("city");
                    if (cityNodes.length > 0) {
                        rankingTable.innerHTML = '';
                        for (let i = 0; i < cityNodes.length; i++) {
                            const cityNode = cityNodes[i];
                            const id = cityNode.getElementsByTagName("id")[0].innerHTML;
                            const name = cityNode.getElementsByTagName("name")[0].innerHTML;
                            const score = cityNode.getElementsByTagName("rankingScore")[0].innerHTML;
                            const item = getItem(id, name, score, scoreMax);
                            rankingTable.innerHTML += item;
                            updatePagination(page);
                        }
                    }
                }
            };

            xhr.onerror = function() {
                alert("Request failed");
            };
            xhr.send();
        };

        const updatePagination = (currentPage) => {
            const box = document.getElementById("pagination");
            box.innerHTML = '';
            if (currentPage > 1) {
                box.innerHTML += '<button class="button is-light" onclick="goToPage('+ (currentPage - 1) +')">Prev</button>';
            }
            box.innerHTML += '<button class="button is-primary" onclick="goToPage('+ (currentPage + 1) +')">Next</button>';
        };

        const getItem = (cityId, cityName, score, maxScore) => {
            const perc = (score / maxScore * 100).toFixed(2);
            return '<div class="item">\
                <a class="name" href="<c:url value="/" />city?id='+cityId+'">' + cityName + '</a>\
                <div class="full-bar">\
                <div class="bar" style="width: ' + perc + '%;">'+ score +'</div>\
                </div>\
                </div>'
        }
    </script>
</body>
</html>
