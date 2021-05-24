package server.service;

import server.dao.ChatRoomDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateChatRoomService {

    private static CreateChatRoomService instance = null;

    public static CreateChatRoomService getInstance() {
        if (instance == null)
            instance = new CreateChatRoomService();
        return instance;
    }

    public int createChatRoom(String userId, String roomType, String friendId) {
        Connection connection = null;
       try {
           connection = ConnectionProvider.getInstance().getConnection();
           connection.setAutoCommit(false);
           int roomId = ChatRoomDao.getInstance().insertChatRoom(connection, userId, roomType, friendId);
           connection.commit();
           return roomId;
       } catch (SQLException exception) {
           JdbcUtil.getInstance().rollback(connection);
           throw new RuntimeException(exception);
       }
    }

}
