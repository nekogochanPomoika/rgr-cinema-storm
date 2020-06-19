package storage;

public class SecondLevelComment {
    public int id;
    public int firstLevelCommentId;
    public String user;
    public String data;

    public String toJSONString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ",\"firstLevelCommentId\":\"" + firstLevelCommentId + '\"' +
                ",\"user\":\"" + user + '\"' +
                ",\"data\":\"" + data + '\"' +
                '}';
    }
}
