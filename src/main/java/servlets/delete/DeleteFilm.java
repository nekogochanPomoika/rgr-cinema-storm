package servlets.delete;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/delete-film")
public class DeleteFilm extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        if (currentUser == null || !currentUser.moderator) {
            resp.getWriter().print("no result");
            resp.getWriter().flush();
        } else {
            int filmId = Integer.parseInt(req.getParameter("filmId"));
            DBHandler.deleteFilm(filmId);
        }
    }
}
