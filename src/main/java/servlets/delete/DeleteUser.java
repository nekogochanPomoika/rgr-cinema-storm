package servlets.delete;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/delete-user")
public class DeleteUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        if (currentUser == null || !currentUser.moderator) {
            resp.getWriter().print("no result");
            resp.getWriter().flush();
        } else {
            int id = Integer.parseInt(req.getParameter("id"));
            DBHandler.deleteUser(id);
        }
    }
}