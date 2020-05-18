package controllers;

import models.Cities;
import models.Language;
import services.CityService;
import services.LanguageService;
import utils.XMLUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FindACityPageServlet", urlPatterns = "/find-a-city")
public class FindACityPageServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            List<Language> languages = (new LanguageService()).getLanguages();
            request.setAttribute("LANGUAGES", languages);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher("find_city.jsp").forward(request, response);
        }
    }
}
