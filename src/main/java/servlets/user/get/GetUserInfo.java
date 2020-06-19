package servlets.user.get;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-user-info")
public class GetUserInfo extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");

        User user = DBHandler.getUserByLogin(login);

        String res;

        if (user.login == null) {
            res = "no result";
        } else {
            res = "{" +
                    "\"name\":\"" + user.login + '\"' +
                    ",\"id\":\"" + user.id + '\"' +
                    "}";
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().print(res);
        resp.getWriter().flush();
    }
}
