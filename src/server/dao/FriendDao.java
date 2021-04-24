package server.dao;

import server.jdbc.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendDao {

    private static FriendDao instance = null;

    public static FriendDao getInstance() {
        if(instance == null)
            instance = new FriendDao();
        return instance;
    }

    public void insertFriend(Connection connection, String userId, String friendId) throws SQLException{
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO friend VALUES (?, ?)");
            statement.setString(1, userId);
            statement.setString(2, friendId);
            statement.executeUpdate();
        } finally {
            JdbcUtil.getInstance().close(statement);
        }
    }

    public ArrayList<String> selectFriendList(Connection connection, String userId) throws SQLException{
        ArrayList<String> friendUserIdList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM friend WHERE user_id = ?");
            statement.setString(1, userId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                friendUserIdList.add(resultSet.getString("friend_id"));
            }
            return friendUserIdList;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
    }
}
