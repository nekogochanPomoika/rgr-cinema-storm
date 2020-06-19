package servlets.film.add;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/add-comment")
public class AddComment extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        req.setCharacterEncoding("UTF-8");
        if (currentUser == null) {
            resp.getWriter().print("not-logged-in");
            resp.getWriter().flush();
        } else {
            String title = req.getParameter("title");
            int rating = Integer.parseInt(req.getParameter("rating"));
            String text = req.getParameter("text");
            int filmId = Integer.parseInt(req.getParameter("filmId"));

            DBHandler.addComment1(currentUser.id, filmId, title, rating, text);
        }
    }
}
