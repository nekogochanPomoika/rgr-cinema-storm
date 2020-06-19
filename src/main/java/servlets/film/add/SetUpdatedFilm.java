package servlets.film.add;

import storage.Manufactory;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = "/set-updated-film")
public class SetUpdatedFilm extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        if (currentUser.moderator) {
            Manufactory.updatedFilmId = Integer.parseInt(req.getParameter("id"));
        } else {
            resp.getWriter().print("no result");
            resp.getWriter().flush();
        }
    }
}
