package server.dao;

import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDao {

    private static MemberDao instance = null;

    public static MemberDao getInstance() {
        if(instance == null)
            instance = new MemberDao();
        return instance;
    }

    public Member selectByUserId(Connection connection, String userId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Member member = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM member WHERE user_id = ?");
            statement.setString(1, userId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                member = new Member(
                        resultSet.getString("user_id"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("status_message"),
                        resultSet.getDate("register_time"),
                        resultSet.getDate("last_login_time")
                );
            }
            return member;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
    }

    public void insert(Connection connection, String userId, String password, String name) throws SQLException{
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO member(user_id, password, name, register_time, last_login_time) VALUES (?, ?, ?, now(), now())");
            statement.setString(1, userId);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.executeUpdate();
        } finally {
            JdbcUtil.getInstance().close(statement);
        }
    }
}