package server.service;

import server.dao.ChatRoomDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.ChatRoom;
import server.model.ClientList;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class LeaveChatRoomService {

    private static LeaveChatRoomService instance = null;

    public static LeaveChatRoomService getInstance() {
        if (instance == null)
            instance = new LeaveChatRoomService();
        return instance;
    }

    public void leaveChatRoom(String userId, int roomId) {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            ChatRoom chatRoom = ChatRoomDao.getInstance().selectChatRoom(connection, roomId);
            if(chatRoom.getHeadcount() == 1)
            {
                ChatRoomDao.getInstance().deleteMemberFromChatRoom(connection, userId, roomId);
                ChatRoomDao.getInstance().deleteChatRoom(connection, roomId);
            } else {
                ChatRoomDao.getInstance().deleteMemberFromChatRoom(connection, userId, roomId);
            }
            connection.commit();
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
