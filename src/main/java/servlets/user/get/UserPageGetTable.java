package servlets.user.get;

import handler.DBHandler;
import storage.Manufactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/user-page-get-table")
public class UserPageGetTable extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page"));
        int userId = Integer.parseInt(req.getParameter("userId"));
        String orderBy = req.getParameter("orderBy");
        boolean desc = req.getParameter("desc").equals("true");

        if (orderBy.equals("name")) desc = !desc;

        DBHandler.getTableForUser(10, page, userId, orderBy, desc);

        StringBuilder res = new StringBuilder();

        try {
            res.append('[');

            res.append(Manufactory.filmsForUserPage.get(0).toJSONString());

            for (int i = 1; i < Manufactory.filmsForUserPage.size(); i++) {
                res.append(',');
                res.append(Manufactory.filmsForUserPage.get(i).toJSONString());
            }

            res.append(']');
        } catch (IndexOutOfBoundsException e) {
            res = new StringBuilder("no result");
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().print(res);
        resp.getWriter().flush();
    }
}
