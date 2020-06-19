package handler;

import storage.Manufactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class FilmUpdater {
    private StringBuilder sqlQuery = new StringBuilder();

    //built sqlQuery and fieldsForStatement
    void buildSqlQuery(ArrayList<String> keys, ArrayList<String> values) {
        sqlQuery.append("UPDATE films SET ");

        sqlQuery.append(keys.get(0));
        sqlQuery.append(" = ");
        sqlQuery.append(values.get(0));

        for (int i = 1; i < keys.size(); i++) {
            sqlQuery.append(", ");
            sqlQuery.append(keys.get(0));
            sqlQuery.append(" = ");
            sqlQuery.append(values.get(0));
        }

        sqlQuery.append(" WHERE id = ?");
    }

    void buildStatement(PreparedStatement statement) throws SQLException {
        statement.setInt(1, Manufactory.updatedFilmId);
    }

    public String getSqlQuery() {
        return sqlQuery.toString();
    }
}
