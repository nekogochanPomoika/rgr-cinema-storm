package storage;

import java.util.ArrayList;

public class FirstLevelComment {
    public int id;
    public String user;
    public int filmId;
    public String header;
    public int rating;
    public String data;
    public ArrayList<SecondLevelComment> secondLevelComments;

    public String toJSONString() {
        StringBuilder comments2 = new StringBuilder();

        try {
            comments2.append('[');
            comments2.append(secondLevelComments.get(0).toJSONString());

            for (int i = 1; i < secondLevelComments.size(); i++) {
                comments2.append(',');
                comments2.append(secondLevelComments.get(i).toJSONString());
            }

            comments2.append(']');
        } catch (IndexOutOfBoundsException e) {
            comments2 = new StringBuilder("\"no result\"");
        }

        return "{" +
                "\"id\":\""     + id     + '\"' +
                ",\"user\":\"" + user + '\"' +
                ",\"filmId\":\"" + filmId + '\"' +
                ",\"header\":\"" + header + '\"' +
                ",\"rating\":\"" + rating + '\"' +
                ",\"data\":\""   + data   + '\"' +
                ",\"comments2\":" + comments2.toString() +
                '}';
    }
}
