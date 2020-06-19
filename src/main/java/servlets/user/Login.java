package servlets.user;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User currentUser = DBHandler.getUserByLogin(login);

        if (currentUser.login == null) {
            resp.getWriter().print("login-not-found");
        } else if (!currentUser.password.equals(password)) {
            resp.getWriter().print("password-not-correct");
        } else {
            req.getSession().setAttribute("currentUser", currentUser);
            resp.getWriter().print("success");
        }

        resp.setContentType("text");
        resp.getWriter().flush();
    }
}
