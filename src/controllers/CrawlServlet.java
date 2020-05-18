package controllers;

import models.Category;
import scraper.MainScraper;
import services.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CrawlServlet", urlPatterns = "/crawl")
public class CrawlServlet extends AbstractServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            MainScraper scraper = new MainScraper();
            Runnable runnable = scraper::startCrawling;
            runnable.run();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        finally {
            response.sendRedirect(request.getContextPath() + "/categories");
        }
    }
}
