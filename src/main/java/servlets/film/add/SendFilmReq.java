package servlets.film.add;

import handler.DBHandler;
import storage.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/send-film-req")
public class SendFilmReq extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            resp.getWriter().print("not-logged-in");
            resp.getWriter().flush();
        } else {
            String name = req.getParameter("name");
            String year = req.getParameter("year");

            DBHandler.addFilmReq(currentUser.login, name, year);
        }
    }
}
