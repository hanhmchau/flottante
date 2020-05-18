package controllers;

import models.Category;
import models.City;
import services.CategoryService;
import services.CityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoriesServlet", urlPatterns = "/categories")
public class CategoriesServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            List<Category> categories = (new CategoryService()).getCategories();
            request.setAttribute("CATEGORIES", categories);
            request.getRequestDispatcher("categories.jsp").forward(request, response);
            return;
        } catch (Exception ignored) {
        }
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
