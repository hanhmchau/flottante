package controllers;

import models.Cities;
import services.CityService;
import utils.XMLUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShowSearchPageServlet", urlPatterns = "/cities")
public class ShowSearchPageServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            CityService cityService = new CityService();
            Cities cities = new Cities(cityService.getCities());
            String citiesXML = XMLUtils.marshal(cities);
            getServletContext().setAttribute("CITIES", citiesXML);
            request.setAttribute("txtSearch", "hiya!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher("search.jsp").forward(request, response);
        }
    }
}
