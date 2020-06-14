package servlets.film;

import handler.DBHandler;
import handler.FilmBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/add-film-base-info")
public class AddFilmBaseInfo extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FilmBuilder.keys.clear();               FilmBuilder.values.clear();

        req.setCharacterEncoding("UTF-8");

        Map<String, String[]> map = req.getParameterMap();

        for (Map.Entry<String, String[]> entry: map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];

            if (value == null || value.trim().length() == 0) continue;

            FilmBuilder.keys.add(key);
            FilmBuilder.values.add(value);
        }

        System.out.println(FilmBuilder.keys);
        System.out.println(FilmBuilder.values);

        DBHandler.addFilm(FilmBuilder.keys, FilmBuilder.values);
    }
}
