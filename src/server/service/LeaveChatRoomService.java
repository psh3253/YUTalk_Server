package server.service;

import server.dao.ChatRoomDao;
import server.dao.MemberDao;
import server.dao.MessageDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.ChatRoom;

import java.sql.Connection;
import java.sql.SQLException;

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
            if(chatRoom.getRoomType().equals("private")) {
                ChatRoomDao.getInstance().hideChatRoom(connection, userId, roomId);
            } else {
                ChatRoomDao.getInstance().deleteMemberFromChatRoom(connection, userId, roomId);
                MessageDao.getInstance().insertMessage(connection, "system", roomId, "info", MemberDao.getInstance().selectMember(connection, userId).getName() + "님이 퇴장하셨습니다.");
                if(chatRoom.getHeadcount() == 1)
                {
                    ChatRoomDao.getInstance().deleteChatRoom(connection, roomId);
                }
            }
            connection.commit();
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
