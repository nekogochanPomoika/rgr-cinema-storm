package servlets.film.get;

import handler.DBHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-films-count")
public class GetFilmsCount extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int count = DBHandler.getFilmsCount();

        resp.setContentType("text");
        resp.getWriter().print(count);
        resp.getWriter().flush();
    }
}
