package servlets.film.get;

import storage.Manufactory;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-film-reqs")
public class GetFilmReqs extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        resp.setCharacterEncoding("UTF-8");

        if (currentUser == null || !currentUser.moderator) {
            resp.getWriter().print("no-moderator");
        } else {
            StringBuilder res = new StringBuilder();
            try {
                res.append('[');

                res.append(Manufactory.filmReqs.get(0).toJSONString());

                for (int i = 1; i < Manufactory.filmReqs.size(); i++) {
                    res.append(',');
                    res.append(Manufactory.filmReqs.get(i).toJSONString());
                }

                res.append(']');
            } catch (IndexOutOfBoundsException e) {
                res = new StringBuilder("no result");
            }
            resp.getWriter().print(res);
        }
        resp.setContentType("application/json");
        resp.getWriter().flush();
    }
}
