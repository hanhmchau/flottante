package controllers;

import models.Cities;
import models.City;
import models.CityFilter;
import services.CategoryService;
import services.CityService;
import utils.XMLUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UpdateCategoriesServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int id = Integer.parseInt(request.getParameter("categoryId"));
            double weight = Double.parseDouble(request.getParameter("weight"));
            new CategoryService().updateCategory(id, weight);
            response.getWriter().write("OK");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
