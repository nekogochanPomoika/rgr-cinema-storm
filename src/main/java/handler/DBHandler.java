package handler;

import storage.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class DBHandler {
    private static String url = "jdbc:mysql://localhost:3306/cinema_storm?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
    private static String login = "admin";
    private static String password = "admin";
    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String login, String mail, String password) {
        String sqlQuery = "INSERT INTO users (login, mail, password) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, mail);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMark(int userId, int filmId, int value) {
        String sqlQuery = "INSERT INTO user_marks (user_id, film_id, mark) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, filmId);
            preparedStatement.setInt(3, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sqlQuery = "UPDATE films SET rating = rating + ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, filmId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sqlQuery = "UPDATE films SET marks_count = marks_count + 1 WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, filmId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void changeMark(int userId, int filmId, int value, int oldValue) {
        String sqlQuery = "UPDATE user_marks SET mark = ? WHERE user_id = ? AND film_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, filmId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sqlQuery = "UPDATE films SET rating = rating + ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, value - oldValue);
            preparedStatement.setInt(2, filmId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void addComment1(int userId, int filmId, String header, int rating, String data) {
        String sqlQuery = "INSERT INTO comments1 (user_id, film_id, header, rating, data) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, filmId);
            preparedStatement.setString(3, header);
            preparedStatement.setInt(4, rating);
            preparedStatement.setString(5, data);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addComment2(int firstLevelCommentId, int userId, String data) {
        String sqlQuery = "INSERT INTO comments2 (comment1_id, user_id, data) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, firstLevelCommentId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, data);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addFilm(ArrayList<String> keys, ArrayList<String> values) {
        FilmAdder filmAdder = new FilmAdder();
        filmAdder.buildSqlQuery(keys, values);

        try (PreparedStatement preparedStatement = connection.prepareStatement(filmAdder.getSqlQuery())) {
            filmAdder.buildStatement(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateFilm(ArrayList<String> keys, ArrayList<String> values) {
        FilmUpdater filmUpdater = new FilmUpdater();
        filmUpdater.buildSqlQuery(keys, values);

        try (PreparedStatement preparedStatement = connection.prepareStatement(filmUpdater.getSqlQuery())) {
            filmUpdater.buildStatement(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addImageUrl(String imageUrl, String filmUrl) {
        String sqlQuery = "UPDATE films SET image_url = ? WHERE url = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, imageUrl);
            preparedStatement.setString(2, filmUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addVideoUrl(String trailerUrl, String filmUrl) {
        String sqlQuery = "UPDATE films SET trailer_url = ? WHERE url = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, trailerUrl);
            preparedStatement.setString(2, filmUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(int id) {

        /*
         * (5) users ----> (4) marks
         * |
         * |-> (3) comments2
         * |
         * |-> (2) comments1 ----> (1) comments2
         *
         * delete in this order
         * */

        String sqlQuery;

        sqlQuery = "DELETE FROM comments2 WHERE comment1_id IN " +
                "(SELECT id FROM comments1 WHERE user_id = ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM comments1 WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM comments2 WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM user_marks WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMark(int userId, int filmId, int value) {

        String sqlQuery = "UPDATE films SET rating = rating - ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setInt(2, filmId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sqlQuery = "UPDATE films SET marks_count = marks_count - 1 WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, filmId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        sqlQuery = "DELETE FROM user_marks WHERE user_id = ? AND film_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, filmId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteComment1(int id) {
        String sqlQuery = "DELETE FROM comments2 WHERE comment1_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM comments1 WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteComment2(int id) {
        String sqlQuery = "DELETE FROM comments2 WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFilm(int id) {
        String sqlQuery;

        sqlQuery = "DELETE FROM comments2 WHERE comment1_id IN " +
                "(SELECT id FROM comments1 WHERE film_id = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM comments1 WHERE film_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM user_marks WHERE film_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlQuery = "DELETE FROM films WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUserByLogin(String login) {
        Manufactory.users.clear();

        User user = new User();

        String sqlQuery = "SELECT * FROM users WHERE login = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user.id = resultSet.getInt("id");
                user.login = resultSet.getString("login");
                user.mail = resultSet.getString("mail");
                user.password = resultSet.getString("password");
                user.moderator = resultSet.getString("moderator").equals("yes");
            }

            Manufactory.users.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void getUsersByLoginTag(String tag) {
        Manufactory.users.clear();

        tag = tag + '%';
        String sqlQuery = "SELECT * FROM users WHERE login LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, tag);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();

                user.id = resultSet.getInt("id");
                user.login = resultSet.getString("login");
                user.mail = resultSet.getString("mail");
                user.password = resultSet.getString("password");
                user.moderator = resultSet.getString("moderator").equals("yes");

                Manufactory.users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getComments1(int film_id) {
        Manufactory.comments1.clear();

        String sqlQuery = "SELECT * FROM comments1 WHERE film_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, film_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FirstLevelComment comment = new FirstLevelComment();

                sqlQuery = "SELECT login FROM users WHERE id = ?";

                String userName = "";

                PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQuery);
                preparedStatement2.setInt(1, resultSet.getInt("user_id"));
                ResultSet resultSet2 = preparedStatement2.executeQuery();

                while (resultSet2.next())  userName = resultSet2.getString(1);

                comment.id = resultSet.getInt("id");
                comment.user = userName;
                comment.filmId = resultSet.getInt("film_id");
                comment.header = resultSet.getString("header");
                comment.rating = resultSet.getInt("rating");
                comment.data = resultSet.getString("data");

                getComments2(comment.id);

                comment.secondLevelComments = new ArrayList<>(Manufactory.comments2);

                Manufactory.comments1.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getComments2(int comment_id) {
        Manufactory.comments2.clear();

        String sqlQuery = "SELECT * FROM comments2 WHERE comment1_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, comment_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SecondLevelComment comment = new SecondLevelComment();

                sqlQuery = "SELECT login FROM users WHERE id = ?";

                String userName = "";

                PreparedStatement preparedStatement2 = connection.prepareStatement(sqlQuery);
                preparedStatement2.setInt(1, resultSet.getInt("user_id"));
                ResultSet resultSet2 = preparedStatement2.executeQuery();

                while (resultSet2.next())  userName = resultSet2.getString(1);

                comment.id = resultSet.getInt("id");
                comment.firstLevelCommentId = resultSet.getInt("comment1_id");
                comment.user = userName;
                comment.data = resultSet.getString("data");

                Manufactory.comments2.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public static void getMarksByUserLogin(int id) {
        Manufactory.marks.clear();

        String sqlQuery = "SELECT * FROM user_marks WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserMarks mark = new UserMarks();

                mark.userId = resultSet.getInt("user_id");
                mark.filmId = resultSet.getInt("film_id");
                mark.mark = resultSet.getInt("mark");

                Manufactory.marks.add(mark);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
    public static void getTableForUser(int pageSize, int pageNumber, int userId, String orderBy, boolean desc) {
        int startNumber = pageSize * (pageNumber - 1);

        String sqlQuery = "SELECT f.url, f.name, f.rating, f.marks_count, m.mark FROM films f INNER JOIN user_marks m " +
                "ON f.id = m.film_id WHERE m.user_id = ? ORDER BY " + orderBy;
        if (desc) sqlQuery += " DESC";
        sqlQuery += " LIMIT ?,?";

        Manufactory.filmsForUserPage.clear();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, startNumber);
            preparedStatement.setInt(3, pageSize);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FilmForUserPage film = new FilmForUserPage();

                film.url = resultSet.getString(1);
                film.name = resultSet.getString(2);
                film.rating = (float) resultSet.getInt(3) / resultSet.getInt(4);
                film.mark = resultSet.getInt(5);

                Manufactory.filmsForUserPage.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void getFilmsCategoriesByUser(int id) {
        for (Map.Entry entry : Manufactory.categories.entrySet()) {
            entry.setValue(0);
        }

        String sqlQuery = "SELECT category FROM films WHERE id IN " +
                "(SELECT film_id FROM user_marks WHERE user_id = ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String str = resultSet.getString("category");
                String[] words = str.split(";");
                for (String w : words) {
                    Manufactory.categories.put(w, Manufactory.categories.get(w) + 1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getMarkByUserAndFilm(int user_id, int film_id) {
        String sqlQuery = "SELECT mark FROM user_marks WHERE user_id = ? AND film_id = ?";

        int mark = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, film_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mark = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mark;
    }

    public static Film getFilm(String url) {
        Manufactory.film = null;

        String sqlQuery = "SELECT * FROM films WHERE url = ?";

        Film film = new Film();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, url);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                film.id = resultSet.getInt("id");
                film.url = resultSet.getString("url");
                film.name = resultSet.getString("name");
                film.year = resultSet.getString("year").split("-")[0];
                film.country = resultSet.getString("country");
                film.category = resultSet.getString("category");
                film.directors = resultSet.getString("directors");
                film.mainActors = resultSet.getString("main_actors");
                film.description = resultSet.getString("description");
                film.englishName = resultSet.getString("english_name");
                film.trailerUrl = resultSet.getString("trailer_url");
                film.imageUrl = resultSet.getString("image_url");
                film.tagline = resultSet.getString("tagline");
                film.budget = resultSet.getInt("budget");
                film.fees = resultSet.getInt("fees");
                film.premiereDate = resultSet.getString("premiere_date");
                film.ageLimit = resultSet.getInt("age_limit");
                film.duration = resultSet.getString("duration");
                film.screenwriters = resultSet.getString("screenwriters");
                film.producers = resultSet.getString("producers");
                film.operators = resultSet.getString("operators");
                film.composers = resultSet.getString("composers");
                film.artists = resultSet.getString("artists");
                film.editors = resultSet.getString("editors");
                film.otherActors = resultSet.getString("other_actors");

                film.rating = (float) resultSet.getInt("rating") / resultSet.getInt("marks_count");
                film.marksCount = resultSet.getInt("marks_count");

                Manufactory.film = film;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return film;
    }

    public static String getFilmUrl(int id) {
        Manufactory.film = null;

        String sqlQuery = "SELECT url FROM films WHERE id = ?";

        String url = "not found";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                url = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static void getFilmsForTableOrderBy(int pageSize, int pageNumber, String orderBy, boolean desc) {
        Manufactory.filmsForTable.clear();

        int startNumber = pageSize * (pageNumber - 1);

        String sqlQuery = "SELECT name, url, rating, marks_count FROM films ORDER BY " + orderBy;
        if (desc) sqlQuery += " DESC";
        sqlQuery += " LIMIT ?,?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, startNumber);
            preparedStatement.setInt(2, pageSize);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FilmForTable film = new FilmForTable();

                film.url = resultSet.getString("url");
                film.name = resultSet.getString("name");
                film.rating = (float) resultSet.getInt("rating") / resultSet.getInt("marks_count");
                film.marksCount = resultSet.getInt("marks_count");

                Manufactory.filmsForTable.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getFilmsForSearchPanelByTag(String tag) {
        Manufactory.filmsForSearchPanels.clear();

        tag = tag + '%';
        String sqlQuery = "SELECT url, name, image_url FROM films WHERE name LIKE ? LIMIT 3";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, tag);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FilmForSearchPanel film = new FilmForSearchPanel();

                film.url = resultSet.getString("url");
                film.name = resultSet.getString("name");
                film.imageUrl = resultSet.getString("image_url");

                Manufactory.filmsForSearchPanels.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getFilmsCount() {
        String sqlQuery = "SELECT COUNT(*) FROM films";
        int count = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getMarksCount(int userId) {
        String sqlQuery = "SELECT COUNT(*) FROM user_marks WHERE user_id = ?";
        int count = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getCommentAuthorById(int id) {
        String sqlQuery = "SELECT user_id FROM comments1 WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getComment2AuthorById(int id) {
        String sqlQuery = "SELECT user_id FROM comments2 WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void addFilmReq(String user_login, String name, String year) {
        String sqlQuery = "INSERT INTO film_reqs ?, ?, ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user_login);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, year);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getFilmsReq() {
        Manufactory.filmReqs.clear();

        String sqlQuery = "SELECT * FROM film_reqs";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FilmReq filmReq = new FilmReq();

                filmReq.id = resultSet.getInt("id");
                filmReq.userLogin = resultSet.getString("user_login");
                filmReq.name = resultSet.getString("name");
                filmReq.year = resultSet.getString("year");

                Manufactory.filmReqs.add(filmReq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeFilmReq(int id) {
        String sqlQuery = "DELETE FROM film_reqs WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        keys.add("url"); values.add("brat102132");
        keys.add("name"); values.add("Брат");
        keys.add("year"); values.add("1995");
        keys.add("country"); values.add("Расия");
        keys.add("category"); values.add("Стрелякла,Ebashilovka");
        keys.add("directors"); values.add("Дядя Богдан");
        keys.add("main_actors"); values.add("Егор крид");
        keys.add("description"); values.add("Брат за брата так за основу взято");
        keys.add("fees"); values.add("20320320");*/

        //addUser("admin", "admin@mail.ru", "admin");

        //getFilm("brat");
        //System.out.println(Manufactory.film.toJSONString());

        deleteMark(1, 37, 0);
    }
}
