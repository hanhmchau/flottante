package controllers;

import services.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MainServlet", urlPatterns = "/main")
public class MainServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String servlet = "";
        switch (action) {
            case "SHOW_SEARCH_PAGE":
                servlet = "ShowSearchPageServlet";
                break;
            case "SHOW_FILTER_PAGE":
                servlet = "ShowFilterPageServlet";
                break;
            case "FILTER":
                servlet = "FilterServlet";
                break;
            case "SHOW_CATEGORY_PAGE":
                servlet = "ShowCategoryPageServlet";
                break;
            case "UPDATE_CATEGORY":
                System.out.println("Hi!");
                servlet = "UpdateCategoryServlet";
                int id = Integer.parseInt(request.getParameter("categoryId"));
                double weight = Double.parseDouble(request.getParameter("weight"));
                try {
                    new CategoryService().updateCategory(id, weight);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "NAN":
                servlet = "NanServlet";
                break;
        }
        response.setStatus(HttpServletResponse.SC_OK);
//        request.getRequestDispatcher(servlet).forward(request, response);
    }
}
