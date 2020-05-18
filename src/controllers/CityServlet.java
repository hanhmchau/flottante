package controllers;

import models.City;
import services.CityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CityServlet", urlPatterns = "/city")
public class CityServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            City city = (new CityService()).getCity(id);
            if (city != null) {
                request.setAttribute("CITY", city);
                request.getRequestDispatcher("city.jsp").forward(request, response);
                return;
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
