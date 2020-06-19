package servlets.user.get;

import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/current-user-name-and-status")
public class CurrentUserNameAndStatus extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        User currentUser = (User) req.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            resp.getWriter().print("not-logged-in");
        } else {
            String res = "{" +
                    "\"login\":\"" + currentUser.login +
                    "\",\"moderator\":\"" + currentUser.moderator +
                    "\"}";
            resp.getWriter().print(res);
        }

        resp.setContentType("application/json");
        resp.getWriter().flush();
    }
}
