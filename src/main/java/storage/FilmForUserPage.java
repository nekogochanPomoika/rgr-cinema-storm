package storage;

public class FilmForUserPage {
    public String name;
    public String url;
    public float rating;
    public int mark;

    public String toJSONString() {
        return "{" +
                "\"url\":\"" + url + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"rating\":\"" + rating + '\"' +
                ", \"mark\":\"" + mark + '\"' +
                '}';
    }
}
