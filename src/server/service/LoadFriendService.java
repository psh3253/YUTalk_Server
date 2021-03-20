package server.service;

import server.dao.FriendDao;
import server.dao.MemberDao;
import server.jdbc.ConnectionProvider;
import server.jdbc.JdbcUtil;
import server.model.Member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadFriendService {

    private static LoadFriendService instance = null;

    public static LoadFriendService getInstance() {
        if (instance == null)
            instance = new LoadFriendService();
        return instance;
    }

    public HashMap<String, HashMap<String, String[]>> loadFriend(String userId) {
        Connection connection = null;
        HashMap<String, HashMap<String, String[]>> responseObject = new HashMap<>();
        HashMap<String, String[]> friendData = new HashMap<>();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);

            ArrayList<String> friendList = FriendDao.getInstance().selectFriend(connection, userId);
            for (int i = 0; i < friendList.size(); i++) {
                Member member = MemberDao.getInstance().selectByUserId(connection, friendList.get(i));
                friendData.put(friendList.get(i), new String[]{member.getUserId(), member.getName(), member.getStatusMessage()});
            }

            responseObject.put("loadFriendResponse", friendData);
            connection.commit();
            return responseObject;
        } catch (SQLException exception) {
            JdbcUtil.getInstance().rollback(connection);
            throw new RuntimeException(exception);
        }
    }
}
