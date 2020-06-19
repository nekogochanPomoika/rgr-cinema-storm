package servlets.film.add;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/add-mark")
public class AddMark extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            resp.getWriter().print("not-logged-in");
        } else {
            int filmId = Integer.parseInt(req.getParameter("filmId"));
            int mark = Integer.parseInt(req.getParameter("mark"));

            int currentMark = DBHandler.getMarkByUserAndFilm(currentUser.id, filmId);

            if (currentMark == 0) {
                DBHandler.addMark(currentUser.id, filmId, mark);
            } else {
                DBHandler.changeMark(currentUser.id, filmId, mark, currentMark);
            }
        }

        resp.getWriter().flush();
    }
}
