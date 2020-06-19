package servlets.user.get;

import handler.DBHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/get-user-marks-count")
public class GetUserMarksCount extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));

        int count;

        count = DBHandler.getMarksCount(userId);

        resp.getWriter().print(count);
        resp.getWriter().flush();
    }
}
