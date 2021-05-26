package server.service;

import server.dao.ChatRoomDao;
import server.dao.MemberDao;
import server.dao.MessageDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.Member;
import server.model.Message;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadChatRoomMemberService {

    private static LoadChatRoomMemberService instance = null;

    public static LoadChatRoomMemberService getInstance() {

        if (instance == null)
            instance = new LoadChatRoomMemberService();
        return instance;
    }

    public HashMap<String, ArrayList<String[]>> loadChatRoomMember(int roomId) {
        Connection connection = null;
        HashMap<String, ArrayList<String[]>> responseObject = new HashMap<>();
        ArrayList<String[]> chatRoomMemberData = new ArrayList<>();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<String> chatRoomMemberList = ChatRoomDao.getInstance().selectChatRoomMemberList(connection, roomId);
            for (int i = 0; i < chatRoomMemberList.size(); i++) {
                Member member = MemberDao.getInstance().selectMember(connection, chatRoomMemberList.get(i));
                chatRoomMemberData.add(new String[]{member.getUserId(), member.getName()});
            }
            responseObject.put("loadChatRoomMemberResponse", chatRoomMemberData);
            connection.commit();
            return responseObject;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
