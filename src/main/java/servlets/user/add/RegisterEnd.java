package servlets.user.add;

import handler.DBHandler;
import handler.NewUserInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/register-end")
public class RegisterEnd extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        if (key.equals(NewUserInfo.key)) {
            DBHandler.addUser(NewUserInfo.login, NewUserInfo.mail, NewUserInfo.password);
            resp.getWriter().print("Аккаунт создан!");
        } else {
            resp.getWriter().print("Ошибка подтверждения!");
        }
        resp.getWriter().flush();
    }
}
