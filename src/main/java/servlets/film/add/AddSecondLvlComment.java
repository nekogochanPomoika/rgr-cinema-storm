package servlets.film.add;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/add-second-lvl-comment")
public class AddSecondLvlComment extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            resp.getWriter().print("not-logged-in");
            resp.getWriter().flush();
        } else {
            req.setCharacterEncoding("UTF-8");

            String text = req.getParameter("text");
            int commentId = Integer.parseInt(req.getParameter("commentId"));
            int userId = currentUser.id;

            DBHandler.addComment2(commentId, userId, text);
        }
    }
}
