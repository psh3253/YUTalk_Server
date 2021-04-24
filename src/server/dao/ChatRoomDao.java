package server.dao;

import server.jdbc.JdbcUtil;
import server.model.ChatRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatRoomDao {

    private static ChatRoomDao instance = null;

    public static ChatRoomDao getInstance() {
        if (instance == null)
            instance = new ChatRoomDao();
        return instance;
    }

    public ArrayList<Integer> selectChatRoomList(Connection connection, String userId) throws SQLException {
        ArrayList<Integer> chatRoomList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM chat_member WHERE user_id = ?");
            statement.setString(1, userId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                chatRoomList.add(resultSet.getInt("room_id"));
            }
            return chatRoomList;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
    }

    public ChatRoom selectChatRoom(Connection connection, int roomId, String userId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ChatRoom chatRoom = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM chat_room WHERE room_id = ?");
            statement.setInt(1, roomId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                chatRoom = new ChatRoom(
                        roomId,
                        resultSet.getString("room_type"),
                        resultSet.getString("name"),
                        resultSet.getInt("headcount"),
                        resultSet.getString("last_message"),
                        toDate(resultSet.getTimestamp("create_time")),
                        toDate(resultSet.getTimestamp("last_time")),
                        0
                        // 수정해야됨
                );
            }
            return chatRoom;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
    }

    private Date toDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }
}
