package storage;

import java.util.ArrayList;
import java.util.HashMap;

public class Manufactory {
    public static ArrayList<User> users = new ArrayList<>();
    public static Film film = new Film();
    public static ArrayList<FirstLevelComment> comments1 = new ArrayList<>();
    public static ArrayList<SecondLevelComment> comments2 = new ArrayList<>();
    public static ArrayList<UserMarks> marks = new ArrayList<>();
    public static ArrayList<FilmForSearchPanel> filmsForSearchPanels = new ArrayList<>();
    public static ArrayList<FilmForTable> filmsForTable = new ArrayList<>();

    public static HashMap<String, Integer> categories = new HashMap<>();

    static {
        categories.put("Биография", 0);
        categories.put("Боевик", 0);
        categories.put("Вестерн", 0);
        categories.put("Военный", 0);
        categories.put("Детектив", 0);
        categories.put("Документальный", 0);
        categories.put("Драма", 0);
        categories.put("История", 0);
        categories.put("Комедия", 0);
        categories.put("Короткометражка", 0);
        categories.put("Криминал", 0);
        categories.put("Мелодрма", 0);
        categories.put("Музыка", 0);
        categories.put("Мультфильм", 0);
        categories.put("Мюзикл", 0);
        categories.put("Приключения", 0);
        categories.put("Семеный", 0);
        categories.put("Сериал", 0);
        categories.put("Спорт", 0);
        categories.put("Триллер", 0);
        categories.put("Ужасы", 0);
        categories.put("Фантастика", 0);
        categories.put("Фильм-нуар", 0);
        categories.put("Фэнтези", 0);
    }
}
