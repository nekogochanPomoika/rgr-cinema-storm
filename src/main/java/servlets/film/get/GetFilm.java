package servlets.film.get;

import handler.DBHandler;
import storage.Manufactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-film")
public class GetFilm extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        String filmUrl = req.getParameter("name");

        DBHandler.getFilm(filmUrl);

        String res = Manufactory.film.toJSONString();

        resp.setContentType("application/json");
        resp.getWriter().print(res);
        resp.getWriter().flush();
    }
}
