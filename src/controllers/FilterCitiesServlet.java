package controllers;

import models.Cities;
import models.City;
import models.CityFilter;
import models.Language;
import services.CityService;
import services.LanguageService;
import utils.XMLUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "FilterCitiesServlet", urlPatterns = "/filter-cities")
public class FilterCitiesServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
//            String filter = request.getParameter("filter");
//            CityFilter cityFilter = (CityFilter) XMLUtils.unmarshal(filter, CityFilter.class);
            int populationMin, populationMax;
            int col, safety;
            String[] languages = new String[0];
            try {
                populationMin = Integer.parseInt(request.getParameter("populationMin"));
            } catch (Exception e) {
                populationMin = 0;
            }
            try {
                populationMax = Integer.parseInt(request.getParameter("populationMax"));
            } catch (Exception e) {
                populationMax = 0;
            }
            try {
                col = Integer.parseInt(request.getParameter("col"));
            } catch (Exception e) {
                col = 3;
            }
            try {
                safety = Integer.parseInt(request.getParameter("safety"));
            } catch (Exception e) {
                safety = 3;
            }
            try {
                languages = request.getParameterValues("languages");
            } catch (Exception ignored) {
            }
            if (languages == null) {
                languages = new String[0];
            }
            int page = Integer.parseInt(request.getParameter("page"));
            CityFilter cityFilter = new CityFilter(page, col, safety, populationMin, populationMax, Arrays.asList(languages));
            CityService cityService = new CityService();
            List<City> cities = cityService.filterCities(cityFilter, page, 10);
            Cities citiesObj = new Cities(cities);
            String resp = XMLUtils.marshal(citiesObj);
            response.getWriter().write(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
