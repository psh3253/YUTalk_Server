package server.service;

import server.dao.ChatRoomDao;
import server.dao.MemberDao;
import server.dao.MessageDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.ChatRoom;
import server.model.Member;
import server.model.Message;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class InviteMemberService {

    private static InviteMemberService instance = null;

    public static InviteMemberService getInstance() {
        if(instance == null)
            instance = new InviteMemberService();
        return instance;
    }

    public void inviteMember(String userId, int roomId, ArrayList<String> memberUserIdList) {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            StringBuilder infoMessage = new StringBuilder(MemberDao.getInstance().selectMember(connection, userId).getName() + "님이");
            for (String s : memberUserIdList) {
                ChatRoomDao.getInstance().insertMemberToChatRoom(connection, s, roomId);
                infoMessage.append(" ").append(MemberDao.getInstance().selectMember(connection, s).getName());
            }
            infoMessage.append("님을 초대하였습니다.");

            MessageDao.getInstance().insertMessage(connection, "system", roomId, "info", String.valueOf(infoMessage));
            connection.commit();
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
