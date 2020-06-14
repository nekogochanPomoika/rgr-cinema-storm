package servlets.film;

import handler.DBHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/check-url-uniqueness")
public class CheckUrlUniqueness extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");

        String res;

        if (DBHandler.getFilm(url).name == null) res = "url-unique";
        else res = "url-not-unique";

        resp.getWriter().print(res);
        resp.getWriter().flush();
    }
}
