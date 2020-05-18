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

@WebServlet(name = "CityResultsServlet", urlPatterns = "/cities-result")
public class CityResultsServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int page = 1;
            int population;
            int col, safety;
            String[] languages = new String[0];
            try {
                population = Integer.parseInt(request.getParameter("population"));
            } catch (Exception e) {
                population = 0;
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
            int populationMin = 0;
            int populationMax = 0;
            switch (population) {
                case 1:
                    populationMin = 5_000_000;
                    break;
                case 2:
                    populationMax = 5_000_000;
                    populationMin = 2_000_000;
                    break;
                case 3:
                    populationMax = 2_000_000;
                    populationMin = 900_000;
                    break;
                case 4:
                    populationMax = 900_000;
                    populationMin = 500_000;
                    break;
                case 5:
                    populationMax = 500_000;
                    break;
            }
            CityFilter cityFilter = new CityFilter(page, col, safety, populationMin,
                    populationMax, Arrays.asList(languages));
            List<City> cities = (new CityService()).filterCities(cityFilter, cityFilter.getPage(), 10);
            request.setAttribute("CITIES", cities);
            request.setAttribute("PAGE", page);
            request.setAttribute("POPULATION_MIN", populationMin);
            request.setAttribute("POPULATION_MAX", populationMax);
            request.setAttribute("COL", col);
            request.setAttribute("SAFETY", safety);
            request.setAttribute("LANGUAGES", languages);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher("city_results.jsp").forward(request, response);
        }
    }
}
