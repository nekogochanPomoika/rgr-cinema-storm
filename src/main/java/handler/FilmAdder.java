package handler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class FilmAdder {
    private HashMap<Integer, String> fieldsForStatement = new HashMap<>();
    private StringBuilder sqlQuery = new StringBuilder();

    //built sqlQuery and fieldsForStatement
    void buildSqlQuery(ArrayList<String> keys, ArrayList<String> values) {
        sqlQuery.append("INSERT INTO films (");

        sqlQuery.append(keys.get(0));
        fieldsForStatement.put(1, values.get(0));

        for (int i = 1; i < keys.size(); i++) {
            sqlQuery.append(", ");
            sqlQuery.append(keys.get(i));

            fieldsForStatement.put(i + 1, values.get(i));
        }

        sqlQuery.append(") VALUES (?");

        for (int i = 1; i < keys.size(); i++) {
            sqlQuery.append(", ?");
        }
        sqlQuery.append(")");
    }

    void buildStatement(PreparedStatement statement) throws SQLException {
        for (Map.Entry<Integer, String> entry : fieldsForStatement.entrySet()) {
            statement.setString(entry.getKey(), entry.getValue());
        }
    }

    public String getSqlQuery() {
        return sqlQuery.toString();
    }
}
