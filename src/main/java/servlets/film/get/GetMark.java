package servlets.film.get;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-mark")
public class GetMark extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        int mark;

        if (currentUser == null) {
            resp.getWriter().print("not-logged-in");
        } else {
            int filmId = Integer.parseInt(req.getParameter("filmId"));
            mark = DBHandler.getMarkByUserAndFilm(currentUser.id, filmId);
            resp.getWriter().print(mark);
        }
        resp.getWriter().flush();
    }
}
