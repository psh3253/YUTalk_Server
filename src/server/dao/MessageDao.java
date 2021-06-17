package server.dao;

import server.jdbc.JdbcUtil;
import server.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class MessageDao {

    private static MessageDao instance = null;

    public static MessageDao getInstance() {
        if (instance == null)
            instance = new MessageDao();
        return instance;
    }

    public void insertMessage(Connection connection, String userId, int roomId, String messageType, String message) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO message(message_id, user_id, room_id, message_type, message, send_time) VALUES (null, ?, ?, ?, ?, now())");
            statement.setString(1, userId);
            statement.setInt(2, roomId);
            statement.setString(3, messageType);
            statement.setString(4, message);
            statement.executeUpdate();
            if (messageType.equals("text")) {
                statement = connection.prepareStatement("UPDATE chat_room SET last_message = ?, last_time = now() WHERE room_id = ?");
                statement.setString(1, message);
                statement.setInt(2, roomId);
                statement.executeUpdate();
            }
            if (ChatRoomDao.getInstance().selectChatRoom(connection, roomId).getRoomType().equals("private")) {
                ArrayList<String> memberList = ChatRoomDao.getInstance().selectChatRoomMemberList(connection, roomId);
                for (String s : memberList) {
                    ChatRoomDao.getInstance().unHideChatRoom(connection, s, roomId);
                }
            }
        } finally {
            JdbcUtil.getInstance().close(statement);
        }
    }

    public ArrayList<Integer> selectMessageList(Connection connection, int roomId) throws SQLException {
        ArrayList<Integer> messageList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM message WHERE room_id = ?");
            statement.setInt(1, roomId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                messageList.add(resultSet.getInt("message_id"));
            }
            return messageList;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
    }

    public Message selectMessage(Connection connection, int messageId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Message message = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM message WHERE message_id = ?");
            statement.setInt(1, messageId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                message = new Message(
                        messageId,
                        resultSet.getString("user_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getString("message_type"),
                        resultSet.getString("message"),
                        toDate(resultSet.getTimestamp("send_time"))
                );
            }
            return message;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
    }

    private Date toDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }
}
