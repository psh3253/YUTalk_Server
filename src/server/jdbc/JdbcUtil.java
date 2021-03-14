package server.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtil {

    private static JdbcUtil instance = null;

    public static JdbcUtil getInstance() {
        if (instance == null)
            instance = new JdbcUtil();
        return instance;
    }

    public void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException exception) {

            }
        }
    }

    public void close(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException exception) {

            }
        }
    }

    public void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException exception) {

            }
        }
    }
}
