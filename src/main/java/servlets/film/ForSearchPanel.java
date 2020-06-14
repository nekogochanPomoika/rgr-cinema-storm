package servlets.film;

import handler.DBHandler;
import storage.Manufactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/for-search-panel")
public class ForSearchPanel extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tag = req.getParameter("tag");

        DBHandler.getFilmsForSearchPanelByTag(tag);

        StringBuilder res = new StringBuilder();

        try {
            res.append('[');

            res.append(Manufactory.filmsForSearchPanels.get(0).toJSONString());

            for (int i = 1; i < Manufactory.filmsForSearchPanels.size(); i++) {
                res.append(',');
                res.append(Manufactory.filmsForSearchPanels.get(i).toJSONString());
            }

            res.append(']');
        } catch (IndexOutOfBoundsException e) {
            res = new StringBuilder();
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        resp.getWriter().print(res.toString());
        resp.getWriter().flush();
    }
}
