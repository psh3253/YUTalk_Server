package server.service;

import server.dao.FriendDao;
import server.dao.MemberDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.Member;

import java.sql.Connection;
import java.sql.SQLException;

public class AddFriendService {

    public static AddFriendService instance = null;

    private static final int ADD_FRIEND_SUCCESS = 0;

    private static final int NOT_EXIST_ID = 1;

    public static AddFriendService getInstance() {
        if (instance == null)
            instance = new AddFriendService();
        return instance;
    }

    public String[] addFriend(String userId, String friendId) {
        Connection connection = null;
        String[] response = new String[5];
        response[0] = "addFriendResponse";
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            Member member = MemberDao.getInstance().selectMember(connection, friendId);
            if (member == null) {
                JdbcUtil.getInstance().rollback(connection);
                response[1] = Integer.toString(NOT_EXIST_ID);
                return response;
            }
            FriendDao.getInstance().insertFriend(connection, userId, friendId);

            response[1] = Integer.toString(ADD_FRIEND_SUCCESS);
            response[2] = member.getUserId();
            response[3] = member.getName();
            response[4] = member.getStatusMessage();
            connection.commit();
            return response;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
