package servlets.film;

import handler.DBHandler;
import storage.Manufactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-back")
public class GetFilmInfo extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        String filmUrl = req.getParameter("name");
        System.out.println(filmUrl);

        DBHandler.getFilm(filmUrl);

        String res = Manufactory.film.toJSONString();

        System.out.println("JSON STRING IS " + res);

        resp.setContentType("application/json");
        resp.getWriter().print(res);
        resp.getWriter().flush();
    }
}
