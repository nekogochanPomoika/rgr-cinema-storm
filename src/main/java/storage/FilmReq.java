package storage;

public class FilmReq {
    public int id;
    public String userLogin;
    public String name;
    public String year;

    public String toJSONString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ", \"userLogin\":\"" + userLogin + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"year\":\"" + year + '\"' +
                '}';
    }
}
