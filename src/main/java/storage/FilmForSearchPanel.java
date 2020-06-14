package storage;

public class FilmForSearchPanel {
    public String url;
    public String name;
    public String imageUrl;

    public String toJSONString() {
        return "{" +
                "\"url\":\"" + url + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"imageUrl\":\"" + imageUrl + '\"' +
                '}';
    }
}
