package servlets.film.get;

import handler.DBHandler;
import storage.Manufactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-comments")
public class GetComments extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int filmId = Integer.parseInt(req.getParameter("filmId"));

        DBHandler.getComments1(filmId);

        StringBuilder res = new StringBuilder();

        try {
            res.append('[');

            res.append(Manufactory.comments1.get(0).toJSONString());
            for (int i = 1; i < Manufactory.comments1.size(); i++) {
                res.append(',');
                res.append(Manufactory.comments1.get(i).toJSONString());
            }

            res.append(']');
        } catch (IndexOutOfBoundsException e) {
            res = new StringBuilder("no result");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(res);
        resp.getWriter().flush();
    }
}
