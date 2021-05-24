package server.dao;

import server.jdbc.JdbcUtil;
import server.model.ChatRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoomDao {

    private static ChatRoomDao instance = null;

    public static ChatRoomDao getInstance() {
        if (instance == null)
            instance = new ChatRoomDao();
        return instance;
    }

    public int insertChatRoom(Connection connection, String userId, String roomType, String friendId) throws SQLException{
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if (roomType.equals("private")) {
                String userName = MemberDao.getInstance().selectMember(connection, userId).getName();
                String friendName = MemberDao.getInstance().selectMember(connection, friendId).getName();
                statement = connection.prepareStatement("INSERT INTO chat_room(room_id, room_type, name, headcount, last_message, create_time, last_time) VALUES (null, 'private', ?, 2, null, now(), now())");
                statement.setString(1, userName + "," + friendName);
                statement.executeUpdate();
                statement = connection.prepareStatement("SELECT LAST_INSERT_ID()");
                resultSet = statement.executeQuery();
                resultSet.next();
                int roomId = resultSet.getInt(1);
                statement = connection.prepareStatement("INSERT INTO chat_member(user_id, room_id, invite_time, last_read_message_id) VALUES (?, ?, now(), 0)");
                statement.setString(1, userId);
                statement.setInt(2, roomId);
                statement.executeUpdate();
                statement = connection.prepareStatement("INSERT INTO chat_member(user_id, room_id, invite_time, last_read_message_id) VALUES (?, ?, now(), 0)");
                statement.setString(1, friendId);
                statement.setInt(2, roomId);
                statement.executeUpdate();
                return roomId;
            } else if (roomType.equals("public")) {
                return 0;
            }
            return -1;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
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

    public ChatRoom selectChatRoom(Connection connection, int roomId) throws SQLException {
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

    public ArrayList<String> selectChatRoomMemberList(Connection connection, int roomId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> memberList = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM chat_member WHERE room_id = ?");
            statement.setInt(1, roomId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                memberList.add(resultSet.getString("user_id"));
            }
            return memberList;
        } finally {
            JdbcUtil.getInstance().close(resultSet);
            JdbcUtil.getInstance().close(statement);
        }
    }

    public void deleteMemberFromChatRoom(Connection connection, String userId, int roomId) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM chat_member WHERE user_id = ? AND room_id = ?");
            statement.setString(1, userId);
            statement.setInt(2, roomId);
            statement.executeUpdate();
            statement = connection.prepareStatement("UPDATE chat_room SET headcount = headcount - 1 WHERE room_id = ?");
            statement.setInt(1, roomId);
            statement.executeUpdate();
        } finally {
            JdbcUtil.getInstance().close(statement);
        }
    }

    public void deleteChatRoom(Connection connection, int roomId) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("DELETE FROM chat_member WHERE room_id = ?");
            statement.setInt(1, roomId);
            statement.executeUpdate();
            statement = connection.prepareStatement("DELETE FROM chat_room WHERE room_id = ?");
            statement.setInt(1, roomId);
            statement.executeUpdate();
        } finally {
            JdbcUtil.getInstance().close(statement);
        }
    }

    private Date toDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }
}
