package storage;

public class FilmForTable {
    public String name;
    public String url;
    public float rating;
    public int marksCount;

    public String toJSONString() {
        return "{" +
                "\"url\":\"" + url + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"rating\":\"" + rating + '\"' +
                ", \"marksCount\":\"" + marksCount + '\"' +
                '}';
    }
}
