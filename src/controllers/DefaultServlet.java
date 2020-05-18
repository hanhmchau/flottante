package controllers;

import models.Cities;
import models.City;
import services.CityService;
import utils.XMLUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DefaultServlet", urlPatterns = "/home")
public class DefaultServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            response.sendRedirect("index.jsp");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
