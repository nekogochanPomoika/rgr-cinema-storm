package servlets.film.get;

import handler.DBHandler;
import storage.Manufactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/main-page-get-table")
public class MainPageGetTable extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page"));
        String orderBy = req.getParameter("orderBy");
        boolean desc = req.getParameter("desc").equals("true");

        if (orderBy.equals("name")) desc = !desc;

        DBHandler.getFilmsForTableOrderBy(10, page, orderBy, desc);

        StringBuilder res = new StringBuilder();

        try {
            res.append('[');

            res.append(Manufactory.filmsForTable.get(0).toJSONString());

            for (int i = 1; i < Manufactory.filmsForTable.size(); i++) {
                res.append(',');
                res.append(Manufactory.filmsForTable.get(i).toJSONString());
            }

            res.append(']');
        } catch (IndexOutOfBoundsException e) {
            res = new StringBuilder("no result");
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        resp.getWriter().print(res.toString());
        resp.getWriter().flush();
    }
}
