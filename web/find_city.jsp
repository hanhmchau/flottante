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
<html>
<head>
    <title>Flottante - Find a City</title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/common.css" />" />
    <link type="text/css" rel="stylesheet" href="<c:url value="/styles/city.css" />" />
</head>
<body>
    <div class="flex">
        <form action="<c:url value="/cities-result"/>" method="get">
            <div class="item">
                <div class="description">
                    <div class="label">Population</div>
                    <div class="sub-label">Do you prefer a big or small city?</div>
                </div>
                <div class="select">
                    <select name="population">
                        <option value="1">Very High</option>
                        <option value="2">High</option>
                        <option value="3">Medium</option>
                        <option value="4">Low</option>
                        <option value="5">Very Low</option>
                        <option value="-1" selected>Doesn't matter</option>
                    </select>
                </div>
            </div>
            <div class="item">
                <div class="description">
                    <div class="label">Low cost of living</div>
                    <div class="sub-label">How important is low cost of living to you?
                    </div>
                </div>
                <div class="select">
                    <select name="col">
                        <option value="4" name="col" selected>Very important</option>
                        <option value="2" name="col">Important</option>
                        <option value="0" name="col">Not important</option>
                    </select>
                </div>
            </div>
            <div class="item">
                <div class="description">
                    <div class="label">Safety standards</div>
                    <div class="sub-label">How important is safety standards to you?
                    </div>
                </div>
                <div class="select">
                    <select name="safety">
                        <option value="3" name="safety" selected>Very important</option>
                        <option value="2" name="safety">Important</option>
                        <option value="1" name="safety">Not very important</option>
                    </select>
                </div>
            </div>
            <div class="item">
                <div class="description">
                    <div class="label">Languages</div>
                    <div class="sub-label">Leave blank to include all languages.
                    </div>
                </div>
                <div class="box">
                    <input class="input" oninput="filter(this)" placeholder="Search for a language..." />
                    <br/>
                    <div class="languages">
                        <c:forEach var="language" items="${LANGUAGES}">
                            <div style="display: none" class="language-state" id="${language.name}">
                                <label>
                                    <input type="checkbox" name="languages" value="${language.code}">
                                        ${language.name}</label>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="submit">
                <input class="button is-primary" type="submit" value="Find your cities" />
            </div>
        </form>
    </div>
<script>
    const filter = input => {
        const inputValue = input.value ? input.value : 'GIBBERISH';
        document.querySelectorAll(".language-state").forEach(val => {
          const id = val.id;
          const inp = val.getElementsByTagName("input")[0];
          if (inp && inp.checked || id.toLowerCase().includes(inputValue.toString().toLowerCase())) {
              val.setAttribute("style", "display: block;");
          } else {
              val.setAttribute("style", "display: none;");
          }
      })
    };
</script>
</body>
</html>
